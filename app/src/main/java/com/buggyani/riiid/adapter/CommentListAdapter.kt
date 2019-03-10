package com.buggyani.test.adapter

/**
 * Created by bslee on 2019-03-10.
 */

import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.buggyani.riiid.R
import com.buggyani.riiid.network.vo.CommentVo
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentListAdapter(commentsData: ObservableArrayList<CommentVo>) : RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>() {

    private val TAG = javaClass.simpleName
    private var commentsList: MutableList<CommentVo>? = commentsData


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentsList!!.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder?, position: Int) {
        val commentVo = commentsList!![position]
        commentVo.run {
            Log.e(TAG, "postId = $postId")
            Log.e(TAG, "id = $id")
            Log.e(TAG, "name = $name")
            Log.e(TAG, "email = $email")
            Log.e(TAG, "body = $body")
            holder!!.name.text = name
            holder.body.text = body
            holder.email.text = email

        }
    }


    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val TAG = javaClass.simpleName
        var body = view.body
        var name = view.name
        var email = view.email
    }
}