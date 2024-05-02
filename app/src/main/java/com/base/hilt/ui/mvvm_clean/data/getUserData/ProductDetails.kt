package com.base.hilt.ui.mvvm_clean.data.getUserData


import com.fasterxml.jackson.annotation.JsonProperty
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDetails(
    @JsonProperty("brand")
    val brand: String? = null,
    @JsonProperty("category")
    val category: String? = null,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("discountPercentage")
    val discountPercentage: Double? = null,
    @JsonProperty("id")
    val id: Int? = null,
    @JsonProperty("images")
    val images: List<String>? = null,
    @JsonProperty("price")
    val price: Int? = null,
    @JsonProperty("rating")
    val rating: Double? = null,
    @JsonProperty("stock")
    val stock: Int? = null,
    @JsonProperty("thumbnail")
    val thumbnail: String? = null,
    @JsonProperty("title")
    val title: String? = null
): Parcelable