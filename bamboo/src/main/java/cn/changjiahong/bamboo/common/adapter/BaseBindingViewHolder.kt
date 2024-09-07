package cn.changjiahong.bamboo.common.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author ChangJiahong
 * @date 2022/7/4
 */
open class BaseBindingViewHolder<VB : ViewDataBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root)