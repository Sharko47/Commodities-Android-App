package com.carnot.commodities.data.model


import com.google.gson.annotations.SerializedName

data class TargetBucket(
    @SerializedName("field")
    val `field`: String?,
    @SerializedName("index")
    val index: String?,
    @SerializedName("type")
    val type: String?
)