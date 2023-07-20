package com.dragos.domain.model

import com.dragos.domain.util.UrlUtils

val NamedAPIResource.id: Int?
    get() = UrlUtils.getIdFromUrl(url)