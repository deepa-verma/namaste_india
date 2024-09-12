package com.quinta.namasteindia.response

import com.google.gson.annotations.SerializedName

data class BarcodeResponse( @SerializedName("data"         ) var scanData         : ScanData?   = ScanData(),
                            @SerializedName("errorMessage" ) var errorMessage : String? = null,
                            @SerializedName("status"       ) var status       : Int?    = null)


data class StockOutProductScans (

    @SerializedName("id"                  ) var id                  : Int?    = null,
    @SerializedName("materialDescription" ) var materialDescription : String? = null,
    @SerializedName("scanCarteQty"        ) var scanCarteQty        : Int?    = null,
    @SerializedName("scanStatus"          ) var scanStatus          : String? = null,
    @SerializedName("totalCarteQty"       ) var totalCarteQty       : Int?    = null

)
data class ScanData (

    @SerializedName("delaviryid"           ) var delaviryid           : Int?                            = null,
    @SerializedName("stockOutProductScans" ) var stockOutProductScans : ArrayList<StockOutProductScans> = arrayListOf(),
    @SerializedName("totalQty"             ) var totalQty             : Int?                            = null,
    @SerializedName("totalScan"            ) var totalScan            : Int?                            = null

)
