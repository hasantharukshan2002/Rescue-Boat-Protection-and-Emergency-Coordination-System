package com.lora.boatapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothManager {
    private static final String TAG = "BluetoothManager";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BluetoothConnectionListener listener;
    private boolean isConnected = false;
    private ReadThread readThread;

    public interface BluetoothConnectionListener {
        void onConnected();
        void onDisconnected();
        void onDataReceived(String data);
        void onError(String error);
    }

    public BluetoothManager(Context context) {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void setConnectionListener(BluetoothConnectionListener listener) {
        this.listener = listener;
    }

    public void connect(BluetoothDevice device) {
        new Thread(() -> {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
                isConnected = true;

                if (listener != null) {
                    new Handler(Looper.getMainLooper()).post(listener::onConnected);
                }

                // Start reading data
                readThread = new ReadThread();
                readThread.start();

                Log.d(TAG, "Connected to device: " + device.getName());
            } catch (IOException e) {
                isConnected = false;
                Log.e(TAG, "Connection failed: " + e.getMessage());
                if (listener != null) {
                    new Handler(Looper.getMainLooper()).post(
                        () -> listener.onError("Connection failed: " + e.getMessage())
                    );
                }
            }
        }).start();
    }

    public void disconnect() {
        isConnected = false;
        try {
            if (readThread != null) {
                readThread.interrupt();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            if (listener != null) {
                listener.onDisconnected();
            }
            Log.d(TAG, "Disconnected");
        } catch (IOException e) {
            Log.e(TAG, "Disconnect error: " + e.getMessage());
        }
    }

    public void sendData(String data) {
        if (isConnected && outputStream != null) {
            try {
                outputStream.write((data + "\n").getBytes());
                Log.d(TAG, "Data sent: " + data);
            } catch (IOException e) {
                Log.e(TAG, "Send error: " + e.getMessage());
                if (listener != null) {
                    new Handler(Looper.getMainLooper()).post(
                        () -> listener.onError("Send failed: " + e.getMessage())
                    );
                }
            }
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (isConnected) {
                try {
                    bytes = inputStream.read(buffer);
                    String receivedData = new String(buffer, 0, bytes).trim();

                    if (listener != null && !receivedData.isEmpty()) {
                        new Handler(Looper.getMainLooper()).post(
                            () -> listener.onDataReceived(receivedData)
                        );
                    }
                } catch (IOException e) {
                    if (isConnected) {
                        Log.e(TAG, "Read error: " + e.getMessage());
                        if (listener != null) {
                            new Handler(Looper.getMainLooper()).post(
                                () -> listener.onError("Connection lost")
                            );
                        }
                        isConnected = false;
                    }
                    break;
                }
            }
        }
    }
}
