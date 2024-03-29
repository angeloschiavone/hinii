package com.angelo.hiniid.rest.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.HiniiException;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.parsers.AbstractParser;
import com.angelo.hiniid.rest.parsers.Parser;
import com.angelo.hiniid.rest.types.HiniiType;

/**
 * @author Angelo Schiavone
 */
abstract public class AbstractHttpApi implements HttpApi {
	protected static final Logger LOG = Logger.getLogger(AbstractHttpApi.class
			.getCanonicalName());
	protected static final boolean DEBUG = Hinii.DEBUG;

	private static final String DEFAULT_CLIENT_VERSION = "com.angelo.hinii";
	private static final String CLIENT_VERSION_HEADER = "User-Agent";
	private static final int TIMEOUT = 25;

	private final DefaultHttpClient mHttpClient;
	private final String mClientVersion;

	public AbstractHttpApi(DefaultHttpClient httpClient, String clientVersion) {
		mHttpClient = httpClient;
		if (clientVersion != null) {
			mClientVersion = clientVersion;
		} else {
			mClientVersion = DEFAULT_CLIENT_VERSION;
		}
	}

	public HiniiType executeHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends HiniiType> parser) throws HiniiParseException,
			HiniiException, IOException {
		if (DEBUG)
			LOG.log(Level.FINE, "doHttpRequest: " + httpRequest.getURI());

		HttpResponse response = executeHttpRequest(httpRequest);
		if (DEBUG)
			LOG.log(Level.FINE, "executed HttpRequest for: "
					+ httpRequest.getURI().toString());

		int statusCode = response.getStatusLine().getStatusCode();

		Log.e("AbstractHttpApi", "Response statusCode " + statusCode);
		switch (statusCode) {
		case 200:
			InputStream is = response.getEntity().getContent();
			Reader r = new InputStreamReader(is, "UTF-8");

			try {
				return parser.parse(AbstractParser.createXmlPullParser(r));
			} finally {
				is.close();
			}

		case 400:
			if (DEBUG)
				LOG.log(Level.FINE, "HTTP Code: 400");
			throw new HiniiException(response.getStatusLine().toString(),
					EntityUtils.toString(response.getEntity()));

		case 401:
			response.getEntity().consumeContent();
			if (DEBUG)
				LOG.log(Level.FINE, "HTTP Code: 401");
			throw new HiniiException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			if (DEBUG)
				LOG.log(Level.FINE, "HTTP Code: 404");
			throw new HiniiException(response.getStatusLine().toString());

		case 500:
			response.getEntity().consumeContent();
			if (DEBUG)
				LOG.log(Level.FINE, "HTTP Code: 500");
			throw new HiniiException(
					"Hinii server not available now. Please try again later.");

		default:
			if (DEBUG)
				LOG.log(Level.FINE, "Default case for status code reached: "
						+ response.getStatusLine().toString());
			response.getEntity().consumeContent();
			throw new HiniiException("Error connecting to Hinii: " + statusCode
					+ ". Try again later.");
		}
	}

	public String doHttpPost(String url, NameValuePair... nameValuePairs)
			throws HiniiParseException, HiniiException, IOException {
		if (DEBUG)
			LOG.log(Level.FINE, "doHttpPost: " + url);
		HttpPost httpPost = createHttpPost(url, nameValuePairs);

		HttpResponse response = executeHttpRequest(httpPost);
		if (DEBUG)
			LOG.log(Level.FINE, "executed HttpRequest for: "
					+ httpPost.getURI().toString());

		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException e) {
				throw new HiniiParseException(e.getMessage());
			}

		case 401:
			response.getEntity().consumeContent();
			throw new HiniiException(response.getStatusLine().toString());

		case 404:
			response.getEntity().consumeContent();
			throw new HiniiException(response.getStatusLine().toString());

		default:
			Log.e("response.getStatusLine().getStatusCode()", ""
					+ response.getStatusLine().getStatusCode());
			response.getEntity().consumeContent();
			throw new HiniiException(response.getStatusLine().toString());
		}
	}

	/**
	 * execute() an httpRequest catching exceptions and returning null instead.
	 * 
	 * @param httpRequest
	 * @return
	 * @throws IOException
	 */
	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest)
			throws IOException {
		if (DEBUG)
			LOG.log(Level.FINE, "executing HttpRequest for: "
					+ httpRequest.getURI().toString());
		try {
			mHttpClient.getConnectionManager().closeExpiredConnections();
			return mHttpClient.execute(httpRequest);
		} catch (IOException e) {
			httpRequest.abort();
			throw e;
		}
	}

	public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		if (DEBUG)
			LOG.log(Level.FINE, "creating HttpGet for: " + url);
		String query = URLEncodedUtils.format(stripNulls(nameValuePairs),
				HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url + "?" + query);
		httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		if (DEBUG)
			LOG.log(Level.FINE, "Created: " + httpGet.getURI());
		return httpGet;
	}

	public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
		if (DEBUG)
			LOG.log(Level.FINE, "creating HttpPost for: " + url);
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(
					stripNulls(nameValuePairs), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException(
					"Unable to encode http parameters.");
		}
		if (DEBUG)
			LOG.log(Level.FINE, "Created: " + httpPost);
		return httpPost;
	}

	private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < nameValuePairs.length; i++) {
			NameValuePair param = nameValuePairs[i];
			if (param.getValue() != null) {
				if (DEBUG)
					LOG.log(Level.FINE, "Param: " + param);
				params.add(param);
			}
		}
		return params;
	}

	/**
	 * Create a thread-safe client. This client does not do redirecting, to
	 * allow us to capture correct "error" codes.
	 * 
	 * @return HttpClient
	 */
	public static final DefaultHttpClient createHttpClient() {
		// Sets up the http part of the service.
		final SchemeRegistry supportedSchemes = new SchemeRegistry();

		// Register the "http" protocol scheme, it is required
		// by the default operator to look up socket factories.
		final SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));

		// Set some client http client parameter defaults.
		final HttpParams httpParams = createHttpParams();
		HttpClientParams.setRedirecting(httpParams, false);

		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(
				httpParams, supportedSchemes);
		return new DefaultHttpClient(ccm, httpParams);
	}

	/**
	 * Create the default HTTP protocol parameters.
	 */
	private static final HttpParams createHttpParams() {
		final HttpParams params = new BasicHttpParams();

		// Turn off stale checking. Our connections break all the time anyway,
		// and it's not worth it to pay the penalty of checking every time.
		HttpConnectionParams.setStaleCheckingEnabled(params, false);

		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		return params;
	}

}
