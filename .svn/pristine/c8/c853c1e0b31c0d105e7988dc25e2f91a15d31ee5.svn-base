package com.hhyg.TyClosing.allShop.info;


import android.os.Parcel;
import android.os.Parcelable;

public class SearchInfo implements Parcelable{
	public String searchContent;
	public String key;
	public String cateId;
	public String brandId;
	public int searchType;	//0 关键字搜索，1 品牌搜索，2分类搜索
	public int sortfieId;	//0价格排序，1更新时间，2热度
	public int sort;		//0升序，1降序
	public int currerPage;
	public int isNeedData;  //0无数据，1有数据
	public int cateLevel;
	public SearchPriceInfo priceInfo = new SearchPriceInfo();
	
	public static final int KEYWORD_SEARCH = 0;
	public static final int BRAND_SEARCH = 1;
	public static final int CATE_SEARCH = 2;

	public static final SearchInfo NewInstance(int type,String id,String content){
		SearchInfo instance = new SearchInfo();
		instance.searchContent = content;
		instance.searchType = type;
		if(type == KEYWORD_SEARCH)
			instance.key = id;
		if(type == BRAND_SEARCH)
			instance.brandId = id;
		if(type == CATE_SEARCH)
			instance.cateId = id;
		return instance;
	}	
	
	public SearchInfo(Parcel parcel) {
		super();
		searchContent = parcel.readString();
		key = parcel.readString();
		cateId = parcel.readString();
		brandId = parcel.readString();
		searchType = parcel.readInt();
		cateLevel = parcel.readInt();
	}
	
	public SearchInfo() {
		super();
	}

	@Override
	public String toString() {
		String str = key+"c:"+cateId+"b:"+brandId+"t:"+searchType+"s:"+sortfieId
					+"cP:"+currerPage+"sort"+sort;
		return str;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(searchContent);
		dest.writeString(key);
		dest.writeString(cateId);
		dest.writeString(brandId);
		dest.writeInt(searchType);
		dest.writeInt(cateLevel);
	}
	public static final Parcelable.Creator<SearchInfo> CREATOR = new Parcelable.Creator<SearchInfo>(){
		@Override
		public SearchInfo createFromParcel(Parcel source) {
			return new SearchInfo(source);
		}
		@Override
		public SearchInfo[] newArray(int size) {
			return new SearchInfo[size];
		}
	};
}
