package cn.changjiahong.boo.repository.impl

import cn.changjiahong.bamboo.base.repository.requestService
import cn.changjiahong.boo.Test
import cn.changjiahong.boo.datasource.ILoginService
import cn.changjiahong.boo.repository.ILoginRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

/**
 *
 * @author ChangJiahong
 * @date 2023/3/9
 */

@Factory
class LoginRepositoryImpl : ILoginRepository() {

    override fun update() = requestService<ILoginService,List<Test>> { update() }

    override fun test(test: Test): Flow<Test> = requestService<ILoginService,Test>  {
        test(test)
    }
}