# Dymith

A clean rebuild of the Dymith Android browser, starting from the Hybrust-inspired home screen.

## Current foundation

- Android app shell using Kotlin and Android WebView.
- Local, offline-first start page in `app/src/main/assets/start.html`.
- Animated geometric splash screen, Vietnamese clock/date, quick links, address/search bar, and light/dark theme.
- Search and navigation are bridged to native Android code.
- WebView hardening: mixed content blocked, content access disabled, file-origin cross-access disabled, and only HTTP(S) navigation accepted.

## Build

Open this project in Android Studio with JDK 17 and Android SDK 35, then run the `app` configuration.

Command line (with a local Gradle installation):

```sh
gradle :app:assembleDebug
```

## Rebuild strategy

This is a clean foundation, not a feature-complete browser yet. Next milestones should be delivered in small, testable layers: tab/session model, downloads, history/bookmarks, permissions, privacy controls, then Rust-backed performance features if measurements justify them.
