package cn.changjiahong.bamboo.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cn.changjiahong.bamboo.common.event_bus.Event
import cn.changjiahong.bamboo.base.viewmodel.BaseViewModel
import cn.changjiahong.bamboo.base.widget.toast.toast
import cn.changjiahong.bamboo.common.IntPairs
import com.gyf.immersionbar.ImmersionBar
import com.hi.dhl.binding.databind.FragmentDataBinding
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

/**
 *
 * @author ChangJiahong
 * @date 2022/6/27
 */
open class BaseFragment<VB : ViewDataBinding>(@LayoutRes val resId: Int) : Fragment(resId){

    val mBinding: VB by databind()

    private var lastView: View? = null

    /**
     * 保证View 只初始化一次
     */
    var viewCreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (lastView == null) {
            lastView = super.onCreateView(inflater, container, savedInstanceState)
            return lastView
        } else {
            return lastView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!viewCreated) {
            viewCreated = true
            mBinding
            onViewInit()
        }
    }

    open fun onViewInit() {

    }

    fun defaultBar():ImmersionBar=
        ImmersionBar.with(this).statusBarDarkFont(true).transparentStatusBar()
            .navigationBarColor("#ffffff").navigationBarDarkIcon(true)


    fun initObserver(viewModel: BaseViewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.toast.collect {
                    if (it.msg.isNotEmpty()) {
                        requireActivity().toast(it.msg, it.duration)
                    } else {
                        requireActivity().toast(getString(it.resId), it.duration)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.finish.collect {
                    requireActivity().finish()
                }
            }
        }

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

    fun databind() = run {
        val vbClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
        val viewBinding = vbClass[0]
        FragmentDataBinding<VB>(viewBinding, this)
    }

}