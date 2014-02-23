package com.main.dotcolonies.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLSurfaceView.Renderer;
import android.view.Display;
import android.view.WindowManager;

import com.main.dotcolonies.DCEngine;

// GAME LOGIC CLASS
public class GameRenderer implements Renderer{
	
	private GameBackground background_layer_1 = new GameBackground(); // background instance 1
	
//	private float bkgScroll1; // instance to hold background pos
//	private float bkgScroll2; // instance to hold background pos
	
	// apply opengl matrix transformations to
	// background 1 texture
	// draw texture
//	private void scrollBackground1(GL10 gl) {
//		if (bkgScroll1 == Float.MAX_VALUE) {
//			bkgScroll1 = 0f;
//		}
//		gl.glMatrixMode(GL10.GL_MODELVIEW); // set matrix mode to manipulate VERTICES (view)
//		gl.glLoadIdentity(); // Resets the matrix ( but to what? )
//		gl.glPushMatrix(); // push the matrix off the stack to manipulate individual vertices and not all...
//		gl.glScalef(1f, 1f, 1f); // Default scale params (original size, 100%)
//		gl.glTranslatef(0f,0f,0f); // Default translate params  (no movement)
//		//I THINK THE ABOVE 5 LINES SET THE VERTICES TO A FIXED POSITION...
//		//SO THAT THE TRANSLATION OF THE TEXTURE POINTS DOESN'T GET AFFECTED?
//		
//		gl.glMatrixMode(GL10.GL_TEXTURE);
//		gl.glLoadIdentity();
//		gl.glTranslatef(bkgScroll1,bkgScroll1,0.0f); // setting scrolling transformations
//		
//		background_layer_1.draw(gl); // renders background_layer_1 texture to screen
//		gl.glPopMatrix(); // pops matrix WITH SCROLLING TRANSFORMATIONS back onto the stack to be rendered or some shit
//		bkgScroll1 += DCEngine.SCROLL_BACKGROUND_1; // what the fuck is this some simple math i am not comprehending
//		gl.glLoadIdentity(); // resets matrix mode settings
//	}
//	
//	// apply opengl matrix transformations to
//		// background 2 texture
//		// draw texture
//	private void scrollBackground2(GL10 gl) {
//		if (bkgScroll2 == Float.MAX_VALUE) {
//			bkgScroll2 = 0f;
//		}
//		gl.glMatrixMode(GL10.GL_MODELVIEW); // set matrix mode to manipulate VERTICES (view)
//		gl.glLoadIdentity(); // Resets the matrix ( but to what? )
//		gl.glPushMatrix(); // push the matrix off the stack to manipulate individual vertices and not all...
//		gl.glScalef(1f, 1f, 1f); // Default scale params (original size, 100%)
//		gl.glTranslatef(0f,0f,0f); // Default translate params  (no movement)
//		//I THINK THE ABOVE 5 LINES SET THE VERTICES TO A FIXED POSITION...
//		//SO THAT THE TRANSLATION OF THE TEXTURE POINTS DOESN'T GET AFFECTED?
//		
//		gl.glMatrixMode(GL10.GL_TEXTURE);
//		gl.glLoadIdentity();
//		gl.glTranslatef(-bkgScroll2,bkgScroll2,0.0f); // setting scrolling transformations
//		
//		background_layer_1.draw(gl); // renders background_layer_1 texture to screen
//		gl.glPopMatrix(); // pops matrix WITH SCROLLING TRANSFORMATIONS back onto the stack to be rendered or some shit
//		bkgScroll2 += DCEngine.SCROLL_BACKGROUND_2; // what the fuck is this some simple math i am not comprehending
//		gl.glLoadIdentity(); // resets matrix mode settings
//	}
	
	// prepare and draw player's 
	// dot texture based on
	// animation states based on
	// frame count
	private void drawDots(GL10 gl) {
		for (int i=0;i<DCEngine.dotContainer.size();i++) {
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(1f,1f,1f);
			gl.glTranslatef(DCEngine.dotContainer.get(i).getxPos()/DCEngine.X_MAX,DCEngine.dotContainer.get(i).getyPos()/DCEngine.Y_MAX,0f);
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			DCEngine.dotContainer.get(0).draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
		}
	}
	
	private void drawColonies(GL10 gl) {
		for (int i=0;i<DCEngine.colonyContainer.size();i++) {
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glScalef(1f,1f,1f);
			gl.glTranslatef(DCEngine.colonyContainer.get(i).getX()/DCEngine.X_MAX,DCEngine.colonyContainer.get(i).getY()/DCEngine.Y_MAX,0f);
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			if (DCEngine.colonyContainer.get(i).isSelected()) {
				gl.glTranslatef(0.0f,0.0f,0.0f);
			}
			else if (DCEngine.colonyContainer.get(i).isAcquired()) {
				gl.glTranslatef(0.5f, 0.5f, 0.0f);
			}
			else {
				gl.glTranslatef(0.5f,0.0f,0.0f);
			}
			DCEngine.colonyContainer.get(0).draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
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
		DCEngine.updateDots();
		DCEngine.updateColonies();
		
		drawDots(gl);
		drawColonies(gl);
		//scrollBackground1(gl);
		//scrollBackground2(gl);
		
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
		WindowManager wm = (WindowManager) DCEngine.context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		DCEngine.setSize(size.x, size.y);

		// TEST CONTAINERS
		DCEngine.colonyContainer.add(new Colony(0.0f*DCEngine.X_MAX,0.0f*DCEngine.Y_MAX,0,true));
		DCEngine.colonyContainer.add(new Colony(0.07f*DCEngine.X_MAX,0.65f*DCEngine.Y_MAX,1,false));
		DCEngine.colonyContainer.add(new Colony(0.15f*DCEngine.X_MAX,0.25f*DCEngine.Y_MAX,2,false));
		DCEngine.colonyContainer.add(new Colony(0.6f*DCEngine.X_MAX,0.07f*DCEngine.Y_MAX,3,false));
		DCEngine.colonyContainer.add(new Colony(0.5f*DCEngine.X_MAX,0.5f*DCEngine.Y_MAX,4,false));
		DCEngine.colonyContainer.add(new Colony(0.4f*DCEngine.X_MAX,0.75f*DCEngine.Y_MAX,5,false));
		
		// TEST DOTS
		DCEngine.dotContainer.add(new Dot(DCEngine.colonyContainer.get(0).getCenterX(),DCEngine.colonyContainer.get(0).getCenterY(), 0));
		
		// LOAD TEXTURES
		background_layer_1.loadTexture(gl, DCEngine.BACKGROUND_LAYER, DCEngine.context);
		
		DCEngine.dotContainer.get(0).loadTexture(gl, DCEngine.DOT_IMG, DCEngine.context);
		DCEngine.colonyContainer.get(0).loadTexture(gl, DCEngine.COLONY_SPRITESHEET, DCEngine.context);
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
	}
}
