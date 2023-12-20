package cn.changjiahong.bamboo.base.router

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.core.net.toUri
import cn.changjiahong.bamboo.base.utils.getParms
import cn.changjiahong.bamboo.common.StringPairs
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.hzsoft.lib.log.KLog
import java.io.Serializable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * 全局导航
 * @author ChangJiahong
 * @date 2022/6/24
 */

/**
 * http://
 * app别名://页面类型/页面地址?参数
 * supei://activity/jump_contact?username=asd&pwd=asdsasda
 */

const val HTTP_SCHEME = "http"
const val HTTPS_SCHEME = "https"

fun nav(url: String, makeStringPairs: StringPairs<Any>.() -> Unit = {}) {
    val uri = url.toUri()
    when (uri.scheme) {
        HTTP_SCHEME, HTTPS_SCHEME -> navHttp(uri, makeStringPairs)
        else -> nav(uri, makeStringPairs)
    }
}

fun navHttp(uri: Uri, makeStringPairs: StringPairs<Any>.() -> Unit = {}) {

}

fun nav(uri: Uri, makeStringPairs: StringPairs<Any>.() -> Unit = {}) {
    try {
        val aRouter = ARouter.getInstance().build(uri.path)
        val params = StringPairs<Any>()
        params.makeStringPairs()
        params.putAll(uri.getParms())
        fillIntentArguments(aRouter, params.pairs)
        aRouter.navigation()
        KLog.d("Router", "导航到$uri")
    } catch (ex: Exception) {
        KLog.d("Router", ex.localizedMessage ?: "")
    }
}

fun navAndClear(url: String, makeStringPairs: StringPairs<Any>.() -> Unit = {}) {
    val uri = url.toUri()
    val aRouter = ARouter.getInstance()
        .build(url.toUri().path)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

    val params = StringPairs<Any>()
    params.makeStringPairs()
    params.putAll(uri.getParms())
    fillIntentArguments(aRouter, params.pairs)
    aRouter.navigation()
}

inline fun <reified T : IProvider> provider() = Provider<T>(T::class)

class Provider<T : IProvider>(val clazz: KClass<T>) : ReadOnlyProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return ARouter.getInstance().navigation<T>(clazz.java)
    }
}


private fun fillIntentArguments(aRouter: Postcard, params: Map<String, Any>) {
    params.forEach { (key, value) ->
        when (value) {
            is Int -> aRouter.withInt(key, value)
            is Long -> aRouter.withLong(key, value)
            is String -> aRouter.withString(key, value)
            is CharSequence -> aRouter.withCharSequence(key, value)
            is Float -> aRouter.withFloat(key, value)
            is Double -> aRouter.withDouble(key, value)
            is Char -> aRouter.withChar(key, value)
            is Short -> aRouter.withShort(key, value)
            is Boolean -> aRouter.withBoolean(key, value)
            is Serializable -> aRouter.withSerializable(key, value)
            is Bundle -> aRouter.withBundle(key, value)
            is Parcelable -> aRouter.withParcelable(key, value)
//            is Array<*> -> when {
//                value.isArrayOf<CharSequence>() -> aRouter.withCharSequenceArray(
//                    key,
//                    value as Array<out CharSequence>?
//                )
////                value.isArrayOf<String>() -> aRouter.withStringArrayList(key, ArrayList<String>((value as Array<String>?)?.toList()))
//                value.isArrayOf<Parcelable>() -> aRouter.withParcelableArray(
//                    key,
//                    value as Array<out Parcelable>?
//                )
//                else -> throw AnkoException("Intent extra ${key} has wrong type ${value.javaClass.name}")
//            }
//            is IntArray -> aRouter.withIntegerArrayList(key, value.toMutableList())
//            is LongArray -> aRouter.putExtra(key, value)
//            is FloatArray -> aRouter.putExtra(key, value)
//            is DoubleArray -> aRouter.putExtra(key, value)
//            is CharArray -> aRouter.withCharArray(key, value)
//            is ShortArray -> aRouter.withShortArray(key, value)
//            is BooleanArray -> aRouter.putExtra(key, value)
            else -> throw AnkoException("Intent extra $key has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
}

open class AnkoException(message: String = "") : RuntimeException(message)
