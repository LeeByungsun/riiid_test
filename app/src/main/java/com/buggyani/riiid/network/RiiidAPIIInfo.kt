package com.buggyani.test.network

import com.buggyani.riiid.network.vo.CommentVo
import com.buggyani.riiid.network.vo.PatchPostVo
import com.buggyani.riiid.network.vo.PostVo
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by bslee on 2019-3-10
 */

interface RiiidAPIIInfo {

    @GET("posts")
    fun getPosts(@Query(value = "_start") start: Int, @Query(value = "_limit") limit: Int): Observable<ArrayList<PostVo>>

    @GET("posts/{id}")
    fun getPostsDetail(@Path("id") id: Int): Observable<PostVo>

    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Observable<Response<Void>>

    @GET("posts/{id}/comments")
    fun getPostsComment(@Path("id") id: Int): Observable<ArrayList<CommentVo>>

    @PATCH("posts/{id}")
    fun setPost(@HeaderMap headers: MutableMap<String, String?>, @Path("id") id: Int, @Body data: PatchPostVo): Observable<PostVo>

    companion object {
        //server Url
        val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}
