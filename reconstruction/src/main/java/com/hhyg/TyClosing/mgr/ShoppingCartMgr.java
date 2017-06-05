package com.hhyg.TyClosing.mgr;
import java.util.ArrayList;

import com.hhyg.TyClosing.dao.ShoppingcartDao;
import com.hhyg.TyClosing.info.ActiveColumns;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.info.SpuInfo;
public class ShoppingCartMgr {
	private static ShoppingCartMgr mInstance = new ShoppingCartMgr();
	private static ShoppingcartDao mDao = new ShoppingcartDao();
	private ArrayList<ShoppingCartInfo> mInfoList = null;
	private ArrayList<ActiveColumns> mColumns;
	private String totalPrice;
	private String realCast;
	private String discount;
	private String errMsg;
	private boolean warnning;
    public static ShoppingCartMgr getInstance() {
		return mInstance;
	}
    
    public ArrayList<ShoppingCartInfo> getAll() {
    	if(mInfoList == null){
    		mInfoList = mDao.GetAll();
    	}
    	return mInfoList;
    }
    
    public void setWarnning(boolean warnning) {
		this.warnning = warnning;
	}
    
    public boolean isWarnning() {
		return warnning;
	}
    
    public ArrayList<ActiveColumns> getColumns(){
    	return mColumns;
    }
    
    public void setColumns(ArrayList<ActiveColumns> mColumns) {
		this.mColumns = mColumns;
	}
    
    public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
    
    public String getErrMsg() {
		return errMsg;
	}
    
    public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getRealCast() {
		return realCast;
	}

	public void setRealCast(String realCast) {
		this.realCast = realCast;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public boolean isInfoExist(String barcode) {
     	boolean res = false;
    	int pos = findPos(barcode);
    	if(pos >=0) {
    		res = true;
    	}
     	return res;
    	
    }
    
    public ShoppingCartInfo getInfoByBarCode(String barCode){
    	ShoppingCartInfo info = null;
    	int pos = findPos(barCode);
    	if(pos >=0) {
    		info = getAll().get(pos);
    	}
    	return info;
    }
    public void updateStock(String barcode, int stock) {
    	mDao.updateStock(stock, barcode);
    	getInfoByBarCode(barcode).stock = stock;
    }
    public void updateShopCnt(String barcode,int cnt){
    	mDao.updateShopCnt(cnt, barcode);
    	getInfoByBarCode(barcode).cnt = cnt;
    }
    
    public void updateActiveId(String barcode,String ActiveId){
    	mDao.updateShopActiveId(ActiveId, barcode);
    	getInfoByBarCode(barcode).activeId = ActiveId;
    }
    
    public boolean addInfo(SpuInfo sInfo, String name, String brand,int cnt,int typeId,String typeName) {
    	boolean res = true;
    	int pos = findPos(sInfo.barCode);
    	if(pos < 0) {
        	mDao.insertInfo(sInfo, name, brand,cnt,String.valueOf(typeId),typeName);
        	ShoppingCartInfo info = new ShoppingCartInfo();
        	info.full = sInfo.full;
        	info.fullReduce =sInfo.fullReduce;
        	info.activeCut = sInfo.activeCut;
        	info.activePrice = sInfo.activePrice;
        	info.attrInfo = sInfo.attrInfo;
        	info.barCode = sInfo.barCode;
        	info.brand = brand;
        	info.citAmount = sInfo.citAmount;
        	info.citPrice = sInfo.citPrice;
        	info.name = name;
        	info.activeName = sInfo.activeName;
        	info.spuName = sInfo.name;
        	info.stock = sInfo.stock;
        	info.cnt =cnt;
        	info.typeName = typeName;
        	info.typeId = typeId;
        	info.msPrice = sInfo.msPrice;
        	if(sInfo.activeInfos != null){
        		info.activeId = sInfo.activeInfos.get(0).getActiveId();
        	}else{
        		info.activeId = "";
        	}
        	if(sInfo.imageLinks != null && sInfo.imageLinks.size() != 0){
        		info.imgUrl = sInfo.imageLinks.get(0);
        	}else{
        		info.imgUrl = "";
        	}
        	mDao.updateShopActiveId(info.activeId, info.barCode);
        	info.activeName =  sInfo.activeName;
        	int lastAcltiveIdPos = findLastActiveIdPos(info.activeId);
        	if(lastAcltiveIdPos<0) {
        		mInfoList.add(info);	
        	}
        	else {
        		mInfoList.add(lastAcltiveIdPos+1, info);
        	}
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
    
    private int findLastActiveIdPos(String activeId) {
    	int pos = -1;
    	int cnt = getAll().size();
    	for(int idx = 0; idx < cnt; idx++) {
    		if(activeId.equals(mInfoList.get(idx).activeId)) {
    			pos = idx;
    		}
    	}
    	return pos;	
    }
}