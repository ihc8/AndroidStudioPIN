package com.example.pin.login

import android.app.Application

class SharedApplication: Application() {

    companion object{
        lateinit var preferences: Preferences
    }

    override fun onCreate(){
        super.onCreate()
        preferences = Preferences(applicationContext)
    }
}