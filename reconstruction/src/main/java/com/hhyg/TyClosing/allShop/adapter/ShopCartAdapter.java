package com.hhyg.TyClosing.allShop.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.fragment.GoodItemClickListener;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.info.ActiveColumns;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.info.ActiveInfo.ActiveType;
import com.hhyg.TyClosing.info.ShopCartItem;
import com.hhyg.TyClosing.mgr.ActiveSellListener;
import com.hhyg.TyClosing.ui.fragment.ShopCartAtyModFragment;
import com.hhyg.TyClosing.ui.fragment.ShopCartAtyModFragment.AtyModListener;
import com.hhyg.TyClosing.ui.view.InSideListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopCartAdapter extends BaseAdapter{
	
	private LayoutInflater mLayoutInflater; 
	private ArrayList<ActiveColumns> mDataSet = new ArrayList<ActiveColumns>();
	private AddCountListener mAddListener;
	private MinusCountListener mMinusListener;
	private DeleteItemListener mDelectLister;
	private AtyModListenerProxy mAtyModProxy;
	
	public ShopCartAdapter(Context context) {
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setmAtyModProxy(AtyModListenerProxy mAtyModProxy) {
		this.mAtyModProxy = mAtyModProxy;
	}
	
	public AddCountListener getmAddListener() {
		return mAddListener;
	}

	public void setmAddListener(AddCountListener mAddListener) {
		this.mAddListener = mAddListener;
	}

	public MinusCountListener getmMinusListener() {
		return mMinusListener;
	}

	public void setmMinusListener(MinusCountListener mMinusListener) {
		this.mMinusListener = mMinusListener;
	}

	public DeleteItemListener getmDelectLister() {
		return mDelectLister;
	}

	public void setmDelectLister(DeleteItemListener mDelectLister) {
		this.mDelectLister = mDelectLister;
	}

	class ViewHolder{
		public TextView typeName;
		public TextView shortDesc;
		public TextView realCast;
		public TextView discount;
		public InSideListView itemLv;
		public ViewGroup castWrap;
		public ImageView privilegeIcon;
	}
	
	
	public void addItem(List<ActiveColumns> items){
		mDataSet.clear();
		mDataSet.addAll(items);
		notifyDataSetChanged();
	}
	
	public void clear(){
		mDataSet.clear();
		notifyDataSetChanged();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder= null;
		if(convertView ==null){
			viewHolder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.shopcart_item, null);
			viewHolder.typeName = (TextView) convertView.findViewById(R.id.typeName);
			viewHolder.shortDesc = (TextView) convertView.findViewById(R.id.shortdesc);
			viewHolder.discount = (TextView) convertView.findViewById(R.id.yyh);
			viewHolder.realCast = (TextView) convertView.findViewById(R.id.xj);
			viewHolder.castWrap = (ViewGroup) convertView.findViewById(R.id.cast);
			viewHolder.itemLv = (InSideListView) convertView.findViewById(R.id.lv);
			viewHolder.privilegeIcon = (ImageView) convertView.findViewById(R.id.privilege_icon);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final ActiveColumns column = mDataSet.get(position);
		final ActiveInfo aInfo = column.getaInfo();
		final CartItemAdapter adapter = new CartItemAdapter(column);
		viewHolder.itemLv.setAdapter(adapter);
		if(aInfo == null || aInfo.getType() == ActiveType.NoStock){
			convertView.setBackgroundResource(R.drawable.shopcart_nostockitem);
			viewHolder.castWrap.setVisibility(View.GONE);
			viewHolder.typeName.setText("无库存");
			viewHolder.shortDesc.setVisibility(View.GONE);
		}else if(aInfo.getType() == ActiveType.Normal){
			convertView.setBackgroundResource(R.drawable.shadow_corner_goods_info);
			viewHolder.castWrap.setVisibility(View.VISIBLE);
			viewHolder.typeName.setText(aInfo.getType_name());
			viewHolder.shortDesc.setVisibility(View.GONE);
			viewHolder.realCast.setText(column.getReal_cast());
			viewHolder.discount.setText(column.getDiscount());
		}else if(aInfo.getType() == ActiveType.Cut){
			convertView.setBackgroundResource(R.drawable.shadow_corner_goods_info);
			viewHolder.castWrap.setVisibility(View.VISIBLE);
			viewHolder.typeName.setText(aInfo.getType_name());
			viewHolder.shortDesc.setText(Html.fromHtml(aInfo.getComments()));
			viewHolder.shortDesc.setTag(aInfo);
			viewHolder.shortDesc.setVisibility(View.VISIBLE);
			viewHolder.realCast.setText(column.getReal_cast());
			viewHolder.discount.setText(column.getDiscount());
		}else{
			convertView.setBackgroundResource(R.drawable.shadow_corner_goods_info);
			viewHolder.castWrap.setVisibility(View.VISIBLE);
			viewHolder.typeName.setText(aInfo.getType_name());
			viewHolder.shortDesc.setText(Html.fromHtml(aInfo.getComments()));
			viewHolder.shortDesc.setVisibility(View.VISIBLE);
			viewHolder.shortDesc.setTag(aInfo);
			viewHolder.realCast.setText(column.getReal_cast());
			viewHolder.discount.setText(column.getDiscount());
		}
		if(aInfo != null && aInfo.getPrivilegeType() == ActiveInfo.PrivilegeType.PRIVILEGE_TYPE){
			viewHolder.privilegeIcon.setVisibility(View.VISIBLE);
		}else{
			viewHolder.privilegeIcon.setVisibility(View.GONE);
		}
		viewHolder.shortDesc.setOnClickListener(new ActiveSellListener());
		return convertView;
	}
	
	class CartItemAdapter extends BaseAdapter{
		private ActiveColumns columns;
		public CartItemAdapter(ActiveColumns p) {
			columns = p;
		}

		class ViewHolder{
			ViewGroup aty_wrap;
			ViewGroup img_wrap;
			ViewGroup name_warp;
			TextView  aty_desc;
			Button    aty_mod;
			ImageView image;
			ImageView youshuiIcon;
			ImageButton addCount;
			ImageButton minusCount;
			ImageButton delect;
			TextView brand;
			TextView name;
			TextView attr;
			TextView cxPrice;
			TextView msPrice;
			TextView cnt;
			TextView stockInfo;
			TextView time;
		}
		
		@Override
		public int getCount() {
			return columns.getCartItems().size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder= null;
			if(convertView ==null){
				viewHolder = new ViewHolder();
				convertView = mLayoutInflater.inflate(R.layout.shopcartlist_item, null);
				viewHolder.aty_wrap = (ViewGroup) convertView.findViewById(R.id.activeinfoswrap);
				viewHolder.aty_desc = (TextView) convertView.findViewById(R.id.aty_desc);
				viewHolder.aty_mod = (Button) convertView.findViewById(R.id.aty_mod);
				viewHolder.image = (ImageView) convertView.findViewById(R.id.img);
				viewHolder.youshuiIcon = (ImageView) convertView.findViewById(R.id.youshui);
				viewHolder.delect = (ImageButton) convertView.findViewById(R.id.x_img);
				viewHolder.brand = (TextView) convertView.findViewById(R.id.brand);
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				viewHolder.attr = (TextView) convertView.findViewById(R.id.selectName);
				viewHolder.addCount = (ImageButton) convertView.findViewById(R.id.add);
				viewHolder.cxPrice = (TextView) convertView.findViewById(R.id.cxjPrice);
				viewHolder.msPrice = (TextView) convertView.findViewById(R.id.msjPrice);
				viewHolder.cnt = (TextView) convertView.findViewById(R.id.cnt);
				viewHolder.stockInfo = (TextView) convertView.findViewById(R.id.no_good);
				viewHolder.minusCount = (ImageButton) convertView.findViewById(R.id.minus);
				viewHolder.time = (TextView) convertView.findViewById(R.id.timetoget);
				viewHolder.img_wrap = (ViewGroup) convertView.findViewById(R.id.imglayout);
				viewHolder.name_warp = (ViewGroup) convertView.findViewById(R.id.textlayout);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final ShopCartItem item = columns.getCartItems().get(position);
			if(item != null ){
				if((item.getActivePrice() != null) && (Double.valueOf(item.getActivePrice())>8000)){
					viewHolder.youshuiIcon.setVisibility(View.VISIBLE);
				}else if((item.getCitPrice() != null) && (Double.valueOf(item.getCitPrice())>8000)){
					viewHolder.youshuiIcon.setVisibility(View.VISIBLE);
				}else{
					viewHolder.youshuiIcon.setVisibility(View.GONE);
				}
				viewHolder.brand.setText(item.getBrand());
				viewHolder.name.setText(item.getName());
				viewHolder.attr.setText(item.getAttrInfo());
				
				viewHolder.img_wrap.setOnClickListener(new GoodItemClickListener(viewHolder.img_wrap.getContext()));
				viewHolder.name_warp.setOnClickListener(new GoodItemClickListener(viewHolder.name_warp.getContext()));
				viewHolder.img_wrap.setTag(item.getBarCode());
				viewHolder.name_warp.setTag(item.getBarCode());
				
				final ActiveInfo aInfo = columns.getaInfo();
				if(aInfo != null && aInfo.getType() == ActiveType.Cut){
					viewHolder.cxPrice.setText(item.getActivePrice());
					viewHolder.cxPrice.setVisibility(View.VISIBLE);
					viewHolder.msPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
					viewHolder.msPrice.getPaint().setAntiAlias(true);
					viewHolder.msPrice.setText(item.getCitPrice());
				}else{
					viewHolder.msPrice.setText(item.getCitPrice());
					viewHolder.cxPrice.setVisibility(View.INVISIBLE);
				}
				if(aInfo != null && aInfo.getType() == ActiveType.NoStock){
					viewHolder.cnt.setText("0");
				}else{
					viewHolder.cnt.setText(""+item.getCnt());
				}
				
				if(item.getStockInfo() != null && item.getStockInfo().length() > 0){
					viewHolder.stockInfo.setVisibility(View.VISIBLE);
					viewHolder.stockInfo.setText(item.getStockInfo());
				}else{
					viewHolder.stockInfo.setVisibility(View.GONE);
				}
				viewHolder.addCount.setTag(item.getBarCode());
				viewHolder.addCount.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mAddListener.onAddCount((String)v.getTag());
					}
				});
				viewHolder.delect.setTag(item.getBarCode());
				viewHolder.delect.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mDelectLister.onDelect((String)v.getTag());
					}
				});
				viewHolder.minusCount.setTag(item.getBarCode());
				viewHolder.minusCount.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMinusListener.onMinusCount((String)v.getTag());
					}
				});
				if(item.getCnt() == 1){
					viewHolder.minusCount.setClickable(false);
					viewHolder.minusCount.setBackgroundResource(R.drawable.button_minus_nop);
				}else{
					viewHolder.minusCount.setClickable(true);
					viewHolder.minusCount.setBackgroundResource(R.drawable.button_minus);
				}
				if(item.getCnt() == item.getStock()){
					viewHolder.addCount.setClickable(false);
					viewHolder.addCount.setBackgroundResource(R.drawable.button_add_nop);
				}else if(aInfo != null && aInfo.getType() == ActiveType.NoStock){
					viewHolder.addCount.setClickable(false);
					viewHolder.addCount.setBackgroundResource(R.drawable.button_add_nop);
				}else{
					viewHolder.addCount.setClickable(true);
					viewHolder.addCount.setBackgroundResource(R.drawable.button_add);
				}
				if(item.getaInfos() != null && item.getaInfos().size() > 1){
					viewHolder.aty_wrap.setVisibility(View.VISIBLE);
					viewHolder.aty_desc.setText(aInfo.getShort_desc());
					viewHolder.aty_mod.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Log.v("event","aty_mod");
							ShopCartAtyModFragment frag = new ShopCartAtyModFragment(aInfo.getActiveId(),item);
							frag.setModLister(new AtyModListener() {
								@Override
								public void onAtyMod(String barcode,String activeId) {
									mAtyModProxy.onProxy(barcode,activeId);
								}
							});
							frag.show(((Activity)v.getContext()).getFragmentManager(), "mod_frag");
						}
					});
				}else{
					viewHolder.aty_wrap.setVisibility(View.GONE);
				}
				final String tag = (String) viewHolder.image.getTag();
				final String uri = item.getImgUrl();
				if(!uri.equals(tag)){
					viewHolder.image.setImageBitmap(null);
				}else if(viewHolder.image.getTag()!=null){
					return convertView;
				}
				viewHolder.image.setTag(uri);
				ImageAware imageAware = new ImageViewAware(viewHolder.image, false);
				ImageLoader.getInstance().displayImage(uri, imageAware, ImageHelper.initBarcodePathOption());
			}
			return convertView;
		}
	}
	
	public interface AddCountListener{
		void onAddCount(String barcode);
	}
	
	public interface MinusCountListener{
		void onMinusCount(String barcode);
	}
	
	public interface DeleteItemListener{
		void onDelect(String barcode);
	}
	
	public interface AtyModListenerProxy{
		void onProxy(String barcode,String id);
	}
}
