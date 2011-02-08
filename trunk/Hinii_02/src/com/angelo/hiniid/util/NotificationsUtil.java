package com.angelo.hiniid.util;

import com.angelo.hiniid.R;
import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.HiniiException;
import com.angelo.hiniid.rest.error.LocationException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * @author Angelo Schiavone
 */
public class NotificationsUtil {
	private static final String TAG = "NotificationsUtil";
	private static final boolean DEBUG = Hinii.DEBUG;

	public static void ToastReasonForFailure(Context context, Exception e) {
		if (DEBUG)
			Log.d(TAG, "Toasting for exception: ", e);

		if (e instanceof SocketTimeoutException) {
			Toast.makeText(context, R.string.hinii_server_request_timed_out,
					Toast.LENGTH_SHORT).show();

		} else if (e instanceof SocketException) {
			Toast.makeText(context, R.string.hinii_server_not_responding,
					Toast.LENGTH_SHORT).show();

		} else if (e instanceof IOException) {
			Toast.makeText(context, R.string.network_unavailable,
					Toast.LENGTH_SHORT).show();

		} else if (e instanceof LocationException) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

		} else if (e instanceof HiniiException) {
			// FoursquareError is one of these
			String message;
			int toastLength = Toast.LENGTH_SHORT;
			if (e.getMessage() == null) {
				message = "Invalid Request";
			} else {
				message = e.getMessage();
				toastLength = Toast.LENGTH_LONG;
			}
			Toast.makeText(context, message, toastLength).show();

		} else {
			Toast.makeText(context,
					"A surprising new problem has occured. Try again!",
					Toast.LENGTH_SHORT).show();
			
		}
	}
}
