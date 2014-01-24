package com.main.dotcolonies;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class DCEngine {

	// Constants used in game
	public static final int GAME_THREAD_DELAY = 1000;
	public static final int MENU_BUTTON_ALPHA = 0;
	public static final boolean HAPTIC_BUTTON_FEEDBACK = true;
	public static final int MAIN_MENU_MUSIC = R.raw.mainmenu;
	public static final int R_VOLUME = 100;
	public static final int L_VOLUME = 100;
	public static final boolean LOOP_BKG_MUSIC = true;
	public static final int GAME_THREAD_FPS_SLEEP = (1000/60);
	public static Context context;
	public static Thread musicThread;
	public static final int BACKGROUND_LAYER = R.drawable.gamebkg;
	public static int dotRenderState = 0;
	
	// housekeeping ... cleans up when app is exited
	public void onExit (View v) {

		Intent playBkgMusic = new Intent(context, GameMusic.class);
		context.stopService(playBkgMusic);
		musicThread.stop(); // idk what to do about this
		
	}
	
}


