package com.example.webview

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import kotlin.random.Random

const val TEST_CAMERA_URL = "https://www.webcamtest.cc/"
const val TEST_LOAD_FILE = "https://www.file.io/"
const val URL = "https://crapinka.ru/yDCrTfJH?aff_sub4=test"

object Utils {

    fun showNotification(
        context: Context,
        title: String,
        body: String
    ) {
        val channelId = "text_chanel_id"

        val builder = NotificationCompat
            .Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Test notification chanel"
            val channelDescription = "Simple description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        manager.notify(Random.nextInt(), builder.build())
    }
}
