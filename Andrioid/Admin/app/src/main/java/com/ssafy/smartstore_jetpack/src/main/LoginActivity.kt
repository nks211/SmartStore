package com.ssafy.smartstore_jetpack.src.main

import android.content.Intent
import android.os.Bundle
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.ssafy.smartstore_jetpack.config.BaseActivity
import com.ssafy.smartstore_jetpack.databinding.ActivityLoginBinding
import com.ssafy.smartstore_jetpack.src.main.login.JoinFragment
import com.ssafy.smartstore_jetpack.src.main.login.LoginFragment

private const val TAG = "LoginActivity_싸피"
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //로그인 된 상태인지 확인
        val user = sharedPreferencesUtil.getUser()

        //로그인 상태 확인. id가 있다면 로그인 된 상태
        if (user.id != ""){
            openMainActivity()
        }
    }
    fun openMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}