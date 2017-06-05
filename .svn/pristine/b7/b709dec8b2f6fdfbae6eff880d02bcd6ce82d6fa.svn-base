package com.hhyg.TyClosing.allShop.proc;

import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.info.ActiveInfo;

public class ActiveInfoProc {
	
	public static ActiveInfo procActiveInfo(JSONObject aInfo_JOBJ){
		ActiveInfo res = null;
		if(aInfo_JOBJ != null){
			res = new ActiveInfo();
			res.setActiveId(aInfo_JOBJ.getString("active_id"));
			res.setName(aInfo_JOBJ.getString("active_name"));
			if(aInfo_JOBJ.getString("active_price") != null){
				res.setActive_price(aInfo_JOBJ.getString("active_price"));
			}
			res.setShort_desc(aInfo_JOBJ.getString("short_desc"));
			res.setType_name(aInfo_JOBJ.getString("type_name"));
			res.setType(aInfo_JOBJ.getString("active_type"));
			res.setPrivilegeType(aInfo_JOBJ.getIntValue("isprivilege"));
		}
		return res;
	}
}
