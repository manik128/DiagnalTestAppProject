package com.app.diagnaltestapp.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.app.diagnaltestapp.R


object Utils {
    private val fontFamily = FontFamily(Font(R.font.titillium_web_regular))
    private val headerTextStyle = TextStyle(
        fontFamily = fontFamily
    )
    fun removeFileExtension(fileName: String): String {
        val lastDotIndex = fileName.lastIndexOf('.')
        return if (lastDotIndex != -1) {
            fileName.substring(0, lastDotIndex)
        } else {
            fileName // No extension found, return the original file name
        }
    }

    fun getTextStyle(): TextStyle {
        return headerTextStyle
    }
}