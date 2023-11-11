package com.example.webview

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.webview.ui.theme.WebViewTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("TOKEN", token)
            }
        }
        setContent {
            WebViewTheme {
                WebViewScreen(this)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 32) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
                ) == PackageManager.PERMISSION_GRANTED
            ) return
            val launcher = registerForActivityResult<String, Boolean>(
                ActivityResultContracts.RequestPermission()
            ) { _: Boolean? -> }
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

}
