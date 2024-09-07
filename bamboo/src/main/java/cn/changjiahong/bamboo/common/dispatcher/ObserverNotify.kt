package cn.changjiahong.bamboo.common.dispatcher

/**
 *
 * @author ChangJiahong
 * @date 2022/7/23
 */
interface ObserverNotify<T:N,N> {
    fun onEvent(t:T)
}