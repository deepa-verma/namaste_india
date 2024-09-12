package com.quinta.namasteindia.ui

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.quinta.namasteindia.R
import com.quinta.namasteindia.utils.UtilMethods
import com.quinta.namasteindia.utils.CustomLoader


class LoginActivity : AppCompatActivity() {
    //fields
  //  private var textInputEmail: TextInputLayout? = null
   // private var textInputPassword: TextInputLayout? = null


    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var loginBtn: Button?= null
    var customLoader: CustomLoader? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

   private fun init()
    {
         customLoader = CustomLoader(this)

       // textInputEmail = findViewById(R.id.txt_input_email)
       // textInputPassword = findViewById(R.id.txt_input_password)
        setStatusBarColor(R.color.blue_700)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        loginBtn = findViewById(R.id.loginBtn)
        loginBtn?.setOnClickListener {
            validation()
        }
    }

    private fun validation() {
        if (etEmail?.text.toString().trim().isEmpty()) {
            etEmail?.isEnabled = true;
            etEmail?.error = "Please Enter username";
        } else if (etPassword?.text.toString().trim().isEmpty()) {
            etPassword?.isEnabled = true;
            etPassword?.error = "Please Enter password";
        }
        else{
            callLoginApi()
        }

    }

    private fun callLoginApi() {
        if (UtilMethods.INSTANCE.isNetworkAvailable(this@LoginActivity)) {

            UtilMethods.INSTANCE.login(
                this@LoginActivity,customLoader!!,
                etEmail?.text.toString().trim(),
                etPassword?.text.toString().trim()
            )
        }
        else {
            Toast.makeText(
                this@LoginActivity,
                "No internet connection",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun setStatusBarColor(@ColorRes colorRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val color = ContextCompat.getColor(this, colorRes)
            window.statusBarColor = color
        }
    }
}