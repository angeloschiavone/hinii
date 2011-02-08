package com.angelo.hiniid.rest.types;

import com.angelo.hiniid.rest.util.ParcelUtils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * *
 * 
 * @author Angelo Schiavone
 */
public class SearchResult implements HiniiType, Parcelable {

	private String resultCode;
	private String resultMsg;

	private Info info;
	private Group<PointValue> iter;

	public SearchResult() {
	}

	private SearchResult(Parcel in) {
		resultCode = ParcelUtils.readStringFromParcel(in);
		resultMsg = ParcelUtils.readStringFromParcel(in);
		info = in.readParcelable(Info.class.getClassLoader());
		iter = new Group<PointValue>();
		int numIter = in.readInt();
		for (int i = 0; i < numIter; i++) {
			PointValue pointValue = in.readParcelable(PointValue.class
					.getClassLoader());
			iter.add(pointValue);
		}
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

	public void setInfo(Info info) {
		this.info = info;
	}

	public Info getInfo() {
		return info;
	}

	public void setIter(Group<PointValue> iter) {
		this.iter = iter;
	}

	public Group<PointValue> getIter() {
		return iter;
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
		out.writeParcelable(info, flags);
		if (iter != null) {
			out.writeInt(iter.size());
			for (int i = 0; i < iter.size(); i++) {
				out.writeParcelable(iter.get(i), flags);
			}
		} else {
			out.writeInt(0);
		}

	}

}
