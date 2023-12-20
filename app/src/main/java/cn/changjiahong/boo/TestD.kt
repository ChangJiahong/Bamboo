package cn.changjiahong.boo

/**
 *
 * @author ChangJiahong
 * @date 2023/3/11
 */
class TestD(val a:Int,val b:String,val c:Boolean,val d:List<Test> ) {
}

class Test(val a:Int,val b:String,val c:User= User(arrayListOf("as","sd")))

class User(val a:List<String>)