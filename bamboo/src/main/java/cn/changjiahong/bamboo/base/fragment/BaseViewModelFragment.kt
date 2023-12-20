package cn.changjiahong.bamboo.base.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import cn.changjiahong.bamboo.base.viewmodel.BaseViewModel
import org.koin.android.ext.android.getKoinScope
import org.koin.androidx.viewmodel.ViewModelStoreOwnerProducer
import org.koin.androidx.viewmodel.ext.android.getViewModelFactory
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import java.lang.reflect.ParameterizedType

/**
 *
 * @author ChangJiahong
 * @date 2022/6/27
 */
open class BaseViewModelFragment<VB : ViewDataBinding, VM : BaseViewModel>(@LayoutRes contentLayoutId: Int) :
    BaseFragment<VB>(contentLayoutId) {
    val mViewModel by viewModel()
    val mActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver(mViewModel)
    }

    @OptIn(KoinInternalApi::class)
    private fun viewModel(
        qualifier: Qualifier? = null,
        owner: ViewModelStoreOwnerProducer = { this },
        parameters: ParametersDefinition? = null
    ): Lazy<VM> {
        val scope = getKoinScope()
        return viewModels(ownerProducer = owner) {
            val vbClass =
                (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VM>>()
            val viewModel = vbClass[1]
            getViewModelFactory(
                owner(),
                viewModel.kotlin,
                qualifier,
                parameters,
                state = null,
                scope
            )
        }
    }

    private fun viewModels(
        ownerProducer: () -> ViewModelStoreOwner = { this },
        factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM> {
        val vbClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VM>>()
        val viewModel = vbClass[1]
        return createViewModelLazy(
            viewModel.kotlin, { ownerProducer().viewModelStore },
            factoryProducer ?: {
                (ownerProducer() as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
                    ?: defaultViewModelProviderFactory
            }
        )
    }
    @MainThread
    fun Fragment.activityViewModels(
        factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM> {
        val vbClass =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VM>>()
        val viewModel = vbClass[1]
        return createViewModelLazy(
            viewModel.kotlin, { requireActivity().viewModelStore },
            factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
        )
    }

}