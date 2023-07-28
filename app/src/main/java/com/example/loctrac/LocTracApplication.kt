package com.example.loctrac

import android.app.Application

class LocTracApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
    }

}