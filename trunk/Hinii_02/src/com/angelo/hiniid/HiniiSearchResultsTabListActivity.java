package com.angelo.hiniid;

import java.util.Observable;
import java.util.Observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.angelo.hiniid.app.LoadableListActivityWithView;
import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.types.Group;
import com.angelo.hiniid.rest.types.PointValue;
import com.angelo.hiniid.rest.types.SearchResult;
import com.angelo.hiniid.util.CheckinTimestampSort;
import com.angelo.hiniid.widget.PointValueListAdapter;
import com.angelo.hiniid.widget.SeparatedListAdapter;

/**
 * @author angelo
 */
public class HiniiSearchResultsTabListActivity extends
		LoadableListActivityWithView {
	static final String TAG = "HiniiSearchResultsTabListActivity";
	static final boolean DEBUG = Hinii.DEBUG;
	
	public static final String QUERY_NEARBY = null;

	private SeparatedListAdapter mListAdapter;
	private SearchLocationObserver mSearchLocationObserver = new SearchLocationObserver();
	private SearchResultsObserver searchResultsObserver = new SearchResultsObserver();

	private ViewGroup mLayoutEmpty;

	private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (DEBUG)
				Log.d(TAG, "onReceive: " + intent);
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registerReceiver(mLoggedOutReceiver, new IntentFilter(
				Hiniid.INTENT_ACTION_LOGGED_OUT));

		initListViewAdapter();
		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras()
					.containsKey(Hiniid.EXTRA_SEARCH_RESULTS)) {
				SearchResult searchResult = getIntent().getExtras()
						.getParcelable(Hiniid.EXTRA_SEARCH_RESULTS);
				putSearchResultsInAdapter(searchResult);
			}
		}
		((HiniiSearchResultsActivity) getParent()).searchResultsObservable
				.addObserver(searchResultsObserver);
	}

	@Override
	public void onResume() {
		super.onResume();

		((Hiniid) getApplication())
				.requestLocationUpdates(mSearchLocationObserver);
	}

	@Override
	public void onPause() {
		super.onPause();

		((Hiniid) getApplication())
				.removeLocationUpdates(mSearchLocationObserver);

		if (isFinishing()) {
			mListAdapter.removeObserver();
			unregisterReceiver(mLoggedOutReceiver);
		}
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	private void initListViewAdapter() {
		mListAdapter = new SeparatedListAdapter(this);

		ListView listView = getListView();
		listView.setAdapter(mListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PointValue pointValue = (PointValue) parent.getAdapter()
						.getItem(position);
				startItemActivity(pointValue);
				/*
				 * TODO angelo properly fire intent if (checkin.getUser() !=
				 * null) { Intent intent = new
				 * Intent(PointValueListActivity.this,
				 * UserDetailsActivity.class);
				 * intent.putExtra(UserDetailsActivity.EXTRA_USER_PARCEL,
				 * checkin.getUser()); intent.putExtra(
				 * UserDetailsActivity.EXTRA_SHOW_ADD_FRIEND_OPTIONS,true);
				 * startActivity(intent); }
				 */
			}
		});

		listView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View arg0) {
				return false;
			}
		});

		// Prepare our no-results view. Something odd is going on with the
		// layout parameters though.
		// If we don't explicitly set the layout to be fill/fill after
		// inflating, the layout jumps
		// to a wrap/wrap layout. Furthermore, sdk 3 crashes with the original
		// layout using two
		// buttons in a horizontal LinearLayout.
		int sdk = new Integer(Build.VERSION.SDK).intValue();
		if (sdk > 3) {
			mLayoutEmpty = (ScrollView) LayoutInflater.from(this).inflate(
					R.layout.hinii_places_activity_empty, null);
		} else {
			mLayoutEmpty = (ScrollView) LayoutInflater.from(this).inflate(
					R.layout.hinii_places_activity_empty, null);
		}

		mLayoutEmpty.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));

	}

	void startItemActivity(PointValue pointValue) {
		Intent intent = new Intent(HiniiSearchResultsTabListActivity.this,
				HiniiPointValueActivity2.class);
		intent.setAction(Intent.ACTION_VIEW);
		intent.putExtra(Hiniid.EXTRA_POINT_VALUE, pointValue);
		startActivity(intent);
	}

	/**
	 * Sort checkin results first by distance [same city | different city], then
	 * within the [same city] bucket, sort by last three hours, today, and
	 * yesterday. If we had no geoloation at the time of the search, we won't
	 * have any distance parameter to do the first level of sorting, in this
	 * case we just place all our friends in the [same city] bucket.
	 */
	private void putSearchResultsInAdapter(SearchResult searchResult) {

		/*
		 * 0 MORNING 1 AFTERNOON 2 NIGHT 20 SPECIAL_ALLDAY
		 */

		Group<PointValue> pointValues = searchResult.getIter();

		Group<PointValue> dayPartMorning = new Group<PointValue>();
		Group<PointValue> dayPartAfternoon = new Group<PointValue>();
		Group<PointValue> dayPartNight = new Group<PointValue>();
		Group<PointValue> dayPartAllDay = new Group<PointValue>();

		if (pointValues != null && pointValues.size() > 0) {

			CheckinTimestampSort timestamps = new CheckinTimestampSort();
			for (PointValue it : pointValues) {

				// If we can't parse the distance value, it's possible that we
				// did not have a geolocation for the device at the time the
				// search was run. In this case just assume this friend is
				// nearby
				// to sort them in the time buckets.
				int meters = 30000;
				try {
					// TODO angelo calculate distance
					// meters = Integer.parseInt(it.getDistance());
				} catch (NumberFormatException ex) {
					if (DEBUG)
						Log.d(TAG,
								"Couldn't parse distance for checkin during friend search.");
					meters = 0;
				}

				try {
					int dayPart = Integer.parseInt(it.getDayPart());

					if (dayPart == 0) {
						dayPartMorning.add(it);
					} else if (dayPart == 1) {
						dayPartAfternoon.add(it);
					} else if (dayPart == 2) {
						dayPartNight.add(it);
					} else // if(dayPart==20)
					{
						dayPartAllDay.add(it);
					}
				} catch (Exception ex) {
					dayPartAllDay.add(it);
				}

			}
		}

		mListAdapter.removeObserver();
		mListAdapter.clear();
		mListAdapter = new SeparatedListAdapter(this);
		if (dayPartMorning.size() > 0) {
			PointValueListAdapter adapter = new PointValueListAdapter(this,
					((Hiniid) getApplication()).getRemoteResourceManager());
			adapter.setGroup(dayPartMorning);
			mListAdapter.addSection(
					getResources().getString(
							R.string.pointvalue_title_sort_morning), adapter);
		}
		if (dayPartAfternoon.size() > 0) {
			PointValueListAdapter adapter = new PointValueListAdapter(this,
					((Hiniid) getApplication()).getRemoteResourceManager());
			adapter.setGroup(dayPartAfternoon);
			mListAdapter.addSection(
					getResources().getString(
							R.string.pointvalue_title_sort_afternoon), adapter);
		}
		if (dayPartNight.size() > 0) {
			PointValueListAdapter adapter = new PointValueListAdapter(this,
					((Hiniid) getApplication()).getRemoteResourceManager());
			adapter.setGroup(dayPartNight);
			mListAdapter.addSection(
					getResources().getString(
							R.string.pointvalue_title_sort_night), adapter);
		}
		if (dayPartAllDay.size() > 0) {
			PointValueListAdapter adapter = new PointValueListAdapter(this,
					((Hiniid) getApplication()).getRemoteResourceManager());
			adapter.setGroup(dayPartAllDay);
			mListAdapter.addSection(
					getResources().getString(
							R.string.pointvalue_title_sort_allday), adapter);
		}
		/*
		 * if (dayPartAllDay.size() > 0) { PointValueListAdapter adapter = new
		 * PointValueListAdapter(this, ((Foursquared)
		 * getApplication()).getRemoteResourceManager());
		 * adapter.setGroup(dayPartAllDay);
		 * mListAdapter.addSection(getResources().getString(
		 * R.string.friendsactivity_title_sort_other_city), adapter); }
		 */

		getListView().setAdapter(mListAdapter);
	}

	/**
	 * This is really just a dummy observer to get the GPS running since this is
	 * the new splash page. After getting a fix, we might want to stop
	 * registering this observer thereafter so it doesn't annoy the user too
	 * much.
	 */
	private class SearchLocationObserver implements Observer {
		@Override
		public void update(Observable observable, Object data) {
		}
	}

	private class SearchResultsObserver implements Observer {
		@Override
		public void update(Observable observable, Object data) {

			putSearchResultsInAdapter((SearchResult) data);
		}
	}
}
