package cn.changjiahong.bamboo.koin

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("cn.changjiahong.bamboo")
class BaseModule


@Single
class A()