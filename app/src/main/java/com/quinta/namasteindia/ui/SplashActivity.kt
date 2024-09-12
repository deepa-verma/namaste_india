package com.quinta.namasteindia.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.quinta.namasteindia.R
import com.quinta.namasteindia.utils.UtilMethods


class SplashActivity : AppCompatActivity() {
    var isLogin: Boolean? = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setStatusBarColor(R.color.statusBarColor)
        isLogin = UtilMethods.INSTANCE.getLoginPref(this)
        //Log.d("isLogin", "$isLogin")

        // Load animation resources
        val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val splashLogo = findViewById<ImageView>(R.id.splash_logo)

        // Apply animations to views
        splashLogo.startAnimation(slideUpAnimation)

        slideUpAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Animation started

            }

            override fun onAnimationEnd(animation: Animation?) {
                // Animation ended, start the fade-out animation
                splashLogo.startAnimation(fadeOutAnimation)

                Handler().postDelayed(Runnable {

                    if (isLogin as Boolean) {
                        //Log.d("isLogin", "$isLogin")
                        //Log.d("Error5", "test")
                        val intent = Intent(this@SplashActivity, DeliveryDetailsActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                       // Log.d("Error6", "$isLogin")
                        //Log.d("Error7", "test")
                        val i = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(i)
                        finish()
                    }

                }, 2000)

            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Animation repeated
            }
        })

        /* fadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
             override fun onAnimationStart(animation: Animation?) {
                 // Animation started
             }

             override fun onAnimationEnd(animation: Animation?) {
                 // Animation ended, start the next activity
                 val mainIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                 startActivity(mainIntent)
                 finish()
             }

             override fun onAnimationRepeat(animation: Animation?) {
                 // Animation repeated
             }
         })*/
    }

    private fun setStatusBarColor(@ColorRes colorRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val color = ContextCompat.getColor(this, colorRes)
            window.statusBarColor = color
        }
    }
}
