package cn.changjiahong.boo.app.main.fragments.msn

import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.changjiahong.bamboo.base.fragment.BaseViewPageFragment
import cn.changjiahong.boo.R
import cn.changjiahong.boo.app.main.fragments.msn.vm.MsnViewModel
import cn.changjiahong.boo.databinding.FragmentMsnBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MsnFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MsnFragment : BaseViewPageFragment<FragmentMsnBinding,MsnViewModel>(R.layout.fragment_msn) {


    companion object {

        val INSTANCE by lazy { MsnFragment() }
    }
}