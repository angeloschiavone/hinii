package com.angelo.hiniid.rest.parsers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.HiniiError;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.types.PointValue;

/**
 * <value> <step>0</step> <duration>100</duration> <sponsor>0</sponsor>
 * <id_category>3</id_category> <point_id>110249</point_id> <point_name>Giro a
 * Santa Rita</point_name> <point_country>Italia</point_country>
 * <point_city>Torino</point_city> <point_address></point_address>
 * <point_lat>45.070560</point_lat> <point_lng>7.686619</point_lng>
 * <image>http:/
 * /alpha.hinii.com/img/typologies/point_example_tipology_23.jpg</image>
 * <point_typology>Shopping, Typical</point_typology> <day_part>1</day_part>
 * <print_day_part>1</print_day_part>
 * <point_url>http://alpha.hinii.com/en/point/
 * view/id/110249/category/3</point_url> </value>
 */
public class PointValueParser extends AbstractParser<PointValue> {
	private static final Logger LOG = Logger.getLogger(PointValueParser.class
			.getCanonicalName());
	private static final boolean DEBUG = Hinii.PARSER_DEBUG;

	@Override
	public PointValue parseInner(XmlPullParser parser)
			throws XmlPullParserException, IOException, HiniiError,
			HiniiParseException {
		parser.require(XmlPullParser.START_TAG, null, null);

		PointValue pointValue = new PointValue();

		while (parser.nextTag() == XmlPullParser.START_TAG) {
			String name = parser.getName();
			if ("step".equals(name)) {
				pointValue.setStep(parser.nextText());
			} else if ("duration".equals(name)) {
				pointValue.setDuration(parser.nextText());
			} else if ("sponsor".equals(name)) {
				pointValue.setSponsor(parser.nextText());

			} else if ("id_category".equals(name)) {
				pointValue.setIdCategory(parser.nextText());
			} else if ("point_id".equals(name)) {
				pointValue.setPointId(parser.nextText());
			} else if ("point_name".equals(name)) {
				pointValue.setPointName(parser.nextText());

			} else if ("point_country".equals(name)) {
				pointValue.setPointCountry(parser.nextText());

			} else if ("point_city".equals(name)) {
				pointValue.setPointCity(parser.nextText());

			} else if ("point_address".equals(name)) {
				pointValue.setPointAddress(parser.nextText());

			} else if ("point_lat".equals(name)) {
				pointValue.setPointLat(parser.nextText());
			} else if ("point_lng".equals(name)) {
				pointValue.setPointLng(parser.nextText());
			} else if ("image".equals(name)) {
				pointValue.setImage(parser.nextText());
			} else if ("point_typology".equals(name)) {
				pointValue.setPointTypology(parser.nextText());
			} else if ("day_part".equals(name)) {
				pointValue.setDayPart(parser.nextText());
			} else if ("print_day_part".equals(name)) {
				pointValue.setPrintDayPart(parser.nextText());
			} else if ("point_url".equals(name)) {
				pointValue.setPointUrl(parser.nextText());
			}

			else {
				// Consume something we don't understand.
				if (DEBUG)
					LOG.log(Level.FINE, "Found tag that we don't recognize: "
							+ name);
				skipSubTree(parser);
			}
		}
		return pointValue;
	}
}
