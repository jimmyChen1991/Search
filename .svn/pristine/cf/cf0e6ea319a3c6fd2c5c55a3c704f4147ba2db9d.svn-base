package com.hhyg.TyClosing.ui;


import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.factory.HotSortfielder;
import com.hhyg.TyClosing.allShop.info.CateLevelIndexInfo;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.allShop.info.SearchPriceInfo;
import com.hhyg.TyClosing.allShop.mgr.SearchGoodMgr;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.fragment.GoodListFragment;
import com.hhyg.TyClosing.fragment.SearchBaseFragment;
import com.hhyg.TyClosing.fragment.SearchBrandContentFragment;
import com.hhyg.TyClosing.fragment.SearchCateContentFragment;
import com.hhyg.TyClosing.fragment.SearchPriceContentFragment;
import com.hhyg.TyClosing.info.ActiveColumns;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.presenter.SearchGoodPresenter;
import com.hhyg.TyClosing.ui.view.AllShopTopEdit;
import com.hhyg.TyClosing.ui.view.DrawerTitle;
import com.hhyg.TyClosing.ui.view.ProgressBar;
import com.hhyg.TyClosing.ui.view.SimpleProgressBar;
import com.hhyg.TyClosing.ui.view.TopEdit;
import com.hhyg.TyClosing.view.SearchGoodView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class GoodListActivity extends Activity implements SearchGoodView,View.OnClickListener{
	private SearchGoodPresenter mSearchGoodPresenter;
	private SearchGoodMgr mSearchGoodMgr = new SearchGoodMgr();
	private GoodListFragment mGoodListFragment;
	private SearchCateContentFragment mSearchCateContentFragment;
	private SearchPriceContentFragment mSearchPriceContentFragment;
	private SearchBrandContentFragment mSearchBrandContentFragment;
	private ImageButton ScanBtn;
	private ImageButton HomeBtn;
	private DrawerLayout  Drawer;
	private RelativeLayout cateRelativeLayout;
	private RelativeLayout priceRelativeLayout;
	private RelativeLayout brandRelativeLayout;
	private RelativeLayout curRelativeLayout;
	private TextView cateTitle;
	private TextView cateTitleContent;
	private TextView cateSplit;
	private TextView priceTitle;
	private TextView priceTitleContent;
	private TextView brandTitle;
	private TextView brandTitleContent;
	private TextView curTitle;
	private TextView curTitleContent;
	private Button commitButton;
	private ImageButton clearBtn;
	private TopEdit topEdit;
	private ProgressBar mProgressBar;
	private ArrayList<SearchBaseFragment> fragments;
	private BroadcastReceiver mReceiver;
	private final int SEARCH_BNRAD_TYPE = 1;
	private final int SEARCH_CATE_TYPE = 2;
	private final String SEARCH_INFO = "searchInfo";
	private String cateId;
	public void openDrawer(){
		Drawer.openDrawer(Gravity.RIGHT);
	} 
	public void closeDrawar(){
		Drawer.closeDrawer(Gravity.RIGHT);
	}
	public void fetchResetSessionInside(){
		mGoodListFragment.resetNullContent();
		mSearchGoodPresenter.fetchSessionSearchGood();
	}
	public void fetchSessionInside(){
		mSearchGoodPresenter.fetchSrollSessionSearchGood();
	}
	public void fetchFirstInside(){
		mSearchGoodPresenter.fetchFirstSearchGood();
	}
	public SearchGoodMgr getSerchGoodMgr(){
		return mSearchGoodMgr;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goodlist);
		findView();
		startProgress();
		topEdit = new AllShopTopEdit(this);
		topEdit.setIconListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(v.getContext(), SearchActivity.class);
				v.getContext().startActivity(it);
				finish();
			}
		});
		topEdit.topEdit();
		handlerRestoreSaveInstanceState(savedInstanceState);
		init();
		initChoseView();
		setFirstCateId();
		mSearchGoodPresenter.attach(this);
		mSearchGoodPresenter.fetchFirstSearchGood();
		Logger.GetInstance().Track("GoodListActivity on Create");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		SearchInfo info = mSearchGoodMgr.searchInfo;
		outState.putParcelable(SEARCH_INFO, info);
		super.onSaveInstanceState(outState);
	}
	
	private void handlerRestoreSaveInstanceState(Bundle savedInstanceState){
		if(savedInstanceState != null){
			mSearchGoodMgr.searchInfo = savedInstanceState.getParcelable(SEARCH_INFO);
		}else{
			mSearchGoodMgr.searchInfo = getIntent().getParcelableExtra(SEARCH_INFO);
		}
		mSearchGoodMgr.setNeedData();
		mSearchGoodMgr.reInitCurPage();
		mSearchGoodMgr.setSearchSortfielder(new HotSortfielder());
	}
	private void init() {
		fragments = new ArrayList<SearchBaseFragment>();
		mSearchGoodPresenter = new SearchGoodPresenter(mSearchGoodMgr);
		mSearchCateContentFragment = new SearchCateContentFragment(new DrawerTitle() {
			@Override
			public void setTitle(String t) {
				cateTitleContent.setText(t);
			}
		},mSearchGoodMgr);
		mSearchPriceContentFragment = new SearchPriceContentFragment(new DrawerTitle() {
			@Override
			public void setTitle(String t) {
				priceTitleContent.setText(t);
			}
		},mSearchGoodMgr);
		mSearchBrandContentFragment = new SearchBrandContentFragment(new DrawerTitle() {
			@Override
			public void setTitle(String t) {
				brandTitleContent.setText(t);
			}
		},mSearchGoodMgr);
		addFragment(mSearchCateContentFragment);
		addFragment(mSearchPriceContentFragment);
		addFragment(mSearchBrandContentFragment);
		getFragmentManager().beginTransaction().hide(mSearchBrandContentFragment).commit();
		getFragmentManager().beginTransaction().hide(mSearchPriceContentFragment).commit();
		fragments.add(mSearchCateContentFragment);
		fragments.add(mSearchPriceContentFragment);
		fragments.add(mSearchBrandContentFragment);
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				finish();
			}
		};
		IntentFilter itFilter = new IntentFilter();
		itFilter.addAction(getResources().getString(R.string.privilege_unbind_receiver));
		registerReceiver(mReceiver,itFilter);
	}
	private void setFirstCateId(){
		if(mSearchGoodMgr.searchInfo.searchType == SEARCH_CATE_TYPE){
			cateId = mSearchGoodMgr.searchInfo.cateId;
		}
	}
	private void addFragment(Fragment fragment) {
		getFragmentManager().beginTransaction().add(R.id.fragcontent, fragment).commit();
    }
    private void choseFragment(Fragment frag) {
    	for(SearchBaseFragment fragment:fragments){
	    	getFragmentManager().beginTransaction().hide(fragment).commit();
    	}
    	getFragmentManager().beginTransaction().show(frag).commit();
    }
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("GoodListActivity");
        Logger.GetInstance().Track("GoodListActivity on onResume");
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("GoodListActivity");
		Logger.GetInstance().Track("GoodListActivity on onPause");
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if((keyCode == KeyEvent.KEYCODE_BACK)&&(Drawer.isDrawerOpen(Gravity.RIGHT))){
			closeDrawar();
		}else{
			return super.onKeyDown(keyCode, event);
		}
		return false;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mSearchGoodPresenter.detach();
		mSearchGoodMgr.clear();
		unregisterReceiver(mReceiver);
	}
	private void findView(){
		mProgressBar = new SimpleProgressBar((ImageView) findViewById(R.id.infoOperating));
		ScanBtn = (ImageButton) findViewById(R.id.scan);
		HomeBtn = (ImageButton)findViewById(R.id.home);
		Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ScanBtn.setOnClickListener(this);
		HomeBtn.setOnClickListener(this);
		mGoodListFragment = (GoodListFragment)getFragmentManager().findFragmentById(R.id.goodshow_frag);
		cateRelativeLayout = (RelativeLayout) findViewById(R.id.catechose);
		priceRelativeLayout = (RelativeLayout)findViewById(R.id.pricechose);
		brandRelativeLayout = (RelativeLayout) findViewById(R.id.brandchose);
		cateTitle = (TextView)findViewById(R.id.catetitle);
		priceTitle = (TextView) findViewById(R.id.pricetitle);
		brandTitle = (TextView) findViewById(R.id.brandtitle);
		cateTitleContent = (TextView) findViewById(R.id.catetitlecontent);
		priceTitleContent = (TextView) findViewById(R.id.pricetitlecontent);
		brandTitleContent = (TextView) findViewById(R.id.brandtitlecontent);
		cateSplit = (TextView) findViewById(R.id.catesplit);
		cateRelativeLayout.setOnClickListener(this);
		priceRelativeLayout.setOnClickListener(this);
		brandRelativeLayout.setOnClickListener(this);
		commitButton = (Button) findViewById(R.id.commitsearchbtn);
		commitButton.setOnClickListener(this);
		clearBtn = (ImageButton) findViewById(R.id.cleanchosebtn);
		clearBtn.setOnClickListener(this);
	}
	private void initChoseView(){
		cateRelativeLayout.setBackgroundResource(R.drawable.catebg);
		cateTitle.setTextColor(Constants.SELECTOR_COLOR);
		cateTitleContent.setTextColor(Constants.SELECTOR_COLOR);
		setCurTitleToCate();
		if(mSearchGoodMgr.searchInfo.searchType == SEARCH_BNRAD_TYPE){
			brandRelativeLayout.setVisibility(View.GONE);
		}else if((mSearchGoodMgr.searchInfo.searchType == SEARCH_CATE_TYPE) && (mSearchGoodMgr.searchInfo.cateLevel == 3)){
			cateRelativeLayout.setVisibility(View.GONE);
			chosePriceLayout();
			setCurTitleToPrice();
			cateSplit.setVisibility(View.GONE);
			choseFragment(mSearchPriceContentFragment);
		}
	}
	private void choseCateLayout(){
		cateRelativeLayout.setBackgroundResource(R.drawable.catebg);
		cateTitle.setTextColor(Constants.SELECTOR_COLOR);
		cateTitleContent.setTextColor(Constants.SELECTOR_COLOR);
	}
	private void reInitTitleLayout(){
		reInitPriceTitle();
		reInitBrandTitle();
		reInitCateTitle();
	}
	private void reInitPriceTitle(){
		priceTitleContent.setText("全部");
	}
	private void reInitBrandTitle(){
		brandTitleContent.setText("全部");
	}
	private void reInitCateTitle(){
		cateTitleContent.setText("全部");
	}
	private void chosePriceLayout(){
		priceRelativeLayout.setBackgroundResource(R.drawable.catebg);
		priceTitle.setTextColor(Constants.SELECTOR_COLOR);
		priceTitleContent.setTextColor(Constants.SELECTOR_COLOR);
	}
	private void choseBrandLayout(){
		brandRelativeLayout.setBackgroundResource(R.drawable.catebg);
		brandTitle.setTextColor(Constants.SELECTOR_COLOR);
		brandTitleContent.setTextColor(Constants.SELECTOR_COLOR);
	}
	private void disCurChoseTitleLayout(){
		curRelativeLayout.setBackgroundColor(Constants.GRAY_COLOR);
		curTitle.setTextColor(Constants.UNSELECTOR_COLOR);
		curTitleContent.setTextColor(Constants.UNSELECTOR_COLOR);
	}
	private void setCurTitleToCate(){
		curRelativeLayout = cateRelativeLayout;
		curTitle = cateTitle;
		curTitleContent = cateTitleContent;
	}
	private void setCurTitleToPrice(){
		curRelativeLayout = priceRelativeLayout;
		curTitle = priceTitle;
		curTitleContent = priceTitleContent;
	}
	private void setCurTitleToBrand(){
		curRelativeLayout = brandRelativeLayout;
		curTitle = brandTitle;
		curTitleContent = brandTitleContent;
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.scan:
				jumpToCaptureActivity();
				break;
			case R.id.home:
				jumpToHomeActivity();
				break;
		}
		switch(v.getId()){
		case  R.id.catechose:
					disCurChoseTitleLayout();
					choseCateLayout();
					setCurTitleToCate();
					choseFragment(mSearchCateContentFragment);
				break;
		case R.id.brandchose:
					disCurChoseTitleLayout();
					choseBrandLayout();
					setCurTitleToBrand();
					choseFragment(mSearchBrandContentFragment);
				break;
		case R.id.pricechose:
					disCurChoseTitleLayout();
					chosePriceLayout();
					setCurTitleToPrice();
					choseFragment(mSearchPriceContentFragment);
				break;
		case R.id.cleanchosebtn:
					clearChosenContent();
					reInitTitleLayout();
					break;
		case R.id.commitsearchbtn:
					mGoodListFragment.ClearAdapterData();
					mGoodListFragment.reInitCurretPage();
					makeSearchRequestInfo();
					commitSearch();
					closeDrawar();
				break;
		}
	}
	private void clearChosenContent() {
		for(SearchBaseFragment frag : fragments){
			frag.clearchosenItem();
		}
	}
	private void commitSearch() {
		checkChoseLight();
		mGoodListFragment.reInitCurretPage();
		fetchResetSessionInside();
	}
	private void checkChoseLight(){
		final boolean priceChoseBool = mSearchPriceContentFragment.getChosenIndex() == -1?true:false;
		final boolean brnadChoseBool = mSearchBrandContentFragment.getChosenIndex() == -1?true:false;
		final boolean cateChoseBool = mSearchCateContentFragment.getCateLevelIndexInfo().secondCateLevelIndex == -1?true:false;
		if(priceChoseBool && brnadChoseBool && cateChoseBool){
			mGoodListFragment.setChoseBtnNormalStatus();
		}else{
			mGoodListFragment.setChoseBtnActiviteStatus();
		}
	}
	
	private void makeSearchRequestInfo() {
		SearchInfo info = mSearchGoodMgr.searchInfo;
		handlerCateInfo(info);
		handlerBrandInfo(info);
		handlerPriceInfo(info);
	}
	
	private void handlerPriceInfo(SearchInfo info) {
		int index = mSearchPriceContentFragment.getChosenIndex();
		SearchPriceInfo priceInfo = null;
		if(index == -1){
			priceInfo = new SearchPriceInfo();
		}else{
			priceInfo = new SearchPriceInfo();
			priceInfo = mSearchPriceContentFragment.getSearchPriceInfo(index);
		}
		info.priceInfo = priceInfo;
	}
	private void handlerBrandInfo(SearchInfo info) {
		if(info.searchType != SEARCH_BNRAD_TYPE){
			handlerSearchBrandTypeInfo(info);
		}
	}
	private void handlerSearchBrandTypeInfo(SearchInfo info){
		int index = mSearchBrandContentFragment.getChosenIndex();
		String BrandId = "";
		if(index == -1){
			BrandId = null;
		}else{
			BrandId = mSearchBrandContentFragment.getSimpleBrandInfo(index).BrandId;
		}
		info.brandId = BrandId;
	}
	private void handlerCateInfo(SearchInfo info){
		handlerSearchCateInfo(info);
	}
	private void handlerSearchCateInfo(SearchInfo info){
		CateLevelIndexInfo LevelInfo = mSearchCateContentFragment.getCateLevelIndexInfo();
		String CateId = "";
		if(LevelInfo.secondCateLevelIndex != -1){
			CateId = mSearchCateContentFragment.getChosenItemId();
		}else if(info.searchType == SEARCH_CATE_TYPE){
			CateId = cateId;
		}
		info.cateId = CateId;
	}
	private void jumpToHomeActivity() {
		Intent it = new Intent();
		it.setClass(this, AllShopActivity.class);
		startActivity(it);
	}
	private void jumpToCaptureActivity() {
		Intent it = new Intent();
		it.setClass(this, CaptureActivity.class);
		startActivity(it);
	}
	@Override
	public void setfirstShopContent() {
		clearChosenContent();
		mGoodListFragment.setFirstContent();
		for(SearchBaseFragment frag : fragments){
			frag.showChoseContent();
		}
	}
	@Override
	public void setSessionShopContent() {
		mGoodListFragment.setSessionContent();
	}
	@Override
	public void startProgress() {
		mProgressBar.startProgress();
	}
	@Override
	public void disProgress() {
		mProgressBar.stopProgress();
	}
	@Override
	public void setNullShopContent() {
		mGoodListFragment.setNullContent();
	}
	@Override
	public void ResetChoseBtn() {
		mGoodListFragment.setAllBtnCanClickBeSideCurBtn();
	}
	@Override
	public void setScollExceptionCanScoll() {
		mGoodListFragment.hanglderScollException();
	}
	@Override
	public void setPrice(String totalPrice, String cast, String fee, String comments) {
	}
}
