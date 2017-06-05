package com.hhyg.TyClosing.ui.adapter;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.info.SpuInfo;
import com.hhyg.TyClosing.mgr.DeleteMgr;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private DeleteAdapter mThis;
	private DeleteMgr mDeleteMgr = DeleteMgr.getInstance();
	
	private ArrayList<ShoppingCartInfo> mSpuInfoList;
	private Handler mFatherHandler;
	private ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();

	public DeleteAdapter(Context context, ArrayList<ShoppingCartInfo> selectedSpuInfoList, Handler handler) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSpuInfoList = selectedSpuInfoList;
		mThis = DeleteAdapter.this;
		mFatherHandler = handler;
	}

	@Override
	public int getCount() {
		return mSpuInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public ImageView img;
		public TextView name;
		public TextView brand;
		public ImageButton delete;
		public TextView activePrice;
		public TextView citPrice;	
		public TextView spuName;
		public ImageButton addToShoppingCart;
		public ImageView bottomLayout;
		public ImageView topLayout;
	}

	 private View.OnClickListener mDeleteClickLister = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String barCode = (String) v.getTag();
				int pos = findPos(barCode);
				if(pos >=0) {
					ShoppingCartInfo info = mSpuInfoList.get(pos);
					mDeleteMgr.deleteInfo(info.barCode);
					mThis.notifyDataSetChanged();
					int spuCnt = mSpuInfoList.size();
					if(spuCnt ==0) {
						mFatherHandler.sendEmptyMessage(2);
					}
				}
			}
		};
	
	private int findPos(String barCode) {
		int pos = -1;
		int cnt = mSpuInfoList.size();
		for(int idx =0; idx <cnt;idx++) {
			if(mSpuInfoList.get(idx).barCode.equals(barCode)){
				pos = idx;
				break;
			}
		}
		return pos;
	}
	
	 private View.OnClickListener mAddToShoppingCartClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String barCode = (String) v.getTag();
			int pos = findPos(barCode);
			if(pos >=0) {
				ShoppingCartInfo info = mSpuInfoList.get(pos);
				final int choseCnt;
				if(!ShoppingCartMgr.getInstance().isInfoExist(info.barCode)){
					choseCnt = 1;
				}else{
					choseCnt = ShoppingCartMgr.getInstance().getInfoByBarCode(info.barCode).cnt +1;
				}
				SpuInfo spu = new SpuInfo();
				spu.name = info.name;
				spu.barCode = info.barCode;
				spu.stock = info.stock;	
				spu.citAmount = info.citAmount;
				spu.attrInfo = info.attrInfo;
				spu.activeId = info.activeId;
				spu.msPrice = info.msPrice;
				if(info.imgUrl != null && !info.imgUrl.equals("")){
					ArrayList<String> imgLinks = new ArrayList<String>();
					imgLinks.add(info.imgUrl);
					spu.imageLinks = imgLinks;
				}
				if(mShoppingCartMgr.isInfoExist(info.barCode)){
					mShoppingCartMgr.updateShopCnt(info.barCode,choseCnt);
				}else{
					mShoppingCartMgr.addInfo(spu, info.name, info.brand, 1, info.typeId, info.typeName);
				}
				Toast.makeText(MyApplication.GetInstance(), "加入购物车成功", Toast.LENGTH_SHORT).show();
				mDeleteMgr.deleteInfo(info.barCode);
				mThis.notifyDataSetChanged();
				int spuCnt = mSpuInfoList.size();
				if(spuCnt ==0) {
					mFatherHandler.sendEmptyMessage(2);
				}

			}

		}
	};
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.delete_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView
					.findViewById(R.id.img);
			holder.name = (TextView) convertView
					.findViewById(R.id.name);
			holder.brand = (TextView) convertView
					.findViewById(R.id.brand);
			holder.addToShoppingCart = (ImageButton) convertView.findViewById(R.id.readd_shop);
			holder.delete = (ImageButton) convertView
					.findViewById(R.id.delete);
			holder.bottomLayout = (ImageView)convertView.findViewById(R.id.bottomlayout);
			holder.spuName = (TextView) convertView
					.findViewById(R.id.selectName);	
			holder.activePrice = (TextView) convertView
					.findViewById(R.id.cxjPrice);
			holder.citPrice = (TextView) convertView
					.findViewById(R.id.msjPrice);
			holder.topLayout = (ImageView)convertView.findViewById(R.id.top_layout);
			holder.delete.setOnClickListener(mDeleteClickLister);
			holder.addToShoppingCart.setOnClickListener(mAddToShoppingCartClick);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		ShoppingCartInfo info = mSpuInfoList.get(position);
		holder.delete.setTag(info.barCode);
		holder.addToShoppingCart.setTag(info.barCode);
		holder.citPrice.setText("￥"+info.msPrice);
        String str = null;
        if(info.name.length()>12){
        	str = info.name.substring(0, 12)+"...";
        }else{
        	str = info.name;
        }
		holder.name.setText(str);
		holder.brand.setText(info.brand);
		holder.spuName.setText("选择:  "+info.attrInfo);
		
		int count = mSpuInfoList.size();
		if(position != (count -1))
			holder.bottomLayout.setVisibility(View.GONE);
		if(position != 0)
			holder.topLayout.setVisibility(View.GONE);
		if(position == 0)
			holder.topLayout.setVisibility(View.VISIBLE);
		
		final String tag = (String) holder.img.getTag();
		final String uri = info.imgUrl;
		if(tag != null && !uri.equals(tag)){
			holder.img.setImageBitmap(null);
		}else if(holder.img.getTag()!=null){
			return convertView;
		}
		holder.img.setTag(uri);
		ImageAware imageAware = new ImageViewAware(holder.img, false);
		ImageLoader.getInstance().displayImage(uri, imageAware, ImageHelper.initBarcodePathOption());
		return convertView;
	}

}
