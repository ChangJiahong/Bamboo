package cn.changjiahong.bamboo.base.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.view.isVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 *
 * @author ChangJiahong
 * @date 2022/6/27
 */


//inline fun <reified T : Activity> Context.startActivity() {
//    startActivity(Intent(this, T::class.java))
//}

inline fun <reified T : Any> Context.intentFor(vararg params: Pair<String, Any?>): Intent =
    AnkoInternals.createIntent(this, T::class.java, params)

inline fun <reified T : Service> Context.stopService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStopService(this, T::class.java, params)

inline fun <reified T : Service> Context.startService(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartService(this, T::class.java, params)

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, Any?>) =
    AnkoInternals.internalStartActivity(this, T::class.java, params)

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.visibility(visibility: Boolean) {
    this.visibility = if (visibility) View.VISIBLE else View.GONE
}

fun View.getReverseVisible(): Int = if (isVisible) View.GONE else View.VISIBLE


inline fun <reified T : Any> new(): T {
    return new(T::class.java)
}

fun <T : Any> new(clz: Class<T>): T {
    val mCreate = clz.getDeclaredConstructor()
    mCreate.isAccessible = true
    return mCreate.newInstance()
}


fun View.setOnOnceClickListener(INTERVAL_TIME:Int=200,click: (v: View) -> Unit) {
    val lastclicktime: Long = (getTag(147) as Long?) ?: 0L
    this.setOnClickListener {
        if (System.currentTimeMillis() - lastclicktime > INTERVAL_TIME) {
            click(it)
            setTag(147, System.currentTimeMillis())
        }
    }
}