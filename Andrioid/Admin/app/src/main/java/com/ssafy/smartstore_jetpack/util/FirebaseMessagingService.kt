package com.ssafy.smartstore_jetpack.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage
import com.ssafy.smartstore_jetpack.config.ApplicationClass

private const val TAG = "FCMServic_싸피"

class FirebaseMessagingService : FirebaseMessagingService() {

    interface OnMessageReceivedListener{
        fun onMessageReceived()
    }

    companion object{
        lateinit var messageReceivedListener: OnMessageReceivedListener
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {

        if(message.notification!=null){


        }else{
            message.data.let {
                val title = it["title"]
                Log.d(TAG, "onMessageReceived: $title")
//            val mainIntent = Intent(this, ApplicationClass::class.java).apply{
//                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            }
//            val mainPendingIntent: PendingIntent = PendingIntent.getActivity(
//                this,
//                0,
//                mainIntent,
//                PendingIntent.FLAG_IMMUTABLE
//            )

                if(title == "makeorder" || title == "makeOrder"){
                    if(messageReceivedListener!=null){
                        Log.d(TAG, "onMessageReceived: here")
                        messageReceivedListener.onMessageReceived()
                    }else{
                        Log.d(TAG, "onMessageReceived: null")
                    }
                }
            }
        }


        super.onMessageReceived(message)
    }


}

