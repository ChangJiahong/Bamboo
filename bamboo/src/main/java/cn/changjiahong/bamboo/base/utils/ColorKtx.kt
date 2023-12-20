package cn.changjiahong.bamboo.base.utils

import android.graphics.Color

/**
 *
 * @author ChangJiahong
 * @date 2023/3/7
 */

inline val String.color: Int?
    get() = try {
        Color.parseColor(this)
    } catch (e: IllegalArgumentException) {
        null
    }