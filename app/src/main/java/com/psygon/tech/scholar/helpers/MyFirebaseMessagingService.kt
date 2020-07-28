package com.psygon.tech.scholar.helpers

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.Exception

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        log("Message received")

        showNotification(this, p0?.notification?.title, p0?.notification?.body)
    }

    override fun onMessageSent(p0: String?) {
        super.onMessageSent(p0)

        log("Message sent")
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()

        log("Message deleted")
    }

    override fun onSendError(p0: String?, p1: Exception?) {
        super.onSendError(p0, p1)

        log("Message send error")
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)

        log("New Token")
    }
}