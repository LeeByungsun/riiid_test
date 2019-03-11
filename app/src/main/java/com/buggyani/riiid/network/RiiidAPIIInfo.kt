package com.buggyani.riiid.network

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

    //posts 가져오기
    @GET("posts")
    fun getPosts(@Query(value = "_start") start: Int, @Query(value = "_limit") limit: Int): Observable<ArrayList<PostVo>>

    //post 상세 가져오기
    @GET("posts/{id}")
    fun getPostsDetail(@Path("id") id: Int): Observable<PostVo>

    //post 삭제
    @DELETE("posts/{id}")
    fun deletePost(@Path("id") id: Int): Observable<Response<Void>>

    //post 상세 commnets 가져오기
    @GET("posts/{id}/comments")
    fun getPostsComment(@Path("id") id: Int): Observable<ArrayList<CommentVo>>

    //post 수정
    @PATCH("posts/{id}")
    fun setPost(@HeaderMap headers: MutableMap<String, String?>, @Path("id") id: Int, @Body data: PatchPostVo): Observable<PostVo>

    companion object {
        //server Url
        val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}
