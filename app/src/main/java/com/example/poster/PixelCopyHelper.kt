package com.example.poster

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.graphics.createBitmap

object PixelCopyHelper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun captureScreenshot(
        activity: Activity,
        view: View,
        listener: OnScreenshotCapturedListener
    ) {
        val window = activity.window
        val bounds = Rect()
        view.getWindowVisibleDisplayFrame(bounds)
        val width = view.width
        val height = view.height
        val bitmap = createBitmap(width, height)

        PixelCopy.request(
            window,
            bounds,
            bitmap,
            { copyResult ->
                if (copyResult == PixelCopy.SUCCESS) {
                    listener.onScreenshotCaptured(bitmap)
                } else {
                    listener.onScreenshotFailed(copyResult)
                }
            },
            Handler(Looper.getMainLooper()) // Ensure callback on main thread
        )
    }

    interface OnScreenshotCapturedListener {
        fun onScreenshotCaptured(bitmap: Bitmap)
        fun onScreenshotFailed(errorCode: Int)
    }
}
