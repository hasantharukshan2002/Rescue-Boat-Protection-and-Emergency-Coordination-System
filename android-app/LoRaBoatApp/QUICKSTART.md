# Android App - Quick Start Guide

## Installation Steps

### Step 1: Open Project in Android Studio
1. Download and install Android Studio (version 2023.1 or later)
2. Open Android Studio
3. Select "Open" → Navigate to `android-app/LoRaBoatApp` folder
4. Wait for Gradle sync to complete

### Step 2: Set Up Emulator or Device
**Option A: Using Physical Device**
- Enable Developer Mode: Settings → About → Tap Build Number 7 times
- Enable USB Debugging: Settings → Developer Options → USB Debugging
- Connect via USB cable

**Option B: Using Emulator**
- In Android Studio: Tools → Device Manager → Create Virtual Device
- Select Pixel 4 device definition
- Choose API 33 system image
- Complete setup

### Step 3: Build and Run
1. Click "Run" button (green play icon)
2. Select your device/emulator
3. Wait for app to compile and install

## Initial Configuration

### For LoRa System Communication

1. **Bluetooth Module Setup**:
   - Ensure HC-05 or similar Bluetooth module is paired with your Android phone
   - Use phone Settings → Bluetooth to pair with BoatLoRa (default name)

2. **Arduino Code**:
   - Modify your Arduino code using the provided [ARDUINO_INTEGRATION.md](ARDUINO_INTEGRATION.md) guide
   - Program the microcontroller with Bluetooth support

3. **Data Format**:
   - Arduino must send data in format: `ID:1,FLIP:0,SIGNAL:75,BATTERY:85,TEMP:28.5,LAT:12.3456,LON:-45.6789`
   - See integration guide for details

## App Usage

1. **Launch App**
   - Open LoRa Boat Monitor from your app drawer

2. **Connect to Device**
   - Click "Connect Device" button
   - Select your Bluetooth LoRa module from list
   - Wait for "Connected" status

3. **Monitor Data**
   - View boat status in real-time
   - Check signal strength, battery, temperature
   - Observe GPS coordinates

4. **Flip Alert**
   - Red alert appears if boat flips
   - Text changes to "FLIPPED!"

## Key Files and Locations

| File | Purpose |
|------|---------|
| `MainActivity.java` | Main monitoring UI |
| `DeviceListActivity.java` | Device selection screen |
| `BluetoothManager.java` | Bluetooth communication |
| `BoatData.java` | Data parsing and model |
| `activity_main.xml` | Main UI layout |
| `AndroidManifest.xml` | App permissions and configuration |

## Permissions

The app requests these permissions on first launch:
- Bluetooth permissions (to connect to devices)
- Location permissions (for GPS coordinates)

**Grant all permissions when prompted for full functionality.**

## Troubleshooting

### App Crashes on Launch
- Ensure Android 5.0 (API 21) or higher
- Check all permissions are granted

### Can't Connect to Device
- Verify device is paired in phone Bluetooth settings
- Restart Bluetooth on both devices
- Check device name in Android settings

### No Data Appearing
- Verify Arduino is transmitting data correctly
- Check Bluetooth connection status shows "Connected"
- Monitor Logcat for error messages (View → Tool Windows → Logcat)

### Permission Denied Error
- Open app Settings → Permissions
- Enable all required permissions
- Restart the app

## Development and Customization

### Change UI Colors
Edit `app/src/main/res/values/colors.xml` and `themes.xml`

### Modify Data Format
Edit `BoatData.parseData()` method in `app/src/main/java/com/lora/boatapp/BoatData.java`

### Add New Sensor
1. Add field to `BoatData` class
2. Add UI element to `activity_main.xml`
3. Update parsing logic in `BoatData.parseData()`
4. Update `MainActivity.updateUI()` to display new data

## Project Structure

```
LoRaBoatApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/lora/boatapp/    # Java source code
│   │   ├── res/                       # Resources (layouts, strings, colors)
│   │   └── AndroidManifest.xml
│   ├── build.gradle                   # App dependencies
│   └── proguard-rules.pro
├── build.gradle                       # Project build config
├── settings.gradle                    # Gradle settings
└── README.md                          # Full documentation
```

## APK Build and Distribution

### Build APK
1. Build → Build Bundle(s) / APK(s) → Build APK(s)
2. APK file created at: `app/build/outputs/apk/debug/app-debug.apk`
3. Copy to device and install

### Build Signed APK (For Release)
1. Build → Build Bundle(s) / APK(s) → Build Signed Bundle / APK
2. Follow wizard to create signing certificate
3. Saves to `app/release/app-release.apk`

## Performance Optimization

### Reduce App Size
- Enable ProGuard: Set `minifyEnabled true` in `build.gradle`
- Remove unused resources

### Reduce Battery Drain
- Increase data transmission interval in Arduino code
- Disable Bluetooth when app is not active
- Use WiFi instead of Bluetooth if available

## Next Steps

1. Configure Arduino with Bluetooth module
2. Test data transmission with Serial Monitor
3. Deploy APK to Android device
4. Verify all features work correctly
5. Customize as needed for your system

## Support Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Bluetooth API Reference](https://developer.android.com/guide/topics/connectivity/bluetooth)
- Arduino Integration Guide (see ARDUINO_INTEGRATION.md)
- Main README for technical details

## Version Info

- **Target Android**: 12.0 and above
- **Minimum Android**: 5.0 (API 21)
- **Build Tools**: 33.0.0
- **Gradle**: 8.0.0
