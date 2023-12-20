package cn.changjiahong.bamboo.base.utils

import android.net.Uri
import android.net.Uri.decode

/**
 *
 * @author ChangJiahong
 * @date 2022/6/24
 */

fun Map<String, String>.toQueryString(): String =
    this.map { "${it.key}=${it.value}" }.joinToString("&")


fun Uri.getParms(): Map<String, String> {
    val map = mutableMapOf<String, String>()
    val query = encodedQuery ?: return map

    val params = query.split("&")
    for (i in params.indices) {
        val p = params[i].split("=")
        map[p[0]] = if (p.size == 2) decode(p[1]) else ""
    }
    return map
}