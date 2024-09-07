package cn.changjiahong.boo.app.main.fragments.devices

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.View
import cn.changjiahong.bamboo.base.bluetooth.BTDevicesManager
import cn.changjiahong.bamboo.base.bluetooth.BTDevicesManager.TOY_PIXEL_SCREEN
import cn.changjiahong.bamboo.base.dialog.stringListDialog
import cn.changjiahong.bamboo.base.fragment.BaseViewPageFragment
import cn.changjiahong.bamboo.base.widget.toast.toast
import cn.changjiahong.boo.R
import cn.changjiahong.bamboo.base.bluetooth.BtScanBroadcastReceiver
import cn.changjiahong.bamboo.base.bluetooth.checkDeviceClass
import cn.changjiahong.bamboo.base.service.BamServiceManager
import cn.changjiahong.bamboo.base.utils.transform
import cn.changjiahong.bamboo.common.event_bus.Event
import cn.changjiahong.boo.app.bt.IBtService
import cn.changjiahong.boo.app.bt.msg.BtEvent
import cn.changjiahong.boo.app.main.fragments.devices.vm.DevicesViewModel
import cn.changjiahong.boo.databinding.FragmentDevicesBinding
import cn.changjiahong.boo.dialog.devicesListDialog


/**
 * 设备控制
 */
class DevicesFragment :
    BaseViewPageFragment<FragmentDevicesBinding, DevicesViewModel>(R.layout.fragment_devices) {
    lateinit var receiver: BtScanBroadcastReceiver

    companion object {


        val INSTANCE by lazy { DevicesFragment() }

    }

    val bondedDevices = ArrayList<BluetoothDevice>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        defaultBar().statusBarView(mBinding.statusView).init()

    }

    @SuppressLint("MissingPermission")
    override fun onViewInit() {
        super.onViewInit()


        requireContext().apply {
            mBinding.apply {
                BTDevicesManager.bluetoothAdapter.bondedDevices.forEach {
                    if (it.checkDeviceClass(TOY_PIXEL_SCREEN)) {
                        bondedDevices.add(it)
                    }
                }

                devices.text = "连接设备"

                val deviceNames = bondedDevices.transform { it.name }

                devices.setOnClickListener {
                    devicesListDialog(arrayListOf("添加设备").apply { addAll(deviceNames)}) { item, position ->
                        toast(item)
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMainEventBus(event: Event) {
        super.onMainEventBus(event)
        when (event.type) {
            BtEvent.CONNECT_SUCCESS -> {
                val bluetoothDevice = event.data as BluetoothDevice
                mBinding.devices.text = bluetoothDevice.name
            }

            BtEvent.CONNECTING -> {
                mBinding.devices.text = "连接中..."
            }

            BtEvent.DIS_CONNECT -> {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(receiver)
    }
}