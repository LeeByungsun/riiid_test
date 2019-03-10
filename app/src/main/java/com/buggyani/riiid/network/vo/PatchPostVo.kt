package com.buggyani.riiid.network.vo

import com.google.gson.annotations.SerializedName

data class PatchPostVo(@SerializedName("title") var title: String,
                       @SerializedName("body") var body: String)