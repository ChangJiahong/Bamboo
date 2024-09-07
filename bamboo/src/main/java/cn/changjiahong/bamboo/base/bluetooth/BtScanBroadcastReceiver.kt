package cn.changjiahong.bamboo.base.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import cn.changjiahong.bamboo.base.manager.ActivityStackManager
import cn.changjiahong.bamboo.base.utils.bluePermissions
import com.hzsoft.lib.log.KLog

/**
 * 蓝牙扫描广播
 * @author ChangJiahong
 * @date 2023/12/24
 */
class BtScanBroadcastReceiver : BroadcastReceiver() {

    companion object{
        public var registerFlag = false
    }

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    BTDevicesManager.dispatchBluetoothDevice(device)
                }
            }
        }
    }
}