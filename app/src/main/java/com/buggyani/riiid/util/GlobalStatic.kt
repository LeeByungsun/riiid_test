package com.buggyani.test.util

/**
 * Created by bslee on 2019-02-16.
 */

object GlobalStatic {


    /*----------------------------------
    sharedPreference
    ------------------------------------*/
//    const val SHARED_FCM_TOKEN_KEY = "Fcm_Token"//fcm token
//    const val SHARED_KEY = "key"// wallet address
//    const val SHARED_SECRET = "secret"//wallet key
//    const val SHARED_IS_WALLET = "iswallet" // exist wallet

    /*----------------------------------
    intent
    ------------------------------------*/
    const val POST_DATA = "post"//fcm token
    /*----------------------------------
    Header
   ------------------------------------*/
    const val HEADER_BALANCE = "X-Wallet-Secret"

    /*----------------------------------
    ETC
   ------------------------------------*/
    const val CREATED_NOTIFICATION_CHANNEL = "CREATED_NOTIFICATION_CHANNEL"//noto 채널 설정
    // noti receiver
    var NOTIRECEIVER_FILTER = "noti_filter"
    const val NOTI_CONNECTED = 100
    const val NOTI_MESSAGE = 200
}
