package fi.lauriari.crypto_condition_android.api

import fi.lauriari.crypto_condition_android.models.CryptoCondition
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoConditionApi {


    @GET("/currencyinfo/allinfo")
    suspend fun getCryptoCondition(
        @Query("startdate") startdate: Long,
        @Query("enddate") enddate: Long
    ): Response<CryptoCondition>

}