package com.lora.boatapp;

public class BoatData {
    public int boatId;
    public boolean isFlipped;
    public int signalStrength;
    public int batteryLevel;
    public double temperature;
    public double latitude;
    public double longitude;
    public long timestamp;

    public BoatData() {
        this.boatId = -1;
        this.isFlipped = false;
        this.signalStrength = 0;
        this.batteryLevel = 0;
        this.temperature = 0.0;
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Parse incoming data from Bluetooth
     * Expected format: "ID:1,FLIP:0,SIGNAL:75,BATTERY:85,TEMP:28.5,LAT:12.3456,LON:-45.6789"
     */
    public static BoatData parseData(String data) {
        BoatData boatData = new BoatData();
        try {
            String[] parts = data.split(",");
            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();

                    switch (key) {
                        case "ID":
                            boatData.boatId = Integer.parseInt(value);
                            break;
                        case "FLIP":
                            boatData.isFlipped = Integer.parseInt(value) == 1;
                            break;
                        case "SIGNAL":
                            boatData.signalStrength = Integer.parseInt(value);
                            break;
                        case "BATTERY":
                            boatData.batteryLevel = Integer.parseInt(value);
                            break;
                        case "TEMP":
                            boatData.temperature = Double.parseDouble(value);
                            break;
                        case "LAT":
                            boatData.latitude = Double.parseDouble(value);
                            break;
                        case "LON":
                            boatData.longitude = Double.parseDouble(value);
                            break;
                    }
                }
            }
            boatData.timestamp = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boatData;
    }

    @Override
    public String toString() {
        return "BoatData{" +
                "boatId=" + boatId +
                ", isFlipped=" + isFlipped +
                ", signalStrength=" + signalStrength +
                ", batteryLevel=" + batteryLevel +
                ", temperature=" + temperature +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
