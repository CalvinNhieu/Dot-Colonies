package com.main.dotcolonies;

import android.view.View;


public class GameLoop extends Thread {
	
	private boolean gameIntact;
	private View game;
	
	public GameLoop(View game) {
		this.game = game;
	}
	
	public void update() {
		
	}
	
	public void render() {
		//game.findViewById(R.id.display).setBackgroundColor(3);
	}
	
	public void setGameState(boolean state) { 
		gameIntact = state;
	}
	
	@Override
	public void run() {
		while (gameIntact) {
			update();
			render();
		}
	}
}
