# Amplitude KMP Sample App

This sample demonstrates how to use the Amplitude Kotlin Multiplatform SDK in a real application with **shared initialization logic** across Android and iOS.

## Features Demonstrated

### Basic Events (`BasicSampleScreen.kt`)
- Simple event tracking
- Events with properties
- Events with options and Plan

### Advanced Features (`AdvancedSampleScreen.kt`)
- **User Identification**: Set user properties using `Identify`
- **Groups**: Assign users to groups and set group properties
- **Revenue Tracking**: Track in-app purchases and revenue
- **Plugins**: Add custom enrichment plugins to modify events

## Setup

### 1. Configure API Keys

**Android:**

Create `local.properties` in the **root project directory** (`amplitude-kmp/local.properties`):

```properties
AMPLITUDE_API_KEY=your_android_api_key_here
```

**iOS:**

Edit `src/iosMain/kotlin/com/amplitude/kmp/sample/AmplitudeConfig.ios.kt`:

```kotlin
actual val amplitudeApiKey: String = "your_ios_api_key_here"
```

**Get your API keys**: https://analytics.amplitude.com/

> **Security Note**: `local.properties` is gitignored and will NOT be committed to version control.

### 2. Run the Sample

**Android:**
```bash
./gradlew :samples:kotlin-kmp-app:installDebug
```

**iOS:**
1. Open `samples/kotlin-kmp-app-ios/iosApp.xcodeproj` in Xcode
2. Xcode will automatically resolve Swift Package Manager dependencies (Amplitude Swift SDK)
3. Run on simulator or device

## Code Structure

```
src/
├── commonMain/          # Shared code for all platforms
│   ├── App.kt          # Main app UI with automatic Amplitude initialization
│   ├── AmplitudeInitializer.kt   # Shared initialization logic
│   ├── AmplitudeConfig.kt        # expect declarations for API key & context
│   ├── AmplitudeManager.kt       # Singleton to hold Amplitude instance
│   ├── Platform.kt               # Platform name utility
│   ├── BasicSampleScreen.kt      # Basic event tracking examples
│   └── AdvancedSampleScreen.kt   # Advanced features (Identify, Revenue, Groups)
├── androidMain/         # Android-specific code
│   ├── MainActivity.kt
│   ├── MainApplication.kt        # Stores Android context
│   ├── AmplitudeConfig.android.kt # Android API key from BuildConfig
│   └── Platform.android.kt       # Returns "Android"
└── iosMain/             # iOS-specific code
    ├── MainViewController.kt     # iOS entry point
    ├── AmplitudeConfig.ios.kt    # iOS API key (hardcoded)
    └── Platform.ios.kt           # Returns "iOS"
```

## Shared Initialization Pattern

This sample demonstrates **100% shared initialization** - all Amplitude configuration happens in `commonMain`:

### 1. Platform Configuration (expect/actual)

**commonMain/AmplitudeConfig.kt:**
```kotlin
expect val amplitudeApiKey: String
expect val platformContext: Any?  // Android Context or null for iOS
```

**androidMain/AmplitudeConfig.android.kt:**
```kotlin
internal var androidContext: Context? = null
actual val amplitudeApiKey: String = BuildConfig.AMPLITUDE_API_KEY
actual val platformContext: Any? get() = androidContext
```

**iosMain/AmplitudeConfig.ios.kt:**
```kotlin
actual val amplitudeApiKey: String = "YOUR_IOS_API_KEY_HERE"
actual val platformContext: Any? = null  // iOS doesn't need context
```

### 2. Shared Initialization Logic

**commonMain/AmplitudeInitializer.kt:**
```kotlin
object AmplitudeInitializer {
    suspend fun initializeAndAwait(
        apiKey: String,
        platformContext: Any? = null,
        userId: String? = null
    ): Amplitude {
        val configuration = Configuration(
            apiKey = apiKey,
            androidContext = platformContext  // Only used on Android
        ).apply {
            autocapture = setOf(
                AutocaptureOption.SESSIONS,
                AutocaptureOption.APP_LIFECYCLES,
                AutocaptureOption.SCREEN_VIEWS
            )
            flushQueueSize = 30
            flushIntervalMillis = 30000
        }

        val amplitude = Amplitude(configuration)

        // Add custom plugins
        amplitude.add(object : Plugin {
            override val type: Plugin.Type = Plugin.Type.ENRICHMENT
            override fun setup(amplitude: Amplitude) {}
            override fun execute(event: BaseEvent): BaseEvent {
                event.eventProperties = event.eventProperties?.toMutableMap() ?: mutableMapOf()
                event.eventProperties?.put("sdk_version", "kmp-1.0")
                event.eventProperties?.put("platform", getPlatformName())
                return event
            }
        })

        if (userId != null) {
            amplitude.setUserId(userId)
        }

        AmplitudeManager.initialize(amplitude)
        return amplitude
    }
}
```

### 3. Automatic Initialization

**commonMain/App.kt:**
```kotlin
@Composable
fun App() {
    // Initialize Amplitude once when app starts
    LaunchedEffect(Unit) {
        AmplitudeInitializer.initializeAndAwait(
            apiKey = amplitudeApiKey,           // From AmplitudeConfig (platform-specific)
            platformContext = platformContext,   // Android Context or null (iOS)
            userId = "kmp-sample-user-${getPlatformName()}"
        )
    }

    MaterialTheme {
        // Rest of the app UI...
    }
}
```

### 4. Platform Setup

**Android (MainApplication.kt):**
```kotlin
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Just store the context - initialization happens in App.kt
        androidContext = applicationContext
    }
}
```

**iOS (no code needed!):**

The iOS app just shows the Compose UI - initialization happens automatically:

```swift
@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()  // Shows the Compose UI
        }
    }
}
// No initialization code needed!
```

## Key API Examples

### Track Events

```kotlin
// Simple event
amplitude.track("button_clicked")

// Event with properties
amplitude.track(
    eventType = "purchase_completed",
    eventProperties = mapOf(
        "item_id" to "12345",
        "price" to 9.99,
        "currency" to "USD"
    )
)

// Event with options
val options = EventOptions().apply {
    plan = Plan(branch = "main", source = "mobile-app")
}
amplitude.track(
    eventType = "feature_used",
    eventProperties = mapOf("feature_name" to "search"),
    options = options
)
```

### User Properties (Identify)

```kotlin
val identify = Identify()
    .set("user_type", "premium")
    .set("preferences", mapOf("theme" to "dark"))
    .add("login_count", 1)

amplitude.identify(identify)
```

### Groups

```kotlin
// Set user's group
amplitude.setGroup("orgId", "15")
amplitude.setGroup("platform", arrayOf("iOS", "KMP"))

// Set group properties
val groupIdentify = Identify()
    .set("plan_type", "enterprise")
    .set("seats", 50)

amplitude.groupIdentify(
    groupType = "orgId",
    groupName = "15",
    identify = groupIdentify
)
```

### Revenue Tracking

```kotlin
val revenue = Revenue().apply {
    productId = "com.example.product"
    price = 9.99
    quantity = 1
    revenueType = "purchase"
    properties = mutableMapOf(
        "category" to "subscription",
        "platform" to getPlatformName()
    )
}

amplitude.revenue(revenue)
```

### Plugins

```kotlin
amplitude.add(object : Plugin {
    override val type: Plugin.Type = Plugin.Type.ENRICHMENT

    override fun setup(amplitude: Amplitude) {
        // Setup code
    }

    override fun execute(event: BaseEvent): BaseEvent {
        // Modify event
        event.eventProperties = event.eventProperties?.toMutableMap() ?: mutableMapOf()
        event.eventProperties?.put("custom_field", "value")
        return event
    }
})
```

## Migration from Android SDK

This sample demonstrates that migrating from the Android-only SDK to the KMP SDK requires **only import changes**:

```diff
- import com.amplitude.android.Amplitude
- import com.amplitude.android.Configuration
- import com.amplitude.core.events.Identify
- import com.amplitude.core.events.Revenue
+ import com.amplitude.kmp.Amplitude
+ import com.amplitude.kmp.Configuration
+ import com.amplitude.kmp.events.Identify
+ import com.amplitude.kmp.events.Revenue
```

All the rest of the code remains identical!

## Benefits of Shared Initialization

- ✅ **Single source of truth**: All initialization logic in `commonMain`
- ✅ **Automatic**: No manual platform-specific setup
- ✅ **Type-safe**: Expect/actual ensures compile-time safety
- ✅ **Testable**: Can test initialization logic in common tests
- ✅ **Maintainable**: Change once, applies everywhere
- ✅ **Consistent**: Same behavior across all platforms

## Resources

- [Main SDK Documentation](../../README.md)
- [iOS Sample README](../kotlin-kmp-app-ios/README.md)
- [Amplitude Developer Center](https://www.docs.developers.amplitude.com/)
- [Kotlin Multiplatform Documentation](https://kotlinlang.org/docs/multiplatform.html)
