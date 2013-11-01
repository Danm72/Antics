package com.antics;

import com.antics.objects.Hexagon;
import com.antics.util.ConfigFile;
import com.antics.util.Music;
import com.antics.util.MusicConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class AntSimulatorActivity extends Activity {
	private int MUSIC_START = 0;
	private Boolean continueMusic = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splashscreen);
		if(!MusicConfig.muteMusic)
			Music.start(this, MUSIC_START);
		

		ConfigFile.context = this;
		new Handler().postDelayed(new Thread(){
			@Override
			
			public void run(){
				
				Intent mainMenu = new Intent(AntSimulatorActivity.this,MainMenuActivity.class);
				AntSimulatorActivity.this.startActivity(mainMenu);
				AntSimulatorActivity.this.finish();
				//override pending transitions
			}
			
		}, ConfigFile.GAME_THREAD_DELAY);
		
		Thread makeBoard = new Thread(){
			@Override
			
			public void run(){
				Hexagon hexagon = new Hexagon(0.06455f / ConfigFile.scale, 0.0f, 6);
				ConfigFile.board.createMap(1.5f, 1.5f,hexagon);
				Log.d("ANTSIM", "Thread has run");
			}
			
		};
		makeBoard.run();
		
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (!continueMusic) {
			Music.pause();
			}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		continueMusic = false;
		Music.start(this, Music.MUSIC_MENU);
	}
	

}
