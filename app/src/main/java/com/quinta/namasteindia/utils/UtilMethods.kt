package com.quinta.namasteindia.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.quinta.namasteindia.response.LoginResponse
import com.quinta.namasteindia.ui.DeliveryDetailsActivity
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

enum class UtilMethods {
    INSTANCE;

    fun isNetworkAvailable(context: Context): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }


    fun login(context: Context, customLoader: CustomLoader, userId: String, password: String) {
        Log.e("login", "$userId")
        try {
            customLoader.show()
            val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
            val call = serviceGenerator.login(userId, password)
            call.enqueue(object : retrofit2.Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: retrofit2.Response<LoginResponse>
                ) {
                    try {
                        customLoader.dismiss()
                        if (response.isSuccessful) {
                            if (response.body()?.status == 0) {
                                Log.e("login", "is : " + Gson().toJson(response.body()).toString())
                                setLoginPref(context, true)
                                Log.e("token", "is : " + response.body()!!.data?.tocken.toString())
                                setToken(context, response.body()!!.data?.tocken.toString())
                                val intent = Intent(context, DeliveryDetailsActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                context.startActivity(intent)
                                (context as Activity).finish()

                            } else {
                                Toast.makeText(
                                    context,
                                    "Invalid userid/password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            // Handle the error response
                            Toast.makeText(
                                context,
                                "Login failed",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(
                                "Login failed:",
                                response.message() + " " + response.code().toString()
                            )

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    // Handle network errors here
                    customLoader.dismiss()
                    t.printStackTrace()
                    Log.e("failure:", t.message.toString())
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


   /* fun stockOutBarcode(barCode: String,deliveryId:String,vehicleNo:String,userId:String) {
            try {
                val apiClient = ApiClient.buildService(ApiService::class.java)
                val call = apiClient.stockOutBarcode(barCode,deliveryId,userId,vehicleNo)
                call.enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                    }
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        Log.e("failure:", t.message.toString())
                    }
                }
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }

    }*/



    open fun setLoginPref(context: Context, isLogin: Boolean) {
        val prefs = context.getSharedPreferences(
            Constants.prefName,
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean(Constants.isLogin, isLogin)
        editor.apply()
    }

    open fun getLoginPref(context: Context): Boolean {
        val prefs = context.getSharedPreferences(
            Constants.prefName,
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean(Constants.isLogin, false)
    }

    /*   // To set a value in SharedPreferences
      open fun setToken(key: String, value: String) {
           val sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
           val editor = sharedPreferences.edit()
           editor.putString(key, value)
           editor.apply()
       }

       // To get a value from SharedPreferences
       open fun getToken(key: String): String? {
           val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
           return sharedPreferences.getString(key, null)
       }*/

    fun setToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences(
            Constants.prefName,
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(Constants.token, token)
        editor.apply()
    }

    open fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(
            Constants.prefName,
            Context.MODE_PRIVATE
        )
        return prefs.getString(Constants.token, null)
    }

    fun setDeliveryQty(context: Context, Qty: String) {
        val prefs = context.getSharedPreferences(
            Constants.prefName,
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(Constants.deliveryQty, Qty)
        editor.apply()
    }

    open fun getDeliveryQty(context: Context): String? {
        val prefs = context.getSharedPreferences(
            Constants.prefName,
            Context.MODE_PRIVATE
        )
        return prefs.getString(Constants.deliveryQty, "")
    }
   /* fun setPosition(context: Context, position: Int) {
        val prefs = context.getSharedPreferences(
            Constants.prefName,
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt(Constants.deliveryQty, position)
        editor.apply()
    }

    open fun getPosition(context: Context): Int? {
        val prefs = context.getSharedPreferences(
            Constants.prefName,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(Constants.deliveryQty, 0)
    }*/


    fun getCurrentDate(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd") // Define your desired date format here
        return dateFormat.format(currentDate)
    }

    interface ApiCallBack {
        fun onSuccess(`object`: Any?)
    }
}