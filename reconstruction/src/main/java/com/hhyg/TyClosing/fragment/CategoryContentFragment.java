package com.hhyg.TyClosing.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBaseAdapter;
import com.hhyg.TyClosing.allShop.adapter.OnItemClickListener;
import com.hhyg.TyClosing.allShop.info.CateInfo;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.ui.GoodListActivity;
import com.hhyg.TyClosing.ui.view.InSideGridView;
import com.hhyg.TyClosing.ui.view.InSideListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryContentFragment extends Fragment{
	private InSideListView mListView;
	private ImageView mHeadView;
	private CateInfo firstLevelCateInfo = new CateInfo();
	private LayoutInflater mLayoutInflater; 
	private CategoryListAdapter mCategoryListAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.categotycontent_frag, container,false);
		findView(view);
		init();
		return view;
	}
	public void showCateInfo(CateInfo fristInfo){
		firstLevelCateInfo = fristInfo;
		mCategoryListAdapter.notifyDataSetChanged();
		mHeadView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToSearchGoodListActivity(firstLevelCateInfo);
			}
		});
		ImageAware imageAware = new ImageViewAware(mHeadView, false); 
		ImageLoader.getInstance().displayImage(firstLevelCateInfo.netUri, imageAware);
	}
	private void findView(View root) {
		mListView = (InSideListView) root.findViewById(R.id.catelv);
		mHeadView = (ImageView) root.findViewById(R.id.cateheadimg);
	}
	private void init() {
		mLayoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		firstLevelCateInfo.childInfos = new ArrayList<CateInfo>();
		mCategoryListAdapter = new CategoryListAdapter();
		mListView.setAdapter(mCategoryListAdapter);
	}
	class CategoryListAdapter extends BaseAdapter{
		class ViewHolder{
			public TextView cateName;
			public ImageButton getMoreBtn;
			public InSideGridView gridView;
		}
		@Override
		public int getCount() {
			return firstLevelCateInfo.childInfos.size();
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
			ViewHolder viewHolder = null;
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView = mLayoutInflater.inflate(R.layout.cate_contentgrid, null);
				viewHolder.cateName = (TextView) convertView.findViewById(R.id.catelevelname);
				viewHolder.getMoreBtn = (ImageButton) convertView.findViewById(R.id.getmorebtn);
				viewHolder.gridView = (InSideGridView) convertView.findViewById(R.id.lv);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final CateInfo info = firstLevelCateInfo.childInfos.get(position);
			viewHolder.cateName.setText(info.name);
			viewHolder.getMoreBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					jumpToSearchGoodListActivity(info);
				}
			});
			CategoryGridAdapter adapter = new CategoryGridAdapter(getActivity());
			adapter.addItem(info.childInfos);
			adapter.setOnItemClickLister(new OnItemClickListener<CateInfo>(){
				@Override
				public void onClick(CateInfo item) {
					jumpToSearchGoodListActivity(item);
				}});
			viewHolder.gridView.setAdapter(adapter);
			return convertView;
		}
	}
	private void jumpToSearchGoodListActivity(CateInfo item){
		SearchInfo searchInfo =	SearchInfo.NewInstance(SearchInfo.CATE_SEARCH, item.cateId, item.name);
		Intent it = new Intent();
		it.setClass(getActivity(), GoodListActivity.class);
		it.putExtra("searchInfo", searchInfo);
		startActivity(it);
	}
	class CategoryGridAdapter extends AllShopBaseAdapter<CateInfo,CategoryGridAdapter.ViewHolder>{
		public CategoryGridAdapter(Context context) {
			super(context);
		}
		class ViewHolder{
			public TextView cateItemName;
			public ImageView cateItemImg;
		}
		@Override
		protected View getConvertView() {
			View view = inflateItemView(R.layout.cate_contentgrid_item);
			return view;
		}
		@Override
		protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
			viewHolder = new ViewHolder();
			viewHolder.cateItemName = (TextView) convertView.findViewById(R.id.cateitemname);
			viewHolder.cateItemImg = (ImageView) convertView.findViewById(R.id.cateitemimg);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, CateInfo item) {
			viewHolder.cateItemName.setText(item.name);
			final String tag = (String) viewHolder.cateItemImg.getTag();
			final String uri = item.netUri;
			if(!uri.equals(tag)){
				viewHolder.cateItemImg.setImageBitmap(null);
			}else if(viewHolder.cateItemImg.getTag()!=null){
				return;
			}
			viewHolder.cateItemImg.setTag(uri);
			ImageAware imageAware = new ImageViewAware(viewHolder.cateItemImg, false);
			ImageLoader.getInstance().displayImage(uri, imageAware, ImageHelper.initBarcodePathOption());
		}
	}

}
