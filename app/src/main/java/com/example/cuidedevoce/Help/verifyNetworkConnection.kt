package com.example.cuidedevoce.Help

import android.content.Context
import android.net.ConnectivityManager

object verifyNetworkConnection {

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = cm.activeNetworkInfo
        return ni != null && ni.isConnected
        return false
    }
}