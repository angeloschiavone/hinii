package com.angelo.hiniid.rest.http;

import java.io.IOException;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import com.angelo.hiniid.rest.error.HiniiException;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.parsers.Parser;
import com.angelo.hiniid.rest.types.HiniiType;

/**
 * @author angelo
 */
public class HttpApiWithoutAuth extends AbstractHttpApi {

	public HttpApiWithoutAuth(DefaultHttpClient httpClient, String clientVersion) {
		super(httpClient, clientVersion);
	}

	public HiniiType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends HiniiType> parser) throws HiniiParseException,
			HiniiException, IOException {
		return executeHttpRequest(httpRequest, parser);
	}
}
