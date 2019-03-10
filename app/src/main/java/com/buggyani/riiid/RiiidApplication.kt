package com.buggyani.riiid

import android.app.Application
import android.content.Context
import com.buggyani.test.network.RiiidAPIIInfo
import com.buggyani.test.util.BPreference
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by bslee on 2019-03-10.
 */
class RiiidApplication : Application() {
    private val TAG = javaClass.simpleName
    private var mPref: BPreference? = null
    private var context: Context? = null


    override fun onCreate() {
        super.onCreate()
        instance = this
        this.context = applicationContext
        setRetrofit_Server(true)
        mPref = BPreference.getInstance(instance!!.applicationContext)
    }

    /**
     * retrofit setting
     */
    private fun setRetrofit_Server(debug: Boolean) {


        retrofit_Server = if (debug) {
            val httpClient = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY
//        logging.level = HttpLoggingInterceptor.Level.HEADERS
            httpClient.addInterceptor(logging)
            Retrofit.Builder().baseUrl(RiiidAPIIInfo.BASE_URL)
                    .client(httpClient.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()).build()
        } else {
            Retrofit.Builder().baseUrl(RiiidAPIIInfo.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create()).build()
        }

        riiid_api_Server = retrofit_Server!!.create(RiiidAPIIInfo::class.java)
    }


    companion object {

        var instance: RiiidApplication? = null
            private set
        var retrofit_Server: Retrofit? = null
        lateinit var riiid_api_Server: RiiidAPIIInfo
        var mHeaders: MutableMap<String, String?> = mutableMapOf()
    }

}
