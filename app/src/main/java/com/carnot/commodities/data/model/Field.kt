package com.carnot.commodities.data.model


import com.google.gson.annotations.SerializedName

data class Field(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?
)