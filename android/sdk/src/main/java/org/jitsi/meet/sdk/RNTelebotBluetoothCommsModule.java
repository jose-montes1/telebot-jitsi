
package org.jitsi.meet.sdk;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import android.content.Intent;
import android.app.Activity;

import java.util.Set;
import java.util.UUID;

import java.io.IOException;
import java.io.OutputStream;

public class RNTelebotBluetoothCommsModule 
				extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  private BluetoothAdapter mBluetoothAdapter;
  private BluetoothSocket mBluetoothSocket;
  private static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  private Boolean btConnectionSucces = false;


  public RNTelebotBluetoothCommsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  }

  @Override
  public String getName() {
    return "RNTelebotBluetoothComms";
  }


  @ReactMethod
  public void getDevices(Callback displayName){
    WritableArray deviceNames = Arguments.createArray();
    if(mBluetoothAdapter != null){
      Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
      if(pairedDevices.size() > 0){
        for(BluetoothDevice device: pairedDevices){
          deviceNames.pushString(device.getName());  
        }
        
      }else{
        deviceNames.pushString("No paired devices");
      }
    }else{
      deviceNames.pushString("Default adapter is false");
    }
    displayName.invoke(deviceNames);
    return;
  }

  @ReactMethod
  public void bluetoothEnabled(){

  }

  @ReactMethod 
  public void connectToDevice(){//Callback displayText){
    String deviceName = "BluetoothV3";
    String deviceAddress = "";
    String resultsText = "Initial Text";
    //Enable the bluetooth adapter if it is off
    if(mBluetoothAdapter.isEnabled() == false){
      Intent turnBtOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
      getCurrentActivity().startActivityForResult(turnBtOn, 1);
    
    }

    //Get the address for the specified device name
    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    if(pairedDevices.size() > 0){
      for(BluetoothDevice device: pairedDevices){
        if(device.getName().equals(deviceName)){
          deviceAddress = device.getAddress(); 
          //displayText.invoke(deviceAddress);
        }  
      }
    }

    //Connect to bluetooth device
    if(mBluetoothSocket == null || !btConnectionSucces){
      try{
        BluetoothDevice dev = mBluetoothAdapter.getRemoteDevice(deviceAddress);
        mBluetoothSocket = dev.createInsecureRfcommSocketToServiceRecord(myUUID);
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        mBluetoothSocket.connect();
        btConnectionSucces = true;
        resultsText = "Connected Succesfully!";
      } catch(IOException e){
        btConnectionSucces = false;
        resultsText = "Failed to connect";
      }
    }

    //displayText.invoke(resultsText);

  }

  @ReactMethod
  public void sendData(String data){
    data = data == null ? "" : data; //Classic one line check
    if(mBluetoothSocket != null && btConnectionSucces){
      String outputText = "";
      try{
        OutputStream stream = mBluetoothSocket.getOutputStream();
        switch(data){
          case "w":
            outputText = "R|100:L|100:";
            break;
          case "a":
            outputText = "R|100:L|-100:";
            break;
          case "s":
            outputText = "R|-100:L|-100:";
            break;
          case "d":
            outputText = "R|-100:L|100:";
            break;
          default:
            break;
        }
        stream.write(outputText.getBytes());
        //stream.flush();    
      }catch(IOException e){

      }
    }
  }



  @ReactMethod
  public void sendAndConnect(String text){
    if(!this.btConnectionSucces){
      connectToDevice();
    }
    sendData(text);
  }

}