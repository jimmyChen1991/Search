package com.hhyg.TyClosing.ui;


import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.factory.HotSortfielder;
import com.hhyg.TyClosing.allShop.info.CateLevelIndexInfo;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.allShop.info.SearchPriceInfo;
import com.hhyg.TyClosing.allShop.mgr.SearchGoodMgr;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.fragment.ActiveGoodListsFragment;
import com.hhyg.TyClosing.fragment.SearchBaseFragment;
import com.hhyg.TyClosing.fragment.SearchBrandContentFragment;
import com.hhyg.TyClosing.fragment.SearchCateContentFragment;
import com.hhyg.TyClosing.fragment.SearchPriceContentFragment;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.presenter.ActiveSellPresenter;
import com.hhyg.TyClosing.ui.view.DrawerTitle;
import com.hhyg.TyClosing.ui.view.ProgressBar;
import com.hhyg.TyClosing.ui.view.SimpleProgressBar;
import com.hhyg.TyClosing.view.SearchGoodView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
public class ActiveSellActivity extends Activity implements SearchGoodView,View.OnClickListener{
	private ActiveSellPresenter mSearchGoodPresenter;
	private SearchGoodMgr mSearchGoodMgr = new SearchGoodMgr();
	private ActiveGoodListsFragment mGoodListFragment;
	private SearchCateContentFragment mSearchCateContentFragment;
	private SearchPriceContentFragment mSearchPriceContentFragment;
	private SearchBrandContentFragment mSearchBrandContentFragment;
	private DrawerLayout  Drawer;
	private RelativeLayout cateRelativeLayout;
	private RelativeLayout priceRelativeLayout;
	private RelativeLayout brandRelativeLayout;
	private RelativeLayout curRelativeLayout;
	private TextView cateTitle;
	private TextView cateTitleContent;
	private TextView priceTitle;
	private TextView priceTitleContent;
	private TextView brandTitle;
	private TextView brandTitleContent;
	private TextView curTitle;
	private TextView curTitleContent;
	private Button commitButton;
	private ImageButton clearBtn;
	private ImageButton backBtn;
	private ArrayList<SearchBaseFragment> fragments;
	private final String ACTIVE_ID = "activeId";
	private ProgressBar mProgressBar;
	private boolean isPrivilege;
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
		handlerRestoreSaveInstanceState();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sellactive_aty);
		findView();
		startProgress();
		init();
		initChoseView();
		mSearchGoodPresenter.attach(this);
		mSearchGoodPresenter.fetchFirstSearchGood();
	}
	
	private void handlerRestoreSaveInstanceState(){
		mSearchGoodMgr.searchInfo = new SearchInfo();
		mSearchGoodMgr.setNeedData();
		mSearchGoodMgr.reInitCurPage();
		mSearchGoodMgr.setSearchSortfielder(new HotSortfielder());
	}
	
	private void init() {
		if(getIntent().getStringExtra(ACTIVE_ID) == null){
			Toast.makeText(this, "活动ID异常", Toast.LENGTH_SHORT).show();
			finish();
		}
		isPrivilege = getIntent().getBooleanExtra("isPrivilege",false);
		TextView activityTitle = (TextView) findViewById(R.id.activitytitle);
		if(!isPrivilege){
			activityTitle.setText(getResources().getString(R.string.discountActivity));
		}else{
			activityTitle.setText(getResources().getString(R.string.privilegeActivity));
		}
		mSearchGoodPresenter = new ActiveSellPresenter(mSearchGoodMgr,getIntent().getStringExtra(ACTIVE_ID),isPrivilege);
		if(isPrivilege){
			mSearchGoodPresenter.setPrivilegeName(getIntent().getStringExtra("privilegeName"));
			mGoodListFragment.setPrivilegeView();
		}
		mGoodListFragment.setListener(mSearchGoodPresenter);
		fragments = new ArrayList<>();
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
		if(!isPrivilege){
			mSearchGoodPresenter.fetchData();
		}
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
	}
	private void findView(){
		mProgressBar = new SimpleProgressBar((ImageView) findViewById(R.id.infoOperating));
		backBtn = (ImageButton) findViewById(R.id.backbtn);
		Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mGoodListFragment = (ActiveGoodListsFragment)getFragmentManager().findFragmentById(R.id.goodshow_frag);
		cateRelativeLayout = (RelativeLayout) findViewById(R.id.catechose);
		priceRelativeLayout = (RelativeLayout)findViewById(R.id.pricechose);
		brandRelativeLayout = (RelativeLayout) findViewById(R.id.brandchose);
		cateTitle = (TextView)findViewById(R.id.catetitle);
		priceTitle = (TextView) findViewById(R.id.pricetitle);
		brandTitle = (TextView) findViewById(R.id.brandtitle);
		cateTitleContent = (TextView) findViewById(R.id.catetitlecontent);
		priceTitleContent = (TextView) findViewById(R.id.pricetitlecontent);
		brandTitleContent = (TextView) findViewById(R.id.brandtitlecontent);
		cateRelativeLayout.setOnClickListener(this);
		priceRelativeLayout.setOnClickListener(this);
		brandRelativeLayout.setOnClickListener(this);
		backBtn.setOnClickListener(this);
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
	}
	private void choseCateLayout(){
		cateRelativeLayout.setBackgroundResource(R.drawable.catebg);
		cateTitle.setTextColor(Constants.SELECTOR_COLOR);
		cateTitleContent.setTextColor(Constants.SELECTOR_COLOR);
	}
	public void setPriceTitle(SearchPriceInfo info){
		priceTitleContent.setText(info.MinPrice+"-"+info.MaxPrice);
	}
	public void setBrandTitle(String brandName){
		brandTitleContent.setText(brandName);
	};
	public void setCateTitle(String cateName){
		cateTitleContent.setText(cateName);
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
		case R.id.backbtn:
					finish();
					break;
		case R.id.catechose:
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
		handlerSearchBrandTypeInfo(info);
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
		}
		info.cateId = CateId;
	}

	@Override
	public void setfirstShopContent() {
		clearChosenContent();
		mGoodListFragment.setSearchTitle();
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
		mGoodListFragment.setPrice(totalPrice,cast,fee,comments);
	}
}
