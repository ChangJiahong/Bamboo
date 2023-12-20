package cn.changjiahong.boo.datasource

import cn.changjiahong.bamboo.http.model.RestResponse
import cn.changjiahong.boo.Test
import cn.changjiahong.boo.UApi
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Body
import retrofit2.http.POST

/**
 *
 * @author ChangJiahong
 * @date 2023/3/12
 */

interface ILoginService {

    @POST(UApi.UPDATE)
    fun update(): Flow<RestResponse<List<Test>>>


    @POST(UApi.TEST)
    fun test(@Body test: Test): Flow<RestResponse<Test>>

}