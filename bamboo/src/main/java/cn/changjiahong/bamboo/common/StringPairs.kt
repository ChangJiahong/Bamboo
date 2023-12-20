package cn.changjiahong.bamboo.common

/**
 * 参数对
 * @author ChangJiahong
 * @date 2019/8/23
 */
open class StringPairs<T> : Pairs<String, T>()
open class IntPairs<T> : Pairs<Int, T>()

open class Pairs<K, T> {
    private var _pairs: MutableMap<K, T> = HashMap()
    val pairs: Map<K, T> = _pairs

    open operator fun K.minus(value: T) {
        _pairs[this] = value
    }

    fun putAll(from: Map<out K, T>) {
        _pairs.putAll(from)
    }

    fun put(key: K, value: T) {
        _pairs[key] = value
    }

    fun containsKey(key: K)= _pairs.containsKey(key)

    fun remove(key: K) = _pairs.remove(key)

    operator fun get(key: K) = _pairs[key]

    operator fun set(name: K, value: T) {
        _pairs[name] = value
    }

}