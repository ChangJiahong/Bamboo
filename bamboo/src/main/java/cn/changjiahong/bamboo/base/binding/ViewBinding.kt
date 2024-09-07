package cn.changjiahong.bamboo.base.binding

import android.view.View
import androidx.databinding.BindingAdapter
import cn.changjiahong.bamboo.base.utils.visibility

/**
 *
 * @author ChangJiahong
 * @date 2022/6/30
 */

object ViewBinding {
    @BindingAdapter(value = ["isSelected"], requireAll = true)
    @JvmStatic
    fun isSelected(v: View, isSelected: Boolean) {
        v.isSelected = isSelected
    }

    @BindingAdapter(value = ["visibility"], requireAll = true)
    @JvmStatic
    fun visibility(v: View, visibility: Boolean) {
        v.visibility(visibility)
    }


}