#include <SPI.h>
#include <LoRa.h>

#define NSS   10
#define RST   9
#define DIO0  2

unsigned long lastPing = 0;
const unsigned long pingInterval = 5000;

bool receiverConnected = false;

bool sendAndWaitAck(String msg) {

  LoRa.beginPacket();
  LoRa.print(msg);
  LoRa.endPacket();

  LoRa.receive();   // switch to RX mode

  unsigned long start = millis();

  while (millis() - start < 1000) {
    int packetSize = LoRa.parsePacket();
    if (packetSize) {
      String resp = "";
      while (LoRa.available()) {
        resp += (char)LoRa.read();
      }
      if (resp == "ACK") return true;
    }
  }
  return false;
}

void setup() {
  Serial.begin(9600);
  while (!Serial);

  Serial.println("LoRa TRANSMITTER starting...");

  LoRa.setPins(NSS, RST, DIO0);

  if (!LoRa.begin(433E6)) {
    Serial.println("LoRa init failed!");
    while (1);
  }

  Serial.println("LoRa init OK");
}

void loop() {

  if (millis() - lastPing > pingInterval) {
    lastPing = millis();

    receiverConnected = sendAndWaitAck("PING");

    if (receiverConnected)
      Serial.println("RECEIVER CONNECTED");
    else
      Serial.println("RECEIVER NOT CONNECTED");
  }
}

  Serial.println("LoRa init OK");
}

void loop() {

  if (millis() - lastPing > pingInterval) {
    lastPing = millis();

    receiverConnected = sendAndWaitAck("PING");

    if (receiverConnected)
      Serial.println("RECEIVER CONNECTED");
    else
      Serial.println("RECEIVER NOT CONNECTED");
  }
}
