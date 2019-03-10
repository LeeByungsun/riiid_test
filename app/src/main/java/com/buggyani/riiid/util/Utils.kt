package com.buggyani.test.util

import android.content.Context
import android.util.Log
import java.text.SimpleDateFormat

/**
 * Created by bslee on 2019-02-16.
 */

class Utils(context: Context) {

    init {
        mContext = context
    }

    companion object {
        lateinit var mContext: Context
        private val TAG = Utils::class.java.simpleName

        fun getData(year: Int, month: Int, day: Int): String {
            var inDate = String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", day)
            Log.e(TAG, "date = $inDate")
            val inSdf = SimpleDateFormat("yyyyMMdd")
            val outSdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
            val date = inSdf.parse(inDate)
            Log.e(TAG, "date = ${date.toString()}")
            val formattedDate = outSdf.format(date)
            return formattedDate.toString().replace("+", "%2b")// +만 encoding되지 않고 : 도 encoding 되어 개별변경
        }


    }
}
