package com.example.rztest.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat.getSystemService


class DownloadUtil {

    companion object {
        fun downloadResourceTask(applicationContext: Context, uri: Uri, fileName: String) {

            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setAllowedOverRoaming(false)
            request.setDescription("Please wait...")
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                fileName + ".jpg"
            )
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val manager = getSystemService(applicationContext, DownloadManager::class.java)
            manager!!.enqueue(request)
        }
    }

}