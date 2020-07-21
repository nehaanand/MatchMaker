package neha.matchmaker.model


import com.google.gson.annotations.SerializedName

data class Id(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    var value: String
)