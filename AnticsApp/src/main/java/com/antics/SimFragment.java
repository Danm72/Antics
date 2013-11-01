package com.antics;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SimFragment extends Fragment {

	private GLSurfaceView glSurfaceView;
	private boolean rendererSet = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle state) {
		glSurfaceView = new GLSurfaceView(this.getActivity());
		// View view = inflater.inflate(R.layout.fragment_test,
		// container,false);
		// TODO Auto-generated method stub

		ActivityManager activityManager = (ActivityManager) this.getActivity()
				.getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo configurationInfo = activityManager
				.getDeviceConfigurationInfo();
		// Even though the latest emulator supports OpenGL ES 2.0,
		// it has a bug where it doesn't set the reqGlEsVersion so
		// the above check doesn't work. The below will detect if the
		// app is running on an emulator, and assume that it supports
		// OpenGL ES 2.0.
		final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000
				|| (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 && (Build.FINGERPRINT
						.startsWith("generic")
						|| Build.MODEL.contains("google_sdk") || Build.MODEL
							.contains("Emulator")));

		if (supportsEs2) {
			// Request an OpenGL ES 2.0 compatible context.
			glSurfaceView.setEGLContextClientVersion(2);

			// Assign our renderer.

			glSurfaceView.setRenderer(new SimulationRenderer(this.getActivity()));
			rendererSet = true;
		} else {
			Log.d("OPENGL", "DOES NOT SUPPORT V2");
			Toast.makeText(this.getActivity(),
					"This device does not support OpenGL ES 2.0.",
					Toast.LENGTH_LONG).show();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int pid = android.os.Process.myPid();
			android.os.Process.killProcess(pid);
		}
		return glSurfaceView;
	}

	@Override
	public void onPause() {
		super.onPause();

		if (rendererSet) {
			glSurfaceView.onPause();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (rendererSet) {
			glSurfaceView.onResume();
		}
	}

}
