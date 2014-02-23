package com.main.dotcolonies.game;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;

import com.main.dotcolonies.DCEngine;

public class Game extends Activity {

	private GameView gView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gView = new GameView (this);
		setContentView(gView);
	}
	
	
	// GET SCREEN TOUCH EVENTS...
	@Override
	public boolean onTouchEvent (MotionEvent event) {
		float X = (float) event.getX();
		float Y = (float) (DCEngine.Y_MAX - event.getY());
		Colony tappedColony = DCEngine.whichContains(DCEngine.colonyContainer, X, Y); // find and store that colony
		
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (DCEngine.contains(DCEngine.colonyContainer, X, Y)) { // if mouseclick is inside a colony
				
				// simply put a colony in selection if none are selected
				if (!DCEngine.isColonySelected()) {
					for (int i=0;i<DCEngine.colonyContainer.size();i++) {
						DCEngine.colonyContainer.get(i).setSelected(false); // set all other colonies to non-selected first
					}
					DCEngine.colonyContainer.get(tappedColony.getIndex()).setSelected(true); // set that colony to selected
					DCEngine.selectedColonyIndex = tappedColony.getIndex(); // set it in the engine as well...
				}
				// if a colony is already selected and previous and new colonies are unique, send all dots inside colony to new colonies center
				else if (tappedColony != DCEngine.colonyContainer.get(DCEngine.selectedColonyIndex)) {
					for (int i=0;i<DCEngine.dotContainer.size();i++) { // loop through all dots
						if (DCEngine.dotContainer.get(i).getParentColonyIndex() == DCEngine.selectedColonyIndex) { // if dot belongs to selected colony
							DCEngine.dotContainer.get(i).setLive(true); // set live to exit dormant behaviour
							DCEngine.dotContainer.get(i).setxTarget(tappedColony.getCenterX()); // redirect dot's target to new colony
							DCEngine.dotContainer.get(i).setyTarget(tappedColony.getCenterY()); 
							
							DCEngine.dotContainer.get(i).setTargetColonyIndex(tappedColony.getIndex()); // set dot's target colony to new colony
						}
					}
					for (int i=0;i<DCEngine.colonyContainer.size();i++) { // loop through colonies
						DCEngine.colonyContainer.get(i).setSelected(false); // set all colonies to non-selected
					}
					DCEngine.selectedColonyIndex = -1; // set in engine as well
				}
			}
			else if (!DCEngine.contains(DCEngine.colonyContainer, X, Y)) {
				//DCEngine.dotContainer.get(0).setxTarget(X);
				//DCEngine.dotContainer.get(0).setyTarget(Y);
				for (int i=0;i<DCEngine.colonyContainer.size();i++) {
					DCEngine.colonyContainer.get(i).setSelected(false);
				}
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
