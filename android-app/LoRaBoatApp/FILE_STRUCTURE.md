# Android App - File Structure Reference

## Complete Project Layout

```
android-app/
└── LoRaBoatApp/                           # Main project folder
    ├── .gradle/                           # Gradle cache (auto-generated)
    ├── .idea/                             # Android Studio config (auto-generated)
    │
    ├── app/                               # App module
    │   ├── build/                         # Build outputs (auto-generated)
    │   │   └── outputs/apk/
    │   │       └── debug/app-debug.apk   # Compiled app (for testing)
    │   │
    │   ├── src/
    │   │   ├── androidTest/               # Android instrumented tests
    │   │   │   └── java/com/lora/boatapp/
    │   │   │
    │   │   ├── test/                      # Unit tests
    │   │   │   └── java/com/lora/boatapp/
    │   │   │
    │   │   └── main/                      # Main app source
    │   │       ├── AndroidManifest.xml    # App permissions & configuration
    │   │       │
    │   │       ├── java/com/lora/boatapp/
    │   │       │   ├── MainActivity.java                 # Main monitoring screen
    │   │       │   ├── DeviceListActivity.java           # Bluetooth device selection
    │   │       │   ├── BluetoothManager.java             # Bluetooth communication
    │   │       │   └── BoatData.java                     # Data model & parser
    │   │       │
    │   │       └── res/                  # Resources
    │   │           ├── drawable/
    │   │           │   └── card_background.xml           # Card UI styling
    │   │           │
    │   │           ├── layout/
    │   │           │   ├── activity_main.xml             # Main UI layout
    │   │           │   ├── activity_device_list.xml      # Device selection UI
    │   │           │   └── item_device.xml               # Device list item
    │   │           │
    │   │           ├── mipmap/
    │   │           │   ├── ic_launcher.xml
    │   │           │   └── ic_launcher_round.xml
    │   │           │
    │   │           └── values/
    │   │               ├── colors.xml                     # Color definitions
    │   │               ├── strings.xml                    # Text strings
    │   │               └── themes.xml                     # App theming
    │   │
    │   ├── build.gradle                  # App-level build configuration
    │   └── proguard-rules.pro             # Code obfuscation rules
    │
    ├── gradle/wrapper/                    # Gradle wrapper (auto-generated)
    │
    ├── build.gradle                       # Project-level build configuration
    ├── settings.gradle                    # Gradle project settings
    ├── gradle.properties                  # Gradle configuration
    ├── gradlew                            # Gradle wrapper script (Linux/Mac)
    ├── gradlew.bat                        # Gradle wrapper script (Windows)
    │
    ├── .gitignore                         # Git ignore patterns
    ├── local.properties                   # Local SDK paths (auto-generated)
    │
    ├── README.md                          # Complete technical documentation
    ├── QUICKSTART.md                      # Quick setup guide
    ├── ARDUINO_INTEGRATION.md             # Arduino integration guide
    └── PROJECT_SUMMARY.md                 # Project overview
```

## Key Files Explained

### Configuration Files

**build.gradle (Project-level)**
```gradle
// Defines plugins and repositories
pluginManagement { ... }
dependencyResolutionManagement { ... }
```

**build.gradle (App-level)**
```gradle
// Defines app build settings
android { ... }
dependencies { ... }
```

**settings.gradle**
```gradle
// Includes app module in build
include ':app'
```

**gradle.properties**
```properties
// Gradle JVM arguments
org.gradle.jvmargs=-Xmx2048m
```

### Android Manifest

**AndroidManifest.xml**
- App permissions (BLUETOOTH, LOCATION)
- Activities declaration
- Feature requirements
- Broadcast receivers (if any)

### Source Code

**MainActivity.java**
- Primary user interface
- Real-time data display
- Button handlers
- UI updates from Bluetooth data

**DeviceListActivity.java**
- Bluetooth device scanning
- Device list presentation
- Device selection handling
- Permission management

**BluetoothManager.java**
- Bluetooth socket management
- Connection/disconnection logic
- Data transmission and reception
- Background read thread for incoming data

**BoatData.java**
- Data model class
- CSV parsing logic
- Field validation
- Data formatting

### Resources

**Layouts (XML)**
- `activity_main.xml` - Main monitoring interface
- `activity_device_list.xml` - Device selection screen
- `item_device.xml` - Device list item template

**Styling (XML)**
- `colors.xml` - Color palette
- `themes.xml` - Material Design theme
- `card_background.xml` - Card view styling

**Strings (XML)**
- `strings.xml` - All text strings used in app
- Easy localization support

### Documentation

**README.md** (Complete Reference)
- Architecture overview
- Feature description
- Installation instructions
- Usage guide
- Customization options
- Troubleshooting section

**QUICKSTART.md** (Fast Setup)
- Step-by-step installation
- Basic configuration
- Initial usage
- Troubleshooting quick tips

**ARDUINO_INTEGRATION.md** (Hardware Guide)
- Arduino code examples
- Bluetooth module setup
- Data format specification
- Debugging tips

**PROJECT_SUMMARY.md** (Overview)
- Project overview
- What's included
- Next steps
- Version information

## Build Output Locations

After building, files appear at:

```
app/build/
├── intermediates/      # Temporary build files
├── outputs/
│   └── apk/
│       ├── debug/
│       │   └── app-debug.apk              # Testing APK
│       └── release/
│           └── app-release.apk            # Production APK
└── ...
```

## Dependencies

All dependencies are managed by Gradle:

```gradle
// In build.gradle (app-level)

dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'
}
```

## File Naming Conventions

### Java Classes
- `MainActivity.java` - Main activity
- `DeviceListActivity.java` - Secondary activity
- `BluetoothManager.java` - Utility class (manager)
- `BoatData.java` - Data model class

### XML Layouts
- `activity_main.xml` - Associated with MainActivity
- `activity_device_list.xml` - Associated with DeviceListActivity
- `item_device.xml` - Reusable item layout

### XML Resources
- `colors.xml` - Color definitions
- `strings.xml` - String resources
- `themes.xml` - Theme definitions
- `*.xml` in drawable/ - Vector/shape drawables

## IDE Integration

### Android Studio File Structure View
```
Android view shows:
- app (project)
  - manifests
  - kotlin+java
  - res
  - gradle scripts
```

### Important Folders for Development

| Folder | Usage |
|--------|-------|
| `app/src/main/java` | Write Java code here |
| `app/src/main/res` | Add layouts, strings, colors |
| `app/src/main` | AndroidManifest.xml goes here |
| `app/src/test` | Write unit tests here |
| `app/build/outputs` | Find compiled APKs here |

## Quick File Lookup

Need to find/edit something?

| Want to... | Edit file... |
|-----------|-------------|
| Add UI element | `activity_main.xml` |
| Change text | `strings.xml` |
| Change colors | `colors.xml` |
| Add Bluetooth logic | `BluetoothManager.java` |
| Parse new data fields | `BoatData.java` |
| Handle button clicks | `MainActivity.java` |
| Add permissions | `AndroidManifest.xml` |
| Change app icon | `mipmap/` folder |

## Generated Files (Don't Edit)

These are automatically generated - don't modify:

- `.gradle/` folder
- `.idea/` folder
- `app/build/` folder
- `local.properties`
- `R.java` (generated resources file)
- `BuildConfig.java`

## Version Control (.gitignore)

Files automatically ignored by Git:

```
.gradle/
.idea/
*.iml
build/
local.properties
```

## Next Steps

1. **Explore**: Open project in Android Studio
2. **Review**: Read README.md for architecture details
3. **Customize**: Modify colors, strings, layout as needed
4. **Build**: Follow QUICKSTART.md to build APK
5. **Deploy**: Install and test on Android device
