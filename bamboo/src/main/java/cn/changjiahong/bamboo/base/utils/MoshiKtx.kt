package cn.changjiahong.bamboo.base.utils

import cn.changjiahong.bamboo.base.utils.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *
 * @author ChangJiahong
 * @date 2023/3/9
 */

object MoshiUtil {

    abstract class MoshiTypeReference<T> // 自定义的类，用来包装泛型

    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory()).build()


    inline fun <reified T> toJson(src: T, indent: String = ""): String {
        return toJson(src, getGenericType<T>(), indent)
    }

    fun <T> toJson(src: T, type: Type, indent: String = ""): String {
        return moshi.adapter<T>(type).indent(indent).toJson(src)
    }

    inline fun <reified T> fromJson(jsonStr: String): T? {
        try {
            val jsonAdapter = moshi.adapter<T>(getGenericType<T>())
            return jsonAdapter.fromJson(jsonStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return fromJson(jsonStr, T::class.java)
    }

    fun <T> fromJson(json: String, type: Type): T? {
        return try {
            moshi.adapter<T>(type).fromJson(json)
        } catch (e: Exception) {
            return null
        }
    }


    inline fun <reified T> getGenericType(): Type {
        return object :
            MoshiTypeReference<T>() {}::class.java
            .genericSuperclass
            .let { it as ParameterizedType }
            .actualTypeArguments
            .first()

    }


}


inline fun <reified T> T.toJson(indent: String = ""): String {
    return MoshiUtil.toJson(this, indent)
}

fun <T> T.toJson(type: Type, indent: String = ""): String {
    return MoshiUtil.toJson(this, type, indent)
}


inline fun <reified T> String.fromJson(): T? {
    return MoshiUtil.fromJson(this)
}

fun <T : Any> String.fromJson(type: Type): T? {
    return MoshiUtil.fromJson(this, type)
}

inline fun <reified T> genericType(): Type {
    return MoshiUtil.getGenericType<T>()
}