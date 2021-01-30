package com.example.qode.preferences

import android.app.Application

class SharedData : Application() {
    companion object{
        lateinit var prefs : PreferenceUtil
        lateinit var isLogin : PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        isLogin = PreferenceUtil(applicationContext)
        super.onCreate()
    }
}