package com.choosemuse.example.libmuse.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import android.os.Handler;

/**
 * Created by Macbook on 8/14/16.
 */
public class BluetoothConnection {

    private final static int REQUEST_ENABLE_BT = 1;

    // BLEAdvertiser:
    // http://code.tutsplus.com/tutorials/how-to-advertise-android-as-a-bluetooth-le-peripheral--cms-25426

    // GAP - Advertisement data
    byte[] advertData = {
            // Flags; this sets the device to use limited discoverable
            // mode (advertises for 30 seconds at a time) instead of general
            // discoverable mode (advertises indefinitely)

            0x02, // length of this data
            0x01, // GAP_ADTYPE_FLAGS,
            0x05, // DEFAULT_DISCOVERABLE_MODE | BREDR_NOT_SUPPORTED,

            0x0B, // Length of the following data
            //0xFF, // GAP_ADTYPE_MANUFACTURER_SPECIFIC,
            -1, // GAP_ADTYPE_MANUFACTURER_SPECIFIC,
            0x11, // VEX Company ID byte1
            0x11, // VEX Company ID byte0
            0x01, // MAJOR_VERSION of the radio firmware,
            0x01, // MINOR_VERSION of the radio firmware,
            0x06, // The following 32 bits are out SSN (Little Endian)
            0x12, // Example SSN = 987654
            0x0F,
            0x00,
            0x01, // MAJOR_VERSION of the peripheral’s software
            0x05 // MINOR_VERSION of the peripheral’s software
    };

    // JS_Data
    //static final String UUID_STRING = "08590F7E-DB05-467E-8757-72F6FAEB13B5";
    // Data_Brain_RX
    //static final String UUID_STRING = "08590F7E-DB05-467E-8757-72F6FAEB13F5";
    // Data_Brain_TX
    static final String UUID_STRING = "08590F7E-DB05-467E-8757-72F6FAEB1306";

    private BluetoothLeScanner mBluetoothLeScanner;
    private Handler mHandler = new Handler();

    public BluetoothConnection(Activity context, int vexId)
    {
        final byte[] result = toByteArray(vexId);

        advertData[9] = result[0];
        advertData[10] = result[1];
        advertData[11] = result[2];
        advertData[12] = result[3];

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        BluetoothLeAdvertiser advertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(false)
                .build();

        ParcelUuid pUuid = new ParcelUuid(UUID.fromString(UUID_STRING));

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                //.addServiceUuid(pUuid)
                .addServiceData(pUuid, advertData)
                .build();

        AdvertiseCallback advertisingCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                System.out.println("onStartSuccess");
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                System.out.println("onStartError: " + errorCode);
                if (errorCode == ADVERTISE_FAILED_ALREADY_STARTED) {
                    System.out.println("ADVERTISE_FAILED_ALREADY_STARTED");
                } else if (errorCode == ADVERTISE_FAILED_DATA_TOO_LARGE) {
                    System.out.println("ADVERTISE_FAILED_DATA_TOO_LARGE");
                } else if (errorCode == ADVERTISE_FAILED_FEATURE_UNSUPPORTED) {
                    System.out.println("ADVERTISE_FAILED_FEATURE_UNSUPPORTED");
                } else if (errorCode == ADVERTISE_FAILED_INTERNAL_ERROR) {
                    System.out.println("ADVERTISE_FAILED_INTERNAL_ERROR");
                } else if (errorCode == ADVERTISE_FAILED_TOO_MANY_ADVERTISERS) {
                    System.out.println("ADVERTISE_FAILED_TOO_MANY_ADVERTISERS");
                }

                super.onStartFailure(errorCode);
            }
        };

        ScanCallback mScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                if( result == null
                        || result.getDevice() == null
                        || TextUtils.isEmpty(result.getDevice().getName()) )
                    return;

                StringBuilder builder = new StringBuilder( result.getDevice().getName() );

                builder.append("\n").append(new String(result.getScanRecord().getServiceData(
                        result.getScanRecord().getServiceUuids().get(0)), Charset.forName("UTF-8")));

                System.out.println("onScanResult: " + builder.toString());
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                System.out.println("onBatchScanResults: " + results);
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                System.out.println("Discovery onScanFailed: " + errorCode);
                super.onScanFailed(errorCode);
            }
        };

        mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();

        List<ScanFilter> filters = new ArrayList<ScanFilter>();

        ScanFilter filter = new ScanFilter.Builder()
                .setServiceUuid( new ParcelUuid(UUID.fromString( UUID_STRING)))
                .build();
        filters.add( filter );

        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode( ScanSettings.SCAN_MODE_LOW_LATENCY )
                .build();

        advertiser.startAdvertising(settings, data, advertisingCallback);

        mBluetoothLeScanner.startScan(filters, scanSettings, mScanCallback);
    }

    byte[] toByteArray(int value) {
        byte[] bytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(value).array();

        for (byte b : bytes) {
            System.out.println("BYTE: " + b);
        }

        return bytes;
    }
}
