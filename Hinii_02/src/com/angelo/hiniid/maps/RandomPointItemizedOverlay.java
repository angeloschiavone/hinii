package com.angelo.hiniid.maps;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.types.HiniiRandomPointResult;
import com.angelo.hiniid.rest.util.PointValueUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * @author angelo, from VenueItemizedOverlay
 */
public class RandomPointItemizedOverlay extends
		BaseGroupItemizedOverlay<HiniiRandomPointResult> {
	public static final String TAG = "PointValueItemizedOverlay";
	public static final boolean DEBUG = Hinii.DEBUG;

	private boolean mPopulatedSpans = false;
	private SpanHolder mSpanHolder = new SpanHolder();

	public RandomPointItemizedOverlay(Drawable defaultMarker) {
		super(defaultMarker);
	}

	@Override
	protected OverlayItem createItem(int i) {
		HiniiRandomPointResult pointValue = (HiniiRandomPointResult) group
				.get(i);
		if (DEBUG)
			Log.d(TAG,
					"creating PointValue overlayItem: " + pointValue.getName());
		int lat = (int) (Double.parseDouble(pointValue.getLatitude()) * 1E6);
		int lng = (int) (Double.parseDouble(pointValue.getLongitude()) * 1E6);
		GeoPoint point = new GeoPoint(lat, lng);
		return new RandomPointOverlayItem(point, pointValue);
	}

	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		if (DEBUG)
			Log.d(TAG, "onTap: " + p);
		mapView.getController().animateTo(p);
		return super.onTap(p, mapView);
	}

	@Override
	public int getLatSpanE6() {
		if (!mPopulatedSpans) {
			populateSpans();
		}
		return mSpanHolder.latSpanE6;
	}

	@Override
	public int getLonSpanE6() {
		if (!mPopulatedSpans) {
			populateSpans();
		}
		return mSpanHolder.lonSpanE6;
	}

	private void populateSpans() {
		int maxLat = 0;
		int minLat = 0;
		int maxLon = 0;
		int minLon = 0;
		for (int i = 0; i < group.size(); i++) {
			HiniiRandomPointResult pointValue = (HiniiRandomPointResult) group
					.get(i);
			if (PointValueUtils.hasValidLocation(pointValue)) {
				int lat = (int) (Double.parseDouble(pointValue.getLatitude()) * 1E6);
				int lon = (int) (Double.parseDouble(pointValue.getLongitude()) * 1E6);

				// LatSpan
				if (lat > maxLat || maxLat == 0) {
					maxLat = lat;
				}
				if (lat < minLat || minLat == 0) {
					minLat = lat;
				}

				// LonSpan
				if (lon < minLon || minLon == 0) {
					minLon = lon;
				}
				if (lon > maxLon || maxLon == 0) {
					maxLon = lon;
				}
			}
		}
		mSpanHolder.latSpanE6 = maxLat - minLat;
		mSpanHolder.lonSpanE6 = maxLon - minLon;
	}

	public static class RandomPointOverlayItem extends OverlayItem {

		private HiniiRandomPointResult randomPoint;

		public RandomPointOverlayItem(GeoPoint point,
				HiniiRandomPointResult pointValue) {
			super(point, pointValue.getName(), pointValue.getAddress());
			randomPoint = pointValue;
		}

		public HiniiRandomPointResult getPointValue() {
			return randomPoint;
		}
	}

	public static final class SpanHolder {
		int latSpanE6 = 0;
		int lonSpanE6 = 0;
	}

}
