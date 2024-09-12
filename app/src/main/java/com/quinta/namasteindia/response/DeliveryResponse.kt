import com.google.gson.annotations.SerializedName

data class DeliveryResponse(
    @SerializedName("data") var data: ArrayList<DeliveryData> = arrayListOf(),
    @SerializedName("status") var status: Int? = null,
    @SerializedName("errorMessage") var errorMessage: String? = null
)

data class DeliveryData(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("deliveryDocNo") var deliveryDocNo: String? = null,
    @SerializedName("lineIteamNo") var lineIteamNo: Int? = null,
    @SerializedName("vehicleNo") var vehicleNo: String? = null,
    @SerializedName("deliveryStatus") var deliveryStatus: String? = null,
    @SerializedName("deliverQty") var deliverQty: Int? = null

)
