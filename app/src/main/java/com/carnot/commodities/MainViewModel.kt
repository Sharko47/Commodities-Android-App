package com.carnot.commodities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carnot.commodities.data.api.helper.NetworkResult
import com.carnot.commodities.data.api.repository.MainRepository
import com.carnot.commodities.data.model.CommoditiesResponse
import com.carnot.commodities.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _response: MutableLiveData<NetworkResult<CommoditiesResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<CommoditiesResponse>> = _response

    var commoditiesResponse: CommoditiesResponse? = null

    var totalDataCount = 0
    var currentPage = 0

    fun getLatestCommodityList(
        filter: String = ""
    ) = viewModelScope.launch {
        _response.value = NetworkResult.Loading()
        mainRepository.getLatestCommodityList(Constants.API_KEY, currentPage, filter).collect {
            _response.value = handleCommoditiesResponse(it)
        }
    }

    private fun handleCommoditiesResponse(response: NetworkResult<CommoditiesResponse>): NetworkResult<CommoditiesResponse> {
        response.let {
            currentPage++
            if (commoditiesResponse == null) {
                commoditiesResponse = it.data
                return NetworkResult.Success(commoditiesResponse!!)
            } else {
                val oldDataList = commoditiesResponse?.records
                val newDataList = it.data?.records
                if (newDataList != null) {
                    oldDataList?.addAll(newDataList)
//                    Log.i("ViewModel","Returning Newly added Response")
//                    Log.i("OLD", oldDataList.toString())
//                    Log.i("DATA", it.data.records.toString())
//                    Log.i("OLD DATA", commoditiesResponse?.records.toString())
//                    if (oldDataList != null) {
//                        commoditiesResponse?.records?.addAll(oldDataList)
//                    }

                    return NetworkResult.Success(commoditiesResponse ?: it.data)
                } else {
                    NetworkResult.Success(commoditiesResponse)
                }
            }
        }
        return NetworkResult.Error(message = "Error loading data")
    }
}