package com.hhyg.TyClosing.fragment;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBaseSimpleAdapter;
import com.hhyg.TyClosing.allShop.adapter.OnItemClickPositionListener;
import com.hhyg.TyClosing.allShop.info.CateInfo;
import com.hhyg.TyClosing.allShop.info.CateLevelIndexInfo;
import com.hhyg.TyClosing.allShop.info.SearchResultInfo;
import com.hhyg.TyClosing.allShop.mgr.SearchGoodMgr;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.ui.view.DrawerTitle;
import com.hhyg.TyClosing.ui.view.InSideListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchCateContentFragment extends SearchBaseFragment implements View.OnClickListener{
	private CateItemAdapter mCateItemAdapter;
	private CateInfo CatesInfo;
	private CateLevelIndexInfo cateLevelIndex;
	
	public SearchCateContentFragment() {
		super();
	}
	@SuppressLint("ValidFragment")
	public SearchCateContentFragment(DrawerTitle t, SearchGoodMgr mgr) {
		super(t, mgr);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.searchcate_frag, container,false);
		findView(view);
		init();
		return view;
	}
	@Override
	public void showChoseContent(){
		SearchResultInfo info = mSearchGoodMgr.searchResult;
		CatesInfo = info.rootCateInfo;
		mCateItemAdapter.addItem(CatesInfo.childInfos);
	}
	public CateLevelIndexInfo getCateLevelIndexInfo(){
		return cateLevelIndex;
	}
	public String getChosenItemId(){
		return CatesInfo.childInfos.get(cateLevelIndex.secondCateLevelIndex).childInfos.get(cateLevelIndex.thirdCateLevelIndex).cateId;
	}
	private void init(){
		cateLevelIndex = new CateLevelIndexInfo();
		mCateItemAdapter = new CateItemAdapter(getActivity());
		contentListView.setAdapter(mCateItemAdapter);
		allChoseItem.setOnClickListener(this);
	}
	class CateItemAdapter extends AllShopBaseSimpleAdapter<CateInfo,CateItemAdapter.ViewHolder>{
		public CateItemAdapter(Context context) {
			super(context);
		}
		class ViewHolder{
			public TextView secondName;
			public InSideListView insideListView;
		}
		@Override
		protected View getConvertView() {
			View convertView = inflateItemView(R.layout.searchcate_item);
			return convertView;
		}
		@Override
		protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
			viewHolder = new ViewHolder();
			viewHolder.secondName = (TextView)convertView.findViewById(R.id.secondcatetitle);
			viewHolder.insideListView = (InSideListView) convertView.findViewById(R.id.catecontentlv);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, CateInfo item,int position) {
			viewHolder.secondName.setText(item.name);
			final CateItemInsideAdapter adapter = new CateItemInsideAdapter(getActivity());
			adapter.setFatherIndex(position);
			adapter.setOnItemClickPositionListener(new OnItemClickPositionListener<CateInfo>(){
				@Override
				public void onClick(CateInfo item, int position) {
					cateLevelIndex.secondCateLevelIndex = adapter.getFatherIndex();
					cateLevelIndex.thirdCateLevelIndex = position;
					title.setTitle(item.name);
					mCateItemAdapter.notifyDataSetChanged();
				}});
			viewHolder.insideListView.setAdapter(adapter);
			adapter.addItem(item.childInfos);
		}
	} 
	class CateItemInsideAdapter extends AllShopBaseSimpleAdapter<CateInfo,CateItemInsideAdapter.ViewHolder>{
		private int fatherIndex;
		public CateItemInsideAdapter(Context context) {
			super(context);
		}
		public void setFatherIndex(int index){
			fatherIndex = index;
		}
		public int getFatherIndex(){
			return fatherIndex;
		}
		class ViewHolder{
			public TextView thirdName;
			public ImageView chosenImg;
		}
		@Override
		protected View getConvertView() {
			View convertView = inflateItemView(R.layout.searchcate_inside_item);
			return convertView;
		}
		@Override
		protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
			viewHolder = new ViewHolder();
			viewHolder.thirdName = (TextView)convertView.findViewById(R.id.itemname);
			viewHolder.chosenImg = (ImageView) convertView.findViewById(R.id.chosenimg);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, CateInfo item,int position) {
			viewHolder.thirdName.setText(item.name);
			if(cateLevelIndex.secondCateLevelIndex == fatherIndex && cateLevelIndex.thirdCateLevelIndex == position){
				viewHolder.chosenImg.setVisibility(View.VISIBLE);
				viewHolder.thirdName.setTextColor(Constants.SELECTOR_COLOR);
				allChosenImg.setVisibility(View.GONE);
				allChoseTitle.setTextColor(Constants.UNSELECTOR_BLACK_COLOR);
			}else{
				viewHolder.chosenImg.setVisibility(View.GONE);
				viewHolder.thirdName.setTextColor(Constants.UNSELECTOR_BLACK_COLOR);
			}
		}
	}
	@Override
	public void onClick(View v) {
		clearchosenItem();
	}
	@Override
	public void clearchosenItem() {
		choseAllItem();
		cateLevelIndex.secondCateLevelIndex = -1;
		cateLevelIndex.thirdCateLevelIndex = -1;
		mCateItemAdapter.notifyDataSetChanged();
	}
	private void choseAllItem(){
		title.setTitle("全部");
		allChosenImg.setVisibility(View.VISIBLE);
		allChoseTitle.setTextColor(Constants.SELECTOR_COLOR);
	}
}
