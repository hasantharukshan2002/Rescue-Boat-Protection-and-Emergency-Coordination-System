#include <Arduino.h>
#include <SoftwareSerial.h>
#include <TinyGPS++.h>

// GPS Module pins (as per your wiring diagram)
#define GPS_RX_PIN 4  // Arduino D4 -> GPS TX
#define GPS_TX_PIN 3  // Arduino D3 -> GPS RX

// Create GPS object and software serial
TinyGPSPlus gps;
SoftwareSerial gpsSerial(GPS_RX_PIN, GPS_TX_PIN);

// Timer for updating location every 5 seconds
unsigned long lastUpdateTime = 0;
const unsigned long updateInterval = 5000; // 5 seconds in milliseconds

void setup()
{
  // Start Serial Monitor
  Serial.begin(9600);
  Serial.println("GPS Module Test");
  Serial.println("Waiting for GPS data...");
  Serial.println("Location will update every 5 seconds");
  
  // Start GPS Serial communication
  gpsSerial.begin(9600);
}

void loop()
{
  // Read data from GPS module
  while (gpsSerial.available() > 0)
  {
    char c = gpsSerial.read();
    // Parse GPS data (but don't display raw data)
    gps.encode(c);
  }
  
  // Check if 30 seconds have passed
  unsigned long currentTime = millis();
  if (currentTime - lastUpdateTime >= updateInterval)
  {
    lastUpdateTime = currentTime;
    
    // If location is valid, display it
    if (gps.location.isValid())
    {
      Serial.println();
      Serial.println("========== GPS DATA ==========");
      Serial.print("Latitude : ");
      Serial.println(gps.location.lat(), 6);
      Serial.print("Longitude: ");
      Serial.println(gps.location.lng(), 6);
      Serial.print("Altitude : ");
      Serial.println(gps.altitude.meters());
      Serial.print("Satellites: ");
      Serial.println(gps.satellites.value());
      Serial.print("Speed    : ");
      Serial.print(gps.speed.kmph());
      Serial.println(" km/h");
      
      // Date and Time
      if (gps.date.isValid())
      {
        Serial.print("Date     : ");
        Serial.print(gps.date.day());
        Serial.print("/");
        Serial.print(gps.date.month());
        Serial.print("/");
        Serial.println(gps.date.year());
      }
      
      if (gps.time.isValid())
      {
        Serial.print("Time     : ");
        if (gps.time.hour() < 10) Serial.print("0");
        Serial.print(gps.time.hour());
        Serial.print(":");
        if (gps.time.minute() < 10) Serial.print("0");
        Serial.print(gps.time.minute());
        Serial.print(":");
        if (gps.time.second() < 10) Serial.print("0");
        Serial.println(gps.time.second());
      }
      Serial.println("==============================");
      Serial.println();
    }
    else
    {
      Serial.println("Waiting for GPS fix... (Satellites: " + String(gps.satellites.value()) + ")");
    }
  }
  
  // Check if no data received for 5 seconds
  if (millis() > 5000 && gps.charsProcessed() < 10)
  {
    Serial.println("No GPS data received. Check wiring!");
    delay(1000);
  }
}