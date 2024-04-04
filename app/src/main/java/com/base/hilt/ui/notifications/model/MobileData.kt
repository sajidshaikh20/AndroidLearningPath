package com.base.hilt.ui.notifications.model


import com.fasterxml.jackson.annotation.JsonProperty

class MobileData : ArrayList<MobileData.MobileDataItem>(){
    data class MobileDataItem(
        @JsonProperty("data")
        val `data`: Data?=null,
        @JsonProperty("id")
        val id: String?=null,
        @JsonProperty("name")
        val name: String?=null
    ) {
        data class Data(
            @JsonProperty("CPU model")
            val cPUModel: String?=null,
            @JsonProperty("capacity")
            val capacity: String?=null,
            @JsonProperty("Capacity")
            val capacity2: String?=null,
            @JsonProperty("capacity GB")
            val capacityGB: Int?=null,
            @JsonProperty("Case Size")
            val caseSize: String?=null,
            @JsonProperty("color")
            val color: String?=null,
            @JsonProperty("Color")
            val color2: String?=null,
            @JsonProperty("Description")
            val description: String?=null,
            @JsonProperty("generation")
            val generation: String?=null,
            @JsonProperty("Generation")
            val generation2: String?=null,
            @JsonProperty("Hard disk size")
            val hardDiskSize: String?=null,
            @JsonProperty("price")
            val price: Double?=null,
            @JsonProperty("Price")
            val price2: String?=null,
            @JsonProperty("Screen size")
            val screenSize: Double?=null,
            @JsonProperty("Strap Colour")
            val strapColour: String?=null,
            @JsonProperty("year")
            val year: Int?=null
        )
    }
}