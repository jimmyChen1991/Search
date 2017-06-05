package com.hhyg.TyClosing.fragment;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.ActiveGoodAdapter;
import com.hhyg.TyClosing.allShop.adapter.ActiveGoodAdapter.ItemAddListener;
import com.hhyg.TyClosing.allShop.factory.HotSortfielder;
import com.hhyg.TyClosing.allShop.factory.LatestTimeSortfielder;
import com.hhyg.TyClosing.allShop.factory.PriceSortfielder;
import com.hhyg.TyClosing.allShop.mgr.SearchGoodMgr;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.ui.ActiveSellActivity;
import com.hhyg.TyClosing.ui.ShopCartActivity;
import com.hhyg.TyClosing.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
public class ActiveGoodListsFragment extends Fragment implements View.OnClickListener{
	private GridView mGridView;
	private ActiveGoodAdapter mAdapter;
    private int visibleLastIndex = 0; //最后的可视项索引 
    @SuppressWarnings("unused")
	private int visibleItemCount; //当前窗口可见项总数  
	private int curretPage;
	private SearchGoodMgr mSearchGoodMgr ;
	private ImageButton SearchByHotBtn;
	private ImageButton SearchByPriceBtn;
	private ImageButton SearchByTimeBtn;
	private ImageButton ChoseBtn;
	private ImageButton mCurBtn;
	private ImageButton mBackTop;
	private Button shopcat;
	private TextView activeDetail;
	private TextView cast;
	private TextView cut;
	private ImageView nullContentImgView;
	private View fullReduceView;
	private TextView fullReduce;
	private ActiveSellActivity mGoodListActivity;
	private int priceStatus; //0为升序，1为降序，-1为不选
	private boolean mCanSrcoll;
	private ItemAddListener listener;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activegood_frag, container);
		findView(view);
		init();
		initCurChoseBtn();
		return view;
	}
	
	public void ClearAdapterData(){
		mAdapter.clear();
	}
	
	public void hanglderScollException(){
		curretPage--;
		setCanSrcoll();
	}
	public void setListener(ItemAddListener listener) {
		this.listener = listener;
		mAdapter.setAddListener(this.listener);
	}

	public void setPrivilegeView(){
		cast.setVisibility(View.GONE);
		cut.setVisibility(View.GONE);
		fullReduceView.setVisibility(View.GONE);
	}

	private void init() {
		mGoodListActivity = (ActiveSellActivity)getActivity();
		mSearchGoodMgr = mGoodListActivity.getSerchGoodMgr();
		priceStatus = -1;
		mAdapter = new ActiveGoodAdapter(getActivity());
		mGridView.setAdapter(mAdapter);
		mGridView.setOnScrollListener(new ScrollStateListener(ImageLoader.getInstance(),true,true));
		curretPage = 1;
		mAdapter.setOnItemClickListener(new GoodItemClickListener(getActivity()));
	}
	
	private void initCurChoseBtn(){
		mCurBtn = SearchByHotBtn;
		setAllBtnCanClick(false);
		setChoseHotSaleStatus();
	}
	private void findView(View root){
		mBackTop = (ImageButton) root.findViewById(R.id.backtotop);
		fullReduceView = root.findViewById(R.id.full);
		fullReduce = (TextView) root.findViewById(R.id.fullin);
		cast = (TextView) root.findViewById(R.id.cast);
		cut = (TextView) root.findViewById(R.id.cut);
		shopcat = (Button) root.findViewById(R.id.goshopcat);
		activeDetail = (TextView) root.findViewById(R.id.activitydetail);
		mGridView = (GridView) root.findViewById(R.id.searchgoodgrid);
		SearchByHotBtn = (ImageButton)root.findViewById(R.id.chosehotsale);
		SearchByPriceBtn = (ImageButton)root.findViewById(R.id.choseprice);
		SearchByTimeBtn = (ImageButton)root.findViewById(R.id.chosenew);
		nullContentImgView = (ImageView) root.findViewById(R.id.nullview);
		ChoseBtn = (ImageButton)root.findViewById(R.id.tochosebtn);
		shopcat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(getActivity(), ShopCartActivity.class);
				getActivity().startActivity(it);
			}
		});
		SearchByHotBtn.setOnClickListener(this);
		SearchByPriceBtn.setOnClickListener(this);
		SearchByTimeBtn.setOnClickListener(this);
		ChoseBtn.setOnClickListener(this);
		mBackTop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mGridView.smoothScrollToPosition(0);
			}
		});
	}
	
	private void setCanSrcoll(){
		mCanSrcoll = true;
	}
	
	private void setCannotSroll(){
		mCanSrcoll = false;
	}
	
	public void setNullContent(){
		activeDetail.setVisibility(View.GONE);
		cast.setVisibility(View.GONE);
		cut.setVisibility(View.GONE);
		fullReduce.setVisibility(View.GONE);
		nullContentImgView.setVisibility(View.VISIBLE);
		mGridView.setVisibility(View.GONE);
		setAllBtnCanClickBeSideCurBtn();
	}
	
	public void resetNullContent(){
		nullContentImgView.setVisibility(View.GONE);
		mGridView.setVisibility(View.VISIBLE);
	}
	
	public void setFirstContent() {
		reInitCurretPage();
		setFullReduceView();
		setGoodListContent();
	}
	public void setSessionContent(){
		setGoodListContent();
	}
	private void setGoodListContent(){
		mBackTop.setVisibility(View.VISIBLE);
		mAdapter.addItem(mSearchGoodMgr.GoodResult);
		setCanSrcoll();
		setAllBtnCanClickBeSideCurBtn();
	}
	private void setFullReduceView(){
		
	}
	
	public void setSearchTitle(){
		final String desc = mSearchGoodMgr.searchResult.title;
		if(desc != null){
			activeDetail.setText(desc);
		}
	}
	
	class ScrollStateListener extends PauseOnScrollListener{
		public ScrollStateListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
				super(imageLoader, pauseOnScroll, pauseOnFling);
		}
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			super.onScrollStateChanged(view, scrollState);
			int itemsLastIndex = mAdapter.getCount() - 1; //数据集最后一项的索引 
	        int lastIndex = itemsLastIndex;
	        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE 
	                && visibleLastIndex == lastIndex&&mCanSrcoll&&(!checkCurPageIsLastPage())) {
	        		setCannotSroll();
		        	curretPage++;
		        	mSearchGoodMgr.setCurretPage(curretPage);
		        	setAllBtnCanClick(false);
		        	mGoodListActivity.fetchSessionInside();
	        } 
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			ActiveGoodListsFragment.this.visibleItemCount = visibleItemCount; 
	        visibleLastIndex = firstVisibleItem + visibleItemCount - 1; 
		}
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	private boolean checkCurPageIsLastPage(){
		if(curretPage == mSearchGoodMgr.searchResult.totalPage){
    		return true;
    	}
		return false;
	}
	private void disChosePriceStatus(){
		SearchByPriceBtn.setBackgroundResource(R.drawable.allshop_search_goodlist_price_normal);
	}
	private void setChosePriceUpStatus(){
		SearchByPriceBtn.setBackgroundResource(R.drawable.allshop_search_goodlist_price_pressed_up);
	}
	private void setChosePriceDownStatus(){
		SearchByPriceBtn.setBackgroundResource(R.drawable.allshop_search_goodlist_price_pressed_down);
	}
	private void setChoseHotSaleStatus(){
		SearchByHotBtn.setBackgroundResource(R.drawable.allshop_search_goodlist_hotsale_pressed);
	}
	private void disChoseHotSaleSatus(){
		SearchByHotBtn.setBackgroundResource(R.drawable.allshop_search_goodlist_hotsale_normal);
	}
	private void setChoseLastedStatus(){
		SearchByTimeBtn.setBackgroundResource(R.drawable.allshop_search_goodlist_newarrival_pressed);
	}
	private void disChoseLastedStatus(){
		SearchByTimeBtn.setBackgroundResource(R.drawable.allshop_search_goodlist_newarrival_normal);
	}
	public void setChoseBtnActiviteStatus(){
		ChoseBtn.setBackgroundResource(R.drawable.allshop_search_goodlist_select);
	}
	public void setChoseBtnNormalStatus(){
		ChoseBtn.setBackgroundResource(R.drawable.allshop_search_goodlist_select_normal);
	}
	private void disChosenCurBtn(){
		disChoseLastedStatus();
		disChoseHotSaleSatus();
		disChosePriceStatus();
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.chosehotsale:
				mCurBtn = SearchByHotBtn;
				setAllBtnCanClick(false);
				disChosenCurBtn();
				setChoseHotSaleStatus();
				fetchHotData();
				clear4FetchSessionInside();
				break;
			case R.id.choseprice:
				mCurBtn = SearchByPriceBtn;
				setAllBtnCanClick(false);
				disChosenCurBtn();
				setChosePriceStatus();
				fetchPriceData();
				clear4FetchSessionInside();
				break;
			case R.id.chosenew:
				mCurBtn = SearchByTimeBtn;
				setAllBtnCanClick(false);
				disChosenCurBtn();
				setChoseLastedStatus();
				fetchLasetData();
				clear4FetchSessionInside();
				break;
			case R.id.tochosebtn:
				openDrawerLayout();
				break;
		}
	}
	public void setAllBtnCanClickBeSideCurBtn(){
		setAllBtnCanClick(true);
		if(!(mCurBtn == SearchByPriceBtn)){
			mCurBtn.setClickable(false);
		}
	}
	private void setAllBtnCanClick(boolean bool){
		SearchByHotBtn.setClickable(bool);
		SearchByTimeBtn.setClickable(bool);
		SearchByPriceBtn.setClickable(bool);
	}
	private void clear4FetchSessionInside(){
		mAdapter.clear();
		reInitCurretPage();
		mGoodListActivity.fetchResetSessionInside();
	}
	public void reInitCurretPage(){
		curretPage = 1;
		mSearchGoodMgr.reInitCurPage();
	}
	private void setChosePriceStatus() {
		if(priceStatus == -1){
			setChosePriceUpStatus();
			priceStatus = 0;
			mSearchGoodMgr.searchInfo.sort = 0;
		}else if(priceStatus == 0){
			setChosePriceDownStatus();
			priceStatus = 1;
			mSearchGoodMgr.searchInfo.sort = 1;
		}else{
			setChosePriceUpStatus();
			priceStatus = 0;
			mSearchGoodMgr.searchInfo.sort = 0;
		}
	}
	private void clearSearchSort(){
		mSearchGoodMgr.searchInfo.sort = -1;
	}
	private void clearPriceSatus(){
		priceStatus = -1;
	}
	private void fetchHotData() {
		clearPriceSatus();
		clearSearchSort();
		mSearchGoodMgr.setSearchSortfielder(new HotSortfielder());
	}
	
	private void fetchPriceData(){
		mSearchGoodMgr.setSearchSortfielder(new PriceSortfielder());
	}
	
	private void fetchLasetData(){
		clearPriceSatus();
		clearSearchSort();
		mSearchGoodMgr.setSearchSortfielder(new LatestTimeSortfielder());
	}
	
	private void openDrawerLayout() {
		mGoodListActivity.openDrawer();
	}
	
	public void setPrice(String totalPrice, String cast2, String fee,String comment) {
		if(totalPrice != null && cast2 != null && fee != null){
			cast.setText("小计   " + Constants.PRICE_TITLE + cast2);
			cut.setText("优惠   " + Constants.PRICE_TITLE + fee);
		}else{
			cast.setText("小计   " + Constants.PRICE_TITLE + "0");
			cut.setText("优惠   " + Constants.PRICE_TITLE + "0");
		}
		if(comment != null && !comment.equals("")){
			fullReduceView.setVisibility(View.VISIBLE);
			fullReduce.setText(Html.fromHtml(comment));
		}else{
			fullReduceView.setVisibility(View.GONE);
		}
	}
}
