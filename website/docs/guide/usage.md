# Usage

## Importing 

```javascript
// Import bits
import { Plugins } from '@capacitor/core'
import 'capacitor-android-bluetoothmanager'
const { BluetoothManagerPlugin } = Plugins
```

## Initialization

The first thing that needs to be done is initializing.

Definition: `initialize(): Promise<void>;`

Underneath the hood, what this does:
 * Requests permissions to the user is they are needed.
 * Checks to see if there is BlueTooth Support 
 * Gets a BluetoothAdapter if there is Bluetooth Support
 * Starts listening to BlueTooth Events.

If this is not called, and you try to use the plugin without initialization, everything will fail. 

This is how you do it:

```javascript
BluetoothManagerPlugin.initialize()
  .then(() => {
    console.log('Initialized !')
    
  })
  .catch(() => {
    console.log('Dont have permissions to work with Bluetooth')
    
  })
``` 

From this point onward you can use any other methods. 
