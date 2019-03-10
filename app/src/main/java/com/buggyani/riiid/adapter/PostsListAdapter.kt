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
import com.buggyani.riiid.network.vo.PostVo
import kotlinx.android.synthetic.main.item_post.view.*

class PostsListAdapter(postsData: ObservableArrayList<PostVo>) : RecyclerView.Adapter<PostsListAdapter.PostsViewHolder>() {

    private val TAG = javaClass.simpleName
    private var postsList: MutableList<PostVo>? = postsData


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.item_post, parent, false)
        return PostsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return postsList!!.size
    }

    override fun onBindViewHolder(holder: PostsViewHolder?, position: Int) {
        val postVo = postsList!![position]
        postVo.run {
            Log.e(TAG, "id = $id")
            Log.e(TAG, "usderId = $usderId")
            Log.e(TAG, "title = $title")
            Log.e(TAG, "body = $body")
            holder!!.title.text = title
            holder.body.text = body

        }
    }

    fun removeAt(position: Int) {
        postsList!!.removeAt(position)
        notifyItemRemoved(position);
//        notifyItemRangeChanged(0, postsList.size());
    }


    class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val TAG = javaClass.simpleName
        var title = view.title
        var body = view.body
    }
}