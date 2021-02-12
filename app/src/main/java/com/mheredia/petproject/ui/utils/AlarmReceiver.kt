package com.mheredia.petproject.ui.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtils().createNotification(context, intent.getStringExtra("notificationMessage"), intent.getStringExtra("notificationId"))
    }
}