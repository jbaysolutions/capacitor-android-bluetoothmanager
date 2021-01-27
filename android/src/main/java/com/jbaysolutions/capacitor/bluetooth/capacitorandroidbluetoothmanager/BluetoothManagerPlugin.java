package com.jbaysolutions.capacitor.bluetooth.capacitorandroidbluetoothmanager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin(
        requestCodes={
                BluetoothManagerPlugin.BLUETOOTHMANAGER_REQUEST_PERMS
        },
        permissions = {
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
        },
        permissionRequestCode = BluetoothManagerPlugin.BLUETOOTHMANAGER_REQUEST_PERMS
)
public class BluetoothManagerPlugin extends Plugin {

    protected static final String TAG = "JBAndroidBLManager";
    protected static final int BLUETOOTHMANAGER_REQUEST_PERMS = 9841;
    protected static final String PLUGIN_EVENT_NAME= "BluetoothManagerPluginEvent";

    private boolean initialized = false;
    private BluetoothAdapter myBTAdapter;

    @PluginMethod
    public void initialize(PluginCall call) {
        if (initialized) {
            Log.e(TAG, "Already Initialized with Success. Stopping new init.");
            return;
        }

        Log.d(TAG, "Initializing BluetoothManagerPlugin");
        if (!hasRequiredPermissions()) {
            Log.d(TAG, "Handling BluetoothManagerPlugin Permissions");
            saveCall(call);
            pluginRequestAllPermissions();
        } else {
            myBTAdapter = BluetoothAdapter.getDefaultAdapter();
            initializeGlocalBroadcastListener();
            this.initialized = true;
            call.success();
        }
    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "handling request perms result");
        PluginCall savedCall = getSavedCall();

        for(int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                if (savedCall != null) {
                    savedCall.reject("User denied permission");
                } else {
                    notifyListeners("PERMISSIONS_DENIED_BY_USER", new JSObject());
                }
                return;
            }
        }

        if (requestCode == BLUETOOTHMANAGER_REQUEST_PERMS) {
            myBTAdapter = BluetoothAdapter.getDefaultAdapter();
            initializeGlocalBroadcastListener();
            this.initialized = true;
            if (savedCall != null) {
                savedCall.success();
            } else {
                notifyListeners("PERMISSIONS_GIVEN_BY_USER", new JSObject());
            }
            return;
        }

        if (savedCall != null) {
            savedCall.reject("Permissions Result without correct Code");
        }
        notifyListeners("PERMISSIONS_WITHOUT_CORRECT_CODE", new JSObject());
    }

    private void initializeGlocalBroadcastListener() {
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getContext().registerReceiver(globalBroadcastReceiver, filter);
    }



    @PluginMethod
    public void hasBluetoothSupport(PluginCall call) {
        JSObject ret = new JSObject();
        if (myBTAdapter != null) {
            ret.put("hwSupport", true);
        } else {
            ret.put("hwSupport", false);
        }
        call.success(ret);
    }

    @PluginMethod
    public void isBluetoothEnabled(PluginCall call) {
        if (myBTAdapter==null) {
            call.reject("No hardware support or no permissions", "HW_FAILURE");
            return;
        }

        JSObject ret = new JSObject();
        ret.put("enabled", myBTAdapter.isEnabled());
        call.success(ret);
    }

    @PluginMethod
    public void getName(PluginCall call) {
        if (myBTAdapter==null) {
            call.reject("No hardware support or no permissions", "HW_FAILURE");
            return;
        }

        JSObject ret = new JSObject();
        ret.put("name", myBTAdapter.getName());
        call.success(ret);
    }

    @PluginMethod
    public void setDeviceName(PluginCall call) {
        if (myBTAdapter==null) {
            call.reject("No hardware support or no permissions", "HW_FAILURE");
            return;
        }

        if (!myBTAdapter.isEnabled()) {
            call.reject("Bluetooth not Enabled", "BLUETOOTH_DISBLED");
            return;
        }

        Log.d(TAG , "############ ALL DATA : " + call.getData().toString());

        String name = call.getString("deviceName");
        if (name == null) {
            call.reject("Property deviceName is null. Can't set null name.", "NULL");
            return;
        }

        boolean result = myBTAdapter.setName(name);
        if (!result) {
            call.reject("Bluetooth not Enabled", "BLUETOOTH_DISBLED");
        } else {
            call.success();
        }
    }

    @PluginMethod
    public void enableBluetooth(PluginCall call) {
        if (myBTAdapter==null) {
            call.reject("No hardware support or no permissions", "HW_FAILURE");
            return;
        }

        if ( myBTAdapter.isEnabled() ) {
            call.reject("Already Enabled", "ALREADY_ENABLED");
            return;
        }

        if (isEnablingOrDisablingProcessHappening) {
            call.reject("Already in the process of Enabling or Disabling", "ALREADY_BUSY");
            return;
        }

        isEnablingOrDisablingProcessHappening = true;
        bluetoothEnablingBroadcastListener.setPluginCall(call, true, getContext());

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getContext().registerReceiver(bluetoothEnablingBroadcastListener, filter);

        /*
            If this call returns false then there was an
            immediate problem that will prevent the adapter from being turned on -
            such as Airplane mode, or the adapter is already turned on.
        */
        boolean isEnableHappening = myBTAdapter.enable();

        if (!isEnableHappening) {
            getContext().unregisterReceiver(bluetoothEnablingBroadcastListener);
            bluetoothEnablingBroadcastListener.terminate();
            isEnablingOrDisablingProcessHappening = false;
        }
    }

    @PluginMethod
    public void disableBluetooth(PluginCall call) {
        if (myBTAdapter==null) {
            call.reject("No hardware support or no permissions", "HW_FAILURE");
            return;
        }

        if ( !myBTAdapter.isEnabled() ) {
            call.reject("Already Disabled", "ALREADY_DISABLED");
            return;
        }

        if (isEnablingOrDisablingProcessHappening) {
            Log.d(TAG,"Already some process being executed!!!! 1");
            call.reject("Already in the process of Enabling or Disabling", "ALREADY_BUSY");
            return;
        }

        isEnablingOrDisablingProcessHappening = true;
        bluetoothEnablingBroadcastListener.setPluginCall(call, false, getContext());

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getContext().registerReceiver(bluetoothEnablingBroadcastListener, filter);

        /*
            If this call returns false then there was an
            immediate problem that will prevent the adapter from being turned on -
            such as Airplane mode, or the adapter is already turned on.
        */
        boolean isDisableHappening = myBTAdapter.disable();

        if (!isDisableHappening) {
            Log.d(TAG,"Disable not happening ! 2");
            getContext().unregisterReceiver(bluetoothEnablingBroadcastListener);
            bluetoothEnablingBroadcastListener.terminate();
            isEnablingOrDisablingProcessHappening = false;
        }
    }

    private boolean isEnablingOrDisablingProcessHappening = false;
    private BluetoothEnablingBroadcastReceiver bluetoothEnablingBroadcastListener = new BluetoothEnablingBroadcastReceiver();

    private BroadcastReceiver globalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "----------------------------- GOT ACTION : " + action);
            if(action!= null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if ( state == BluetoothAdapter.STATE_TURNING_ON) {
                    JSObject ret = new JSObject();
                    ret.put("eventType", "STATE_TURNING_ON");
                    notifyListeners(PLUGIN_EVENT_NAME, ret);
                } else if ( state == BluetoothAdapter.STATE_TURNING_OFF) {
                    JSObject ret = new JSObject();
                    ret.put("eventType", "STATE_TURNING_OFF");
                    notifyListeners(PLUGIN_EVENT_NAME, ret);
                } else if ( state == BluetoothAdapter.STATE_ON) {
                    JSObject ret = new JSObject();
                    ret.put("eventType", "STATE_ON");
                    notifyListeners(PLUGIN_EVENT_NAME, ret);
                } else if ( state == BluetoothAdapter.STATE_OFF ) {
                    JSObject ret = new JSObject();
                    ret.put("eventType", "STATE_OFF");
                    notifyListeners(PLUGIN_EVENT_NAME, ret);
                }
            }
        }
    };

    public class BluetoothEnablingBroadcastReceiver extends BroadcastReceiver {

        private Context runningContext;
        private PluginCall call;
        private boolean isEnablingProcess;

        public void setPluginCall(PluginCall call, boolean isEnablingProcess, Context runningContext) {
            this.call = call;
            this.isEnablingProcess = isEnablingProcess;
            this.runningContext = runningContext;
        }

        public void terminate() {
            this.call = null;
            this.runningContext = null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action!= null && action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if ( state == BluetoothAdapter.STATE_TURNING_ON) {
                    Log.d(TAG,"--- ACTION_STATE_CHANGED --- STATE_TURNING_ON ");
                    return;
                }
                if ( state == BluetoothAdapter.STATE_TURNING_OFF) {
                    Log.d(TAG,"--- ACTION_STATE_CHANGED --- STATE_TURNING_OFF ");
                    return;
                }


                if ( state == BluetoothAdapter.STATE_ON) {
                    Log.d(TAG,"--- ACTION_STATE_CHANGED --- STATE_ON ");
                    if (isEnablingProcess) {
                        call.success();
                    } else {
                        call.reject("Could not Enable BL");
                    }
                    runningContext.unregisterReceiver(this);
                    bluetoothEnablingBroadcastListener.terminate();
                }

                if ( state == BluetoothAdapter.STATE_OFF ) {
                    Log.d(TAG,"--- ACTION_STATE_CHANGED --- STATE_OFF ");
                    if (isEnablingProcess) {
                        call.reject("Could not Enable BL");
                    } else {
                        call.success();
                    }
                    runningContext.unregisterReceiver(this);
                    bluetoothEnablingBroadcastListener.terminate();
                }
                isEnablingOrDisablingProcessHappening = false;
                this.call = null;

            }
        }
    }




}
