package com.angelo.hiniid.rest.types;

import com.angelo.hiniid.rest.util.ParcelUtils;

import android.os.Parcel;
import android.os.Parcelable;

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
public class Info implements HiniiType, Parcelable {

	private String searchId;
	private String searchUrl;
	private String companyKind;
	private String dayKind;
	private String dayPart;
	private String city;
	private String latitude;
	private String longitude;
	private String accomodation;
	private String dayWeek;
	private String startTime;
	private String searchAuthor;
	private String tsCreation;

	public Info() {
	}

	private Info(Parcel in) {

		searchId = ParcelUtils.readStringFromParcel(in);
		searchUrl = ParcelUtils.readStringFromParcel(in);
		companyKind = ParcelUtils.readStringFromParcel(in);
		dayKind = ParcelUtils.readStringFromParcel(in);
		dayPart = ParcelUtils.readStringFromParcel(in);
		city = ParcelUtils.readStringFromParcel(in);
		latitude = ParcelUtils.readStringFromParcel(in);
		longitude = ParcelUtils.readStringFromParcel(in);
		accomodation = ParcelUtils.readStringFromParcel(in);
		dayWeek = ParcelUtils.readStringFromParcel(in);
		startTime = ParcelUtils.readStringFromParcel(in);
		searchAuthor = ParcelUtils.readStringFromParcel(in);
		tsCreation = ParcelUtils.readStringFromParcel(in);
	}

	public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
		public Info createFromParcel(Parcel in) {
			return new Info(in);
		}

		@Override
		public Info[] newArray(int size) {
			return new Info[size];
		}
	};

	@Override
	public void writeToParcel(Parcel out, int flags) {

		ParcelUtils.writeStringToParcel(out, searchId);
		ParcelUtils.writeStringToParcel(out, searchUrl);
		ParcelUtils.writeStringToParcel(out, companyKind);
		ParcelUtils.writeStringToParcel(out, dayKind);
		ParcelUtils.writeStringToParcel(out, dayPart);
		ParcelUtils.writeStringToParcel(out, city);
		ParcelUtils.writeStringToParcel(out, latitude);
		ParcelUtils.writeStringToParcel(out, longitude);
		ParcelUtils.writeStringToParcel(out, accomodation);
		ParcelUtils.writeStringToParcel(out, dayWeek);
		ParcelUtils.writeStringToParcel(out, startTime);
		ParcelUtils.writeStringToParcel(out, searchAuthor);
		ParcelUtils.writeStringToParcel(out, tsCreation);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void setSearchId(String searchId) {
		this.searchId = searchId;
	}

	public String getSearchId() {
		return searchId;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setCompanyKind(String companyKind) {
		this.companyKind = companyKind;
	}

	public String getCompanyKind() {
		return companyKind;
	}

	public void setDayKind(String dayKind) {
		this.dayKind = dayKind;
	}

	public String getDayKind() {
		return dayKind;
	}

	public void setDayPart(String dayPart) {
		this.dayPart = dayPart;
	}

	public String getDayPart() {
		return dayPart;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setAccomodation(String accomodation) {
		this.accomodation = accomodation;
	}

	public String getAccomodation() {
		return accomodation;
	}

	public void setDayWeek(String dayWeek) {
		this.dayWeek = dayWeek;
	}

	public String getDayWeek() {
		return dayWeek;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setSearchAuthor(String searchAuthor) {
		this.searchAuthor = searchAuthor;
	}

	public String getSearchAuthor() {
		return searchAuthor;
	}

	public void setTsCreation(String tsCreation) {
		this.tsCreation = tsCreation;
	}

	public String getTsCreation() {
		return tsCreation;
	}
}
