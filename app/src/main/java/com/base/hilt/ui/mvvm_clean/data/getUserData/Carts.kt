package com.base.hilt.ui.mvvm_clean.data.getUserData


import com.fasterxml.jackson.annotation.JsonProperty

data class Carts(
    @JsonProperty("carts")
    val carts: List<Cart?>?=null,
    @JsonProperty("limit")
    val limit: Int?=null,
    @JsonProperty("skip")
    val skip: Int?=null,
    @JsonProperty("total")
    val total: Int?=null
) {
    data class Cart(
        @JsonProperty("discountedTotal")
        val discountedTotal: Int?=null,
        @JsonProperty("id")
        val id: Int?=null,
        @JsonProperty("products")
        val products: List<Product?>?=null,
        @JsonProperty("total")
        val total: Int?=null,
        @JsonProperty("totalProducts")
        val totalProducts: Int?=null,
        @JsonProperty("totalQuantity")
        val totalQuantity: Int?=null,
        @JsonProperty("userId")
        val userId: Int?=null
    ) {
        data class Product(
            @JsonProperty("discountPercentage")
            val discountPercentage: Double?=null,
            @JsonProperty("discountedPrice")
            val discountedPrice: Int?=null,
            @JsonProperty("id")
            val id: Int?=null,
            @JsonProperty("price")
            val price: Int?=null,
            @JsonProperty("quantity")
            val quantity: Int?=null,
            @JsonProperty("thumbnail")
            val thumbnail: String?=null,
            @JsonProperty("title")
            val title: String?=null,
            @JsonProperty("total")
            val total: Int?=null
        )
    }
}