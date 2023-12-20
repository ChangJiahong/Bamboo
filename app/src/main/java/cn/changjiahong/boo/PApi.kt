package cn.changjiahong.boo

import cn.changjiahong.bamboo.base.activity.BaseActivity


/**
 *
 * @author ChangJiahong
 * @date 2023/3/8
 */
object PApi {

    object Main {
        const val home = BaseActivity.HOMEPAGE
    }


//    object Main : G(null, "main") {
//        object Home : G(this, "home") {
//            val p1 = value("/page1")
//        }
//    }
//
//    open class G(val parent: G? = null, val group: String) {
//        var base = ""
//
//        fun value(g:String):String{
//            if (parent==null){
//                return "/$group$g"
//            }
//            if (base.isNotEmpty()){
//                return "$base$g"
//            }
//            base = parent.value("/$group$g")
//            return base
//        }
//    }

}