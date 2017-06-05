package com.hhyg.TyClosing.allShop.info;

import com.hhyg.TyClosing.info.ActiveInfo;

public class GoodItemInfo {
	public String citPrice;
	public String activePrice;
	public String activeCut;
	public String full;
	public String full_reduce;
	public String barCode;
	public String attr;
	public String name;
	public String brandName;
	public String netUri;
	public int stock;
	public int shelveStatus;
	public int citAmount;
	public int typeId;
	public String CitName;
	public ActiveInfo activeInfo;
	public String getGoodName(){
		if(name.length()>10){
			name = name.substring(0,10)+"...";
		}
		return name;
	}
	public String getActiviteCut(){
		if(getActiviteCutStringLength()){
			return CheckActiviteCutHasPointCut()?activeCut.substring(0, 1)+"."+activeCut.substring(1, 2):activeCut.substring(0, 1);
		}
		return activeCut;
	}
	private boolean getActiviteCutStringLength(){
		if(activeCut.length()>0){
			return true;
		}
		return false;
	}
	private boolean CheckActiviteCutHasPointCut(){
		char c = '0';
		if(activeCut.charAt(1)!= c){
			return true;
		}
		return false;
	}
}
