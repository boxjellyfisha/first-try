package com.example.deer.boochat.tab_fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deer.boochat.R;

/**
 * Created by deer on 2015/8/20.
 */
public class FriendFragment extends Fragment {

    public static FriendFragment newInstance() {

        FriendFragment fragment = new FriendFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_friend, container, false);

        return view;
    }
}
