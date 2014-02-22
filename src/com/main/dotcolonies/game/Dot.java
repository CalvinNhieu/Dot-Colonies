package com.main.dotcolonies.game;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Dot {
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;
	private ByteBuffer indexBuffer;
	private int[] textures = new int [1];
	private float xPos;// starting dot xpos
	private float yPos;// starting dot ypos
	private float xPos_; // post instance of dot xpos for bezier curve implementation
	private float yPos_; // post instance of dot ypos for bezier curve implementation
	private float xTarget; // dot's target (moving towards) X	
	private float yTarget; // dot's target (moving towards) Y
	private float distance;// dot's distance from target
	
	private float vertices[] = {
			0.0f,0.0f,0.0f,
			0.006f,0.0f,0.0f,
			0.006f,0.004f,0.0f,
			0.0f,0.004f,0.0f,
	};
	
	private float texture[] = {
			0.0f, 0.0f,
			1f, 0.0f,
			1f,1f,
			0.0f,1f,
	};
	
	private byte indices[] = {
		0,1,2,
		0,2,3,
	};
	
	public Dot (float xPos, float yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		xPos_ = xPos;
		yPos_ = yPos;
		xTarget = xPos;
		yTarget = yPos;
		distance = 0;
		
		ByteBuffer byteBuff = ByteBuffer.allocateDirect(vertices.length*4);
		byteBuff.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuff.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		byteBuff = ByteBuffer.allocateDirect(texture.length*4);
		byteBuff.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuff.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0); 
		
		indexBuffer = ByteBuffer.allocateDirect(indices.length);
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
	public void draw(GL10 gl) {
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_BYTE, indexBuffer);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_CULL_FACE);
	}

	public void loadTexture(GL10 gl, int texture, Context context) {
		InputStream imagestream = context.getResources().openRawResource(texture);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(imagestream);
		}
		catch (Exception e) {
		}
		finally {
			try {
				imagestream.close();
				imagestream = null;
			} 
			catch (IOException e) {
				
			}
		}
		gl.glGenTextures(1,textures,0);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
		
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		bitmap.recycle();
	}

	public FloatBuffer getVertexBuffer() {
		return vertexBuffer;
	}

	public void setVertexBuffer(FloatBuffer vertexBuffer) {
		this.vertexBuffer = vertexBuffer;
	}

	public FloatBuffer getTextureBuffer() {
		return textureBuffer;
	}

	public void setTextureBuffer(FloatBuffer textureBuffer) {
		this.textureBuffer = textureBuffer;
	}

	public ByteBuffer getIndexBuffer() {
		return indexBuffer;
	}

	public void setIndexBuffer(ByteBuffer indexBuffer) {
		this.indexBuffer = indexBuffer;
	}

	public int[] getTextures() {
		return textures;
	}

	public void setTextures(int[] textures) {
		this.textures = textures;
	}

	public float getxPos() {
		return xPos;
	}

	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	public float getyPos() {
		return yPos;
	}

	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

	public float getxPos_() {
		return xPos_;
	}

	public void setxPos_(float xPos_) {
		this.xPos_ = xPos_;
	}

	public float getyPos_() {
		return yPos_;
	}

	public void setyPos_(float yPos_) {
		this.yPos_ = yPos_;
	}

	public float getxTarget() {
		return xTarget;
	}

	public void setxTarget(float xTarget) {
		this.xTarget = xTarget;
	}

	public float getyTarget() {
		return yTarget;
	}

	public void setyTarget(float yTarget) {
		this.yTarget = yTarget;
	}

	public float[] getVertices() {
		return vertices;
	}

	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}

	public float[] getTexture() {
		return texture;
	}

	public void setTexture(float[] texture) {
		this.texture = texture;
	}

	public byte[] getIndices() {
		return indices;
	}

	public void setIndices(byte[] indices) {
		this.indices = indices;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	
}


