package com.hhyg.TyClosing.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.info.CategoryInfo;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.ui.GoodListActivity;
import com.nostra13.universalimageloader.cache.disc.impl.BrandFileGetter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AllShopCateFragment extends AllShopBaseFragment implements View.OnClickListener{
	private ArrayList<CategoryInfo> mCateInfoList;
	private ArrayList<ImageView> mImageViewList;
	private DisplayImageOptions options;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.allshop_cate_frag, container);
		mImageViewList = new ArrayList<ImageView>();
		findView(view);
		return view;
	}
	private void findView(View root) {
		ImageView cateOne = (ImageView) root.findViewById(R.id.cat1);
		ImageView cateTwo = (ImageView) root.findViewById(R.id.cat2);
		ImageView cateThree = (ImageView) root.findViewById(R.id.cat3);
		ImageView cateFour = (ImageView) root.findViewById(R.id.cat4);
		ImageView cateFive = (ImageView) root.findViewById(R.id.cat5);
		ImageView cateSix = (ImageView) root.findViewById(R.id.cat6);
		mImageViewList.add(cateOne);
		mImageViewList.add(cateTwo);
		mImageViewList.add(cateThree);
		mImageViewList.add(cateFour);
		mImageViewList.add(cateFive);
		mImageViewList.add(cateSix);
	}
	@Override
	public void setLastestContent() {
		options = new DisplayImageOptions.Builder()   
		        .resetViewBeforeLoading(false)  // default  
		        .delayBeforeLoading(1000) 
		        .diskCachePath(new BrandFileGetter())
		        .cacheInMemory(true) // default  
		        .cacheOnDisk(true) // default  
		        .considerExifParams(false) // default  
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT) // default  
		        .bitmapConfig(Bitmap.Config.RGB_565) 
		        .displayer(new SimpleBitmapDisplayer()) // default  
		        .handler(new Handler())
		        .displayer(new RoundedBitmapDisplayer(20))// default  
		        .build();
		mCateInfoList = mAllShopInfoMgr.getAllShopInfo().catInfoList;
		for(int idx = 0;idx<mCateInfoList.size();idx++){
			ImageView view = mImageViewList.get(idx);
			view.setTag(mCateInfoList.get(idx));
			view.setOnClickListener(this);
			ImageAware imageAware = new ImageViewAware(view, false);
			ImageLoader.getInstance().displayImage(mCateInfoList.get(idx).netUri, imageAware, options);
		}
	}
	@Override
	public void onClick(View v) {
		UserTrackMgr.getInstance().clear();
		//UserTrackMgr.getInstance().enter("AllShopCateFragment");
		CategoryInfo item = (CategoryInfo) v.getTag();
        UserTrackMgr.getInstance().onEvent("cataloguetolist", item.cateName);
		jumpToSearchActivity((CategoryInfo) v.getTag());
	}
	private void jumpToSearchActivity(CategoryInfo item) {
		SearchInfo searchInfo = SearchInfo.NewInstance(SearchInfo.CATE_SEARCH, item.id, item.cateName);
		Intent it = new Intent();
		it.putExtra("searchInfo", searchInfo);
		it.setClass(getActivity(), GoodListActivity.class);
		startActivity(it);
	}
}
