package com.choosemuse.example.libmuse.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;

/**
 * Created by Macbook on 8/18/16.
 */
public class VexBluetoothGattServerCallback extends BluetoothGattServerCallback {

    @Override
    public void onNotificationSent(BluetoothDevice device, int status) {
        super.onNotificationSent(device, status);

        System.out.println("VexBluetoothGattServerCallback: ONNOTIFICATIONSENT");
    }

    @Override
    public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
        super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);

        System.out.println("VexBluetoothGattServerCallback: ONDESCRIPTORWRITEREQUEST");
    }

    @Override
    public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
        super.onDescriptorReadRequest(device, requestId, offset, descriptor);

        System.out.println("VexBluetoothGattServerCallback: ONDESCRIPTORREADREQUEST");
    }

    @Override
    public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicReadRequest(device, requestId, offset, characteristic);

        System.out.println("VexBluetoothGattServerCallback: onCharacteristicReadRequest");
    }

    @Override
    public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
        super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);

        System.out.println("VexBluetoothGattServerCallback: onCharacteristicWriteRequest");
    }

    @Override
    public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
        super.onConnectionStateChange(device, status, newState);

        System.out.println("VexBluetoothGattServerCallback: onConnectionStateChange");
    }

    @Override
    public void onServiceAdded(int status, BluetoothGattService service) {
        super.onServiceAdded(status, service);

        System.out.println("VexBluetoothGattServerCallback: onServiceAdded");
    }

    @Override
    public void onMtuChanged(BluetoothDevice device, int mtu) {
        super.onMtuChanged(device, mtu);

        System.out.println("VexBluetoothGattServerCallback: onMtuChanged");
    }

    @Override
    public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
        super.onExecuteWrite(device, requestId, execute);

        System.out.println("VexBluetoothGattServerCallback: onExecuteWrite");
    }
}
