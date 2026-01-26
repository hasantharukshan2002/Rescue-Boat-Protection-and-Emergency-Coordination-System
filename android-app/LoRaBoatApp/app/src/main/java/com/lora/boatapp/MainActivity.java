package com.lora.boatapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BluetoothManager.BluetoothConnectionListener {
    private static final int REQUEST_DEVICE_SELECT = 1;

    private BluetoothManager bluetoothManager;
    private Button btnConnect;
    private TextView tvConnectionStatus;
    private TextView tvBoatID;
    private TextView tvFlipStatus;
    private TextView tvSignalStrength;
    private TextView tvBatteryLevel;
    private TextView tvTemperature;
    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvLastUpdate;
    private ProgressBar pbSignalStrength;
    private ProgressBar pbBatteryLevel;
    private FrameLayout flipAlertContainer;

    private BoatData currentBoatData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        bluetoothManager = new BluetoothManager(this);
        bluetoothManager.setConnectionListener(this);
        currentBoatData = new BoatData();

        btnConnect.setOnClickListener(v -> {
            if (bluetoothManager.isConnected()) {
                disconnectDevice();
            } else {
                selectDevice();
            }
        });
    }

    private void initializeViews() {
        btnConnect = findViewById(R.id.btnConnect);
        tvConnectionStatus = findViewById(R.id.tvConnectionStatus);
        tvBoatID = findViewById(R.id.tvBoatID);
        tvFlipStatus = findViewById(R.id.tvFlipStatus);
        tvSignalStrength = findViewById(R.id.tvSignalStrength);
        tvBatteryLevel = findViewById(R.id.tvBatteryLevel);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvLatitude = findViewById(R.id.tvLatitude);
        tvLongitude = findViewById(R.id.tvLongitude);
        tvLastUpdate = findViewById(R.id.tvLastUpdate);
        pbSignalStrength = findViewById(R.id.pbSignalStrength);
        pbBatteryLevel = findViewById(R.id.pbBatteryLevel);
        flipAlertContainer = findViewById(R.id.flipAlertContainer);
    }

    private void selectDevice() {
        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(intent, REQUEST_DEVICE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_DEVICE_SELECT && resultCode == Activity.RESULT_OK) {
            String deviceAddress = data.getStringExtra("device_address");
            String deviceName = data.getStringExtra("device_name");

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                        == PackageManager.PERMISSION_GRANTED) {
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                    bluetoothManager.connect(device);
                }
            } else {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                bluetoothManager.connect(device);
            }
        }
    }

    private void disconnectDevice() {
        bluetoothManager.disconnect();
    }

    @Override
    public void onConnected() {
        runOnUiThread(() -> {
            tvConnectionStatus.setText(R.string.status_connected);
            tvConnectionStatus.setTextColor(getResources().getColor(R.color.green, null));
            btnConnect.setText(R.string.btn_disconnect);
            Toast.makeText(this, "Connected to device", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDisconnected() {
        runOnUiThread(() -> {
            tvConnectionStatus.setText(R.string.status_disconnected);
            tvConnectionStatus.setTextColor(getResources().getColor(R.color.orange, null));
            btnConnect.setText(R.string.btn_connect);
            flipAlertContainer.setVisibility(FrameLayout.GONE);
            Toast.makeText(this, "Disconnected from device", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDataReceived(String data) {
        runOnUiThread(() -> {
            try {
                currentBoatData = BoatData.parseData(data);
                updateUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onError(String error) {
        runOnUiThread(() -> {
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            tvConnectionStatus.setText(R.string.status_disconnected);
            tvConnectionStatus.setTextColor(getResources().getColor(R.color.orange, null));
            btnConnect.setText(R.string.btn_connect);
        });
    }

    private void updateUI() {
        // Update boat ID
        if (currentBoatData.boatId >= 0) {
            tvBoatID.setText(String.valueOf(currentBoatData.boatId));
        }

        // Update flip status with alert
        if (currentBoatData.isFlipped) {
            tvFlipStatus.setText(R.string.flipped);
            tvFlipStatus.setTextColor(getResources().getColor(R.color.red, null));
            flipAlertContainer.setVisibility(FrameLayout.VISIBLE);
        } else {
            tvFlipStatus.setText(R.string.normal);
            tvFlipStatus.setTextColor(getResources().getColor(R.color.green, null));
            flipAlertContainer.setVisibility(FrameLayout.GONE);
        }

        // Update signal strength
        tvSignalStrength.setText(currentBoatData.signalStrength + "%");
        pbSignalStrength.setProgress(currentBoatData.signalStrength);

        // Update battery level
        tvBatteryLevel.setText(currentBoatData.batteryLevel + "%");
        pbBatteryLevel.setProgress(currentBoatData.batteryLevel);

        // Update temperature
        tvTemperature.setText(String.format(Locale.US, "%.1f°C", currentBoatData.temperature));

        // Update location
        tvLatitude.setText(String.format(Locale.US, "%.6f", currentBoatData.latitude));
        tvLongitude.setText(String.format(Locale.US, "%.6f", currentBoatData.longitude));

        // Update last update time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String currentTime = sdf.format(new Date(currentBoatData.timestamp));
        tvLastUpdate.setText("Last update: " + currentTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothManager.isConnected()) {
            bluetoothManager.disconnect();
        }
    }
}
