package com.ssafy.smartstore_jetpack.util

import android.content.Context
import android.content.SharedPreferences
import com.ssafy.smartstore_jetpack.dto.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedPreferencesUtil (context: Context) {
    val SHARED_PREFERENCES_NAME = "smartstore_preference"
    val COOKIES_KEY_NAME = "cookies"

    var preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    //사용자 정보 저장
    fun addUser(user:User){
        val editor = preferences.edit()
        editor.putString("id", user.id)
        editor.putString("name", user.name)
        editor.putBoolean("isAdmin", user.isAdmin)
        editor.apply()
    }

    fun getUser(): User{
        val id = preferences.getString("id", "")
        if (id != ""){
            val name = preferences.getString("name", "")
            val isAdmin = preferences.getBoolean("isAdmin", false)
            return User(id!!, "", name!!, isAdmin)
        }else{
            return User()
        }
    }

    fun deleteUser(){
        CoroutineScope(Dispatchers.IO).launch {
            val bool = RetrofitUtil.userService.logout(getUser())
            if(bool){
                //preference 지우기
                val editor = preferences.edit()
                editor.clear()
                editor.apply()
            }
        }
    }

    fun addUserCookie(cookies: HashSet<String>) {
        val editor = preferences.edit()
        editor.putStringSet(COOKIES_KEY_NAME, cookies)
        editor.apply()
    }

    fun getUserCookie(): MutableSet<String>? {
        return preferences.getStringSet(COOKIES_KEY_NAME, HashSet())
    }

    fun deleteUserCookie() {
        preferences.edit().remove(COOKIES_KEY_NAME).apply()
    }


}