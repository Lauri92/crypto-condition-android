package fi.lauriari.crypto_condition_android.repository

import android.util.Log
import fi.lauriari.crypto_condition_android.api.RetrofitInstance
import fi.lauriari.crypto_condition_android.models.CryptoCondition
import retrofit2.Response

class Repository {

    suspend fun getCryptoCondition(
        startdate: Long,
        enddate: Long
    ): Response<CryptoCondition> {
        return RetrofitInstance.api.getCryptoCondition(startdate, enddate)
    }
}