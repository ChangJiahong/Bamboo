package cn.changjiahong.bamboo.base.utils

import android.net.Uri

/**
 *
 * @author ChangJiahong
 * @date 2023/3/13
 */
object Sign {

    val signToken = "112223"


    fun sign(mp: Map<String, Any>): String {
        val params = HashMap<String, String>()
        mp.forEach { (s, any) ->
            if (any is String) {
                params[s] = any
            } else {
                params[s] = any.toJson()
            }
        }
        val sortedMap = params.toSortedMap { o1, o2 -> o1.compareTo(o2) }
        val s = sortedMap.toQueryString()

        val enS = Uri.encode("$s$signToken")
        val encode = MD5.md5(enS)
        return encode
    }


}

fun Map<String, Any>.sign(): String {
    return Sign.sign(this)
}