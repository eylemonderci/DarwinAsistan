package com.example.darwinasistan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val sharedPrefs = context.getSharedPreferences("DarwinPrefs", Context.MODE_PRIVATE)
        val isSoundEnabled = sharedPrefs.getBoolean("sound_enabled", true) // Varsayılan: Açık

        if (!isSoundEnabled) {
            return
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "feed_alarm_channel"
        val channelName = "Yemleme Alarmı"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                description = "Darwin'in yemek saati bildirimleri"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Tıklama Aksiyonu
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            tapIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Bildirimi Yapılandırma
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Darwin Acıktı! 🐟")
            .setContentText("Yemleme vakti geldi. Hadi Darwin'i doyuralım!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(101, builder.build())
    }
}