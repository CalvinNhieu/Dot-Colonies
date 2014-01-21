package com.main.dotcolonies.game;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Game extends Activity {

	private GameView gView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gView = new GameView (this);
		setContentView(gView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	
}
