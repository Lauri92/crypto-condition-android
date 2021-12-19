package fi.lauriari.crypto_condition_android.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.lauriari.crypto_condition_android.models.CryptoCondition
import fi.lauriari.crypto_condition_android.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class CryptoConditionViewModel : ViewModel() {

    private val cryptoConditionRepository = Repository()

    var cryptoConditionInfo: MutableLiveData<Response<CryptoCondition>> = MutableLiveData()
    var requestFail: MutableLiveData<Boolean> = MutableLiveData()

    fun getCryptoCondition(startdate: Long, enddate: Long) {
        viewModelScope.launch {
            try {
                val info = cryptoConditionRepository.getCryptoCondition(startdate, enddate)
                cryptoConditionInfo.value = info
            } catch (e: Exception) {
                requestFail.value = true
                Log.d("exception", e.toString())
            }
        }
    }

}