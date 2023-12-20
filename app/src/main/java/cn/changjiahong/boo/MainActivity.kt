package cn.changjiahong.boo

import android.content.Intent
import android.os.Bundle
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import cn.changjiahong.bamboo.base.activity.BaseViewModelActivity
import cn.changjiahong.bamboo.base.lan.LanguageManager
import cn.changjiahong.bamboo.base.sp.SPSetting
import cn.changjiahong.boo.databinding.ActivityMainBinding
import cn.changjiahong.boo.repository.ILoginRepository
import com.alibaba.android.arouter.facade.annotation.Route
import org.koin.android.ext.android.inject
import java.util.*

@Route(path = PApi.Main.home)
class MainActivity : BaseViewModelActivity<ActivityMainBinding,MainViewModel>(R.layout.activity_main) {

    val iLoginRepository:ILoginRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sp = SPSetting("test")

        mBinding.change.setOnClickListener {

//            CustomTabsIntent.Builder().build().launchUrl(this,"https://www.baidu.com".toUri())


//            ContextCompat.startActivity(this,Intent(Intent.ACTION_VIEW).apply {
//
//
//                putExtra(CustomTabsIntent.EXTRA_ENABLE_INSTANT_APPS, false)
//
//                putExtras(Bundle())
//
//                putExtra(CustomTabsIntent.EXTRA_SHARE_STATE, 0)
//                putExtra(CustomTabsIntent.EXTRA_ENABLE_INSTANT_APPS, true)
//                putExtra(CustomTabsIntent.EXTRA_SHARE_STATE, 0);
//
//
//                setData("https://www.baidu.com".toUri())
//            },null)
//            mViewModel.test()


//            val s = """
//                {
//                    "code":200,
//                    "msg":"ok",
//                    "data":[{"a":0,"c":false,"d":[{"b":null}]}]}
//            """.trimIndent()
//
//            val a :RestResponse<List<TestD>>? = s.fromJson()
//
//            sp.putJson("a",a)
//            KLog.d("Moshi",sp.getString("a",""))
//            val b:RestResponse<List<TestD>>? = sp.get("a")
//            KLog.d("Moshi",b?.toJson()?:"")
//            toast(a?.toString()?:"")


           if (LanguageManager.getCurrentLan()== Locale.ENGLISH) {
               changeLanguage(Locale.SIMPLIFIED_CHINESE)
           }else{
               changeLanguage(Locale.ENGLISH)
           }
        }

    }
}