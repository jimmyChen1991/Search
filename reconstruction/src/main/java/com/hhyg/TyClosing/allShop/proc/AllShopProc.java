package com.hhyg.TyClosing.allShop.proc;
import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.info.AllShopInfo;
import com.hhyg.TyClosing.allShop.info.BrandImgInfo;
import com.hhyg.TyClosing.allShop.info.CategoryInfo;
import com.hhyg.TyClosing.allShop.info.FlashSaleInfo;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.allShop.info.ReCommendInfo;
import com.hhyg.TyClosing.allShop.info.SpecialInfo;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.info.ActiveInfo;
public class AllShopProc implements ProcMsgHelper{
	private final String BASE_NAME = "ty_pad_index_";
	private final String SLIDER = BASE_NAME + "slider";
	private final String AD = BASE_NAME +"advertising";
	private final String CAT = BASE_NAME +"cate";
	private final String HOT_BRAND = BASE_NAME+"hotbrand";
	private final String GIFT_THEME = BASE_NAME+"giftstheme";
	private final String FLASH_SALA = BASE_NAME+"xianshitehui";
	private final String COMMEND_GOOD = BASE_NAME+"recommendgood";
	private AllShopInfo mAllShopInfo;
	private OnProcListener mProcListener;
	public void setOnProcListener(OnProcListener mProcListener) {
		this.mProcListener = mProcListener;
	}
	public AllShopInfo getAllShopInfo() {
		return mAllShopInfo;
	}
	public AllShopProc() {
		super();
		this.mAllShopInfo = new AllShopInfo();
	}
	@Override
	public void ProcMsg(String msgBody) throws JSONException, IOException {
		JSONObject jsonObj = JSONObject.parseObject(msgBody);
		JSONObject data = jsonObj.getJSONObject("data");
		mAllShopInfo.sliderInfoList = procSlider(data);
		mAllShopInfo.AdInfoList = procAd(data);
		mAllShopInfo.brandInfoList = procBrand(data);
		mAllShopInfo.catInfoList = procCat(data);
		mAllShopInfo.giftList = procGift(data);
		mAllShopInfo.recommendInfoList = procRecommend(data);
		mAllShopInfo.flashInfo = procFlashSale(data);
		mProcListener.OnProcCompleted();
	}
	private ArrayList<SpecialInfo> procGift(JSONObject data) throws JSONException{
		ArrayList<SpecialInfo> result = new ArrayList<SpecialInfo>();
		JSONArray ar = data.getJSONArray(GIFT_THEME);
		result = procSpecial(ar);
		return result;
	}
	private ArrayList<SpecialInfo> procSlider(JSONObject data) throws JSONException{
		ArrayList<SpecialInfo> result = new ArrayList<SpecialInfo>();
		JSONArray ar = data.getJSONArray(SLIDER);
		result = procSpecial(ar);
		return result;
	}
	private ArrayList<SpecialInfo> procAd(JSONObject data) throws JSONException{
		ArrayList<SpecialInfo> result = new ArrayList<SpecialInfo>();
		JSONArray ar = data.getJSONArray(AD);
		result = procSpecial(ar);
		return result;
	}
	private ArrayList<SpecialInfo> procSpecial(JSONArray ar) throws JSONException{
		ArrayList<SpecialInfo> result = new ArrayList<SpecialInfo>();
		for(int idx = 0;idx<ar.size();idx++){
			JSONObject json = ar.getJSONObject(idx);
			SpecialInfo info = new SpecialInfo();
			info.id = json.getString("specialid");
			info.netUri = json.getString("imgurl");
			result.add(info);
		}
		return result;
	}
	private ArrayList<BrandImgInfo> procBrand(JSONObject data) throws JSONException{
		ArrayList<BrandImgInfo> result = new ArrayList<BrandImgInfo>();
		JSONObject baseJson = data.getJSONObject(HOT_BRAND);
		JSONArray ar = baseJson.getJSONArray("hotbrand");
		for(int idx = 0;idx<ar.size();idx++){
			JSONObject json = ar.getJSONObject(idx);
			BrandImgInfo info = new BrandImgInfo();
			info.id = json.getString("brandid");
			info.name = json.getString("brandname");
			info.netUri = json.getString("imgurl");
			result.add(info);
		}
		return result;
	}
	private ArrayList<CategoryInfo> procCat(JSONObject data) throws JSONException{
		ArrayList<CategoryInfo> result = new ArrayList<CategoryInfo>();
		JSONArray ar = data.getJSONArray(CAT);
		for(int idx = 0;idx<ar.size();idx++){
			JSONObject json = ar.getJSONObject(idx);
			CategoryInfo info = new CategoryInfo();
			info.id = json.getString("catid");
			info.netUri = json.getString("imgurl");
			info.cateName = json.getString("name");
			info.level = json.getIntValue("level");
			result.add(info);
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	private ArrayList<ReCommendInfo> procRecommend(JSONObject data) throws JSONException{
		ArrayList<ReCommendInfo> result = new ArrayList<ReCommendInfo>();
		JSONArray ar = data.getJSONArray(COMMEND_GOOD);
		for(int idx = 0;idx<ar.size();idx++){
			JSONObject json = ar.getJSONObject(idx);
			ReCommendInfo info = new ReCommendInfo();
			info.RecommendTile = json.getString("title");
			JSONArray array = json.getJSONArray("goods");
			ArrayList<GoodItemInfo> curResult = new ArrayList<GoodItemInfo>();
			for(int idx1 = 0;idx1<array.size();idx1++){
				JSONObject jsonobj = array.getJSONObject(idx1);
				GoodItemInfo goodInfo = new GoodItemInfo();
				goodInfo.barCode = jsonobj.getString("barcode");
				goodInfo.activeCut = jsonobj.getString("active_cut");
				goodInfo.activePrice= jsonobj.getString("active_price");
				goodInfo.brandName = jsonobj.getString("brandname");
				goodInfo.name = jsonobj.getString("name");
				goodInfo.full = jsonobj.getString("full");
				goodInfo.citPrice = jsonobj.getString("price");
				goodInfo.netUri = jsonobj.getString("image");
				goodInfo.stock = jsonobj.getIntValue("stock");
				JSONObject aInfo_JOBJ = jsonobj.getJSONObject("activeinfo");
				if(aInfo_JOBJ != null){
					ActiveInfo aInfo = ActiveInfoProc.procActiveInfo(aInfo_JOBJ);
					goodInfo.activeInfo = aInfo;
				}
				curResult.add(goodInfo);
			}
			info.GoodList = (ArrayList<GoodItemInfo>) curResult.clone();
			result.add(info);
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	private FlashSaleInfo procFlashSale(JSONObject data) throws JSONException{
		FlashSaleInfo info = new FlashSaleInfo();
		ArrayList<GoodItemInfo> resultList = new ArrayList<GoodItemInfo>();
		JSONObject curObj = data.getJSONObject(FLASH_SALA);
		info.specialId = curObj.getString("specialid");
		JSONArray ar = curObj.getJSONArray("goods");
		for(int idx = 0;idx<ar.size();idx++){
			JSONObject jsonobj = ar.getJSONObject(idx);
			GoodItemInfo goodInfo = new GoodItemInfo();
			goodInfo.barCode = jsonobj.getString("barcode");
			goodInfo.activeCut = jsonobj.getString("active_cut");
			goodInfo.activePrice= jsonobj.getString("active_price");
			goodInfo.brandName = jsonobj.getString("brandname");
			goodInfo.name = jsonobj.getString("name");
			goodInfo.citPrice = jsonobj.getString("price");
			goodInfo.netUri = jsonobj.getString("image");
			goodInfo.stock = jsonobj.getIntValue("stock");
			goodInfo.full = jsonobj.getString("full");
			JSONObject aInfo_JOBJ = jsonobj.getJSONObject("activeinfo");
			if(aInfo_JOBJ != null){
				ActiveInfo aInfo = ActiveInfoProc.procActiveInfo(aInfo_JOBJ);
				goodInfo.activeInfo = aInfo;
			}
			resultList.add(goodInfo);
		}
		info.GoodList = (ArrayList<GoodItemInfo>) resultList.clone();
		return info;
	}
	public interface OnProcListener{
		void OnProcCompleted();
	}
}
