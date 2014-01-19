package com.main.dotcolonies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity {
	
	Button play;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		play = (Button) findViewById(R.id.play_button);
		
		play.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				setFragment(new GameFragment());
				play.setVisibility(View.GONE);
			}
		});
	}
	
	public void setFragment(Fragment f) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.container,f);
		ft.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
}
