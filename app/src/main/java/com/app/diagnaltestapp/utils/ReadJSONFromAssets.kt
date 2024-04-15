package com.app.diagnaltestapp.utils

import android.content.Context
import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

fun ReadJSONFromAssets(context: Context, path: String): String {
    val identifier = "[ReadJSON]"
    try {
        val file = context.assets.open("$path")
        Log.i(
            identifier,
            " Found File: $file.",
        )
        val bufferedReader = BufferedReader(InputStreamReader(file))
        val stringBuilder = StringBuilder()
        bufferedReader.useLines { lines ->
            lines.forEach {
                stringBuilder.append(it)
            }
        }
        Log.i(
            identifier,
            "getJSON   stringBuilder: $stringBuilder.",
        )
        val jsonString = stringBuilder.toString()
        Log.i(
            identifier,
            " JSON as String: $jsonString.",
        )
        return jsonString
    } catch (e: Exception) {
        Log.e(
            identifier,
            " Error reading JSON: $e.",
        )
        e.printStackTrace()
        return ""
    }
}

fun getJsonFilesFromAssets(context: Context): List<String> {
    val assetManager = context.assets
    val jsonFiles = mutableListOf<String>()

    try {
        // List all files in the "assets" folder
        val files = assetManager.list("") ?: emptyArray()

        // Filter JSON files
        for (file in files) {
            if (file.endsWith(".json")) {
                jsonFiles.add(file)
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return jsonFiles
}