package com.carnot.commodities.data.api.repository

import com.carnot.commodities.data.api.helper.BaseApiResponse
import com.carnot.commodities.data.api.helper.NetworkResult
import com.carnot.commodities.data.api.helper.RemoteDataSource
import com.carnot.commodities.data.model.CommoditiesResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class MainRepository @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    BaseApiResponse() {
    suspend fun getLatestCommodityList(
        apiKey: String,
        offset: Int,
        filter: String = ""
    ): Flow<NetworkResult<CommoditiesResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getLatestCommodityList(apiKey, offset, filter) })
        }.flowOn(Dispatchers.IO)
    }
}