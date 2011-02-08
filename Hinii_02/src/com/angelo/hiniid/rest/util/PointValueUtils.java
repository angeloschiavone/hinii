package com.angelo.hiniid.rest.util;

import com.angelo.hiniid.rest.types.HiniiRandomPointResult;
import com.angelo.hiniid.rest.types.PointValue;

/**
 * @author Angelo Schiavone
 */
public class PointValueUtils {

	public static final boolean isValid(PointValue pointValue) {
		return !(pointValue == null || pointValue.getPointId() == null || pointValue
				.getPointId().length() == 0);
	}

	public static final boolean hasValidLocation(PointValue pointValue) {
		boolean valid = false;
		if (pointValue != null) {
			String geoLat = pointValue.getPointLat();
			String geoLong = pointValue.getPointLng();
			if (!(geoLat == null || geoLat.length() == 0 || geoLong == null || geoLong
					.length() == 0)) {
				if (geoLat != "0" || geoLong != "0") {
					valid = true;
				}
			}
		}
		return valid;
	}

	public static final boolean hasValidLocation(
			HiniiRandomPointResult randomPoint) {
		boolean valid = false;
		if (randomPoint != null) {
			String geoLat = randomPoint.getLatitude();
			String geoLong = randomPoint.getLongitude();
			if (!(geoLat == null || geoLat.length() == 0 || geoLong == null || geoLong
					.length() == 0)) {
				if (geoLat != "0" || geoLong != "0") {
					valid = true;
				}
			}
		}
		return valid;
	}

}
