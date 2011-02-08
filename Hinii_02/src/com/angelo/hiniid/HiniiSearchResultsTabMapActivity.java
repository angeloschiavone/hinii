package com.angelo.hiniid;

import java.util.Observable;
import java.util.Observer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.angelo.hiniid.maps.CrashFixMyLocationOverlay;
import com.angelo.hiniid.maps.PointValueItemizedOverlay;
import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.types.Group;
import com.angelo.hiniid.rest.types.PointValue;
import com.angelo.hiniid.rest.types.SearchResult;
import com.angelo.hiniid.rest.util.PointValueUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * @author angelo from VenueMapActivity
 */
public class HiniiSearchResultsTabMapActivity extends MapActivity {
	public static final String TAG = "HiniiSearchResultsTabMapActivity";
	public static final boolean DEBUG = Hinii.DEBUG;

	private MapView mMapView;
	private MapController mMapController;
	private PointValueItemizedOverlay mOverlay = null;
	private MyLocationOverlay mMyLocationOverlay = null;

	private SearchResultsObserver searchResultsObserver = new SearchResultsObserver();

	// private HiniiRandomPointResult mmPointValue;
	private Group<PointValue> pointValues;
	private Observer mPointValueObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.hinii_point_value_list_map_activity);
		initMap();
		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras()
					.containsKey(Hiniid.EXTRA_SEARCH_RESULTS)) {
				SearchResult searchResult = getIntent().getExtras()
						.getParcelable(Hiniid.EXTRA_SEARCH_RESULTS);
				// putSearchResultsInAdapter(searchResult);
				setSearchResult(searchResult);
			}
		}

		Button mapsButton = (Button) findViewById(R.id.mapsButton2);
		mapsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);

				// Uri uri =
				// Uri.parse("http://maps.google.com/maps?&saddr=13.042206,80.17000&daddr=9.580000,78.100000");
				// Uri uri =
				// Uri.parse("http://maps.google.com/maps?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA&sensor=false");
				// 40.763500,-73.979305
				// Uri uri =
				// Uri.parse("geo:40.763500,-73.979305+40.763500,-72.979305+40.763500,-71.979305");
				Uri uri = Uri.parse("geo:0,0?q="
						+ pointValues.get(0).getPointName() + " near "
						+ pointValues.get(0).getPointCity());

				intent.setData(uri);
				startActivity(intent);

			}
		});

		SearchResult searchResult = ((HiniiSearchResultsActivity) getParent()).searchResultsObservable
				.getResults();

		if (searchResult != null) {
			setSearchResult(searchResult);
			updateMap();

		}

		((HiniiSearchResultsActivity) getParent()).searchResultsObservable
				.addObserver(searchResultsObserver);
	}

	@Override
	public void onResume() {
		super.onResume();
		mMyLocationOverlay.enableMyLocation();

		// mMyLocationOverlay.enableCompass(); // Disabled due to a sdk 1.5
		// emulator bug
	}

	@Override
	public void onPause() {
		super.onPause();
		mMyLocationOverlay.disableMyLocation();
		mMyLocationOverlay.disableCompass();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}

	private void initMap() {
		mMapView = (MapView) findViewById(R.id.mapView);
		mMapView.setBuiltInZoomControls(true);
		mMapController = mMapView.getController();

		mMyLocationOverlay = new CrashFixMyLocationOverlay(this, mMapView);
		mMapView.getOverlays().add(mMyLocationOverlay);

		mOverlay = new PointValueItemizedOverlay(this.getResources()
				.getDrawable(R.drawable.map_marker_blue), this);
	}

	private void setSearchResult(SearchResult searchResult) {
		Group<PointValue> pointValues = searchResult.getIter();
		pointValues.setType("Current Point Value");

		this.pointValues = pointValues;
		for (PointValue pointValue : pointValues) {
			if (!PointValueUtils.hasValidLocation(pointValue)) {
				return;
			}
		}
		Log.e(TAG, "mOverlay " + (mOverlay == null));
		Log.e(TAG, "pointValues " + (pointValues == null));

		mOverlay.setGroup(pointValues);

		mMapView.getOverlays().add(mOverlay);
	}

	private void updateMap() {
		GeoPoint center;
		if (mOverlay != null && mOverlay.size() > 0) {
			center = mOverlay.getCenter();
		} else {
			return;
		}
		mMapController.animateTo(center);
		mMapController.setZoom(13);
	}

	private final class PointValueObserver implements Observer {
		@Override
		public void update(Observable observable, Object data) {
			setSearchResult((SearchResult) data);
			updateMap();
		}
	}

	private class SearchResultsObserver implements Observer {
		@Override
		public void update(Observable observable, Object data) {
			setSearchResult((SearchResult) data);
			updateMap();
		}
	}
}
