package com.example.deer.boochat.tab_fragment;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deer.boochat.R;

/**
 * Created by deer on 2015/8/20.
 */
public class SettingFragment extends Fragment {
    CollapsingToolbarLayout collapsingToolbarLayout;

    public static SettingFragment newInstance()
    {
        SettingFragment f=new SettingFragment();
        return f;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test, container, false);

        return view;
    }

}
