package cn.changjiahong.bamboo.base.utils

import android.content.Context
import cn.changjiahong.bamboo.BuildConfig
import java.util.*

/**
 * @description
 * @author ChangJiahong
 * @date 2021/9/12
 */
object PropSettings {

    private lateinit var properties : Properties

    fun init(context: Context){
        val name = if (BuildConfig.DEBUG) "config_debug" else "config"
        init(context,"$name.properties")
    }

    fun init(context: Context,fileName:String){
        properties = Properties()
        properties.load(context.assets.open(fileName))
    }

    fun getString(key: String): String {
        return properties.getProperty(key)?:""
    }
}