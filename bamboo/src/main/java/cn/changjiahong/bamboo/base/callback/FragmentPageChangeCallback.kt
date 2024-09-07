package cn.changjiahong.bamboo.base.callback

import androidx.viewpager2.widget.ViewPager2
import cn.changjiahong.bamboo.base.fragment.BaseViewPageFragment
import kotlin.math.roundToInt

/**
 *
 * @author ChangJiahong
 * @date 2023/12/20
 */
class FragmentPageChangeCallback(private val fragments: List<BaseViewPageFragment<*, *>>) :
    ViewPager2.OnPageChangeCallback() {
    var prePosition = -1
    private var oldOffset = 0f

    override fun onPageScrollStateChanged(state: Int) {
        super.onPageScrollStateChanged(state)
        fragments.forEach { it.onPageScrollStateChanged(state) }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)

        val offset = positionOffset - oldOffset
        oldOffset = positionOffset

        val roundedPosition = (position + positionOffset).roundToInt()
        if (offset > 0) {
            // 右滑

        } else if (offset < 0) {
            // 左滑

        }

        fragments.forEach { it.onPageScrolled(position, positionOffset, positionOffsetPixels) }

    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        fragments.forEach { it.onPageSelected(position) }
    }

}