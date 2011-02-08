package com.angelo.hiniid.rest.parsers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.angelo.hiniid.rest.Hinii;
import com.angelo.hiniid.rest.error.HiniiError;
import com.angelo.hiniid.rest.error.HiniiParseException;
import com.angelo.hiniid.rest.types.HiniiRandomPointResult;

/*
 http://hinii.com/en/api/pointview/id/12/
 Result explanation
 <?xml version="1.0" encoding="UTF-8"?>
 <root>
 // Result code and message*, to understand if some error happened
 <result_code>0</result_code>
 <result_msg>Ok</result_msg>
 // Point's id
 <id>53195</id>
 // Point's information
 <name>wine bar Frank</name>
 <country>Italy</country>
 <city>Roma</city>
 <address>Via emilia 1</address>
 <latitude>41.907478</latitude>
 <longitude>12.488610</longitude>
 <telephone>12248012121</telephone>
 <email>info@hinii.com</email>
 <website>www.abcwebsite.com</website>
 <latitude>41.907478</latitude>
 <longitude>12.488610</longitude>
 // The address is geocoded by google or not? (1 = yes, 0 = filled by users)
 // If so, the address can be not fully correct, such as the civic number near
 <geocoded>0</geocoded>
 <speciality>Black Chocolate</speciality>
 <description>National museum...</description>
 // Cost avarage (From 1 to 5), If 0 is not defined
 <cost>3</cost>
 // Cost avg (ex. 15$, currency depends on point's country)
 <cost_avg>15</cost_avg>
 // Characteristic index (From 1 to 5), If 0 is not defined
 <characeristic>3</characeristic>
 // Avarage of time spent in this point (minutes)
 <time_avg>25</time_avg>
 // Indoor or outdoor, or both?*
 <indoor_outdoor>2</indoor_outdoor>
 // Is this a Sponsor point or not?
 <sponsor>0</sponsor>
 // Current category id
 <current_category>2</current_category>
 // Current category name
 <current_category_value>Bars&Spots</current_category_value>
 // Other categories ids, comma separated
 <categories_other_ids>1</categories_other_ids>
 // Other categories names, comma separated
 <categories_other_values>Restaurant</categories_other_values>
 // Point's typologies, comma separated
 <typology>Bar Cafe</typology>
 // Point's typologies ids, comma separated
 <typology_ids>47, 52</typology_ids>
 // Suggested day kinds ids*, comma separated
 <day_kind_ids>0, 3, 5</day_kind_ids>
 // Suggested day kinds names*, comma separated
 <day_kind_values>Fun, Sports, Normal</day_kind_values>
 // Suggested company kinds ids*, comma separated
 <company_kind_ids>0, 1, 2</company_kind_ids>
 // Suggested company kinds names*, comma separated
 <company_kind_values>Alone, Couple, Friends</company_kind_values>
 // Suggested season ids, comma separated
 <season_suggested_ids>0</season_suggested_ids>
 // Suggested season names, comma separated
 <season_suggested_values>Winter</season_suggested_values>
 // Suggested day part ids*, comma separated
 <suggested_day_part_ids>0, 1</suggested_day_part_ids>
 // Suggested day part names*, comma separated
 <suggested_day_part_values>Morning, Afternoon</suggested_day_part_values>
 // Day ids when the Point is closed at lunch*, comma separated
 <closed_lunch>0, 1, 2, 3, 4, 5, 6</closed_lunch>
 // Day ids when the Point is closed at dinner*, comma separated
 <closed_dinner>0, 1</closed_dinner>
 // Day ids when the Point is closed in the morning*, comma separated
 <closed_morning>0, 1, 2, 3, 4, 5, 6</closed_morning>
 // Day ids when the Point is closed in the afternoon*, comma separated
 <closed_afternoon>0, 1</closed_afternoon>
 // Day ids when the Point is closed in the night*, comma separated
 <closed_night>0, 1</closed_night>
 // Venue name of the place where the Event take place (just Event category)
 <venue>San Siro</venue>
 // Subject of the event
 <subject>Chocolate</subject>
 // Event start time (just Event category)
 <from_hour>11:00:00</from_hour>
 // Event end time (just Event category)
 <to_hour>22:30:00</to_hour>
 // Days where the event take place (just Event category)
 <calendar_days>
 <value>2010-06-25</value>
 <value>2010-06-26</value>
 </calendar_days>
 // The Events repeat periodically?*
 <repeat>2</repeat>
 // Just in Walks, contain the steps of the walk
 <route>
 // Pair of coordinates
 <value>
 <lat>45.076370</lat>
 <lng>7.670238</lng>
 </value>
 ...
 <route>
 // Accomodation chain, just for Accomodation category
 <accomodation_chain>Hilton hotels</accomodation_chain>
 // Ranking stars for Hotels quality, just for Accomodation category
 <ranking_stars>5</ranking_stars>
 // Tags, comma separated
 <tag_cloud>tasty drinks, good beds</tag_cloud>
 // Source Code*
 <source>1</source>
 // Id on the external service
 <id_ext>191250</id_ext>
 // Link to the original spot report
 <source_link>http://gowalla.com/spots/191250</source_link>
 // This point is a Sponsor?
 <sponsor>0</sponsor>
 // Image of the point. Can also be an example image of one of the typologies
 (ussually 60x60)
 <image>http://mw2.google.com/mw-panoramio/photos/square/485276.jpg</image>
 // Original image, not scaled
 <image_orig>http://mw2.google.com/mw-panoramio/photos/medium/485276.jpg</image_orig>
 // Avarage of user's vote
 <vote_avg>5</vote_avg>
 // Number of votes
 <vote_num>2</vote_num>
 // Number of comments
 <comment_num>3</comment_num>
 // Path of the stickers to win, adding informations to that point, comma
 separated
 <stickers_to_win>
 http://hinii/img/stickers/100/sticker_1_0.png,
 */
public class HiniiRandomPointParser extends
		AbstractParser<HiniiRandomPointResult> {
	private static final Logger LOG = Logger
			.getLogger(HiniiRandomPointParser.class.getCanonicalName());
	private static final boolean DEBUG = Hinii.PARSER_DEBUG;

	@Override
	public HiniiRandomPointResult parseInner(XmlPullParser parser)
			throws XmlPullParserException, IOException, HiniiError,
			HiniiParseException {
		parser.require(XmlPullParser.START_TAG, null, null);

		HiniiRandomPointResult randomPoint = new HiniiRandomPointResult();

		// @TODO do parse also calendarDays and route;

		while (parser.nextTag() == XmlPullParser.START_TAG) {
			String name = parser.getName();
			if ("result_code".equals(name)) {
				randomPoint.setResultCode(parser.nextText());
			} else if ("result_msg".equals(name)) {
				randomPoint.setResultMsg(parser.nextText());
			} else if ("id".equals(name)) {
				randomPoint.setId(parser.nextText());
			} else if ("name".equals(name)) {
				randomPoint.setName(parser.nextText());
			} else if ("country".equals(name)) {
				randomPoint.setCountry(parser.nextText());
			} else if ("city".equals(name)) {
				randomPoint.setCity(parser.nextText());
			} else if ("address".equals(name)) {
				randomPoint.setAddress(parser.nextText());
			} else if ("latitude".equals(name)) {
				randomPoint.setLatitude(parser.nextText());
			} else if ("longitude".equals(name)) {
				randomPoint.setLongitude(parser.nextText());
			} else if ("telephone".equals(name)) {
				randomPoint.setTelephone(parser.nextText());
			} else if ("email".equals(name)) {
				randomPoint.setEmail(parser.nextText());
			} else if ("website".equals(name)) {
				randomPoint.setWebsite(parser.nextText());
			} else if ("geocoded".equals(name)) {
				randomPoint.setGeocoded(parser.nextText());
			} else if ("speciality".equals(name)) {
				randomPoint.setSpeciality(parser.nextText());
			} else if ("description".equals(name)) {
				randomPoint.setDescription(parser.nextText());
			} else if ("cost".equals(name)) {
				randomPoint.setCost(parser.nextText());
			} else if ("cost_avg".equals(name)) {
				randomPoint.setCostAvg(parser.nextText());
			} else if ("characeristic".equals(name)) {
				randomPoint.setCharaceristic(parser.nextText());
			} else if ("time_avg".equals(name)) {
				randomPoint.setTimeAvg(parser.nextText());
			} else if ("indoor_outdoor".equals(name)) {
				randomPoint.setIndoorOutdoor(parser.nextText());
			} else if ("sponsor".equals(name)) {
				randomPoint.setSponsor(parser.nextText());
			} else if ("current_category".equals(name)) {
				randomPoint.setCurrentCategory(parser.nextText());
			} else if ("current_category_value".equals(name)) {
				randomPoint.setCurrentCategoryValue(parser.nextText());
			} else if ("categories_other_ids".equals(name)) {
				randomPoint.setCategoriesOtherIds(parser.nextText());
			} else if ("categories_other_values".equals(name)) {
				randomPoint.setCategoriesOtherValues(parser.nextText());
			} else if ("typology".equals(name)) {
				randomPoint.setTypology(parser.nextText());
			} else if ("typology_ids".equals(name)) {
				randomPoint.setTypologyIds(parser.nextText());
			} else if ("day_kind_ids".equals(name)) {
				randomPoint.setDayKindIds(parser.nextText());
			} else if ("day_kind_values".equals(name)) {
				randomPoint.setDayKindValues(parser.nextText());
			} else if ("company_kind_ids".equals(name)) {
				randomPoint.setCompanyKindIds(parser.nextText());
			} else if ("company_kind_values".equals(name)) {
				randomPoint.setCompanyKindValues(parser.nextText());
			} else if ("season_suggested_ids".equals(name)) {
				randomPoint.setSeasonSuggestedIds(parser.nextText());
			} else if ("season_suggested_values".equals(name)) {
				randomPoint.setSeasonSuggestedValues(parser.nextText());
			} else if ("suggested_day_part_ids".equals(name)) {
				randomPoint.setSuggestedDayPartIds(parser.nextText());
			} else if ("suggested_day_part_values".equals(name)) {
				randomPoint.setSuggestedDayPartValues(parser.nextText());
			} else if ("closed_lunch".equals(name)) {
				randomPoint.setClosedLunch(parser.nextText());
			} else if ("closed_dinner".equals(name)) {
				randomPoint.setClosedDinner(parser.nextText());
			} else if ("closed_morning".equals(name)) {
				randomPoint.setClosedMorning(parser.nextText());
			} else if ("closed_afternoon".equals(name)) {
				randomPoint.setClosedAfternoon(parser.nextText());
			} else if ("closed_night".equals(name)) {
				randomPoint.setClosedNight(parser.nextText());
			} else if ("venue".equals(name)) {
				randomPoint.setVenue(parser.nextText());
			} else if ("subject".equals(name)) {
				randomPoint.setSubject(parser.nextText());
			} else if ("from_hour".equals(name)) {
				randomPoint.setFromHour(parser.nextText());
			} else if ("to_hour".equals(name)) {
				randomPoint.setToHour(parser.nextText());
			} else if ("repeat".equals(name)) {
				randomPoint.setRepeat(parser.nextText());
			} else if ("accomodation_chain".equals(name)) {
				randomPoint.setAccomodationChain(parser.nextText());
			} else if ("ranking_stars".equals(name)) {
				randomPoint.setRankingStars(parser.nextText());
			} else if ("tag_cloud".equals(name)) {
				randomPoint.setTagCloud(parser.nextText());
			} else if ("source".equals(name)) {
				randomPoint.setSource(parser.nextText());
			} else if ("id_ext".equals(name)) {
				randomPoint.setIdExt(parser.nextText());
			} else if ("source_link".equals(name)) {
				randomPoint.setSourceLink(parser.nextText());
			} else if ("image".equals(name)) {
				randomPoint.setImage(parser.nextText());
			} else if ("image_orig".equals(name)) {
				randomPoint.setImageOrig(parser.nextText());
			} else if ("vote_avg".equals(name)) {
				randomPoint.setVoteAvg(parser.nextText());
			} else if ("vote_num".equals(name)) {
				randomPoint.setVoteNum(parser.nextText());
			} else if ("comment_num".equals(name)) {
				randomPoint.setCommentNum(parser.nextText());
			} else if ("stickers_to_win".equals(name)) {
				randomPoint.setStickersToWin(parser.nextText());
			} else if ("tokens_to_win".equals(name)) {
				randomPoint.setTokensToWin(parser.nextText());
			} else if ("missing_fields".equals(name)) {
				randomPoint.setMissingFields(parser.nextText());
			} else if ("url".equals(name)) {
				randomPoint.setUrl(parser.nextText());
			} else if ("language".equals(name)) {
				randomPoint.setLanguage(parser.nextText());
			} else if ("author".equals(name)) {
				randomPoint.setAuthor(parser.nextText());
			} else {
				// Consume something we don't understand.
				if (DEBUG)
					LOG.log(Level.FINE, "Found tag that we don't recognize: "
							+ name);
				skipSubTree(parser);
			}
		}
		return randomPoint;
	}
}
