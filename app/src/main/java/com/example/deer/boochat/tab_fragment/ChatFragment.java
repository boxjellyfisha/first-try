package com.example.deer.boochat.tab_fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deer.boochat.R;

/**
 * Created by deer on 2015/8/20.
 */
public class ChatFragment extends ListFragment {
    public static ChatFragment newInstance()
    {
        ChatFragment f=new ChatFragment();
        return f;
    }

}
