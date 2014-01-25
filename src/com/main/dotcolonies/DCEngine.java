package com.main.dotcolonies;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class DCEngine {

	// Constants used in game
	public static final int GAME_THREAD_DELAY = 1000; // delay before game thread begins (splash screen display time)
	public static final int MENU_BUTTON_ALPHA = 0; // transparency for menu button
	public static final boolean HAPTIC_BUTTON_FEEDBACK = true; // constant for button vibrate feedback
	public static final int MAIN_MENU_MUSIC = R.raw.mainmenu; // main menu sound file location
	public static final int R_VOLUME = 100; // constant for right ear volume output
	public static final int L_VOLUME = 100; // constant for left ear volume output
	public static final boolean LOOP_BKG_MUSIC = true; // constant for looping music flag
	public static final int GAME_THREAD_FPS_SLEEP = (1000/60); // value to output 60fps
	public static Context context; // Context of the engine
	public static Thread musicThread; // separate thread to run music player service
	public static final int BACKGROUND_LAYER = R.drawable.gamebkg; // image to display as game bkg
	public static int dotRenderState = 1; // for dot spinning animation
	public static final int DOT_SPRITE_SHEET = R.drawable.mainsprites; // sprite for dot animations
	public static final int DOT_POS_1 = 1; // dot animation state x4...
	public static final int DOT_POS_2 = 2;
	public static final int DOT_POS_3 = 3;
	public static final int DOT_POS_4 = 4;
	public static final int FRAMES_PER_ANIM = 2; // # of frames per spin animation update
	public static float playerPosX = 19.0f; // starting dot xpos
	public static float playerPosY = 27.0f; // starting dot ypos
	public static float playerPosX_ = playerPosX; // post instance of player xpos for bezier curve implementation
	public static float playerPosY_ = playerPosY; // post instance of player ypos for bezier curve implementation
	public static float targetX = playerPosX; // player's target (moving towards) X	
	public static float targetY = playerPosY; // player's target (moving towards) Y
	public static final float acceleration = 0.03f; // player's acceleration value
	public static final float lag = 0.94f; // to provide a smoother movement
	public static float distance = 0; // distance from player to target
	public static float SCROLL_BACKGROUND_1 = 0.001f; // speed at which background 1 will scroll
	public static float SCROLL_BACKGROUND_2 = -0.00075f; // speed at which background 2 will scroll
	public static final float X_SCALE = 54.0f; // scale value from android canvas to OpenGL
	public static final float Y_SCALE = 64.54f; // scale value from android canvas to OpenGL
	public static final float Y_CANVAS_MAX = 1775.0f; // android canvas y max value
	
	// housekeeping ... cleans up when app is exited
	public void onExit (View v) {

		Intent playBkgMusic = new Intent(context, GameMusic.class);
		context.stopService(playBkgMusic);
		musicThread.stop(); // idk what to do about this
		
	}
	
}


