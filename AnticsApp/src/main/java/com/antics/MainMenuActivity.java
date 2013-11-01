package com.antics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.antics.util.ConfigFile;
import com.antics.util.Music;
import com.antics.util.MusicConfig;

public class MainMenuActivity extends Activity {
	private Boolean changed = false;
	private Boolean continueMusic = false;
	private int MUSIC_MENU = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainmenu);

		Button enter = (Button) findViewById(R.id.EnterButton);
		Button exit = (Button) findViewById(R.id.ExitButton);

		if(!MusicConfig.muteMusic)
			Music.start(this, MUSIC_MENU);
		enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent simulator = new Intent(getApplicationContext(),
						SimulatorFragment.class);
				MainMenuActivity.this.startActivity(simulator);
			}
		});

		exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean clean = false;
				clean = ConfigFile.onExit();

				if (clean && MusicConfig.muteMusic) {
					int pid = android.os.Process.myPid();
					android.os.Process.killProcess(pid);
				}
				if (MusicConfig.muteMusic == false) {
					int pid = android.os.Process.myPid();
					android.os.Process.killProcess(pid);
				}

			}
		});
	}

	@Override
	protected void onStop() {
		if (!changed) {
			ConfigFile.onExit();
			super.onStop();
		}
	}

	@Override
	protected void onDestroy() {
		ConfigFile.onExit();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
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
