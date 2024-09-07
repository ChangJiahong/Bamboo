package cn.changjiahong.boo.app.bt

import android.bluetooth.BluetoothDevice
import cn.changjiahong.bamboo.base.service.IService
import cn.changjiahong.boo.app.bt.msg.BtMsg


/**
 *
 * @author ChangJiahong
 * @date 2024/1/1
 */
interface IBtService {

    fun connect(address: String)

    fun send()

    fun disConnect()

    fun getConnectedDevice(): BluetoothDevice?


}
