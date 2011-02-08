package com.angelo.hiniid;

import java.util.Observable;
import java.util.Observer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.angelo.hiniid.maps.CrashFixMyLocationOverlay;
import com.angelo.hiniid.maps.PointValueItemizedOverlay;
import com.angelo.hiniid.maps.RandomPointItemizedOverlay;
import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.parsers.HiniiRandomPointParser;
import com.angelo.hiniid.rest.types.Group;
import com.angelo.hiniid.rest.types.HiniiRandomPointResult;
import com.angelo.hiniid.rest.types.PointValue;
import com.angelo.hiniid.rest.util.PointValueUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

/**
 * @author angelo from VenueMapActivity
 */
public class HiniiPointValueMapActivity extends MapActivity {
	public static final String TAG = "HiniiPointValueListMapActivity";
	public static final boolean DEBUG = Hinii.DEBUG;

	private MapView mMapView;
	private MapController mMapController;
	private RandomPointItemizedOverlay mOverlay = null;
	private MyLocationOverlay mMyLocationOverlay = null;

	private HiniiRandomPointResult mmPointValue;
	private Observer mPointValueObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hinii_point_value_list_map_activity);

		Button mapsButton = (Button) findViewById(R.id.mapsButton2);
		mapsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse( //
						"geo:0,0?q=" + mmPointValue.getName() + " near "
								+ mmPointValue.getCity()));
				startActivity(intent);

			}
		});

		initMap();

		HiniiRandomPointResult pointValue = ((HiniiPointValueActivity2) getParent()).pointValueObservable
				.getPointValue();
		if (pointValue != null) {
			setPointValue(pointValue);
			updateMap();

		} else {
			mPointValueObserver = new PointValueObserver();
			((HiniiPointValueActivity2) getParent()).pointValueObservable
					.addObserver(mPointValueObserver);
		}
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
		return false;
	}

	private void initMap() {
		mMapView = (MapView) findViewById(R.id.mapView);
		mMapView.setBuiltInZoomControls(true);
		mMapController = mMapView.getController();

		mMyLocationOverlay = new CrashFixMyLocationOverlay(this, mMapView);
		mMapView.getOverlays().add(mMyLocationOverlay);

		mOverlay = new RandomPointItemizedOverlay(this.getResources()
				.getDrawable(R.drawable.map_marker_blue));
	}

	private void setPointValue(HiniiRandomPointResult randomPoint) {
		Group<HiniiRandomPointResult> randomPointGroup = new Group<HiniiRandomPointResult>();
		randomPointGroup.setType("Current Point Value");
		randomPointGroup.add(randomPoint);
		mmPointValue = randomPoint;
		if (PointValueUtils.hasValidLocation(randomPoint)) {
			mOverlay.setGroup(randomPointGroup);
			mMapView.getOverlays().add(mOverlay);
		}
	}

	private void updateMap() {
		GeoPoint center;
		if (mOverlay != null && mOverlay.size() > 0) {
			center = mOverlay.getCenter();
		} else {
			return;
		}
		mMapController.animateTo(center);
		mMapController.setZoom(17);
	}

	private final class PointValueObserver implements Observer {
		@Override
		public void update(Observable observable, Object data) {
			setPointValue((HiniiRandomPointResult) data);
			updateMap();
		}
	}
}
