package cn.changjiahong.bamboo.base.viewmodel

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.changjiahong.bamboo.common.event_bus.Event
import cn.changjiahong.bamboo.base.router.export.IExportLoginService
import cn.changjiahong.bamboo.base.router.provider
import cn.changjiahong.bamboo.domain.ToastDomain
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import org.greenrobot.eventbus.EventBus

/**
 *
 * @author ChangJiahong
 * @date 2022/6/24
 */
open class BaseViewModel : ViewModel() {

    /**
     * 登录接口
     */
    val loginService = provider<IExportLoginService>()

    private val _toast: Channel<ToastDomain> = Channel()
    val toast = _toast.receiveAsFlow()

    private val _finish: Channel<Int> = Channel()
    val finish = _finish.receiveAsFlow()


    fun finish() {
        viewModelScope.launch {
            _finish.send(1)
        }
    }

    fun toast(resId: Int = 0, msg: String = "", duration: Int) {
        viewModelScope.launch {
            _toast.send(ToastDomain(resId, msg, duration))
        }
    }

    fun toast(msg: String) {
        toast(0, msg, 1500)
    }

    fun toast(@IdRes resId: Int) {
        toast(resId, "", 1500)
    }

    // TODO:默认处理异常操作
//    fun handleExp(
//        catch: Throwable,
//        vararg exclude: Int,
//        customAction: (catch: ApiExp) -> Unit = {}
//    ) {
//        when (catch) {
//            is HttpException -> {
//                toast(catch.localizedMessage ?: "")
//            }
//            is JsonParseException -> {
//                toast(catch.localizedMessage ?: "")
//            }
//            is ApiExp -> {
//                if (!exclude.contains(catch.code)) {
//
//                    if (catch.code == 410003) {
//                        //登录过期
//                        toast("${catch.msg}")
//                        exportService.logout()
//                    } else if (catch.code == 600021) {
//                        customAction(catch)
//                    } else {
//                        toast("${catch.msg}")
//                        customAction(catch)
//                    }
//                } else {
//                    customAction(catch)
//                }
//            }
//            else -> {
//                catch.printStackTrace()
//            }
//        }
//    }

    fun postEvent(event: Event) {
        EventBus.getDefault().post(event)
    }

    fun countDownCoroutines(
        total: Int,
        scope: CoroutineScope,
        onTick: (Int) -> Unit,
        onStart: (() -> Unit) = {},
        onFinish: (() -> Unit) = {},
    ): Job {
        return flow {
            for (i in total downTo 0) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.Main)
            .onStart { onStart.invoke() }
            .catch {
                println("catch exception")
            }
            .onCompletion { cash ->
                if (cash == null) {
                    onFinish.invoke()
                }
            }
            .onEach { onTick.invoke(it) }
            .launchIn(scope)
    }
}
