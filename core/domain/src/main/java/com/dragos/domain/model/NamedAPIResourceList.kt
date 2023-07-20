package com.dragos.domain.model

import com.google.gson.annotations.SerializedName

data class NamedAPIResourceList(
    @SerializedName("count")
    val count: Int,

    @SerializedName("next")
    val next: String?,

    @SerializedName("prev")
    val prev: String?,

    @SerializedName("results")
    val results: List<NamedAPIResource>
)