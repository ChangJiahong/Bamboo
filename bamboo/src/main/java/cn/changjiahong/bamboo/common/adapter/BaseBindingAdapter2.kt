package cn.changjiahong.bamboo.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @author ChangJiahong
 * @date 2022/7/4
 */
abstract class BaseBindingAdapter2<VB : ViewDataBinding, T>(context: Context,val data:ArrayList<T>) :
    RecyclerView.Adapter<BaseBindingViewHolder<VB>>() {

    private val layoutInflater = LayoutInflater.from(context)

    private var onClick:(View,T,Int)->Unit={_,_,_->}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<VB> {
        return BaseBindingViewHolder(onCreateBindingView(layoutInflater, parent, viewType))
    }

    abstract fun onCreateBindingView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): VB

    override fun onBindViewHolder(holder: BaseBindingViewHolder<VB>, position: Int) {

        val item = data[position]

        holder.itemView.setOnClickListener {
            onClick(it,item,position)
        }

        onBindingData(holder.binding, position, item)

    }

    abstract fun onBindingData(binding: VB, position: Int, item: T)

    override fun getItemCount(): Int = data.size


    fun setOnItemClickListener(onClick:(View,T,Int)->Unit){
        this.onClick = onClick
    }
}