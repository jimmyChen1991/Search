package com.hhyg.TyClosing.mgr;

import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.ui.ActiveSellActivity;

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
		it.putExtra("activeId", aInfo.getActiveId());
		it.putExtra("desc", aInfo.getShort_desc());
		it.setClass(v.getContext(), ActiveSellActivity.class);
		v.getContext().startActivity(it);
	}
}
