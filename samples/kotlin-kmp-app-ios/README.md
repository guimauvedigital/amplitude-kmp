# Amplitude KMP iOS Sample App

This is the iOS wrapper for the Amplitude KMP sample app, which uses Compose Multiplatform for the UI.

## Setup

### 1. Configure Your API Key

Edit `../kotlin-kmp-app/src/iosMain/kotlin/com/amplitude/kmp/sample/AmplitudeConfig.ios.kt`:

```kotlin
actual val amplitudeApiKey: String = "YOUR_IOS_API_KEY_HERE"
```

> **Note**: The API key is configured in Kotlin shared code, not in Swift. Amplitude is initialized automatically when
> the app loads!

### 2. Open the Project

Open the Xcode project directly (no workspace needed with SPM):

```bash
cd samples/kotlin-kmp-app-ios
open iosApp.xcodeproj
```

### 3. Resolve Dependencies

When you first open the project, Xcode will automatically:

1. Download the Amplitude Swift SDK via Swift Package Manager (1.16.2+)
2. Resolve all package dependencies

Or manually: **File → Packages → Resolve Package Versions**

### 4. Build the Kotlin Framework

From the repository root:

```bash
./gradlew :samples:kotlin-kmp-app:embedAndSignAppleFrameworkForXcode
```

Or just run the app in Xcode - the build phase will run Gradle automatically.

## Architecture

- **Swift Layer**: Minimal SwiftUI wrapper (`iOSApp.swift`, `ContentView.swift`)
- **Kotlin Framework**: `AmplitudeKMPSample.framework` built from `../kotlin-kmp-app`
- **UI**: Compose Multiplatform shared between Android and iOS
- **Amplitude SDK**: iOS native SDK added via **Swift Package Manager**
- **Initialization**: **Automatic** - Amplitude initializes in `App.kt` via `LaunchedEffect`

## Initialization Pattern

**No iOS-specific initialization needed!**

Amplitude is initialized automatically when the app starts via shared Kotlin code:

```kotlin
// In commonMain/App.kt - runs automatically on all platforms
@Composable
fun App() {
    LaunchedEffect(Unit) {
        AmplitudeInitializer.initializeAndAwait(
            apiKey = amplitudeApiKey,           // From iosMain/AmplitudeConfig.ios.kt
            platformContext = platformContext,   // null on iOS
            userId = "kmp-sample-user-iOS"
        )
    }

    // Rest of the app UI...
}
```

**Swift code is minimal** - just wraps the Compose UI:

```swift
import SwiftUI
import AmplitudeKMPSample

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

// That's it! Amplitude initializes automatically.
```

## Building

The Xcode build process automatically:

1. Runs Gradle task `:samples:kotlin-kmp-app:embedAndSignAppleFrameworkForXcode`
2. Builds the `AmplitudeKMPSample.framework` (includes Compose UI and shared code)
3. Links the Amplitude Swift SDK (from SPM)
4. Embeds everything in the iOS app

## Framework Location

The Kotlin framework is built to:

```
../kotlin-kmp-app/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)/AmplitudeKMPSample.framework
```

## Dependencies

- **Swift Package Manager**: Amplitude Swift SDK (1.16.2+)
- **Kotlin Framework**: AmplitudeKMPSample (built by Gradle)

No CocoaPods required! 🎉

## Shared Initialization Benefits

- ✅ Single initialization logic in `commonMain`
- ✅ No Swift code needed for Amplitude setup
- ✅ Automatic initialization when app loads
- ✅ Configuration shared across platforms
- ✅ Same API key management pattern as Android

## Notes

- The sample app uses the same Compose UI as the Android version
- All Amplitude tracking happens through shared Kotlin code
- The Amplitude Swift SDK is automatically linked via SPM
- No platform-specific tracking code needed
- API key is configured in Kotlin, not Swift
