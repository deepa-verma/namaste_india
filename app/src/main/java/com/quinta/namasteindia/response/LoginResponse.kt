package com.quinta.namasteindia.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("data"         ) var data         : Data?   = Data(),
    @SerializedName("status"       ) var status       : Int?    = null,
    @SerializedName("errorMessage" ) var errorMessage : String? = null

)


data class Data (

    @SerializedName("status" ) var status : String? = null,
    @SerializedName("tocken" ) var tocken : String? = null

)

