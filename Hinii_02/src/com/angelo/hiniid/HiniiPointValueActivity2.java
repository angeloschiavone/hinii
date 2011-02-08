package com.angelo.hiniid;

import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.types.HiniiRandomPointResult;
import com.angelo.hiniid.rest.types.PointValue;
import com.angelo.hiniid.util.MenuUtils;
import com.angelo.hiniid.util.NotificationsUtil;
import com.angelo.hiniid.util.RemoteResourceManager;
import com.angelo.hiniid.util.TabsUtil;

/**
 * @author angelo, from VenueActivity
 */
public class HiniiPointValueActivity2 extends TabActivity {
	private static final String TAG = "HiniiPointValueActivity2";

	private static final boolean DEBUG = Hinii.DEBUG;
	private boolean mIsUsersPhotoSet;

	private static final int RESULT_CODE_ACTIVITY_CHECKIN_EXECUTE = 1;

	final PointValueObservable pointValueObservable = new PointValueObservable();
	private StateHolder mStateHolder;
	private final HashSet<Object> mProgressBarTasks = new HashSet<Object>();

	private ImageView mImageViewPhoto;
	private TextView mTextViewName;
	private LinearLayout mLayoutVote;
	private LinearLayout mLayoutCost;
	private TextView mTextViewCost;
	private TextView mTextViewVoteAvg;
	private TabHost mTabHost;
	private LinearLayout mLayoutProgressBar;

	private RemoteResourceManager mRrm;
	private RemoteResourceManagerObserver mResourcesObserver;
	private boolean mCheckedInSuccessfully = false;

	private BroadcastReceiver mLoggedOutReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (DEBUG)
				Log.d(TAG, "onReceive: " + intent);
			finish();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hinii_point_value_activity);
		registerReceiver(mLoggedOutReceiver, new IntentFilter(
				Hiniid.INTENT_ACTION_LOGGED_OUT));
		mIsUsersPhotoSet = false;
		// initTabHost();
		Object retained = getLastNonConfigurationInstance();
		if (retained != null && retained instanceof StateHolder) {
			mStateHolder = (StateHolder) retained;
			mStateHolder.setActivityForTaskUserDetails(this);
		} else {

			mStateHolder = new StateHolder();

			String pointValueId = null;
			if (getIntent().getExtras() != null) {
				if (getIntent().getExtras().containsKey(
						Hiniid.EXTRA_RANDOM_POINT_RESULT)) {
					HiniiRandomPointResult randomPoint = getIntent()
							.getExtras().getParcelable(
									Hiniid.EXTRA_RANDOM_POINT_RESULT);
					pointValueId = randomPoint.getId();
					mStateHolder.setRandomPoint(randomPoint);
				} else if (getIntent().getExtras().containsKey(
						Hiniid.EXTRA_POINT_ID)) {
					pointValueId = getIntent().getExtras().getString(
							Hiniid.EXTRA_POINT_ID);
				} else if (getIntent().getExtras().containsKey(
						Hiniid.EXTRA_POINT_VALUE)) {
					PointValue pointVal = (PointValue) getIntent().getExtras()
							.getParcelable(Hiniid.EXTRA_POINT_VALUE);
					pointValueId = pointVal.getPointId();
				} else {
					Log
							.e(TAG,
									"UserDetailsActivity requires a userid in its intent extras.");
					finish();
					return;
				}

			} else {
				Log
						.e(TAG,
								"UserDetailsActivity requires a userid in its intent extras.");
				finish();
				return;
			}

			mStateHolder.startTaskPointValueDetails(this, pointValueId);
		}

		mRrm = ((Hiniid) getApplication()).getRemoteResourceManager();
		mResourcesObserver = new RemoteResourceManagerObserver();
		mRrm.addObserver(mResourcesObserver);

		ensureUi();
		populateUi();

		if (mStateHolder.getIsRunningPointValueDetailsTask() == false) {
			populateUiAfterFullUserObjectFetched();
		}
	}

	private void ensureUi() {
		mImageViewPhoto = (ImageView) findViewById(R.id.userDetailsActivityPhoto);
		mImageViewPhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mStateHolder.getRandomPoint() != null) {
					// If "_thumbs" exists, remove it to get the url of the
					// full-size image.
					String photoUrl = mStateHolder.getRandomPoint()
							.getImageOrig();

					Intent intent = new Intent();
					intent.setClass(HiniiPointValueActivity2.this,
							FetchImageForViewIntent.class);
					intent
							.putExtra(FetchImageForViewIntent.IMAGE_URL,
									photoUrl);
					intent
							.putExtra(
									FetchImageForViewIntent.PROGRESS_BAR_TITLE,
									getResources()
											.getString(
													R.string.user_activity_fetch_full_image_title));
					intent
							.putExtra(
									FetchImageForViewIntent.PROGRESS_BAR_MESSAGE,
									getResources()
											.getString(
													R.string.user_activity_fetch_full_image_message));
					startActivity(intent);
				}
			}
		});

		mTextViewName = (TextView) findViewById(R.id.textViewRandomPointName);
		mTextViewCost = (TextView) findViewById(R.id.textViewRandomPointCostAvg);
		mTextViewVoteAvg = (TextView) findViewById(R.id.textViewRandomPointVote);

		// When the user clicks the mayorships section, then launch the
		// mayorships activity.
		mLayoutVote = (LinearLayout) findViewById(R.id.randomPointActivityVoteLayout);
		mLayoutVote.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// startMayorshipsActivity();
			}
		});
		mLayoutVote.setEnabled(false);

		// When the user clicks the badges section, then launch the badges
		// activity.
		mLayoutCost = (LinearLayout) findViewById(R.id.randomPointActivityCostLayout);
		mLayoutCost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// startBadgesActivity();
			}
		});
		mLayoutCost.setEnabled(false);

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuUtils.addPreferencesToMenu(this, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
				mCheckedInSuccessfully = true;
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

	private void onRandomPointSet() {
		HiniiRandomPointResult randomPoint = mStateHolder.randomPoint;
		if (DEBUG)
			Log.d(TAG, "onPointValueSet:" + randomPoint.getName());
		setTitle(randomPoint.getName() + " - Hinii");

	}

	private void setRandomPointValue(HiniiRandomPointResult randomPoint) {
		mStateHolder.randomPoint = randomPoint;
		mStateHolder.pointValueId = randomPoint.getId();
		pointValueObservable.notifyObservers(randomPoint);
		onRandomPointSet();
	}

	class PointValueObservable extends Observable {
		@Override
		public void notifyObservers(Object data) {
			setChanged();
			super.notifyObservers(data);
		}

		public HiniiRandomPointResult getPointValue() {
			return mStateHolder.randomPoint;
		}
	}

	private void onUserDetailsTaskComplete(HiniiRandomPointResult pointResult,
			Exception ex) {
		setProgressBarIndeterminateVisibility(false);
		mStateHolder.setFetchedPointValueDetails(true);
		mStateHolder.setIsRunningPointValueDetailsTask(false);
		if (pointResult != null) {
			mStateHolder.setRandomPoint(pointResult);
			populateUiAfterFullUserObjectFetched();
		} else {
			NotificationsUtil.ToastReasonForFailure(this, ex);
		}
	}

	private void populateUiAfterFullUserObjectFetched() {
		populateUi();

		// User object may still be unavailable.
		HiniiRandomPointResult randomPoint = mStateHolder.getRandomPoint();
		if (randomPoint == null) {
			return;
		}

		mLayoutProgressBar.setVisibility(View.GONE);

		mTabHost.clearAllTabs();
		mTabHost.getTabWidget().setVisibility(View.VISIBLE);

		TabHost.TabSpec specTab1 = mTabHost.newTabSpec("tab1");
		// ----------------
		String tag;
		Intent intent;

		tag = (String) this.getText(R.string.pointvalue_details_tab);
		intent = new Intent(this, HiniiPointValueTabActivity.class);
		intent.putExtra(Hiniid.EXTRA_RANDOM_POINT_RESULT, randomPoint);
		TabsUtil.addNativeLookingTab(this, mTabHost, "t2", tag,
				R.drawable.places_tab, intent);

		tag = (String) this.getText(R.string.pointvalue_map_tab);
		intent = new Intent(this, HiniiPointValueMapActivity.class);
		TabsUtil.addNativeLookingTab(this, mTabHost, "t1", tag,
				R.drawable.map_tab, intent);

		// User can also now click on the badges / mayorships layouts.
		mLayoutCost.setEnabled(true);
		mLayoutVote.setEnabled(true);
	}



	private void populateUi() {
		HiniiRandomPointResult randomPoint = mStateHolder.getRandomPoint();

		// User photo.
		if (randomPoint != null && mIsUsersPhotoSet == false) {
			mImageViewPhoto.setImageResource(R.drawable.blank_girl);
			if (randomPoint != null) {
				Uri uriPhoto = Uri.parse(randomPoint.getImage());
				if (mRrm.exists(uriPhoto)) {
					try {
						Bitmap bitmap = BitmapFactory.decodeStream(mRrm
								.getInputStream(Uri.parse(randomPoint
										.getImage())));
						mImageViewPhoto.setImageBitmap(bitmap);
						mIsUsersPhotoSet = true;
					} catch (IOException e) {
					}
				} else {
					mRrm.request(uriPhoto);
				}
			}
		}

		// User name.
		if (randomPoint != null) {
			mTextViewName.setText(randomPoint.getName());
		} else {
			mTextViewName.setText("");
		}

		// Cost average.
		if (randomPoint != null) {
			if (mStateHolder.getFetchedPointValueDetails()) {
				String cost = randomPoint.getCost();
				if ((cost != null) & (!cost.equals("0"))) {
					mTextViewCost.setText(randomPoint.getCost());
				} else {
					mTextViewCost.setText("-");
				}
			} else {

			}
		} else {
			mTextViewCost.setText("-");
		}

		// Vote Avg
		if (randomPoint != null) {
			String voteNum = randomPoint.getVoteNum();
			if (Integer.parseInt(voteNum.trim()) != 0)

				if ((mStateHolder.getFetchedPointValueDetails())
						& (randomPoint.getVoteAvg() != null)
						& (voteNum != null)
						& (Integer.parseInt(voteNum.trim()) != 0)) {

					mTextViewVoteAvg.setText(randomPoint.getVoteAvg() + "/5");
				} else {
					mTextViewVoteAvg.setText("-");
				}
		} else {
			mTextViewVoteAvg.setText("-");
		}
	}

	private static final class StateHolder {
		private HiniiRandomPointResult randomPoint = null;

		String pointValueId = null;
		private boolean mIsRunningPointValueDetailsTask;
		private UserDetailsTask mTaskUserDetails;
		private boolean mFetchedPointValueDetails;

		public void startTaskPointValueDetails(
				HiniiPointValueActivity2 activity, String userId) {
			mIsRunningPointValueDetailsTask = true;
			mTaskUserDetails = new UserDetailsTask(activity);
			mTaskUserDetails.execute(userId);
		}

		public void setIsRunningPointValueDetailsTask(boolean isRunning) {
			mIsRunningPointValueDetailsTask = isRunning;
		}

		public boolean getIsRunningPointValueDetailsTask() {
			return mIsRunningPointValueDetailsTask;
		}

		public void setFetchedPointValueDetails(boolean fetched) {
			mFetchedPointValueDetails = fetched;
		}

		public boolean getFetchedPointValueDetails() {
			return mFetchedPointValueDetails;
		}

		/**
		 * @param randomPoint
		 *            the randomPoint to set
		 */
		public void setRandomPoint(HiniiRandomPointResult randomPoint) {
			this.randomPoint = randomPoint;
		}

		public void setActivityForTaskUserDetails(
				HiniiPointValueActivity2 activity) {
			if (mTaskUserDetails != null) {
				mTaskUserDetails.setActivity(activity);
			}
		}

		/**
		 * @return the randomPoint
		 */
		public HiniiRandomPointResult getRandomPoint() {
			return randomPoint;
		}

		public void cancelTasks() {
			if (mTaskUserDetails != null) {
				mTaskUserDetails.setActivity(null);
				mTaskUserDetails.cancel(true);
			}
		}

	}

	/**
	 * Even if the caller supplies us with a User object parcelable, it won't
	 * have all the badge etc extra info in it. As soon as the activity starts,
	 * we launch this task to fetch a full user object, and merge it with
	 * whatever is already supplied in mUser.
	 */
	private static class UserDetailsTask extends
			AsyncTask<String, Void, HiniiRandomPointResult> {

		private HiniiPointValueActivity2 mActivity;
		private Exception mReason;

		public UserDetailsTask(HiniiPointValueActivity2 activity) {
			mActivity = activity;
		}

		@Override
		protected void onPreExecute() {
			mActivity.setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected HiniiRandomPointResult doInBackground(String... params) {
			try {
				return ((Hiniid) mActivity.getApplication()).getHinii()
						.getRandomPointResult(params[0]);
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
		protected void onPostExecute(HiniiRandomPointResult randomPoint) {
			if (mActivity != null) {
				mActivity.onUserDetailsTaskComplete(randomPoint, mReason);
			}
		}

		@Override
		protected void onCancelled() {
			if (mActivity != null) {
				mActivity.onUserDetailsTaskComplete(null, mReason);
			}
		}

		public void setActivity(HiniiPointValueActivity2 activity) {
			mActivity = activity;
		}
	}

	private class RemoteResourceManagerObserver implements Observer {
		@Override
		public void update(Observable observable, Object data) {
			mImageViewPhoto.getHandler().post(new Runnable() {
				@Override
				public void run() {
					if (mStateHolder.getRandomPoint() != null) {
						Uri uriPhoto = Uri.parse(mStateHolder.getRandomPoint()
								.getImage());
						if (mRrm.exists(uriPhoto)) {
							try {
								Bitmap bitmap = BitmapFactory.decodeStream(mRrm
										.getInputStream(uriPhoto));
								mImageViewPhoto.setImageBitmap(bitmap);
								mIsUsersPhotoSet = true;
								mImageViewPhoto.setImageBitmap(bitmap);
							} catch (IOException e) {
							}
						}
					}
				}
			});
		}
	}
}
