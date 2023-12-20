package cn.changjiahong.bamboo.base.sp

import cn.changjiahong.bamboo.base.app.BambooApplication
import cn.changjiahong.bamboo.base.utils.fromJson
import cn.changjiahong.bamboo.common.StringPairs
import com.google.gson.Gson
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 *
 * @author ChangJiahong
 * @date 2022/5/18
 */
class SPSetting(val domain: String) : SP {
    private val gson = Gson()
    private val spUtils: SPHelper by lazy {
        SPHelper(BambooApplication.context, domain)
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean =
        spUtils.getBoolean(getKey(key), defValue)

    override fun getLong(key: String, defValue: Long): Long = spUtils.getLong(getKey(key), defValue)

    override fun getString(key: String, defValue: String): String =
        spUtils.getString(getKey(key), defValue)

    override fun getFloat(key: String, defValue: Float): Float =
        spUtils.getFloat(getKey(key), defValue)

    override fun getInt(key: String, defValue: Int): Int = spUtils.getInt(getKey(key), defValue)

    override fun getStringSet(key: String, defValue: Set<String>): Set<String> =
        spUtils.getStringSet(getKey(key), defValue)

    override fun <T:Any> get(key: String, type: Type): T? {
        val json = getString(key, "")
        return if (json.isNotEmpty()) json.fromJson( type) else null
    }

    override fun put(key: String, value: Set<String>): Boolean =
        spUtils.editHelper().put(getKey(key), value).commit()

    override fun put(key: String, value: Any): Boolean =
        spUtils.editHelper().put(getKey(key), value).commit()

    override fun put(makeStringPairs: StringPairs<Any>.() -> Unit): Boolean =
        spUtils.editHelper().put(makeStringPairs).commit()

    override fun <T> putJson(key: String, value: T): Boolean {
        if (value != null) {
            return spUtils.editHelper().put(getKey(key), gson.toJson(value)).commit()
        }
        return false
    }

    override fun contains(key: String): Boolean = spUtils.contains(getKey(key))

    override fun getKey(key: String): String = key
}