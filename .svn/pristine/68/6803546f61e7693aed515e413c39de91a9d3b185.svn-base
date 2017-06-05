package com.hhyg.TyClosing.mgr;

import java.util.ArrayList;

import com.hhyg.TyClosing.info.ShoppingCartInfo;

public class RestrictionMgr {
	
	private final double FREE_DUTY_TOTLE_THREAD = 16000;
	private ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();
	public final double FREE_DUTY_THREAD = 8000;
	
	private static RestrictionMgr mInstance = new RestrictionMgr();
	public String mErroInfo = "";

	
	public static RestrictionMgr getInstance() { 
		return mInstance;	
	}
	
	public String getErro(){
		return mErroInfo;
	}
	
    public boolean IsHaveTaxGoods() {
    	boolean res = false;
    	ArrayList<ShoppingCartInfo> infoList = mShoppingCartMgr.getAll();
    	int cnt = infoList.size();
    	for(int idx =0; idx <cnt; idx++) {
    		ShoppingCartInfo info = infoList.get(idx);
    		if(info.activePrice >= FREE_DUTY_THREAD) {
    			res = true;
    			break;
    		}
    	}
    	return res;	
    }
    public boolean TestIsHaveTaxGoods(ArrayList<ShoppingCartInfo> infoList){
    	boolean res = false;
    	int cnt = infoList.size();
    	for(int idx =0; idx <cnt; idx++) {
    		ShoppingCartInfo info = infoList.get(idx);
    		if(info.activePrice >= FREE_DUTY_THREAD) {
    			res = true;
    			break;
    		}
    	}
	
    	return res;	
    	
    }
	
	public boolean IsFreeOfDutyLargerThanLimit() {
		boolean res = false;
	    ArrayList<ShoppingCartInfo> infoList = mShoppingCartMgr.getAll();
		int cnt = infoList.size();
		double totle = 0;
		for(int idx =0; idx < cnt; idx++) {
			if(infoList.get(idx).activePrice < FREE_DUTY_THREAD) {
				totle += infoList.get(idx).activePrice *infoList.get(idx).cnt;
			}
		}
		if(totle > FREE_DUTY_TOTLE_THREAD){
			mErroInfo = "免税商品总额不能超过16000";
			res = true;
			return res;
		}
		return res;
	}
	
	public boolean IsTaxGoodsCntLargerThanLimit() {
		boolean res = false;
		 ArrayList<ShoppingCartInfo> infoList = mShoppingCartMgr.getAll();
			int cnt = infoList.size();
			int bigPriceGoodsCnt = 0;
			for(int idx =0; idx < cnt; idx++) {
				if(infoList.get(idx).activePrice >=FREE_DUTY_THREAD) {
					bigPriceGoodsCnt+=infoList.get(idx).cnt;
				}
			}
			if(bigPriceGoodsCnt > 1) {
				res = true;
				mErroInfo = "单次离岛限购一件有税商品";
			}
		return res;
	}
	
	
	public boolean IsTypeRestictionCntLargerThanLimit() {
		boolean res = false;
		ArrayList<Integer> idList =  new ArrayList<Integer> (); 
		ArrayList<ShoppingCartInfo> infoList = mShoppingCartMgr.getAll();
		int cnt = infoList.size();
		for(int idx = 0; idx < cnt; idx++) {
			int typeId = infoList.get(idx).typeId;
			if(!idList.contains(typeId)) {
				idList.add(typeId);
			}		
		}
		
		int idCnt = idList.size();
		String barCode = "";
		for(int idx = 0; idx < idCnt; idx++) {
			int typeId = idList.get(idx);
			int buyCnt = 0;
			for(int i = 0; i < cnt; i++) {
				if(typeId==infoList.get(i).typeId) {	
					buyCnt +=infoList.get(i).cnt;
					barCode = infoList.get(i).barCode;
				}
			}

			ShoppingCartInfo info = mShoppingCartMgr.getInfoByBarCode(barCode);
			if(buyCnt > info.citAmount) {
				mErroInfo =info.typeName+"类商品单次离岛限购"+info.citAmount+"件";
				res = true;
				break;
			}
			
		}
		idList.clear();
		idList = null;
		return res;		
	}
	
	
	public boolean IsStockNotEnough() {
		boolean res = false;
		 ArrayList<ShoppingCartInfo> infoList = mShoppingCartMgr.getAll();
			int cnt = infoList.size();
			for(int idx =0; idx < cnt; idx++) {
				if(infoList.get(idx).stock <infoList.get(idx).cnt) {
					res = true;
					break;
				}
			}
			if(res) {
				res = true;
				mErroInfo = "部分商品库存不足，请在购物车内调整购买数量";
			}
		return res;
	}
	
}