package com.hhyg.TyClosing.allShop.proc;

import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.info.ActiveColumns;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.info.ActiveInfo.ActiveType;
import com.hhyg.TyClosing.info.ShopCartItem;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;

public class ShopCartProc implements ProcMsgHelper{
	private OnProcListener listener;
	private ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();
	@Override
	public void ProcMsg(String msgBody) throws JSONException, IOException {
		JSONObject mgsParse = JSONObject.parseObject(msgBody);
		JSONObject data = mgsParse.getJSONObject("data");
		handlerAllInfo(data);
		handlerActiveColumns(data);
		handlerWarnning(data);
		listener.OnProcCompleted();
	}
	
	private void handlerWarnning(JSONObject data) {
		JSONObject amout_JOBJ = new JSONObject();
		try{
			amout_JOBJ = data.getJSONObject("amount_info");
		}catch(ClassCastException e){
			JSONObject price_JOBJ = data.getJSONObject("price_info");
			if(!price_JOBJ.getBooleanValue("status")){
				mShoppingCartMgr.setWarnning(true);
				mShoppingCartMgr.setErrMsg(price_JOBJ.getString("msg"));
			}
			mShoppingCartMgr.setWarnning(false);
			mShoppingCartMgr.setErrMsg(null);
			return;
		}
		for(String key : amout_JOBJ.keySet()){
			JSONObject info = amout_JOBJ.getJSONObject(key);
			mShoppingCartMgr.setWarnning(true);
			mShoppingCartMgr.setErrMsg(info.getString("limit_count"));
		}
	}

	private void handlerActiveColumns(JSONObject data) {
		JSONObject columns = data.getJSONObject("active_columns");
		if(columns != null){
			ArrayList<ActiveColumns> columnsList = new ArrayList<ActiveColumns>();
			for(String key :columns.keySet()){
				JSONObject column_JOBJ = columns.getJSONObject(key);
				ActiveColumns aColumns = new ActiveColumns();
				ActiveInfo aInfo = new ActiveInfo();
				if(!key.equals("noStock")){
					aColumns.setDiscount(column_JOBJ.getString("preferentialPrice"));
					aColumns.setTotal_price(column_JOBJ.getString("total_price"));
					aColumns.setReal_cast(column_JOBJ.getString("active_price"));
					aInfo.setActiveId(key);
					aInfo.setDesc(column_JOBJ.getString("desc"));
					aInfo.setShort_desc(column_JOBJ.getString("short_desc"));
					aInfo.setType(column_JOBJ.getString("type"));
					aInfo.setType_name(column_JOBJ.getString("type_name"));
					aInfo.setComments(column_JOBJ.getString("desc_fee"));
					aInfo.setPrivilegeType(column_JOBJ.getIntValue("isprivilege"));
					aColumns.setaInfo(aInfo);
				}else{
					aInfo.setType(ActiveType.NoStock);
				}
				aColumns.setaInfo(aInfo);
				JSONArray goods = column_JOBJ.getJSONArray("goods");
				if(goods != null){
					ArrayList<ShopCartItem> cartList = new ArrayList<ShopCartItem>();
					for(int index =0 ; index < goods.size() ; index++){
						JSONObject goodItem = goods.getJSONObject(index);
						ShopCartItem item = new ShopCartItem();
						final ShoppingCartInfo oldInfo = ShoppingCartMgr.getInstance().getInfoByBarCode(goodItem.getString("barcode"));
						item.setBarCode(goodItem.getString("barcode"));
						item.setName(goodItem.getString("name"));
						item.setCitPrice(goodItem.getString("price"));
						if(goodItem.getString("price") != null){
							item.setActivePrice(goodItem.getString("active_price"));
						}
						item.setStock(goodItem.getIntValue("stock"));
						item.setStockInfo(goodItem.getString("stock_info"));
						item.setImgUrl(goodItem.getString("image"));
						item.setAttrInfo(goodItem.getString("attr_info"));
						item.setCnt(goodItem.getIntValue("count"));
						item.setBrand(oldInfo.brand);
						JSONArray aInfos = goodItem.getJSONArray("otherActivities");
						if(aInfos != null){
							ArrayList<ActiveInfo> activeList = new ArrayList<ActiveInfo>();
							for(int position = 0 ; position < aInfos.size() ; position ++){
								JSONObject otherAInfo_JOBJ = aInfos.getJSONObject(position);
								ActiveInfo otherAInfo = new ActiveInfo();
								otherAInfo.setActiveId(otherAInfo_JOBJ.getString("code"));
								otherAInfo.setShort_desc(otherAInfo_JOBJ.getString("short_desc"));
								otherAInfo.setType(otherAInfo_JOBJ.getString("type"));
								otherAInfo.setType_name(otherAInfo_JOBJ.getString("type_name"));
								otherAInfo.setPrivilegeType(otherAInfo_JOBJ.getString("activityType"));
								activeList.add(otherAInfo);
							}
							item.setaInfos(activeList);
						}
						cartList.add(item);
					}
					aColumns.setCartItems(cartList);
				}
				if(aInfo.getType() == ActiveType.NoStock){
					columnsList.add(0, aColumns);
				}else{
					columnsList.add(aColumns);
				}
			}
			mShoppingCartMgr.setColumns(columnsList);
		}
	}

	private void handlerAllInfo(JSONObject data) {
		final String totalPrice = data.getString("total_price");
		final String discount = data.getString("preferentialPrice");
		final String realCast = data.getString("active_price");
		mShoppingCartMgr.setDiscount(discount);
		mShoppingCartMgr.setRealCast(realCast);
		mShoppingCartMgr.setTotalPrice(totalPrice);
	}

	public OnProcListener getListener() {
		return listener;
	}

	public void setListener(OnProcListener l) {
		listener = l;
	}

	public interface OnProcListener{
		void OnProcCompleted();
	} 
	
}
