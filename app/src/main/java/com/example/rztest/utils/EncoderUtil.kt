package com.example.rztest.utils

import android.content.Context
import android.content.ContextWrapper
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets


class EncoderUtil {
    companion object {
        fun toImage(keyA: String, keyB: String, context: Context) {
            val imgByteArrayA: ByteArray = keyA.toByteArray()
            val imgByteArrayB: ByteArray = keyB.toByteArray()

            for ((index, i) in imgByteArrayA.withIndex()) {
                imgByteArrayA[index] = i.plus(1).toByte()
            }

            for ((index, i) in imgByteArrayB.withIndex()) {
                imgByteArrayB[index] = i.plus(1).toByte()
            }

            val cw = ContextWrapper(context)
            val directory = cw.getDir("keysDir", Context.MODE_PRIVATE)
            val fileA = File(directory, "image_a.jpg")

            var imgOutFile = FileOutputStream(fileA.path)
            imgOutFile.write(imgByteArrayA)
            imgOutFile.close()

            val fileB = File(directory, "image_b.jpg")
            imgOutFile = FileOutputStream(fileB.path)
            imgOutFile.write(imgByteArrayB)
            imgOutFile.close()
        }

        fun fromFile(b: ByteArray?): String {
            var tempByteArray: ByteArray? = null
            b?.let {
                tempByteArray = ByteArray(it.size)
                for ((index, i) in it.withIndex()) {
                    tempByteArray!![index] = i.minus(1).toByte()
                }
            }

            return String(tempByteArray!!, StandardCharsets.UTF_8)
        }
    }

}