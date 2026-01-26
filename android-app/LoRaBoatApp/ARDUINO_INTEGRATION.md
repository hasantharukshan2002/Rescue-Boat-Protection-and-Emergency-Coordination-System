# Arduino to Android Bluetooth Integration Guide

This guide explains how to modify your LoRa Arduino code to work with the Android app.

## Overview

The Android app expects data in a specific format sent over Bluetooth. Your Arduino needs to:
1. Connect to a Bluetooth module (HC-05 or similar)
2. Format sensor data properly
3. Transmit data at regular intervals

## Bluetooth Module Setup

### Supported Modules
- HC-05 (Classic Bluetooth)
- HM-10 (Bluetooth Low Energy - BLE)

### Wiring Example (HC-05)
```
HC-05 VCC   → Arduino 5V
HC-05 GND   → Arduino GND
HC-05 TX    → Arduino RX (via voltage divider)
HC-05 RX    → Arduino TX
```

## Data Format

Send data as comma-separated key-value pairs:

```
ID:1,FLIP:0,SIGNAL:75,BATTERY:85,TEMP:28.5,LAT:12.3456,LON:-45.6789
```

### Field Specifications

| Field | Type | Range | Description |
|-------|------|-------|-------------|
| ID | Integer | 0-255 | Unique boat identifier |
| FLIP | Integer | 0 or 1 | 0=Normal, 1=Flipped |
| SIGNAL | Integer | 0-100 | LoRa signal strength (%) |
| BATTERY | Integer | 0-100 | Battery level (%) |
| TEMP | Float | -40 to 125 | Temperature in °C |
| LAT | Float | -90 to 90 | Latitude coordinate |
| LON | Float | -180 to 180 | Longitude coordinate |

## Integration Code Example

Add this to your existing Arduino sketch:

```cpp
#include <Wire.h>
#include <SPI.h>
#include <LoRa.h>
#include <SoftwareSerial.h>  // For Bluetooth module

// ===== BLUETOOTH SETUP =====
SoftwareSerial btSerial(8, 9);  // RX, TX pins for Bluetooth module

// ===== GLOBAL VARIABLES =====
#define BOAT_ID 1
unsigned long lastTransmit = 0;
const unsigned long TRANSMIT_INTERVAL = 5000;  // Send data every 5 seconds

// Sensor variables
struct SensorData {
    int boatId = BOAT_ID;
    bool isFlipped = false;
    int signalStrength = 0;
    int batteryLevel = 100;
    float temperature = 25.0;
    double latitude = 0.0;
    double longitude = 0.0;
} sensorData;

void setup() {
    Serial.begin(9600);      // Serial monitor
    btSerial.begin(9600);    // HC-05 module uses 9600 baud rate
    
    // Your existing setup code...
    // Initialize LoRa, MPU6050, etc.
}

void loop() {
    // Your existing loop code...
    
    // Send data to Android app at regular intervals
    if (millis() - lastTransmit >= TRANSMIT_INTERVAL) {
        updateSensorData();
        sendToAndroidApp();
        lastTransmit = millis();
    }
}

/**
 * Update sensor readings from your devices
 * Modify this function based on your actual sensors
 */
void updateSensorData() {
    // Update flip detection from MPU6050
    sensorData.isFlipped = checkIfFlipped();
    
    // Update signal strength from LoRa RSSI
    sensorData.signalStrength = map(LoRa.packetRssi(), -120, -40, 0, 100);
    
    // Update battery level (if you have a battery monitor)
    sensorData.batteryLevel = readBatteryLevel();
    
    // Update temperature (if you have a temperature sensor)
    sensorData.temperature = readTemperature();
    
    // Update GPS coordinates (if you have GPS module)
    readGPS(sensorData.latitude, sensorData.longitude);
}

/**
 * Send formatted data to Android app via Bluetooth
 */
void sendToAndroidApp() {
    String data = "ID:" + String(sensorData.boatId) +
                  ",FLIP:" + String(sensorData.isFlipped ? 1 : 0) +
                  ",SIGNAL:" + String(sensorData.signalStrength) +
                  ",BATTERY:" + String(sensorData.batteryLevel) +
                  ",TEMP:" + String(sensorData.temperature, 1) +
                  ",LAT:" + String(sensorData.latitude, 6) +
                  ",LON:" + String(sensorData.longitude, 6);
    
    btSerial.println(data);
    
    // Also print to serial monitor for debugging
    Serial.print("Sent to App: ");
    Serial.println(data);
}

/**
 * Check if boat has flipped (from your MPU6050 code)
 */
bool checkIfFlipped() {
    // Your existing flip detection code
    // Read MPU6050 accelerometer/gyro data
    // Calculate tilt angle
    // Return true if angle exceeds threshold
    
    return false;  // Placeholder
}

/**
 * Read battery level
 * Requires analog pin connected to battery voltage divider
 */
int readBatteryLevel() {
    int analogValue = analogRead(A0);  // Change to your battery monitor pin
    int voltage = map(analogValue, 0, 1023, 0, 5000);  // Convert to mV
    int batteryPercent = map(voltage, 3000, 4200, 0, 100);  // 3V min, 4.2V max
    return constrain(batteryPercent, 0, 100);
}

/**
 * Read temperature
 * Requires temperature sensor (e.g., DS18B20, DHT22, etc.)
 */
float readTemperature() {
    // Your existing temperature reading code
    // Example for analog sensor:
    // int raw = analogRead(A1);
    // float temp = (raw / 1023.0) * 125 - 40;  // Adjust formula for your sensor
    
    return 25.0;  // Placeholder
}

/**
 * Read GPS coordinates
 * Requires GPS module connected (e.g., NEO-6M)
 */
void readGPS(double &lat, double &lon) {
    // Your existing GPS reading code
    // Parse NMEA sentences and extract coordinates
    
    // Placeholder
    lat = 12.3456;
    lon = -45.6789;
}
```

## Bluetooth Module Configuration

### HC-05 Default Settings
- Baud Rate: 9600
- Name: HC-05
- Pin: 1234
- Mode: Slave (waits for connection)

### AT Commands to Configure (Optional)
```
// Use serial monitor with Arduino to send these commands
AT              // Test connection
AT+NAME=BoatLoRa  // Set device name
AT+PSWD=1234    // Set password
AT+UART=9600,0,0  // Set baud rate
```

## Debugging

### Serial Monitor Output
Use the Serial Monitor to verify data format:
```
Sent to App: ID:1,FLIP:0,SIGNAL:75,BATTERY:85,TEMP:28.5,LAT:12.3456,LON:-45.6789
```

### Android Logcat
Check Android Studio Logcat for received data:
```
D/BluetoothManager: Data sent: ID:1,FLIP:0,SIGNAL:75,BATTERY:85,TEMP:28.5,LAT:12.3456,LON:-45.6789
```

## Troubleshooting

### App Can't Find Device
- Ensure Bluetooth module is powered
- Verify module is in pairing mode
- Check if device appears in Android Bluetooth settings

### Data Not Appearing
- Verify baud rate (usually 9600)
- Check data format matches specification
- Ensure newline character is sent (`println()`)
- Monitor serial output to verify Arduino is sending data

### Connection Drops
- Check Bluetooth module power supply
- Verify antenna placement
- Reduce transmit interval if needed
- Check for interference

### Garbled Data
- Verify baud rate matches (9600 most common)
- Check voltage levels on RX pin (may need voltage divider)
- Ensure stable power supply

## Performance Tips

1. **Transmit Interval**: Adjust `TRANSMIT_INTERVAL` for your needs
   - 2000ms: Very responsive but uses more power
   - 5000ms: Balance of responsiveness and power
   - 10000ms: Low power, less responsive

2. **Data Precision**: Reduce decimal places if needed
   ```cpp
   String(sensorData.temperature, 1)  // 1 decimal place
   String(sensorData.latitude, 4)     // 4 decimal places
   ```

3. **Power Optimization**: Send data only when needed
   ```cpp
   if (sensorData.isFlipped || dataChanged) {
       sendToAndroidApp();
   }
   ```

## Example: Complete Minimal Sketch

```cpp
#include <SoftwareSerial.h>

SoftwareSerial btSerial(8, 9);  // RX, TX for Bluetooth

void setup() {
    Serial.begin(9600);
    btSerial.begin(9600);
}

void loop() {
    // Simulate sensor data
    String data = "ID:1,FLIP:0,SIGNAL:85,BATTERY:90,TEMP:28.5,LAT:40.7128,LON:-74.0060";
    btSerial.println(data);
    Serial.println("Sent: " + data);
    
    delay(5000);  // Send every 5 seconds
}
```

## Next Steps

1. Implement sensor reading functions for your specific hardware
2. Configure Bluetooth module baud rate to match your Arduino
3. Test communication with Serial Monitor
4. Deploy on Android device
5. Monitor Logcat for any data parsing errors
