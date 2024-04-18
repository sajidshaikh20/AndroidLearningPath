package com.base.hilt.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.base.hilt.MainActivity
import com.base.hilt.R
import com.base.hilt.utils.Constants.push_type
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint


const val channelId = "learning_path"
const val channelName = "channel.one"

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("", "onNewToken sajid: $token")

    }
    // private var bitmap: Bitmap? = null

    override fun onMessageReceived(remote: RemoteMessage) {

        Log.i("FirebaseMessage", "onMessageReceived:${remote.notification?.title.toString()}")
        Log.i("FirebaseMessage", "onMessageReceived:${remote.notification}")

        if (remote.notification !== null) {

            remote.notification!!.title?.let {

            }
            remote.notification?.let {
                val messageBody = it.body ?: ""
                val title = it.title ?: ""

                val img = it.imageUrl
                Log.i("img", "onMessageReceived: $img")

                Log.d("complete paylod", remote.notification.toString())
                Log.d("payload", remote.data.toString())

                val activityToOpen = when {
                    title == "Title 1" && messageBody == "Message 2" -> "MainActivity"
                    else -> "MainActivity"
                }
                // generateNotification(title,messageBody,activityToOpen)
                sendNotification(messageBody, activityToOpen, title, it)
            }


        }
    }

    override fun handleIntent(intent: Intent) {
        try {
            if (intent.extras != null) {
                val builder = RemoteMessage.Builder("MessagingService")
                for (key in intent.extras!!.keySet()) {
                    builder.addData(key!!, intent.extras!![key].toString())
                }
                onMessageReceived(builder.build())
            } else {
                super.handleIntent(intent)
            }
        } catch (e: Exception) {
            super.handleIntent(intent)
        }
    }



    private fun sendNotification(
        messageBody: String,
        activityToOpen: String,
        title: String,
        notification: RemoteMessage.Notification
    ) {

        val intent = when (activityToOpen) {
            "MainActivity" -> {
                Log.i("noti_action", "sendNotification:$activityToOpen")
                val intent=  Intent(this, MainActivity::class.java)
                intent.putExtra(push_type, "User")
            }

            else -> Intent(this, MainActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        var notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setGroup("my_notification_group")
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setChronometerCountDown(true)

        notificationBuilder = notificationBuilder.setContent(getRemoteView(title, messageBody))
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            val channel2 = NotificationChannel(
                "channel2Id",
                "Channel 2 Name",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel2.enableVibration(false)

            notificationManager.createNotificationChannel(channel)

            notificationManager.createNotificationChannel(channel2)
        }
        notificationManager.notify(channelId, 0, notificationBuilder.build())

    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews(packageName, R.layout.notification_layout)
        remoteView.setTextViewText(R.id.tvtitle, title)
        remoteView.setTextViewText(R.id.tvDiscription, message)
        remoteView.setImageViewResource(R.id.sivlogo, R.drawable.ic_notification)

        return remoteView
    }


    /*private fun generateNotification(title: String, message: String, activityToOpen: String) {

       val intent = when (activityToOpen) {
           "MainActivity2" -> {
               Log.i("MainActivity2", "sendNotification:$activityToOpen")
               //Logic where we can navigate
               Intent(this, MainActivity::class.java)
           }

           else -> Intent(this, MainActivity::class.java)
       }
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
       val pendingIntent = PendingIntent.getActivity(
           this,
           0,
           intent,
           PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
       )

       val notificationLayout = RemoteViews(packageName, R.layout.notification_layout)
       val notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification_layout)


       var builder: NotificationCompat.Builder = NotificationCompat.Builder(
           applicationContext,
           channelId
       ).setAutoCancel(true)
           .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
           .setOnlyAlertOnce(true)
           .setContentTitle(title)
           .setContentText(message)
           .setPriority(NotificationCompat.PRIORITY_DEFAULT)
           .setContentIntent(pendingIntent)
           .setChronometerCountDown(true)


       builder = builder.setContent(getRemoteView(title, message))


       val notificationManager =
           getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           val notificationChannel =
               NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
           notificationManager.createNotificationChannel(notificationChannel)
       }
       notificationManager.notify(channelId, 0, builder.build())

   }*/


}
