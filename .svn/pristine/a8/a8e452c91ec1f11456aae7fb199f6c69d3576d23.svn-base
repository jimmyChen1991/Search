package com.hhyg.TyClosing.presenter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.handler.SimpleHandler;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.info.BaseSkuModel;
import com.hhyg.TyClosing.info.DisplayAttrGroup;
import com.hhyg.TyClosing.info.DisplayAttrItem;
import com.hhyg.TyClosing.info.GoodsInfo;
import com.hhyg.TyClosing.info.SkuAttr;
import com.hhyg.TyClosing.info.SkuAttrGroup;
import com.hhyg.TyClosing.info.SpuInfo;
import com.hhyg.TyClosing.mgr.ActiveInfoComparator;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.mgr.Sku;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.view.GoodInfoView;

import android.os.Message;
import android.widget.Toast;

public class GoodInfoPresenter extends BasePresenter<GoodInfoView>{
	private String GOOD_INFO_URI = Constants.getIndexUrl();
	private GoodInfoProc mGoodInfoProc = new GoodInfoProc();
	private String BarCode;
	private GoodsInfo good;
	
    public GoodInfoPresenter(String b) {
    	BarCode = b;
	}
	public void fetchBarcodeData(){
		if(mView != null){
			mView.startProgress();
		}
		UserTrackMgr.getInstance().onEvent("specialtodetail", BarCode);
		mHttpRequester.post(GOOD_INFO_URI+"?r=goodinfo/getbybarcode", MakeJsonString(),
				new NetCallBackHandlerException(new LocalHandler(), mGoodInfoProc));
	}
	
	protected String MakeJsonString() {
		JSONObject param = new JSONObject();
		param.put("op", "getbybarcode");
		JSONObject data = new JSONObject();
		data.put("barcode", BarCode);
		param.put("imei", MyApplication.GetInstance().getAndroidId());
		param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
		param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
		param.put("data", data);
		return param.toString();
	}
	
	class GoodInfoProc implements ProcMsgHelper {
		@Override
		public void ProcMsg(String msgBody) throws JSONException, IOException {
			JSONObject jsonObj = JSONObject.parseObject(msgBody);
			String op = jsonObj.getString("op");
			if (op.equals("getbybarcode")) {
				int errcode = jsonObj.getIntValue("errcode");
				if (errcode == 1) {
					JSONObject data = jsonObj.getJSONObject("data");
//					boolean newAttr = data.getBooleanValue("is_select_attr");
					boolean newAttr = true;
					good = new GoodsInfo();
					good.name = data.getString("goodname");
					good.brand = data.getString("goodbrand");
					JSONArray spuInfo = data.getJSONArray("spu_info");
					final int citAmount = data.getIntValue("cit_amount");
					good.citCateId = data.getIntValue("cit_cate_id");
					good.typeName = data.getString("cit_name");
					good.brandId = data.getString("goodbrand_id");
					for (int idx = 0; idx < spuInfo.size(); idx++) {
						SpuInfo sInfo = new SpuInfo();
						JSONObject obj = (JSONObject) spuInfo.get(idx);
						sInfo.name = obj.getString("name");
						sInfo.barCode = obj.getString("barcode");
						sInfo.citPrice = obj.getDouble("cit_price");
						sInfo.msPrice = obj.getString("cit_price");
						if(obj.getString("active_price").equals("")){
							sInfo.activePrice = 0;
						}else{
								sInfo.activePrice = obj.getDouble("active_price");
						}
						sInfo.activeCut = obj.getDouble("active_cut");
						sInfo.stock = obj.getIntValue("stock");
						sInfo.citAmount = citAmount;
						sInfo.activeName = obj.getString("active_name");
						sInfo.attrInfo = obj.getString("attr_info");
						sInfo.full = obj.getString("full");
						sInfo.fullReduce = obj.getString("full_reduce");
						sInfo.shelveStatus = obj.getIntValue("shelve_status");
						sInfo.description = obj.getString("description");
						ArrayList<String> imageLinks = new ArrayList<String>();
						JSONArray imageArray = obj.getJSONArray("other_images");
						for(int index = 0;index < imageArray.size();index++){
							String img = imageArray.getString(index);
							imageLinks.add(img);
						}
						sInfo.imageLinks = imageLinks;
						JSONObject aInfo_JOBJ = obj.getJSONObject("activeinfo");
						if(aInfo_JOBJ != null){
							JSONArray activities_JOBJ = aInfo_JOBJ.getJSONArray("activities");
							if(activities_JOBJ != null && activities_JOBJ.size() != 0){
								ArrayList<ActiveInfo> aList = new ArrayList<ActiveInfo>();
								for(int index = 0 ; index < activities_JOBJ.size() ; index ++){
									JSONObject aItem_JOBJ = activities_JOBJ.getJSONObject(index);
									ActiveInfo aInfo = new ActiveInfo();
									aInfo.setActiveId(aItem_JOBJ.getString("id"));
									aInfo.setName(aItem_JOBJ.getString("active_name"));
									aInfo.setPriority(aItem_JOBJ.getIntValue("priority"));
									aInfo.setShort_desc(aItem_JOBJ.getString("short_desc"));
									aInfo.setType(aItem_JOBJ.getString("active_type"));
									aInfo.setType_name(aItem_JOBJ.getString("type_name"));
									aInfo.setPrivilegeType(aItem_JOBJ.getIntValue("isprivilege"));
									if(aItem_JOBJ.getString("type_name") != null){
										aInfo.setDesc(aItem_JOBJ.getString("desc"));
									}
									if(aItem_JOBJ.getString("comments") != null){
										aInfo.setComments(aItem_JOBJ.getString("comments"));
									}
									if(aItem_JOBJ.getString("active_price")!= null){
										aInfo.setActive_price(aItem_JOBJ.getString("active_price"));
									}
									aList.add(aInfo);
								}
//								ActiveInfoComparator comparator = new ActiveInfoComparator();
//								Collections.sort(aList, comparator);
								sInfo.activeInfos = aList;
							}
						}
						JSONArray displayAttrs = obj.getJSONArray("show_attrs");
						ArrayList<DisplayAttrGroup> disGroups = new ArrayList<DisplayAttrGroup>();
						for(int index = 0; index < displayAttrs.size() ; index++){
							DisplayAttrGroup group = new DisplayAttrGroup();
							JSONObject groupObj = displayAttrs.getJSONObject(index);
							group.setName(groupObj.getString("group_name"));
							JSONArray item = groupObj.getJSONArray("item");
							ArrayList<DisplayAttrItem> items = new ArrayList<DisplayAttrItem>();
							if(item != null){
							for(int i = 0;i < item.size(); i ++){
								JSONObject itemObj = item.getJSONObject(i);
								DisplayAttrItem attrItem = new DisplayAttrItem();
								attrItem.setName(itemObj.getString("attr_name"));
								attrItem.setVaule(itemObj.getString("attr_value"));
								items.add(attrItem);
							}}
							group.setAttrItems(items);
							disGroups.add(group);
						}
						sInfo.displayAttrGroups = disGroups;
						ArrayList<DisplayAttrGroup> baseGroups = new ArrayList<DisplayAttrGroup>();
						JSONArray baseAttrs = obj.getJSONArray("base_attrs");
						try{
							baseAttrs = obj.getJSONArray("base_attrs");
							}
						catch (Exception e){
							baseAttrs = new JSONArray();
						}
						for(int index = 0; index < baseAttrs.size() ; index++){
							DisplayAttrGroup group = new DisplayAttrGroup();
							JSONObject groupObj = baseAttrs.getJSONObject(index);
							group.setName(groupObj.getString("group_name"));
							JSONArray item = groupObj.getJSONArray("item");
							ArrayList<DisplayAttrItem> items = new ArrayList<DisplayAttrItem>();
							if(item != null){
							for(int i = 0;i < item.size(); i ++){
								JSONObject itemObj = item.getJSONObject(i);
								DisplayAttrItem attrItem = new DisplayAttrItem();
								attrItem.setName(itemObj.getString("attr_name"));
								attrItem.setVaule(itemObj.getString("attr_value"));
								items.add(attrItem);
								}
							}
							group.setAttrItems(items);
							baseGroups.add(group);
						}
						sInfo.baseAttrGroups = baseGroups;
						if (sInfo.shelveStatus == 1) {
							good.spuInfoList.add(sInfo);
						}
					}
					if(isGoodShelve()){
						if(newAttr){
							postShowGoodMsg();
						}else{
							JSONArray attrgs = data.getJSONArray("chose_attr");
							SkuAttrGroup[] attr_groups = new SkuAttrGroup[attrgs.size()];
							for(int idx = 0;idx <attrgs.size();idx++){
								JSONObject obj = (JSONObject) attrgs.get(idx);
								JSONArray attrs = obj.getJSONArray("attr_value");
								SkuAttr attritems[] = new SkuAttr[attrs.size()];
								for(int index = 0;index < attrs.size() ; index++){
									JSONObject attrobj = (JSONObject) attrs.get(index);
									attritems[index] = new SkuAttr(attrobj.getIntValue("id"), attrobj.getString("value"));
								}
								attr_groups[idx] = new SkuAttrGroup(obj.getString("attr_name"), attritems);
							}
							
							JSONArray skuArray = data.getJSONArray("attr_display");
							Map<String,BaseSkuModel> initSku = new HashMap<String,BaseSkuModel>();
							ArrayList<Integer> ids = new ArrayList<Integer>();
							for(int idx = 0;idx < skuArray.size();idx++){
								JSONObject skuItem = skuArray.getJSONObject(idx);
								for(String key:skuItem.keySet()){
									String barcode = skuItem.getJSONObject(key).getString("barcode");
									if("3147758037654".equals(barcode)){
										String[] ids_str = key.split(";");
										for(int i = 0 ;i < ids_str.length ; i++){
											ids.add(Integer.valueOf(ids_str[i]));
										}
									}
									initSku.put(key, new BaseSkuModel(barcode));
								}
							}
							Collections.sort(ids);
							int[] ids_result = new int[ids.size()]; 
							for(int i = 0; i < ids.size();i++){
								ids_result[i] = ids.get(i);
							}
							Map<String,BaseSkuModel> result = Sku.skuCollection(initSku);
							postAttrGoodMsg(result,attr_groups,ids_result);
						}
						return;
				}
					if(!isGoodShelve()){
						postShowNoGoodMsg();
						return;
					}
				}
			}
		}
	}
	
	private void postShowGoodMsg() {
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				if(mView != null){
					mView.setGoodInfoContent(good);
					mView.disProgress();
				}
			}
		});
	}
	private void postShowNoGoodMsg(){
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				if(mView != null){
					mView.setNoGoodExistContent();
					mView.disProgress();
				}
			}
		});
	}
	private void postAttrGoodMsg(final Map<String,BaseSkuModel> result,final SkuAttrGroup[] attrGroups,final int[] ids){
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				if(mView != null){
					mView.setNewAttrView(result,attrGroups,good,ids);
					mView.disProgress();
				}
			}
		});
		
		
	}
	
	private boolean isGoodShelve() {
		return good.spuInfoList.size()>0;
	}
	
   class LocalHandler extends SimpleHandler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 4:{
					Toast.makeText(MyApplication.GetInstance(), (String) msg.obj, Toast.LENGTH_SHORT).show();
					if(mView != null){
						mView.setExceptionView();
					}
					break;
				}
			}
			if(mView != null){
				mView.disProgress();
			}
		}
	}
}
