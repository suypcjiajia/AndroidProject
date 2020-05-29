package android.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;


/**
 * Created by Administrator on 2018-04-01.
 */

public class SpBLE {
    public static final int STATE_DISCONNECTED = 0;
    private final static String ACTION_GATT_DISCONNECTING =
            "com.usr.bluetooth.le.ACTION_GATT_DISCONNECTING";
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    private static Handler mHandler = null;
    // UUIDs for UAT service and associated characteristics.
//    public static UUID UART_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
//    public static UUID TX_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
//    public static UUID RX_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E");
    public static UUID UART_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static UUID TX_UUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb");
    public static UUID RX_UUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    // UUID for the BTLE client characteristic which is necessary for notifications.
    public static UUID CLIENT_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    // BTLE state
    private BluetoothAdapter adapter;
    private BluetoothGatt gatt;
    private BluetoothGattCharacteristic tx;
    private BluetoothGattCharacteristic rx;
    private static Context mContext;
    private static int mConnectionState = STATE_DISCONNECTED;
    public static BluetoothAdapter mBluetoothAdapter;
    private static String mBluetoothDeviceAddress;
    private static String mBluetoothDeviceName;


    public SpBLE(Context c, Handler h) {
        mContext = c;
        mHandler = h;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public SpBLE(Context c) {
        mContext = c;
    }

    public void setUUID(UUID uart, UUID txd, UUID rxd) {
        UART_UUID = uart;
        TX_UUID = txd;
        RX_UUID = rxd;
    }


    public void ConnectDevice(BluetoothDevice bluetoothDevice) {
        writeLine(bluetoothDevice.getName());
        gatt = bluetoothDevice.connectGatt(mContext, false, callback);
    }

    public void connect(final String address, final String devicename, final Context context) {
        mContext = context;
        if (mBluetoothAdapter == null || address == null) {
            return;
        }
        final BluetoothDevice device = mBluetoothAdapter
                .getRemoteDevice(address);
        if (device == null) {
            return;
        }
        mBluetoothDeviceAddress = address;
        mBluetoothDeviceName = devicename;
        gatt = device.connectGatt(mContext, false, callback);
    }

    public void connect(final BluetoothDevice device, final Context context) {
        mContext = context;
        if (device == null) {
            return;
        }
        mConnectionState = STATE_CONNECTING;
        // mHandler.obtainMessage(MainActivity.MESSAGE_STATE_CHANGE, mConnectionState, -1).sendToTarget();
        mBluetoothDeviceAddress = device.getAddress();
        mBluetoothDeviceName = device.getName();
        retryCount = 0;
        gatt = device.connectGatt(mContext, false, callback);

    }

    public static String getmBluetoothDeviceAddress() {
        return mBluetoothDeviceAddress;
    }

    public static String getmBluetoothDeviceName() {
        return mBluetoothDeviceName;
    }

    public int getConnectionState() {
        return mConnectionState;
    }

    public int getRSSI() {
        if (gatt == null) return 0;
        gatt.readRemoteRssi();
        return 0;
    }

    int retryCount = 0;
    // Main BTLE device callback where much of the logic occurs.
    private BluetoothGattCallback callback = new BluetoothGattCallback() {
        // Called whenever the device connection state changes, i.e. from disconnected to connected.
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                writeLine("Connected!");
                mConnectionState = STATE_CONNECTED;
                //mHandler.obtainMessage(MainActivity.MESSAGE_STATE_CHANGE, mConnectionState, -1).sendToTarget();
                // Discover services.
                if (!gatt.discoverServices()) {
                    writeLine("Failed to start discovering services!");
                }
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                writeLine("Disconnected!");
                mConnectionState = STATE_DISCONNECTED;
                //mHandler.obtainMessage(MainActivity.MESSAGE_STATE_CHANGE, mConnectionState, -1).sendToTarget();
            } else if (newState == BluetoothGatt.STATE_CONNECTING) {
                writeLine("Connecting!");
            } else {
                writeLine("Connection state changed.  New state: " + newState);
            }
        }

        // Called when services have been discovered on the remote device.
        // It seems to be necessary to wait for this discovery to occur before
        // manipulating any services or characteristics.
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                writeLine("Service discovery completed!");
              //  bianliUUID(gatt);

            } else {
                writeLine("Service discovery failed with status: " + status);
            }

            if (UART_UUID == null) {
                writeLine("uart UUID error");
                return;
            }
            writeLine(UART_UUID.toString());

            if (gatt.getService(UART_UUID) == null) {
                writeLine("service error");
                // Discover services.

//                mHandler.obtainMessage(101, 0, -1).sendToTarget();
//                if (retryCount++ < 10)
//                    if (!gatt.discoverServices()) {
//                        writeLine("Failed to start discovering services!");
//                    }
                return;
            }
            // Save reference to each characteristic.
            tx = gatt.getService(UART_UUID).getCharacteristic(TX_UUID);
            rx = gatt.getService(UART_UUID).getCharacteristic(RX_UUID);
            if (tx != null) {
                int charaProp = tx.getProperties();
                // 可读
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {

                }
                // 可写，注：要 & 其可写的两个属性
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0
                        || (charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {

                }
                // 可通知，可指示
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0
                        || (charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {

                }
            }
            // Setup notifications on RX characteristic changes (i.e. data received).
            // First call setCharacteristicNotification to enable notification.
            if (!gatt.setCharacteristicNotification(rx, true)) {
                writeLine("Couldn't set notifications for RX characteristic!");

            }
            // Next update the RX characteristic's client descriptor to enable notifications.
            if (rx.getDescriptor(CLIENT_UUID) != null) {
                BluetoothGattDescriptor desc = rx.getDescriptor(CLIENT_UUID);
                desc.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                if (!gatt.writeDescriptor(desc)) {
                    writeLine("Couldn't write RX client descriptor value!");
                    //mHandler.obtainMessage(101, 1, -1).sendToTarget();
                }
            } else {
                writeLine("Couldn't get RX client descriptor!");
                //mHandler.obtainMessage(101, 1, -1).sendToTarget();
            }
        }

        // Called when a remote characteristic changes (like the RX characteristic).
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            String hexString  = HexCode.bytesToHexStringSpace(characteristic.getValue());
            Log.w(TAG, "onCharacteristicChanged:"  + hexString);
            mHandler.obtainMessage(MainActivity.MESSAGE_READ, hexString.getBytes().length,
                    -1, hexString.getBytes()).sendToTarget();
            ProtocolAnalysis.print(characteristic.getValue());
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
            }
        }


        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.w(TAG, "onCharacteristicRead:"  + HexCode.bytesToHexString(characteristic.getValue()));
            }
        }

        @Override
        public void onCharacteristicWrite (BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
            if( status == BluetoothGatt.GATT_SUCCESS) {

                String hexStirng = HexCode.bytesToHexString(characteristic.getValue());
                Log.w(TAG, "onCharacteristicWrite:" +  hexStirng);
                mHandler.obtainMessage(MainActivity.MESSAGE_WRITE, hexStirng.getBytes()).sendToTarget();
            }
        }
    };




    private void writeLine(String str) {
        Log.d("BLE", str);
    }

    public void Write(byte[] data) {
        // Update TX characteristic value.  Note the setValue overload that takes a byte array must be used.
        if (tx == null) {
            writeLine("tx is null");
            return;
        }
        tx.setValue(data);
        if (gatt.writeCharacteristic(tx)) {
            writeLine("have writed TX characteristic!");
        } else {
            writeLine("Couldn't write TX characteristic!");
        }
    }

    public void disconnect() {
        if (gatt != null) {
            // For better reliability be careful to disconnect and close the connection.
            gatt.disconnect();
            gatt.close();
            gatt = null;
            tx = null;
            rx = null;
            mConnectionState = STATE_DISCONNECTED;
            //mHandler.obtainMessage(MainActivity.MESSAGE_STATE_CHANGE, mConnectionState, -1).sendToTarget();
        }
    }

    private List<UUID> parseUUIDs(final byte[] advertisedData) {
        List<UUID> uuids = new ArrayList<UUID>();

        int offset = 0;
        while (offset < (advertisedData.length - 2)) {
            int len = advertisedData[offset++];
            if (len == 0)
                break;

            int type = advertisedData[offset++];
            switch (type) {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (len > 1) {
                        int uuid16 = advertisedData[offset++];
                        uuid16 += (advertisedData[offset++] << 8);
                        len -= 2;
                        uuids.add(UUID.fromString(String.format("%08x-0000-1000-8000-00805f9b34fb", uuid16)));
                    }
                    break;
                case 0x06:// Partial list of 128-bit UUIDs
                case 0x07:// Complete list of 128-bit UUIDs
                    // Loop through the advertised 128-bit UUID's.
                    while (len >= 16) {
                        try {
                            // Wrap the advertised bits and order them.
                            ByteBuffer buffer = ByteBuffer.wrap(advertisedData, offset++, 16).order(ByteOrder.LITTLE_ENDIAN);
                            long mostSignificantBit = buffer.getLong();
                            long leastSignificantBit = buffer.getLong();
                            uuids.add(new UUID(leastSignificantBit,
                                    mostSignificantBit));
                        } catch (IndexOutOfBoundsException e) {
                            // Defensive programming.
                            //Log.e(LOG_TAG, e.toString());
                            continue;
                        } finally {
                            // Move the offset to read the next uuid.
                            offset += 15;
                            len -= 16;
                        }
                    }
                    break;
                default:
                    offset += (len - 1);
                    break;
            }
        }
        return uuids;
    }



    private  void bianliUUID(BluetoothGatt gatt){

        List<BluetoothGattService> bluetoothGattServices = gatt.getServices();

        Log.d(TAG,"bluetoothGattServices counts:" + bluetoothGattServices.size());
        //发现服务是可以在这里查找支持的所有服务
//                        BluetoothGattService bluetoothGattService = gatt.getService(UUID.randomUUID());
        for (BluetoothGattService bluetoothGattService : bluetoothGattServices) {
            UUID uuid = bluetoothGattService.getUuid();
            Log.d(TAG, "onServicesDiscovered--uuid=" + uuid);
            List<BluetoothGattCharacteristic> bluetoothGattCharacteristics = bluetoothGattService.getCharacteristics();
            /*获取指定服务uuid的特征值*/
           // BluetoothGattCharacteristic mBluetoothGattCharacteristic = bluetoothGattService.getCharacteristic(uuid);
//                            gatt.readCharacteristic(mBluetoothGattCharacteristic);
            for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattCharacteristics) {
                Log.d(TAG, "onServicesDiscovered--特征值 uuid=" + bluetoothGattCharacteristic.getUuid());
//                                gatt.readCharacteristic(bluetoothGattCharacteristic);
//                                bluetoothGattCharacteristic.getValue();

            //    final int charaProp = bluetoothGattCharacteristic.getProperties();

//                                bluetoothGattCharacteristic.getWriteType()==BluetoothGattCharacteristic.PROPERTY_READ
                /*如果该字符串可读*/
//                if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
//                    Log.d(TAG, "onServicesDiscovered--字符串可读--");
//                    byte[] value = new byte[20];
//                    bluetoothGattCharacteristic.setValue(value[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
//                    String writeBytes = "HYL";
//                    bluetoothGattCharacteristic.setValue(writeBytes.getBytes());
//                }
//                if (gatt.setCharacteristicNotification(bluetoothGattCharacteristic, true)) {
//                    Log.d(TAG, "onServicesDiscovered--设置通知成功=--" + uuid);
//                }
			/*3.再从指定的Characteristic中，我们可以通过getDescriptor()方法来获取该特征所包含的descriptor
				以上的BluetoothGattService、BluetoothGattCharacteristic、BluetoothGattDescriptor。
				我们都可以通过其getUuid()方法，来获取其对应的Uuid，从而判断是否是自己需要的。*/
//                List<BluetoothGattDescriptor> bluetoothGattDescriptors = bluetoothGattCharacteristic.getDescriptors();
//                for (BluetoothGattDescriptor bluetoothGattDescriptor : bluetoothGattDescriptors) {
//                    Log.d(TAG, "onServicesDiscovered--Descriptor uuid=" + bluetoothGattDescriptor.getUuid());
////                                    bluetoothGattDescriptor.getValue();
//                }
            }
        }
    }

}
