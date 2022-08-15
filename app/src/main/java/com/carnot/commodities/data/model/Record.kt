package com.carnot.commodities.data.model


import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("arrival_date")
    val arrivalDate: String?,
    @SerializedName("commodity")
    val commodity: String?,
    @SerializedName("district")
    val district: String?,
    @SerializedName("market")
    val market: String?,
    @SerializedName("max_price")
    val maxPrice: String?,
    @SerializedName("min_price")
    val minPrice: String?,
    @SerializedName("modal_price")
    val modalPrice: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("variety")
    val variety: String?
)