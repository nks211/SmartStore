package com.ssafy.smartstore_jetpack.src.main.my

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentUserInfoUpdateBinding
import com.ssafy.smartstore_jetpack.dto.User
import com.ssafy.smartstore_jetpack.src.main.LoginActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivityViewModel
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch

class UserInfoUpdateFragment: BaseFragment<FragmentUserInfoUpdateBinding>(FragmentUserInfoUpdateBinding::bind, R.layout.fragment_user_info_update) {
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity.hideBottomNav(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = ApplicationClass.sharedPreferencesUtil.getUser().id
        binding.editTextJoinID.text = id
        binding.editTextJoinName.setText(ApplicationClass.sharedPreferencesUtil.getUser().name)


        binding.btnJoin.setOnClickListener {
            val password = binding.editTextJoinPW.text.toString()
            val repeat = binding.editTextJoinPWConfirm.text.toString()
            val nickname = binding.editTextJoinName.text.toString()

            if (password != "" && (repeat == password) && nickname != "" ) {
                lifecycleScope.launch {
                    val user = User(id, password, nickname)
                    RetrofitUtil.userService.update(user)
                    ApplicationClass.sharedPreferencesUtil.deleteUser()
                    showToast("다시 로그인 해 주세요.")
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }

            else{
                if (password == "") {
                    showToast("비밀번호를 입력하세요.")
                }else if(password != repeat){
                    showToast("확인용 비밀번호가 같지 않습니다")
                }
                if (nickname == "") {
                    showToast("닉네임을 입력하세요.")
                }
            }
        }

        binding.btnCancel.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }

    }



}