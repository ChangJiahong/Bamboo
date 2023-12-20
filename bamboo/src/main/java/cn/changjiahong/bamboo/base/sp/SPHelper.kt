package cn.changjiahong.bamboo.base.sp

import android.content.Context
import android.content.SharedPreferences
import cn.changjiahong.bamboo.common.StringPairs

/**
 *
 * SharedPreferences 帮助类
 * @author ChangJiahong
 * @date 2022/5/18
 */
class SPHelper(val context: Context, domain: String = CONFIG) {


    companion object {
        /**
         * 默认存储地址
         */
        const val CONFIG = "config"
    }

    /**
     * SharedPreferences 持有
     */
    private var sp: SharedPreferences = context.getSharedPreferences(domain, Context.MODE_PRIVATE)

    val editHelper = { EditorHelper(sp) }

    fun getString(key: String, defValue: String): String {
        return sp.getString(key, defValue) ?: ""
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }

    fun getFloat(key: String, defValue: Float): Float {
        return sp.getFloat(key, defValue)
    }

    fun getInt(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    fun getLong(key: String, defValue: Long): Long {
        return sp.getLong(key, defValue)
    }

    fun getStringSet(key: String, defValue: Set<String>): Set<String> {
        return sp.getStringSet(key, defValue) ?: setOf()
    }

    fun contains(key: String): Boolean {
        return sp.contains(key)
    }

    class EditorHelper(sph: SharedPreferences) {
        private var editor: SharedPreferences.Editor = sph.edit()

        /**
         * 保存仅支持String、Int、Long、Float、Boolean基本类型
         */
        fun put(key: String, value: Any): EditorHelper {
            when (value) {
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Long -> editor.putLong(key, value)
                is Float -> editor.putFloat(key, value)
                is Boolean -> editor.putBoolean(key, value)
//                else -> throw Exception("SharedPreferences不支持存入${value.javaClass.canonicalName}类型的参数")
                else -> {}
            }
            return this
        }

        /**
         * 存入set<Stirng>类型的
         */
        fun put(key: String, value: Set<String>): EditorHelper {
            editor.putStringSet(key, value)
            return this
        }


        /**
         * 以参数类型存入
         */
        fun put(makeStringPairs: StringPairs<Any>.() -> Unit): EditorHelper {
            val paramStringPairs = StringPairs<Any>()
            paramStringPairs.makeStringPairs()
            paramStringPairs.pairs.forEach {
                put(it.key, it.value)
            }
            return this
        }


        /**
         * 提交到硬盘
         */
        fun commit(): Boolean {
            return editor.commit()
        }

        /**
         * 提交到硬盘
         */
        fun apply() {
            editor.apply()
        }
    }


}

/**
 * Context扩展函数
 * 包装SPHelperKt类，在内部处理SharedPreferences储存
 */
fun Context.putToSharedPreferences(
    domain: String = SPHelper.CONFIG, dos: SPHelper.EditorHelper.() -> Unit
): SPHelper.EditorHelper {
    return SPHelper(this, domain).editHelper().apply {
        // 操作
        dos()
//        // 提交
//        apply()
    }
}

/**
 * Context扩展函数
 * 包装SPHelperKt类，在内部处理SharedPreferences储存
 */
fun Context.getFromSharedPreferences(
    domain: String = SPHelper.CONFIG, dos: SPHelper.() -> Unit
): SPHelper {
    return SPHelper(this, domain).apply {
        // 操作
        dos()
    }
}


/**
 * 从SharedPreferences获取String
 */
fun Context.getStringFromSharedPreferences(
    key: String, defValue: String = "", domain: String = SPHelper.CONFIG
): String {
    return SPHelper(this, domain).getString(key, defValue)
}

/**
 * 从SharedPreferences获取String Set
 */
fun Context.getStringSetFromSharedPreferences(
    key: String, defValue: Set<String> = setOf(), domain: String = SPHelper.CONFIG
): Set<String> {
    return SPHelper(this, domain).getStringSet(key, defValue)
}

/**
 * 从SharedPreferences获取Boolean
 */
fun Context.getBooleanFromSharedPreferences(
    key: String, defValue: Boolean = false, domain: String = SPHelper.CONFIG
): Boolean {
    return SPHelper(this, domain).getBoolean(key, defValue)
}

/**
 * 从SharedPreferences获取Float
 */
fun Context.getFloatFromSharedPreferences(
    key: String, defValue: Float = 0F, domain: String = SPHelper.CONFIG
): Float {
    return SPHelper(this, domain).getFloat(key, defValue)
}

/**
 * 从SharedPreferences获取Int
 */
fun Context.getIntFromSharedPreferences(
    key: String, defValue: Int = 0, domain: String = SPHelper.CONFIG
): Int {
    return SPHelper(this, domain).getInt(key, defValue)
}

/**
 * 从SharedPreferences获取Long
 */
fun Context.getLongFromSharedPreferences(
    key: String, defValue: Long = 0L, domain: String = SPHelper.CONFIG
): Long {
    return SPHelper(this, domain).getLong(key, defValue)
}