package com.hhyg.TyClosing.mgr;

import com.hhyg.TyClosing.ui.SearchActivity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class HotSearchListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		Intent it = new Intent();
		it.setClass(v.getContext(), SearchActivity.class);
		v.getContext().startActivity(it);
	}

}
