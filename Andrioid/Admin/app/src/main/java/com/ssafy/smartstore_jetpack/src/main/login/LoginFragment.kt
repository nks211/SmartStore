package com.ssafy.smartstore_jetpack.src.main.login

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.BaseFragment
import com.ssafy.smartstore_jetpack.databinding.FragmentLoginBinding
import com.ssafy.smartstore_jetpack.dto.User
import com.ssafy.smartstore_jetpack.src.main.LoginActivity
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import com.ssafy.smartstore_jetpack.util.SharedPreferencesUtil
import kotlinx.coroutines.launch
import kotlin.math.log


// 로그인 화면
private const val TAG = "LoginFragment_싸피"
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::bind, R.layout.fragment_login){
    private lateinit var loginActivity: LoginActivity
    private val viewModel : LoginViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loginActivity = context as LoginActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner) {
            if (it.id.isNotEmpty()) {
                ApplicationClass.sharedPreferencesUtil.addUser(it)
                loginActivity.openMainActivity()
            }
            else {
                showToast("ID, Password를 확인하세요.")
            }
        }
        // 로그인 구현
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                val id = binding.editTextLoginID.text.toString()
                val password = binding.editTextLoginPW.text.toString()
                viewModel.login(id, password)
            }

        }

        binding.btnJoin.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.joinFragment)
        }
    }
}


