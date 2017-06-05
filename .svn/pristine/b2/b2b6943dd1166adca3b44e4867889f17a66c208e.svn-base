package com.hhyg.TyClosing.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.RecommendAdapter;
import com.hhyg.TyClosing.allShop.info.ReCommendInfo;
import com.hhyg.TyClosing.ui.view.InSideListView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class AllShopRecommendFragment extends AllShopBaseFragment{
	private InSideListView mListView;
	private RecommendAdapter mAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.allshop_recommendgood_frag, container);
		findView(view);
		init();
		return view;
	}
	private void findView(View root) {
		mListView = (InSideListView)root.findViewById(R.id.lv);
	}
	private void init(){
		mAdapter = new RecommendAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(new GoodItemClickListener(getActivity()));
	}
	@Override
	public void setLastestContent() {
		ArrayList<ReCommendInfo> result = mAllShopInfoMgr.getAllShopInfo().recommendInfoList;
		mAdapter.addItem(result);
	}
}
