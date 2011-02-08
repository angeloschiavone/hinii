/**
 * Copyright 2009 Joe LaPenna
 */

package com.angelo.hiniid.rest.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.HiniiError;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.types.HiniiType;

/**
 * @author Joe LaPenna (joe@joelapenna.com)
 */
public abstract class AbstractParser<T extends HiniiType> implements Parser<T> {
	private static final Logger LOG = Logger.getLogger(AbstractParser.class
			.getCanonicalName());
	private static final boolean DEBUG = Hinii.PARSER_DEBUG;

	private static XmlPullParserFactory sFactory;
	static {
		try {
			sFactory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException e) {
			throw new IllegalStateException("Could not create a factory");
		}
	}

	abstract protected T parseInner(final XmlPullParser parser)
			throws IOException, XmlPullParserException, HiniiError,
			HiniiParseException;

	public final T parse(XmlPullParser parser) throws HiniiParseException,
			HiniiError {
		try {

			if (parser.getEventType() == XmlPullParser.START_DOCUMENT) {
				parser.nextTag();
				if (parser.getName().equals("error")) {
					throw new HiniiError(parser.nextText());
				}
			}
			return parseInner(parser);
		} catch (IOException e) {
			if (DEBUG)
				LOG.log(Level.FINE, "IOException", e);
			throw new HiniiParseException(e.getMessage());
		} catch (XmlPullParserException e) {
			if (DEBUG)
				LOG.log(Level.FINE, "XmlPullParserException", e);
			Log.e("XmlPullParserException", "throwable", e);
			throw new HiniiParseException(e.getMessage());
		}
	}

	public static final XmlPullParser createXmlPullParser(Reader is) {
		XmlPullParser parser;

		try {
			parser = sFactory.newPullParser();
			if (DEBUG) {
				StringBuffer sb = new StringBuffer();

				if (DEBUG) {
					while (true) {
						final int ch = is.read();
						if (ch < 0) {
							break;
						} else {
							sb.append((char) ch);
						}
					}
					is.close();
					LOG.log(Level.FINE, sb.toString());
				}
				for (int i = 0; i < 12; i++) {
					Log.e("sb.toString() " + i + " char ", ":" + sb.charAt(i));
				}

				Log.e("sb.toString()", sb.toString());
				while (sb.charAt(0) != '<')
					sb.deleteCharAt(0);
				parser.setInput(new StringReader(sb.toString()));
			} else {
				/*
				 * int mark = 0; while(is.read()!='<') mark++; is.mark(mark);
				 * is.reset();
				 */
				parser.setInput(is/* , null */);
			}
		} catch (XmlPullParserException e) {
			throw new IllegalArgumentException();
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
		return parser;
	}

	public static void skipSubTree(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, null, null);
		int level = 1;
		while (level > 0) {
			int eventType = parser.next();
			if (eventType == XmlPullParser.END_TAG) {
				--level;
			} else if (eventType == XmlPullParser.START_TAG) {
				++level;
			}
		}
	}

}
