package com.buggyani.riiid.network.vo

import com.google.gson.annotations.SerializedName

/**
 * Comment Data class
 */
data class CommentVo(@SerializedName("postId") var postId: Int,
                     @SerializedName("id") var id: Int,
                     @SerializedName("name") var name: String,
                     @SerializedName("email") var email: String,
                     @SerializedName("body") var body: String)