package com.angelo.hiniid;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.LocationException;
import com.angelo.hiniid.rest.types.SearchResult;
import com.angelo.hiniid.util.MenuUtils;
import com.angelo.hiniid.util.NotificationsUtil;
import com.angelo.hiniid.util.RemoteResourceManager;
import com.angelo.hiniid.util.TabsUtil;

/**
 * @author angelo, from VenueActivity
 */
public class HiniiSearchResultsActivity extends TabActivity {
	private static final String TAG = "HiniiSearchResultsActivity";

	private static final boolean DEBUG = Hinii.DEBUG;

	private static final int MENU_REFRESH = 1;
	private static final int MENU_VISIT_HINII = 2;
	// private static final int MENU_TIPADD = 2;

	// private static final int MENU_MYINFO = 4;

	private static final int RESULT_CODE_ACTIVITY_CHECKIN_EXECUTE = 1;

	final SearchResultsObservable searchResultsObservable = new SearchResultsObservable();
	private StateHolder mStateHolder;
	private final HashSet<Object> mProgressBarTasks = new HashSet<Object>();
	private SearchLocationObserver mSearchLocationObserver = new SearchLocationObserver();
	private ProgressDialog dialogRefreshProgress;
	private TextView mTextViewName;
	private TabHost mTabHost;
	private LinearLayout mLayoutProgressBar;
	private RemoteResourceManager mRrm;
	private String strCompany;
	private String strDaykind;
	private String strDaypart;
	private String strCity;

	private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (DEBUG)
				Log.d(TAG, "onReceive: " + intent);
			finish();
		}
	};

	private void getExtras() {
		strCompany = (String) getIntent().getExtras().get(
				HiniiSearchActivity.EXTRA_HINII_SEARCH_COMPANY);
		strDaykind = (String) getIntent().getExtras().get(
				HiniiSearchActivity.EXTRA_HINII_SEARCH_DAYKIND);
		strDaypart = (String) getIntent().getExtras().get(
				HiniiSearchActivity.EXTRA_HINII_SEARCH_DAYPART);
		strCity = (String) getIntent().getExtras().get(
				HiniiSearchActivity.EXTRA_HINII_SEARCH_CITY);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((Hiniid) getApplication())
				.requestLocationUpdates(mSearchLocationObserver);

		getExtras();
		setContentView(R.layout.hinii_search_results_activity);
		registerReceiver(mLoggedOutReceiver, new IntentFilter(
				Hiniid.INTENT_ACTION_LOGGED_OUT));
		
		// initTabHost();
		Object retained = getLastNonConfigurationInstance();
		if (retained != null && retained instanceof StateHolder) {
			mStateHolder = (StateHolder) retained;
			mStateHolder.setActivityForTask(this);
		} else {

			mStateHolder = new StateHolder();

			if (getIntent().getExtras() != null) {
				if (getIntent().getExtras().containsKey(
						Hiniid.EXTRA_SEARCH_RESULTS)) {
					SearchResult results = getIntent().getExtras()
							.getParcelable(Hiniid.EXTRA_SEARCH_RESULTS);

					mStateHolder.setResults(results);
				}

			} else {
				Log.e(TAG,
						"UserDetailsActivity requires a userid in its intent extras.");
				finish();
				return;
			}

			mStateHolder.startTask(this);
		}

		mRrm = ((Hiniid) getApplication()).getRemoteResourceManager();
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

		ensureUi();

		if (mStateHolder.getIsRunningTask() == false) {
			populateUiAfterFullUserObjectFetched();
		}
	}

	private String utilityGetValue(int items, int intNumbers, String strItem) {
		String[] numbers = getResources().getStringArray(intNumbers);
		String[] values = getResources().getStringArray(items);

		for (int i = 0; i < numbers.length; i++) {
			if (strItem.equals(numbers[i]))
				return values[i];
		}
		return "";
	}

	private void ensureUi() {

		mTextViewName = (TextView) findViewById(R.id.textViewSearchParam);

		String companyValue = utilityGetValue(
				R.array.hinii_search_company_item,
				R.array.hinii_search_company_item_values, strCompany);
		String dayKindValue = utilityGetValue(
				R.array.hinii_search_daykind_item,
				R.array.hinii_search_daykind_item_values, strDaykind);
		String dayPartValue = utilityGetValue(
				R.array.hinii_search_daypart_item,
				R.array.hinii_search_daypart_item_values, strDaypart);

		StringBuilder strHeader = new StringBuilder();
		strHeader.append(companyValue + " - ");
		strHeader.append(dayKindValue + " - ");
		strHeader.append(dayPartValue + " - ");
		strHeader.append(strCity);

		mTextViewName.setText(strHeader);
		// At startup, we need to have at least one tab. Once we load the full
		// user object,
		// we can clear all tabs, and add our real tabs once we know what they
		// are.
		mTabHost = getTabHost();
		mTabHost.addTab(mTabHost.newTabSpec("dummy").setIndicator("")
				.setContent(R.id.userDetailsActivityTextViewTabDummy));
		mTabHost.getTabWidget().setVisibility(View.GONE);

		mLayoutProgressBar = (LinearLayout) findViewById(R.id.userDetailsActivityLayoutProgressBar);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mLoggedOutReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();

		((Hiniid) getApplication())
				.requestLocationUpdates(mSearchLocationObserver);
	}

	@Override
	protected void onPause() {
		super.onPause();
		((Hiniid) getApplication())
				.removeLocationUpdates(mSearchLocationObserver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_REFRESH, 2, R.string.refresh_label).setIcon(
				R.drawable.ic_menu_refresh);
		/*
		 * menu.add(Menu.NONE, MENU_VISIT_HINII, 2,
		 * R.string.menu_view_path_on_hinii_dot_com).setIcon(
		 * R.drawable.ic_menu_refresh);
		 */
		/*
		 * int sdk = new Integer(Build.VERSION.SDK).intValue(); if (sdk < 4) {
		 * int menuIcon = UserUtils .getDrawableForMeMenuItemByGender(((Hiniid)
		 * getApplication()) .getUserGender()); menu.add(Menu.NONE, MENU_MYINFO,
		 * Menu.NONE, R.string.myinfo_label) // .setIcon(menuIcon); }
		 */

		MenuUtils.addPreferencesToMenu(this, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	private void reload() {
		if (mStateHolder.getIsRunningTask()) {
			Toast.makeText(this,
					getString(R.string.search_already_in_progress_toast),
					Toast.LENGTH_SHORT).show();
			return;
		}

		CharSequence strLoading = getResources().getString(R.string.loading);
		dialogRefreshProgress = ProgressDialog.show(
				HiniiSearchResultsActivity.this, "", strLoading, true);
		mStateHolder.startTask(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_REFRESH:
			reload();
			return true;
		case MENU_VISIT_HINII:

			Hinii hinii = ((Hiniid) getApplication()).getHinii();

			if (strCity != null) {
				startActivity(new Intent( //
						Intent.ACTION_VIEW, Uri.parse((hinii
								.getSearchResultQuery(strCompany, strDaykind,
										strDaypart, strCity)))));
			} else {
				Location location;

				try {
					location = ((Hiniid) getApplication())
							.getLastKnownLocationOrThrow();
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();

					Log.e("PIPPOLO",
							"e "
									+ hinii.getSearchResultQuery(
											strCompany, strDaykind, strDaypart,
											latitude, longitude));

					startActivity(new Intent( //
							Intent.ACTION_VIEW, Uri.parse((hinii
									.getSearchResultQuery(strCompany,
											strDaykind, strDaypart, latitude,
											longitude)))));
				} catch (LocationException e) {
					// TODO Auto-generated catch block
					Log.e("plutolo", e.toString());
					e.printStackTrace();
				}

			}

			return true;
			/*
			 * case MENU_MYINFO: Intent intentUser = new
			 * Intent(HiniiSearchResultsActivity.this,
			 * UserDetailsActivity.class);
			 * intentUser.putExtra(UserDetailsActivity.EXTRA_USER_ID, ((Hiniid)
			 * getApplication()).getUserId()); startActivity(intentUser); return
			 * true;
			 */
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public Dialog onCreateDialog(int id) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View layout;

		switch (id) {

		}
		return null;
	}

	@Override
	public void onPrepareDialog(int id, Dialog dialog) {
		// If the tip add was a success we must have set mStateHolder.tip. If
		// that is the case, then
		// we clear the dialog because clearly they're looking to add a new tip
		// and not post the
		// same one again.

	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return mStateHolder;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RESULT_CODE_ACTIVITY_CHECKIN_EXECUTE:
			if (resultCode == Activity.RESULT_OK) {
			 
			}
			break;
		}
	}

	public void startProgressBar(String taskId) {
		boolean added = mProgressBarTasks.add(taskId);
		if (!added) {
			if (DEBUG)
				Log.d(TAG, "Received start for already tracked task. Ignoring");
		}
		setProgressBarIndeterminateVisibility(true);
	}

	public void stopProgressBar(String taskId) {
		boolean removed = mProgressBarTasks.remove(taskId);
		if (!removed) {
			if (DEBUG)
				Log.d(TAG, "Received stop for untracked task. Ignoring");
		} else if (mProgressBarTasks.isEmpty()) {
			setProgressBarIndeterminateVisibility(false);
		}
	}

	/*
	 * private void initTabHost() { //final TabHost tabHost = this.getTabHost();
	 * String tag; Intent intent;
	 * 
	 * tag = (String) this.getText(R.string.pointvalue_details_tab); intent =
	 * new Intent(this, HiniiPointValueTabActivity.class);
	 * TabsUtil.addNativeLookingTab(this, mTabHost, "t2", tag,
	 * R.drawable.places_tab, intent);
	 * 
	 * tag = (String) this.getText(R.string.pointvalue_map_tab); intent = new
	 * Intent(this, HiniiPointValueMapActivity.class);
	 * TabsUtil.addNativeLookingTab(this, mTabHost, "t1", tag,
	 * R.drawable.map_tab, intent); }
	 */

	class SearchResultsObservable extends Observable {
		@Override
		public void notifyObservers(Object data) {
			setChanged();
			super.notifyObservers(data);
		}

		public SearchResult getResults() {
			return mStateHolder.results;
		}
	}

	private ViewGroup mLayoutEmpty;

	private void onTaskComplete(SearchResult searchResult, Exception ex) {
		Log.e(TAG, "onTaskComplete " + (searchResult == null));
		setProgressBarIndeterminateVisibility(false);
		mStateHolder.setFetchedResults(true);
		mStateHolder.setIsRunningTask(false);
		if (searchResult != null) {
			mStateHolder.setResults(searchResult);
			populateUiAfterFullUserObjectFetched();
			searchResultsObservable.notifyObservers(searchResult);
			if (dialogRefreshProgress != null)
				if (dialogRefreshProgress.isShowing())
					dialogRefreshProgress.hide();
		} else {
			NotificationsUtil.ToastReasonForFailure(this, ex);
			setEmptyView(mLayoutEmpty);
		}
	}

	public void setEmptyView(View view) {
		LinearLayout parent = (LinearLayout) findViewById(R.id.userDetailsActivityLayoutProgressBar);
		parent.removeAllViews();
		parent.addView(view);
	}

	private void populateUiAfterFullUserObjectFetched() {

		// User object may still be unavailable.
		SearchResult results = mStateHolder.getResults();
		Log.e(TAG, "onTaskComplete results " + (results == null));
		if (results == null) {
			return;
		}

		if (mTabHost.getTabWidget().getVisibility() != View.VISIBLE) {
			mLayoutProgressBar.setVisibility(View.GONE);

			mTabHost.clearAllTabs();
			mTabHost.getTabWidget().setVisibility(View.VISIBLE);

			String tag;
			Intent intent;
			tag = (String) this.getText(R.string.hinii_search_results_tab_list);
			intent = new Intent(this, HiniiSearchResultsTabListActivity.class);
			intent.putExtra(Hiniid.EXTRA_SEARCH_RESULTS, results);
			TabsUtil.addNativeLookingTab(this, mTabHost, "t2", tag,
					R.drawable.itinerary_tab, intent);

			tag = (String) this.getText(R.string.hinii_search_results_tab_map);
			intent = new Intent(this, HiniiSearchResultsTabMapActivity.class);
			intent.putExtra(Hiniid.EXTRA_SEARCH_RESULTS, results);
			TabsUtil.addNativeLookingTab(this, mTabHost, "t1", tag,
					R.drawable.multi_map_tab, intent);
		}
	}

	private static final class StateHolder {
		private SearchResult results = null;

		private boolean mIsRunningTask;
		private MyTask mTask;
		private boolean mFetchedResults;

		public void startTask(HiniiSearchResultsActivity activity) {
			mIsRunningTask = true;
			mTask = new MyTask(activity);
			mTask.execute();
		}

		public void setIsRunningTask(boolean isRunning) {
			mIsRunningTask = isRunning;
		}

		public boolean getIsRunningTask() {
			return mIsRunningTask;
		}

		public void setFetchedResults(boolean fetched) {
			mFetchedResults = fetched;
		}

		public boolean getFetchedResults() {
			return mFetchedResults;
		}

		/**
		 * @param randomPoint
		 *            the randomPoint to set
		 */
		public void setResults(SearchResult results) {
			this.results = results;
		}

		public void setActivityForTask(HiniiSearchResultsActivity activity) {
			if (mTask != null) {
				mTask.setActivity(activity);
			}
		}

		/**
		 * @return the SearchResult
		 */
		public SearchResult getResults() {
			return results;
		}

		public void cancelTasks() {
			if (mTask != null) {
				mTask.setActivity(null);
				mTask.cancel(true);
			}
		}

	}

	/**
	 * Even if the caller supplies us with a User object parcelable, it won't
	 * have all the badge etc extra info in it. As soon as the activity starts,
	 * we launch this task to fetch a full user object, and merge it with
	 * whatever is already supplied in mUser.
	 */
	private static class MyTask extends AsyncTask<String, Void, SearchResult> {

		private HiniiSearchResultsActivity mActivity;
		private Exception mReason;

		public MyTask(HiniiSearchResultsActivity activity) {
			mActivity = activity;

		}

		@Override
		protected void onPreExecute() {
			mActivity.setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected SearchResult doInBackground(String... params) {
			try {
				Hinii hinii = ((Hiniid) mActivity.getApplication())
						.getHinii();
				if (mActivity.strCity != null
						& (!mActivity.strCity.equals(""))
						& (!mActivity.strCity.equalsIgnoreCase(mActivity
								.getResources().getString(
										R.string.hinii_search_where_nearby)))) {
					SearchResult searchResult = hinii.getSearchResult(
							mActivity.strCompany, mActivity.strDaykind,
							mActivity.strDaypart, mActivity.strCity);
					return searchResult;
				}

				try {
					Location location = ((Hiniid) mActivity.getApplication())
							.getLastKnownLocationOrThrow();

					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					 
					SearchResult searchResult = hinii.getSearchResult(
							mActivity.strCompany, mActivity.strDaykind,
							mActivity.strDaypart, latitude, longitude);
					return searchResult;
				} catch (LocationException e) {
					// TODO Auto-generated catch block
					Log.e("LocationException", "LocationException", e);
				}

				/*
				 * return ((Hiniid) mActivity.getApplication()).getFoursquare()
				 * .getRandomPointResult(params[0]);
				 */
				/*
				 * return ((Hiniid) mActivity.getApplication()) .getFoursquare()
				 * .user( params[0], true, true, LocationUtils
				 * .createFoursquareLocation(((Hiniid) mActivity
				 * .getApplication()) .getLastKnownLocation()));
				 */
			} catch (Exception e) {
				mReason = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(SearchResult searchResult) {
			if (mActivity != null) {
				mActivity.onTaskComplete(searchResult, mReason);
			}
		}

		@Override
		protected void onCancelled() {
			if (mActivity != null) {
				mActivity.onTaskComplete(null, mReason);
			}
		}

		public void setActivity(HiniiSearchResultsActivity activity) {
			mActivity = activity;
		}
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

}
