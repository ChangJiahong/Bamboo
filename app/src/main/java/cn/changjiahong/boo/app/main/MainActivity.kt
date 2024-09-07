package cn.changjiahong.boo.app.main

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cn.changjiahong.bamboo.base.activity.BaseViewModelActivity
import cn.changjiahong.bamboo.base.fragment.BaseViewPageFragment
import cn.changjiahong.bamboo.base.utils.collectIn
import cn.changjiahong.bamboo.common.adapter.ViewPager2Adapter
import cn.changjiahong.boo.PApi
import cn.changjiahong.boo.R
import cn.changjiahong.boo.app.main.fragments.devices.DevicesFragment
import cn.changjiahong.boo.app.main.fragments.msn.MsnFragment
import cn.changjiahong.boo.app.main.fragments.square.SquareFragment
import cn.changjiahong.boo.app.main.fragments.user.UserFragment
import cn.changjiahong.boo.databinding.ActivityMainBinding
import cn.changjiahong.boo.dialog.newDeviceDialog
import cn.changjiahong.boo.repository.ILoginRepository
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject
import java.util.*

@Route(path = PApi.Main.home)
class MainActivity :
    BaseViewModelActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    val iLoginRepository: ILoginRepository by inject()

    private val fragments = ArrayList<BaseViewPageFragment<*, *>>()

    val icons = arrayOf(
        R.drawable.tabbar_0,
        R.drawable.tabbar_1,
        R.drawable.tabbar_2,
        R.drawable.tabbar_3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragments.apply {
            add(SquareFragment.INSTANCE)
            add(DevicesFragment.INSTANCE)
            add(MsnFragment.INSTANCE)
            add(UserFragment.INSTANCE)
        }

        mBinding.apply {
            viewPager.adapter = ViewPager2Adapter(supportFragmentManager, lifecycle, fragments)
            viewPager.offscreenPageLimit = fragments.size
            viewPager.isUserInputEnabled = true

            viewPager.currentItem = 1

            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                }

                override fun onTabReselected(tab: TabLayout.Tab) {

                }
            })

            TabLayoutMediator(
                tabLayout, viewPager
            ) { tab, position ->
                tab.setCustomView(R.layout.layout_tab_icon)
                tab.setIcon(icons[position])
            }.attach()
        }

        mViewModel.findNewDevice.collectIn(lifecycleScope) {
            newDeviceDialog(it)
        }

        mViewModel.btScanDevices()


    }

}