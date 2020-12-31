package com.example.secondzaets

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.core.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.secondzaets.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val connectionStateReceiver = ConnectionStateReceiver()
    private val buttonReceiver = ButtonReceiver()
    private lateinit var binding: ActivityMainBinding

    private val tag = "MAIN_ACTIVITY"
    private val CHANNEL_ID = "MAIN_ACTIVITY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupReceiver()
        setupListener()
    }

    fun setupReceiver() {
        registerReceiver(connectionStateReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        registerReceiver(buttonReceiver, IntentFilter(TAP_SIGNAL))
    }

    fun setupListener() {
        binding.bReceiver.setOnClickListener {
            Log.d(tag, "bReceiver clicked")
            sendBroadcast(Intent(TAP_SIGNAL))
        }
        binding.bMaps.setOnClickListener {
            Log.d(tag, "bMaps clicked")
            showUniversity()
        }
        binding.bOpenActivity.setOnClickListener {
            SecondActivity.start(this, "Hello new YEAR", 2021)
        }
        binding.bSimpleNotification.setOnClickListener {
            sendSimpleNotification()
        }
        binding.bNotificationWithAction.setOnClickListener {
            sendNotificationWithAction()
        }
        binding.bNotificationWithReply.setOnClickListener {
            sendNotificationWithReply()
        }
        binding.bNotificationWithProgress.setOnClickListener {
            sendNotificationWithProgress()
        }
    }

    private fun sendNotificationWithProgress() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Download knowleadge")
            .setContentText("Downloading...")
            .setProgress(100, 0, false)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        createNotificationChannel()
        NotificationManagerCompat.from(this).notify(1, builder.build())

        var i = 1
        while (i < 100) {
            Thread.sleep(1000)
            i++
            builder
                .setProgress(100, i, false)
            NotificationManagerCompat.from(this).notify(1, builder.build())
        }

        builder
            .setContentText("Download complete")
            .setProgress(0, 0, false)
        NotificationManagerCompat.from(this).notify(1, builder.build())
    }

    private fun sendNotificationWithReply() {
        val intent = Intent(this, ButtonReceiver::class.java).apply {
            action = REPLY_ACTION
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)

        var remoteInput: RemoteInput = RemoteInput.Builder(TEXT_FROM_MESSAGES).run {
            setLabel("Type message")
            build()
        }

        val action: NotificationCompat.Action = NotificationCompat.Action.Builder(R.drawable.ic_launcher_foreground, "Reply", pendingIntent)
            .addRemoteInput(remoteInput)
            .build();

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("My first notification")
            .setContentText("It`s my first notification!! Kotlin is awesome!!")
            .setStyle(NotificationCompat.BigTextStyle().bigText("It`s my first notification!! Kotlin is awesome!!"))
            .addAction(action)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        createNotificationChannel()
        NotificationManagerCompat.from(this).notify(1, builder.build())
    }

    private fun sendNotificationWithAction() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("My first notification")
            .setContentText("It`s my first notification!! Kotlin is awesome!!")
            .setStyle(NotificationCompat.BigTextStyle().bigText("It`s my first notification!! Kotlin is awesome!!"))
            .addAction(R.drawable.ic_launcher_foreground, "To app", pendingIntent)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        createNotificationChannel()
        NotificationManagerCompat.from(this).notify(1, builder.build())
    }

    fun sendSimpleNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("My first notification")
            .setContentText("It`s my first notification!! Kotlin is awesome!!")
            .setStyle(NotificationCompat.BigTextStyle().bigText("It`s my first notification!! Kotlin is awesome!!"))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        createNotificationChannel()
        NotificationManagerCompat.from(this).notify(1, builder.build())
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "name", importance).apply {
                description = "descriptionText"
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showUniversity() {
        val mapIntent = Intent()
        val location = Uri.parse("https://www.google.com/maps/search/?api=1&query=48.455399,35.062057")
        mapIntent.action = Intent.ACTION_VIEW
        mapIntent.data = location
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    override fun onStart() {
        super.onStart()
        setupReceiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectionStateReceiver)
        unregisterReceiver(buttonReceiver)
    }
}