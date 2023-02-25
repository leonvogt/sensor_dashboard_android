package sensor.dashboard.com

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("PushNotification: Message received")
    }

    override fun onNewToken(token: String) {
        println("PushNotification: New token -> $token")
    }
}