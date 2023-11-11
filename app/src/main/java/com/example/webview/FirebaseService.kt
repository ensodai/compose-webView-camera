package com.example.webview

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class FirebaseService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title
        val content = message.notification?.body
        val data = Gson().toJson(message.data)

        if (title != null && content != null) {
            Utils.showNotification(this, title,content)
        }
    }
}