# Capacitor-Android-BluetoothManager

**Only for Android .... in case the name of the plugin is not explicit enough**

Capacitor plugin to allow access to the underlying Bluetooth Manager, if one exists  

**This is a work in progress for an internal project**

## How to use this

* Git clone this code

* Build it using : `yarn build`

* Add the plugin to your Capacitor Android Project

```
yarn add file:/path/to/capacitor-android-bluetoothmanager
```

*  Do Android Plugin integration

```java
package com.example.android.project;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Plugin;

import java.util.ArrayList;

// ----------------- IMPORT THE PLUGIN ---------------------- 
import com.jbaysolutions.capacitor.bluetooth.capacitorandroidbluetoothmanager.BluetoothManagerPlugin; 

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initializes the Bridge
    this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
      // Additional plugins you've installed go here
      // Ex: add(TotallyAwesomePlugin.class);
      
      // -------------- ADD THE PLUGIN ------------- 
      add(BluetoothManagerPlugin.class);
    }});
  }
}
```

* Use it on your Capacitor Project:

```javascript
// Import bits
import { Plugins } from '@capacitor/core'
import 'capacitor-android-bluetoothmanager'
const { BluetoothManagerPlugin } = Plugins

// and then use the BluetoothManagerPlugin


```
