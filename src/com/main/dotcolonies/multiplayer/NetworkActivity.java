package com.main.dotcolonies.multiplayer;

import com.main.dotcolonies.R;
import com.main.dotcolonies.R.layout;
import com.main.dotcolonies.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NetworkActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_network);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
