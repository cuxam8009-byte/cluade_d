package com.dymith.browser

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private val homeUrl = "file:///android_asset/home.html"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK
        window.decorView.systemUiVisibility = 0
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        webView = WebView(this).apply {
            setBackgroundColor(Color.BLACK)
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadsImagesAutomatically = true
                useWideViewPort = true
                loadWithOverviewMode = true
                builtInZoomControls = false
                displayZoomControls = false
                mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
                allowFileAccess = false
                allowContentAccess = false
                javaScriptCanOpenWindowsAutomatically = false
                setSupportMultipleWindows(false)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) safeBrowsingEnabled = true
            }
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    return !(url == homeUrl || url.startsWith("https://") || url.startsWith("http://"))
                }
                override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
                    if (url == homeUrl) view.addJavascriptInterface(HomeBridge(), "Dymith")
                    else view.removeJavascriptInterface("Dymith")
                }
                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    if (request.isForMainFrame) Toast.makeText(this@MainActivity, "Không thể tải trang. Kiểm tra kết nối mạng.", Toast.LENGTH_LONG).show()
                }
            }
        }
        setContentView(webView)
        webView.addJavascriptInterface(HomeBridge(), "Dymith")
        webView.loadUrl(homeUrl)
    }

    private inner class HomeBridge {
        @JavascriptInterface fun search(text: String) = runOnUiThread { openSearch(text) }
        @JavascriptInterface fun open(address: String) = runOnUiThread { openAddress(address) }
        @JavascriptInterface fun home() = runOnUiThread { showHome() }
        @JavascriptInterface fun setTheme(dark: Boolean) = runOnUiThread {
            window.statusBarColor = if (dark) Color.BLACK else Color.WHITE
            window.navigationBarColor = if (dark) Color.BLACK else Color.WHITE
            window.decorView.systemUiVisibility = if (dark) 0 else
                (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
        }
    }

    private fun openSearch(raw: String) {
        val value = raw.trim()
        if (value.isEmpty()) return
        val candidate = if (value.contains("://")) value else if (
            android.util.Patterns.WEB_URL.matcher(value).matches() && value.contains(".")
        ) "https://" + value else "https://www.google.com/search?q=" + Uri.encode(value)
        openAddress(candidate)
    }

    private fun openAddress(raw: String) {
        val uri = runCatching { Uri.parse(raw.trim()) }.getOrNull() ?: return
        if ((uri.scheme != "https" && uri.scheme != "http") || uri.host.isNullOrBlank()) {
            Toast.makeText(this, "Chỉ hỗ trợ địa chỉ HTTP hoặc HTTPS hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }
        webView.removeJavascriptInterface("Dymith")
        webView.loadUrl(uri.toString())
    }

    private fun showHome() {
        webView.addJavascriptInterface(HomeBridge(), "Dymith")
        webView.loadUrl(homeUrl)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (::webView.isInitialized && webView.canGoBack()) webView.goBack() else showHome()
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            webView.stopLoading()
            webView.removeJavascriptInterface("Dymith")
            webView.destroy()
        }
        super.onDestroy()
    }
}
