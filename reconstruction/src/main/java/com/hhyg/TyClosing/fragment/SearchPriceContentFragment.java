package com.hhyg.TyClosing.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBaseSimpleAdapter;
import com.hhyg.TyClosing.allShop.adapter.OnItemClickPositionListener;
import com.hhyg.TyClosing.allShop.info.SearchPriceInfo;
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

public class SearchPriceContentFragment extends SearchBaseFragment implements OnClickListener{
	private PriceItemAdapter mPriceItemAdapter;
	private ArrayList<SearchPriceInfo> prices;
	private int chosenIndex = -1;
	
	public SearchPriceContentFragment() {
		super();
	}
	@SuppressLint("ValidFragment")
	public SearchPriceContentFragment(DrawerTitle t, SearchGoodMgr mgr) {
		super(t, mgr);
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.searchprice_frag, container,false);
		findView(view);
		init();
		return view;
	}
	@Override
	public void showChoseContent() {
		mPriceItemAdapter.addItem(prices);
	}
	@Override
	public void clearchosenItem() {
		title.setTitle("全部");
		allChosenImg.setVisibility(View.VISIBLE);
		allChoseTitle.setTextColor(Constants.SELECTOR_COLOR);
		chosenIndex = -1;
		mPriceItemAdapter.notifyDataSetChanged();
	}
	public SearchPriceInfo getSearchPriceInfo(int position){
		return prices.get(position);
	}
	public int getChosenIndex(){
		return chosenIndex;
	}
	private void init() {
		initPrices();
		mPriceItemAdapter = new PriceItemAdapter(getActivity());
		contentListView.setAdapter(mPriceItemAdapter);
		allChoseItem.setOnClickListener(this);
		mPriceItemAdapter.setOnItemClickPositionListener(new OnItemClickPositionListener<SearchPriceInfo>(){
			@Override
			public void onClick(SearchPriceInfo item, int position) {
				chosenIndex = position;
				title.setTitle(item.MinPrice + "-" + item.MaxPrice);
				mPriceItemAdapter.notifyDataSetChanged();
			}});
	}
	private void initPrices(){
		prices = new ArrayList<SearchPriceInfo>();
		SearchPriceInfo info = new SearchPriceInfo();
		info.MaxPrice = "99";
		info.MinPrice = "0";
		prices.add(info);
		SearchPriceInfo info1 = new SearchPriceInfo();
		info1.MaxPrice = "299";
		info1.MinPrice = "100";
		prices.add(info1);
		SearchPriceInfo info2 = new SearchPriceInfo();
		info2.MaxPrice = "499";
		info2.MinPrice = "300";
		prices.add(info2);
		SearchPriceInfo info3 = new SearchPriceInfo();
		info3.MaxPrice = "999";
		info3.MinPrice = "500";
		prices.add(info3);
		for(int idx = 1;idx<8;idx++){
			SearchPriceInfo info4 = new SearchPriceInfo();
			info4.MaxPrice = idx+"999";
			info4.MinPrice = idx+"000";
			prices.add(info4);
		}
		SearchPriceInfo info5 = new SearchPriceInfo();
		info5.MaxPrice = "25000";
		info5.MinPrice = "8000";
		prices.add(info5);
	}
	class PriceItemAdapter extends AllShopBaseSimpleAdapter<SearchPriceInfo,PriceItemAdapter.ViewHolder>{
		public PriceItemAdapter(Context context) {
			super(context);
		}
		class ViewHolder{
			public TextView priceName;
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
			viewHolder.priceName = (TextView)convertView.findViewById(R.id.itemname);
			viewHolder.chosenImg = (ImageView) convertView.findViewById(R.id.chosenimg);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, SearchPriceInfo item,int position) {
			viewHolder.priceName.setText(item.MinPrice+"-"+item.MaxPrice);
			if(chosenIndex == position){
				viewHolder.priceName.setTextColor(Constants.SELECTOR_COLOR);
				viewHolder.chosenImg.setVisibility(View.VISIBLE);
				allChosenImg.setVisibility(View.GONE);
				allChoseTitle.setTextColor(Constants.UNSELECTOR_BLACK_COLOR);
			}else{
				viewHolder.priceName.setTextColor(Constants.UNSELECTOR_BLACK_COLOR);
				viewHolder.chosenImg.setVisibility(View.GONE);
			}
		}
	}
	@Override
	public void onClick(View v) {
		clearchosenItem();
	}
}
