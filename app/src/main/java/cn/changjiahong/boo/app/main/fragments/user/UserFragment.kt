package cn.changjiahong.boo.app.main.fragments.user

import androidx.fragment.app.Fragment
import cn.changjiahong.bamboo.base.fragment.BaseViewPageFragment
import cn.changjiahong.boo.R
import cn.changjiahong.boo.app.main.fragments.user.vm.UserViewModel
import cn.changjiahong.boo.databinding.FragmentUserBinding

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : BaseViewPageFragment<FragmentUserBinding, UserViewModel>(R.layout.fragment_user) {




    companion object {

        val INSTANCE by lazy { UserFragment() }
    }
}