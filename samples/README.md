# Amplitude KMP SDK Samples

This directory contains sample applications demonstrating how to use the Amplitude Kotlin Multiplatform SDK.

## Available Samples

### kotlin-kmp-app
A complete Kotlin Multiplatform sample app using Compose Multiplatform.

**Features:**
- Basic event tracking
- Events with properties and options
- User identification with `Identify`
- Group management
- Revenue tracking
- Custom plugins

**Platforms:**
- Android
- iOS

**Tech Stack:**
- Kotlin Multiplatform
- Compose Multiplatform
- Amplitude KMP SDK

[View Sample](kotlin-kmp-app/)

## Running the Samples

### Android

```bash
# Build and install on device/emulator
./gradlew :samples:kotlin-kmp-app:installDebug

# Or just build
./gradlew :samples:kotlin-kmp-app:assembleDebug
```

### iOS

1. Open the Xcode workspace
2. Select the `kotlin-kmp-app` iOS target
3. Run on simulator or device

**Note:** You may need to run `pod install` in the iOS directory first.

## Setting Up Your API Key

Before running the samples, you need to configure your Amplitude API keys.

### Android API Key Setup

1. **Create `local.properties`** in the root project directory (`amplitude-kmp/local.properties`):
   ```properties
   AMPLITUDE_API_KEY=your_android_api_key_here
   ```

2. **Get your API key** from: https://analytics.amplitude.com/

> **Security Note**: `local.properties` is gitignored and will NOT be committed. Your API keys stay secure!

The Android app will automatically load this via `BuildConfig.AMPLITUDE_API_KEY`.

### iOS API Key Setup

Edit `samples/kotlin-kmp-app/src/iosMain/kotlin/com/amplitude/kmp/sample/AmplitudeConfig.ios.kt`:

```kotlin
actual val amplitudeApiKey: String = "your_ios_api_key_here"
```

Replace `"your_ios_api_key_here"` with your actual Amplitude API key.

### How Initialization Works

Both platforms use **shared initialization logic** in `commonMain`:

- Amplitude is initialized automatically when the App composable loads
- Android gets the API key from `BuildConfig` (loaded from `local.properties`)
- iOS gets the API key from the `AmplitudeConfig.ios.kt` file
- All initialization logic is shared in `AmplitudeInitializer.kt`
- No platform-specific initialization code needed!

## Learning Path

We recommend exploring the samples in this order:

1. **Basic Events** (`BasicSampleScreen.kt`)
   - Simple event tracking
   - Events with properties
   - Events with options

2. **Advanced Features** (`AdvancedSampleScreen.kt`)
   - User properties (Identify)
   - Groups
   - Revenue tracking

3. **Initialization** (`MainApplication.kt` / `MainViewController.kt`)
   - Configuration options
   - Autocapture settings
   - Custom plugins

## Migration from Android SDK

These samples are designed to demonstrate API compatibility. If you're migrating from the Android SDK:

1. Change imports from `com.amplitude.android.*` to `com.amplitude.kmp.*`
2. On Android, change `androidContext` in Configuration
3. All other code remains identical!

See [Migration Guide](../README.md#migration-from-android-sdk) for details.

## Additional Resources

- [Amplitude KMP SDK Documentation](../README.md)
- [Android Sample README](kotlin-kmp-app/README.md)
- [Amplitude Developer Docs](https://www.docs.developers.amplitude.com/)
