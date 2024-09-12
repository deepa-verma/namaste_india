package com.quinta.namasteindia.ui

import DeliveryResponse
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.quinta.namasteindia.adapter.DeliveryListAdapter
import com.quinta.namasteindia.R
import com.quinta.namasteindia.utils.UtilMethods
import com.quinta.namasteindia.utils.ApiService
import com.quinta.namasteindia.utils.CustomLoader
import com.quinta.namasteindia.utils.ServiceGenerator
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class DeliveryDetailsActivity : AppCompatActivity() {
    private var tvDate: TextView? = null
    private var noData: TextView? = null
    private var imgCalender: ImageView? = null
    private var logout: ImageView? = null
    private var refresh: ImageView? = null
    private var recyclerView: RecyclerView? = null
    var tokenValue: String? = ""
    var customLoader: CustomLoader? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_details)
        init()
    }

    private fun init() {
        setStatusBarColor(R.color.statusBarColor)
        customLoader = CustomLoader(this)
        tokenValue = UtilMethods.INSTANCE.getToken(this)
        //Log.e("TokenValue", tokenValue!!)
        val layoutManager = LinearLayoutManager(this)
        noData = findViewById(R.id.tv_noData)
        tvDate = findViewById(R.id.tv_date)
        val currentDate = getCurrentDate()
        tvDate?.text = currentDate
        logout = findViewById(R.id.logout)
        logout?.setOnClickListener(View.OnClickListener {
            UtilMethods.INSTANCE.setLoginPref(this, false)
            UtilMethods.INSTANCE.setToken(this, "")
            UtilMethods.INSTANCE.setDeliveryQty(this, "")
            val startIntent = Intent(this@DeliveryDetailsActivity, LoginActivity::class.java)
            startIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            this.startActivity(startIntent)
        })

        val animation: Animation = RotateAnimation(
            0.0f, 360.0f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.repeatCount = -1
        animation.duration = 2000

        refresh = findViewById(R.id.refresh)
        refresh?.setOnClickListener(View.OnClickListener {
            hitApi(tokenValue!!)
        })
        imgCalender = findViewById(R.id.img_calender)
        imgCalender?.setOnClickListener(View.OnClickListener {
            showDatePickerDialog()
        })
        recyclerView = findViewById(R.id.list_item)
        /*   val divider = DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
           divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.recycler_divider)!!)
           recyclerView?.addItemDecoration(divider)*/

        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)
        if (tokenValue != null) {
            hitApi(tokenValue!!)
        }
    }

    private fun hitApi(token: String) {
        if (UtilMethods.INSTANCE.isNetworkAvailable(this)) {
            /* loader.show()
             loader.setCancelable(false)
             loader.setCanceledOnTouchOutside(false)*/

            getDeliveryDetails(tvDate?.text.toString(), token)
        } else {
            Toast.makeText(
                this,
                "No internet connection",
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    private fun getDeliveryDetails(date: String, token: String) {
        try {
            //Log.d("request", date)
            //Log.d("request", token)
            customLoader?.show()
            val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
            val call = serviceGenerator.getDeliveryList(date, token)
            call.enqueue(object : retrofit2.Callback<DeliveryResponse> {
                override fun onResponse(
                    call: Call<DeliveryResponse>,
                    response: Response<DeliveryResponse>
                ) {
                    customLoader?.dismiss()
                    if (response.isSuccessful) {

                        // Log.e("Delivery", "is : " + Gson().toJson(response.body()).toString())
                        if (response.body()?.status == 0) {

                            if (response.body()?.data != null && response.body()?.data?.size!! > 0) {
                                noData?.visibility = View.GONE
                                recyclerView?.visibility = View.VISIBLE

                                val adapter = DeliveryListAdapter(response.body()?.data!!)
                                recyclerView?.adapter = adapter
                            } else {
                                noData?.visibility = View.VISIBLE
                                recyclerView?.visibility = View.GONE
                            }
                        } else {
                            noData?.visibility = View.VISIBLE
                            recyclerView?.visibility = View.GONE
                            Toast.makeText(
                                this@DeliveryDetailsActivity,
                                "No Delivery Details found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<DeliveryResponse>, t: Throwable) {
                    customLoader?.dismiss()
                    t.printStackTrace()
                    Log.e("failure:", t.message.toString())
                }
            }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setStatusBarColor(@ColorRes colorRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val color = ContextCompat.getColor(this, colorRes)
            window.statusBarColor = color
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Update the TextView with the selected date
                val selectedDate = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Date(selectedYear - 1900, selectedMonth, selectedDay))
                tvDate?.text = selectedDate
                hitApi(tokenValue!!)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }
}