package com.carnot.commodities.data.api.helper

import com.carnot.commodities.data.api.ApiService
import javax.inject.Inject

/**
 * Remote Data Source class with all the suspend function which can only be triggered using Coroutine on the independent background thread
 * @param apiService Rest Api Implementation with all the endpoints required for this project
 */
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getLatestCommodityList(apiKey: String, offset: Int, filter: String = "") =
        apiService.getLatestCommodityList(apiKey = apiKey, offset = offset, filter = filter)
}