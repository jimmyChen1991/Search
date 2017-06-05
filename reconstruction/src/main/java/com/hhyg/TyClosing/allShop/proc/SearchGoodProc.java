package com.hhyg.TyClosing.allShop.proc;

import java.io.IOException;
import com.alibaba.fastjson.JSONException;
public class SearchGoodProc extends BaseSearchGoodProc{
	public SearchGoodProc() {
		super();
	}
	@Override
	public void ProcMsg(String msgBody) throws JSONException,IOException {
		super.ProcMsg(msgBody);
		mOnSearchListener.OnSearchCompleted();
	}
}
