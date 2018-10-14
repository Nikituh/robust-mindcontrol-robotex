package com.choosemuse.example.libmuse.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseData;
import android.os.ParcelUuid;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;

/**
 * Created by Macbook on 8/18/16.
 */
public class BluetoothConnection2 {

    // GAP - Advertisement data
    byte[] advertisingBytes = {
            0x01, // MAJOR_VERSION of the radio firmware,
            0x02, // MINOR_VERSION of the radio firmware,

            0x06, // The following 32 bits are out SSN (Little Endian)
            0x12, // Example SSN = 987654
            0x0F,
            0x00,

            0x01, // MAJOR_VERSION of the peripheral’s software
            0x06  // MINOR_VERSION of the peripheral’s software

            //0x01, // Wrong version values for debugging
            //0x03
    };

    UUID CONTROLLER_UUID = UUID.fromString("08590F7E-DB05-467E-8757-72F6FAEB13A5");
    UUID JS_DATA_UUID = UUID.fromString("08590F7E-DB05-467E-8757-72F6FAEB13B5");
    UUID JS_RATE_UUID = UUID.fromString("08590F7E-DB05-467E-8757-72F6FAEB13C5");

    BluetoothManager manager;
    BluetoothGattServer server;
    VexBluetoothGattServerCallback callback;
    BluetoothLeAdvertiser advertiser;
    BluetoothAdapter adapter;

    public BluetoothConnection2(Activity context, int vexId) {

        manager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        callback = new VexBluetoothGattServerCallback();

        adapter = manager.getAdapter();
        advertiser = adapter.getBluetoothLeAdvertiser();
        server = manager.openGattServer(context, callback);

        final byte[] result = toByteArray(vexId);

        advertisingBytes[2] = result[0];
        advertisingBytes[3] = result[1];
        advertisingBytes[4] = result[2];
        advertisingBytes[5] = result[3];

        initServer();
        startAdvertising();
    }

    void initServer()
    {
        BluetoothGattService vexControllerService = new BluetoothGattService(CONTROLLER_UUID,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);

        BluetoothGattCharacteristic JS_DATA_Char = new BluetoothGattCharacteristic(JS_DATA_UUID,
                BluetoothGattCharacteristic.PROPERTY_READ |
                        BluetoothGattCharacteristic.PROPERTY_NOTIFY |
                        BluetoothGattCharacteristic.PROPERTY_INDICATE,
                BluetoothGattCharacteristic.PERMISSION_READ| BluetoothGattCharacteristic.PERMISSION_WRITE);

        BluetoothGattCharacteristic JS_RATE_Char = new BluetoothGattCharacteristic(JS_RATE_UUID,
                BluetoothGattCharacteristic.PROPERTY_READ |
                        BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE |
                        BluetoothGattCharacteristic.PROPERTY_WRITE,
                BluetoothGattCharacteristic.PERMISSION_READ| BluetoothGattCharacteristic.PERMISSION_WRITE);

        vexControllerService.addCharacteristic(JS_DATA_Char);
        vexControllerService.addCharacteristic(JS_RATE_Char);

        server.addService(vexControllerService);
    }

    /*
     * Callback handles events from the framework describing
     * if we were successful in starting the advertisement requests.
     */
    public void startAdvertising()
    {
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .build();

        AdvertiseData data = new AdvertiseData.Builder()
                //.setIncludeDeviceName(false)
                .setIncludeDeviceName(true)
                //.addServiceUuid(new ParcelUuid(JS_DATA_UUID))
                .addManufacturerData(0x1111, advertisingBytes)
                .build();

        if (!adapter.isEnabled()) {
            System.out.println("BLE DISABLED, FUCK YOU!");
            return;
        } else {
            System.out.println("BLE ENABLED!");
        }

        advertiser.startAdvertising(settings, data, mAdvertiseCallback);
    }

    private AdvertiseCallback mAdvertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            System.out.println("Peripheral Advertise Started.");
            System.out.println("GATT Server Ready");
        }

        @Override
        public void onStartFailure(int errorCode) {

            String description = "";
            if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_FEATURE_UNSUPPORTED)
                description = "ADVERTISE_FAILED_FEATURE_UNSUPPORTED";
            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_TOO_MANY_ADVERTISERS)
                description = "ADVERTISE_FAILED_TOO_MANY_ADVERTISERS";
            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_ALREADY_STARTED)
                description = "ADVERTISE_FAILED_ALREADY_STARTED";
            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_DATA_TOO_LARGE)
                description = "ADVERTISE_FAILED_DATA_TOO_LARGE";
            else if (errorCode == AdvertiseCallback.ADVERTISE_FAILED_INTERNAL_ERROR)
                description = "ADVERTISE_FAILED_INTERNAL_ERROR";
            else description = "unknown";

            System.out.println("Peripheral Advertise Failed: " + description);
            System.out.println("GATT Server Error " + description);
        }
    };

    byte[] toByteArray(int value) {
        byte[] bytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();

        for (byte b : bytes) {
            System.out.println("BYTE: " + b);
        }

        return bytes;
    }

}
