package com.quinta.namasteindia.utils

import DeliveryResponse
import com.quinta.namasteindia.request.ScanRequest
import com.quinta.namasteindia.response.BarcodeResponse
import com.quinta.namasteindia.response.LoginResponse
import com.quinta.namasteindia.response.ProductDetailsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("/api-ni/ProductMaster/userLogin")
    fun login(
        @Query("userId") userId: String,
        @Query("password") password: String
    ): Call<LoginResponse>

    @GET("/api-ni/ProductMaster/getDeliveryDetaisList")
    fun getDeliveryList(
        @Query("date") date: String,
        @Query("tocken") tocken: String
    ): Call<DeliveryResponse>

    @GET("/api-ni/ProductMaster/getProductDetaisList")
    fun getProductList(
        @Query("deliveryId") deliveryId: String,
        @Query("tocken") tocken: String
    ): Call<ProductDetailsResponse>



    @POST("api-ni/ProductMaster/scanBarcodeDetails")
    fun scanBarcode(@Body scanReq: ScanRequest): Call<BarcodeResponse>

    @GET("api-ni/ProductMaster/sendDelaviryDetails")
    fun sendDeliveryDetails(@Query("deliveryId") deliveryId: String,
                            @Query("tocken") tocken: String): Call<ResponseBody>
    @GET("/Data/StockOutBarcode")
    fun stockOutBarcode(@Query("Barcode ") barCode: String,
                            @Query("ActionVal ") deliveryId: String,
                            @Query("UserId ") userId: String,
                            @Query("VehicleNo ") vehicleNo: String): Call<ResponseBody>

}