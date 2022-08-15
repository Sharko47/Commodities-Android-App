package com.carnot.commodities.data.model


import com.google.gson.annotations.SerializedName

data class CommoditiesResponse(
    @SerializedName("active")
    val active: String?,
    @SerializedName("catalog_uuid")
    val catalogUuid: String?,
    @SerializedName("count")
    val count: Int?,
    @SerializedName("created")
    val created: Int?,
    @SerializedName("created_date")
    val createdDate: String?,
    @SerializedName("desc")
    val desc: String?,
    @SerializedName("external_ws")
    val externalWs: Int?,
    @SerializedName("external_ws_url")
    val externalWsUrl: String?,
    @SerializedName("field")
    val `field`: List<Field>?,
    @SerializedName("index_name")
    val indexName: String?,
    @SerializedName("limit")
    val limit: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("offset")
    val offset: String?,
    @SerializedName("org")
    val org: List<String>?,
    @SerializedName("org_type")
    val orgType: String?,
    @SerializedName("records")
    val records: MutableList<Record>?,
    @SerializedName("sector")
    val sector: List<String>?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("target_bucket")
    val targetBucket: TargetBucket?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("updated")
    val updated: Int?,
    @SerializedName("updated_date")
    val updatedDate: String?,
    @SerializedName("version")
    val version: String?,
    @SerializedName("visualizable")
    val visualizable: String?
)