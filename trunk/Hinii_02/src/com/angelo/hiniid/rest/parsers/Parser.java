package com.angelo.hiniid.rest.parsers;

import com.angelo.hiniid.rest.error.HiniiError;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.types.HiniiType;

import org.xmlpull.v1.XmlPullParser;

/**
 * @author Angelo Schiavone
 */
public interface Parser<T extends HiniiType> {

	public abstract T parse(XmlPullParser parser) throws HiniiError,
			HiniiParseException;

}
