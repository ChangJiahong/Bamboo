package cn.changjiahong.bamboo.base.lan

import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*


/**
 *  APP语言管理
 * @author ChangJiahong
 * @date 2022/6/28
 */
object LanguageManager {

    private val TAG = "LanManager"

    private val supportLanguages =
        arrayListOf(
            Locale.SIMPLIFIED_CHINESE,
            Locale.TRADITIONAL_CHINESE,
            Locale.ENGLISH
        )

    private val defaultLanguage: Locale = Locale.SIMPLIFIED_CHINESE

    fun isSupport(locale: Locale): Boolean {
        return supportLanguages.contains(locale)
    }

    /**
     * 应用多语言切换，重写BaseActivity中的attachBaseContext即可
     * 采用本地SP存储的语言
     *
     * @param context 上下文
     * @return context
     */
    fun attachBaseContext(context: Context): Context {
        val language: Locale = getLanguageLocale()
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            createConfigurationContext(context, language)
        } else {
            updateConfiguration(context, language)
        }
    }

    /**
     * Android 7.1以上通过createConfigurationContext
     * N增加了通过config.setLocales去修改多语言
     *
     * @param context  上下文
     * @param language 语言
     * @return context
     */
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private fun createConfigurationContext(context: Context, language: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        Log.d(TAG, "current Language locale = ${language.language}")
        val localeList = LocaleList(language)
        configuration.setLocales(localeList)
        return context.createConfigurationContext(configuration)
    }

    /**
     * Android 7.1 以下通过 updateConfiguration
     *
     * @param context  context
     * @param language 语言
     * @return Context
     */
    private fun updateConfiguration(context: Context, language: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        Log.e(TAG, "updateLocalApiLow==== " + language.language)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
            configuration.setLocales(LocaleList(language))
        } else {
            // updateConfiguration
            configuration.locale = language
            val dm = resources.displayMetrics
            resources.updateConfiguration(configuration, dm)
        }
        return context
    }

    /**
     * 切换语言
     *
     * @param language 语言
     * @param activity 当前界面
     * @param cls      跳转的界面
     */
    fun switchLanguage(context: Context, language: Locale) {
        if (!isSupport(language)){
            return
        }
        LanguageSetting.setAppLanguage(language)
        attachBaseContext(context)
    }

    fun getCurrentLan() = getLanguageLocale()

    fun getSystemLan() = LanguageSetting.getSystemLanguage()

    /**
     * 获取app设置语言
     */
    private fun getLanguageLocale(): Locale {
        var spLan = LanguageSetting.getAppLanguage()
        if (!isSupport(spLan)) {
            // 如果系统语言不支持，使用app默认语言
            spLan = defaultLanguage
            LanguageSetting.setAppLanguage(spLan)
        }
        return spLan
    }

    /**
     * 获取系统语言
     */
    fun getSystemLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList.getDefault().get(0)
        } else {
            Locale.getDefault()
        }
    }

    fun isChina(): Boolean {
        return getCurrentLan().language.startsWith("zh")
    }

}