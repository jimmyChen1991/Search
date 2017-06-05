package com.hhyg.TyClosing.dao;

import java.util.ArrayList;

import com.hhyg.TyClosing.global.DbOpenHelper;
import com.hhyg.TyClosing.info.PickUpInfo;

public class PickUpInfoDao {
	private final String mTableName = "pickup_info";
	private DbOpenHelper mDb = DbOpenHelper.GetInstance();
	public void insert(PickUpInfo info){
		String[] keys = {"id","name","prepareTime"};
		String[] values = {String.valueOf(info.id),info.name,String.valueOf(info.prepareTime)};
		mDb.insert(mTableName, keys, values);
	}
	public void delete(){
		String sql = "delete from pickup_info";
		mDb.execuate(sql);
	}
	public ArrayList<PickUpInfo> getAllPickUpInfo(){
		String sql = "select * from " + mTableName;
		String[] keys = {"id","name","prepareTime"};
		String[][] resArr = mDb.rawQuery(sql, keys);
		ArrayList<PickUpInfo> values = new ArrayList<PickUpInfo>();
		for(int idx = 0;idx<resArr.length;idx++){
			String[] res = resArr[idx];
			PickUpInfo info = new PickUpInfo();
			info.id = Integer.valueOf(res[0]);
			info.name = res[1];
			info.prepareTime = Integer.valueOf(res[2]);
			values.add(info);
		}
		return values;
	}
}
