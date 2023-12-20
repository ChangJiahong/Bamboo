package cn.changjiahong.bamboo.base.utils

import java.security.MessageDigest

/**
 *
 * @author ChangJiahong
 * @date 2022/7/12
 */
object MD5 {
    /** md5加密 */
    fun md5(content: String): String {
        val hash = MessageDigest.getInstance("MD5").digest(content.toByteArray())
        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            var str = Integer.toHexString(b.toInt())
            if (b < 0x10) {
                str = "0$str"
            }
            hex.append(str.substring(str.length - 2))
        }
        return hex.toString()
    }

}

fun String.md5(): String = MD5.md5(this)