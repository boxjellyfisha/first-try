package com.example.deer.boochat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.deer.boochat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by deer on 2015/8/3.用來存放裝置與裝置序號配對
 */
public class DeviceTableAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private Map<String,String> mTable;
    private ArrayList<String> mList;

    DeviceTableAdapter(Context context, LayoutInflater inflater)
    {
        mContext=context;
        mInflater=inflater;
        mTable=new HashMap<String,String>();
        mList=new ArrayList<String>();
    }

    public void add(String serial_number,String device_name)
    {
        if(mTable.containsKey(serial_number));
        else
        {
            mTable.put(serial_number, device_name);
            mList.add(serial_number);
        }
    }
    public String findBySerial(String serial_number)
    {
        return mTable.get(serial_number);
    }
    public Boolean checkBySerial(String serial_number){return mTable.containsKey(serial_number);}

    public String findByPosition(int position){
        String tmp="";
        tmp=mList.get(position);
        tmp=findBySerial(tmp);
        return tmp;
    }

    public String getSerial(int position){ return mList.get(position);}
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Reuse an old view if we can, otherwise create a new one.
        if (view == null) {
            view = mInflater.inflate(R.layout.device_name_list, null);
        }

        TextView deviceNameView = (TextView) view.findViewById(R.id.device_name_txt);
        deviceNameView.setText(findBySerial(mList.get(position)));
        return view;
    }

}
