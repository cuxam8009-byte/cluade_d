package com.dymith.browser

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.SafeBrowsingResponse
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity() {
    private lateinit var browser: WebView
    private val startPage = "file:///android_asset/start.html"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT

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
            settings.safeBrowsingEnabled = true

            addJavascriptInterface(BrowserBridge(), "Dymith")
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    request: WebResourceRequest
                ): Boolean {
                    val uri = request.url
                    val scheme = uri.scheme?.lowercase()
                    return when {
                        uri.toString() == startPage -> false
                        scheme == "https" || scheme == "http" -> false
                        else -> true
                    }
                }
            }
        }

        setContentView(browser)
        if (savedInstanceState == null) {
            browser.loadUrl(startPage)
        } else {
            browser.restoreState(savedInstanceState)
        }

        onBackPressedDispatcher.addCallback(this) {
            if (browser.canGoBack()) browser.goBack() else finish()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        browser.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        browser.removeJavascriptInterface("Dymith")
        browser.stopLoading()
        browser.destroy()
        super.onDestroy()
    }

    private inner class BrowserBridge {
        @JavascriptInterface
        fun search(rawQuery: String) {
            val query = rawQuery.trim()
            if (query.isEmpty()) return
            val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
            runOnUiThread {
                browser.loadUrl("https://www.google.com/search?q=$encoded")
            }
        }

        @JavascriptInterface
        fun open(rawUrl: String) {
            val safeUrl = rawUrl.trim()
            val isWebUrl = safeUrl.startsWith("https://") || safeUrl.startsWith("http://")
            if (!isWebUrl) return
            runOnUiThread { browser.loadUrl(safeUrl) }
        }

        @JavascriptInterface
        fun home() {
            runOnUiThread { browser.loadUrl(startPage) }
        }
    }
}
