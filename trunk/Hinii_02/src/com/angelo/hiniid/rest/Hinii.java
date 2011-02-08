package com.angelo.hiniid.rest;

import java.io.IOException;
import java.util.logging.Logger;

import android.util.Log;

import com.angelo.hiniid.rest.error.HiniiError;
import com.angelo.hiniid.rest.error.HiniiException;
import com.angelo.hiniid.rest.types.HiniiRandomPointResult;
import com.angelo.hiniid.rest.types.SearchResult;

/**
 * @author Angelo Schiavone
 */
public class Hinii {
	private static final Logger LOG = Logger.getLogger("com.angelo.hinii");
	public static final boolean DEBUG = true;
	public static final boolean PARSER_DEBUG = true;

	public static final String HINII_API_DOMAIN = "atools.hinii.com";
	public static final String HINII_PREFERENCES = "http://hinii.com/";
	private HiniiHttpApi hiniiHttpApi;

	public Hinii(HiniiHttpApi httpApi) {
		hiniiHttpApi = httpApi;
	}

	public HiniiRandomPointResult getRandomPointResult(String id)
			throws HiniiException, HiniiError, IOException {

		// http://hinii.com/en/api/pointview/id/12/category/5/details/0/
		Log.e("getRandomPointResult", "getRandomPointResult");
		HiniiRandomPointResult randomPoint = hiniiHttpApi
				.getRandomPointResult(id);
		Log.e("randomPoint.getName() ",
				"randomPoint.getName() " + randomPoint.getName());
		return randomPoint;
	}

	public SearchResult getSearchResult(String company, String daykind,
			String daypart) throws HiniiException, HiniiError, IOException {
		/*
		 * "http://alpha.hinii.com/en/api/search/company_kind/2/day_kind/0/" +
		 * "day_part/1/city/Milano%2cItalia/lat/45.4419/lng/9.1635/" +
		 * "day_week_code/3/distance/2/";
		 */

		return hiniiHttpApi.getPointValues(Integer.parseInt(company),
				Integer.parseInt(daykind), Integer.parseInt(daypart),
				"Milano,Italy", 45.44, 9.16, 4, 2);

		// return hiniiHttpApi.getSearchResultMilano();
	}

	public SearchResult getSearchResult(String company, String daykind,
			String daypart, double lat, double lng) throws HiniiException,
			HiniiError, IOException {
		/*
		 * "http://alpha.hinii.com/en/api/search/company_kind/2/day_kind/0/" +
		 * "day_part/1/city/Milano%2cItalia/lat/45.4419/lng/9.1635/" +
		 * "day_week_code/3/distance/2/";
		 */

		return hiniiHttpApi.getPointValues(Integer.parseInt(company),
				Integer.parseInt(daykind), Integer.parseInt(daypart),
				"Bologna,Italy", lat, lng, 4, 2);

		// return hiniiHttpApi.getSearchResultMilano();
	}

	public String getSearchResultQuery(String company, String daykind,
			String daypart, double lat, double lng) {
		/*
		 * "http://alpha.hinii.com/en/api/search/company_kind/2/day_kind/0/" +
		 * "day_part/1/city/Milano%2cItalia/lat/45.4419/lng/9.1635/" +
		 * "day_week_code/3/distance/2/";
		 */

		return hiniiHttpApi.getQuerySearchResult(Integer.parseInt(company),
				Integer.parseInt(daykind), Integer.parseInt(daypart),
				"Bologna,Italy", lat, lng, 4, 2);

		// return hiniiHttpApi.getSearchResultMilano();
	}

	public String getSearchResultQuery(String company, String daykind,
			String daypart, String city) {
		return hiniiHttpApi.getQuerySearchResult(Integer.parseInt(company),
				Integer.parseInt(daykind), Integer.parseInt(daypart), city, 4,
				2);
	}

	public SearchResult getSearchResult(String company, String daykind,
			String daypart, String city) throws HiniiException, HiniiError,
			IOException {
		/*
		 * "http://alpha.hinii.com/en/api/search/company_kind/2/day_kind/0/" +
		 * "day_part/1/city/Milano%2cItalia/lat/45.4419/lng/9.1635/" +
		 * "day_week_code/3/distance/2/";
		 */

		return hiniiHttpApi.getPointValues(Integer.parseInt(company),
				Integer.parseInt(daykind), Integer.parseInt(daypart), city, 4,
				2);

		// return hiniiHttpApi.getSearchResultMilano();
	}

	public static final HiniiHttpApi createHttpApi(String domain,
			String clientVersion, boolean useOAuth) {

		return new HiniiHttpApi(domain, clientVersion, useOAuth);
	}

	public static final HiniiHttpApi createHttpApi(String clientVersion,
			boolean useOAuth) {
		return createHttpApi(HINII_API_DOMAIN, clientVersion, useOAuth);
	}

}
