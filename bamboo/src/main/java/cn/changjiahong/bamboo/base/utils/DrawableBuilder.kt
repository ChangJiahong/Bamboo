package cn.changjiahong.bamboo.base.utils

import android.graphics.drawable.GradientDrawable
import android.view.View

/**
 * 快速生成背景
 */
class DrawableBuilder {
    var conner: Float? = null
    private var conners: Array<Float>? = null
    var color: Int? = null
    var colors: Array<Int>? = null
    var orientation: GradientDrawable.Orientation? = null
    var storkeColor: Int? = null
    var storkeWidth: Int? = null

    constructor()
    constructor(params: Map<String, String>) {
        params.forEach { (key, value) ->
            when (key) {
                "conner" -> conner = value.toFloatOrNull()?.dp
                "conners" -> conners(
                    value.split(",").transform { it.toFloatOrNull()?.dp }.toTypedArray())
                "color" -> color = value.color
                "colors" -> colors = value.split(",").transform { it.color }.toTypedArray()
                "storkeColor" -> storkeColor = value.color
                "storkeWidth" -> storkeWidth = value.toIntOrNull()?.dp
            }
        }
    }

    constructor(invoke: DrawableBuilder.() -> Unit) {
        invoke()
    }

    constructor(view: View, invoke: DrawableBuilder.() -> Unit) {
        invoke()
        into(view)
    }

    /* ！！！ 只有view的size完全一致才可以复用GradientDrawable ！！！*/
    fun build(): GradientDrawable {
        val gd = GradientDrawable()
        if (conner != null) {
            gd.cornerRadius = conner!!.toFloat()
        }
        if (conners != null) {
            gd.cornerRadii = conners!!.toFloatArray()
        }
        if (color != null) {
            gd.setColor(color!!)
        }
        if (colors != null) {
            gd.colors = colors!!.toIntArray()
        }
        if (orientation != null) {
            gd.orientation = orientation
        }
        if (storkeWidth != null && storkeColor != null) {
            gd.setStroke(storkeWidth!!, storkeColor!!)
        }
        return gd
    }

    fun into(view: View) {
        val gd = GradientDrawable()
        if (conner != null) {
            gd.cornerRadius = conner!!.toFloat()
        }
        if (conners != null) {
            gd.cornerRadii = conners!!.toFloatArray()
        }
        if (color != null) {
            gd.setColor(color!!)
        }
        if (colors != null) {
            gd.colors = colors!!.toIntArray()
        }
        if (orientation != null) {
            gd.orientation = orientation
        }
        if (storkeWidth != null && storkeColor != null) {
            gd.setStroke(storkeWidth!!, storkeColor!!)
        }
        view.background = gd
    }

    fun conner(conner: Float): DrawableBuilder {
        this.conner = conner
        return this
    }

    fun conners(conners: Array<Float>): DrawableBuilder {
        this.conners = conners
        return this
    }

    /**
     * 四个角的圆角，单位依然是px
     * @param c1  px
     * @param c2  px
     * @param c3  px
     * @param c4  px
     * @return
     */
    fun conners(c1: Float, c2: Float, c3: Float, c4: Float): DrawableBuilder {
        conners = arrayOf(c1, c1, c2, c2, c3, c3, c4, c4)
        return this
    }

    fun color(color: Int): DrawableBuilder {
        this.color = color
        return this
    }

    fun colors(colors: Array<Int>): DrawableBuilder {
        this.colors = colors
        return this
    }

    fun storkeColor(storkeColor: Int): DrawableBuilder {
        this.storkeColor = storkeColor
        return this
    }

    fun storkeWidth(storkeWidth: Int): DrawableBuilder {
        this.storkeWidth = storkeWidth
        return this
    }

    fun orientation(orientation: GradientDrawable.Orientation): DrawableBuilder {
        this.orientation = orientation
        return this
    }

    fun gravity(gravity: Gravity): DrawableBuilder {
        if (gravity == Gravity.START_END) {
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
        } else if (gravity == Gravity.END_START) {
            orientation = GradientDrawable.Orientation.RIGHT_LEFT
        }
        return this
    }

    enum class Gravity {
        START_END, END_START
    }
}