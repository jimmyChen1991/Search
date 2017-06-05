package com.hhyg.TyClosing.allShop.adapter;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.info.BrandImgInfo;
import com.hhyg.TyClosing.global.ImageHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class AllShopBrandAdapter extends AllShopBaseAdapter<BrandImgInfo,AllShopBrandAdapter.ViewHolder>{
	public AllShopBrandAdapter(Context context) {
		super(context);
	}
	class ViewHolder{
		public ImageView ImageItem;
	}

	@Override
	protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
		viewHolder = new ViewHolder();
		viewHolder.ImageItem = (ImageView)convertView.findViewById(R.id.brandimg);
		return viewHolder;
	}
	@Override
	protected void bindDataToItemView(ViewHolder viewHolder, BrandImgInfo item) {
		ImageAware imageAware = new ImageViewAware(viewHolder.ImageItem, false);
		ImageLoader.getInstance().displayImage(item.netUri, imageAware, ImageHelper.initBrandPathOption());
	}
	@Override
	protected View getConvertView() {
		View convertView = inflateItemView(R.layout.allshopbrand_item);
		return convertView;
	}
}
