package cn.changjiahong.bamboo.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 *
 * @author ChangJiahong
 * @date 2022/7/4
 */
open class SimpleBindingAdapter<VB : ViewDataBinding, T>(
    context: Context,
    @LayoutRes val layoutId: Int,
    val variableId: Int,
    data: ArrayList<T> = ArrayList<T>()
) :
    BaseBindingAdapter<VB, T>(context, data) {

    override fun onCreateBindingView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): VB {
        val db: VB = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)
        onCreatedBindingView(db)
        return db
    }

    open fun onCreatedBindingView(binding: VB) {

    }

    override fun onBindingData(binding: VB, position: Int, item: T) {
        binding.setVariable(variableId, item)
        binding.executePendingBindings()
    }
}