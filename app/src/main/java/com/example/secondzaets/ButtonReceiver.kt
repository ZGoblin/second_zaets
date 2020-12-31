package com.example.secondzaets

import android.app.Application
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput

class ButtonReceiver: BroadcastReceiver() {
    private val tag = "BUTTON_RECEIVER"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TAP_SIGNAL) {
            Toast.makeText(context, "$tag - user tap on button", Toast.LENGTH_SHORT).show()
            Log.d(tag, "user tap on button")
        }
        if (intent?.action == REPLY_ACTION) {
            Log.d(tag, "REPLY_ACTION")
            val string = getMessageText(intent)
            string?.also {
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
            }

            val repliedNotification = NotificationCompat.Builder(context!!, "MAIN_ACTIVITY")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("Processed")
                .build()

// Issue the new notification.
            NotificationManagerCompat.from(context).apply {
                this.notify(1, repliedNotification)
            }
        }
    }

        fun getMessageText(intent: Intent): String? {
            return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(TEXT_FROM_MESSAGES)
                .toString()
        }
}