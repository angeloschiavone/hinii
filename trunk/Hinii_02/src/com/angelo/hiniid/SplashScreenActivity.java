package com.angelo.hiniid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

public class SplashScreenActivity extends Activity {

	protected boolean _active = true;
	protected int _splashTime = 5000; // time to display the splash screen in ms

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_activity);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					finish();
					startActivity(new Intent(
							"com.angelo.hiniid.HiniiSearchActivity"));
					// stop();

				}
			}
		};
		splashTread.start();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("Splash ACTION_DOWN", "Action=" + event.getAction());
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
			Log.e("Splash ACTION_DOWN", "ACTION_DOWN");
		}
		return true;
	}
}