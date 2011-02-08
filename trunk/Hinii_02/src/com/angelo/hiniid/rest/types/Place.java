package com.angelo.hiniid.rest.types;

import com.angelo.hiniid.rest.util.ParcelUtils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author Angelo Schiavone
 */
public class Place implements HiniiType, Parcelable {
	/*
	 * <id>176164</id> <name>Starbucks</name> <address>125 Worth St</address>
	 * <city>New York</city> <country>USA</country>
	 * <latitude>40.715309</latitude> <longitude>-74.002838</longitude>
	 * <sponsor>0</sponsor> <similarityCity>100</similarityCity>
	 * <similarity>250</similarity>
	 * <url>http://hinii.com/en/point/view/id/176164</url>
	 */
	private String mId;
	private String mName;
	private String mAddress;
	private String mCity;
	private String mCountry;
	private String mLatitude;
	private String mLongitude;
	private String mSponsor;
	private String mSimilarityCity;
	private String mSimilarity;
	private String mUrl;

	public Place() {
	}

	private Place(Parcel in) {
		mId = ParcelUtils.readStringFromParcel(in);
		mName = ParcelUtils.readStringFromParcel(in);
		mAddress = ParcelUtils.readStringFromParcel(in);
		mCity = ParcelUtils.readStringFromParcel(in);
		mCountry = ParcelUtils.readStringFromParcel(in);
		mLatitude = ParcelUtils.readStringFromParcel(in);
		mLongitude = ParcelUtils.readStringFromParcel(in);
		mSponsor = ParcelUtils.readStringFromParcel(in);
		mSimilarityCity = ParcelUtils.readStringFromParcel(in);
		mSimilarity = ParcelUtils.readStringFromParcel(in);
		mUrl = ParcelUtils.readStringFromParcel(in);
	}

	public static final Parcelable.Creator<Place> CREATOR = new Parcelable.Creator<Place>() {
		public Place createFromParcel(Parcel in) {
			return new Place(in);
		}

		@Override
		public Place[] newArray(int size) {
			return new Place[size];
		}
	};

	@Override
	public void writeToParcel(Parcel out, int flags) {
		ParcelUtils.writeStringToParcel(out, mId);
		ParcelUtils.writeStringToParcel(out, mName);
		ParcelUtils.writeStringToParcel(out, mAddress);
		ParcelUtils.writeStringToParcel(out, mCity);
		ParcelUtils.writeStringToParcel(out, mCountry);
		ParcelUtils.writeStringToParcel(out, mLatitude);
		ParcelUtils.writeStringToParcel(out, mLongitude);
		ParcelUtils.writeStringToParcel(out, mSponsor);
		ParcelUtils.writeStringToParcel(out, mSimilarityCity);
		ParcelUtils.writeStringToParcel(out, mSimilarity);
		ParcelUtils.writeStringToParcel(out, mUrl);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getName() {
		return mName;
	}

	public void setAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setCity(String mCity) {
		this.mCity = mCity;
	}

	public String getCity() {
		return mCity;
	}

	public void setCountry(String mCountry) {
		this.mCountry = mCountry;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setLatitude(String mLatitude) {
		this.mLatitude = mLatitude;
	}

	public String getLatitude() {
		return mLatitude;
	}

	public void setLongitude(String mLongitude) {
		this.mLongitude = mLongitude;
	}

	public String getLongitude() {
		return mLongitude;
	}

	public void setSponsor(String mSponsor) {
		this.mSponsor = mSponsor;
	}

	public String getSponsor() {
		return mSponsor;
	}

	public void setSimilarityCity(String mSimilarityCity) {
		this.mSimilarityCity = mSimilarityCity;
	}

	public String getSimilarityCity() {
		return mSimilarityCity;
	}

	public void setSimilarity(String mSimilarity) {
		this.mSimilarity = mSimilarity;
	}

	public String getSimilarity() {
		return mSimilarity;
	}

	public void setUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setId(String mId) {
		this.mId = mId;
	}

	public String getId() {
		return mId;
	}

}
