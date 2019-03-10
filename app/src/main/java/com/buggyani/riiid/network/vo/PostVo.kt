package com.buggyani.riiid.network.vo

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostVo(@SerializedName("userId") var usderId: Int,
                  @SerializedName("id") var id: Int,
                  @SerializedName("title") var title: String,
                  @SerializedName("body") var body: String) : Serializable