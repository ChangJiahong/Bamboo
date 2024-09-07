package cn.changjiahong.boo.app.main.fragments.square

import android.annotation.SuppressLint
import android.os.Bundle
import cn.changjiahong.bamboo.base.fragment.BaseViewPageFragment
import cn.changjiahong.boo.R
import cn.changjiahong.boo.app.main.fragments.square.vm.SquareViewModel
import cn.changjiahong.boo.databinding.FragmentSquareBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * 广场
 */
class SquareFragment :
    BaseViewPageFragment<FragmentSquareBinding, SquareViewModel>(R.layout.fragment_square) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingPermission")
    override fun onViewInit() {
        super.onViewInit()



    }


    companion object {

        val INSTANCE by lazy { SquareFragment() }
    }
}