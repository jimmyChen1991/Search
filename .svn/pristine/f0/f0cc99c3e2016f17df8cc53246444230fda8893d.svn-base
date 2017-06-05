package com.hhyg.TyClosing.allShop.proc;

import java.io.IOException;
import com.alibaba.fastjson.JSONException;
public class ActiveGoodProc extends BaseActiveGoodProc{
	public ActiveGoodProc() {
		super();
	}
	@Override
	public void ProcMsg(String msgBody) throws JSONException,IOException {
		super.ProcMsg(msgBody);
		mOnSearchListener.OnSearchCompleted();
	}
}
