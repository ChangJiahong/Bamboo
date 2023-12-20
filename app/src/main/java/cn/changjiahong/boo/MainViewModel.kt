package cn.changjiahong.boo

import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewModelScope
import cn.changjiahong.bamboo.base.utils.collectIn
import cn.changjiahong.bamboo.base.utils.toJson
import cn.changjiahong.bamboo.base.viewmodel.BaseViewModel
import cn.changjiahong.bamboo.base.widget.toast.toast
import cn.changjiahong.boo.repository.ILoginRepository
import kotlinx.coroutines.flow.catch
import org.koin.android.annotation.KoinViewModel

/**
 *
 * @author ChangJiahong
 * @date 2023/3/16
 */

@KoinViewModel
class MainViewModel(val iLoginRepository: ILoginRepository) : BaseViewModel() {

    fun test(){

        iLoginRepository.test(Test(231,"hhh")).catch {
            toast(it.message?:"")
        }.collectIn(viewModelScope){
            toast(it.toJson())
        }
    }

}