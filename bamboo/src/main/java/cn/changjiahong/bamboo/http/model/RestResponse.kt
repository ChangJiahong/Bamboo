package cn.changjiahong.bamboo.http.model

/**
 *
 * @author ChangJiahong
 * @date 2022/6/29
 */
data class RestResponse<T>(
    val code: Int,
    val msg: String="",
    val data: T?
) {
    fun isSuccess() = code == 1

    fun checkData() = data != null

    fun successRestData() = isSuccess() && checkData()

    fun requsetData() = data!!
}