<div align="center">

# 🚤 Rescue Boat Protection and Emergency Coordination System

### Embedded Wireless Safety System for Boat Accident Detection

![Platform](https://img.shields.io/badge/Platform-Embedded-blue)
![MCU](https://img.shields.io/badge/Microcontroller-ATmega328P%20%7C%20ESP32-green)
![Communication](https://img.shields.io/badge/Wireless-HC12-orange)
![Sensor](https://img.shields.io/badge/Sensor-MPU6050-red)
![Status](https://img.shields.io/badge/Status-Development-yellow)

</div>

---

## 📌 Overview

This project implements a wireless safety and coordination system for boats using motion sensors, microcontroller processing, and RF communication.

When a boat flip is detected, an emergency signal is transmitted to nearby boats to provide real-time warning and coordination.

The system is designed using embedded hardware modules and wireless transceivers suitable for marine environments.

---

## ✨ Features

* 🚨 Boat flip detection
* 📡 Wireless emergency alert
* 🔗 Multi-boat communication
* ⚙️ Embedded microcontroller control
* 🔔 Real-time warning system
* 📍 Expandable for GPS support
* 🔋 Low power design

---

## 🔧 Hardware Used

* ATmega328P / Arduino / ESP32
* HC12 Wireless Transceiver Module
* MPU6050 Accelerometer & Gyroscope
* GPS Module (optional)
* Buzzer / LED alert
* Voltage regulator
* Power supply module

---

## 📡 Communication Module

The system was initially designed using **LoRa SX1278** for long-range communication.

During testing, configuration complexity and unstable connection were observed, therefore the module was replaced with **HC12 transceiver**, which provides:

* Simple UART communication
* Easy configuration
* Stable data transmission
* Suitable range for boat coordination
* Faster hardware implementation

---

## ⚙️ Working Principle

1. MPU6050 detects abnormal tilt
2. Microcontroller checks flip condition
3. Emergency signal generated
4. HC12 transmits alert message
5. Nearby boats receive warning
6. Buzzer / LED activated

---

## 📂 Repository Structure

Docs/ → Project documents
Simulations/ → EasyEDA circuit files
coding/ → Embedded source code
esp32/lora/ → Wireless communication tests
.idea/ → IDE workspace files

---

## 🌊 Applications

* Fishing boat safety system
* Rescue boat coordination
* Marine accident alert system
* Small vessel monitoring
* Coastal safety projects

---

## 🚀 Future Improvements

* LoRa mesh communication
* GSM / LTE alert system
* Base station monitoring
* Mobile application
* AI-based accident detection

---

## 📜 License

This project is for educational and research purposes.
