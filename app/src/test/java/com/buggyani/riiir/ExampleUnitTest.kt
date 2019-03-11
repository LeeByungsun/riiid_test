package com.buggyani.riiid

import android.app.Application
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.buggyani.riiid.RiiidApplication
import com.buggyani.riiid.RiiidApplication.Companion.mHeaders
import com.buggyani.riiid.RiiidApplication.Companion.riiid_api_Server
import com.buggyani.riiid.network.RiiidAPIIInfo
import com.buggyani.riiid.network.vo.CommentVo
import com.buggyani.riiid.network.vo.PatchPostVo
import com.buggyani.riiid.network.vo.PostVo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {

    var application = RiiidApplication()
    @Before
    fun init() {
        application.setRetrofitServer(true)
        RiiidApplication.riiid_api_Server = RiiidApplication.retrofit_Server!!.create(RiiidAPIIInfo::class.java)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun getPosts() {
        riiid_api_Server.getPosts(0, 30)
                .subscribe({ t: ArrayList<PostVo>? ->
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    @Test
    fun deletePost() {
        riiid_api_Server.deletePost(3)
                .subscribe({
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    @Test
    fun getPostDetail() {
        riiid_api_Server.getPostsDetail(3)
                .subscribe({ t: PostVo ->
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    @Test
    fun getPostComment() {
        riiid_api_Server.getPostsComment(4)
                .subscribe({ t: ArrayList<CommentVo>? ->
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    @Test
    fun setPatchPost() {
        mHeaders[GlobalStatic.HEADER_CONTENTTYPE] = "application/json"
        mHeaders[GlobalStatic.HEADER_CHARSET] = "utf-8"
        val newData = PatchPostVo("123456", "testtest")
        riiid_api_Server.setPost(mHeaders, 2, newData)
                .subscribe({ t: PostVo ->
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

}