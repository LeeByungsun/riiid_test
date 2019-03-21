package com.buggyani.riiid.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.widget.Toast
import com.buggyani.riiid.R
import com.buggyani.riiid.RiiidApplication
import com.buggyani.riiid.network.vo.PostVo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val mApplication: Application = application;
    private val TAG = javaClass.simpleName
    private var disposable: Disposable? = null
    var posts = MutableLiveData<ArrayList<PostVo>>()
    var postList = ArrayList<PostVo>()
    var mStart = 0;


    fun getPosts(): LiveData<ArrayList<PostVo>> {
        return posts
    }

    fun setPosts(data: LiveData<ArrayList<PostVo>>) {
        posts = data as MutableLiveData
    }

    fun getPostsList() {
        Log.e(TAG, "mIndex = $mStart")
        disposable = RiiidApplication.riiid_api_Server.getPosts(mStart, 15)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: ArrayList<PostVo>? ->

                    when (t!!.size) {
                        0 -> {
                            Log.e(TAG, "t size = ${t!!.size}")
                            Toast.makeText(mApplication, mApplication.getString(R.string.nodata), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            t!!.forEach {
                                postList.add(it)
                            }
                            posts.postValue(postList)
                            mStart += 15
                            Log.e(TAG, "mIndex = $mStart")
//                            mPostDataAdapter.notifyDataSetChanged()
                        }
                    }

                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }
}