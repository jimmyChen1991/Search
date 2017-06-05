package com.hhyg.TyClosing.allShop.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class SimpleBaseAdapter<D,V> extends BaseAdapter{
	protected ArrayList<D> mDataSet = new ArrayList<D>();
	protected Context mContext;
	public SimpleBaseAdapter(Context context) {
		super();
		mContext = context;
	}
	public void addItem(List<D> items){
		items.removeAll(mDataSet);
		mDataSet.addAll(items);
		notifyDataSetChanged();
	}
    public void clear() {
        mDataSet.clear();
        notifyDataSetChanged();
    }
	protected D getDetailItem(int position){
		return mDataSet.get(position);
	}
    protected View inflateItemView(int layoutId) {
        return (View)((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
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
    protected abstract View getConvertView();
	protected abstract V getViewInstance(View convertView,V viewHolder);
}
