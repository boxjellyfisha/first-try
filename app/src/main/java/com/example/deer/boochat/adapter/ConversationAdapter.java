package com.example.deer.boochat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.deer.boochat.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by deer on 2015/8/22.
 */
public class ConversationAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private ArrayList<Map<String,Object>> mList;

    private String yname="Red Hong";

    boolean previous;

    public ConversationAdapter(LayoutInflater inflater)
    {
        mInflater=inflater;
        mList=new ArrayList<Map<String,Object>>();
    }

    public void add(String s,Boolean b)
    {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("message",s);
        map.put("flag",b);
        mList.add(map);
    }
    public void setDeviceName(String n){yname=n;}

    private void setName(View v)
    {
      TextView name=(TextView)v.findViewById(R.id.your_name);
        name.setText(yname);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
    @Override
    public long getItemId(int position){return mList.get(position).hashCode();}
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Reuse an old view if we can, otherwise create a new one.

        //ViewHolder holder = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        Date mDate = new Date();
        String dts=sdf.format(mDate);
        String dts2=sdf2.format(mDate);
        Boolean flag=(Boolean)mList.get(position).get("flag");
        if(position>0)
        previous =(Boolean)mList.get(position-1).get("flag");

        if(view == null || flag!=previous)
        {
            if (flag){
                view = mInflater.inflate(R.layout.you_say, null);
                setName(view);
            }
            else {
                view = mInflater.inflate(R.layout.i_say, null);
            }
        }

        TextView sentence=(TextView) view.findViewById(R.id.c_s);
        TextView time=(TextView) view.findViewById(R.id.c_time);
        sentence.setText((String)mList.get(position).get("message"));
        time.setText(dts + "\n" + dts2);

        return view;
    }
    static class ViewHolder {
        TextView sentence;
        TextView time;
        TextView name;
        Boolean flag;

   }

}
