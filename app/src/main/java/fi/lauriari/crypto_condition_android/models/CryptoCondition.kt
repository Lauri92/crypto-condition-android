package fi.lauriari.crypto_condition_android.models

import com.google.gson.annotations.SerializedName


data class CryptoCondition(

    @SerializedName("bearishTrend") val bearishTrend: BearishTrend,
    @SerializedName("highestVolume") val highestVolume: HighestVolume,
    @SerializedName("timeMachine") val timeMachine: TimeMachine
)

data class BearishTrend(

    @SerializedName("length") val length: Int,
    @SerializedName("startDate") val startDate: Long,
    @SerializedName("endDate") val endDate: Long
)

data class HighestVolume(

    @SerializedName("date") val date: Long,
    @SerializedName("volume") val volume: Double
)

data class TimeMachine(

    @SerializedName("bestDayToSell") val bestDayToSell: BestDayToSell,
    @SerializedName("bestDayToBuy") val bestDayToBuy: BestDayToBuy
)

data class BestDayToBuy(

    @SerializedName("date") val date: Long?,
    @SerializedName("price") val price: Double?
)


data class BestDayToSell(

    @SerializedName("date") val date: Long?,
    @SerializedName("price") val price: Double?
)