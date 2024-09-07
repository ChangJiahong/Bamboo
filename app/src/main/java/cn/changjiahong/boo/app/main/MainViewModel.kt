package cn.changjiahong.boo.app.main

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.lifecycle.viewModelScope
import cn.changjiahong.bamboo.base.bluetooth.BTDevicesManager
import cn.changjiahong.bamboo.base.bluetooth.BTDevicesManager.TOY_PIXEL_SCREEN
import cn.changjiahong.bamboo.base.service.BamServiceManager
import cn.changjiahong.bamboo.base.utils.collectIn
import cn.changjiahong.bamboo.base.utils.toJson
import cn.changjiahong.bamboo.base.viewmodel.BaseViewModel
import cn.changjiahong.boo.Test
import cn.changjiahong.boo.app.bt.BtService
import cn.changjiahong.boo.app.bt.IBtService
import cn.changjiahong.boo.app.bt.msg.BtMsgDispatcher
import cn.changjiahong.boo.app.bt.msg.DefaultBtMsg
import cn.changjiahong.boo.app.bt.msg.DeviceInfoBtMsg
import cn.changjiahong.boo.repository.ILoginRepository
import com.hzsoft.lib.log.KLog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 *
 * @author ChangJiahong
 * @date 2023/3/16
 */

@KoinViewModel
class MainViewModel(val iLoginRepository: ILoginRepository) : BaseViewModel() {

    private val _findNewDevice = MutableSharedFlow<BluetoothDevice>()
    val findNewDevice = _findNewDevice.asSharedFlow()

    fun test() {

        iLoginRepository.test(Test(231, "hhh")).catch {
            toast(it.message ?: "")
        }.collectIn(viewModelScope) {
            toast(it.toJson())
        }
    }


    @SuppressLint("MissingPermission")
    fun btScanDevices() {

        BTDevicesManager.startScan {
            if (it.bluetoothClass.deviceClass == TOY_PIXEL_SCREEN) {
                BTDevicesManager.stopScan()
                when (it.bondState) {
                    BluetoothDevice.BOND_NONE -> {
                        viewModelScope.launch {
                            _findNewDevice.emit(it)
                        }
                    }

                    BluetoothDevice.BOND_BONDED -> {
                        // 自动连接
                        BamServiceManager.getService<IBtService>()?.connect(it.address)
//                        BamServiceManager.getService<BtService>()?.connect(it.address)
                    }
                }
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }


}