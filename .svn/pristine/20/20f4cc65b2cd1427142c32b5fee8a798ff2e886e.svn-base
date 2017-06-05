package com.hhyg.TyClosing.info;

import android.os.Parcel;
import android.os.Parcelable;

public class PickUpInfo implements Parcelable{  
	public int id;
	public String name;
	public int prepareTime;
	public PickUpInfo(){
		super();
	}
	public PickUpInfo(Parcel parcelable) {
		super();
		id = parcelable.readInt();
		name = parcelable.readString();
		prepareTime = parcelable.readInt();
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeInt(prepareTime);
	}
	 public static final Parcelable.Creator<PickUpInfo> CREATOR = new Parcelable.Creator<PickUpInfo>() {
		 @Override
	        public PickUpInfo createFromParcel(Parcel source) {
	            return new PickUpInfo(source);
	        }
		 @Override
	        public PickUpInfo[] newArray(int size) {
	            return new PickUpInfo[size];
		 }};
}