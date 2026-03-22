<p align="center">
  <img src="images/cover.png" width="900">
</p>

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

---

## ✨ Features

- Boat flip detection using MPU6050  
- Wireless emergency alert  
- Multi-boat communication  
- Embedded microcontroller control  
- Real-time warning system  
- Expandable for GPS  

---

## 🔧 Hardware Used

- ATmega328P / Arduino / ESP32  
- HC12 Wireless Transceiver  
- MPU6050 Sensor  
- GPS Module (optional)  
- Buzzer / LED alert  

---

## 📡 Communication Module

Originally designed using LoRa SX1278, but replaced with HC12 transceiver due to configuration complexity and unstable communication during testing.

HC12 provides simple UART communication and stable wireless connection suitable for boat coordination.

---

## 📂 Repository Structure

Docs → Documents  
Simulations → Circuit files  
coding → Embedded code  
esp32/lora → Communication tests  
.idea → IDE files  

---

## 🚀 Future Improvements

- LoRa mesh network  
- GSM alert system  
- Base station monitoring  
- Mobile app  
- AI detection  
