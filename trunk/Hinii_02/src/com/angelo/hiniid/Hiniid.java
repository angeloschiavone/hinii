package com.angelo.hiniid;

import java.io.File;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.angelo.hiniid.location.BestLocationListener;
import com.angelo.hiniid.preferences.HiniiPreferences;
import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.LocationException;
import com.angelo.hiniid.util.NullDiskCache;
import com.angelo.hiniid.util.RemoteResourceManager;

/**
 * @author Angelo Schiavone 
 */
public class Hiniid extends Application {
	private static final String TAG = "Hiniid";
	private static final boolean DEBUG = Hinii.DEBUG;
	static {
		Logger.getLogger("com.angelo.hinii").setLevel(Level.ALL);
	}

	public static final String PACKAGE_NAME = "com.angelo.hiniid";
	public static final String INTENT_ACTION_LOGGED_OUT = "com.angelo.hiniid.intent.action.LOGGED_OUT";
	public static final String INTENT_ACTION_LOGGED_IN = "com.angelo.hiniid.intent.action.LOGGED_IN";
	public static final String EXTRA_VENUE_ID = "com.angelo.hiniid.VENUE_ID";

	public static final String EXTRA_POINT_VALUE = "com.angelo.hiniid.POINT_VALUE";
	public static final String EXTRA_SEARCH_RESULTS = "com.angelo.hiniid.SEARCH_RESULTS";
	public static final String EXTRA_RANDOM_POINT_RESULT = "com.angelo.hiniid.RANDOM_POINT_RESULT";
	public static final String EXTRA_POINT_ID = "com.angelo.hiniid.POINT_ID";

	private String mVersion = null;

	private TaskHandler mTaskHandler;
	private HandlerThread mTaskThread;

	private SharedPreferences mPrefs;
	private RemoteResourceManager mRemoteResourceManager;

	private Hinii myHinii;

	private BestLocationListener mBestLocationListener = new BestLocationListener();

	private boolean mIsFirstRun;

	@Override
	public void onCreate() {
		Log.i(TAG, "Using Debug Log:\t" + DEBUG);

		mVersion = getVersionString(this);

		// Check if this is a new install by seeing if our preference file
		// exists on disk.
		mIsFirstRun = checkIfIsFirstRun();

		// Setup Prefs (to load dumpcatcher)
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		// Setup some defaults in our preferences if not set yet.
		HiniiPreferences.setupDefaults(mPrefs, getResources());

		mTaskThread = new HandlerThread(TAG + "-AsyncThread");
		mTaskThread.start();
		mTaskHandler = new TaskHandler(mTaskThread.getLooper());
		loadResourceManagers();
		new MediaCardStateBroadcastReceiver().register();
		myHinii = new Hinii(Hinii.createHttpApi(mVersion,
				false));
	}
	/**
	 * Set up resource managers on the application depending on SD card state.
	 * 
	 * @author Joe LaPenna (joe@joelapenna.com)
	 */
	private class MediaCardStateBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (DEBUG)
				Log.d(TAG, "Media state changed, reloading resource managers:"
						+ intent.getAction());
			if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
				getRemoteResourceManager().shutdown();
				loadResourceManagers();
			} else if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
				loadResourceManagers();
			}
		}

		public void register() {
			// Register our media card broadcast receiver so we can
			// enable/disable the cache as
			// appropriate.
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
			intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
			// intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
			// intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);
			// intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
			// intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
			// intentFilter.addAction(Intent.ACTION_MEDIA_NOFS);
			// intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
			// intentFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
			intentFilter.addDataScheme("file");
			registerReceiver(this, intentFilter);
		}
	}

	public Hinii getHinii() {
		return myHinii;
	}

	public String getVersion() {

		if (mVersion != null) {
			return mVersion;
		} else {
			return "";
		}
	}

	public RemoteResourceManager getRemoteResourceManager() {
		return mRemoteResourceManager;
	}

	public BestLocationListener requestLocationUpdates(boolean gps) {
		mBestLocationListener.register(
				(LocationManager) getSystemService(Context.LOCATION_SERVICE),
				gps);
		return mBestLocationListener;
	}

	public BestLocationListener requestLocationUpdates(Observer observer) {
		mBestLocationListener.addObserver(observer);
		mBestLocationListener.register(
				(LocationManager) getSystemService(Context.LOCATION_SERVICE),
				true);
		return mBestLocationListener;
	}
	
	
	private void loadResourceManagers() {
		// We probably don't have SD card access if we get an
		// IllegalStateException. If it did, lets
		// at least have some sort of disk cache so that things don't npe when
		// trying to access the
		// resource managers.
		try {
			if (DEBUG)
				Log.d(TAG, "Attempting to load RemoteResourceManager(cache)");
			mRemoteResourceManager = new RemoteResourceManager("cache");
		} catch (IllegalStateException e) {
			if (DEBUG)
				Log.e(TAG, "Falling back to NullDiskCache for RemoteResourceManager");
			mRemoteResourceManager = new RemoteResourceManager(
					new NullDiskCache());
		}
	}
	
	public void removeLocationUpdates() {
		mBestLocationListener
				.unregister((LocationManager) getSystemService(Context.LOCATION_SERVICE));
	}

	public void removeLocationUpdates(Observer observer) {
		mBestLocationListener.deleteObserver(observer);
		this.removeLocationUpdates();
	}

	public Location getLastKnownLocation() {
		return mBestLocationListener.getLastKnownLocation();
	}

	public Location getLastKnownLocationOrThrow() throws LocationException {
		Location location = mBestLocationListener.getLastKnownLocation();
		/*
		 * if (location == null) { throw new LocationException(); }
		 */
		return location;
	}

	public void clearLastKnownLocation() {
		mBestLocationListener.clearLastKnownLocation();
	}

	public void requestStartService() {
		mTaskHandler.sendMessage( //
				mTaskHandler.obtainMessage(TaskHandler.MESSAGE_START_SERVICE));
	}

	public void requestUpdateUser() {
		mTaskHandler.sendEmptyMessage(TaskHandler.MESSAGE_UPDATE_USER);
	}

	/**
	 * Constructs the version string of the application.
	 * 
	 * @param context
	 *            the context to use for getting package info
	 * @return the versions string of the application
	 */
	private static String getVersionString(Context context) {
		// Get a version string for the app.
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(PACKAGE_NAME, 0);
			return PACKAGE_NAME + ":" + String.valueOf(pi.versionCode);
		} catch (NameNotFoundException e) {
			if (DEBUG)
				Log.d(TAG, "Could not retrieve package info", e);
			throw new RuntimeException(e);
		}
	}

	public boolean getIsFirstRun() {
		return mIsFirstRun;
	}

	private boolean checkIfIsFirstRun() {
		File file = new File(
				"/data/data/com.angelo.hiniid/shared_prefs/com.angelo.hinii_preferences.xml");
		return !file.exists();
	}

	private class TaskHandler extends Handler {

		private static final int MESSAGE_UPDATE_USER = 1;
		private static final int MESSAGE_START_SERVICE = 2;

		public TaskHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (DEBUG)
				Log.d(TAG, "handleMessage: " + msg.what);
		}
	}
}
