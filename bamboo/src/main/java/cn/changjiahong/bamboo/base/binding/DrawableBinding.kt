package cn.changjiahong.bamboo.base.binding

import android.view.View
import androidx.databinding.BindingAdapter
import cn.changjiahong.bamboo.base.utils.DrawableBuilder
import com.hzsoft.lib.log.KLog

/**
 *
 * @author ChangJiahong
 * @date 2022/6/27
 */
object DrawableBinding {
    /**
     * conner
     * conners
     * color
     * colors,
     * storkeColor,
     * storkeWidth
     *
     * conner=9;color=#f24957
     */
    @BindingAdapter(value = ["drawableBuilderString"], requireAll = true)
    @JvmStatic
    fun drawableBuilder(v: View, drawableBuilderString: String) {
        KLog.d("",drawableBuilderString)
        val params = drawableBuilderString.split(";")
        val map = mutableMapOf<String, String>()
        params.forEach {
            val p = it.split("=")
            if (p.size == 2) {
                map[p[0]] = p[1]
            }
        }
        DrawableBuilder(map).into(v)
    }

}