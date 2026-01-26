# LoRa Boat Flip Detection - Android App

A real-time monitoring Android application for the LoRa-based Boat Flip Detection System. This app connects to the embedded LoRa system via Bluetooth and displays critical sensor data and alert notifications.

## Features

- **Bluetooth Connectivity**: Seamlessly connects to LoRa Bluetooth module
- **Real-time Monitoring**: Displays live boat status and sensor readings
- **Flip Alert System**: Immediate visual and notification alerts when boat flips
- **Comprehensive Sensor Display**:
  - Boat ID tracking
  - Signal strength indicator
  - Battery level monitoring
  - Temperature readings
  - GPS location coordinates (Latitude/Longitude)
- **Device Management**: Scan and select from paired Bluetooth devices
- **Responsive UI**: Material Design interface with smooth interactions
- **Last Update Timestamp**: Tracks when data was last received

## Project Structure

```
LoRaBoatApp/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/lora/boatapp/
│   │       │   ├── MainActivity.java          # Main monitoring screen
│   │       │   ├── DeviceListActivity.java    # Bluetooth device selection
│   │       │   ├── BluetoothManager.java      # Bluetooth communication handler
│   │       │   └── BoatData.java              # Data model and parser
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   ├── activity_main.xml      # Main UI layout
│   │       │   │   └── activity_device_list.xml # Device selection UI
│   │       │   ├── values/
│   │       │   │   ├── strings.xml            # String resources
│   │       │   │   ├── colors.xml             # Color definitions
│   │       │   │   └── themes.xml             # Theme configuration
│   │       │   └── drawable/
│   │       │       └── card_background.xml    # Card styling
│   │       └── AndroidManifest.xml            # App manifest with permissions
│   ├── build.gradle                           # App-level build configuration
│   └── proguard-rules.pro
├── build.gradle                               # Project-level build configuration
├── settings.gradle                            # Project settings
└── gradle.properties                          # Gradle properties

```

## Requirements

- **Android Version**: Minimum SDK 21 (Android 5.0), Target SDK 33
- **Bluetooth**: Device must support Bluetooth (required)
- **Permissions**: Bluetooth, Location services required
- **Java**: 11 or higher

## Installation

### Prerequisites
- Android Studio (2023.1 or later)
- Android SDK 33+
- Gradle 8.0+

### Steps

1. **Open the Project**
   ```bash
   # Navigate to the app directory
   cd android-app/LoRaBoatApp
   ```

2. **Open in Android Studio**
   - File → Open → Select the LoRaBoatApp folder

3. **Sync Gradle**
   - Click "Sync Now" when prompted

4. **Build the App**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)

5. **Run on Device/Emulator**
   - Connect Android device (or start emulator)
   - Run → Run 'app'

## Usage

### Initial Setup

1. **Launch the App**: Open LoRa Boat Monitor on your Android device

2. **Connect to Device**:
   - Click "Connect Device" button
   - Select your Bluetooth LoRa module from the paired devices list
   - Wait for connection confirmation

3. **Monitor Data**:
   - View real-time boat status and sensor readings
   - Observe signal strength and battery level
   - Monitor GPS coordinates

4. **Flip Alert**:
   - If the boat flips, a prominent red alert box will appear
   - The flip status will change from "NORMAL" to "FLIPPED!"

### Data Format

The app expects Bluetooth data in the following format:

```
ID:1,FLIP:0,SIGNAL:75,BATTERY:85,TEMP:28.5,LAT:12.3456,LON:-45.6789
```

**Field Descriptions:**
- `ID`: Boat identifier (0-255)
- `FLIP`: Flip status (0=normal, 1=flipped)
- `SIGNAL`: Signal strength (0-100%)
- `BATTERY`: Battery level (0-100%)
- `TEMP`: Temperature in Celsius
- `LAT`: Latitude coordinate
- `LON`: Longitude coordinate

## Architecture

### Bluetooth Communication
- **BluetoothManager.java**: Handles all Bluetooth operations
  - Connection establishment
  - Data transmission and reception
  - Connection state management
  - Error handling

### Data Model
- **BoatData.java**: Parses and stores sensor data
  - Automatic parsing of incoming Bluetooth messages
  - Data validation
  - Timestamp tracking

### UI Components
- **MainActivity.java**: Main monitoring interface
  - Real-time data updates
  - Alert notifications
  - UI state management
- **DeviceListActivity.java**: Bluetooth device selection
  - Scan for paired devices
  - Permission handling
  - Device connection setup

## Permissions

The app requires the following permissions:

```xml
android.permission.BLUETOOTH              <!-- Connect to devices -->
android.permission.BLUETOOTH_ADMIN        <!-- Enable/disable Bluetooth -->
android.permission.BLUETOOTH_CONNECT       <!-- Connect to paired devices (Android 12+) -->
android.permission.BLUETOOTH_SCAN          <!-- Discover devices (Android 12+) -->
android.permission.ACCESS_FINE_LOCATION   <!-- GPS location access -->
android.permission.ACCESS_COARSE_LOCATION <!-- Approximate location -->
```

### Runtime Permissions (Android 6.0+)

The app requests Bluetooth permissions at runtime:
- Users must grant permissions after app launch
- Permissions required for device scanning and connection

## Customization

### Modify Data Format

Edit the `parseData()` method in [BoatData.java](app/src/main/java/com/lora/boatapp/BoatData.java#L30) to match your LoRa system's output format.

### Change UI Colors

Edit [colors.xml](app/src/main/res/values/colors.xml) and [themes.xml](app/src/main/res/values/themes.xml).

### Add Additional Sensors

1. Add fields to `BoatData` class
2. Add UI elements to [activity_main.xml](app/src/main/res/layout/activity_main.xml)
3. Update parser in `BoatData.parseData()`
4. Update UI refresh in `MainActivity.updateUI()`

## Integration with LoRa Arduino System

The Android app expects the Arduino to transmit formatted data over Bluetooth. Example Arduino code:

```cpp
// Send data over Bluetooth with proper formatting
void sendBoatData() {
    String data = "ID:" + String(BOAT_ID) + 
                  ",FLIP:" + String(isFlipped ? 1 : 0) +
                  ",SIGNAL:" + String(signalStrength) +
                  ",BATTERY:" + String(batteryLevel) +
                  ",TEMP:" + String(temperature) +
                  ",LAT:" + String(latitude) +
                  ",LON:" + String(longitude);
    Serial.println(data);
}
```

## Troubleshooting

### App Won't Connect
- Ensure Bluetooth is enabled on device
- Verify device is paired in Bluetooth settings
- Check Android version supports Bluetooth (requires Android 5.0+)

### Permission Errors
- Grant all requested permissions when prompted
- Check app permissions in Settings → Apps → LoRa Boat Monitor

### Data Not Displaying
- Verify LoRa module is transmitting correct data format
- Check Bluetooth connection status
- Monitor Logcat for error messages

### Battery Drains Quickly
- Reduce Bluetooth communication frequency
- Close app when not monitoring
- Disable location services if not needed

## Dependencies

- AndroidX AppCompat: 1.6.1
- Material Components: 1.9.0
- ConstraintLayout: 2.1.4
- SwipeRefreshLayout: 1.1.0

## License

This project is part of the Embedded Systems course and the LoRa-based Boat Flip Detection System.

## Support

For issues or questions:
1. Check the troubleshooting section
2. Review Logcat logs for error messages
3. Verify data format matches expected structure
4. Ensure all permissions are granted

## Future Enhancements

- Historical data logging and chart visualization
- Alert notifications and push notifications
- Multiple boat monitoring
- Map view for location tracking
- Data export and reporting features
- Enhanced security with encryption
