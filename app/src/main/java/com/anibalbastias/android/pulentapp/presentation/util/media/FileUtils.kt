package com.anibalbastias.android.pulentapp.presentation.util.media

import android.content.Context
import okhttp3.*
import okio.Okio
import java.io.File
import java.io.IOException

/**
 * Created by anibalbastias on 11-09-17.
 * Utils class for download files
 */

object FileUtils {

    @Throws(Exception::class)
    fun downloadFileAsync(context: Context?, filename: String, downloadUrl: String,
                          callback: DownloadFileListener) {
        val client = OkHttpClient()
        val request = Request.Builder().url(downloadUrl).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback.onFailedDownloadFile()
                    throw IOException("Failed to download file: $response")
                }

                if (context != null) {
                    val downloadedFile = File(context.cacheDir, filename)
                    val sink = Okio.buffer(Okio.sink(downloadedFile))
                    sink.writeAll(response.body()?.source())
                    sink.close()

                    callback.onSuccessDownloadFile(downloadedFile)
                } else {
                    callback.onFailedDownloadFile()
                }
            }
        })
    }

    interface DownloadFileListener {
        fun onSuccessDownloadFile(file: File)

        fun onFailedDownloadFile()
    }
}
