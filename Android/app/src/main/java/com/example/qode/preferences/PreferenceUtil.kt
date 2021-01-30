package com.example.qode.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs : SharedPreferences = context.getSharedPreferences("firstTime", Context.MODE_PRIVATE)
    private val isLogin : SharedPreferences = context.getSharedPreferences("isLogin", Context.MODE_PRIVATE)

    fun getIsLogin(key:String, defValue: String):String{
        return prefs.getString(key,defValue).toString()
    }
    fun setIsLogin(key:String, str:String){
        prefs.edit().putString(key, str).apply()
    }


    fun getFirstTime(key:String, defValue : String):String{
        return prefs.getString(key, defValue).toString()
    }
    fun setFirstTime(key:String,str:String){
        prefs.edit().putString(key, str).apply()
    }


}