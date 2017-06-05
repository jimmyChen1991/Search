package com.hhyg.TyClosing.info;

import java.util.ArrayList;

public class GoodsInfo {
	public String name;
	public String brand;
	public String typeName;
	public int citCateId;		
	public String brandId;
	public ArrayList<SpuInfo> spuInfoList;

	public GoodsInfo(){
		spuInfoList = new ArrayList<SpuInfo> ();
	}
	
	public boolean isGoodShelve(){
		return spuInfoList.size() > 0;
	}
	
}


