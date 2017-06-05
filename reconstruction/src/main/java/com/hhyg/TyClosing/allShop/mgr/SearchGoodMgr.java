package com.hhyg.TyClosing.allShop.mgr;

import java.util.ArrayList;

import com.hhyg.TyClosing.allShop.factory.Sortfielder;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.allShop.info.SearchResultInfo;

public class SearchGoodMgr {
	public ArrayList<GoodItemInfo> GoodResult;
	public SearchInfo searchInfo;
	public SearchResultInfo searchResult;
	public void setCurretPage(int page){
		searchInfo.currerPage = page;
	}
	public void setSearchContent(String content){
		searchInfo.searchContent = content;
	}
	public void setSearchSortfielder(Sortfielder fiedler){
		searchInfo.sortfieId = fiedler.getSortfielder();
	}
	public void reInitCurPage(){
		searchInfo.currerPage = 1;
	}
	public void setNeedData(){
		searchInfo.isNeedData = 1;
	}
	public void setNoNeedData(){
		searchInfo.isNeedData = 0;
	}
	public void clear(){
		GoodResult = null;
		searchInfo = null;
		searchResult = null;
	}
	
}
