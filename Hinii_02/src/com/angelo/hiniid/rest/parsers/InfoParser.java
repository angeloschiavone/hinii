package com.angelo.hiniid.rest.parsers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.HiniiError;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.types.Info;

/**
 * <info> <search_id>11296</search_id>
 * <search_url>http://alpha.hinii.com/en/search/view/id/11296</search_url>
 * <company_kind>2</company_kind> <day_kind>0</day_kind> <day_part>1</day_part>
 * <city>Torino,Italia</city> <latitude>45.070560</latitude>
 * <longitude>7.686619</longitude> <accomodation>0</accomodation>
 * <day_week>3</day_week> <start_time>15:30:00</start_time>
 * <search_author>0</search_author> <ts_creation>2010-06-17
 * 10:14:47</ts_creation> </info>
 */
public class InfoParser extends AbstractParser<Info> {
	private static final Logger LOG = Logger.getLogger(InfoParser.class
			.getCanonicalName());
	private static final boolean DEBUG = Hinii.PARSER_DEBUG;

	@Override
	public Info parseInner(XmlPullParser parser) throws XmlPullParserException,
			IOException, HiniiError, HiniiParseException {
		parser.require(XmlPullParser.START_TAG, null, null);

		Info info = new Info();

		while (parser.nextTag() == XmlPullParser.START_TAG) {
			String name = parser.getName();
			if ("search_id".equals(name)) {
				info.setSearchId(parser.nextText());

			} else if ("search_url".equals(name)) {
				info.setSearchUrl(parser.nextText());

			} else if ("company_kind".equals(name)) {
				info.setCompanyKind(parser.nextText());

			} else if ("day_kind".equals(name)) {
				info.setDayKind(parser.nextText());

			} else if ("day_part".equals(name)) {
				info.setDayPart(parser.nextText());

			} else if ("city".equals(name)) {
				info.setCity(parser.nextText());

			} else if ("latitude".equals(name)) {
				info.setLatitude(parser.nextText());

			} else if ("longitude".equals(name)) {
				info.setLongitude(parser.nextText());

			} else if ("accomodation".equals(name)) {
				info.setAccomodation(parser.nextText());

			} else if ("day_week".equals(name)) {
				info.setDayWeek(parser.nextText());

			} else if ("start_time".equals(name)) {
				info.setStartTime(parser.nextText());

			} else if ("search_author".equals(name)) {
				info.setSearchAuthor(parser.nextText());

			} else if ("ts_creation".equals(name)) {
				info.setTsCreation(parser.nextText());

			} else {
				// Consume something we don't understand.
				if (DEBUG)
					LOG.log(Level.FINE, "Found tag that we don't recognize: "
							+ name);
				skipSubTree(parser);
			}
		}
		return info;
	}
}
