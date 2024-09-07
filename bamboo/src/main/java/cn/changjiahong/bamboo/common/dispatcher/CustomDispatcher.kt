package cn.changjiahong.bamboo.common.dispatcher

import cn.changjiahong.bamboo.common.StringPairs

/**
 *
 * @author ChangJiahong
 * @date 2022/7/24
 */

abstract class CustomDispatcher<N> {


    private val dispatchMap =
        HashMap<Class<out N>, ArrayList<ObserverNotify<*, N>>>()

    private val typeMap: StringPairs<Class<out N>> = StringPairs()

    open operator fun String.minus(value: Class<out N>) {
        typeMap.put(this, value)
    }

    fun <T : N> registerNotification(
        clazz: Class<T>,
        notification: ObserverNotify<T, N>
    ) {
        if (!dispatchMap.containsKey(clazz)) {
            dispatchMap[clazz] = ArrayList()
        }
        if (!dispatchMap[clazz]!!.contains(notification)) {
            dispatchMap[clazz]!!.add(notification)
        }
    }

    fun <T : N> unRegisterNotification(
        clazz: Class<T>,
        notification: ObserverNotify<T, N>
    ) {
        if (dispatchMap.containsKey(clazz)) {
            if (dispatchMap[clazz]!!.contains(notification)) {
                dispatchMap[clazz]!!.remove(notification)
            }
        }
    }

    inline fun <reified T : N> registerNotification(notification: ObserverNotify<T, N>) {
        registerNotification(T::class.java, notification)
    }

    inline fun <reified T : N> registerNotification(crossinline notification: (T) -> Unit) {
        registerNotification(object : ObserverNotify<T, N> {
            override fun onEvent(t: T) {
                notification(t)
            }
        })
    }

    inline fun <reified T : N> unRegisterNotification(notification: ObserverNotify<T, N>) {
        unRegisterNotification(T::class.java, notification)
    }

    inline fun <reified T : N> notification(
        clazz: Class<out T>,
        notification: N?
    ) {
        val notif: T = if (notification != null) {
            notification as T
        } else {
            new(clazz)
        }
        if (containsDispatcher(clazz)) {
            getDispatcher(clazz)!!.forEach {
                val observerNotify = it as ObserverNotify<T, N>
                observerNotify.onEvent(notif)
            }
        }

    }

    fun getDispatcher(clazz: Class<out N>) = dispatchMap[clazz]

    fun containsDispatcher(clazz: Class<out N>) = dispatchMap.containsKey(clazz)

    fun getTypeClazz(type: String) = typeMap[type]

    fun <T : Any> new(clz: Class<T>): T {
        val mCreate = clz.getDeclaredConstructor()
        mCreate.isAccessible = true
        return mCreate.newInstance()
    }

}
