package com.hhyg.TyClosing.allShop.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class AllShopBaseSimpleAdapter<D,V> extends SimpleBaseAdapter<D,V>{
	private OnItemClickPositionListener<D> OnItemClickPositionListener;
	public AllShopBaseSimpleAdapter(Context context) {
		super(context);
	}
	public void setOnItemClickPositionListener(OnItemClickPositionListener<D> onItemClickPositionListener) {
		OnItemClickPositionListener = onItemClickPositionListener;
	}
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		V viewHolder = null;
		if(convertView == null){
			convertView = getConvertView();
			viewHolder = getViewInstance(convertView,viewHolder);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (V)convertView.getTag();
		}
		final D item = mDataSet.get(position);
		bindDataToItemView(viewHolder,item,position);
		setupItemViewClickListener(convertView,item,position);
		return convertView;
	}
	private void setupItemViewClickListener(View convertView,final D item, final int position) {
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (OnItemClickPositionListener != null) {
					OnItemClickPositionListener.onClick(item,position);
				}
			}
		});
	}
	protected abstract void bindDataToItemView(V viewHolder,D item,int position);
}
