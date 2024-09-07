package cn.changjiahong.bamboo.base.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import cn.changjiahong.bamboo.BR
import cn.changjiahong.bamboo.R
import cn.changjiahong.bamboo.common.adapter.SimpleBindingAdapter
import cn.changjiahong.bamboo.databinding.BaseDialogListBinding
import cn.changjiahong.bamboo.databinding.BaseDialogListItemBinding
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView

/**
 *
 * @author ChangJiahong
 * @date 2023/12/25
 */

fun <VB : ViewDataBinding, T> Context.mListDialog(
    items: List<T>, @LayoutRes viewRes: Int? = null,
    variableId: Int = 0,
    bindingView: (binding: VB, position: Int, item: T) -> Unit = {_,_,_->},
    onClick: (item: T, position: Int) -> Unit = { _, _ -> }
) {
    if (viewRes == null) return
    MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
        val listBinding = DataBindingUtil.bind<BaseDialogListBinding>(
            LayoutInflater.from(context).inflate(
                R.layout.base_dialog_list, null
            )
        )!!
        cornerRadius(16f)
        customView(view = listBinding.root, noVerticalPadding = true, dialogWrapContent = true)

        listBinding.apply {
            val adapter = object : SimpleBindingAdapter<VB, T>(
                this@mListDialog,
                viewRes,
                variableId
            ) {

                override fun onBindingData(binding: VB, position: Int, item: T) {
                    super.onBindingData(binding, position, item)
                    bindingView(binding, position, item)
                }
            }
            list.adapter = adapter
            adapter.setOnItemClickListener { _, s, i ->
                onClick(s, i)
            }
            list.layoutManager = LinearLayoutManager(
                list.context,
                LinearLayoutManager.VERTICAL, false
            )
            adapter.setAllData(items)
        }

    }
}

fun Context.stringListDialog(
    myItems: ArrayList<String>,
    onClick: (item: String, position: Int) -> Unit = { _, _ -> }
) {
    mListDialog<BaseDialogListItemBinding, String>(
        myItems,
        R.layout.base_dialog_list_item,
        BR.item,onClick= onClick
    )
}