package com.hhyg.TyClosing.allShop.info;

import android.os.Parcel;
import android.os.Parcelable;

public class SpecialInfo extends BaseInfo implements Parcelable{

	public SpecialInfo(Parcel pacel) {
		super();
		id = pacel.readString();
		netUri = pacel.readString();
	}
	
	public SpecialInfo() {
		super();
	}
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(netUri);
	}
	public static final Parcelable.Creator<SpecialInfo>  CREATOR = new Parcelable.Creator<SpecialInfo>(){
		@Override
		public SpecialInfo createFromParcel(Parcel source) {
			return new SpecialInfo(source);
		}

		@Override
		public SpecialInfo[] newArray(int size) {
			return new SpecialInfo[size];
		}
		
	};
}
