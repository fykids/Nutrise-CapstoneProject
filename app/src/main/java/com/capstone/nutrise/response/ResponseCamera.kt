package com.capstone.nutrise.response

import com.google.gson.annotations.SerializedName

data class ResponseCamera(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
