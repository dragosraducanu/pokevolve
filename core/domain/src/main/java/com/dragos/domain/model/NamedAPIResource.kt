package com.dragos.domain.model

import com.google.gson.annotations.SerializedName

data class NamedAPIResource(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String
)