package com.hhyg.TyClosing.mgr;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.entities.search.SearchGoodsParam;
import com.hhyg.TyClosing.entities.search.SearchType;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.ui.SearchGoodActivity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ActiveSellListener implements OnClickListener{
	
	@Override
	public void onClick(View v) {
		if(v.getTag() == null){
			return;
		}
		Intent it = new Intent();
		ActiveInfo aInfo = (ActiveInfo) v.getTag();
		if(aInfo.getActiveId() == null || aInfo.getShort_desc() == null){
			return;
		}
		SearchGoodsParam.DataBean bean = new SearchGoodsParam.DataBean();
		bean.setActivityId(aInfo.getActiveId());
		it.putExtra(v.getContext().getString(R.string.search_token),bean);
		it.putExtra(v.getContext().getString(R.string.search_type), SearchType.ACTIVITY.ordinal());
		it.putExtra(v.getContext().getString(R.string.search_desc),aInfo.getShort_desc());
		it.putExtra(v.getContext().getString(R.string.search_content),aInfo.getType_name());
		it.setClass(v.getContext(), SearchGoodActivity.class);
		v.getContext().startActivity(it);
	}
}
