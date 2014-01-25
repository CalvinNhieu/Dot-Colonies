package com.main.dotcolonies.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

import com.main.dotcolonies.DCEngine;
import com.main.dotcolonies.Dot;

// GAME LOGIC CLASS
public class GameRenderer implements Renderer{
	
	private GameBackground background_layer_1 = new GameBackground(); // background instance 1
	private GameBackground background_layer_2 = new GameBackground(); // background instance 2 for layering effect
	
	private Dot dot = new Dot(); // create a new dot. The player
	private int frameCounter = 0; // to control animation rates
	
	private float bkgScroll1; // instance to hold background pos
	private float bkgScroll2; // instance to hold background pos
	
	// apply opengl matrix transformations to
	// background 1 texture
	// draw texture
	private void scrollBackground1(GL10 gl) {
		if (bkgScroll1 == Float.MAX_VALUE) {
			bkgScroll1 = 0f;
		}
		gl.glMatrixMode(GL10.GL_MODELVIEW); // set matrix mode to manipulate VERTICES (view)
		gl.glLoadIdentity(); // Resets the matrix ( but to what? )
		gl.glPushMatrix(); // push the matrix off the stack to manipulate individual vertices and not all...
		gl.glScalef(1f, 1f, 1f); // Default scale params (original size, 100%)
		gl.glTranslatef(0f,0f,0f); // Default translate params  (no movement)
		//I THINK THE ABOVE 5 LINES SET THE VERTICES TO A FIXED POSITION...
		//SO THAT THE TRANSLATION OF THE TEXTURE POINTS DOESN'T GET AFFECTED?
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(bkgScroll1,bkgScroll1,0.0f); // setting scrolling transformations
		
		background_layer_1.draw(gl); // renders background_layer_1 texture to screen
		gl.glPopMatrix(); // pops matrix WITH SCROLLING TRANSFORMATIONS back onto the stack to be rendered or some shit
		bkgScroll1 += DCEngine.SCROLL_BACKGROUND_1; // what the fuck is this some simple math i am not comprehending
		gl.glLoadIdentity(); // resets matrix mode settings
	}
	
	// apply opengl matrix transformations to
		// background 2 texture
		// draw texture
	private void scrollBackground2(GL10 gl) {
		if (bkgScroll2 == Float.MAX_VALUE) {
			bkgScroll2 = 0f;
		}
		gl.glMatrixMode(GL10.GL_MODELVIEW); // set matrix mode to manipulate VERTICES (view)
		gl.glLoadIdentity(); // Resets the matrix ( but to what? )
		gl.glPushMatrix(); // push the matrix off the stack to manipulate individual vertices and not all...
		gl.glScalef(1f, 1f, 1f); // Default scale params (original size, 100%)
		gl.glTranslatef(0f,0f,0f); // Default translate params  (no movement)
		//I THINK THE ABOVE 5 LINES SET THE VERTICES TO A FIXED POSITION...
		//SO THAT THE TRANSLATION OF THE TEXTURE POINTS DOESN'T GET AFFECTED?
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(-bkgScroll2,bkgScroll2,0.0f); // setting scrolling transformations
		
		background_layer_2.draw(gl); // renders background_layer_1 texture to screen
		gl.glPopMatrix(); // pops matrix WITH SCROLLING TRANSFORMATIONS back onto the stack to be rendered or some shit
		bkgScroll2 += DCEngine.SCROLL_BACKGROUND_2; // what the fuck is this some simple math i am not comprehending
		gl.glLoadIdentity(); // resets matrix mode settings
	}
	
	
	// BEZIER CURVE METHODS
	private void pushDot() {
		DCEngine.distance = hypotenuse();
		DCEngine.playerPosX += DCEngine.acceleration*((DCEngine.targetX-DCEngine.playerPosX)/DCEngine.distance);
		DCEngine.playerPosY += DCEngine.acceleration*((DCEngine.targetY-DCEngine.playerPosY)/DCEngine.distance);
	}
	
	private float hypotenuse() { // calculates hypotenuse of a triangle
		return (float) (Math.sqrt((DCEngine.playerPosX-DCEngine.targetX)*(DCEngine.playerPosX-DCEngine.targetX) + (DCEngine.playerPosY-DCEngine.targetY)*(DCEngine.playerPosY-DCEngine.targetY)));
	}
	
	private void moveToTarget() {
		if (Math.abs(DCEngine.playerPosX-DCEngine.targetX)<0.05f &&Math.abs(DCEngine.playerPosY-DCEngine.targetY)<0.05f) {
			DCEngine.playerPosX_ = DCEngine.playerPosX = DCEngine.targetX;
			DCEngine.playerPosY_ = DCEngine.playerPosY = DCEngine.targetY;
		}
		else {
			pushDot();
		}
	}
	
	private void moveDot() {
		float x_temp = DCEngine.playerPosX;
		float y_temp = DCEngine.playerPosY;
		DCEngine.playerPosX += (DCEngine.playerPosX - DCEngine.playerPosX_)*DCEngine.lag;
		DCEngine.playerPosY += (DCEngine.playerPosY - DCEngine.playerPosY_)*DCEngine.lag;
		DCEngine.playerPosX_ = x_temp;
		DCEngine.playerPosY_ = y_temp;
	}
	// BEZIER CURVES METHODS END
	
	// update player's values
	private void updateDot (GL10 gl) {
		moveToTarget();
		moveDot();
	}
	
	// prepare and draw player's 
	// dot texture based on
	// animation states based on
	// frame count
	private void drawDot(GL10 gl) {
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.05f,0.035f,1f);
		gl.glTranslatef(DCEngine.playerPosX,DCEngine.playerPosY,0f);
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		switch (DCEngine.dotRenderState) {
		case DCEngine.DOT_POS_1:
			gl.glTranslatef(0.0f,0.0f,0.0f);
			break;
		case DCEngine.DOT_POS_2:
			gl.glTranslatef(0.25f,0.0f,0.0f);
			break;
		case DCEngine.DOT_POS_3:
			gl.glTranslatef(0.5f,0.0f,0.0f);
			break;
		case DCEngine.DOT_POS_4:
			gl.glTranslatef(0.75f,0.0f,0.0f);
			break;
		}
		dot.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		frameCounter++;
		
		if (frameCounter==DCEngine.FRAMES_PER_ANIM) {
			frameCounter = 0;
			DCEngine.dotRenderState++;
			if (DCEngine.dotRenderState == 5) {
				DCEngine.dotRenderState = 1;
			}
		}
	}
	
	// constantly called. THE GAME LOOP
	@Override
	public void onDrawFrame(GL10 gl) {
		try {
			Thread.sleep(DCEngine.GAME_THREAD_FPS_SLEEP); // regulates to 60fps
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// clear the screen
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT); // must be called before transforming and rendering matrices?
		
		// BEGIN GAME LOGIC
		updateDot(gl);
		drawDot(gl);
		scrollBackground1(gl);
		scrollBackground2(gl);
		
	}
	
	// is called when created
	// instantiates and sets initial
	// rendering behaviours
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// the next 4 lines allow 
		// the flexibility to render
		// in 2D ?...
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		// enabling transparency
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
		
		// add all textures here
	}
	
	// is called on initial startup
	// screen orientation change
	// and screen resizing
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// defines surface size (sort of...Lens? What?)
		// width and height are sent in
		// Default: width and height of screen minus top notif bar
		gl.glViewport(0,0,width,height);
		
		// allows you to work with how the scene is rendered
		gl.glMatrixMode(GL10.GL_PROJECTION);
		
		// "default state" of opengl
		gl.glLoadIdentity();
		
		// THE PROCESSOR (sounds important)
		gl.glOrthof(0f, 1f, 0f, 1f, -1f, 1f);
		
		//load and draw background_layer_1 texture
		background_layer_1.loadTexture(gl, DCEngine.BACKGROUND_LAYER, DCEngine.context);
		background_layer_2.loadTexture(gl, DCEngine.BACKGROUND_LAYER, DCEngine.context);
		
		dot.loadTexture(gl, DCEngine.DOT_SPRITE_SHEET, DCEngine.context);
	}

}
