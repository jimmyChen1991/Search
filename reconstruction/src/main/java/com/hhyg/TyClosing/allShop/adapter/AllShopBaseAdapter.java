package com.hhyg.TyClosing.allShop.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class AllShopBaseAdapter<D,V> extends SimpleBaseAdapter<D,V>{
	private OnItemClickListener<D> mOnItemClickListener;
	public AllShopBaseAdapter(Context context) {
		super(context);
	}
	public void setOnItemClickLister(OnItemClickListener<D> listener){
		this.mOnItemClickListener = listener;
	}
	@SuppressWarnings({"unchecked" })
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
		bindDataToItemView(viewHolder,item);
		setupItemViewClickListener(convertView,item);
		return convertView;
	}
	protected void setupItemViewClickListener(View viewHolder, final D item) {
		viewHolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnItemClickListener != null) {
					mOnItemClickListener.onClick(item);
				}
			}
		});
	}
	protected abstract void bindDataToItemView(V viewHolder,D item);
}
