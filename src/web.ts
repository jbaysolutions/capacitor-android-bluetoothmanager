import { WebPlugin } from '@capacitor/core';
import { BluetoothManagerPluginPlugin } from './definitions';

export class BluetoothManagerPluginWeb extends WebPlugin implements BluetoothManagerPluginPlugin {
  constructor() {
    super({
      name: 'BluetoothManagerPlugin',
      platforms: ['web'],
    });
  }

  async initialize(): Promise<void> {

  }

  async hasBluetoothSupport(): Promise<{ hwSupport: boolean }> {
    return { hwSupport: false };
  }

  async isBluetoothEnabled(): Promise<{ enabled: boolean }> {
    return { enabled: false };
  }

  async isDiscoverable(): Promise<{ discoverable: boolean }> {
    return { discoverable: false };
  }

  async getName(): Promise<{ name: string }> {
    throw TypeError("No hardware support or no permissions")
  }

  async enableBluetooth(): Promise<void> {
    throw TypeError("No hardware support or no permissions");
  }

  async disableBluetooth(): Promise<void> {
    throw TypeError("No hardware support or no permissions");
  }

  async setDeviceName(deviceName: string): Promise<void> {
    throw TypeError("No hardware support or no permissions : " + deviceName);
  }

  async setDiscoverable(duration: number): Promise<void> {
    throw TypeError("No hardware support or no permissions : " + duration);
  }
}

const BluetoothManagerPlugin = new BluetoothManagerPluginWeb();

export { BluetoothManagerPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BluetoothManagerPlugin);
