package cn.changjiahong.bamboo.base.activity

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cn.changjiahong.bamboo.common.event_bus.Event
import cn.changjiahong.bamboo.base.lan.LanguageManager
import cn.changjiahong.bamboo.base.router.nav
import cn.changjiahong.bamboo.base.router.navAndClear
import cn.changjiahong.bamboo.base.viewmodel.BaseViewModel
import cn.changjiahong.bamboo.base.widget.toast.toast
import cn.changjiahong.bamboo.common.IntPairs
import com.gyf.immersionbar.ImmersionBar
import com.hi.dhl.binding.databind.ActivityDataBinding
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Locale

/**
 *
 * @author ChangJiahong
 * @date 2022/6/23
 */
open class BaseActivity<VB : ViewDataBinding>(@LayoutRes val resId: Int) : AppCompatActivity() {

    companion object{
        const val HOMEPAGE="/main/homepage"
    }

    val mBinding: VB by databind(resId)

    fun defaultBar() = ImmersionBar.with(this).statusBarDarkFont(true).transparentStatusBar()
        .navigationBarColor("#ffffff").navigationBarDarkIcon(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding
        defaultBar().init()

        EventBus.getDefault().register(this)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun initObserver(viewModel: BaseViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.finish.collect {
                    finish()
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.toast.collect {
                    if (it.msg.isNotEmpty()) {
                        toast(it.msg, it.duration)
                    } else {
                        if (it.resId != 0)
                            toast(getString(it.resId), it.duration)
                    }
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.run { LanguageManager.attachBaseContext(newBase) })
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LanguageManager.attachBaseContext(this);
    }


    fun bindingVariable(makeIntPairs: IntPairs<Any>.() -> Unit) {
        val paramStringPairs = IntPairs<Any>()
        paramStringPairs.makeIntPairs()
        paramStringPairs.pairs.forEach {
            mBinding.setVariable(it.key, it.value)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMainEventBus(event: Event) {

    }

    fun postEvent(event: Event) {
        EventBus.getDefault().post(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    fun applySystemFits() {
        ImmersionBar.with(this).applySystemFits(true).init()
    }

    fun databind(@LayoutRes resId: Int) =
        ActivityDataBinding<VB>(this, resId)


    fun changeLanguage(locale: Locale) {
        LanguageManager.switchLanguage(this, locale)
        navAndClear(HOMEPAGE)
    }
}