package com.hhyg.TyClosing.ui.adapter;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.RestrictionMgr;
import com.hhyg.TyClosing.ui.GoodsInfoActivity;
import com.hhyg.TyClosing.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by mjf on 2016/11/16.
 */
public class ShopListAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<ShoppingCartInfo> mShopList = new ArrayList<ShoppingCartInfo>();
    private Context mContext;
    public ShopListAdapter(Context context) {
    	mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class SlidingViewHolder {
        ImageView shopImgView;
        ImageView youshui;
        TextView brandView;
        TextView spuNameView;
        TextView msPriceView;
        TextView shopcntView;
        TextView priceCalaView;
        TextView priceTv;
        TextView totalcastTv;
        TextView priceOrign;
        RelativeLayout root;
    }
    
    private View.OnClickListener mGoodsInfoListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String barCode = (String) v.getTag();
			Intent it = new Intent();
			it.putExtra("barcode", barCode);
			it.setClass(mContext, GoodsInfoActivity.class);
			mContext.startActivity(it);
		}
	};
	
    public void setData(ArrayList<ShoppingCartInfo> arr){
        mShopList = arr;
        this.notifyDataSetChanged();
    }
    
    @Override public int getCount() {return mShopList.size();}
    @Override public Object getItem(int position) {return null;}
    @Override public long getItemId(int position) {return 0;}
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        SlidingViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new SlidingViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.ordershoplist, null);
            viewHolder.shopImgView = (ImageView) convertView.findViewById(R.id.ordershopimage);
            viewHolder.brandView = (TextView) convertView.findViewById(R.id.ordershopbrand_shopname);
            viewHolder.shopcntView = (TextView) convertView.findViewById(R.id.shopcnt);
            viewHolder.spuNameView = (TextView) convertView.findViewById(R.id.ordershopspuname);
            viewHolder.msPriceView = (TextView) convertView.findViewById(R.id.ordershopmsprice);
            viewHolder.youshui = (ImageView) convertView.findViewById(R.id.youshui);
            viewHolder.priceOrign = (TextView) convertView.findViewById(R.id.originprice);
            viewHolder.root = (RelativeLayout) convertView.findViewById(R.id.root);
            viewHolder.root.setOnClickListener(mGoodsInfoListener);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SlidingViewHolder) convertView.getTag();
        }
        ShoppingCartInfo info = mShopList.get(position);
        viewHolder.root.setTag(info.barCode);
        String name = "";
        if (info.name.length() > 12) {
            name = info.name.substring(0, 12) + "...";
        } else {
            name = info.name;
        }
        String strUnit = info.brand + "  ";
        if(StringUtil.isEmpty(info.brand)) {
            strUnit = "";
            String clazz = this.getClass().getName();
            String method = Thread.currentThread() .getStackTrace()[2].getMethodName();
            Logger.GetInstance().Error("brand is empty  " + clazz + "  " + method);
        }

        viewHolder.brandView.setText(strUnit + name);
        viewHolder.spuNameView.setText("选择 : " + info.attrInfo);
        if (info.activePrice >= RestrictionMgr.getInstance().FREE_DUTY_THREAD) {
            viewHolder.youshui.setBackgroundResource(R.drawable.youshui);
            viewHolder.youshui.setVisibility(View.VISIBLE);
        } else {
            viewHolder.youshui.setVisibility(View.GONE);
        }
        viewHolder.msPriceView.setText("¥" + info.msPrice);
        viewHolder.shopcntView.setText("x" + info.cnt);

        if(StringUtil.isEmpty(info.price)){
            viewHolder.priceOrign.setVisibility(View.INVISIBLE);
        }
        else{
            viewHolder.priceOrign.setVisibility(View.VISIBLE);
            viewHolder.priceOrign.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
            viewHolder.priceOrign.setText(info.price);
        }
        
    	if(!info.imgUrl.equals(viewHolder.shopImgView.getTag())){
    		viewHolder.shopImgView.setImageBitmap(null);
		}else if(viewHolder.shopImgView.getTag()!=null){
			return convertView;
		}
    	viewHolder.shopImgView.setTag(info.imgUrl);
		ImageAware imageAware = new ImageViewAware(viewHolder.shopImgView, false);
		ImageLoader.getInstance().displayImage(info.imgUrl, imageAware, ImageHelper.initBarcodePathOption());
        return convertView;
    }
}