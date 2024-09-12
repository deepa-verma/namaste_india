package com.quinta.namasteindia.response

import com.google.gson.annotations.SerializedName

data class ProductDetailsResponse(
    @SerializedName("data") var data: ArrayList<ProductData> = arrayListOf(),
    @SerializedName("status") var status: Int? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null
)
data class ProductData (

    @SerializedName("productId"          ) var productId          : Int?    = null,
    @SerializedName("productCode"        ) var productCode        : String? = null,
    @SerializedName("productDescription" ) var productDescription : String? = null,
    @SerializedName("crateQty"           ) var crateQty           : String? = null,
    @SerializedName("cartonQty"          ) var cartonQty          : String? = null

)
