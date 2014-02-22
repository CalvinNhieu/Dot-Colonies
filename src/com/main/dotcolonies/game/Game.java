package com.main.dotcolonies.game;


import com.main.dotcolonies.DCEngine;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;

public class Game extends Activity {

	private GameView gView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gView = new GameView (this);
		setContentView(gView);
	}
	
	
	// GET SCREEN TOUCH EVENTS...
	// acquire touch coordinates - 
	// scale to opengl float values
	// store in engine
	@Override
	public boolean onTouchEvent (MotionEvent event) {
		DCEngine.dotContainer.get(0).setxTarget(event.getX());
		DCEngine.dotContainer.get(0).setyTarget((DCEngine.Y_MAX - event.getY()));
		
		return false;
	}

	// stoopid method
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	
}
