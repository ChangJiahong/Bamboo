package cn.changjiahong.boo.app.bt

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.IBinder
import cn.changjiahong.bamboo.base.bluetooth.BTDevicesManager
import cn.changjiahong.bamboo.base.service.BaseService
import cn.changjiahong.bamboo.base.utils.fromJson
import cn.changjiahong.bamboo.common.event_bus.Event
import cn.changjiahong.boo.app.bt.msg.BtEvent
import cn.changjiahong.boo.app.bt.msg.BtMsg
import cn.changjiahong.boo.app.bt.msg.BtMsgDispatcher
import cn.changjiahong.boo.app.bt.msg.DefaultBtMsg
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader


class BtService : BaseService(), IBtService {

    private val KEY_TYPE = "type"
    private val KEY_DATA = "data"

    lateinit var address: String

    /**
     * 当前连接设备
     */
    lateinit var bluetoothSocket: BluetoothSocket


    override fun onBind(intent: Intent): IBinder? {

        return null
    }


    /**
     * 接收消息，分发消息
     */
    private fun loopReader() {
        if (!::bluetoothSocket.isInitialized || !bluetoothSocket.isConnected) {
            return
        }
        val reader = InputStreamReader(bluetoothSocket.inputStream)
        val loop = serviceScope.launch(Dispatchers.IO) {
            while (true) {
                try {
                    if (!reader.ready())
                        continue
                    reader.forEachLine {
                        dispatcher(it)
                    }
                } catch (e: IOException) {
                    // 断开连接
                    reader.close()
                    // 被动断开
                    postEvent(Event(BtEvent.DIS_CONNECT, ""))
                    break
                }
            }
        }
    }

    private fun dispatcher(json: String) {
//        val msgType = json.fromJson<MsgType>()
        try {
            val obj = JSONObject(json)
            val type = obj.getString(KEY_TYPE)
            val data = obj.getString(KEY_DATA)
            val btMsg: BtMsg?
//            val clazz = Class.forName("cn.changjiahong.boo.app.bt.msg.${msgType.type}")
            val clazz = BtMsgDispatcher.getTypeClazz(type)
            clazz?.let {
                btMsg = data.fromJson(it)
                BtMsgDispatcher.notification(clazz, btMsg)
                return
            }
            BtMsgDispatcher.notification(
                DefaultBtMsg::class.java,
                DefaultBtMsg(data, type)
            )
            return

        } catch (e: JsonSyntaxException) {
            // default
            BtMsgDispatcher.notification(DefaultBtMsg::class.java, DefaultBtMsg(json))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun connect(address: String) {
        postEvent(Event(BtEvent.CONNECTING, ""))
        if (::bluetoothSocket.isInitialized && bluetoothSocket.isConnected) {
            disConnect()
        }
        this.address = address
        serviceScope.launch(Dispatchers.IO) {
            // 连接设备
            bluetoothSocket = BTDevicesManager.connect(address)
            postEvent(Event(BtEvent.CONNECT_SUCCESS, bluetoothSocket.remoteDevice))
            loopReader()
        }
    }

    override fun send() {

    }

    override fun disConnect() {
        if (bluetoothSocket.isConnected) {
            bluetoothSocket.close()
        }
        postEvent(Event(BtEvent.DIS_CONNECT, ""))
    }

    override fun getConnectedDevice(): BluetoothDevice? {
        if (::bluetoothSocket.isInitialized&&bluetoothSocket.isConnected) {
            return bluetoothSocket.remoteDevice
        }
        return null
    }
}