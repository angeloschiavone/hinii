/**
 * Copyright 2009 Joe LaPenna
 */

package com.angelo.hiniid.maps;

import com.angelo.hiniid.rest.Hinii; 
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

/**
 * See: <a href=
 * "http://groups.google.com/group/android-developers/browse_thread/thread/43615742f462bbf1/8918ddfc92808c42?"
 * >MyLocationOverlay causing crash in 1.6 (Donut)</a>
 * 
 * @author Joe LaPenna (joe@joelapenna.com)
 */
public class CrashFixMyLocationOverlay extends MyLocationOverlay {
	static final String TAG = "CrashFixMyLocationOverlay";
	static final boolean DEBUG = Hinii.DEBUG;

	public CrashFixMyLocationOverlay(Context context, MapView mapView) {
		super(context, mapView);
	}

	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		try {
			return super.draw(canvas, mapView, shadow, when);
		} catch (ClassCastException e) {
			if (DEBUG)
				Log.d(TAG, "Encountered overlay crash bug", e);
	
			return false;
		}
	}

}
