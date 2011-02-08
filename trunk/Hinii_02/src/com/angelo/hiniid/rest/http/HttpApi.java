package com.angelo.hiniid.rest.http;

import com.angelo.hiniid.rest.error.HiniiException;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.parsers.Parser;
import com.angelo.hiniid.rest.types.HiniiType;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;

/**
 * @author Angelo Schiavone
 */
public interface HttpApi {

	abstract public HiniiType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends HiniiType> parser) throws HiniiParseException,
			HiniiException, IOException;

	abstract public String doHttpPost(String url,
			NameValuePair... nameValuePairs) throws HiniiParseException,
			HiniiException, IOException;

	abstract public HttpGet createHttpGet(String url,
			NameValuePair... nameValuePairs);

	abstract public HttpPost createHttpPost(String url,
			NameValuePair... nameValuePairs);
}
