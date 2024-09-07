package cn.changjiahong.bamboo.base.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import androidx.core.content.ContextCompat.getSystemService
import cn.changjiahong.bamboo.base.app.BambooApplication
import cn.changjiahong.bamboo.base.manager.ActivityStackManager
import cn.changjiahong.bamboo.base.utils.bluePermissions
import java.util.UUID

/**
 * 蓝牙工具类
 * 扫描，扫描回调通知，
 * @author ChangJiahong
 * @date 2023/12/24
 */
@SuppressLint("MissingPermission")
object BTDevicesManager {

    /**
     * 设备识别码
     */
    const val TOY_PIXEL_SCREEN = 0x0818
    const val BLUETOOTH_SERIAL_PORT = "00001101-0000-1000-8000-00805F9B34FB"


    val receiver = BtScanBroadcastReceiver()
    private var btScanResults: (BluetoothDevice) -> Unit = {}

    private val bluetoothManager =
        BambooApplication.context.getSystemService(BluetoothManager::class.java) as BluetoothManager

    val bluetoothAdapter = bluetoothManager.adapter

    fun registerReceiver(context: Context) {
        if (BtScanBroadcastReceiver.registerFlag) {
            return
        }
        context.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
    }

    fun unregisterReceiver(context: Context) {
        if (!BtScanBroadcastReceiver.registerFlag) {
            return
        }
        context.unregisterReceiver(receiver)
    }

    /**
     * 扫描
     */
    fun startScan(res: (BluetoothDevice) -> Unit) {
        if (bluetoothAdapter == null) return
        val context = ActivityStackManager.instance.currentActivity() ?: return
        context.bluePermissions { _, _ ->
            if (bluetoothAdapter.isDiscovering) {
                bluetoothAdapter.cancelDiscovery()
            }
            btScanResults = res
            bluetoothAdapter.startDiscovery()
        }
    }

    fun stopScan() {
        btScanResults = {}
        bluetoothAdapter.cancelDiscovery()
    }

    fun dispatchBluetoothDevice(device: BluetoothDevice) {
        btScanResults(device)
    }

    fun connect(address: String): BluetoothSocket {
        val bluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
        if (bluetoothDevice.bondState != BluetoothDevice.BOND_BONDED) {
            bluetoothDevice.createBond()
        }
        val uuid: String = BLUETOOTH_SERIAL_PORT
        val socket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        bluetoothDevice.uuids
        socket.connect()
        return socket
    }


}

@SuppressLint("MissingPermission")
fun BluetoothDevice.checkDeviceClass(cla: Int): Boolean {
    return bluetoothClass.deviceClass == cla
}