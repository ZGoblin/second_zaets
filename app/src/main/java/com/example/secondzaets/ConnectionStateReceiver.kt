package com.example.secondzaets

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast

class ConnectionStateReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("receiverMy", "onReceive")
        context?.also {
            val connManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connManager.activeNetwork
            if (activeNetwork == null) {
                Toast.makeText(context, "Out of Internet", Toast.LENGTH_SHORT).show()
                return
            }
            val netCop = connManager.getNetworkCapabilities(activeNetwork) ?: return
            when {
                netCop.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> Toast.makeText(context, "Internet over BLUETOOTH", Toast.LENGTH_SHORT).show()
                netCop.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> Toast.makeText(context, "Internet over WIFI", Toast.LENGTH_SHORT).show()
                netCop.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> Toast.makeText(context, "Internet over CELLULAR", Toast.LENGTH_SHORT).show()
                netCop.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> Toast.makeText(context, "Internet over ETHERNET", Toast.LENGTH_SHORT).show()
            }
        }
    }

}