package com.hhyg.TyClosing.allShop.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.info.GoodItemInfo;
import com.hhyg.TyClosing.allShop.info.ReCommendInfo;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.info.ActiveInfo.ActiveType;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.ui.view.InSideGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecommendAdapter extends BaseAdapter{
	private LayoutInflater mLayoutInflater; 
	private ArrayList<ReCommendInfo> mDataSet = new ArrayList<ReCommendInfo>();
	private OnItemClickListener<GoodItemInfo> mOnItemClickListener;
	public RecommendAdapter(Context context) {
		super();
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void addItem(List<ReCommendInfo> items){
		mDataSet.clear();
		mDataSet.addAll(items);
		notifyDataSetChanged();
	}
	public void setOnItemClickListener(OnItemClickListener<GoodItemInfo> listener){
		mOnItemClickListener = listener;
	}
	class ViewHolder{
		public TextView title;
		public InSideGridView grid;
	}
	@Override
	public int getCount() {
		return mDataSet.size();
	}
	@Override
	public Object getItem(int position) {
		return null;
	}
	@Override
	public long getItemId(int position) {
		return 0;
	}
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder= null;
		if(convertView ==null){
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.allshoprecommend_item, null);
			viewHolder.title = (TextView)convertView.findViewById(R.id.toptitle);
			viewHolder.grid = (InSideGridView)convertView.findViewById(R.id.commendgrid);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ReCommendInfo info = mDataSet.get(position);
		viewHolder.title.setText(info.RecommendTile);
		GoodAdapter adapter = new GoodAdapter(info.GoodList);
		viewHolder.grid.setAdapter(adapter);
		return convertView;
	}
	class GoodAdapter extends BaseAdapter{
		private ArrayList<GoodItemInfo> GoodInfos = new ArrayList<GoodItemInfo>();
		class GoodViewHolder{
			public ImageView imgItem;
			public ImageView noStock;
			public TextView brandName;
			public TextView name;
			public TextView activePrice;
			public TextView citPrice;
			public TextView activiteIndicator;
			public ImageView privilegeIcon;
		}
		public GoodAdapter(ArrayList<GoodItemInfo> lists){
			GoodInfos = lists;
		}
		@Override
		public int getCount() {
			return GoodInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GoodViewHolder viewHolder = null;
			if(convertView == null){
				viewHolder = new GoodViewHolder();
				convertView = mLayoutInflater.inflate(R.layout.allshoprecommendgrid_item, null);
				viewHolder.imgItem = (ImageView) convertView.findViewById(R.id.goodimg);
				viewHolder.brandName = (TextView) convertView.findViewById(R.id.brandname);
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				viewHolder.activePrice = (TextView) convertView.findViewById(R.id.acviteprice);
				viewHolder.citPrice = (TextView) convertView.findViewById(R.id.citprice);
				viewHolder.activiteIndicator = (TextView) convertView.findViewById(R.id.activite_indicator_text);
				viewHolder.noStock = (ImageView) convertView.findViewById(R.id.nostockimg);
				viewHolder.privilegeIcon = (ImageView) convertView.findViewById(R.id.privilege_icon);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (GoodViewHolder) convertView.getTag();
			}
			final GoodItemInfo info = GoodInfos.get(position);
			viewHolder.brandName.setText(info.brandName);
			viewHolder.name.setText(info.getGoodName());
			if(info.stock == 0){
				viewHolder.noStock.setVisibility(View.VISIBLE);
			}else{
				viewHolder.noStock.setVisibility(View.GONE);
			}
			
			final ActiveInfo aInfo = info.activeInfo;
			if(aInfo == null || aInfo.getType() == null || aInfo.getType() == ActiveInfo.ActiveType.Normal){
				viewHolder.activiteIndicator.setVisibility(View.INVISIBLE);
				viewHolder.activePrice.setVisibility(View.VISIBLE);
				viewHolder.activePrice.setText(Constants.PRICE_TITLE+info.citPrice);
				viewHolder.citPrice.setVisibility(View.GONE);
			}else if(aInfo.getType() == ActiveType.Cut){
				viewHolder.activiteIndicator.setVisibility(View.VISIBLE);
				viewHolder.activiteIndicator.setText(aInfo.getShort_desc());
				viewHolder.activePrice.setVisibility(View.VISIBLE);
				viewHolder.activePrice.setText(Constants.PRICE_TITLE+aInfo.getActive_price());
				viewHolder.citPrice.setVisibility(View.VISIBLE);
				viewHolder.citPrice.setText(Constants.PRICE_TITLE+info.citPrice);
				viewHolder.citPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				viewHolder.citPrice.getPaint().setAntiAlias(true);
			}else{
				viewHolder.activePrice.setVisibility(View.VISIBLE);
				viewHolder.citPrice.setVisibility(View.GONE);
				viewHolder.activePrice.setText(Constants.PRICE_TITLE+info.citPrice);
				viewHolder.activiteIndicator.setVisibility(View.VISIBLE);
				viewHolder.activiteIndicator.setText(aInfo.getShort_desc());
			}

			if(aInfo != null && aInfo.getPrivilegeType() == ActiveInfo.PrivilegeType.PRIVILEGE_TYPE){
				viewHolder.privilegeIcon.setVisibility(View.VISIBLE);
			}else{
				viewHolder.privilegeIcon.setVisibility(View.GONE);
			}
			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mOnItemClickListener != null){
						UserTrackMgr.getInstance().onEvent("recommendtolist",info.barCode);
						mOnItemClickListener.onClick(info);
					}
				}
			});
			final String tag = (String) viewHolder.imgItem.getTag();
			final String uri = info.netUri;
			if(!uri.equals(tag)){
				viewHolder.imgItem.setImageBitmap(null);
			}else if(viewHolder.imgItem.getTag()!=null){
				return convertView;
			}
			viewHolder.imgItem.setTag(uri);
			ImageAware imageAware = new ImageViewAware(viewHolder.imgItem, false);
			ImageLoader.getInstance().displayImage(uri, imageAware, ImageHelper.initBarcodePathOption());
			return convertView;
		}
	}
}
