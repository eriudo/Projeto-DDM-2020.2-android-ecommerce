package br.edu.ufrn.android_ecommerce

import com.google.gson.annotations.SerializedName

data class Product (
        @SerializedName("description")
        var description: String? = null,

        @SerializedName("name")
        var name: String? = null,

        @SerializedName("price")
        var price: String? = null,

        @SerializedName("image")
        var image: String? = null
)