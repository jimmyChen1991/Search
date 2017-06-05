package com.hhyg.TyClosing.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBaseAdapter;
import com.hhyg.TyClosing.allShop.adapter.OnItemClickListener;
import com.hhyg.TyClosing.allShop.info.SpecialInfo;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.ui.SpecialActivity;
import com.hhyg.TyClosing.ui.view.InSideGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AllShopGiftFragment extends AllShopBaseFragment{
	private InSideGridView mGridView;
	private MyAdapter mAdapter;
	private ArrayList<SpecialInfo> mDataList = new ArrayList<SpecialInfo>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.allshop_gift_frag, container);
		findView(view);
		init();
		return view;
	}
	private void init() {
		mAdapter = new MyAdapter(getActivity());
		mGridView.setAdapter(mAdapter);
		mAdapter.setOnItemClickLister(new OnItemClickListener<SpecialInfo>(){
			@Override
			public void onClick(SpecialInfo item) {
				UserTrackMgr.getInstance().onEvent("gifttolist", item.id);
				String SpecialId = item.id;
				jumpToSpecialActivity(SpecialId);
			}});
	}
	private void jumpToSpecialActivity(String specialId) {
		UserTrackMgr.getInstance().clear();
		UserTrackMgr.getInstance().enter("SpecialActivity");
		Intent intent = new Intent(getActivity(), SpecialActivity.class);
		intent.putExtra("specialid", specialId);
		startActivity(intent);
	}
	private void findView(View root){
		mGridView = (InSideGridView)root.findViewById(R.id.giftgrid);
	}
	@Override
	public void setLastestContent() {
		mDataList = mAllShopInfoMgr.getAllShopInfo().giftList;
		mAdapter.clear();
		mAdapter.addItem(mDataList);
	}
	class MyAdapter extends AllShopBaseAdapter<SpecialInfo,MyAdapter.ViewHolder>{
		public MyAdapter(Context context) {
			super(context);
		}
		class ViewHolder{
			public ImageView imgItem;
		}
		@Override
		protected View getConvertView() {
			View convertView = inflateItemView(R.layout.allshopgift_item);
			return convertView;
		}
		@Override
		protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
			viewHolder = new ViewHolder();
			viewHolder.imgItem = (ImageView)convertView.findViewById(R.id.giftimg);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, SpecialInfo item) {
			ImageAware imageAware = new ImageViewAware(viewHolder.imgItem, false);
			ImageLoader.getInstance().displayImage(item.netUri, imageAware, ImageHelper.initSpecialPathOption());
		}
	}
}
