package fi.lauriari.crypto_condition_android.repository

import fi.lauriari.crypto_condition_android.api.RetrofitInstance
import fi.lauriari.crypto_condition_android.models.BearishTrendInfo
import retrofit2.Response

class Repository {

    suspend fun getBearishTrendInfo(
        startdate: String,
        enddate: String
    ): Response<BearishTrendInfo> {
        return RetrofitInstance.api.getBearishTrendInfo(startdate, enddate)
    }
}