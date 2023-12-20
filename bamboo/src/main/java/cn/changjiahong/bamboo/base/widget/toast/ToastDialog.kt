package cn.changjiahong.bamboo.base.widget.toast

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.graphics.toColorInt
import cn.changjiahong.bamboo.R
import cn.changjiahong.bamboo.base.utils.DrawableBuilder
import cn.changjiahong.bamboo.base.utils.dp

/**
 * Created by Administrator on 13-7-29. 弹出提示框
 */
class ToastDialog(val context: Context,val handler: Handler) {
    private var toast: ToastCompat? = null

    /**
     * 显示Toast
     *
     * @param tvString
     * @param kLastTime
     */
    fun toastShow(tvString: String?, kLastTime: Int) {
        handler.post {
            try {
                if (TextUtils.isEmpty(tvString)) {
                    return@post
                }
                val layout: View =
                    LayoutInflater.from(context).inflate(R.layout.base_toast_default, null)
                DrawableBuilder(layout.findViewById(R.id.toast_content_main)) {
                    conner = 50f.dp
                    color = "#a6000000".toColorInt()
                }
                val text: TextView = layout.findViewById<TextView>(R.id.toast_content)
                text.text = tvString
                if (toast != null) {
                    toast!!.cancel()
                }
                toast = ToastCompat.makeText(context)
                toast?.setGravity(Gravity.CENTER, 0, 0)
                toast?.duration = kLastTime
                toast?.setView(layout)
                toast?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}