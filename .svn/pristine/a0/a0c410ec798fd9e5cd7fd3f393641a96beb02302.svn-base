package com.hhyg.TyClosing.mgr;
import java.util.ArrayList;

import com.hhyg.TyClosing.dao.DeleteDao;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.info.SpuInfo;
public class DeleteMgr {
	private static DeleteMgr mInstance = new DeleteMgr();
	private DeleteDao mDao = new DeleteDao();
	private ArrayList<ShoppingCartInfo> mInfoList = null;
	
    public static DeleteMgr getInstance() {
		return mInstance;
	}
    public boolean isInfoExist(String barcode) {
     	boolean res = false;
    	int pos = findPos(barcode);
    	if(pos >=0) {
    		res = true;
    	}
     	return res;
    }
    
    public ArrayList<ShoppingCartInfo> getAll() {
    	if(mInfoList == null){
    		mInfoList = mDao.GetAll();
    	}
    	return mInfoList;
    }
    
    public boolean addInfo(SpuInfo sInfo, String name, String brand, int cnt,String typeId,String typeName) {
    	boolean res = true;
    	int pos = findPos(sInfo.barCode);
    	if(pos < 0) {
    		res = mDao.insertInfo(sInfo, name, brand,cnt,typeId,typeName);
        	ShoppingCartInfo info = new ShoppingCartInfo();
        	info.activeCut = sInfo.activeCut;
        	info.activePrice = sInfo.activePrice;
        	info.activeId = sInfo.activeId;
        	info.typeName = typeName;
        	info.attrInfo = sInfo.attrInfo;
        	info.barCode = sInfo.barCode;
        	info.brand = brand;
        	info.citAmount = sInfo.citAmount;
        	info.citPrice = sInfo.citPrice;
        	info.name = name;
        	info.activeName = sInfo.activeName;
        	info.spuName = sInfo.name;
        	info.stock = sInfo.stock;
        	info.cnt  =cnt;
        	info.full = sInfo.full;
        	info.fullReduce = sInfo.fullReduce;
        	info.imgUrl = sInfo.imgUrl;
        	getAll().add(info);	
    	}else {
    		res = false;
    	}
    	return res;
    }
    
    public boolean addInfo(ShoppingCartInfo cartInfo){
    	boolean res = true;
    	int pos = findPos(cartInfo.barCode);
    	if(pos < 0) {
    		mDao.insertInfo(cartInfo);
        	ShoppingCartInfo info = new ShoppingCartInfo();
        	info.activeCut = cartInfo.activeCut;
        	info.activePrice = cartInfo.activePrice;
        	info.activeId = cartInfo.activeId;
        	info.typeName = cartInfo.typeName;
        	info.attrInfo = cartInfo.attrInfo;
        	info.barCode = cartInfo.barCode;
        	info.brand = cartInfo.brand;
        	info.citAmount = cartInfo.citAmount;
        	info.citPrice = cartInfo.citPrice;
        	info.name = cartInfo.name;
        	info.activeName = cartInfo.activeName;
        	info.spuName = cartInfo.name;
        	info.stock = cartInfo.stock;
        	info.cnt  =cartInfo.cnt;
        	info.full = cartInfo.full;
        	info.fullReduce = cartInfo.fullReduce;
        	info.imgUrl = cartInfo.imgUrl;
        	info.msPrice = cartInfo.msPrice;
        	getAll().add(info);	
    	}else {
    		res = false;
    	}
    	return res;
    }
    
    public void deleteInfo(String barCode) {
    	int pos = findPos(barCode);
    	if(pos >= 0) {
    		getAll().remove(pos);
    		mDao.deleteSpuInfo(barCode);
    	}
    }
    
    public void clear() {
    	getAll().clear();
    	mDao.clearAll();  	
    }
    
    private int findPos(String barCode) {
    	int pos = -1;
    	int cnt = getAll().size();
    	for(int idx = 0; idx <cnt; idx++) {
    		if(barCode.equals(getAll().get(idx).barCode)) {
    			pos = idx;
    			break;
    		}
    	}
    	return pos;
    }
}