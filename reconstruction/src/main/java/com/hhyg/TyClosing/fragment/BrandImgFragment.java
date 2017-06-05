package com.hhyg.TyClosing.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBaseAdapter;
import com.hhyg.TyClosing.allShop.adapter.OnItemClickListener;
import com.hhyg.TyClosing.allShop.info.BrandImgInfo;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.ui.GoodListActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
public class BrandImgFragment extends Fragment{
	private GridView mGridView;
	private BrandGridViewAdapter mAdapter;
	private BrandOnlyImgGridViewAdapter mBrandOnlyImgGridViewAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.brand_img, container);
		findView(view);
		init();
		return view;
	}
	public void setLetterImg(ArrayList<BrandImgInfo> imgList) {
		mAdapter.clear();
		mAdapter.addItem(imgList);
		mGridView.setAdapter(mAdapter);
	}
	public void setHotBrandImg(ArrayList<BrandImgInfo> imgList){
		mBrandOnlyImgGridViewAdapter.clear();
		Logger.GetInstance().Debug("come in "+ imgList.size());
		mBrandOnlyImgGridViewAdapter.addItem(imgList);
		mGridView.setAdapter(mBrandOnlyImgGridViewAdapter);
	}
	private void findView(View root) {
		mGridView = (GridView) root.findViewById(R.id.brandimggrid);
	}
	private void init(){
		mAdapter =  new BrandGridViewAdapter(getActivity());
		mBrandOnlyImgGridViewAdapter = new BrandOnlyImgGridViewAdapter(getActivity());
		mAdapter.setOnItemClickLister(new OnItemClickListener<BrandImgInfo>(){
			@Override
			public void onClick(BrandImgInfo item) {
				jumpToSearchGoodListActivity(item);
			}
		});
		mBrandOnlyImgGridViewAdapter.setOnItemClickLister(new OnItemClickListener<BrandImgInfo>(){
			@Override
			public void onClick(BrandImgInfo item) {
				jumpToSearchGoodListActivity(item);
			}
		});
		mGridView.setAdapter(mBrandOnlyImgGridViewAdapter);
	}
	private void jumpToSearchGoodListActivity(BrandImgInfo item) {
		Intent intent = new Intent(getActivity(), GoodListActivity.class);
		SearchInfo info = SearchInfo.NewInstance(SearchInfo.BRAND_SEARCH, item.id, item.name);
		intent.putExtra("searchInfo",info);
		startActivity(intent);
	}
	class BrandGridViewAdapter extends AllShopBaseAdapter<BrandImgInfo,BrandGridViewAdapter.ViewHolder>{
		public BrandGridViewAdapter(Context context) {
			super(context);
		}
		class ViewHolder{
			public ImageView brandImgItem;
			public TextView BrandName;
		}
		@Override
		protected View getConvertView() {
			View view = inflateItemView(R.layout.brand_imggrid);
			return view;
		}
		@Override
		protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
			viewHolder = new ViewHolder();
			viewHolder.brandImgItem = (ImageView) convertView.findViewById(R.id.brandimg);
			viewHolder.BrandName = (TextView) convertView.findViewById(R.id.brandname);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, BrandImgInfo item) {
			viewHolder.BrandName.setText(item.name);
			final String tag = (String) viewHolder.brandImgItem.getTag();
			final String uri = item.netUri;
			if(!uri.equals(tag)){
				viewHolder.brandImgItem.setImageBitmap(null);
			}else if(viewHolder.brandImgItem.getTag()!=null){
				return;
			}
			viewHolder.brandImgItem.setTag(uri);
			ImageAware imageAware = new ImageViewAware(viewHolder.brandImgItem, false);
			ImageLoader.getInstance().displayImage(item.netUri, imageAware, ImageHelper.initBrandPathOption());
		}
	}
	class BrandOnlyImgGridViewAdapter extends AllShopBaseAdapter<BrandImgInfo, BrandOnlyImgGridViewAdapter.ViewHolder> {
		public BrandOnlyImgGridViewAdapter(Context context) {
			super(context);
		}
		class ViewHolder {
			public ImageView brandImgItem;
		}
		@Override
		protected View getConvertView() {
			View view  = inflateItemView(R.layout.brandhot_item);
			return view;
		}
		@Override
		protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
			viewHolder = new ViewHolder();
			viewHolder.brandImgItem = (ImageView) convertView.findViewById(R.id.brandhotimgitem);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, BrandImgInfo item) {
			ImageAware imageAware = new ImageViewAware(viewHolder.brandImgItem, false);
			ImageLoader.getInstance().displayImage(item.netUri, imageAware, ImageHelper.initBrandPathOption());
		}
	}
}
