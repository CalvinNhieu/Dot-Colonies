package com.main.dotcolonies;

import java.util.ArrayList;

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
	public static final int DOT_IMG = R.drawable.dot; // sprite for dot animations
	public static final float acceleration = 0.015f; // player's acceleration value
	public static final float lag = 0.93f; // to provide a smoother movement
	public static float SCROLL_BACKGROUND_1 = 0.001f; // speed at which background 1 will scroll
	public static float SCROLL_BACKGROUND_2 = -0.00075f; // speed at which background 2 will scroll
	public static final float X_SCALE = 54.0f; // scale value from android canvas to OpenGL
	public static final float Y_SCALE = 64.54f; // scale value from android canvas to OpenGL
	public static final float Y_CANVAS_MAX = 1775.0f; // android canvas y max value
	public static final ArrayList<Dot> dotContainer = new ArrayList<Dot>(); // contains all the dots
	
	// BEZIER CURVE METHODS
		private static void pushDot(Dot d) {
			d.setDistance(hypotenuse(d));
			d.setxPos(d.getxPos() + acceleration*((d.getxTarget()-d.getxPos())/d.getDistance()));
			d.setyPos(d.getyPos() + acceleration*((d.getyTarget()-d.getyPos())/d.getDistance()));
		}
		
		private static float hypotenuse(Dot d) { // calculates hypotenuse of a triangle
			return (float) (Math.sqrt((d.getxPos()-d.getxTarget())*(d.getxPos()-d.getxTarget()) + (d.getyPos()-d.getyTarget())*(d.getyPos()-d.getyTarget())));
		}
		
		public static void moveToTarget(Dot d) {
			if (Math.abs(d.getxPos()-d.getxTarget())<0.001f &&Math.abs(d.getyPos()-d.getyTarget())<0.001f) {
				d.setxPos(d.getxTarget()); 
				d.setyPos(d.getyTarget());
				d.setxPos_(d.getxPos());
				d.setyPos_(d.getyPos());
			}
			else {
				pushDot(d);
			}
		}
		
		public static void moveDot(Dot d) {
			float x_temp = d.getxPos();
			float y_temp = d.getyPos();
			d.setxPos(d.getxPos() + ((d.getxPos() - d.getxPos_())*lag));
			d.setyPos(d.getyPos() + ((d.getyPos() - d.getyPos_())*lag));
			d.setxPos_(x_temp);
			d.setyPos_(y_temp);
		}
		// BEZIER CURVES METHODS END
	
	// housekeeping ... cleans up when app is exited
	public void onExit (View v) {

		Intent playBkgMusic = new Intent(context, GameMusic.class);
		context.stopService(playBkgMusic);
		musicThread.stop(); // idk what to do about this
		
	}
	
}


