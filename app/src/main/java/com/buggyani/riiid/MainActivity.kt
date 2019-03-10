package com.buggyani.riiid

import android.app.AlertDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AbsListView
import com.buggyani.riiid.RiiidApplication.Companion.riiid_api_Server
import com.buggyani.riiid.adapter.RecyclerItemClickListenr
import com.buggyani.riiid.databinding.ActivityMainBinding
import com.buggyani.riiid.network.vo.PostVo
import com.buggyani.test.adapter.PostsListAdapter
import com.buggyani.test.util.BPreference
import com.buggyani.test.util.GlobalStatic.POST_DATA
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private var mPref: BPreference? = null
    private lateinit var binding: ActivityMainBinding
    private var mPostsData = ObservableArrayList<PostVo>()
    private lateinit var mPostDataAdapter: PostsListAdapter
    private val mCtx: MainActivity
        get() = this

    private var mStart: Int = 0

    private var disposable: Disposable? = null

    private var isScrolling = false
    private var currentItem = 0
    private var totalItem = 0
    private var scrollOutItems = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        intiUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun onResume() {
        super.onResume()
        if (mPostsData.size != 0) {
            mStart = 0;
            mPostsData.clear()
            getPostsList()
        }
    }

    init {
        mPref = BPreference.getInstance(this)
    }

    private fun intiUI() {
        initRecyclerView(mPostsData)
        getPostsList()
    }

    private fun initRecyclerView(data: ObservableArrayList<PostVo>) {
        mPostDataAdapter = PostsListAdapter(data)
        posts_recyclerview.adapter = mPostDataAdapter
        posts_recyclerview.layoutManager = LinearLayoutManager(applicationContext)
        posts_recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                Log.e(TAG, "State = $newState")
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manager: LinearLayoutManager = posts_recyclerview.layoutManager as LinearLayoutManager
                currentItem = manager.childCount
                totalItem = manager.itemCount
                scrollOutItems = manager.findFirstVisibleItemPosition()

                Log.e(TAG, "currentItem = $currentItem")
                Log.e(TAG, "scrollOutItems = $scrollOutItems")
                Log.e(TAG, "totalItem = $totalItem")
                if (isScrolling && (currentItem + scrollOutItems == totalItem)) {
                    Log.e(TAG, "Bottom")
                    isScrolling = false
                    getPostsList()
                }


            }
        })
        posts_recyclerview.addOnItemTouchListener(RecyclerItemClickListenr(this, posts_recyclerview, object : RecyclerItemClickListenr.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.e(TAG, "onItemClick = $position")
                detailPost(position)
            }

            override fun onItemLongClick(view: View?, position: Int) {
                Log.e(TAG, "onItemLongClick = $position")
                deleteDialog(position)
            }
        }))
    }


    private fun getPostsList() {
        Log.e(TAG, "mIndex = $mStart")
        disposable = riiid_api_Server.getPosts(mStart, 30)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: ArrayList<PostVo>? ->
                    t!!.forEach {
                        mPostsData.add(it)
                        it.run {
                            Log.e(TAG, "-----------------------------------")
                            Log.e(TAG, "title = $title")
                            Log.e(TAG, "body = $body")
                            Log.e(TAG, "usderId = $usderId")
                            Log.e(TAG, "id = $id")
                            Log.e(TAG, "-----------------------------------")
                        }
                    }
                    mStart = mStart + 30
                    Log.e(TAG, "mIndex = $mStart")
                    mPostDataAdapter.notifyDataSetChanged()
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    fun deleteDialog(positon: Int) {
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle(getString(R.string.delete))
        builder.setMessage(getString(R.string.delete_sure))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            deletePost(positon)

        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun deletePost(id: Int) {
        Log.e(TAG, "mIndex = $mStart")
        disposable = riiid_api_Server.deletePost(id + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e(TAG, "deleteOK")
                    mPostDataAdapter.removeAt(id)
                    mPostDataAdapter.notifyDataSetChanged()
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    fun detailPost(id: Int) {
        disposable = riiid_api_Server.getPostsDetail(id + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: PostVo ->
                    t.run {
                        Log.e(TAG, "title = $title")
                        Log.e(TAG, "body = $body")
                        Log.e(TAG, "usderId = $usderId")
                        Log.e(TAG, "id = $id")
                    }
                    sendActivity(t)
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    fun sendActivity(data: PostVo) {
        intent = Intent(this, DetailViewActivity::class.java)
        intent.putExtra(POST_DATA, data)
        startActivity(intent)
    }


}
