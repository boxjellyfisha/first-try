package com.example.deer.boochat;

import android.os.ParcelUuid;

/**
 * Constants for use in the Bluetooth Advertisements sample
 */
public class Constants {

    /**
     * UUID identified with this app - set as Service UUID for BLE Advertisements.
     *
     * Bluetooth requires a certain format for UUIDs associated with Services.
     * The official specification can be found here:
     *
     */
    public static final ParcelUuid Service_UUID = ParcelUuid
            .fromString("00001800-0000-1000-8000-00805f9b34fb");


    public static final int REQUEST_ENABLE_BT = 1;


    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    //再傳遞參數時 透過此變數 來對應包裝後的資料要給誰用(往何處進行下一步)
    public static final String FRAGMENT_CHANGE="FRAGMENT_CHANGE";
    public static final int SCANNER_FRAGMENT_CHANGE =  1;
    public static final int ADVERTISER_FRAGMENT_CHANGE =  2;
    public static final int CHAT_FRAGMENT_CHANGE = 3;

    //之後要控制掃描的固定值
    public static final int STOP_SCANNING =  1;
    public static final int START_SCANNING = 2;

    //存放 廣播包內容 (Service Data)
    public static final String ADVERTISER_DATA =  "ADVERTISER_DATA";
    //控制可以傳送 (在傳遞給對方成功後，才再次開放)
    public static final String SEND_BTN_ENABLE =  "SEND_BTN_ENABLE";
    //操作控制碼
    public static final String OP_CODE ="opcode";
    //0x01 0x02包使用到
    public static final String LOCAL_NUM ="set_local_number";
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name_list";

    public static final String FORWARD = "forward";
    public static final String FORWARD_SENDER = "forward_sender";
    public static final String FORWARD_RECEIVER = "forward_receiver";

    public static final String COUNT = "data_total_size";

    public static final String POPUP_WINDOW_IS_SHOWING = "popupWindow_isShowing";
    //裝置序號
    public static final String BuildSERIAL = android.os.Build.SERIAL;
    public static final byte[] sendSERIAL = BuildSERIAL.getBytes();

}
