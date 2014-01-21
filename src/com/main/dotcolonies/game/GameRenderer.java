package com.main.dotcolonies.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.main.dotcolonies.DCEngine;

import android.opengl.GLSurfaceView.Renderer;

public class GameRenderer implements Renderer{
	
	private GameBackground background = new GameBackground();
	
	// constantly called
	@Override
	public void onDrawFrame(GL10 gl) {
		background.draw(gl);
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
		
		//load and draw background texture
		background.loadTexture(gl, DCEngine.BACKGROUND_LAYER, DCEngine.context);
	}

}
