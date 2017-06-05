package com.hhyg.TyClosing.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBaseSimpleAdapter;
import com.hhyg.TyClosing.allShop.adapter.OnItemClickPositionListener;
import com.hhyg.TyClosing.allShop.info.SimpleBrandInfo;
import com.hhyg.TyClosing.allShop.mgr.SearchGoodMgr;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.ui.view.DrawerTitle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchBrandContentFragment extends SearchBaseFragment implements OnClickListener{
	private BrandItemAdapter mBrandItemAdapter;
	private ArrayList<SimpleBrandInfo> BrandsInfo;
	private int chosenIndex;
	
	public SearchBrandContentFragment() {
		super();
	}
	@SuppressLint("ValidFragment")
	public SearchBrandContentFragment(DrawerTitle t, SearchGoodMgr mgr) {
		super(t, mgr);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.searchbrand_frag, container,false);
		findView(view);
		init();
		return view;
	}
	public SimpleBrandInfo getSimpleBrandInfo(int position){
		return BrandsInfo.get(position);
	}
	public int getChosenIndex(){
		return chosenIndex;
	}
	@Override
	public void clearchosenItem() {
		title.setTitle("全部");
		allChosenImg.setVisibility(View.VISIBLE);
		allChoseTitle.setTextColor(Constants.SELECTOR_COLOR);
		chosenIndex = -1;
		mBrandItemAdapter.notifyDataSetChanged();
	}
	@Override
	public void showChoseContent() {
		BrandsInfo = mSearchGoodMgr.searchResult.BrandsInfo;
		mBrandItemAdapter.addItem(BrandsInfo);
	}
	private void init() {
		mBrandItemAdapter = new BrandItemAdapter(getActivity());
		chosenIndex = -1;
		contentListView.setAdapter(mBrandItemAdapter);
		allChoseItem.setOnClickListener(this);
		mBrandItemAdapter.setOnItemClickPositionListener(new OnItemClickPositionListener<SimpleBrandInfo>(){
			@Override
			public void onClick(SimpleBrandInfo item, int position) {
				chosenIndex = position;
				String name = "";
				if(item.BrandName.length()>10){
					name = item.BrandName.substring(0, 10);
				}else{
					name = item.BrandName;
				}
				title.setTitle(name);
				mBrandItemAdapter.notifyDataSetChanged();
			}
		});
	}
	class BrandItemAdapter extends AllShopBaseSimpleAdapter<SimpleBrandInfo,BrandItemAdapter.ViewHolder>{
		public BrandItemAdapter(Context context) {
			super(context);
		}
		class ViewHolder{
			public TextView brandName;
			public ImageView chosenImg;
		}
		@Override
		protected View getConvertView() {
			View convertView = inflateItemView(R.layout.searchitem);
			return convertView;
		}
		@Override
		protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
			viewHolder = new ViewHolder();
			viewHolder.brandName = (TextView)convertView.findViewById(R.id.itemname);
			viewHolder.chosenImg = (ImageView) convertView.findViewById(R.id.chosenimg);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, SimpleBrandInfo item ,int position) {
			viewHolder.brandName.setText(item.BrandName);
			if(chosenIndex == position){
				viewHolder.brandName.setTextColor(Constants.SELECTOR_COLOR);
				viewHolder.chosenImg.setVisibility(View.VISIBLE);
				allChosenImg.setVisibility(View.GONE);
				allChoseTitle.setTextColor(Constants.UNSELECTOR_BLACK_COLOR);
			}else{
				viewHolder.brandName.setTextColor(Constants.UNSELECTOR_BLACK_COLOR);
				viewHolder.chosenImg.setVisibility(View.GONE);
			}
		}
	}
	@Override
	public void onClick(View v) {
		clearchosenItem();
	}

}