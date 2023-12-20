package cn.changjiahong.bamboo.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import cn.changjiahong.bamboo.base.viewmodel.BaseViewModel
import org.koin.android.ext.android.getKoinScope
import org.koin.androidx.viewmodel.ViewModelParameter
import org.koin.androidx.viewmodel.pickFactory
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import java.lang.reflect.ParameterizedType

/**
 * 自动注入ViewModel
 * 如需自定义ViewModel注入参数，请直接继承{@link com.copallive.lib_base.activity.BaseActivity}
 * @author ChangJiahong
 * @date 2022/6/24
 */
open class BaseViewModelActivity<VB : ViewDataBinding, VM : BaseViewModel>(@LayoutRes contentLayoutId: Int) :
    BaseActivity<VB>(contentLayoutId) {

    protected val mViewModel: VM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver(mViewModel)
    }

    @OptIn(KoinInternalApi::class)
   private fun viewModel(
        qualifier: Qualifier? = null,
        owner: ViewModelStoreOwner = this,
        parameters: ParametersDefinition? = null
    ): Lazy<VM> {
        val scope = getKoinScope()
        return viewModels {
            val vbClass =
                (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VM>>()
            val viewModel = vbClass[1]
            val hasState = false
            val viewModelParameters = ViewModelParameter(
                clazz = viewModel.kotlin,
                qualifier = qualifier,
                parameters = parameters,
                state = null,
                viewModelStoreOwner = owner,
                registryOwner = if (hasState) owner as? SavedStateRegistryOwner else null
            )
            scope.pickFactory(viewModelParameters)
        }
    }

    private fun viewModels(
        factoryProducer: (() -> ViewModelProvider.Factory)? = null
    ): Lazy<VM> {
        val factoryPromise = factoryProducer ?: {
            defaultViewModelProviderFactory
        }
            val vbClass =
                (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VM>>()
            val viewModel = vbClass[1]
            return ViewModelLazy(viewModel.kotlin, { viewModelStore }, factoryPromise)
    }
}