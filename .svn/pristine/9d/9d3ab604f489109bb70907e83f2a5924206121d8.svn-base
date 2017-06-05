package com.hhyg.TyClosing.dao;

import com.hhyg.TyClosing.global.DbOpenHelper;

public class InitInfoDao {
	private final String mTableName = "init_info";
	private DbOpenHelper mDb = DbOpenHelper.GetInstance();
	public void insert(String shopId,String channleId){
		String keys[] = {"shopid","channleid"};
		String Values[] = {shopId,channleId};
		mDb.insert(mTableName, keys, Values);
	}
	public void updata(String shopId,String channleId){
		delete();
		insert(shopId,channleId);
	}
	public void delete(){
		String sql = "delete from init_info";
		mDb.execuate(sql);
	}
	public String getShopId(){
		String sql = "select * from " + mTableName;
		String[] keys = {"shopid"};
		String[][] resArr = mDb.rawQuery(sql, keys);
		String shopId = resArr[0][0];
		return shopId;
	}
	public String getChannleId(){
		String sql = "select * from " + mTableName;
		String[] keys = {"channleid"};
		String[][] resArr = mDb.rawQuery(sql, keys);
		String ChannleId = resArr[0][0];
		return ChannleId;
	}
}
