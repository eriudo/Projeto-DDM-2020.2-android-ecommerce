package br.edu.ufrn.android_ecommerce

import com.google.gson.annotations.SerializedName

data class Photo (
        @SerializedName("filename")
        var filename: String? = null
)