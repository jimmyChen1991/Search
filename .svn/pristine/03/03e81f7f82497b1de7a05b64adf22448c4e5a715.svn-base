package com.hhyg.TyClosing.fragment;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBaseAdapter;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.allShop.info.ReCommendInfo;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.ImageHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
public class SpecialContentFragment extends Fragment{
	private TextView reCommendTitle;
	private GridView mGridView;
	private MyAdapter mAdapter;
	private ReCommendInfo mReCommendInfo;
	public SpecialContentFragment(){super();}
	@SuppressLint("ValidFragment")
	public SpecialContentFragment(ReCommendInfo ReCommendInfo) {
		this.mReCommendInfo = ReCommendInfo;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.specialcontent_frag, container,false);
		findView(view);
		init();
		return view;
	}
	public void showSpecialContent(ReCommendInfo info){
		mReCommendInfo = info;
		reCommendTitle.setText(mReCommendInfo.RecommendTile);
		mAdapter.addItem(mReCommendInfo.GoodList);
	}
	private void findView(View root) {
		reCommendTitle = (TextView) root.findViewById(R.id.specialtitle);
		mGridView = (GridView) root.findViewById(R.id.specialgrid);
		reCommendTitle.setText(mReCommendInfo.RecommendTile);
		mAdapter = new MyAdapter(getActivity());
		mAdapter.setOnItemClickLister(new GoodItemClickListener(getActivity()));
		mGridView.setAdapter(mAdapter);
		mAdapter.addItem(mReCommendInfo.GoodList);
	}
	private void init(){
		mGridView.setAdapter(mAdapter);
	}
	class MyAdapter extends AllShopBaseAdapter<GoodItemInfo,MyAdapter.ViewHolder>{
		public MyAdapter(Context context) {
			super(context);
		}
		class ViewHolder{
			public ImageView imgItem;
			public ImageView noStock;
			public TextView brandName;
			public TextView name;
			public TextView activePrice;
			public TextView citPrice;
			public TextView activiteIndictor;
		}
		@Override
		protected View getConvertView() {
			View convertView = inflateItemView(R.layout.allshoprecommendgrid_item);
			return convertView;
		}
		@Override
		protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
			viewHolder = new ViewHolder();
			viewHolder.imgItem = (ImageView) convertView.findViewById(R.id.goodimg);
			viewHolder.brandName = (TextView) convertView.findViewById(R.id.brandname);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.activePrice = (TextView) convertView.findViewById(R.id.acviteprice);
			viewHolder.citPrice = (TextView) convertView.findViewById(R.id.citprice);
			viewHolder.activiteIndictor = (TextView) convertView.findViewById(R.id.activite_indicator_text);
			viewHolder.noStock = (ImageView) convertView.findViewById(R.id.nostockimg);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, GoodItemInfo item) {
			viewHolder.brandName.setText(item.brandName);
			viewHolder.name.setText(item.getGoodName());
			if(item.stock == 0){
				viewHolder.noStock.setVisibility(View.VISIBLE);
			}else{
				viewHolder.noStock.setVisibility(View.GONE);
			}
			if((!item.activeCut.equals("100"))&&(!item.activeCut.equals(""))){
				viewHolder.activiteIndictor.setVisibility(View.VISIBLE);
				viewHolder.activiteIndictor.setText(item.getActiviteCut()+"折");
				viewHolder.activePrice.setVisibility(View.VISIBLE);
				viewHolder.activePrice.setText(Constants.ZUXIAO_TITLE+item.activePrice);
				viewHolder.citPrice.setText(Constants.MIANSHUI_TITLE+item.citPrice);
				viewHolder.citPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				viewHolder.citPrice.getPaint().setAntiAlias(true);
			}else if(!item.full.equals("")){
				viewHolder.activePrice.setVisibility(View.INVISIBLE);
				viewHolder.citPrice.getPaint().reset();
				viewHolder.citPrice.setText(Constants.MIANSHUI_TITLE+item.citPrice);
				viewHolder.citPrice.setTextSize(12);
				viewHolder.citPrice.setTextColor(Constants.UNSELECTOR_COLOR);
				viewHolder.activiteIndictor.setVisibility(View.VISIBLE);
				viewHolder.activiteIndictor.setText("满减");
			}else{
				viewHolder.citPrice.getPaint().reset();
				viewHolder.citPrice.setTextColor(Constants.UNSELECTOR_COLOR);
				viewHolder.citPrice.setTextSize(12);
				viewHolder.activePrice.setVisibility(View.INVISIBLE);
				viewHolder.citPrice.setText(Constants.MIANSHUI_TITLE+item.citPrice);
				viewHolder.activiteIndictor.setVisibility(View.INVISIBLE);
			}
			final String tag = (String) viewHolder.imgItem.getTag();
			final String uri = item.netUri;
			if(!uri.equals(tag)){
				viewHolder.imgItem.setImageBitmap(null);
			}else if(viewHolder.imgItem.getTag()!=null){
				return;
			}
			viewHolder.imgItem.setTag(uri);
			ImageAware imageAware = new ImageViewAware(viewHolder.imgItem, false);
			ImageLoader.getInstance().displayImage(uri, imageAware, ImageHelper.initBarcodePathOption());
		}
	}
}
