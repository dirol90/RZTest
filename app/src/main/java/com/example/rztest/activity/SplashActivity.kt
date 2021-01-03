package com.example.rztest.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.rztest.R
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Launcher)
        super.onCreate(savedInstanceState)
        Timer("LogoDelay", false).schedule(500) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            this@SplashActivity.finish()
        }
    }
}