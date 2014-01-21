package com.main.dotcolonies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import com.main.dotcolonies.game.Game;

public class MainActivity extends Activity {
	
	// declare buttons on main menu
	private ImageButton play;
	private ImageButton exit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Loads music (initializes media player and sets default settings... volume, sound file)
		DCEngine.musicThread = new Thread() {
			@Override
			public void run (){
				Intent playBkgMusic = new Intent (getApplicationContext(), GameMusic.class);
				startService(playBkgMusic);
				DCEngine.context = getApplicationContext();
			}
		};
		// Thread code is defined above. Thread code is executed with start();
		DCEngine.musicThread.start();
		
		// initialize play and exit buttons
		play = (ImageButton) findViewById(R.id.play_button);
		exit = (ImageButton) findViewById(R.id.exit_button);
		
		// Set transparency and no vibrate on click
		play.getBackground().setAlpha(DCEngine.MENU_BUTTON_ALPHA);
		play.setHapticFeedbackEnabled(DCEngine.HAPTIC_BUTTON_FEEDBACK);
		
		exit.getBackground().setAlpha(DCEngine.MENU_BUTTON_ALPHA);
		exit.setHapticFeedbackEnabled(DCEngine.HAPTIC_BUTTON_FEEDBACK);
		
		// to move to game state
		play.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent gameStart = new Intent (MainActivity.this, Game.class);
				startActivity(gameStart);
				MainActivity.this.finish();
			}
		});
		
		// to exit app
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	@Override
	public void onPause () {
		super.onPause();
		// pause music if activity pauses 
		Intent playBkgMusic = new Intent (getApplicationContext(), GameMusic.class);
		stopService(playBkgMusic);
	}
	
	@Override
	public void onResume () {
		super.onResume();
		// resume music
		Intent playBkgMusic = new Intent (getApplicationContext(), GameMusic.class);
		startService(playBkgMusic);
	}
}
