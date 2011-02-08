package com.angelo.hiniid.rest.types;

import android.os.Parcel;
import android.os.Parcelable;

import com.angelo.hiniid.rest.util.ParcelUtils;

/**
 * Auto-generated: 2009-12-06 10:20:15.288167
 * 
 * @author Angelo
 */
public class HiniiRandomPointResult implements HiniiType, Parcelable {

	private String resultCode;
	private String resultMsg;
	private String id;
	private String name;
	private String country;
	private String city;
	private String address;
	private String latitude;
	private String longitude;
	private String telephone;
	private String email;
	private String website;
	private String geocoded;
	private String speciality;
	private String description;
	private String cost;
	private String costAvg;
	private String characeristic;
	private String timeAvg;
	private String indoorOutdoor;
	private String sponsor;
	private String currentCategory;
	private String currentCategoryValue;
	private String categoriesOtherIds;
	private String categoriesOtherValues;
	private String typology;
	private String typologyIds;
	private String dayKindIds;
	private String dayKindValues;
	private String companyKindIds;
	private String companyKindValues;
	private String seasonSuggestedIds;
	private String seasonSuggestedValues;
	private String suggestedDayPartIds;
	private String suggestedDayPartValues;
	private String closedLunch;
	private String closedDinner;
	private String closedMorning;
	private String closedAfternoon;
	private String closedNight;
	private String venue;
	private String subject;
	private String fromHour;
	private String toHour;
	// private HiniiCalendarDay calendarDays;
	private String repeat;
	// private HiniiRoute route;
	private String accomodationChain;
	private String rankingStars;
	private String tagCloud;
	private String source;
	private String idExt;
	private String sourceLink;
	private String image;
	private String imageOrig;
	private String voteAvg;
	private String voteNum;
	private String commentNum;
	private String stickersToWin;
	private String tokensToWin;
	private String missingFields;
	private String url;
	private String language;
	private String author;

	public HiniiRandomPointResult() {
	}

	private HiniiRandomPointResult(Parcel in) {

		resultCode = ParcelUtils.readStringFromParcel(in);
		resultMsg = ParcelUtils.readStringFromParcel(in);
		id = ParcelUtils.readStringFromParcel(in);
		name = ParcelUtils.readStringFromParcel(in);
		country = ParcelUtils.readStringFromParcel(in);
		city = ParcelUtils.readStringFromParcel(in);
		address = ParcelUtils.readStringFromParcel(in);
		latitude = ParcelUtils.readStringFromParcel(in);
		longitude = ParcelUtils.readStringFromParcel(in);
		telephone = ParcelUtils.readStringFromParcel(in);
		email = ParcelUtils.readStringFromParcel(in);
		website = ParcelUtils.readStringFromParcel(in);
		geocoded = ParcelUtils.readStringFromParcel(in);
		speciality = ParcelUtils.readStringFromParcel(in);
		description = ParcelUtils.readStringFromParcel(in);
		cost = ParcelUtils.readStringFromParcel(in);
		costAvg = ParcelUtils.readStringFromParcel(in);
		characeristic = ParcelUtils.readStringFromParcel(in);
		timeAvg = ParcelUtils.readStringFromParcel(in);
		indoorOutdoor = ParcelUtils.readStringFromParcel(in);
		sponsor = ParcelUtils.readStringFromParcel(in);
		currentCategory = ParcelUtils.readStringFromParcel(in);
		currentCategoryValue = ParcelUtils.readStringFromParcel(in);
		categoriesOtherIds = ParcelUtils.readStringFromParcel(in);
		categoriesOtherValues = ParcelUtils.readStringFromParcel(in);
		typology = ParcelUtils.readStringFromParcel(in);
		typologyIds = ParcelUtils.readStringFromParcel(in);
		dayKindIds = ParcelUtils.readStringFromParcel(in);
		dayKindValues = ParcelUtils.readStringFromParcel(in);
		companyKindIds = ParcelUtils.readStringFromParcel(in);
		companyKindValues = ParcelUtils.readStringFromParcel(in);
		seasonSuggestedIds = ParcelUtils.readStringFromParcel(in);
		seasonSuggestedValues = ParcelUtils.readStringFromParcel(in);
		suggestedDayPartIds = ParcelUtils.readStringFromParcel(in);
		suggestedDayPartValues = ParcelUtils.readStringFromParcel(in);
		closedLunch = ParcelUtils.readStringFromParcel(in);
		closedDinner = ParcelUtils.readStringFromParcel(in);
		closedMorning = ParcelUtils.readStringFromParcel(in);
		closedAfternoon = ParcelUtils.readStringFromParcel(in);
		closedNight = ParcelUtils.readStringFromParcel(in);
		venue = ParcelUtils.readStringFromParcel(in);
		subject = ParcelUtils.readStringFromParcel(in);
		fromHour = ParcelUtils.readStringFromParcel(in);
		toHour = ParcelUtils.readStringFromParcel(in);
		// private HiniiCalendarDay calendarDays=
		// ParcelUtils.readStringFromParcel(in);
		repeat = ParcelUtils.readStringFromParcel(in);
		// private HiniiRoute route= ParcelUtils.readStringFromParcel(in);
		accomodationChain = ParcelUtils.readStringFromParcel(in);
		rankingStars = ParcelUtils.readStringFromParcel(in);
		tagCloud = ParcelUtils.readStringFromParcel(in);
		source = ParcelUtils.readStringFromParcel(in);
		idExt = ParcelUtils.readStringFromParcel(in);
		sourceLink = ParcelUtils.readStringFromParcel(in);
		image = ParcelUtils.readStringFromParcel(in);
		imageOrig = ParcelUtils.readStringFromParcel(in);
		voteAvg = ParcelUtils.readStringFromParcel(in);
		voteNum = ParcelUtils.readStringFromParcel(in);
		commentNum = ParcelUtils.readStringFromParcel(in);
		stickersToWin = ParcelUtils.readStringFromParcel(in);
		tokensToWin = ParcelUtils.readStringFromParcel(in);
		missingFields = ParcelUtils.readStringFromParcel(in);
		url = ParcelUtils.readStringFromParcel(in);
		language = ParcelUtils.readStringFromParcel(in);
		author = ParcelUtils.readStringFromParcel(in);

	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param website
	 *            the website to set
	 */
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @return the website
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * @param geocoded
	 *            the geocoded to set
	 */
	public void setGeocoded(String geocoded) {
		this.geocoded = geocoded;
	}

	/**
	 * @return the geocoded
	 */
	public String getGeocoded() {
		return geocoded;
	}

	/**
	 * @param speciality
	 *            the speciality to set
	 */
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	/**
	 * @return the speciality
	 */
	public String getSpeciality() {
		return speciality;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}

	/**
	 * @return the cost
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * @param costAvg
	 *            the costAvg to set
	 */
	public void setCostAvg(String costAvg) {
		this.costAvg = costAvg;
	}

	/**
	 * @return the costAvg
	 */
	public String getCostAvg() {
		return costAvg;
	}

	/**
	 * @param characeristic
	 *            the characeristic to set
	 */
	public void setCharaceristic(String characeristic) {
		this.characeristic = characeristic;
	}

	/**
	 * @return the characeristic
	 */
	public String getCharaceristic() {
		return characeristic;
	}

	/**
	 * @param timeAvg
	 *            the timeAvg to set
	 */
	public void setTimeAvg(String timeAvg) {
		this.timeAvg = timeAvg;
	}

	/**
	 * @return the timeAvg
	 */
	public String getTimeAvg() {
		return timeAvg;
	}

	/**
	 * @param indoorOutdoor
	 *            the indoorOutdoor to set
	 */
	public void setIndoorOutdoor(String indoorOutdoor) {
		this.indoorOutdoor = indoorOutdoor;
	}

	/**
	 * @return the indoorOutdoor
	 */
	public String getIndoorOutdoor() {
		return indoorOutdoor;
	}

	/**
	 * @param sponsor
	 *            the sponsor to set
	 */
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	/**
	 * @return the sponsor
	 */
	public String getSponsor() {
		return sponsor;
	}

	/**
	 * @param currentCategory
	 *            the currentCategory to set
	 */
	public void setCurrentCategory(String currentCategory) {
		this.currentCategory = currentCategory;
	}

	/**
	 * @return the currentCategory
	 */
	public String getCurrentCategory() {
		return currentCategory;
	}

	/**
	 * @param currentCategoryValue
	 *            the currentCategoryValue to set
	 */
	public void setCurrentCategoryValue(String currentCategoryValue) {
		this.currentCategoryValue = currentCategoryValue;
	}

	/**
	 * @return the currentCategoryValue
	 */
	public String getCurrentCategoryValue() {
		return currentCategoryValue;
	}

	/**
	 * @param categoriesOtherIds
	 *            the categoriesOtherIds to set
	 */
	public void setCategoriesOtherIds(String categoriesOtherIds) {
		this.categoriesOtherIds = categoriesOtherIds;
	}

	/**
	 * @return the categoriesOtherIds
	 */
	public String getCategoriesOtherIds() {
		return categoriesOtherIds;
	}

	/**
	 * @param categoriesOtherValues
	 *            the categoriesOtherValues to set
	 */
	public void setCategoriesOtherValues(String categoriesOtherValues) {
		this.categoriesOtherValues = categoriesOtherValues;
	}

	/**
	 * @return the categoriesOtherValues
	 */
	public String getCategoriesOtherValues() {
		return categoriesOtherValues;
	}

	/**
	 * @param typology
	 *            the typology to set
	 */
	public void setTypology(String typology) {
		this.typology = typology;
	}

	/**
	 * @return the typology
	 */
	public String getTypology() {
		return typology;
	}

	/**
	 * @param typologyIds
	 *            the typologyIds to set
	 */
	public void setTypologyIds(String typologyIds) {
		this.typologyIds = typologyIds;
	}

	/**
	 * @return the typologyIds
	 */
	public String getTypologyIds() {
		return typologyIds;
	}

	/**
	 * @param dayKindIds
	 *            the dayKindIds to set
	 */
	public void setDayKindIds(String dayKindIds) {
		this.dayKindIds = dayKindIds;
	}

	/**
	 * @return the dayKindIds
	 */
	public String getDayKindIds() {
		return dayKindIds;
	}

	/**
	 * @param dayKindValues
	 *            the dayKindValues to set
	 */
	public void setDayKindValues(String dayKindValues) {
		this.dayKindValues = dayKindValues;
	}

	/**
	 * @return the dayKindValues
	 */
	public String getDayKindValues() {
		return dayKindValues;
	}

	/**
	 * @param companyKindIds
	 *            the companyKindIds to set
	 */
	public void setCompanyKindIds(String companyKindIds) {
		this.companyKindIds = companyKindIds;
	}

	/**
	 * @return the companyKindIds
	 */
	public String getCompanyKindIds() {
		return companyKindIds;
	}

	/**
	 * @param companyKindValues
	 *            the companyKindValues to set
	 */
	public void setCompanyKindValues(String companyKindValues) {
		this.companyKindValues = companyKindValues;
	}

	/**
	 * @return the companyKindValues
	 */
	public String getCompanyKindValues() {
		return companyKindValues;
	}

	/**
	 * @param seasonSuggestedIds
	 *            the seasonSuggestedIds to set
	 */
	public void setSeasonSuggestedIds(String seasonSuggestedIds) {
		this.seasonSuggestedIds = seasonSuggestedIds;
	}

	/**
	 * @return the seasonSuggestedIds
	 */
	public String getSeasonSuggestedIds() {
		return seasonSuggestedIds;
	}

	/**
	 * @param seasonSuggestedValues
	 *            the seasonSuggestedValues to set
	 */
	public void setSeasonSuggestedValues(String seasonSuggestedValues) {
		this.seasonSuggestedValues = seasonSuggestedValues;
	}

	/**
	 * @return the seasonSuggestedValues
	 */
	public String getSeasonSuggestedValues() {
		return seasonSuggestedValues;
	}

	/**
	 * @param suggestedDayPartIds
	 *            the suggestedDayPartIds to set
	 */
	public void setSuggestedDayPartIds(String suggestedDayPartIds) {
		this.suggestedDayPartIds = suggestedDayPartIds;
	}

	/**
	 * @return the suggestedDayPartIds
	 */
	public String getSuggestedDayPartIds() {
		return suggestedDayPartIds;
	}

	/**
	 * @param suggestedDayPartValues
	 *            the suggestedDayPartValues to set
	 */
	public void setSuggestedDayPartValues(String suggestedDayPartValues) {
		this.suggestedDayPartValues = suggestedDayPartValues;
	}

	/**
	 * @return the suggestedDayPartValues
	 */
	public String getSuggestedDayPartValues() {
		return suggestedDayPartValues;
	}

	/**
	 * @param closedLunch
	 *            the closedLunch to set
	 */
	public void setClosedLunch(String closedLunch) {
		this.closedLunch = closedLunch;
	}

	/**
	 * @return the closedLunch
	 */
	public String getClosedLunch() {
		return closedLunch;
	}

	/**
	 * @param closedDinner
	 *            the closedDinner to set
	 */
	public void setClosedDinner(String closedDinner) {
		this.closedDinner = closedDinner;
	}

	/**
	 * @return the closedDinner
	 */
	public String getClosedDinner() {
		return closedDinner;
	}

	/**
	 * @param closedMorning
	 *            the closedMorning to set
	 */
	public void setClosedMorning(String closedMorning) {
		this.closedMorning = closedMorning;
	}

	/**
	 * @return the closedMorning
	 */
	public String getClosedMorning() {
		return closedMorning;
	}

	/**
	 * @param closedAfternoon
	 *            the closedAfternoon to set
	 */
	public void setClosedAfternoon(String closedAfternoon) {
		this.closedAfternoon = closedAfternoon;
	}

	/**
	 * @return the closedAfternoon
	 */
	public String getClosedAfternoon() {
		return closedAfternoon;
	}

	/**
	 * @param closedNight
	 *            the closedNight to set
	 */
	public void setClosedNight(String closedNight) {
		this.closedNight = closedNight;
	}

	/**
	 * @return the closedNight
	 */
	public String getClosedNight() {
		return closedNight;
	}

	/**
	 * @param venue
	 *            the venue to set
	 */
	public void setVenue(String venue) {
		this.venue = venue;
	}

	/**
	 * @return the venue
	 */
	public String getVenue() {
		return venue;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param fromHour
	 *            the fromHour to set
	 */
	public void setFromHour(String fromHour) {
		this.fromHour = fromHour;
	}

	/**
	 * @return the fromHour
	 */
	public String getFromHour() {
		return fromHour;
	}

	/**
	 * @param toHour
	 *            the toHour to set
	 */
	public void setToHour(String toHour) {
		this.toHour = toHour;
	}

	/**
	 * @return the toHour
	 */
	public String getToHour() {
		return toHour;
	}

	/**
	 * @param repeat
	 *            the repeat to set
	 */
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	/**
	 * @return the repeat
	 */
	public String getRepeat() {
		return repeat;
	}

	/**
	 * @param accomodationChain
	 *            the accomodationChain to set
	 */
	public void setAccomodationChain(String accomodationChain) {
		this.accomodationChain = accomodationChain;
	}

	/**
	 * @return the accomodationChain
	 */
	public String getAccomodationChain() {
		return accomodationChain;
	}

	/**
	 * @param rankingStars
	 *            the rankingStars to set
	 */
	public void setRankingStars(String rankingStars) {
		this.rankingStars = rankingStars;
	}

	/**
	 * @return the rankingStars
	 */
	public String getRankingStars() {
		return rankingStars;
	}

	/**
	 * @param tagCloud
	 *            the tagCloud to set
	 */
	public void setTagCloud(String tagCloud) {
		this.tagCloud = tagCloud;
	}

	/**
	 * @return the tagCloud
	 */
	public String getTagCloud() {
		return tagCloud;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param idExt
	 *            the idExt to set
	 */
	public void setIdExt(String idExt) {
		this.idExt = idExt;
	}

	/**
	 * @return the idExt
	 */
	public String getIdExt() {
		return idExt;
	}

	/**
	 * @param sourceLink
	 *            the sourceLink to set
	 */
	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}

	/**
	 * @return the sourceLink
	 */
	public String getSourceLink() {
		return sourceLink;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param imageOrig
	 *            the imageOrig to set
	 */
	public void setImageOrig(String imageOrig) {
		this.imageOrig = imageOrig;
	}

	/**
	 * @return the imageOrig
	 */
	public String getImageOrig() {
		return imageOrig;
	}

	/**
	 * @param voteAvg
	 *            the voteAvg to set
	 */
	public void setVoteAvg(String voteAvg) {
		this.voteAvg = voteAvg;
	}

	/**
	 * @return the voteAvg
	 */
	public String getVoteAvg() {
		return voteAvg;
	}

	/**
	 * @param voteNum
	 *            the voteNum to set
	 */
	public void setVoteNum(String voteNum) {
		this.voteNum = voteNum;
	}

	/**
	 * @return the voteNum
	 */
	public String getVoteNum() {
		return voteNum;
	}

	/**
	 * @param commentNum
	 *            the commentNum to set
	 */
	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	/**
	 * @return the commentNum
	 */
	public String getCommentNum() {
		return commentNum;
	}

	/**
	 * @param stickersToWin
	 *            the stickersToWin to set
	 */
	public void setStickersToWin(String stickersToWin) {
		this.stickersToWin = stickersToWin;
	}

	/**
	 * @return the stickersToWin
	 */
	public String getStickersToWin() {
		return stickersToWin;
	}

	/**
	 * @param tokensToWin
	 *            the tokensToWin to set
	 */
	public void setTokensToWin(String tokensToWin) {
		this.tokensToWin = tokensToWin;
	}

	/**
	 * @return the tokensToWin
	 */
	public String getTokensToWin() {
		return tokensToWin;
	}

	/**
	 * @param missingFields
	 *            the missingFields to set
	 */
	public void setMissingFields(String missingFields) {
		this.missingFields = missingFields;
	}

	/**
	 * @return the missingFields
	 */
	public String getMissingFields() {
		return missingFields;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {

		ParcelUtils.writeStringToParcel(out, resultCode);
		ParcelUtils.writeStringToParcel(out, resultMsg);
		ParcelUtils.writeStringToParcel(out, id);
		ParcelUtils.writeStringToParcel(out, name);
		ParcelUtils.writeStringToParcel(out, country);
		ParcelUtils.writeStringToParcel(out, city);
		ParcelUtils.writeStringToParcel(out, address);
		ParcelUtils.writeStringToParcel(out, latitude);
		ParcelUtils.writeStringToParcel(out, longitude);
		ParcelUtils.writeStringToParcel(out, telephone);
		ParcelUtils.writeStringToParcel(out, email);
		ParcelUtils.writeStringToParcel(out, website);
		ParcelUtils.writeStringToParcel(out, geocoded);
		ParcelUtils.writeStringToParcel(out, speciality);
		ParcelUtils.writeStringToParcel(out, description);
		ParcelUtils.writeStringToParcel(out, cost);
		ParcelUtils.writeStringToParcel(out, costAvg);
		ParcelUtils.writeStringToParcel(out, characeristic);
		ParcelUtils.writeStringToParcel(out, timeAvg);
		ParcelUtils.writeStringToParcel(out, indoorOutdoor);
		ParcelUtils.writeStringToParcel(out, sponsor);
		ParcelUtils.writeStringToParcel(out, currentCategory);
		ParcelUtils.writeStringToParcel(out, currentCategoryValue);
		ParcelUtils.writeStringToParcel(out, categoriesOtherIds);
		ParcelUtils.writeStringToParcel(out, categoriesOtherValues);
		ParcelUtils.writeStringToParcel(out, typology);
		ParcelUtils.writeStringToParcel(out, typologyIds);
		ParcelUtils.writeStringToParcel(out, dayKindIds);
		ParcelUtils.writeStringToParcel(out, dayKindValues);
		ParcelUtils.writeStringToParcel(out, companyKindIds);
		ParcelUtils.writeStringToParcel(out, companyKindValues);
		ParcelUtils.writeStringToParcel(out, seasonSuggestedIds);
		ParcelUtils.writeStringToParcel(out, seasonSuggestedValues);
		ParcelUtils.writeStringToParcel(out, suggestedDayPartIds);
		ParcelUtils.writeStringToParcel(out, suggestedDayPartValues);
		ParcelUtils.writeStringToParcel(out, closedLunch);
		ParcelUtils.writeStringToParcel(out, closedDinner);
		ParcelUtils.writeStringToParcel(out, closedMorning);
		ParcelUtils.writeStringToParcel(out, closedAfternoon);
		ParcelUtils.writeStringToParcel(out, closedNight);
		ParcelUtils.writeStringToParcel(out, venue);
		ParcelUtils.writeStringToParcel(out, subject);
		ParcelUtils.writeStringToParcel(out, fromHour);
		ParcelUtils.writeStringToParcel(out, toHour);
		// private HiniiCalendarDay calendarDays);
		ParcelUtils.writeStringToParcel(out, repeat);
		// private HiniiRoute route);
		ParcelUtils.writeStringToParcel(out, accomodationChain);
		ParcelUtils.writeStringToParcel(out, rankingStars);
		ParcelUtils.writeStringToParcel(out, tagCloud);
		ParcelUtils.writeStringToParcel(out, source);
		ParcelUtils.writeStringToParcel(out, idExt);
		ParcelUtils.writeStringToParcel(out, sourceLink);
		ParcelUtils.writeStringToParcel(out, image);
		ParcelUtils.writeStringToParcel(out, imageOrig);
		ParcelUtils.writeStringToParcel(out, voteAvg);
		ParcelUtils.writeStringToParcel(out, voteNum);
		ParcelUtils.writeStringToParcel(out, commentNum);
		ParcelUtils.writeStringToParcel(out, stickersToWin);
		ParcelUtils.writeStringToParcel(out, tokensToWin);
		ParcelUtils.writeStringToParcel(out, missingFields);
		ParcelUtils.writeStringToParcel(out, url);
		ParcelUtils.writeStringToParcel(out, language);
		ParcelUtils.writeStringToParcel(out, author);

	}

}
