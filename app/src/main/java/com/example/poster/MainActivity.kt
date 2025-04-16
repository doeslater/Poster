package com.example.poster

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.poster.data.model.Article
import com.example.poster.data.model.weather.WeatherResponse
import com.example.poster.databinding.ActivityMainBinding
import com.example.poster.ui.MainPopup
import com.example.poster.ui.PopupType
import com.example.poster.ui.WallCanvas
import com.example.poster.util.ConnectivityObserver
import com.example.poster.util.NetworkConnectivityObserver
import com.example.poster.util.PixelCopyHelper
import com.example.poster.viewmodel.MainViewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity(), PixelCopyHelper.OnScreenshotCapturedListener {
    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
        )
    }

    private val TAG = MainActivity::class.java.name
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var connectivityObserver: ConnectivityObserver
    private var headlineNewsState = mutableStateOf(ArrayList<Article>())
    private var weatherState = mutableStateOf(
        WeatherResponse(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.composeView.setContent {
            val status = connectivityObserver.observe().collectAsState(
                initial = ConnectivityObserver.Status.Unavailable
            )

            if (status.value == ConnectivityObserver.Status.Available) {
                WallCanvas(
                    modifier = Modifier,
                    headlineNewsState = headlineNewsState,
                    weatherState = weatherState,
                    onRefresh = {
                        getNews()
                        getWeather()
                    }
                )
            } else if (status.value == ConnectivityObserver.Status.Unavailable) {
                MainPopup(
                    popupType = PopupType.NO_INTERNET,
                    onWifiSettings = {
                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    },
                )
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.bottomImage.setOnClickListener {
            PixelCopyHelper.captureScreenshot(this, binding.main, this)
        }


        if (!isPermissionGranted()) {
            verifyStoragePermission(this)
//            openAppSettings(this)
        }

        getNews()
        getWeather()

    }

    private fun getNews() {
        mainViewModel.getBreakingNews().observe(this) { topNews ->
            try {
                topNews.let { headlineNewsState.value = it as ArrayList<Article> }
            } catch (e: Exception) {
                Log.e(TAG, "getNews: ${e.message}")
            }
        }
    }

    private fun getWeather() {
        mainViewModel.getWeather().observe(this) { weather ->
            try {
                weather?.let { weatherState.value = it }
            } catch (e: Exception) {
                Log.e(TAG, "getWeather: ${e.message}")
            }
        }
    }

    fun takeScreenshot(bitmap: Bitmap) {
        try {
            val imagesDir = Environment.getExternalStoragePublicDirectory("Wallpaper")
            val file = File(imagesDir, "poster.png")

            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, it)
                it.flush()
                it.close()
            }

            MediaScannerConnection.scanFile(
                this, arrayOf(file.path),
                null, null
            )

            Toast.makeText(this, "Image saved successfully: ${file.path}", Toast.LENGTH_LONG).show()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (isPermissionGranted()) {
            openAppSettings(this)
        } else {
            Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun isPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    private fun verifyStoragePermission(activity: Activity?) {
        activity?.let {

            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            val permission: Int =
                ActivityCompat.checkSelfPermission(it, Manifest.permission.WRITE_EXTERNAL_STORAGE)

            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(it, permissions, REQUEST_CODE_PERMISSIONS)
            }
        }
    }

    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    override fun onScreenshotCaptured(bitmap: Bitmap) {
        takeScreenshot(bitmap)
    }

    override fun onScreenshotFailed(errorCode: Int) {
        Toast.makeText(this, "Screenshot failed with error code: $errorCode", Toast.LENGTH_SHORT)
            .show()
    }

}