package com.angelo.hiniid.rest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.logging.Logger;

import org.apache.http.auth.AuthScope;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.angelo.hiniid.rest.error.HiniiError;
import com.angelo.hiniid.rest.error.HiniiException;
import com.angelo.hiniid.rest.http.AbstractHttpApi;
import com.angelo.hiniid.rest.http.HttpApi;
import com.angelo.hiniid.rest.http.HttpApiWithoutAuth;
import com.angelo.hiniid.rest.parsers.HiniiRandomPointParser;
import com.angelo.hiniid.rest.parsers.SearchResultParser;
import com.angelo.hiniid.rest.types.HiniiRandomPointResult;
import com.angelo.hiniid.rest.types.SearchResult;

/**
 * @author Angelo Schiavone
 */
class HiniiHttpApi {
	private static final Logger LOG = Logger.getLogger(HiniiHttpApi.class
			.getCanonicalName());
	private static final boolean DEBUG = Hinii.DEBUG;
	// ---------------------------------------------------------------------------
	private static final String URL_API_LANGUAGE_EN = "/en";
	private static final String URL_API_LANGUAGE_IT = "/it";
	private static final String URL_API_ACTION = "/api";
	private static final String URL_API_COMPANY_KIND = "/company_kind";
	private static final String URL_API_DAY_KIND = "/day_kind";
	private static final String URL_API_DAY_PART = "/day_part";
	private static final String URL_API_CITY = "/city";
	private static final String URL_API_LAT = "/lat";
	private static final String URL_API_LNG = "/lng";
	private static final String URL_API_ID = "/id";
	private static final String URL_API_DAY_WEEK_CODE = "/day_week_code";
	private static final String URL_API_DISTANCE = "/distance";
	// ---------------------------------------------------------------------------
	private final DefaultHttpClient mHttpClient = AbstractHttpApi
			.createHttpClient();
	private HttpApi mHttpApi;
	private HttpApi mHttpApiHinii;

	BasicNameValuePair androidTokenValuePair = new BasicNameValuePair("token",
			"0652d407ca059e8f6a23d1311eda1691");

	private final String mApiBaseUrl;
	private final AuthScope mAuthScope;

	public HiniiHttpApi(String domain, String clientVersion, boolean useOAuth) {
		mApiBaseUrl = "http://" + domain + "";
		mAuthScope = new AuthScope(domain, 80);

		mHttpApiHinii = new HttpApiWithoutAuth(mHttpClient, clientVersion);
	}

	public HiniiRandomPointResult getRandomPointResult(String id)
			throws HiniiException, HiniiError, IOException {
		/*
		 * "http://alpha.hinii.com/en/api/search/company_kind/2/day_kind/0/" +
		 * "day_part/1/city/Milano%2cItalia/lat/45.4419/lng/9.1635/" +
		 * "day_week_code/3/distance/2/";
		 */
		String query = mApiBaseUrl + getUrlApiLanguage()
				+ couple(URL_API_ACTION, "pointview") + couple(URL_API_ID, id)
				+ "/";

		Log.e("query", query);

		HttpPost httpPost = mHttpApiHinii.createHttpPost(query,
				androidTokenValuePair);
		HiniiRandomPointResult randomPoint = (HiniiRandomPointResult) mHttpApiHinii
				.doHttpRequest(httpPost, new HiniiRandomPointParser());
		return randomPoint;
	}

	private String getUrlApiLanguage() {
		Locale locale = Locale.getDefault();
		if (locale.getCountry().equalsIgnoreCase("it"))
			return URL_API_LANGUAGE_IT;
		else
			return URL_API_LANGUAGE_EN;
	}

	public String getQuerySearchResult(int companyKind, int dayKind,
			int dayPart, String city, int dayWeekCode, int distance) {

		return mApiBaseUrl + getUrlApiLanguage()
				+ couple(URL_API_ACTION, "search")
				+ couple(URL_API_COMPANY_KIND, companyKind)
				+ couple(URL_API_DAY_KIND, dayKind)
				+ couple(URL_API_DAY_PART, dayPart)
				+ couple(URL_API_CITY, city)
				+ couple(URL_API_DAY_WEEK_CODE, dayWeekCode)
				+ couple(URL_API_DISTANCE, distance) + "/";
	}

	public String getQuerySearchResult(int companyKind, int dayKind,
			int dayPart, String city, double lat, double lng, int dayWeekCode,
			int distance) {
		/*
		 * "http://alpha.hinii.com/en/api/search/company_kind/2/day_kind/0/" +
		 * "day_part/1/city/Milano%2cItalia/lat/45.4419/lng/9.1635/" +
		 * "day_week_code/3/distance/2/";
		 */
		return mApiBaseUrl + getUrlApiLanguage()
				+ couple(URL_API_ACTION, "search")
				+ couple(URL_API_COMPANY_KIND, companyKind)
				+ couple(URL_API_DAY_KIND, dayKind)
				+ couple(URL_API_DAY_PART, dayPart)
				+ couple(URL_API_CITY, city) + couple(URL_API_LAT, lat)
				+ couple(URL_API_LNG, lng)
				+ couple(URL_API_DAY_WEEK_CODE, dayWeekCode)
				+ couple(URL_API_DISTANCE, distance) + "/";
	}

	public SearchResult getPointValues(int companyKind, int dayKind,
			int dayPart, String city, int dayWeekCode, int distance)
			throws HiniiException, HiniiError, IOException {
		/*
		 * "http://alpha.hinii.com/en/api/search/company_kind/2/day_kind/0/" +
		 * "day_part/1/city/Milano%2cItalia/lat/45.4419/lng/9.1635/" +
		 * "day_week_code/3/distance/2/";
		 */
		String query = mApiBaseUrl + getUrlApiLanguage()
				+ couple(URL_API_ACTION, "search")
				+ couple(URL_API_COMPANY_KIND, companyKind)
				+ couple(URL_API_DAY_KIND, dayKind)
				+ couple(URL_API_DAY_PART, dayPart)
				+ couple(URL_API_CITY, city)
				+ couple(URL_API_DAY_WEEK_CODE, dayWeekCode)
				+ couple(URL_API_DISTANCE, distance) + "/";

		Log.e("query", query);

		HttpPost httpPost = mHttpApiHinii.createHttpPost(query,
				androidTokenValuePair);
		SearchResult searchResult = (SearchResult) mHttpApiHinii.doHttpRequest(
				httpPost, new SearchResultParser());
		return searchResult;
	}

	public SearchResult getPointValues(int companyKind, int dayKind,
			int dayPart, String city, double lat, double lng, int dayWeekCode,
			int distance) throws HiniiException, HiniiError, IOException {
		/*
		 * "http://alpha.hinii.com/en/api/search/company_kind/2/day_kind/0/" +
		 * "day_part/1/city/Milano%2cItalia/lat/45.4419/lng/9.1635/" +
		 * "day_week_code/3/distance/2/";
		 */
		String query = mApiBaseUrl + getUrlApiLanguage()
				+ couple(URL_API_ACTION, "search")
				+ couple(URL_API_COMPANY_KIND, companyKind)
				+ couple(URL_API_DAY_KIND, dayKind)
				+ couple(URL_API_DAY_PART, dayPart)
				+ couple(URL_API_CITY, city) + couple(URL_API_LAT, lat)
				+ couple(URL_API_LNG, lng)
				+ couple(URL_API_DAY_WEEK_CODE, dayWeekCode)
				+ couple(URL_API_DISTANCE, distance) + "/";

		Log.e("query", query);

		HttpPost httpPost = mHttpApiHinii.createHttpPost(query,
				androidTokenValuePair);
		SearchResult searchResult = (SearchResult) mHttpApiHinii.doHttpRequest(
				httpPost, new SearchResultParser());
		return searchResult;
	}

	public static String couple(String key, String value) {
		return key + "/" + URLEncoder.encode(value);
	}

	public static String couple(String key, int value) {
		return key + "/" + value;
	}

	public static String couple(String key, double value) {
		return key + "/" + value;
	}
}
