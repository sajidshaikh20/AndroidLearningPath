package com.base.hilt.ui.notifications.model

import com.fasterxml.jackson.annotation.JsonProperty

data class MobileDataItem(
    @JsonProperty("data")
    val `data`: Data?,
    @JsonProperty("id")
    val id: String?,
    @JsonProperty("name")
    val name: String?
)
