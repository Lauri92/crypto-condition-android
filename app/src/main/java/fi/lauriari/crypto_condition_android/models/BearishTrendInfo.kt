package fi.lauriari.crypto_condition_android.models

import com.google.gson.annotations.SerializedName

data class BearishTrendInfo(
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Int,
)