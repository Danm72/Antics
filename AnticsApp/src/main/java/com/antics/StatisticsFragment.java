package com.antics;

import com.antics.util.ConfigFile;
import com.antics.util.FrameRateConfig;
import com.antics.util.Music;
import com.antics.util.MusicConfig;
import com.googlecode.androidannotations.annotations.EActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StatisticsFragment extends ListFragment {
	int gatherer = 0;
	int builder = 0;
	int soldier = 0;
	Runnable statsThread = null;

	private Handler handler = new Handler();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ConfigFile.setAdapter(new SampleAdapter(getActivity()));

		setListAdapter(ConfigFile.getAdapter());

		statsThread = new Runnable() {

			@Override
			public void run() {
				ConfigFile.adapter.clear();
				populateArray();
				ConfigFile.adapter.notifyDataSetChanged();

				handler.postDelayed(statsThread, 500);
			}
		};

		handler.post(statsThread);
	}

	private class SampleItem {
		public String tag;
		public String detail;

		// public int iconRes;
		public SampleItem(String tag, String detail) {
			this.tag = tag;
			this.detail = detail;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.row, null);
			}
			TextView detail = (TextView) convertView
					.findViewById(R.id.row_detail);
			detail.setText(getItem(position).detail);
			final TextView title = (TextView) convertView
					.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("Stats", "Click" + title.getText().toString());
					String word = title.getText().toString();
					if (word.contains("Music")
							&& MusicConfig.muteMusic == false) {
						MusicConfig.muteMusic = true;
						Music.pause();
						Log.d("Stats", "Click" + MusicConfig.muteMusic);
					} else if (word.contains("Music")
							&& MusicConfig.muteMusic == true) {
						MusicConfig.muteMusic = false;
						Music.start(getActivity(), 1);
					}

					if (word.contains("Reset"))
						ConfigFile.resetAntLocation();
					if (word.contains("Increase") && ConfigFile.drawSpeed > 0)
						ConfigFile.drawSpeed -= 200 ;
					if (word.contains("Decrease") && ConfigFile.drawSpeed < 1000)
						ConfigFile.drawSpeed += 200 ;

				}
			});

			return convertView;
		}
	}

	private void populateArray() {
		ConfigFile.board.countAnts();
		ConfigFile.adapter.add(new SampleItem("", " Hive Statistics"));
		ConfigFile.adapter.add(new SampleItem("", ""));
		ConfigFile.adapter.add(new SampleItem("Food",
				ConfigFile.board.tiles[ConfigFile.NEST_X][ConfigFile.NEST_Y].food()
						+ ""));
		ConfigFile.adapter.add(new SampleItem("Ants", ConfigFile.ants.size() + ""));
		ConfigFile.adapter.add(new SampleItem("Gatherers",
				ConfigFile.board.gatherer + ""));
		ConfigFile.adapter.add(new SampleItem("Builders", ConfigFile.board.builder
				+ ""));
		ConfigFile.adapter.add(new SampleItem("Soldiers", ConfigFile.board.soldier
				+ ""));
		ConfigFile.adapter.add(new SampleItem("Total Ant", ConfigFile.board.ants
				+ ""));
		ConfigFile.adapter.add(new SampleItem("Dead Ants", ConfigFile.board.dead
				+ ""));

		ConfigFile.adapter.add(new SampleItem("", " "));

		ConfigFile.adapter.add(new SampleItem("", " Config info"));
		ConfigFile.adapter.add(new SampleItem("", " "));
		ConfigFile.adapter.add(new SampleItem("Reset Ants locations", ""));

		ConfigFile.adapter.add(new SampleItem("Music muted : ",
				MusicConfig.muteMusic + ""));
		ConfigFile.adapter.add(new SampleItem("Draw Time Estimate : ",
				FrameRateConfig.dt + ""));
		ConfigFile.adapter.add(new SampleItem("Current Speed", ConfigFile.drawSpeed
				+ ""));
		ConfigFile.adapter.add(new SampleItem("Increase Speed", ""));
		ConfigFile.adapter.add(new SampleItem("Decrease Speed", ""));
	}

}
