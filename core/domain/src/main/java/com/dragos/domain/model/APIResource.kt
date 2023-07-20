package com.dragos.domain.model

import com.google.gson.annotations.SerializedName

data class APIResource(
    @SerializedName("url")
    val url: String
)