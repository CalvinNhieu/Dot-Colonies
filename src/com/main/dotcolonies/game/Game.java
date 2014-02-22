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
		float X = (float) event.getX();
		float Y = (float) (DCEngine.Y_MAX - event.getY());
		
		if (DCEngine.contains(DCEngine.colonyContainer, X, Y)) {
			DCEngine.whichContains(DCEngine.colonyContainer, X, Y).setSelected(true);
		}
		else if (!DCEngine.contains(DCEngine.colonyContainer, X, Y)) {
			DCEngine.dotContainer.get(0).setxTarget(X);
			DCEngine.dotContainer.get(0).setyTarget(Y);
			for (int i=0;i<DCEngine.colonyContainer.size();i++) {
				DCEngine.colonyContainer.get(i).setSelected(false);
			}
		}
		
		return false;
	}

	// stoopid method
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	
}
