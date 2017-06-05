package com.hhyg.TyClosing.global;

import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.allShop.info.SearchResultInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.util.StringUtil;

import java.util.Map;

//public class JsonPostParamBuilder implements PostParamBuilder{
//	public interface RequestParamBuilder{
//		JSONObject requestParamBuild();
//	}
//	private RequestParamBuilder mRequestParamBuilder;
//	public void setRequestParamBuilder(RequestParamBuilder builder) {
//		mRequestParamBuilder = builder;
//	}
//	@Override
//	public String postParamBuild() {
//		JSONObject param = BaseParamBuild();
//		if(mRequestParamBuilder != null){
//			//param.put("data", mRequestParamBuilder.requestParamBuild());
//			JSONObject obUserJson = mRequestParamBuilder.requestParamBuild();
//			for (Map.Entry<String, Object> entry : obUserJson.entrySet()) {
//				param.put(entry.getKey(), entry.getValue());
//			}
//			String strOp = param.getString("op");
//			assert (StringUtil.isEmpty(strOp) == false);
//		}
//		return param.toString();
//	}
//	private JSONObject BaseParamBuild() {
//		JSONObject param = new JSONObject();
//		param.put("imei", MyApplication.GetInstance().getAndroidId());
//		param.put("device_type", "android");
//		param.put("saleId", ClosingRefInfoMgr.getInstance().getSalerInfo().getSalerId());
//		param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
//		param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
//		return param;
//	}
//}

public class JsonPostParamBuilder{
	static public String makeParam() {
		JSONObject param = getCommon();
		return param.toString();
	}

	static public String makeParam(String strOp) {
		JSONObject param = getCommon();
		param.put("op",strOp);
		return param.toString();
	}

	static public String makeParam(JSONObject obUserJson){
		JSONObject param = getCommon();
		for (Map.Entry<String, Object> entry : obUserJson.entrySet()) {
			param.put(entry.getKey(), entry.getValue());
		}
		String strOp = param.getString("op");
		assert (StringUtil.isEmpty(strOp) == false);
		return param.toString();
	}

	static public JSONObject getCommon(){
		JSONObject param = new JSONObject();
		try {
			param.put("imei", MyApplication.GetInstance().getAndroidId());
			param.put("device_type", "android");
			param.put("saleId", ClosingRefInfoMgr.getInstance().getSalerInfo().getSalerId());
			param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
			param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
			param.put("platformId",3);
		}
		catch (Exception e){

		}
		return param;
	}

	static public com.alibaba.fastjson.JSONObject parseJsonFromString(String msg){
		com.alibaba.fastjson.JSONObject jsonObject = null;
		if(StringUtil.isEmpty(msg) == false) {
			try {
				jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
			} catch (Exception e) {
				Logger.GetInstance().Exception("Json parse error :" + msg);
			}
		}
		if(jsonObject == null)
			jsonObject = new com.alibaba.fastjson.JSONObject();
		return jsonObject;
	}
}