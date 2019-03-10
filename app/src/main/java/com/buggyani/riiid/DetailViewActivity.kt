package com.buggyani.riiid

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.AbsListView
import com.buggyani.riiid.adapter.RecyclerItemClickListenr
import com.buggyani.riiid.databinding.ActivityDetailviewBinding
import com.buggyani.riiid.network.vo.CommentVo
import com.buggyani.riiid.network.vo.PostVo
import com.buggyani.test.adapter.CommentListAdapter
import com.buggyani.test.adapter.PostsListAdapter
import com.buggyani.test.util.GlobalStatic
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detailview.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class DetailViewActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityDetailviewBinding
    private lateinit var postData: PostVo

    private var mCommentsData = ObservableArrayList<CommentVo>()
    private lateinit var mCommentsDataAdapter: CommentListAdapter
    private var disposable: Disposable? = null
    private var id = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detailview)
        postData = intent.getSerializableExtra(GlobalStatic.POST_DATA) as PostVo
        intiUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }


    private fun intiUI() {
        getCommentsList(postData.id)
        initRecyclerView(mCommentsData)
        titleTv.text = postData.title
        bodyTv.text = postData.body


    }

    private fun initRecyclerView(data: ObservableArrayList<CommentVo>) {
        mCommentsDataAdapter = CommentListAdapter(data)
        comment.adapter = mCommentsDataAdapter
        comment.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun getCommentsList(id: Int) {
        disposable = RiiidApplication.riiid_api_Server.getPostsComment(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: ArrayList<CommentVo>? ->
                    t!!.forEach {
                        mCommentsData.add(it)
                        it.run {
                            Log.e(TAG, "-----------------------------------")
                            Log.e(TAG, "name = $name")
                            Log.e(TAG, "body = $body")
                            Log.e(TAG, "postId = $postId")
                            Log.e(TAG, "email = $email")
                            Log.e(TAG, "id = $it.id")
                            Log.e(TAG, "-----------------------------------")
                        }
                    }
                    mCommentsDataAdapter.notifyDataSetChanged()
                }, { t: Throwable? -> t!!.printStackTrace() })
    }


}