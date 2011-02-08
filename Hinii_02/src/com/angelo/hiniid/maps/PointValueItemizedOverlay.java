package com.angelo.hiniid.maps;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.types.PointValue;
import com.angelo.hiniid.rest.util.PointValueUtils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

/**
 * @author angelo, from VenueItemizedOverlay
 */
public class PointValueItemizedOverlay extends
		BaseGroupItemizedOverlay<PointValue> {
	public static final String TAG = "PointValueItemizedOverlay";
	public static final boolean DEBUG = Hinii.DEBUG;

	private boolean mPopulatedSpans = false;
	private SpanHolder mSpanHolder = new SpanHolder();
	private MapActivity parentMapActivity;

	public PointValueItemizedOverlay(Drawable defaultMarker,
			MapActivity parentMap) {
		super(defaultMarker);
		this.parentMapActivity = parentMap;
	}

	@Override
	protected OverlayItem createItem(int i) {
		PointValue pointValue = (PointValue) group.get(i);
		if (DEBUG)
			Log.d(TAG,
					"creating PointValue overlayItem: "
							+ pointValue.getPointName());
		int lat = (int) (Double.parseDouble(pointValue.getPointLat()) * 1E6);
		int lng = (int) (Double.parseDouble(pointValue.getPointLng()) * 1E6);
		GeoPoint point = new GeoPoint(lat, lng);
		return new PointValueOverlayItem(point, pointValue);
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
			PointValue pointValue = (PointValue) group.get(i);
			if (PointValueUtils.hasValidLocation(pointValue)) {
				int lat = (int) (Double.parseDouble(pointValue.getPointLat()) * 1E6);
				int lon = (int) (Double.parseDouble(pointValue.getPointLng()) * 1E6);

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

	@Override
	protected boolean onTap(int i) {
		Toast.makeText(
				parentMapActivity,
				group.get(i).getPointName() + "\n"
						+ group.get(i).getPointAddress() + "\n"
						+ group.get(i).getPointCity(), Toast.LENGTH_SHORT)
				.show();
		return (true);
	}

	// getCost
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		super.draw(canvas, mapView, shadow);
		Projection projection = mapView.getProjection();

		GeoPoint srcGeo;
		GeoPoint dstGeo;

		Paint mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.MAGENTA);
		mPaint.setAlpha(100);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(5);

		Path path = new Path();

		for (int i = 0; i < group.size() - 1; i++) {
			int latSrc = (int) (Double.parseDouble(group.get(i).getPointLat()) * 1E6);
			int lngSrc = (int) (Double.parseDouble(group.get(i).getPointLng()) * 1E6);
			srcGeo = new GeoPoint(latSrc, lngSrc);

			int latDst = (int) (Double.parseDouble(group.get(i + 1)
					.getPointLat()) * 1E6);
			int lngDst = (int) (Double.parseDouble(group.get(i + 1)
					.getPointLng()) * 1E6);
			dstGeo = new GeoPoint(latDst, lngDst);

			Point srcPoint = new Point();
			projection.toPixels(srcGeo, srcPoint);

			Point dstPoint = new Point();
			projection.toPixels(dstGeo, dstPoint);

			path.moveTo(dstPoint.x, dstPoint.y);
			path.lineTo(srcPoint.x, srcPoint.y);

			canvas.drawPath(path, mPaint);
		}
		return true;
	}

	public static class PointValueOverlayItem extends OverlayItem {

		private PointValue mPointValue;

		public PointValueOverlayItem(GeoPoint point, PointValue pointValue) {
			super(point, pointValue.getPointName(), pointValue
					.getPointAddress());
			mPointValue = pointValue;
		}

		public PointValue getPointValue() {
			return mPointValue;
		}
	}

	public static final class SpanHolder {
		int latSpanE6 = 0;
		int lonSpanE6 = 0;
	}

}
