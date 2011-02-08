package com.angelo.hiniid.rest.parsers;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.HiniiError;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.types.SearchResult;

/*
 "http://alpha.hinii.com/en/api/search/company_kind/2/day_kind/0/"+
 "day_part/1/city/Milano%2cItalia/lat/45.4419/lng/9.1635/" +
 "day_week_code/3/distance/2/"

 <root>
 <result_code>0</result_code>
 <result_msg>Ok</result_msg>
 <info>
 <search_id>11300</search_id>
 <search_url>http://alpha.hinii.com/en/search/view/id/11300</search_url>
 <company_kind>2</company_kind>
 <day_kind>0</day_kind>
 <day_part>1</day_part>
 <city>Milano,Italia</city>
 <latitude>45.441898</latitude>
 <longitude>9.163500</longitude>
 <accomodation>0</accomodation>
 <day_week>3</day_week>
 <start_time>15:30:00</start_time>
 <search_author>0</search_author>
 <ts_creation>2010-06-17 11:35:04</ts_creation>
 </info>
 <iter>
 <value>
 <step>0</step>
 <duration>100</duration>
 <sponsor>0</sponsor>
 <id_category>3</id_category>
 <point_id></point_id>
 <point_name></point_name>
 <point_country></point_country>
 <point_city></point_city>
 <point_address></point_address>
 <point_lat></point_lat>
 <point_lng></point_lng>
 <image></image>
 <point_typology></point_typology>
 <day_part></day_part>
 <print_day_part></print_day_part>
 <point_url></point_url>
 </value>
 <value>
 <step>1</step>
 <duration>130</duration>
 <sponsor>0</sponsor>
 <id_category>0</id_category>
 <point_id>168324</point_id>
 */
public class SearchResultParser extends AbstractParser<SearchResult> {
	private static final Logger LOG = Logger.getLogger(SearchResultParser.class
			.getCanonicalName());
	private static final boolean DEBUG = Hinii.PARSER_DEBUG;

	@Override
	public SearchResult parseInner(XmlPullParser parser)
			throws XmlPullParserException, IOException, HiniiError,
			HiniiParseException {
		parser.require(XmlPullParser.START_TAG, null, null);

		SearchResult searchResult = new SearchResult();

		while (parser.nextTag() == XmlPullParser.START_TAG) {
			String name = parser.getName();

			Log.e("SearchResultParser", "name" + name);

			if ("result_code".equals(name)) {
				searchResult.setResultCode(parser.nextText());
			} else if ("result_msg".equals(name)) {
				searchResult.setResultMsg(parser.nextText());
			} else if ("info".equals(name)) {
				searchResult.setInfo(new InfoParser().parse(parser));
			} else if ("iter".equals(name)) {
				searchResult.setIter(new GroupParser(new PointValueParser())
						.parse(parser));
			} else {
				// Consume something we don't understand.
				if (DEBUG)
					LOG.log(Level.FINE, "Found tag that we don't recognize: "
							+ name);
				skipSubTree(parser);
			}
		}

		for (int i = searchResult.getIter().size() - 1; i >= 0; i--) {
			if (searchResult.getIter().get(i).getPointId().equals(""))
				searchResult.getIter().remove(searchResult.getIter().get(i));
		}

		return searchResult;
	}
}
