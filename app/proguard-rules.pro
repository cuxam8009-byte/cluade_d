# Keep methods exposed to the trusted local start page's JavaScript bridge.
-keepclassmembers class com.dymith.browser.MainActivity$BrowserBridge {
    @android.webkit.JavascriptInterface <methods>;
}
