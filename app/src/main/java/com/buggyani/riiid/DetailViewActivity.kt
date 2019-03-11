package com.buggyani.riiid

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.buggyani.riiid.RiiidApplication.Companion.mHeaders
import com.buggyani.riiid.RiiidApplication.Companion.riiid_api_Server
import com.buggyani.riiid.adapter.CommentListAdapter
import com.buggyani.riiid.databinding.ActivityDetailviewBinding
import com.buggyani.riiid.network.vo.CommentVo
import com.buggyani.riiid.network.vo.PatchPostVo
import com.buggyani.riiid.network.vo.PostVo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detailview.*
import kotlinx.android.synthetic.main.dialog_edit.view.*
import java.util.*


class DetailViewActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityDetailviewBinding
    private lateinit var postData: PostVo

    private var mCommentsData = ObservableArrayList<CommentVo>()
    private lateinit var mCommentsDataAdapter: CommentListAdapter
    private var disposable: Disposable? = null

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
        binding.edit = this
        titleTv.text = postData.title
        bodyTv.text = postData.body
        getCommentsList(postData.id)
        initRecyclerView(mCommentsData)
    }

    private fun initRecyclerView(data: ObservableArrayList<CommentVo>) {
        mCommentsDataAdapter = CommentListAdapter(data)
        comment.adapter = mCommentsDataAdapter
        comment.layoutManager = LinearLayoutManager(applicationContext)
    }

    /**
     * get commets list
     */
    private fun getCommentsList(id: Int) {
        progress.visibility = View.VISIBLE
        comment.visibility = View.INVISIBLE
        disposable = riiid_api_Server.getPostsComment(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: ArrayList<CommentVo>? ->
                    t!!.forEach {
                        mCommentsData.add(it)
                        it.run {
                            Log.e(TAG, "name = $name")
                            Log.e(TAG, "body = $body")
                            Log.e(TAG, "postId = $postId")
                            Log.e(TAG, "email = $email")
                            Log.e(TAG, "id = $it.id")
                        }
                    }
                    progress.visibility = View.INVISIBLE
                    comment.visibility = View.VISIBLE
                    mCommentsDataAdapter.notifyDataSetChanged()
                }, { t: Throwable? -> t!!.printStackTrace() })
    }

    /**
     * edit post dialog
     */
    fun editDialog() {
        Log.e(TAG, "editDialog")
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit, null)
        val title = dialogView.title
        val body = dialogView.body
        title.setText(postData.title)
        body.setText(postData.body)

        builder.setView(dialogView)
                .setPositiveButton(getString(R.string.save)) { _, _ ->
                    val newPost = PatchPostVo(title.text.toString(), body.text.toString())
                    patchPost(newPost)
                }
                .setNegativeButton(getString(R.string.cancel)) { _, _ ->

                }
                .show()
    }

    /**
     * patch post
     */
    private fun patchPost(newData: PatchPostVo) {
        if (mHeaders[GlobalStatic.HEADER_CONTENTTYPE] == null || mHeaders[GlobalStatic.HEADER_CHARSET] == null) {
            mHeaders[GlobalStatic.HEADER_CONTENTTYPE] = "application/json"
            mHeaders[GlobalStatic.HEADER_CHARSET] = "utf-8"
        }
        disposable = riiid_api_Server.setPost(mHeaders, postData.id, newData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t: PostVo ->
                    t.run {
                        Log.e(TAG, "body = $body")
                        Log.e(TAG, "title = $title")
                        Log.e(TAG, "usderId = $usderId")
                        Log.e(TAG, "id = $id")
                        titleTv.text = title
                        bodyTv.text = body
                    }
                }, { t: Throwable? -> t!!.printStackTrace() })
    }
}