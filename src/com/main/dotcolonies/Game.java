package com.main.dotcolonies;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Game extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

}
