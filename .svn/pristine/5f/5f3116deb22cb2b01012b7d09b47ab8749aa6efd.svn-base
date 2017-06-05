package com.hhyg.TyClosing.fragment;


import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.info.BrandImgInfo;
import com.hhyg.TyClosing.presenter.BrandTitlePresenter;
import com.hhyg.TyClosing.ui.BrandActivity;
import com.hhyg.TyClosing.view.BrandTitleView;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

public class BrandTitleFragment extends Fragment implements BrandTitleView{
	private BrandTitlePresenter mTitlePresent;
	private ListView listView;
	private BrandAdapter mBrandAdapter;
	private BrandActivity mBrandActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		View view = inflater.inflate(R.layout.brand_title_frg, container, false);
		mTitlePresent = new BrandTitlePresenter();
		initBrandList(view);
		mBrandActivity = (BrandActivity) getActivity();
		mTitlePresent.attach(this);
		mTitlePresent.fetchLastestBrand();	
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mTitlePresent.detach();
	}
	public void initBrandList(View rootView){
		listView = (ListView)rootView.findViewById(R.id.title_list);		
		mBrandAdapter = new BrandAdapter(getActivity());
		listView.setAdapter(mBrandAdapter);
	}
	@Override
	public void onFetchedBrand(ArrayList<String> result) {
		mBrandAdapter.addItem(result);
		mBrandAdapter.notifyDataSetChanged();
		mBrandActivity.disBrandProgress();
	}
	class BrandAdapter extends BaseAdapter{
		private LayoutInflater mLayoutInflater;
    	private boolean[] btnStatus;
    	private int Titleindex;
    	private Button curButton;
    	private boolean firstIn;
    	private ArrayList<String> mBrands;
		class ViewHolder{
			Button brand;			
		}
		public BrandAdapter(Context context) {
			super();
			mLayoutInflater = (LayoutInflater)getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
			mBrands = new ArrayList<String>();
		}
		public void addItem(ArrayList<String> infos){
			btnStatus = new boolean[infos.size()];
			firstIn = true;
			btnStatus[0] = true;
			Titleindex = 0;
			mBrands = infos;
    		notifyDataSetChanged();
    	}
		@Override
		public int getCount() {
			return mBrands.size();
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
				convertView = mLayoutInflater.inflate(R.layout.brand_title_list, null);
				viewHolder.brand = (Button)convertView.findViewById(R.id.brand_title);
				convertView.setTag(viewHolder);				
			}else{
				viewHolder = (ViewHolder) convertView.getTag();				
			}
			String item = mBrands.get(position);
			final int curPosition = position;
			viewHolder.brand.setText(item);
			if(firstIn == true && position == 0){
				curButton = viewHolder.brand;
				firstIn = false;
				setImgContent(item);
			}
			if(Titleindex == position){
				curButton = viewHolder.brand;
			}
			if(!btnStatus[position]){
				viewHolder.brand.setBackgroundColor(Color.rgb(51, 51, 51));
				viewHolder.brand.setTextColor(Color.rgb(153, 153, 153));
			}else{
				viewHolder.brand.setTextColor(Color.rgb(195, 140, 86));
				viewHolder.brand.setBackgroundResource(R.drawable.catebg);
			}
			viewHolder.brand.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Button btn = (Button) v;
					if(curButton != null){
						curButton.setBackgroundColor(Color.rgb(51, 51, 51));
						curButton.setTextColor(Color.rgb(153, 153, 153));
					}
					curButton = btn;
					btnStatus[curPosition] = true;
					btnStatus[Titleindex] = false;
					Titleindex = curPosition;
					btn.setTextColor(Color.rgb(195, 140, 86));
					btn.setBackgroundResource(R.drawable.catebg);
					String letter = mBrands.get(curPosition);
					setImgContent(letter);
				}
			});
			return convertView;
		}
		private void setImgContent(String letter){
			ArrayList<BrandImgInfo> imgList = mTitlePresent.getLetterInfo(letter);
			BrandImgFragment brandImgFragment = (BrandImgFragment)getFragmentManager().findFragmentById(R.id.brand_img_fragment);
			if(!letter.equals("热门品牌")){
				brandImgFragment.setLetterImg(imgList);
			}else{
				brandImgFragment.setHotBrandImg(imgList);
			}
		}
	}
	@Override
	public void startProgress() {
		
	}
	@Override
	public void disProgress() {
		mBrandActivity.disBrandProgress();
	}
}
