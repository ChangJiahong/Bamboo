package cn.changjiahong.boo.repository

import cn.changjiahong.boo.Test
import kotlinx.coroutines.flow.Flow

/**
 *
 * @author ChangJiahong
 * @date 2023/3/9
 */

abstract class ILoginRepository : Repository() {

    abstract fun update(): Flow<List<Test>>
    abstract fun test(test: Test): Flow<Test>
}