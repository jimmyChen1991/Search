package com.hhyg.TyClosing.dao;

import java.util.ArrayList;
import com.hhyg.TyClosing.global.DbOpenHelper;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.info.SpuInfo;

public class ShoppingcartDao {
	private final String mTableName = "shoppingcart";
	private DbOpenHelper mDb = DbOpenHelper.GetInstance();
	
	public boolean insertInfo(SpuInfo spuInfo, String name, String brand,int cnt,String typeId,String typeName){
		String[] keys = {"name", "brand","spuname","attrinfo", "stock","citamount","activename","citprice","activeprice","activecut","barcode","cnt","typeid","typename","activeid","full","fullreduce","imgurl","msprice"};
		String[] values = {name,brand,spuInfo.name,spuInfo.attrInfo, String.valueOf(spuInfo.stock), String.valueOf(spuInfo.citAmount),spuInfo.activeName,String.valueOf(spuInfo.citPrice),String.valueOf(spuInfo.activePrice),String.valueOf(spuInfo.activeCut),spuInfo.barCode,String.valueOf(cnt),typeId,typeName,spuInfo.activeId,spuInfo.full,spuInfo.fullReduce,spuInfo.imgUrl,spuInfo.msPrice};
		mDb.insert(mTableName, keys, values);
		return true;
	}
	public void updateStock(int stock, String barcode) {
		String sql = "update shoppingcart set stock='" + stock + "' where barcode = '" + barcode +"'";
		mDb.execuate(sql);	
	}
	public void updateShopCnt(int cnt, String barcode){
		String sql = "update shoppingcart set cnt='" + cnt + "' where barcode = '" + barcode +"'";
		mDb.execuate(sql);
	}
	public void updateShopActiveId(String aId,String barcode){
		String sql = "update shoppingcart set activeid='" + aId + "' where barcode = '" + barcode +"'";
		mDb.execuate(sql);
	}
	public void deleteSpuInfo(String barCode) {
		String sql = "delete from shoppingcart where barcode ='"+barCode+"'";
		mDb.execuate(sql);
	}
	public void clearAll() {
		String sql = "delete from shoppingcart";
		mDb.execuate(sql);
	}
	public ArrayList<ShoppingCartInfo> GetAll() {
		ArrayList<ShoppingCartInfo> valueList = new ArrayList<ShoppingCartInfo>();
		ShoppingCartInfo info = null;
		String sql = "select * from " + mTableName;
		String[] keys = {"name", "brand","spuname","attrinfo", "stock","citamount","activename","citprice","activeprice","activecut","barcode","cnt","typeid","typename","activeid","full","fullreduce","imgurl"};
		String[][] resArr = mDb.rawQuery(sql, keys);
        int cnt = resArr.length;
        for(int idx = 0; idx < cnt; idx++) {
        	info = new ShoppingCartInfo();
        	String[] res = resArr[idx];
			info.name = res[0];
			info.brand = res[1];
			info.spuName = res[2];
			info.attrInfo = res[3];
			info.stock = Integer.parseInt(res[4]) ;
			info.citAmount = Integer.parseInt(res[5]);
			info.activeName = res[6];
			info.citPrice = Double.parseDouble(res[7]) ;
			info.activePrice = Double.parseDouble(res[8]) ;
			info.activeCut = Double.parseDouble(res[9]) ;
			info.barCode = res[10] ;
        	info.cnt = Integer.parseInt(res[11]);
        	info.typeId = Integer.parseInt(res[12]);
			info.typeName =  res[13];
			info.activeId =  res[14];
			info.full = res[15];
			info.fullReduce = res[16];
			info.imgUrl = res[17];
			valueList.add(info);
        }	
		return valueList;
	}	
}
