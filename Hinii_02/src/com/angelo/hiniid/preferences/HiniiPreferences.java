package com.angelo.hiniid.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;

import com.angelo.hiniid.R;
import com.angelo.hiniid.rest.Hinii;

/**
 * @author angelo
 */
public class HiniiPreferences {
	private static final String TAG = "HiniiPreferences";
	private static final boolean DEBUG = Hinii.DEBUG;

	// Visible Preferences (sync with preferences.xml)
	public static final String PREFERENCE_SHARE_CHECKIN = "share_checkin";
	public static final String PREFERENCE_IMMEDIATE_CHECKIN = "immediate_checkin";
	public static final String PREFERENCE_STARTUP_TAB = "startup_tab";

	// Hacks for preference activity extra UI elements.

	public static final String PREFERENCE_SEND_FEEDBACK_HINII = "contact_hinii";
	public static final String PREFERENCE_SEND_FEEDBACK_AUTHOR = "contact_author";
	public static final String PREFERENCE_VISIT_HINII = "visit_hinii";

	public static final String PREFERENCE_ADVANCED_SETTINGS = "advanced_settings";
	public static final String PREFERENCE_TWITTER_CHECKIN = "twitter_checkin";
	public static final String PREFERENCE_FACEBOOK_CHECKIN = "facebook_checkin";
	public static final String PREFERENCE_FRIEND_REQUESTS = "friend_requests";
	public static final String PREFERENCE_FRIEND_ADD = "friend_add";
	public static final String PREFERENCE_CHANGELOG = "changelog";
	public static final String PREFERENCE_CITY_NAME = "city_name";
	public static final String PREFERENCE_LOGOUT = "logout";
	public static final String PREFERENCE_SEND_FEEDBACK = "send_feedback";
	public static final String PREFERENCE_PINGS = "pings_on";
	public static final String PREFERENCE_PINGS_INTERVAL = "pings_refresh_interval_in_minutes";
	public static final String PREFERENCE_PINGS_VIBRATE = "pings_vibrate";

	// Credentials related preferences
	public static final String PREFERENCE_LOGIN = "phone";
	public static final String PREFERENCE_PASSWORD = "password";

	// Extra info for getUserCity
	private static final String PREFERENCE_CITY_ID = "city_id";
	private static final String PREFERENCE_CITY_GEOLAT = "city_geolat";
	private static final String PREFERENCE_CITY_GEOLONG = "city_geolong";
	private static final String PREFERENCE_CITY_SHORTNAME = "city_shortname";

	// Extra info for getUserId
	private static final String PREFERENCE_ID = "id";

	// Extra info about the user, their gender, to control icon used for 'me' in
	// the UI.
	private static final String PREFERENCE_GENDER = "gender";

	// Extra info, can the user have followers or not.
	public static final String PREFERENCE_CAN_HAVE_FOLLOWERS = "can_have_followers";

	// Not-in-XML preferences for dumpcatcher
	public static final String PREFERENCE_DUMPCATCHER_CLIENT = "dumpcatcher_client";

	// Keeps track of the last changelog version shown to the user at startup.
	private static final String PREFERENCE_LAST_SEEN_CHANGELOG_VERSION = "last_seen_changelog_version";

	// User can choose to clear geolocation on each search.
	public static final String PREFERENCE_CACHE_GEOLOCATION_FOR_SEARCHES = "cache_geolocation_for_searches";

	// If we're compiled to show the prelaunch activity, flag stating whether to
	// skip
	// showing it on startup.
	public static final String PREFERENCE_SHOW_PRELAUNCH_ACTIVITY = "show_prelaunch_activity";

	// Last time pings service ran.
	public static final String PREFERENCE_PINGS_SERVICE_LAST_RUN_TIME = "pings_service_last_run_time";

	/**
	 * Gives us a chance to set some default preferences if this is the first
	 * install of the application.
	 */
	public static void setupDefaults(SharedPreferences preferences,
			Resources resources) {
		Editor editor = preferences.edit();
		if (!preferences.contains(PREFERENCE_STARTUP_TAB)) {
			String[] startupTabValues = resources
					.getStringArray(R.array.startup_tabs_values);
			editor.putString(PREFERENCE_STARTUP_TAB, startupTabValues[0]);
		}
		if (!preferences.contains(PREFERENCE_CACHE_GEOLOCATION_FOR_SEARCHES)) {
			editor.putBoolean(PREFERENCE_CACHE_GEOLOCATION_FOR_SEARCHES, true);
		}
		if (!preferences.contains(PREFERENCE_SHOW_PRELAUNCH_ACTIVITY)) {
			editor.putBoolean(PREFERENCE_SHOW_PRELAUNCH_ACTIVITY, true);
		}
		if (!preferences.contains(PREFERENCE_PINGS_INTERVAL)) {
			editor.putString(PREFERENCE_PINGS_INTERVAL, "30");
		}
		editor.commit();
	}

}
