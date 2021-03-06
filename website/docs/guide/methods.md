# Methods

## hasBluetoothSupport()
 
Definition: `hasBluetoothSupport(): Promise<{ hwSupport: boolean }>;`

```javascript
BluetoothManagerPlugin.hasBluetoothSupport()
    .then(answer => {
        if (answer.hwSupport) {
            // YES WE HAVE SUPPORT
        } else {
            // NO WE DONT HAVE SUPPORT
        }
    })
```

## isBluetoothEnabled()

Definition: `isBluetoothEnabled(): Promise<{ enabled: boolean }>;`

Error can be thrown if the adaptor is not inited, which can happen in one of two conditions :
* initialize() was not called
* the device doesn't have BT support. IF initialize was called, you can use `hasBluetoothSupport` to check this.  

```javascript
BluetoothManagerPlugin.isBluetoothEnabled()
    .then(result => {
        console.log('Is bluetooth enabled? ' + result.enabled);
    })
    .catch(error => {
        console.log('Error : ' + JSON.stringify(error))
    })
```

## enableBluetooth()

Definition: `enableBluetooth(): Promise<void>;`

**THIS METHOD IS ASYNC!** 

Error can be thrown if :
* Was not initialized
* Already Enabled
* Already busy disabling or enabling BT 

```javascript
BluetoothManagerPlugin.enableBluetooth()
    .then(()) => {
        console.log('Called, and no error happened');
    })
    .catch(error => {
        console.log('Error : ' + JSON.stringify(error))
    })
```

## disableBluetooth()

Definition: `disableBluetooth(): Promise<void>;`    

**THIS METHOD IS ASYNC!** 

Error can be thrown if :
* Was not initialized
* Already Disabled
* Already busy disabling or enabling BT 

```javascript
BluetoothManagerPlugin.disableBluetooth()
    .then(()) => {
        console.log('Called, and no error happened');
    })
    .catch(error => {
        console.log('Error : ' + JSON.stringify(error))
    })
```

## getName()

Definition: `getName(): Promise<{ name: string }>;`

```javascript
BluetoothManagerPlugin.getName()
    .then((result) => {
        console.log(' The name is  : ' + result.name)
    })
    .catch(error => {
        console.log(' Hw support failure : ' + JSON.stringify(error))
    })
```

## setDeviceName()

Definition: `setDeviceName(deviceName: string): Promise<void>;`

Errors can be thrown if :
* BT adapter was not inited.
* BT adapter is in a state that does not allow for this change
* the new provided name is null .... you can't set name to null, ok?

```javascript
BluetoothManagerPlugin.setDeviceName({
      deviceName: 'NEW_NAME_HERE'
    })
    .then(() => {
        console.log(' Name Changed !')
    })
    .catch(error => {
        console.log(' Failed for Reason: ' + JSON.stringify(error))
    })
```

## isDiscoverable()

Definition: `  isDiscoverable(): Promise<{ discoverable: boolean }>;`

Error can be thrown if the adaptor is not inited, which can happen in one of two conditions :
* initialize() was not called
* the device doesn't have BT support. IF initialize was called, you can use `hasBluetoothSupport` to check this.  

```javascript
BluetoothManagerPlugin.isDiscoverable()
    .then(result => {
        console.log('Is bluetooth Discoverable? ' + result.discoverable);
    })
    .catch(error => {
        console.log('Error : ' + JSON.stringify(error))
    })
```

## setDiscoverable()

Definition: `setDiscoverable(duration: number): Promise<void>;`

Params:
* duration : is an integer, for the seconds if should stay discoverable, which is capped at 300. So if you put a value like 5000, it is capped at 300 by Android.   

Errors can be thrown if :
* BT adapter was not inited.
* BT adapter is in a state that does not allow for this change
* The plugin couldn't find a Context from where to launch the Discoverable request. (for those confortable with Android, we need `startActivity()` for this, which we call from a `context`.)

If the device is already discoverable, the method does nothing. **It does not extend the amount of time!** . You should keep checking for `isDiscoverable()` and setting it using `setDiscoverable()` when needed.   

```javascript
BluetoothManagerPlugin.setDiscoverable({
      duration: 300
    })
    .then(() => {
        console.log(' We are setting it Discoverable !')
    })
    .catch(error => {
        console.log(' Failed for Reason: ' + JSON.stringify(error))
    })
```
