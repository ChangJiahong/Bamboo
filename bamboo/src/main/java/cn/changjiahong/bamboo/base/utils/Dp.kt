package cn.changjiahong.bamboo.base.utils

import cn.changjiahong.bamboo.base.app.BambooApplication


inline val Int.dp: Int get() = dip2px(this.toFloat()).toInt()
inline val Float.dp: Float get() = dip2px(this)
inline val Double.dp: Double get() = dip2px(this.toFloat()).toDouble()

fun dip2px(dipValue: Float): Float {
    val scale = BambooApplication.context.resources.displayMetrics.density
    return (dipValue * scale + 0.5f)
}


inline val Int.sp: Int get() = sp2px(this.toFloat()).toInt()
inline val Float.sp: Float get() = dip2px(this)
inline val Double.sp: Double get() = sp2px(this.toFloat()).toDouble()

fun sp2px(sp:Float):Float{
    val scale = BambooApplication.context.resources.displayMetrics.scaledDensity
    return (sp * scale + 0.5f)
}
