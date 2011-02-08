package com.angelo.hiniid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.angelo.hiniid.preferences.HiniiPreferences;
import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.util.FeedbackUtils;

/**
 * @author angelo
 */
public class HiniiPreferenceActivity extends
		android.preference.PreferenceActivity {
	private static final String TAG = "HiniiPreferenceActivity";

	private static final boolean DEBUG = Hinii.DEBUG;

	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.preferences_hinii);
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (DEBUG)
			Log.d(TAG, "onPreferenceTreeClick");

		String key = preference.getKey();
		if (HiniiPreferences.PREFERENCE_SEND_FEEDBACK_HINII.equals(key)) {
			FeedbackUtils.sendFeedBackHinii(this, (Hiniid) getApplication());
		} else if (HiniiPreferences.PREFERENCE_VISIT_HINII.equals(key)) {
			startActivity(new Intent( //
					Intent.ACTION_VIEW, Uri.parse(Hinii.HINII_PREFERENCES)));

		} else if (HiniiPreferences.PREFERENCE_SEND_FEEDBACK_AUTHOR.equals(key)) {
			FeedbackUtils.sendFeedBackAuthor(this, (Hiniid) getApplication());
		} else if (HiniiPreferences.PREFERENCE_CHANGELOG.equals(key)) {
			startActivity(new Intent(this, ChangelogActivity.class));
		}
		return true;
	}
}
