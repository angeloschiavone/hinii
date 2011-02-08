package com.angelo.hiniid.rest.parsers;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.HiniiError;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.http.HttpApiWithoutAuth;
import com.angelo.hiniid.rest.types.Group;
import com.angelo.hiniid.rest.types.HiniiType;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Angelo Schiavone
 */
public class GroupParser extends AbstractParser<Group> {
	private static final Logger LOG = Logger.getLogger(GroupParser.class
			.getCanonicalName());
	private static final boolean DEBUG = Hinii.PARSER_DEBUG;

	private Parser<? extends HiniiType> mSubParser;

	public GroupParser(Parser<? extends HiniiType> subParser) {
		this.mSubParser = subParser;
	}

	@Override
	public Group<HiniiType> parseInner(XmlPullParser parser)
			throws XmlPullParserException, IOException, HiniiParseException,
			HiniiError {

		Group<HiniiType> group = new Group<HiniiType>();
		group.setType(parser.getAttributeValue(null, "type"));

		while (parser.nextTag() == XmlPullParser.START_TAG) {
			HiniiType item = this.mSubParser.parse(parser);
			if (DEBUG)
				LOG.log(Level.FINE, "adding item: " + item);
			group.add(item);
		}
		return group;
	}
}
