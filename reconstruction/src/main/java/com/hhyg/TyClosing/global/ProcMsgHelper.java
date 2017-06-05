package com.hhyg.TyClosing.global;
import java.io.IOException;

import com.alibaba.fastjson.JSONException;
public interface ProcMsgHelper{
		
	public void ProcMsg(String msgBody) throws JSONException,IOException;

}
