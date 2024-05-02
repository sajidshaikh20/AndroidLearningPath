package com.base.hilt.ui.notifications.model

import com.fasterxml.jackson.annotation.JsonProperty

data class Data(
    @JsonProperty("CPU model")
    val cPUModel: String?,
    @JsonProperty("capacity")
    val capacity2: String?,
    @JsonProperty("Capacity")
    var capacity: String?,
    @JsonProperty("capacity GB")
    val capacityGB: Int?,
    @JsonProperty("Case Size")
    val caseSize: String?,
    @JsonProperty("color")
    val color: String?,
    @JsonProperty("Color")
    val color2: String?,
    @JsonProperty("Description")
    val description: String?,
    @JsonProperty("generation")
    val generation2: String?,
    @JsonProperty("Generation")
    var generation: String?,
    @JsonProperty("Hard disk size")
    val hardDiskSize: String?,
    @JsonProperty("price")
    val price2: Double?,
    @JsonProperty("Price")
    var price: String? = null,
    @JsonProperty("Screen size")
    val screenSize: Double?,
    @JsonProperty("Strap Colour")
    val strapColour: String?,
    @JsonProperty("year")
    val year: Int?
){

    init {
        price()
    }

    private fun price(){
        if (price==null || price2!=null){
            price = price2.toString()
        }
    }

}