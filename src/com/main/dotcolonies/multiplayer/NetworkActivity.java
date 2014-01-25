package com.main.dotcolonies.multiplayer;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.view.Menu;

import com.main.dotcolonies.R;

public class NetworkActivity extends Activity {

	// listener for wifi connection management (can listen for different attributes of current wifi connection)
	private final IntentFilter intentFilter = new IntentFilter();
	private WifiP2pManager wifiManager;
	private Channel ch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_network);
		
		//Indicates a change in the Wi-Fi P2P status.
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

	    // Indicates a change in the list of available peers.
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

	    // Indicates the state of Wi-Fi P2P connectivity has changed.
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

	    // Indicates this device's details have changed.
	    intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	
	    wifiManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
	    ch = wifiManager.initialize(this, getMainLooper(), null);
	    
	    
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
