package com.mheredia.petproject.ui.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mheredia.petproject.MainActivity
import com.mheredia.petproject.R
import java.util.*

class NotificationUtils {

    fun cancelNotification(notificationId: String, context: Context) {
        val alarmIntent =
            Intent(context, AlarmReceiver::class.java) // AlarmReceiver1 = broadcast receiver
        PendingIntent.getBroadcast(
            context,
            notificationId.hashCode(),
            alarmIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        ).cancel()
    }


    fun setNotification(
        context: Context,
        calendar: Calendar,
        notificationMessage: String = "",
        notificationId: String = "",
        alarmManager: AlarmManager
    ) {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            putExtra("notificationMessage", notificationMessage)
            putExtra("notificationId", notificationId)
        }
         val pendingIntent=PendingIntent.getBroadcast(context, notificationId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    fun createNotification(
        context: Context,
        name: String?,
        id: String?
    ) {
        val notificationBuilder =
            NotificationCompat.Builder(context, "${context.packageName}").apply {
                setSmallIcon(R.drawable.ic_launcher_foreground)
                setContentTitle(name)
                setContentText(name)
                priority = NotificationCompat.PRIORITY_DEFAULT
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                setContentIntent(pendingIntent)
            }
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }

    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = context.packageName
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}