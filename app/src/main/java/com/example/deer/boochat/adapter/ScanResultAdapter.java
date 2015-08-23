package com.example.deer.boochat.adapter;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.deer.boochat.Constants;
import com.example.deer.boochat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class ScanResultAdapter extends BaseAdapter {

    private ArrayList<ScanResult> mArrayList;

    private Context mContext;

    private LayoutInflater mInflater;

    private ArrayList<BluetoothDevice> mLeDevices;

    private ArrayList<byte[]> mDeviceData;

    private Map<String,String> adDataPair;

    public int size=0;

    public Boolean flag;

    byte[] packageData;

    byte count;

    String stringData;




    ScanResultAdapter(Context context, LayoutInflater inflater) {
        super();
        mContext = context;
        mInflater = inflater;
        mArrayList = new ArrayList<>();
        mLeDevices = new ArrayList<BluetoothDevice>();
        mDeviceData = new ArrayList<byte[]>();
        adDataPair=new HashMap<String,String>();

    }

    public void addDevice(BluetoothDevice device) {
        if(!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }
    public void readData(ScanResult scanResult)
    {
        ScanRecord record= scanResult.getScanRecord();

        //讀ServiceData 訊息內容 (此封包的主要內容)
        byte[] data =record.getServiceData(Constants.Service_UUID);
        //讀目前的封包序號
        byte[] d=record.getManufacturerSpecificData(1);

            if (count == d[1]) ;//重複就不加入
            else
                mDeviceData.add(data);

            count = d[1];//表示接收到第幾個封包  實際值為倒數封包數值

        //讀Record的內容並轉為16進位
        packageData=record.getBytes();
        final StringBuilder stringBuilder2 = new StringBuilder(packageData.length);
        for(byte byteChar : packageData)
            stringBuilder2.append(String.format("%02X ", byteChar));
        stringData=stringBuilder2.toString();

         //String add=scanResult.getDevice().getAddress();
         //       if(adDataPair.get(add)==null)
         //   {
         //       adDataPair.put(Integer.toString(i), add);
         //       i++;
         //   }
        //Integer.parseInt(adDataPair.get(add))
        //ArrayList<String> adTime=null;
        //adTime.add(record.getServiceData(Constants.Service_UUID).toString());
        //adData.put(add, adTime);
        // scanResult.getTimestampNanos();
    }

    public String getPackage() {return stringData;}//測試用

    public ArrayList<byte[]> getadData()
    {
        return mDeviceData;
    }

    public int getDataCount() {return mDeviceData.size();}

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mArrayList.get(position).getDevice().getAddress().hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Reuse an old view if we can, otherwise create a new one.
        if (view == null) {
            view = mInflater.inflate(R.layout.tab_chat, null);
        }

        TextView deviceNameView = (TextView) view.findViewById(R.id.device_name);
        TextView deviceAddressView = (TextView) view.findViewById(R.id.device_address);
       // TextView lastSeenView = (TextView) view.findViewById(R.id.last_seen);

        ScanResult scanResult = mArrayList.get(position);

        deviceNameView.setText(scanResult.getDevice().getName());
        deviceAddressView.setText(scanResult.getDevice().getAddress());
       // lastSeenView.setText(getTimeSinceString(mContext, scanResult.getTimestampNanos()));

        return view;
    }

    public Boolean getFlag(){return flag;}
    /**
     * Search the adapter for an existing device address and return it, otherwise return -1.
     */
    private int getPosition(String address) {
        int position = -1;
        for (int i = 0; i < mArrayList.size(); i++) {
            if (mArrayList.get(i).getDevice().getAddress().equals(address)) {
                position = i;
                break;
            }
        }
        return position;
    }


    /**
     * Add a ScanResult item to the adapter if a result from that device isn't already present.
     * Otherwise updates the existing position with the new ScanResult.
     */
    public void add(ScanResult scanResult) {


        int existingPosition = getPosition(scanResult.getDevice().getAddress());

        if (existingPosition >= 0) {
            // Device is already in list, update its record.
            mArrayList.set(existingPosition, scanResult);
            flag=false;
        } else {
            // Add new Device's ScanResult to list.
            mArrayList.add(scanResult);
            flag=true;
        }
    }

    /**
     * Clear out the adapter.
     */
    public void clear() {
        mArrayList.clear();
        adDataPair.clear();
        mLeDevices.clear();
    }

    public void tmpClear(){mDeviceData.clear();}
    /**
     * Takes in a number of nanoseconds and returns a human-readable string giving a vague
     * description of how long ago that was.
     */
    public static String getTimeSinceString(Context context, long timeNanoseconds) {
        String lastSeenText = context.getResources().getString(R.string.last_seen) + " ";

        long timeSince = SystemClock.elapsedRealtimeNanos() - timeNanoseconds;
        long secondsSince = TimeUnit.SECONDS.convert(timeSince, TimeUnit.NANOSECONDS);

        if (secondsSince < 5) {
            lastSeenText += context.getResources().getString(R.string.just_now);
        } else if (secondsSince < 60) {
            lastSeenText += secondsSince + " " + context.getResources()
                    .getString(R.string.seconds_ago);
        } else {
            long minutesSince = TimeUnit.MINUTES.convert(secondsSince, TimeUnit.SECONDS);
            if (minutesSince < 60) {
                if (minutesSince == 1) {
                    lastSeenText += minutesSince + " " + context.getResources()
                            .getString(R.string.minute_ago);
                } else {
                    lastSeenText += minutesSince + " " + context.getResources()
                            .getString(R.string.minutes_ago);
                }
            } else {
                long hoursSince = TimeUnit.HOURS.convert(minutesSince, TimeUnit.MINUTES);
                if (hoursSince == 1) {
                    lastSeenText += hoursSince + " " + context.getResources()
                            .getString(R.string.hour_ago);
                } else {
                    lastSeenText += hoursSince + " " + context.getResources()
                            .getString(R.string.hours_ago);
                }
            }
        }

        return lastSeenText;
    }

}
