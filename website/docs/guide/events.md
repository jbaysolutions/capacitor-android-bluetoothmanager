# Events

While the app in running, after the Plugin is initialized, the plugin starts listening to specific events on the Bluetooth Adapter.

## State Changes

When state changes happen on the BT adapter the Plugin notifies Listeners with `BluetoothManagerPluginEvent`

## Listening for Events 

To listen to these events you must add a listener to it.

```javascript
    var bluetoothEventListener = Plugins.BluetoothManagerPlugin.addListener(
        'BluetoothManagerPluginEvent',
        (result) => {
            
            console.log('BluetoothManagerPluginEvent : ' + JSON.stringify(result))
            
            if (result.eventType === 'STATE_TURNING_ON') {
            
            } else if (result.eventType === 'STATE_TURNING_OFF') {
            
            } else if (result.eventType === 'STATE_ON') {
                
            } else if (result.eventType === 'STATE_OFF') {
            
            }
        }
    )
``` 

## Event

The object returned at this time looks like this :

```javascript
{
    eventType: STRING
}
```

Expected `eventType` :
 * **STATE_TURNING_ON**  : When the bluetooth adapter is in the process of turning on
 * **STATE_TURNING_OFF** : When the bluetooth adapter is in the process of turning off
 * **STATE_ON** : When the Bluetooth adapter is now ON
 * **STATE_OFF** : When the Bluetooth adapter is now OFF 

## Unregistering

Remember to unregister it when you leave and don't need to be listening anymore, by calling `remove()`:

```javascript
bluetoothEventListener.remove()
```
