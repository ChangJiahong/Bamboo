package cn.changjiahong.bamboo.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.changjiahong.bamboo.base.fragment.BaseViewPageFragment

/**
 *
 * @author ChangJiahong
 * @date 2022/6/27
 */
open class ViewPager2Adapter (
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: List<BaseViewPageFragment<*, *>>
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    private val TAG = this::class.java.canonicalName
    private val added = arrayListOf<Long>()

    override fun createFragment(position: Int): Fragment {
        added.add(getItemId(position))
        fragments[position].position = position
        return fragments[position]
    }

    override fun containsItem(itemId: Long): Boolean {
        return added.contains(itemId)
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

}