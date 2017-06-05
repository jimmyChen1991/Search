package com.hhyg.TyClosing.fragment;

import com.hhyg.TyClosing.allShop.adapter.OnItemClickListener;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.ui.GoodsInfoActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class GoodItemClickListener implements OnItemClickListener<GoodItemInfo>,View.OnClickListener{
	private Context context;
	public GoodItemClickListener(Context c) {
		context = c;
	}
	@Override
	public void onClick(View v) {
		jumpToGoodsInfoActivity((String) v.getTag());
	}
	@Override
	public void onClick(GoodItemInfo item) {
		jumpToGoodsInfoActivity(item.barCode);
	}
	private void jumpToGoodsInfoActivity(String barcode){
		Intent it = new Intent();
		it.setClass(context, GoodsInfoActivity.class);
		it.putExtra("barcode",barcode);
		context.startActivity(it);
	}
}
