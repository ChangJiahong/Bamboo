package cn.changjiahong.bamboo.base.fragment

import androidx.annotation.LayoutRes
import androidx.annotation.Px
import androidx.databinding.ViewDataBinding
import androidx.viewpager2.widget.ViewPager2.ScrollState
import cn.changjiahong.bamboo.base.viewmodel.BaseViewModel

/**
 *
 * @author ChangJiahong
 * @date 2022/6/27
 */
open class BaseViewPageFragment<VB : ViewDataBinding, VM : BaseViewModel>(@LayoutRes contentLayoutId: Int) :
    BaseViewModelFragment<VB, VM>(contentLayoutId) {

    var position = 0;


    open fun onPageScrolled(
        position: Int, positionOffset: Float,
        @Px positionOffsetPixels: Int
    ) {
    }

    open fun onPageSelected(position: Int) {}


    open fun onPageScrollStateChanged(@ScrollState state: Int) {}

}