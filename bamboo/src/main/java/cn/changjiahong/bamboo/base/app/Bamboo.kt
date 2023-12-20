package cn.changjiahong.bamboo.base.app

import android.app.Application
import cn.changjiahong.bamboo.base.app.Bamboo.unRegister
import cn.changjiahong.bamboo.common.StringPairs
import org.koin.core.module.Module

/**
 *
 * @author ChangJiahong
 * @date 2023/3/7
 */
object Bamboo {

    private val _apps = StringPairs<Application>()
    private val _koins = StringPairs<Module>()

    val apps: Map<String, Application>
        get() = _apps.pairs

    val koins: Map<String, Module>
        get() = _koins.pairs

    val koinModules: List<Module>
        get() = koins.values.toList()


    fun init(app: Application) {

    }

    fun registerModuleKoin(name: String, koin: Module) {
        _koins.register(name, koin)
    }

    fun unRegisterModuleKoin(name: String) {
        _koins.unRegister(name)
    }

    fun registerModuleApp(name: String, module: Application): Boolean {
        if (_apps.containsKey(name)) {
            return false
        }
        _apps[name] = module
        return true
    }

    fun unRegisterModuleApp(name: String) {
        if (_apps.containsKey(name)) {
            _apps.remove(name)
        }
    }


    private fun <T> StringPairs<T>.register(name: String, value: T): Boolean {
        if (containsKey(name)) {
            return false
        }
        put(name, value)
        return true
    }

    private fun <T> StringPairs<T>.unRegister(name: String) {
        if (containsKey(name)) {
            remove(name)
        }
    }
}