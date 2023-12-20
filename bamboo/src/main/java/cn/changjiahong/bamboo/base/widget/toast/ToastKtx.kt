package cn.changjiahong.bamboo.base.widget.toast

import android.content.Context
import android.os.Handler
import android.os.Looper

/**
 *
 * @author ChangJiahong
 * @date 2022/6/30
 */

fun Context.toast(msg:String,kLastTime:Int){
    val result = ToastDialog(this, Handler(Looper.myLooper()?: Looper.getMainLooper()))
    result.toastShow(msg,kLastTime)
}

fun Context.longToast(msg: String){
    toast(msg,2000)
}

fun Context.toast(msg: String){
    toast(msg,1500)
}

fun Context.toast(res:Int){
    toast(getString(res))
}

fun appToast(msg:String){
//    BaseApplication.context.toast(msg)
}
fun appToast(res:Int){
//    BaseApplication.context.toast(res)
}