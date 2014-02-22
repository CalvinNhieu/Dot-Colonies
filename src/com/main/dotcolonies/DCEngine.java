package com.main.dotcolonies;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.main.dotcolonies.game.Colony;
import com.main.dotcolonies.game.Dot;

public class DCEngine {
	
	// Constants used in game
	
	// SCREEN
	public static Context context; // Context of the engine
	public static float X_MAX; // max x value of screen
	public static float Y_MAX; // max y value of screen
	
	// MENU VARS
	public static final int GAME_THREAD_DELAY = 1000; // delay before game thread begins (splash screen display time)
	public static final int MENU_BUTTON_ALPHA = 0; // transparency for menu button
	public static final boolean HAPTIC_BUTTON_FEEDBACK = true; // constant for button vibrate feedback
	public static final int MAIN_MENU_MUSIC = R.raw.mainmenu; // main menu sound file location
	public static final int R_VOLUME = 100; // constant for right ear volume output
	public static final int L_VOLUME = 100; // constant for left ear volume output
	public static final boolean LOOP_BKG_MUSIC = true; // constant for looping music flag
	public static final int GAME_THREAD_FPS_SLEEP = (1000/60); // value to output 60fps
	public static Thread musicThread; // separate thread to run music player service
	
	
	// IMAGES
	public static final int BACKGROUND_LAYER = R.drawable.gamebkg; // image to display as game bkg
	public static final int DOT_IMG = R.drawable.dot; // sprite for dot animations
	public static final int COLONY_SPRITESHEET = R.drawable.colonyspritesheet;
	
	// GLOBAL GAME VARS
	public static final float acceleration = 0.5f; // player's acceleration value
	public static final float lag = 0.93f; // to provide a smoother movement
	public static float SCROLL_BACKGROUND_1 = 0.001f; // speed at which background 1 will scroll
	public static float SCROLL_BACKGROUND_2 = -0.00075f; // speed at which background 2 will scroll
	public static final ArrayList<Dot> dotContainer = new ArrayList<Dot>(); // contains all the dots
	public static final ArrayList<Colony> colonyContainer = new ArrayList<Colony>(); // contains all colonies
	
	// see if a coordinate is contained in a colony
	public static boolean contains (ArrayList<Colony> c, float x, float y) {
		boolean contained = false;
		for (int i=0;i<c.size();i++) {
			float centerX = c.get(i).getX() + c.get(i).getRadius(); // coordinates of the center of the colony
			float centerY = c.get(i).getY() + c.get(i).getRadius();
			float distanceFromCenter = hypotenuse(x, y, centerX, centerY); // calculate distance between dot and center
			if(distanceFromCenter <= c.get(i).getRadius()) contained = true; // true if dot is inside
		}
		return contained;
	}
	
	// returns which colony contains point
	public static Colony whichContains (ArrayList<Colony> c, float x, float y) {
		for (int i=0;i<c.size();i++) {
			float distanceFromCenter = hypotenuse(x, y, c.get(i).getCenterX(), c.get(i).getCenterY()); // calculate distance between dot and center
			if(distanceFromCenter <= c.get(i).getRadius()) return c.get(i); // true if dot is inside
		}
		return null;
	}
	
	// BEZIER CURVE METHODS
	private static void pushDot(Dot d) {
		d.setDistance(hypotenuse(d.getxPos(), d.getyPos(), d.getxTarget(), d.getyTarget()));
		d.setxPos(d.getxPos() + acceleration*((d.getxTarget()-d.getxPos())/d.getDistance()));
		d.setyPos(d.getyPos() + acceleration*((d.getyTarget()-d.getyPos())/d.getDistance()));
	}
	
	private static float hypotenuse(float x1, float y1, float x2, float y2) { // calculates hypotenuse of a triangle
		return (float) (Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)));
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
	public static void onExit (View v) {

		Intent playBkgMusic = new Intent(context, GameMusic.class);
		context.stopService(playBkgMusic);
		musicThread.stop(); // idk what to do about this
		
	}
	
	public static void setSize(float x, float y) {
		X_MAX = x;
		Y_MAX = y;
	}
	
}


