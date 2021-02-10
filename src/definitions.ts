declare module '@capacitor/core' {
  interface PluginRegistry {
    BluetoothManagerPlugin: BluetoothManagerPluginPlugin;
  }
}

export interface BluetoothManagerPluginPlugin {

  initialize(): Promise<void>;

  hasBluetoothSupport(): Promise<{ hwSupport: boolean }>;

  isBluetoothEnabled(): Promise<{ enabled: boolean }>;

  isDiscoverable(): Promise<{ discoverable: boolean }>;

  getName(): Promise<{ name: string }>;

  enableBluetooth(): Promise<void>;

  disableBluetooth(): Promise<void>;

  setDeviceName(deviceName: string): Promise<void>;

  setDiscoverable(duration: number): Promise<void>;
}
