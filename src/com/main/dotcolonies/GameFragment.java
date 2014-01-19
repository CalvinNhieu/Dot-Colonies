package com.main.dotcolonies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameFragment extends Fragment{
	
	private GameLoop loop;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_fragment, container, false);
        loop = new GameLoop(this.getView());
        loop.setGameState(true);
        return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		loop.start();
	}

	@Override
	public void onPause() {
        try {
			loop.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		
	}
	
	public void render() {
	}
}
