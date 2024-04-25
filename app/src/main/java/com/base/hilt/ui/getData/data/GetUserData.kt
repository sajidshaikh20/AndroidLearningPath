package com.base.hilt.ui.getData.data


import com.fasterxml.jackson.annotation.JsonProperty

data class GetUserData(
    @JsonProperty("address")
    val address: Address? =null,
    @JsonProperty("age")
    val age: Int? =null,
    @JsonProperty("bank")
    val bank: Bank? =null,
    @JsonProperty("birthDate")
    val birthDate: String? =null,
    @JsonProperty("bloodGroup")
    val bloodGroup: String? =null,
    @JsonProperty("company")
    val company: Company? =null,
    @JsonProperty("crypto")
    val crypto: Crypto? =null,
    @JsonProperty("domain")
    val domain: String? =null,
    @JsonProperty("ein")
    val ein: String? =null,
    @JsonProperty("email")
    val email: String? =null,
    @JsonProperty("eyeColor")
    val eyeColor: String? =null,
    @JsonProperty("firstName")
    val firstName: String? =null,
    @JsonProperty("gender")
    val gender: String? =null,
    @JsonProperty("hair")
    val hair: Hair? =null,
    @JsonProperty("height")
    val height: Int? =null,
    @JsonProperty("id")
    val id: Int? =null,
    @JsonProperty("image")
    val image: String? =null,
    @JsonProperty("ip")
    val ip: String? =null,
    @JsonProperty("lastName")
    val lastName: String? =null,
    @JsonProperty("macAddress")
    val macAddress: String? =null,
    @JsonProperty("maidenName")
    val maidenName: String? =null,
    @JsonProperty("password")
    val password: String? =null,
    @JsonProperty("phone")
    val phone: String? =null,
    @JsonProperty("ssn")
    val ssn: String? =null,
    @JsonProperty("university")
    val university: String? =null,
    @JsonProperty("userAgent")
    val userAgent: String? =null,
    @JsonProperty("username")
    val username: String? =null,
    @JsonProperty("weight")
    val weight: Double? =null
) {
    data class Address(
        @JsonProperty("address")
        val address: String? =null,
        @JsonProperty("city")
        val city: String? =null,
        @JsonProperty("coordinates")
        val coordinates: Coordinates? =null,
        @JsonProperty("postalCode")
        val postalCode: String? =null,
        @JsonProperty("state")
        val state: String? =null
    ) {
        data class Coordinates(
            @JsonProperty("lat")
            val lat: Double? =null,
            @JsonProperty("lng")
            val lng: Double? =null
        )
    }

    data class Bank(
        @JsonProperty("cardExpire")
        val cardExpire: String? =null,
        @JsonProperty("cardNumber")
        val cardNumber: String? =null,
        @JsonProperty("cardType")
        val cardType: String? =null,
        @JsonProperty("currency")
        val currency: String? =null,
        @JsonProperty("iban")
        val iban: String? =null
    )

    data class Company(
        @JsonProperty("address")
        val address: Address? =null,
        @JsonProperty("department")
        val department: String? =null,
        @JsonProperty("name")
        val name: String? =null,
        @JsonProperty("title")
        val title: String? =null
    ) {
        data class Address(
            @JsonProperty("address")
            val address: String? =null,
            @JsonProperty("city")
            val city: String? =null,
            @JsonProperty("coordinates")
            val coordinates: Coordinates? =null,
            @JsonProperty("postalCode")
            val postalCode: String? =null,
            @JsonProperty("state")
            val state: String? =null
        ) {
            data class Coordinates(
                @JsonProperty("lat")
                val lat: Double? =null,
                @JsonProperty("lng")
                val lng: Double? =null
            )
        }
    }

    data class Crypto(
        @JsonProperty("coin")
        val coin: String? =null,
        @JsonProperty("network")
        val network: String? =null,
        @JsonProperty("wallet")
        val wallet: String? =null
    )

    data class Hair(
        @JsonProperty("color")
        val color: String? =null,
        @JsonProperty("type")
        val type: String? =null
    )
}