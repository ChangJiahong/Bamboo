package cn.changjiahong.boo

import cn.changjiahong.bamboo.http.IUApi

/**
 *
 * @author ChangJiahong
 * @date 2023/3/12
 */
object UApi: IUApi("http://192.168.0.112:8080") {


    const val UPDATE ="/m1/2425407-0-default/update"
    const val TEST ="/public/test/json"

}