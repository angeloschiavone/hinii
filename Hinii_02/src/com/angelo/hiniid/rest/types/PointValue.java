/**
 * Copyright 2009 Joe LaPenna
 */

package com.angelo.hiniid.rest.types;

import com.angelo.hiniid.rest.util.ParcelUtils;

import android.os.Parcel;
import android.os.Parcelable;

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
public class PointValue implements HiniiType, Parcelable {

	private String step;
	private String duration;
	private String sponsor;
	private String idCategory;
	private String pointId;
	private String pointName;
	private String pointCountry;
	private String pointCity;
	private String pointAddress;
	private String pointLat;
	private String pointLng;
	private String image;
	private String pointTypology;
	private String dayPart;
	private String printDayPart;
	private String pointUrl;

	public PointValue() {
	}

	private PointValue(Parcel in) {
		step = ParcelUtils.readStringFromParcel(in);
		duration = ParcelUtils.readStringFromParcel(in);
		sponsor = ParcelUtils.readStringFromParcel(in);
		idCategory = ParcelUtils.readStringFromParcel(in);
		pointId = ParcelUtils.readStringFromParcel(in);
		pointName = ParcelUtils.readStringFromParcel(in);
		pointCountry = ParcelUtils.readStringFromParcel(in);
		pointCity = ParcelUtils.readStringFromParcel(in);
		pointAddress = ParcelUtils.readStringFromParcel(in);
		pointLat = ParcelUtils.readStringFromParcel(in);
		pointLng = ParcelUtils.readStringFromParcel(in);
		image = ParcelUtils.readStringFromParcel(in);
		pointTypology = ParcelUtils.readStringFromParcel(in);
		dayPart = ParcelUtils.readStringFromParcel(in);
		printDayPart = ParcelUtils.readStringFromParcel(in);
		pointUrl = ParcelUtils.readStringFromParcel(in);
	}

	public static final Parcelable.Creator<PointValue> CREATOR = new Parcelable.Creator<PointValue>() {
		public PointValue createFromParcel(Parcel in) {
			return new PointValue(in);
		}

		@Override
		public PointValue[] newArray(int size) {
			return new PointValue[size];
		}
	};

	@Override
	public void writeToParcel(Parcel out, int flags) {
		ParcelUtils.writeStringToParcel(out, step);
		ParcelUtils.writeStringToParcel(out, duration);
		ParcelUtils.writeStringToParcel(out, sponsor);
		ParcelUtils.writeStringToParcel(out, idCategory);
		ParcelUtils.writeStringToParcel(out, pointId);
		ParcelUtils.writeStringToParcel(out, pointName);
		ParcelUtils.writeStringToParcel(out, pointCountry);
		ParcelUtils.writeStringToParcel(out, pointCity);
		ParcelUtils.writeStringToParcel(out, pointAddress);
		ParcelUtils.writeStringToParcel(out, pointLat);
		ParcelUtils.writeStringToParcel(out, pointLng);
		ParcelUtils.writeStringToParcel(out, image);
		ParcelUtils.writeStringToParcel(out, pointTypology);
		ParcelUtils.writeStringToParcel(out, dayPart);
		ParcelUtils.writeStringToParcel(out, printDayPart);
		ParcelUtils.writeStringToParcel(out, pointUrl);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}

	public String getSponsor() {
		return sponsor;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getStep() {
		return step;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDuration() {
		return duration;
	}

	public void setIdCategory(String idCategory) {
		this.idCategory = idCategory;
	}

	public String getIdCategory() {
		return idCategory;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointCountry(String pointCountry) {
		this.pointCountry = pointCountry;
	}

	public String getPointCountry() {
		return pointCountry;
	}

	public void setPointCity(String pointCity) {
		this.pointCity = pointCity;
	}

	public String getPointCity() {
		return pointCity;
	}

	public void setPointAddress(String pointAddress) {
		this.pointAddress = pointAddress;
	}

	public String getPointAddress() {
		return pointAddress;
	}

	public void setPointLat(String pointLat) {
		this.pointLat = pointLat;
	}

	public String getPointLat() {
		return pointLat;
	}

	public void setPointLng(String pointLng) {
		this.pointLng = pointLng;
	}

	public String getPointLng() {
		return pointLng;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setPointTypology(String pointTypology) {
		this.pointTypology = pointTypology;
	}

	public String getPointTypology() {
		return pointTypology;
	}

	public void setDayPart(String dayPart) {
		this.dayPart = dayPart;
	}

	public String getDayPart() {
		return dayPart;
	}

	public void setPrintDayPart(String printDayPart) {
		this.printDayPart = printDayPart;
	}

	public String getPrintDayPart() {
		return printDayPart;
	}

	public void setPointUrl(String pointUrl) {
		this.pointUrl = pointUrl;
	}

	public String getPointUrl() {
		return pointUrl;
	}

}
