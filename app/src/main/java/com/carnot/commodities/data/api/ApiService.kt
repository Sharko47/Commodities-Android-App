package com.carnot.commodities.data.api

import com.carnot.commodities.data.model.CommoditiesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("9ef84268-d588-465a-a308-a864a43d0070")
    suspend fun getLatestCommodityList(
        @Query("api-key") apiKey: String,
        @Query("offset") offset: Int,//Page no.
        @Query("format") dataFormat: String = "json",
        @Query("limit") limit: Int = 10,
        @Query("filters", encoded = false) filter : String = ""
    ): Response<CommoditiesResponse>
}