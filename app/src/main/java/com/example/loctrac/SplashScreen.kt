package com.example.loctrac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val isUserLoggedIn = SharedPref.getBoolean(PrefConstant.IS_USER_LOGGED_IN)

        if (isUserLoggedIn){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}