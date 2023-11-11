package com.example.webview

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Composable
fun WebViewScreen(mainActivity: MainActivity) {
    var backEnabled by remember { mutableStateOf(false) }
    var filePathCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            filePathCallback?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(result.resultCode, result.data))
        } else {
            filePathCallback?.onReceiveValue(null)
        }
        filePathCallback = null
    }

    val webView = remember {
        WebView(mainActivity).apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                    backEnabled = view.canGoBack()
                }
            }
            webChromeClient = object : WebChromeClient() {

                @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                override fun onPermissionRequest(request: PermissionRequest) {
                    val cameraPermission = Manifest.permission.CAMERA
                    val imagePermission = Manifest.permission.READ_MEDIA_IMAGES

                    if (ContextCompat.checkSelfPermission(mainActivity, cameraPermission) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(mainActivity, imagePermission) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            mainActivity as Activity,
                            arrayOf(cameraPermission, imagePermission),
                            1
                        )
                    } else {
                        request.grant(request.resources)
                    }
                }

                override fun onShowFileChooser(
                    webView: WebView,
                    filePath: ValueCallback<Array<Uri>>,
                    fileChooserParams: FileChooserParams
                ): Boolean {
                    filePathCallback = filePath
                    val intent = fileChooserParams.createIntent()
                    try {
                        launcher.launch(intent)
                    } catch (e: ActivityNotFoundException) {
                        filePathCallback = null
                        Toast.makeText(mainActivity, "Cannot open file chooser", Toast.LENGTH_LONG).show()
                        return false
                    }
                    return true
                }
            }
            loadUrl(URL)
        }
    }
    BackHandler(enabled = backEnabled) {
        webView.goBack()
    }
    Box(Modifier.fillMaxSize()) {
        AndroidView(factory = { webView })
    }
}
