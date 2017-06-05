package com.hhyg.TyClosing.presenter;

import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.handler.SimpleProgressHandler;
import com.hhyg.TyClosing.allShop.info.CateInfo;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.view.BrandTitleView;
import com.hhyg.TyClosing.view.CategoryView;

public class CategotyPresenter extends BasePresenter<CategoryView>{
	private final String CATEGOTY_URI = Constants.getIndexUrl()+"?r=allcates/index";
	private final String HOT_CATEGOTY_URI = Constants.getIndexUrl()+"?r=hotcat/hotcat";
	private CategoryProc mCategoryProc;
	private HotCategoryProc mHotCategoryProc;
	private CateInfo rootCateInfo;
	private SimpleProgressHandler<BrandTitleView> mSimpleProgressHandler = new SimpleProgressHandler<BrandTitleView>();
	public CateInfo getRootCateInfo(){
		return rootCateInfo;
	}
	public CategotyPresenter() {
		super();
		init();
	}
	private void init() {
		mCategoryProc = new CategoryProc();
		mHotCategoryProc = new HotCategoryProc();
		rootCateInfo = new CateInfo();
		rootCateInfo.childInfos = new ArrayList<CateInfo>();
	}
	public void fetchLastedCategotyDate(){
		mView.startProgress();
		fetchHotCateData();
	}
	private void fetchCateData(){
		mHttpRequester.post(CATEGOTY_URI, JsonPostParamBuilder.makeParam(), new NetCallBackHandlerException(mSimpleProgressHandler,mCategoryProc));
	}
	private void fetchHotCateData(){
		mHttpRequester.post(HOT_CATEGOTY_URI,JsonPostParamBuilder.makeParam(), new NetCallBackHandlerException(mSimpleProgressHandler,mHotCategoryProc));
	}
	class HotCategoryProc implements ProcMsgHelper{
		@Override
		public void ProcMsg(String msgBody) throws JSONException, IOException {
			JSONObject jsonObj = JSONObject.parseObject(msgBody);
			JSONArray data = jsonObj.getJSONArray("data");
			CateInfo firstInfo = new CateInfo();
			firstInfo.name = "热门品类";
			ArrayList<CateInfo> result = new ArrayList<CateInfo>();
			for(int idx = 0;idx<data.size();idx++){
				CateInfo secondInfo = new CateInfo();
				JSONObject json =  data.getJSONObject(idx);
				secondInfo.cateId = json.getString("catid");
				secondInfo.name = json.getString("classname");
				secondInfo.netUri = json.getString("imgurl");
				secondInfo.cateLevel = json.getIntValue("level");
				result.add(secondInfo);
			}
			firstInfo.childInfos = result;
			rootCateInfo.childInfos.add(firstInfo);
			mHandler.post(new Runnable(){
				@Override
				public void run() {
					fetchCateData();
				}
			});
		}
	}
	class CategoryProc implements ProcMsgHelper{
		@Override
		public void ProcMsg(String msgBody) throws JSONException, IOException {
			JSONObject jsonObj = JSONObject.parseObject(msgBody);
			JSONArray data = jsonObj.getJSONArray("data");
			for(int idx = 0;idx < data.size();idx++){
				CateInfo firstLevelInfo = new CateInfo();
				JSONObject firstJson = data.getJSONObject(idx);
				firstLevelInfo.cateId = firstJson.getString("id");
				firstLevelInfo.name = firstJson.getString("name");
				firstLevelInfo.netUri = firstJson.getString("image");
				firstLevelInfo.cateLevel = firstJson.getIntValue("level");
				firstLevelInfo.childInfos = new ArrayList<CateInfo>();
				JSONArray secondSubData = firstJson.getJSONArray("subcates");
				for(int idx1 = 0;idx1<secondSubData.size();idx1++){
					CateInfo SecondLevelInfo = new CateInfo();
					JSONObject secondJson = secondSubData.getJSONObject(idx1);
					SecondLevelInfo.cateId = secondJson.getString("id");
					SecondLevelInfo.name = secondJson.getString("name");
					SecondLevelInfo.netUri = secondJson.getString("image");
					SecondLevelInfo.cateLevel = secondJson.getIntValue("level");
					SecondLevelInfo.childInfos = new ArrayList<CateInfo>(); 
					JSONArray ThridSubData = secondJson.getJSONArray("subcates");
					for(int idx2 = 0;idx2<ThridSubData.size();idx2++){
						CateInfo ThridLevelInfo = new CateInfo();
						JSONObject thirdJson = ThridSubData.getJSONObject(idx2);
						ThridLevelInfo.cateId = thirdJson.getString("id");
						ThridLevelInfo.name = thirdJson.getString("name");
						ThridLevelInfo.netUri = thirdJson.getString("image");
						ThridLevelInfo.cateLevel = thirdJson.getIntValue("level");
						ThridLevelInfo.childInfos = null;
						SecondLevelInfo.childInfos.add(ThridLevelInfo);
					}
					firstLevelInfo.childInfos.add(SecondLevelInfo);
				}
				rootCateInfo.childInfos.add(firstLevelInfo);
			}
			mHandler.post(new Runnable(){
				@Override
				public void run() {
					if(mView != null){
						mView.showCategoryView(rootCateInfo);
						mView.disProgress();
					}
				}
			});
		}
	}
}
