package fi.lauriari.crypto_condition_android.api

import fi.lauriari.crypto_condition_android.models.BearishTrendInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoConditionApi {


    @GET("/currencyinfo/bearishtrend")
    suspend fun getBearishTrendInfo(
        @Query("startdate") startdate: String,
        @Query("enddate") enddate: String
    ): Response<BearishTrendInfo>

}