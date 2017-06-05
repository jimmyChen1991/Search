package com.hhyg.TyClosing.dao;

import com.hhyg.TyClosing.global.DbOpenHelper;
import com.hhyg.TyClosing.info.SalerInfo;

public class SalerInfoDao {
	private final String mTableName = "saler_info";
	private DbOpenHelper mDb = DbOpenHelper.GetInstance();
	public void insert(SalerInfo info){
		String keys[] = {"salerid","username","salername","shopname","shoptype"};
		String Values[] = {info.getSalerId(),info.getUserName(),info.getSalerName(),info.getShopName(),info.getShopType()};
		mDb.insert(mTableName, keys, Values);
	}
	public void updata(SalerInfo info){
		delete();
		insert(info);
	}
	public void delete(){
		String sql = "delete from saler_info";
		mDb.execuate(sql);
	}
	public SalerInfo getSalerInfo(){
		String sql = "select * from " + mTableName;
		String keys[] = {"salerid","username","salername","shopname","shoptype"};
		String[][] resArr = mDb.rawQuery(sql, keys);
		SalerInfo info = new SalerInfo();
		String[] res = resArr[0];
		info.setSalerId(res[0]);
		info.setUserName(res[1]);
		info.setSalerName(res[2]);
		info.setShopName(res[3]);
		info.setShopType(res[4]);
		return info;
	}
}
