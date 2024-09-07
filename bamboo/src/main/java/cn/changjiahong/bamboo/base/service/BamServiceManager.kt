package cn.changjiahong.bamboo.base.service

import kotlin.reflect.KClass
import kotlin.reflect.cast

/**
 * Service 管理类
 * @author ChangJiahong
 * @date 2023/12/31
 */
object BamServiceManager {
    private val _serviceMap = HashMap<KClass<out BaseService>, BaseService>()
    val serviceMap: Map<KClass<out BaseService>, BaseService>
        get() = _serviceMap

    fun registerService(baseService: BaseService) {
        _serviceMap[baseService::class] = baseService
    }

    fun unRegisterService(baseService: BaseService) {
        if (_serviceMap.containsKey(baseService::class)) {
            _serviceMap.remove(baseService::class)
        }
    }

    fun <T : BaseService> getService(kClass: KClass<T>): T? {
        if (_serviceMap.containsKey(kClass)) {
            return kClass.cast(_serviceMap[kClass])
        }
        return null
    }

    inline fun <reified T : Any> getService(): T? {
        serviceMap.forEach { (t, u) ->
            if (u is T) {
                return u
            }
        }

        return null
    }

}


