package sensor.dashboard.com.services

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class PushNotificationService : FirebaseMessagingService() {
    // onMessageReceived gets called when the app is in the foreground
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val channelId = "alarm_priority_1"
        val channelName = "Alarm Priority 1"
        val channelDescription = "Alarm Priority 1"

        maybeCreateNotificationChannel(channelId, channelName, channelDescription)
        displayNotification(remoteMessage, channelId, channelName)
        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(token: String) {
        println("PushNotification: New token -> $token")
    }
    private fun displayNotification(remoteMessage: RemoteMessage, channelId: String, channelName: String) {
        val messageTitle = remoteMessage.notification!!.title
        val messageText  = remoteMessage.notification!!.body

        val messageData  = remoteMessage.data
        val sensorId     = messageData["sensor_id"]!!.toInt()

        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        val notification: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(messageTitle)
            .setContentText(messageText)
            .setSmallIcon(R.drawable.sym_def_app_icon)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("PushNotification: Permission not granted")
            return
        }

        // Display the notification
        NotificationManagerCompat.from(this).notify(sensorId, notification.build())
    }

    private fun maybeCreateNotificationChannel(channelId: String, channelName: String, channelDescription: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}