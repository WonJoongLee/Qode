package com.example.qode.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val prefs : SharedPreferences = context.getSharedPreferences("firstTime", Context.MODE_PRIVATE)
    fun getFirstTime(key:String, defValue : String):String{
        return prefs.getString(key, defValue).toString()
    }
    fun setFirstTime(key:String,str:String){
        prefs.edit().putString(key, str).apply()
    }

}