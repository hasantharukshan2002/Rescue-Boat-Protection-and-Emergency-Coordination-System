import { useState, useEffect } from 'react';
import { storage } from './utils/localStorage';
import { playNotificationSound } from './utils/sound';
import { vibrate } from './utils/vibration';
import { getCurrentRoute, routes, navigate } from './utils/routes';
import { LoginScreen } from './components/LoginScreen';
import { AlertsScreen } from './components/AlertsScreen';
import { MapScreen } from './components/MapScreen';
import { SOSScreen } from './components/SOSScreen';
import { SettingsScreen } from './components/SettingsScreen';
import { BottomNav } from './components/BottomNav';

import { BluetoothProvider, useBluetooth } from './context/BluetoothContext';
import { BluetoothStatusIcon } from './components/BluetoothStatusIcon';
import { Alert } from './models/types';

function AppContent() {
  const [currentRoute, setCurrentRoute] = useState(getCurrentRoute());
  const [isReady, setIsReady] = useState(false);
  // Login shows only once per app session (cold start), not on every navigation
  const [isLoggedIn, setIsLoggedIn] = useState(() => {
    return sessionStorage.getItem('session_logged_in') === 'true' ||
           storage.getUserRole() !== null;
  });
  const { isConnected, lastReceivedData } = useBluetooth();

  useEffect(() => {
    setIsReady(true);

    const handleHashChange = () => {
      setCurrentRoute(getCurrentRoute());
    };

    window.addEventListener('hashchange', handleHashChange);
    return () => window.removeEventListener('hashchange', handleHashChange);
  }, []);

  useEffect(() => {
    if (isConnected) {
      navigate(routes.MAP);
    }
  }, [isConnected]);

  // Global Bluetooth Data Listener for SOS
  useEffect(() => {
    if (lastReceivedData && lastReceivedData.startsWith('SOS')) {
      const parts = lastReceivedData.trim().split(',');
      const lat = parseFloat(parts[1]) || 0;
      const lng = parseFloat(parts[2]) || 0;
      
      const sosAlert: Alert = {
        id: `HW-SOS-${Date.now()}`,
        type: 'SOS',
        timestamp: new Date().toISOString(),
        boatId: "Hardware Button",
        status: 'New',
        severity: 'high',
        lat,
        lng,
        message: 'Hardware SOS Triggered!'
      };

      storage.addAlert(sosAlert);
      
      const settings = storage.getSettings();
      if (settings.sound) playNotificationSound();
      if (settings.vibration) vibrate([200, 100, 200, 100, 200]);
      
      // Auto-navigate to Alerts screen to show the SOS
      navigate(routes.ALERTS);
    }
  }, [lastReceivedData]);

  if (!isReady) {
    return null;
  }


  const showLogin = !isLoggedIn || currentRoute === routes.LOGIN;

  const handleLogin = () => {
    sessionStorage.setItem('session_logged_in', 'true');
    setIsLoggedIn(true);
    navigate(routes.ALERTS);
  };

  if (showLogin) {
    return <LoginScreen onLogin={handleLogin} />;
  }

  const renderScreen = () => {
    switch (currentRoute) {
      case routes.ALERTS:
        return <AlertsScreen />;
      case routes.MAP:
        return <MapScreen />;
      case routes.SOS:
        return <SOSScreen />;
      case routes.SETTINGS:
        return <SettingsScreen />;
      default:
        return <AlertsScreen />;
    }
  };

  return (
    <div className="min-h-screen bg-slate-900">
      <BluetoothStatusIcon />
      {renderScreen()}
      <BottomNav />
    </div>
  );
}

function App() {
  return (
    <BluetoothProvider>
      <AppContent />
    </BluetoothProvider>
  );
}

export default App;