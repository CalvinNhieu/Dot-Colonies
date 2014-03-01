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
	public static final float GAME_FPS = 60f; // target frame rate 
	public static final int GAME_THREAD_FPS_SLEEP = (int) (1000/GAME_FPS); // value to output 60fps
	public static Thread musicThread; // separate thread to run music player service
	
	// IMAGES
	public static final int BACKGROUND_LAYER = R.drawable.gamebkg; // image to display as game bkg
	public static final int DOT_IMG = R.drawable.dot; // sprite for dot animations
	public static final int COLONY_SPRITESHEET = R.drawable.colonyspritesheet;
	
	// GLOBAL GAME VARS
	public static final float acceleration = 0.5f; // player's acceleration
	public static final float lag = 0.93f; // to provide a smoother
	public static final int FRAME_SPAWN_RATE = 1; // number of frames per dot spawn
	public static final int DOT_CAPACITY = 50; // MAXIMUM # OF DOTS TO SPAWN
	public static int frameCount = 0; // counts number of frames 
	public static float SCROLL_BACKGROUND_1 = 0.001f; // speed at which background 1 will scroll
	public static float SCROLL_BACKGROUND_2 = -0.00075f; // speed at which background 2 will scroll
	public static final ArrayList<Dot> dotContainer = new ArrayList<Dot>(); // contains all the dots
	public static final ArrayList<Colony> colonyContainer = new ArrayList<Colony>(); // contains all colonies
	public static int selectedColonyIndex = -1; // index of the currently selected colony (-1 means none selected)
	public static long last = System.currentTimeMillis();
	public static long  curr = 0;
	public static float delta = 0;
	public static float FPS = 0;
	
	// update colonies' statuses
	public static void updateColonies () {
		for (int i=1;i<colonyContainer.size();i++) {
			colonyContainer.get(i).setAcquired(false);
		}
		for (int i=0;i<dotContainer.size();i++) {
			if (dotContainer.get(i).getParentColonyIndex() != -1) 
				colonyContainer.get(dotContainer.get(i).getParentColonyIndex()).setAcquired(true);
		}
	}
	
	// see if a coordinate is contained in a colony
	public static boolean contains (ArrayList<Colony> c, float x, float y) {
		boolean contained = false;
		for (int i=0;i<c.size();i++) {
			float centerX = c.get(i).getX() + Colony.getRadius(); // coordinates of the center of the colony
			float centerY = c.get(i).getY() + Colony.getRadius();
			float distanceFromCenter = hypotenuse(x, y, centerX, centerY); // calculate distance between dot and center
			if(distanceFromCenter <= Colony.getRadius()) contained = true; // true if dot is inside
		}
		return contained;
	}
	
	// returns which colony contains point
	public static Colony whichContains (ArrayList<Colony> c, float x, float y) {
		for (int i=0;i<c.size();i++) {
				float distanceFromCenter = hypotenuse(x, y, c.get(i).getCenterX(), c.get(i).getCenterY()); // calculate distance between dot and center
				if(distanceFromCenter <= Colony.getRadius()) return c.get(i); // true if dot is inside
		}
		return null;
	}
	
	// checks if any colonies are currently selected
	public static boolean isColonySelected () {
		for (int i=0;i<colonyContainer.size();i++) {
			if (colonyContainer.get(i).isSelected()) {
				selectedColonyIndex = i;
				return true;
			}
		}
		selectedColonyIndex = -1;
		return false;
	}
	
	// update player's values
	public static void updateDots () {
		spawnDots();
		for (int i=0;i<dotContainer.size();i++) {
			
			updateDotBehaviour(contains(colonyContainer, dotContainer.get(i).getxPos(), dotContainer.get(i).getyPos()), dotContainer.get(i));
			
			moveToTarget(dotContainer.get(i));
			moveDot(dotContainer.get(i));

		}
	}
	
	// determines dot behaviour logic based on whether dot is live or not
	public static void updateDotBehaviour(boolean indicator, Dot d) {
		if (d.isLive()) { // if live, wait for dot to enter new target colony
			checkForReEntry(d);
		}
		else if (!d.isLive()) { // if dormant, act dormant inside current colony
			dormantDotBehaviour(d);
		}
	}
	
	// spawn dots in acquired colonies
	public static void spawnDots() {
		if (frameCount > FRAME_SPAWN_RATE) frameCount = 0;
		else if (frameCount >= FRAME_SPAWN_RATE && dotContainer.size()<DOT_CAPACITY) 
			dotContainer.add(new Dot(colonyContainer.get(0).getCenterX(),colonyContainer.get(0).getCenterY(),0));
		frameCount++;
	}
	
	
	// INITIATE DORMANT DOT BEHAVIOUR AFTER SPAWN
	public static void initiateDot (Dot d) {
		Colony containingColony = whichContains(colonyContainer, d.getxPos(), d.getyPos()); // is the colony dot is inside
		double theta = Math.random()*2.0*Math.PI; // calculate a random angle 
		d.setxTarget((float) (Colony.getRadius()*Math.cos(theta)) + containingColony.getCenterX()); // redirect dot's target to random point on colony's edge
		d.setyTarget((float) (Colony.getRadius()*Math.sin(theta)) + containingColony.getCenterY());
	}
	
	// HOW THE DOT BEHAVES WITHIN A COLONY
	public static void dormantDotBehaviour (Dot d) {
		Colony containingColony = whichContains(colonyContainer, d.getxPos(), d.getyPos()); // is the colony dot is inside
		float dist = hypotenuse(d.getxPos(),d.getyPos(),containingColony.getCenterX(),containingColony.getCenterY()); // distance from dot and colony's center
		if (dist > (Colony.getRadius()-80)) { // if dot is nearing the edge
			
			double theta = Math.random()*2.0*Math.PI; // calculate a random angle 
			d.setxTarget((float) (Colony.getRadius()*Math.cos(theta)) + containingColony.getCenterX()); // redirect dot's target to random point on colony's edge
			d.setyTarget((float) (Colony.getRadius()*Math.sin(theta)) + containingColony.getCenterY());
		}
	}
	
	// method to check and reset dots to dormant behaviour (upon reentering target colony)
	public static void checkForReEntry (Dot d) {
		if (hypotenuse(d.getxPos(), d.getyPos(), colonyContainer.get(d.getTargetColonyIndex()).getCenterX(), colonyContainer.get(d.getTargetColonyIndex()).getCenterY()) <= Colony.getRadius()) {
			d.setParentColonyIndex(d.getTargetColonyIndex());
			d.setTargetColonyIndex(-1);
			d.setLive(false);
		}
	}
	
	// BEZIER CURVE METHODS
	private static void pushDot(Dot d) {
		d.setDistance(hypotenuse(d.getxPos(), d.getyPos(), d.getxTarget(), d.getyTarget()));
		d.setxPos(d.getxPos() + (acceleration*((d.getxTarget()-d.getxPos())/d.getDistance()))*delta*GAME_FPS);
		d.setyPos(d.getyPos() + (acceleration*((d.getyTarget()-d.getyPos())/d.getDistance()))*delta*GAME_FPS);
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
	
	
	
	public static void setSize(float x, float y) {
		X_MAX = x;
		Y_MAX = y;
	}
	
	public static void setDelta () {
		curr = System.currentTimeMillis();
		FPS = 1000/(curr-last);
		if (FPS < 30) FPS = 30;
		delta = 0.6f/FPS;
		last = System.currentTimeMillis();
	}
	
	// housekeeping ... cleans up when app is exited
	public static void onExit (View v) {

		Intent playBkgMusic = new Intent(context, GameMusic.class);
		context.stopService(playBkgMusic);
		musicThread.stop(); // idk what to do about this
		
	}
	
}


