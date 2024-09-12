package com.quinta.namasteindia.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.quinta.namasteindia.adapter.ScanUpdateAdapter
import com.quinta.namasteindia.R
import com.quinta.namasteindia.request.ScanRequest
import com.quinta.namasteindia.response.BarcodeResponse
import com.quinta.namasteindia.response.ProductData
import com.quinta.namasteindia.utils.UtilMethods
import com.quinta.namasteindia.utils.ApiService
import com.quinta.namasteindia.utils.CustomLoader
import com.quinta.namasteindia.utils.ServiceGenerator
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class ScanActivity : AppCompatActivity() {
    private var tvDelQuantity: TextView? = null
    private var noData: TextView? = null
    private var barcode: EditText? = null
    private var backButton: ImageView? = null
    private var listCard: CardView? = null
    private var recyclerView: RecyclerView? = null
    private val dataList: List<ProductData>? = null
    var deliveryQty: String? = ""
    private var deliveryId: String? = ""
    private var vehicleNo: String? = ""
    private var position: Int? = 0
    private var tokenValue: String? = ""
    var customLoader: CustomLoader? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        init()
    }

    private fun init() {
        setStatusBarColor(R.color.statusBarColor)
        customLoader = CustomLoader(this)
        val layoutManager = LinearLayoutManager(this)
        tokenValue = UtilMethods.INSTANCE.getToken(this)

        // Log.e("DeliveryQty",deliveryQty!!)
        tvDelQuantity = findViewById(R.id.tv_deliveryQty)
        backButton = findViewById(R.id.img_back)
        barcode = findViewById(R.id.et_barcode)
        backButton?.setOnClickListener(View.OnClickListener { finish() })
        noData = findViewById(R.id.noData)
        listCard = findViewById(R.id.list_card)

        recyclerView = findViewById(R.id.list_data)
        recyclerView?.layoutManager = layoutManager

        deliveryQty = UtilMethods.INSTANCE.getDeliveryQty(this)
        // Log.e("deliveryQty=", deliveryQty.toString())
        // val dataList = intent.getSerializableExtra("data_list_key") as List<ProductData>
        // val  dataList= intent.getSerializableExtra("data_list_key") as List<ProductData>
        if (deliveryQty == null || deliveryQty == "") {
            deliveryQty = intent.getStringExtra("deliveryQty")
            tvDelQuantity?.text = deliveryQty
        } else {
            tvDelQuantity?.text = deliveryQty
        }

        deliveryId = intent.getStringExtra("deliveryId")
        vehicleNo = intent.getStringExtra("vehicleNo")
       /* Log.e("deliveryQty", deliveryQty.toString())
        Log.e("dataList", dataList.toString())*/
        hitScanApi(deliveryId!!, barcode!!, vehicleNo!!, tokenValue!!, "1")
        //hitApi(this, tokenValue!!, deliveryId!!, deliveryQty!!)

        barcode?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    // Perform action on key press
                    //Log.e("hello", "helloooo")
                    hitScanApi(deliveryId!!, barcode!!, vehicleNo!!, tokenValue!!, "2")
                    /* if (UtilMethods.INSTANCE.isNetworkAvailable(this@ScanActivity)) {
                         *//* loader.show()
                         loader.setCancelable(false)
                         loader.setCanceledOnTouchOutside(false)*//*
                        hitScanApi(deliveryId!!, barcode!!, vehicleNo!!, tokenValue!!)
                        scanBarCode(deliveryId!!, barcode!!, vehicleNo!!, tokenValue!!)
                    } else {
                        Toast.makeText(
                            this@ScanActivity,
                            "No internet connection",
                            Toast.LENGTH_SHORT
                        ).show()

                    }*/
                    return true
                }
                return false
            }
        })


    }


    private fun scanBarCode(
        deliveryId: String,
        barcode: EditText,
        vehicleNumber: String,
        token: String,
        str: String
    ) {
        try {
            customLoader?.show()
            var barcodeValue = ""

            barcodeValue = if (str == "1") {
                "00000000_00000000"
            } else {
                barcode.text.toString().trim()
            }

            /* var barcodeValue = barcode.text.toString().trim().ifEmpty {
                 "00000000_00000000"
             }*/
            val req = ScanRequest(
                deliveryId.toInt(),
                barcodeValue,
                vehicleNumber,
                token
            )

         /*   Log.e("Data1", deliveryId)
            Log.e("Data2", barcodeValue)
            Log.e("Data3", vehicleNumber)
            Log.e("Data4", token)*/
            val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
            val call = serviceGenerator.scanBarcode(req)
            call.enqueue(object : retrofit2.Callback<BarcodeResponse> {
                override fun onResponse(
                    call: Call<BarcodeResponse>,
                    response: Response<BarcodeResponse>
                ) {
                    customLoader?.dismiss()
                    //Log.e("ScanResponse", "is : " + Gson().toJson(response.body()).toString())
                    if (response.isSuccessful) {
                        barcode.text.clear()
                        barcode.isCursorVisible = true
                        barcode.requestFocus()
                       // Log.e("ScanResponse1", "is : " + Gson().toJson(response.body()).toString())
                        if (response.body()?.status == 0 || response.body()?.status == 1) {

                            if (response.body()?.errorMessage != "") {
                                Toast.makeText(
                                    this@ScanActivity,
                                    response.body()?.errorMessage.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            if (response.body()?.scanData?.stockOutProductScans != null && response.body()?.scanData?.stockOutProductScans?.size!! > 0) {
                                noData?.visibility = View.GONE
                                recyclerView?.visibility = View.VISIBLE

                                val adapter =
                                    ScanUpdateAdapter(response.body()?.scanData?.stockOutProductScans!!)
                                recyclerView?.adapter = adapter
                            } else {
                                noData?.visibility = View.VISIBLE
                                recyclerView?.visibility = View.GONE
                            }

                            tvDelQuantity?.text =
                                (response.body()!!.scanData?.totalQty!! - response.body()!!.scanData?.totalScan!!).toString()
                            UtilMethods.INSTANCE.setDeliveryQty(
                                this@ScanActivity,
                                (response.body()!!.scanData?.totalQty!! - response.body()!!.scanData?.totalScan!!).toString()
                            )

                            if (response.body()!!.scanData?.totalQty == response.body()!!.scanData?.totalScan) {
                                sendDeliveryData(deliveryId, token)

                            }
                        } else {
                           // Log.e("ScanResponse", "No update")
                        }

                    }
                }

                override fun onFailure(call: Call<BarcodeResponse>, t: Throwable) {
                    t.printStackTrace()
                    customLoader?.dismiss()
                   // Log.e("failure:", t.message.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendDeliveryData(deliveryId: String, token: String) {
        if (UtilMethods.INSTANCE.isNetworkAvailable(this)) {
            try {
                customLoader?.show()
                val serviceGenerator = ServiceGenerator.buildService(ApiService::class.java)
                val call = serviceGenerator.sendDeliveryDetails(deliveryId, token)
                call.enqueue(object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        customLoader?.dismiss()
                        if (response.isSuccessful) {
                           /* Log.e(
                                "sendDeliveryDetails",
                                "is : " + Gson().toJson(response.body()).toString())*/
                            /*val intent = Intent(this@ScanActivity, DeliveryDetailsActivity::class.java)
                            startActivity(intent)*/
                            finish()
                            /*  if (response.body()?.status == 0) {


                                  Toast.makeText(
                                      this@ScanActivity,
                                      "Delivery data send successfully",
                                      Toast.LENGTH_SHORT
                                  ).show()
                              } else {
                                  Toast.makeText(
                                      this@ScanActivity,
                                      "Something went wrong",
                                      Toast.LENGTH_SHORT
                                  ).show()
                              }*/
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        customLoader?.dismiss()
                       // Log.e("failure:", t.message.toString())
                    }
                }
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(
                this,
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

    private fun hitScanApi(
        deliveryId: String,
        barcode: EditText,
        vehicleNo: String,
        token: String,
        str: String
    ) {
        if (UtilMethods.INSTANCE.isNetworkAvailable(this)) {
            scanBarCode(deliveryId!!, barcode, vehicleNo!!, token!!, str)
        } else {
            Toast.makeText(
                this,
                "No internet connection",
                Toast.LENGTH_SHORT
            ).show()

        }
    }
}