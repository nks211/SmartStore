package com.ssafy.smartstore_jetpack.src.main.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.smartstore_jetpack.dto.User
import com.ssafy.smartstore_jetpack.util.RetrofitUtil
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    fun login(id: String, password: String) {
        viewModelScope.launch {
            try {
                _user.value = RetrofitUtil.userService.login(User(id, password))
            }
            catch (e : Exception) {
                _user.value = User()
            }

        }
    }

}