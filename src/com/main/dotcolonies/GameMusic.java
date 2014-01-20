package com.main.dotcolonies;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class GameMusic extends Service {
	
	public static boolean isRunning = false;
	MediaPlayer player;
	
	public void setMusicOptions (Context context, boolean isLooped, int rVolume, int lVolume, int file) {
		player = MediaPlayer.create(context, file);
		player.setLooping(isLooped);
		player.setVolume(rVolume,lVolume);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		setMusicOptions(this,DCEngine.LOOP_BKG_MUSIC,DCEngine.R_VOLUME,DCEngine.L_VOLUME,DCEngine.MAIN_MENU_MUSIC);
	}
	
	public int onStartCommand(Intent in, int flags, int startId) {
		try {
			player.start();
			isRunning = true;
		}
		catch (Exception e) {
			isRunning = false;
			player.stop();
		}
		return 1;
	}
	
	public void onStart(Intent in, int startId){
		
	}
	public void onStop() {
		isRunning = false;
	}
	
	public void onPause() {
		player.stop();
		player.release();
	}
	@Override
	public void onDestroy() {
		player.stop();
		player.release();
	}
	
	@Override
	public void onLowMemory() {
		player.stop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
