package com.luisa.alex.obd2_peek;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    String deviceAddress = new String();
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enableBTButton(View view){
        //Init the toast Message
        CharSequence BTmsg = "Test Clicked!";


        //---------------BLUETOOTH-------------
        //get the Bluetooth adapter
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //check if Bluetooth is supported
        if(mBluetoothAdapter == null){
            BTmsg = "Bluetooth NOT Supported!";
        }else{
            BTmsg = "Bluetooth is Supported!";
        }

        //Enable Bluetooth if not enabled
        if(!mBluetoothAdapter.isEnabled()){
            BTmsg = "requesting to enable Bluetooth";
            //If the bluetooth is not enabled on the device
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BT = 1;
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        /*
        //Query all paired Devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        //If there are any paired devices found
        if(pairedDevices.size() > 0){
            //ArrayAdapter mArrayAdapter = TODO
            //Loop through all the devices
            for (BluetoothDevice device : pairedDevices){
                //add to an Array adapter
                //TODO
            }
        }
        */

        //Create and Display the Toast
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, BTmsg, duration);
        toast.show();
    }

    //-------------DEBUG----------------
    public void buttonTest(View view){
        String testMsg = "Test Clicked";

        //---------------TUTORIAL-------------
        ArrayList<String> deviceStrs = new ArrayList();
        final ArrayList<BluetoothDevice> devices = new ArrayList();

        //Query all paired devices
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice device : pairedDevices){
                deviceStrs.add(device.getName() + "\n" + device.getAddress());
                devices.add(device);
            }
        }

        //Display the List
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        ArrayAdapter adapter = new ArrayAdapter(
                this,
                android.R.layout.select_dialog_singlechoice,
                deviceStrs.toArray(new String[deviceStrs.size()]));

        //The user selects the device
        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                deviceAddress = devices.get(position).getAddress();
                connectBTDevice(deviceAddress);
            }
        });

        alertDialog.setTitle("Choose Bluetooth Device");
        alertDialog.show();

        //Create and Display the Toast
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, testMsg, duration);
        //toast.show();
    }

    //Given a BT device address creates a socket and connects to it
    public void connectBTDevice(String deviceAddress){
        Log.v(TAG, "connectBT() called");
        //Create and Display the Toast
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "connecting to: " + deviceAddress, duration);
        toast.show();


        //connect to the actual device
        BluetoothSocket btSocket;
        BluetoothAdapter btAdapater = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = btAdapater.getRemoteDevice(deviceAddress);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try{
            btSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
            btSocket.connect();
        }catch(Exception e){
            Toast exceptionToast = Toast.makeText(context, "Exception: " + e, duration);
            Log.v(TAG, e.toString());
            exceptionToast.show();
        }

        //init the OBD Device
        
    }
}