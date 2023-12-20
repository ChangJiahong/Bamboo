package cn.changjiahong.bamboo.base.sp

import cn.changjiahong.bamboo.base.utils.genericType
import cn.changjiahong.bamboo.common.StringPairs
import java.lang.reflect.Type


/**
 *
 * @author ChangJiahong
 * @date 2022/5/18
 */
interface SP {

    fun getBoolean(key: String, defValue: Boolean): Boolean

    fun getLong(key: String, defValue: Long): Long

    fun getString(key: String, defValue: String): String

    fun getFloat(key: String, defValue: Float): Float

    fun getInt(key: String, defValue: Int): Int

    fun getStringSet(key: String, defValue: Set<String>): Set<String>

    fun <T:Any> get(key: String,type: Type):T?

    fun put(key: String, value: Set<String>): Boolean

    fun put(key: String, value: Any): Boolean

    fun <T> putJson(key: String,value: T):Boolean

    fun put(makeStringPairs: StringPairs<Any>.() -> Unit): Boolean

    fun contains(key: String): Boolean

    /**
     * key转换
     */
    fun getKey(key: String):String
}

inline fun <reified T:Any> SP.get(key: String):T? = get(key, genericType<T>())