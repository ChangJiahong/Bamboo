package cn.changjiahong.bamboo.base.lan

import cn.changjiahong.bamboo.base.app.BambooApplication
import cn.changjiahong.bamboo.base.sp.SP
import cn.changjiahong.bamboo.base.sp.SPSetting
import java.util.*

/**
 *
 * @author ChangJiahong
 * @date 2022/6/30
 */
private const val LANGUAGE_DOMAIN = "language_domain"

object LanguageSetting : SP by SPSetting(LANGUAGE_DOMAIN) {

    /**
     * 手机系统语言
     */
    const val SYSTEM_LANGUAGE = "system_language"

    /**
     * app设置语言
     */
    const val APP_LANGUAGE = "language"

    fun getSystemLanguage(): Locale {
        val system = getString(SYSTEM_LANGUAGE, "")
        if (system.isNotEmpty()) {
            return Locale.forLanguageTag(system)
        }
        val systemLocale = LanguageManager.getSystemLocale(BambooApplication.context)
        put(SYSTEM_LANGUAGE, systemLocale.toLanguageTag())
        return systemLocale
    }


    fun getAppLanguage(): Locale {
        val appLan = getString(APP_LANGUAGE, "")
        if (appLan.isNotEmpty()) {
            return Locale.forLanguageTag(appLan)
        }
        return getSystemLanguage()
    }

    fun setAppLanguage(lan:Locale) = run { put(APP_LANGUAGE,lan.toLanguageTag()) }
}