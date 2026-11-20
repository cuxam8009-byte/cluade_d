package com.dymith.browser

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.WindowCompat
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : Activity() {
    private lateinit var browser: WebView
    private val startPage = "file:///android_asset/start.html"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false
        }

        browser = WebView(this).apply {
            setBackgroundColor(Color.BLACK)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadsImagesAutomatically = true
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.allowFileAccess = true
            settings.allowContentAccess = false
            settings.allowFileAccessFromFileURLs = false
            settings.allowUniversalAccessFromFileURLs = false
            settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) settings.safeBrowsingEnabled = true

            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
                    if (url == startPage) view.addJavascriptInterface(BrowserBridge(), "Dymith")
                    else view.removeJavascriptInterface("Dymith")
                    super.onPageStarted(view, url, favicon)
                }

                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val scheme = request.url.scheme?.lowercase()
                    return scheme != "https" && scheme != "http"
                }
            }
        }
        setContentView(browser)
        if (savedInstanceState == null) browser.loadUrl(startPage) else browser.restoreState(savedInstanceState)
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (::browser.isInitialized && browser.canGoBack()) browser.goBack() else super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (::browser.isInitialized) browser.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        if (::browser.isInitialized) {
            browser.removeJavascriptInterface("Dymith")
            browser.stopLoading()
            browser.destroy()
        }
        super.onDestroy()
    }

    private inner class BrowserBridge {
        @JavascriptInterface
        fun search(rawQuery: String) {
            val query = rawQuery.trim()
            if (query.isEmpty()) return
            val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
            runOnUiThread { browser.loadUrl("https://www.google.com/search?q=$encoded") }
        }

        @JavascriptInterface
        fun open(rawUrl: String) {
            val safeUrl = rawUrl.trim()
            if (!safeUrl.startsWith("https://") && !safeUrl.startsWith("http://")) return
            runOnUiThread { browser.loadUrl(safeUrl) }
        }

        @JavascriptInterface
        fun home() {
            runOnUiThread { browser.loadUrl(startPage) }
        }
    }
}