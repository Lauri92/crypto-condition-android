package fi.lauriari.crypto_condition_android.api

import fi.lauriari.crypto_condition_android.constants.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: CryptoConditionApi by lazy {
        retrofit.create(CryptoConditionApi::class.java)
    }
}