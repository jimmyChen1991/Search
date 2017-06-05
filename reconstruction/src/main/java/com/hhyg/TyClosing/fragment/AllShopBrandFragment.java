package com.hhyg.TyClosing.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBrandAdapter;
import com.hhyg.TyClosing.allShop.adapter.OnItemClickListener;
import com.hhyg.TyClosing.allShop.info.BrandImgInfo;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.ui.GoodListActivity;
import com.hhyg.TyClosing.ui.view.InSideGridView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class AllShopBrandFragment extends AllShopBaseFragment{
	private ArrayList<BrandImgInfo> mBrandList;
	private InSideGridView mGridView;
	private AllShopBrandAdapter mAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.allshop_brand_frag, container);
		findView(view);
		init();
		return view;
	}
	private void findView(View root){
		mGridView = (InSideGridView) root.findViewById(R.id.brandgrid);
	}
	private void init(){
		mBrandList = new ArrayList<BrandImgInfo>();
		mAdapter = new AllShopBrandAdapter(getActivity());
		mAdapter.setOnItemClickLister(new OnItemClickListener<BrandImgInfo>(){
			@Override
			public void onClick(BrandImgInfo item) {
				jumpToSearchActivity(item);
			}});
		mGridView.setAdapter(mAdapter);
	}
	@Override
	public void setLastestContent() {
		mBrandList = mAllShopInfoMgr.getAllShopInfo().brandInfoList;
		mAdapter.clear();
		mAdapter.addItem(mBrandList);
		mAdapter.notifyDataSetChanged();
	}
	private void jumpToSearchActivity(BrandImgInfo item){
		UserTrackMgr.getInstance().clear();
		UserTrackMgr.getInstance().enter("AllShopBrandFragment");
		UserTrackMgr.getInstance().onEvent("brandtolist",item.name);
		SearchInfo info = SearchInfo.NewInstance(SearchInfo.BRAND_SEARCH, item.id, item.name);
		Intent intent = new Intent(getActivity(), GoodListActivity.class);
		intent.putExtra("searchInfo", info);
		startActivity(intent);
	}
}
