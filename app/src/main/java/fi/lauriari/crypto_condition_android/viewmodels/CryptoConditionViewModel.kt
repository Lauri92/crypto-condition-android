package fi.lauriari.crypto_condition_android.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.lauriari.crypto_condition_android.models.BearishTrendInfo
import fi.lauriari.crypto_condition_android.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class CryptoConditionViewModel : ViewModel() {

    private val cryptoConditionRepository = Repository()

    var bearishTrendInfo: MutableLiveData<Response<BearishTrendInfo>> = MutableLiveData()
    var requestFail: MutableLiveData<String> = MutableLiveData()

    fun getBearishTrendInfo(startdate: String, enddate: String) {
        viewModelScope.launch {
            val info = cryptoConditionRepository.getBearishTrendInfo(startdate, enddate)
            bearishTrendInfo.value = info
        }
    }

}