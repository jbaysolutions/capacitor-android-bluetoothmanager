# Installation

## Adding Plugin - NPM

	npm install capacitor-android-hotspot --save
    
## Adding Plugin - Yarn

    yarn add capacitor-android-hotspot

## Enabling Plugin

Open the `MainActivity.java` class of your Android Capacitor app.

Import the Plugin :

    import com.jbaysolutions.capacitor.bluetooth.capacitorandroidbluetoothmanager.BluetoothManagerPlugin; 

Add the Plugin class inside the Bridge initialization:

```java
// Initializes the Bridge
this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
  // Additional plugins you've installed go here
  // Ex: add(TotallyAwesomePlugin.class);
   
  add(BluetoothManagerPlugin.class);
}});
```

## Example


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
