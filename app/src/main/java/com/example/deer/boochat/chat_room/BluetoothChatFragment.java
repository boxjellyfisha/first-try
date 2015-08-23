/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.deer.boochat.chat_room;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deer.boochat.Constants;
import com.example.deer.boochat.R;


/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Fragment {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_ADVERTISE_DATA = "ADVERTISE_DATA";
    public static final String EXTRAS_PACKAGE_DATA = "PACKAGE_DATA";

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;
    String mDeviceName;
    String mDeviceAddress;
    String mData;
    String mPackage;
    Boolean Send_btn=false;  //表示現在的發送狀態

    /*取得掃描片段*/
   // ScannerFragment sFrag= (ScannerFragment)
    //        getActivity().getSupportFragmentManager().findFragmentById(R.id.scanner_fragment_container);
    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */

    OnChatListener mCallback;

    // Container Activity must implement this interface
    public interface OnChatListener {
        void onSendMessage(Bundle b);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnChatListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mDeviceName = bundle.getString(EXTRAS_DEVICE_NAME);
            mDeviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }
            setupChat();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        mSendButton.setEnabled(Send_btn);
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab_public, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {


        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    Bundle bundle=new Bundle();
                    bundle.putString(BluetoothChatFragment.EXTRAS_ADVERTISE_DATA, message);
                    mCallback.onSendMessage(bundle);
                    //對話框上顯示
                    mConversationArrayAdapter.add("Me:  " + message);
                    //設為不可發送 並清除訊息文字編輯區
                    Send_btn=false;
                    mSendButton.setEnabled(Send_btn);
                    mOutStringBuffer.setLength(0);
                    mOutEditText.setText(mOutStringBuffer);

                }
            }
        });

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (Send_btn && actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                Bundle bundle=new Bundle();
                bundle.putString(EXTRAS_ADVERTISE_DATA, message);
                mCallback.onSendMessage(bundle);
                //對話框上顯示
                mConversationArrayAdapter.add("Me:  " + message);
                //設定為不可發送 清除訊息文字編輯區
                Send_btn=false;
                mSendButton.setEnabled(Send_btn);
                mOutStringBuffer.setLength(0);
                mOutEditText.setText(mOutStringBuffer);

            }
            return true;
        }
    };

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the ScannerFragment
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:

                    break;

            }
        }
    };

  /*  public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break; */

    //from SCAN
    public void updateChatView(Bundle bundle)
    {
        if (bundle != null)
        {
            mDeviceName = bundle.getString(EXTRAS_DEVICE_NAME);
            mDeviceAddress = bundle.getString(EXTRAS_DEVICE_ADDRESS);
            mPackage = bundle.getString(EXTRAS_PACKAGE_DATA);

            if(bundle.getInt(Constants.OP_CODE)==2)
            mDeviceName = bundle.getString(Constants.DEVICE_NAME);
            mData = bundle.getString(Constants.ADVERTISER_DATA);

            Toast.makeText(getActivity(), "顯示: " + mData, Toast.LENGTH_LONG).show();
            mConversationArrayAdapter.add(mDeviceName + ":  " + mData);
            //Toast.makeText(getActivity(),mData, Toast.LENGTH_SHORT).show();
        }
    }

    //from AD
    public void callBackCheck(Bundle bundle)
    {
        switch(bundle.getInt(Constants.OP_CODE)) {
            case 1:
                mConversationArrayAdapter.add("加入" + bundle.getString(Constants.DEVICE_NAME) + ":"
                        + bundle.getString(Constants.ADVERTISER_DATA));
                Toast.makeText(getActivity(), "註冊成功", Toast.LENGTH_LONG).show();
                Send_btn=true;
                mSendButton.setEnabled(Send_btn);
                break;
            case 2:
                Send_btn=bundle.getBoolean(Constants.SEND_BTN_ENABLE);
                mSendButton.setEnabled(Send_btn);
                Toast.makeText(getActivity(), "訊息發送成功!", Toast.LENGTH_LONG).show();
                break;
            case 5:
                mConversationArrayAdapter.add("成功註冊完畢!\n可以開始與" + bundle.getString(Constants.DEVICE_NAME) + "聊天了!\n"
                        + bundle.getString(Constants.ADVERTISER_DATA));
                Send_btn=true;
                mSendButton.setEnabled(Send_btn);
                //Toast.makeText(getActivity(),"開始聊天吧", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
