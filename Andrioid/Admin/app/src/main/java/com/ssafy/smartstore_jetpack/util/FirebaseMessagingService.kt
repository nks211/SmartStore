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
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage
import com.ssafy.smartstore_jetpack.config.ApplicationClass
import com.ssafy.smartstore_jetpack.config.ApplicationClass.Companion.channel_id

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

    private fun makeNotification(title: String, content: String){
        val mainIntent = Intent(this, ApplicationClass::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val mainPendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this, channel_id)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setContentIntent(mainPendingIntent)

        NotificationManagerCompat.from(this).apply{
            notify(101, builder.build())
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {

        if(message.notification!=null){
            val title = message.notification!!.title.toString()
            val content = message.notification!!.body.toString()
            makeNotification(title, content)
        }else{
            message.data.let {
                val title = it["title"]
                Log.d(TAG, "onMessageReceived: $title")


                if(title == "makeorder" || title == "makeOrder"){
                    if(messageReceivedListener!=null){
                        Log.d(TAG, "onMessageReceived: here")
                        messageReceivedListener.onMessageReceived()
                    }else{
                        Log.d(TAG, "onMessageReceived: null")
                    }
                }else{
                    makeNotification(title.toString(), it["body"].toString())
                }
            }
        }


        super.onMessageReceived(message)
    }


}

