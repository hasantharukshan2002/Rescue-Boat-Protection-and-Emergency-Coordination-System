package com.lora.boatapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Set;

public class DeviceListActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> adapter;
    private ArrayList<BluetoothDevice> deviceList;
    private ArrayList<String> deviceNames;
    private ListView lvDevices;
    private TextView tvNoDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        lvDevices = findViewById(R.id.lvDevices);
        tvNoDevices = findViewById(R.id.tvNoDevices);
        Button btnScan = findViewById(R.id.btnScan);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceList = new ArrayList<>();
        deviceNames = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deviceNames);
        lvDevices.setAdapter(adapter);

        // Check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.BLUETOOTH_SCAN,
                                android.Manifest.permission.BLUETOOTH_CONNECT,
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_PERMISSION_CODE);
            } else {
                scanDevices();
            }
        } else {
            scanDevices();
        }

        btnScan.setOnClickListener(v -> scanDevices());

        lvDevices.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice selectedDevice = deviceList.get(position);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("device_address", selectedDevice.getAddress());
            resultIntent.putExtra("device_name", selectedDevice.getName());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void scanDevices() {
        deviceList.clear();
        deviceNames.clear();
        adapter.notifyDataSetChanged();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                        == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
            } else {
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            return;
        }

        Set<BluetoothDevice> pairedDevices = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                    == PackageManager.PERMISSION_GRANTED) {
                pairedDevices = bluetoothAdapter.getBondedDevices();
            }
        } else {
            pairedDevices = bluetoothAdapter.getBondedDevices();
        }

        if (pairedDevices != null && !pairedDevices.isEmpty()) {
            for (BluetoothDevice device : pairedDevices) {
                deviceList.add(device);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT)
                            == PackageManager.PERMISSION_GRANTED) {
                        deviceNames.add(device.getName() + " (" + device.getAddress() + ")");
                    }
                } else {
                    deviceNames.add(device.getName() + " (" + device.getAddress() + ")");
                }
            }
            adapter.notifyDataSetChanged();
            lvDevices.setVisibility(ListView.VISIBLE);
            tvNoDevices.setVisibility(TextView.GONE);
        } else {
            lvDevices.setVisibility(ListView.GONE);
            tvNoDevices.setVisibility(TextView.VISIBLE);
            Toast.makeText(this, "No paired devices found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanDevices();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
