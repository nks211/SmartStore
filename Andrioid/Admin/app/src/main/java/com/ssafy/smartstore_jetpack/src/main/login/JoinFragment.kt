package com.ssafy.smartstore_jetpack.src.main.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentJoinBinding
import com.ssafy.smartstore_jetpack.dto.User
import com.ssafy.smartstore_jetpack.src.main.LoginActivity
import com.ssafy.smartstore_jetpack.src.main.MainActivity
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch

// 회원 가입 화면
private const val TAG = "JoinFragment_싸피"
class JoinFragment : BaseFragment<FragmentJoinBinding>(FragmentJoinBinding::bind, R.layout.fragment_join){
    private var checkedId = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnJoin.isEnabled = false

        //id 중복 확인 버튼
        binding.btnConfirm.setOnClickListener{

            lifecycleScope.launch {
                val id = binding.editTextJoinID.text.toString()
                checkedId = RetrofitUtil.userService.isUsedId(id)
                if (!checkedId) {
                    binding.btnJoin.isEnabled = true
                    Toast.makeText(requireContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(requireContext(), "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
            }

        }

        // 회원가입 버튼
        binding.btnJoin.setOnClickListener {

            val id = binding.editTextJoinID.text.toString()
            val password = binding.editTextJoinPW.text.toString()
            val nickname = binding.editTextJoinName.text.toString()

            if (!checkedId) {
                if (id != "" && password != "" && nickname != "") {
                    lifecycleScope.launch {
                        val user = User(id, password, nickname)
                        RetrofitUtil.userService.insert(user)
                        Navigation.findNavController(requireView()).popBackStack()
                    }
                }
                else{
                    if (id == "") {
                        showToast("아이디를 입력하세요.")
                    }
                    if (password != "") {
                        showToast("비밀번호를 입력하세요.")
                    }
                    if (nickname == "") {
                        showToast("닉네임을 입력하세요.")
                    }
                }
            }

        }
    }
}