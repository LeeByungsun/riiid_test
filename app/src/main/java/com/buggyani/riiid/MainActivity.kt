package com.buggyani.riiid

import android.app.AlertDialog
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.buggyani.riiid.GlobalStatic.POST_DATA
import com.buggyani.riiid.RiiidApplication.Companion.riiid_api_Server
import com.buggyani.riiid.adapter.PostsListAdapter
import com.buggyani.riiid.adapter.RecyclerItemClickListener
import com.buggyani.riiid.databinding.ActivityMainBinding
import com.buggyani.riiid.network.vo.PostVo
import com.buggyani.riiid.viewmodel.PostViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityMainBinding
    //    private var mPostsData = ObservableArrayList<PostVo>()
    private lateinit var mPostDataAdapter: PostsListAdapter

    private var mStart: Int = 0
    private var disposable: Disposable? = null
    private var isScrolling = false
    private var currentItem = 0
    private var totalItem = 0
    private var scrollOutItems = 0
    private lateinit var listViewModel: PostViewModel

    private val mCtx: MainActivity
        get() = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        listViewModel = PostViewModel(this)

        intiUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    override fun onResume() {
        super.onResume()
//        if (mPostsData.size != 0) {
//            mStart = 0
//            mPostsData.clear()
//            getPostsList()
//        }
    }

    private fun intiUI() {
//        initRecyclerView(mPostsData)

        listViewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)


        listViewModel.getPostsList()
        listViewModel.posts.observe(this, Observer { t ->
            initRecyclerView(t!!)
//            mPostDataAdapter.notifyDataSetChanged()
        })

//        getPostsList()
    }

    /**
     * init recyclerView
     */
//    private fun initRecyclerView(data: ObservableArrayList<PostVo>) {
    private fun initRecyclerView(data: ArrayList<PostVo>) {
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
                    listViewModel.getPostsList()
//                    mPostDataAdapter.notifyDataSetChanged()
//                    listViewModel.getPosts()
                }


            }
        })
        posts_recyclerview.addOnItemTouchListener(RecyclerItemClickListener(this, posts_recyclerview, object : RecyclerItemClickListener.OnItemClickListener {
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


    /**
     * get Posts 30개씩
     */
    /*fun getPostsList() {
        Log.e(TAG, "mIndex = $mStart")
        disposable = riiid_api_Server.getPosts(mStart, 30)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: ArrayList<PostVo>? ->

                    when (t!!.size) {
                        0 -> {
                            Log.e(TAG, "t size = ${t!!.size}")
                            Toast.makeText(mCtx, getString(R.string.nodata), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
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
                            mStart += 30
                            Log.e(TAG, "mIndex = $mStart")
                            mPostDataAdapter.notifyDataSetChanged()
                        }
                    }

                }, { t: Throwable? -> t!!.printStackTrace() })
    }*/

    /**
     *  delete Post Dialog
     */
    fun deleteDialog(position: Int) {
        val builder = AlertDialog.Builder(mCtx)

        builder.setTitle(getString(R.string.delete))
        builder.setMessage(getString(R.string.delete_sure))

        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            deletePost(position)

        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /**
     * delete post
     */
    private fun deletePost(id: Int) {
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

    /**
     * get post detail data
     */
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

    /**
     * send detail activity
     */
    private fun sendActivity(data: PostVo) {
        intent = Intent(this, DetailViewActivity::class.java)
        intent.putExtra(POST_DATA, data)
        startActivity(intent)
    }
}
