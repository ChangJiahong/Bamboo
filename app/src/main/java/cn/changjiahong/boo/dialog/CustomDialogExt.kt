package cn.changjiahong.boo.dialog

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import cn.changjiahong.bamboo.base.dialog.mListDialog
import cn.changjiahong.bamboo.base.service.BamServiceManager
import cn.changjiahong.bamboo.base.utils.visibility
import cn.changjiahong.bamboo.base.widget.toast.toast
import cn.changjiahong.bamboo.databinding.BaseDialogListItemBinding
import cn.changjiahong.boo.R
import cn.changjiahong.boo.BR
import cn.changjiahong.boo.app.bt.BtService
import cn.changjiahong.boo.app.bt.IBtService
import cn.changjiahong.boo.databinding.DialogDeviceItemBinding
import cn.changjiahong.boo.databinding.NewDeviceLayoutBinding
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.hzsoft.lib.log.KLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.UUID

/**
 *
 * @author ChangJiahong
 * @date 2023/12/26
 */

@SuppressLint("MissingPermission")
fun Context.newDeviceDialog(bluetoothDevice: BluetoothDevice) {
    MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
        val listBinding = DataBindingUtil.bind<NewDeviceLayoutBinding>(
            LayoutInflater.from(context).inflate(
                R.layout.new_device_layout, null
            )
        )!!
        cornerRadius(27f)
        customView(view = listBinding.root, noVerticalPadding = true, dialogWrapContent = true)

        listBinding.title.text = bluetoothDevice.name
        listBinding.positive.setOnClickListener {
            toast(bluetoothDevice.name)
            BamServiceManager.getService<IBtService>()?.connect(bluetoothDevice.address)

        }

    }
}

@SuppressLint("MissingPermission")
fun Context.devicesListDialog(
    myItems: ArrayList<String>,
    onClick: (item: String, position: Int) -> Unit = { _, _ -> }
) {
    mListDialog<DialogDeviceItemBinding, String>(
        myItems,
        R.layout.dialog_device_item,
        BR.item, { binding, position, item ->
                binding.checked.visibility(
                    item == (BamServiceManager.getService<IBtService>()?.getConnectedDevice()?.name
                        ?: "")
                )
        }, onClick
    )
}