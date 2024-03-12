package com.vungn.backvietlibrary.util.helper

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class FileHelper(private val context: Context, private val fileName: String) {
    fun getOutputPdfFile(): File {
        val mediaStorageDir = File(context.cacheDir.path + "/pdf")
        if (!mediaStorageDir.exists()) {
            val mkdir = mediaStorageDir.mkdirs()
            Log.d("", "getOutputPdfFile: $mkdir")
        }
        return File(mediaStorageDir, fileName)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun setPdfDownload(byte: String?) = callbackFlow {
        if (byte != null) {
            val file = getOutputPdfFile()
            if (!file.exists()) {
                val created = file.createNewFile()
                Log.d("", "setPdfDownload: $created")
            }
            try {
                FileOutputStream(file).use { fos ->
                    val decoder = Base64.decode(byte, 0)
                    fos.write(decoder)
                    launch { send(true) }
                    fos.flush()
                    fos.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch { send(false) }
            }
        }
        awaitClose { }
    }.flowOn(Dispatchers.IO)
}