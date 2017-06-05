package com.hhyg.TyClosing.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.info.ActiveInfo.ActiveType;
import com.hhyg.TyClosing.info.BaseSkuModel;
import com.hhyg.TyClosing.info.DisplayAttrGroup;
import com.hhyg.TyClosing.info.DisplayAttrItem;
import com.hhyg.TyClosing.info.GoodsInfo;
import com.hhyg.TyClosing.info.PickUpInfo;
import com.hhyg.TyClosing.info.SkuAttrGroup;
import com.hhyg.TyClosing.info.SpuInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ActiveSellListener;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.hhyg.TyClosing.presenter.GoodInfoPresenter;
import com.hhyg.TyClosing.ui.dialog.CustomGoodsTimeDialog;
import com.hhyg.TyClosing.ui.view.InSideListView;
import com.hhyg.TyClosing.ui.view.ProgressBar;
import com.hhyg.TyClosing.ui.view.SimpleProgressBar;
import com.hhyg.TyClosing.util.StringUtil;
import com.hhyg.TyClosing.view.GoodInfoView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.readystatesoftware.viewbadger.BadgeView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsInfoActivity extends Activity implements GoodInfoView {
	private MyApplication mApp = MyApplication.GetInstance();
	private ViewPager mViewPager;
	private LinearLayout mPropertyLayout;
	private LinearLayout mDisPlayLayout;
	private Button mCurSelectedPropertyButton = null;
	private String mBarCode;
	private ImageView mNoGoods;
	private GoodsInfo mInfo = null;
	private Handler mUiHandler = new Handler(Looper.getMainLooper());
	private ArrayList<Button> mChildBtns = new ArrayList<Button>();
	private RelativeLayout cxPriceLayout;
	private LinearLayout bottomLayout;
	private ScrollView mScrollView;
	private ImageView mNullImg;
	private TextView mBrandNameTextView;
	private TextView mMsTitle;
	private TextView mMsPrice;
	private TextView mCxPrice;
	private TextView mGoodName;
	private TextView mpricefixTitle;
	private TextView countText;
	private TextView mStockInfo;
	private TextView mLeftTimeTv;
	private ViewGroup activeWrap;
	private ImageView activeSplit;
	private WebView descripView;
	private ImageButton mBtShoppingcartDd;
	private ImageButton mHelpBtn;
	private ImageButton mAddCount;
	private ImageButton mMinusCount;
	private Button mBtAdd;
	private ImageButton mBtScan;
	private RelativeLayout mBtShoppingcart;
	private InSideListView mActiveLv;
	private ViewGroup commentView;
	private TextView  comment;
	private SpuInfo curSpuInfo;
	private ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();
	private ClosingRefInfoMgr closingRefInfoMgr = ClosingRefInfoMgr.getInstance();
	private BadgeView mBadgeView;
	private ImageView[] mTips;
	private LinearLayout mTipGroup;
	private GoodInfoPresenter mInfoPresenter;
	private int mleftStock = 0;
	private String mleftTime = null;
	private ProgressBar mProgressBar;
	private int ChosenSpuIndex;
	private final Object lock = new Object();
	private LayoutInflater mInflater;
	private JSONArray mGoodsStockInfo = null;
	private String mBtag = "";
	private PickUpInfo mInfoPickUp = null;
	private GetStockCallback mStockCallback = new GetStockCallback();
	private ArrayList<ArrayList<Button>> mAttrsviewgroup;
	private final String SELECTED = "selected";
	private final String UNSELECTED = "unselected";
	private Map<String, BaseSkuModel> skuResult;
	private boolean newAttr = false;
	private boolean contentSet = false;
	private BroadcastReceiver mReceiver;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_info);
		mBarCode = getIntent().getStringExtra("barcode");
		findView();
		mInfoPresenter = new GoodInfoPresenter(mBarCode);
		mInfoPresenter.attach(this);
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.v("net","receive");
				finish();
			}
		};
		IntentFilter itF = new IntentFilter();
		itF.addAction(getResources().getString(R.string.privilege_unbind_receiver));
		registerReceiver(mReceiver,itF);
		mInfoPresenter.fetchBarcodeData();
		Logger.GetInstance().Track("GoodsInfoActivity on Create");
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
	public void setGoodInfoContent(GoodsInfo info) {
		mInfo = info;
		mBrandNameTextView.setText(mInfo.brand);
		setContent();
		initCurSelectedProBtn();
		PropertySelected(mCurSelectedPropertyButton);
		mScrollView.setVisibility(View.VISIBLE);
		mBtAdd.setClickable(true);
		bottomLayout.setVisibility(View.VISIBLE);
		contentSet = true;
	}

	@Override
	public void setNewAttrView(Map<String, BaseSkuModel> result, SkuAttrGroup[] attrGroups, GoodsInfo info,
			int[] currentIds) {
		this.skuResult = result;
		newAttr = true;
		mInfo = info;
		mBrandNameTextView.setText(mInfo.brand);
		mAttrsviewgroup = new ArrayList<ArrayList<Button>>();
		for (int idx = 0; idx < attrGroups.length; idx++) {
			addAttrGroup(attrGroups[idx], currentIds[idx]);
		}
		setAirPort();
		mApp.setUserSelectAir(mInfoPickUp);
		checkAttrState(null);
		mScrollView.setVisibility(View.VISIBLE);
		mBtAdd.setClickable(true);
		bottomLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void setNoGoodExistContent() {
		mNullImg.setBackgroundResource(R.drawable.shop_noshelve);
		mNullImg.setVisibility(View.VISIBLE);
		Toast.makeText(this, "商品未上架", Toast.LENGTH_LONG).show();
	}

	@Override
	public void setExceptionView() {
		mNullImg.setBackgroundResource(R.drawable.barcode_fail);
		mNullImg.setVisibility(View.VISIBLE);
	}

	private void initCurSelectedProBtn() {
		mCurSelectedPropertyButton = mChildBtns.get(ChosenSpuIndex);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mInfoPresenter.detach();
		unregisterReceiver(mReceiver);
		Logger.GetInstance().Track("GoodsInfoActivity on Destory");
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateBadgeView();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("GoodsInfoActivity");
		Logger.GetInstance().Track("GoodsInfoActivity on onResume");
	}

	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("GoodsInfoActivity");
		Logger.GetInstance().Track("GoodsInfoActivity on onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (contentSet) {
			checkAddShopState();
			refreshCount();
		}
	}

	private void initTips(int pointCnt) {
		// 将点点加入到ViewGroup中
		mTips = null;
		mTips = new ImageView[pointCnt];
		// 动态创建
		for (int i = 0; i < mTips.length; i++) {
			ImageView imageView = new ImageView(this);
			// 控制点间距离
			LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			ll.setMargins(15, 0, 15, 0);
			imageView.setLayoutParams(ll);
			mTips[i] = imageView;
			if (i == 0) {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			mTipGroup.addView(imageView);
		}
	}

	private void updateBadgeView() {
		mBadgeView.setBadgeMargin(0, 5);
		mBadgeView.setBadgeBackgroundColor(Color.rgb(195, 140, 86));
		final int ShopcarCnt = mShoppingCartMgr.getAll().size();
		mBadgeView.setText(String.valueOf(ShopcarCnt));
		mBadgeView.setTextSize(10);
		mBadgeView.show();
	}

	private void findView() {
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDisPlayLayout = (LinearLayout) findViewById(R.id.displayAttrLayout);
		mStockInfo = (TextView) findViewById(R.id.leftcount);
		mAddCount = (ImageButton) findViewById(R.id.add);
		mMinusCount = (ImageButton) findViewById(R.id.minus);
		countText = (TextView) findViewById(R.id.cnt);
		mNullImg = (ImageView) findViewById(R.id.nullimg);
		bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
		mProgressBar = new SimpleProgressBar((ImageView) findViewById(R.id.infoOperating));
		mBrandNameTextView = (TextView) findViewById(R.id.brandTitle);
		mGoodName = (TextView) findViewById(R.id.goodName);
		mPropertyLayout = (LinearLayout) findViewById(R.id.propertyLayout);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		cxPriceLayout = (RelativeLayout) findViewById(R.id.cxpriceLayout);
		mpricefixTitle = (TextView) findViewById(R.id.cxpricefixTitle);
		mMsTitle = (TextView) findViewById(R.id.msfixTitle);
		mMsPrice = (TextView) findViewById(R.id.mspriceTv);
		mCxPrice = (TextView) findViewById(R.id.cxpriceTv);
		mBtAdd = (Button) findViewById(R.id.oprator);
		mBtScan = (ImageButton) findViewById(R.id.button_scan);
		mNoGoods = (ImageView) findViewById(R.id.nogoods);
		mTipGroup = (LinearLayout) findViewById(R.id.viewGroup);
		mBtShoppingcart = (RelativeLayout) findViewById(R.id.button_shoppingcartlayout);
		mBtShoppingcartDd = (ImageButton) findViewById(R.id.button_shoppingcart);
		descripView = (WebView) findViewById(R.id.detailWv);
		mBadgeView = new BadgeView(this, mBtShoppingcartDd);
		mScrollView = (ScrollView) findViewById(R.id.scrollView1);
		mScrollView.setVisibility(View.GONE);
		mHelpBtn = (ImageButton) findViewById(R.id.help);
		mHelpBtn.setVisibility(closingRefInfoMgr.isShopTypeOutside() ? View.VISIBLE : View.GONE);
		mLeftTimeTv = (TextView) findViewById(R.id.timetoget);
		activeWrap = (ViewGroup) findViewById(R.id.activewrap);
		activeSplit = (ImageView) findViewById(R.id.gooddetailsplit1);
		mActiveLv = (InSideListView) findViewById(R.id.activeLv);
		commentView = (ViewGroup) findViewById(R.id.full);
		comment = (TextView) findViewById(R.id.fullin);
		initClickListener();
	}

	private void setContent() {
		initPropertyView();
		initPropertyButtonState();
		setAirPort();
		mApp.setUserSelectAir(mInfoPickUp);
	}

	private void initClickListener() {
		mViewPager.setOnPageChangeListener(new ViewPagerChangeImp());
		mBtAdd.setOnClickListener(mAddToShoppingCart);
		mBtAdd.setClickable(false);
		mBtScan.setOnClickListener(mScanClickLister);
		mBtShoppingcart.setOnClickListener(mShoppingCartClickListener);
		mBtShoppingcartDd.setOnClickListener(mShoppingCartClickListener);
		mHelpBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomGoodsTimeDialog dlg = new CustomGoodsTimeDialog();
				dlg.airName = mInfoPickUp.name;
				dlg.show(getFragmentManager(), "CustomGoodsTimeDialog");
			}
		});
		mBrandNameTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchInfo info = SearchInfo.NewInstance(SearchInfo.BRAND_SEARCH, mInfo.brandId, mInfo.brand);
				Intent intent = new Intent();
				intent.setClass(GoodsInfoActivity.this, GoodListActivity.class);
				intent.putExtra("searchInfo", info);
				startActivity(intent);
			}
		});
		mAddCount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBtag = "add";
				refreshCount();
			}
		});
		mMinusCount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBtag = "minus";
				refreshCount();
			}
		});
	}

	public void setAirPort() {
		final ArrayList<PickUpInfo> list = ClosingRefInfoMgr.getInstance().getAllPickUpAddr();
		int pickUpCnt = list.size();
		final int allBtn[] = new int[] { R.id.button_air1, R.id.button_air2, R.id.button_air3, R.id.button_air4,
				R.id.button_air5 };
		for (int i = 0; i < 5; i++) {
			Button btn = (Button) findViewById(allBtn[i]);
			if (i < pickUpCnt) {
				PickUpInfo info = list.get(i);
				btn.setText(info.name);
				btn.setVisibility(View.VISIBLE);
			} else
				btn.setVisibility(View.GONE);
			btn.setTag(String.valueOf(i));
			if (closingRefInfoMgr.getChosenPickupInfoIndex() == i) {
				btn.setTextColor(Color.parseColor("#c48c56"));
				btn.setBackgroundResource(R.drawable.button_sel);
			}
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					for (int i = 0; i < 5; i++) {
						String strI = String.valueOf(i);
						Button btn = (Button) findViewById(allBtn[i]);
						btn.setSelected(v.getTag().equals(strI));
						int color1 = android.graphics.Color.parseColor("#c48c56");
						int color2 = android.graphics.Color.parseColor("#484848");
						btn.setTextColor(v.getTag().equals(strI) ? color1 : color2);
						btn.setBackgroundResource(
								v.getTag().equals(strI) ? R.drawable.button_sel : R.drawable.button_sel_normal);
						if (v.getTag().equals(strI)) {
							closingRefInfoMgr.setAndSaveChosenPickupInfoIndex(i);
							mInfoPickUp = list.get(i);
							refreshCount();
						}
					}
				}
			});
		}
		mInfoPickUp = list.get(closingRefInfoMgr.getChosenPickupInfoIndex());
		mLeftTimeTv.setText(getGoodsPromptText());
	}

	private int getCurrentCount() {
		String str = countText.getText().toString();
		return Integer.parseInt(str);
	}

	private void addCallBack() {
		if (mBtag.equals("add")) {
			if (mleftStock > getCurrentCount()) {
				countText.setText(String.valueOf(getCurrentCount() + 1));
			} else {
				Toast.makeText(MyApplication.GetInstance(), "库存不足", Toast.LENGTH_SHORT).show();
			}
		}
		mBtag = "";
	}

	private void minusCallback() {
		if (mBtag.equals("minus")) {
			if (getCurrentCount() > 1)
				countText.setText(String.valueOf((getCurrentCount() - 1)));
		}
		mBtag = "";
	}

	private String getGoodsPromptText() {
		if (ClosingRefInfoMgr.getInstance().isShopTypeOutside() == false) {
			return "下单" + mInfoPickUp.prepareTime + "分钟后可提货";
		}
		return null;
	}

	private void refreshCount() {
		setAllButton(false);
		JSONObject refreshJsonParam = makeRefreshCount();
		if (refreshJsonParam == null) {
			return;
		}
		try {
			MyApplication.GetInstance().post(Constants.getServiceUrl(),
					JsonPostParamBuilder.makeParam(refreshJsonParam), mStockCallback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class GetStockCallback implements INetWorkCallBack {
		@Override
		public void PostProcess(int msgId, String msg) {
			if (msgId == mApp.MSG_TYPE_VALUE) {
				try {
					JSONObject jsonObject = null;
					jsonObject = JSONObject.parseObject(msg);
					String errcode = jsonObject.getString("errcode");
					if ("1".equals(errcode)) {
						JSONObject obj = jsonObject.getJSONObject("data");
						mleftTime = obj.getString("time");
						JSONArray arr = obj.getJSONArray("goods_sku");
						mGoodsStockInfo = arr;
						mUiHandler.post(new Runnable() {
							public void run() {
								updateUI();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			mUiHandler.post(new Runnable() {
				public void run() {
					setAllButton(true);
				}
			});
		}
	}

	private JSONObject makeRefreshCount() {
		final SpuInfo info = getCurSpuInfo();
		if (info == null) {
			return null;
		}
		JSONObject mj = new JSONObject();
		mj.put("op", "getstocklist");
		JSONArray arr = new JSONArray();
		JSONObject m = new JSONObject();
		m.put("barcode", info.barCode);
		m.put("count", 1);
		arr.add(0, m);
		m = new JSONObject();
		m.put("deliverplace", mInfoPickUp.id);
		m.put("goods_sku", arr);
		mj.put("data", m);
		return mj;
	}

	private void updateUI() {
		if (mGoodsStockInfo.size() != 1)
			return;
		JSONObject obj = mGoodsStockInfo.getJSONObject(0);
		mleftStock = Integer.parseInt(obj.getString("stock"));
		SpuInfo curSpuInfo = getCurSpuInfo();
		curSpuInfo.name = obj.getString("name");
		curSpuInfo.barCode = obj.getString("barcode");
		curSpuInfo.citPrice = obj.getDouble("cit_price");
		curSpuInfo.activePrice = obj.getDouble("active_price");
		curSpuInfo.activeCut = obj.getDouble("active_cut");
		curSpuInfo.activeName = obj.getString("active_name");
		curSpuInfo.attrInfo = obj.getString("attr_info");
		curSpuInfo.full = obj.getString("full");
		curSpuInfo.fullReduce = obj.getString("full_reduce");
		curSpuInfo.shelveStatus = obj.getIntValue("shelve_status");
		curSpuInfo.stock = mleftStock;
		int i = -0, j = 0;
		String str = "商品限购数量" + curSpuInfo.citAmount + "件";
		if (mleftStock < 8) {
			str += "    库存紧张";
		} else if (mleftStock < curSpuInfo.citAmount) {
			str += "    仅剩" + mleftStock + "件";
			i = str.indexOf("剩") + 1;
			j = str.length() - 1;
		}
		setPrice();
		if (!newAttr) {
			upNewBtnState();
		}
		Spannable word = new SpannableString(str);
		if (i != 0)
			word.setSpan(new ForegroundColorSpan(Color.RED), i, j, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		mStockInfo.setText(word);

		str = getGoodsPromptText();
		mLeftTimeTv.setText(StringUtil.isEmpty(str) ? mleftTime : str);

		if (mleftStock == 0) {
			mStockInfo.setText("暂时无货");
		}

		if (getCurrentCount() == mleftStock) {
			str = "商品限购数量" + curSpuInfo.citAmount + "件 ";
			str += "    仅剩" + mleftStock + "件";
			i = str.indexOf("剩") + 1;
			j = str.length() - 1;
			word = new SpannableString(str);
			if (i != 0)
				word.setSpan(new ForegroundColorSpan(Color.RED), i, j, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			mStockInfo.setText(word);
		}

		if (getCurrentCount() == curSpuInfo.citAmount) {
			str = null;
			if (StringUtil.isEmpty(mInfo.typeName)) {
				str = "此商品单次离岛限购" + curSpuInfo.citAmount + "件";
			} else {
				str = mInfo.typeName + "类商品单次离岛限购" + curSpuInfo.citAmount + "件";
			}
			Toast.makeText(mApp, str, Toast.LENGTH_SHORT).show();
		}
		if ("add".equals(mBtag)) {
			addCallBack();
		} else if ("minus".equals(mBtag)) {
			minusCallback();
		}
		if (getCurrentCount() == 1) {
			mMinusCount.setClickable(false);
			mMinusCount.setBackgroundResource(R.drawable.button_minus_nop);
		} else {
			mMinusCount.setClickable(true);
			mMinusCount.setBackgroundResource(R.drawable.button_minus);
		}
		if (getCurrentCount() < mleftStock && getCurrentCount() < curSpuInfo.citAmount) {
			mAddCount.setClickable(true);
			mAddCount.setBackgroundResource(R.drawable.button_add);
		} else {
			mAddCount.setClickable(false);
			mAddCount.setBackgroundResource(R.drawable.button_add_nop);
		}
		if ("addcar".equals(mBtag)) {
			addGoodsToCar();
		}
	}

	private void initPropertyButtonState() {
		for (int idx = 0; idx < mInfo.spuInfoList.size(); idx++) {
			final String barCode = mInfo.spuInfoList.get(idx).barCode;
			final boolean existInBarCode = mShoppingCartMgr.isInfoExist(barCode);
			if (isChosenSpuIndex(idx) && existInBarCode) {
				setPropertyBtnHasInShopcartSeleted(idx);
				continue;
			}
			if (isChosenSpuIndex(idx) && (!existInBarCode) && (isSpuStockExist(idx))) {
				setPropertyBtnSelected(idx);
				continue;
			}
			if (isChosenSpuIndex(idx) && (!existInBarCode) && (!isSpuStockExist(idx))) {
				setPropertyBtnNostockSelected(idx);
				continue;
			}
			if ((!isChosenSpuIndex(idx)) && existInBarCode) {
				setPropertyBtnHasInShopcart(idx);
				continue;
			}
			if ((!isChosenSpuIndex(idx)) && (!existInBarCode) && (isSpuStockExist(idx))) {
				setPropertyBtnNormal(idx);
				continue;
			}
			if ((!isChosenSpuIndex(idx)) && (!existInBarCode) && (!isSpuStockExist(idx))) {
				setPropertyBtnNostock(idx);
				continue;
			}
		}
	}

	private boolean isSpuStockExist(int idx) {
		return mInfo.spuInfoList.get(idx).stock > 0;
	}

	private boolean isChosenSpuIndex(int idx) {
		return idx == ChosenSpuIndex;
	}

	private void setPropertyBtnNostockSelected(int idx) {
		mChildBtns.get(idx).setBackgroundResource(R.drawable.property_no_stock_selected);
		mChildBtns.get(idx).setTextColor(Color.rgb(195, 140, 86));
	}

	private void setPropertyBtnNostock(int idx) {
		mChildBtns.get(idx).setBackgroundResource(R.drawable.property_no_stock);
		mChildBtns.get(idx).setTextColor(Color.rgb(51, 51, 51));
	}

	private void setPropertyBtnNormal(int idx) {
		mChildBtns.get(idx).setBackgroundResource(R.drawable.property_normal);
		mChildBtns.get(idx).setTextColor(Color.rgb(51, 51, 51));
	}

	private void setPropertyBtnHasInShopcart(int idx) {
		mChildBtns.get(idx).setBackgroundResource(R.drawable.has_inshopcart);
		mChildBtns.get(idx).setTextColor(Color.rgb(51, 51, 51));
	}

	private void setPropertyBtnHasInShopcartSeleted(int idx) {
		mChildBtns.get(idx).setBackgroundResource(R.drawable.has_inshopcart_selector);
		mChildBtns.get(idx).setTextColor(Color.rgb(195, 140, 86));
	}

	private void setPropertyBtnSelected(int idx) {
		mChildBtns.get(idx).setBackgroundResource(R.drawable.property_normal_selected);
		mChildBtns.get(idx).setTextColor(Color.rgb(195, 140, 86));
	}

	private void initPropertyView() {
		LinearLayout root = (LinearLayout) mInflater.inflate(R.layout.good_attr_layout, null, false);
		TextView attrName = (TextView) root.findViewById(R.id.attrname);
		LinearLayout attrGroup = (LinearLayout) root.findViewById(R.id.attrLayout);
		attrName.setText("选择:");
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout
		layoutParams.setMargins(10, 0, 10, 0);
		ArrayList<Button> buttonList = new ArrayList<Button>();
		int totoalBtns = 0;
		for (int i = 0; i < mInfo.spuInfoList.size(); i++) {
			String item = mInfo.spuInfoList.get(i).attrInfo;
			LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(260, 84);
			totoalBtns++;
			itemParams.setMargins(0, 20, 20, 0);// 外边距
			Button childBtn = new Button(this);
			childBtn.setText(item);
			childBtn.setTextColor(Color.rgb(255, 255, 255));
			childBtn.setTextSize(15);
			childBtn.setPadding(10, 6, 10, 0);// 内边距
			childBtn.setOnClickListener(mPropertyClickListener);
			String barCode = mInfo.spuInfoList.get(i).barCode;
			if (barCode.equals(mBarCode)) {
				ChosenSpuIndex = i;
			}
			childBtn.setTag(barCode);
			childBtn.setLayoutParams(itemParams);
			mChildBtns.add(childBtn);
			buttonList.add(childBtn);
			if (totoalBtns >= 4) {
				LinearLayout horizLL = new LinearLayout(this);
				horizLL.setOrientation(LinearLayout.HORIZONTAL);
				horizLL.setLayoutParams(layoutParams);
				for (Button addBtn : buttonList) {
					horizLL.addView(addBtn);
				}
				attrGroup.addView(horizLL);
				buttonList.clear();
				totoalBtns = 0;
			}
		}
		// 最后一行添加一下
		if (!buttonList.isEmpty()) {
			LinearLayout horizLL = new LinearLayout(this);
			horizLL.setOrientation(LinearLayout.HORIZONTAL);
			horizLL.setLayoutParams(layoutParams);
			for (Button addBtn : buttonList) {
				horizLL.addView(addBtn);
			}
			attrGroup.addView(horizLL);
			totoalBtns = 0;
			buttonList.clear();
		}
		buttonList = null;
		mPropertyLayout.addView(root);
	}

	private void addAttrGroup(SkuAttrGroup group, int currentId) {
		LinearLayout root = (LinearLayout) mInflater.inflate(R.layout.good_attr_layout, null, false);
		TextView attrName = (TextView) root.findViewById(R.id.attrname);
		LinearLayout attrGroup = (LinearLayout) root.findViewById(R.id.attrLayout);
		attrName.setText(group.getAttrName() + ":");
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout
		layoutParams.setMargins(10, 0, 10, 0);
		ArrayList<Button> buttonList = new ArrayList<Button>();
		ArrayList<Button> btnRecord = new ArrayList<Button>();
		int totoalBtns = 0;
		for (int i = 0; i < group.getAttrs().length; i++) {
			String item = group.getAttrs()[i].getAttrVaule();
			LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(260, 84);
			totoalBtns++;
			itemParams.setMargins(0, 20, 20, 0);// 外边距
			Button childBtn = new Button(this);
			childBtn.setText(item);
			childBtn.setTextColor(Color.rgb(51, 51, 51));
			childBtn.setTextSize(15);
			childBtn.setId(group.getAttrs()[i].getAttrId());
			childBtn.setPadding(10, 6, 10, 0);// 内边距
			childBtn.setOnClickListener(mAttrClickListener);
			childBtn.setLayoutParams(itemParams);
			if (group.getAttrs()[i].getAttrId() == currentId) {
				childBtn.setBackgroundResource(R.drawable.property_normal_selected);
				childBtn.setTextColor(Color.rgb(195, 140, 86));
				childBtn.setTag(R.id.tag_skuselect, SELECTED);
			} else {
				childBtn.setBackgroundResource(R.drawable.property_normal);
				childBtn.setTextColor(Color.rgb(51, 51, 51));
				childBtn.setTag(R.id.tag_skuselect, UNSELECTED);
			}
			buttonList.add(childBtn);
			btnRecord.add(childBtn);
			if (totoalBtns >= 4) {
				LinearLayout horizLL = new LinearLayout(this);
				horizLL.setOrientation(LinearLayout.HORIZONTAL);
				horizLL.setLayoutParams(layoutParams);
				for (Button addBtn : buttonList) {
					horizLL.addView(addBtn);
				}
				attrGroup.addView(horizLL);
				buttonList.clear();
				totoalBtns = 0;
			}
		}
		// 最后一行添加一下
		if (!buttonList.isEmpty()) {
			LinearLayout horizLL = new LinearLayout(this);
			horizLL.setOrientation(LinearLayout.HORIZONTAL);
			horizLL.setLayoutParams(layoutParams);
			for (Button addBtn : buttonList) {
				horizLL.addView(addBtn);
			}
			attrGroup.addView(horizLL);
			totoalBtns = 0;
			buttonList.clear();
		}
		buttonList = null;
		mAttrsviewgroup.add(btnRecord);
		mPropertyLayout.addView(root);
	}

	private View.OnClickListener mAttrClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			ViewGroup parent = (ViewGroup) v.getParent();
			View oldView = null;
			for (int idx = 0; idx < parent.getChildCount(); idx++) {
				View child = parent.getChildAt(idx);
				if (child.getTag(R.id.tag_skuselect).equals(SELECTED)) {
					oldView = child;
					break;
				}
			}
			if (oldView == null) {
				v.setTag(R.id.tag_skuselect, SELECTED);
				v.setBackgroundResource(R.drawable.property_normal_selected);
				((TextView) v).setTextColor(Color.rgb(195, 140, 86));
			} else if (oldView == v) {
				v.setTag(R.id.tag_skuselect, UNSELECTED);
				v.setBackgroundResource(R.drawable.property_normal);
				((TextView) v).setTextColor(Color.rgb(51, 51, 51));
			} else {
				oldView.setTag(R.id.tag_skuselect, UNSELECTED);
				oldView.setBackgroundResource(R.drawable.property_normal);
				((TextView) oldView).setTextColor(Color.rgb(51, 51, 51));
				v.setTag(R.id.tag_skuselect, SELECTED);
				v.setBackgroundResource(R.drawable.property_normal_selected);
				((TextView) v).setTextColor(Color.rgb(195, 140, 86));
			}
			checkAttrState(v);
		}
	};

	private void checkAttrState(View v) {
		ArrayList<View> selectedViews = new ArrayList<View>();
		for (int index = 0; index < mAttrsviewgroup.size(); index++) {
			ArrayList<Button> group = mAttrsviewgroup.get(index);
			for (int idx = 0; idx < group.size(); idx++) {
				View testChild = group.get(idx);
				if (testChild.getTag(R.id.tag_skuselect).equals(SELECTED)) {
					selectedViews.add(testChild);
					continue;
				}
			}
		}
		if (selectedViews.size() != 0) {
			ArrayList<Integer> selectedIds = new ArrayList<Integer>();
			for (View selectedView : selectedViews) {
				selectedIds.add(selectedView.getId());
			}
			Collections.sort(selectedIds);
			for (int index = 0; index < mAttrsviewgroup.size(); index++) {
				ArrayList<Button> group = mAttrsviewgroup.get(index);
				for (int idx = 0; idx < group.size(); idx++) {
					View testChild = group.get(idx);
					if (selectedViews.contains(testChild)) {
						continue;
					}
					if (v != null) {
						if (v == testChild) {
							continue;
						}
					}
					ArrayList<Integer> testAttrIds = new ArrayList<Integer>();
					ViewGroup testParent = (ViewGroup) testChild.getParent();
					View selectBrother = null;
					for (int i = 0; i < testParent.getChildCount(); i++) {
						View testBrother = testParent.getChildAt(i);
						if (testBrother.getTag(R.id.tag_skuselect).equals(SELECTED)) {
							selectBrother = testBrother;
							break;
						}
					}
					if (selectBrother != null) {
						int selectBrotherID = selectBrother.getId();
						for (int i = 0; i < selectedIds.size(); i++) {
							if (selectedIds.get(i) != selectBrotherID) {
								testAttrIds.add(selectedIds.get(i));
							}
						}
					} else {
						testAttrIds = (ArrayList<Integer>) selectedIds.clone();
					}
					testAttrIds.add(testChild.getId());
					Collections.sort(testAttrIds);
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < testAttrIds.size(); i++) {
						if (i == (testAttrIds.size() - 1)) {
							sb.append(testAttrIds.get(i));
						} else {
							sb.append(testAttrIds.get(i)).append(";");
						}
					}
					String key = sb.toString();
					if (skuResult.get(key) == null) {
						testChild.setBackgroundResource(R.drawable.property_no_stock);
						testChild.setClickable(false);
					} else {
						testChild.setBackgroundResource(R.drawable.property_normal);
						testChild.setClickable(true);

					}
				}
			}
			if (selectedIds.size() == mAttrsviewgroup.size()) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < selectedIds.size(); i++) {
					if (i == (selectedIds.size() - 1)) {
						sb.append(selectedIds.get(i));
					} else {
						sb.append(selectedIds.get(i)).append(";");
					}
				}
				String key = sb.toString();
				AttrSelected(skuResult.get(key).getBarcode());
			}
		} else {
			for (int index = 0; index < mAttrsviewgroup.size(); index++) {
				ArrayList<Button> group = mAttrsviewgroup.get(index);
				for (int idx = 0; idx < group.size(); idx++) {
					View childItem = group.get(idx);
					if (skuResult.get(String.valueOf(childItem.getId())) == null) {
						childItem.setBackgroundResource(R.drawable.property_no_stock);
						childItem.setClickable(false);
					} else {
						childItem.setBackgroundResource(R.drawable.property_normal);
						childItem.setClickable(true);
					}
				}
			}
		}

	}

	private View.OnClickListener mPropertyClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			PropertySelected(v);
		}
	};
	private View.OnClickListener mAddToShoppingCart = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mBtag = "addcar";
			refreshCount();
		}
	};

	private void addGoodsToCar() {
		final SpuInfo spuInfo = getCurSpuInfo();
		if (spuInfo == null) {
			return;
		}
		boolean existInShoppingCart = mShoppingCartMgr.isInfoExist(spuInfo.barCode);
		synchronized (lock) {
			if (spuInfo.shelveStatus == 1 && spuInfo.stock > 0 && !existInShoppingCart) {
				if (spuInfo.checkfull()) {
					Toast.makeText(mApp, "该规格满减数据设置有误", Toast.LENGTH_SHORT).show();
					return;
				}
				int nCount = getCurrentCount();
				if (nCount <= spuInfo.stock) {
					boolean res = mShoppingCartMgr.addInfo(spuInfo, mInfo.name, mInfo.brand, nCount, mInfo.citCateId,
							mInfo.typeName);
					if (res) {// 若加入购物车成功
						Toast.makeText(GoodsInfoActivity.this, "加入购物车成功", Toast.LENGTH_SHORT).show();
						if (!newAttr) {
							mCurSelectedPropertyButton.setBackgroundResource(R.drawable.has_inshopcart_selector);
						}
						mBtAdd.setBackgroundResource(R.drawable.button_01);
						mBtAdd.setText("已加入购物车");
					}
				} else {
					Toast.makeText(mApp, "库存不足，请修改库存后，再添加到购物车", Toast.LENGTH_SHORT).show();
				}
			}
		}
		updateBadgeView();// 更新购物车图片的数量信息
		mBtag = null;
	}

	private View.OnClickListener mShoppingCartClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Logger.GetInstance().Action("去购物车");
			synchronized (lock) {
				Intent intent = new Intent();
				intent.setClass(GoodsInfoActivity.this, ShopCartActivity.class);
				startActivity(intent);
			}
		}
	};

	private View.OnClickListener mScanClickLister = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Logger.GetInstance().Debug("GoodsInfoActivity mScanClickLister begin");
			synchronized (lock) {
				GoodsInfoActivity.this.finish();
			}
			Logger.GetInstance().Debug("GoodsInfoActivity mScanClickLister end");
		}
	};

	/***
	 * 点击前的当前规格按钮的状态改变
	 */
	private void upOldBtnState() {
		final SpuInfo spuInfo = getCurSpuInfo();
		if (spuInfo == null) {
			return;
		}
		if (mCurSelectedPropertyButton != null) {
			if (mShoppingCartMgr.isInfoExist(spuInfo.barCode)) {
				mCurSelectedPropertyButton.setBackgroundResource(R.drawable.has_inshopcart);
				mCurSelectedPropertyButton.setTextColor(Color.rgb(51, 51, 51));
			} else {
				if (spuInfo.stock > 0) {
					mCurSelectedPropertyButton.setBackgroundResource(R.drawable.property_normal);
					mCurSelectedPropertyButton.setTextColor(Color.rgb(51, 51, 51));
				} else {
					mCurSelectedPropertyButton.setBackgroundResource(R.drawable.property_no_stock);
					mCurSelectedPropertyButton.setTextColor(Color.rgb(51, 51, 51));
				}
			}
		}
	}

	private void checkAddShopState() {
		final SpuInfo spuInfo = getCurSpuInfo();
		if (spuInfo == null) {
			return;
		}
		if (mShoppingCartMgr.isInfoExist(spuInfo.barCode)) {
			mBtAdd.setBackgroundResource(R.drawable.button_01);
			mBtAdd.setText("已加入购物车");
		} else {
			if (spuInfo.stock > 0) {
				mBtAdd.setBackgroundResource(R.drawable.button_02);
				mBtAdd.setText("加入购物车");
			} else {
				mBtAdd.setBackgroundResource(R.drawable.xiajia);
				mBtAdd.setText("暂时无货");
			}
		}
		updataNoGoodView(spuInfo);
	}

	private void updataNoGoodView(SpuInfo spuInfo) {
		if (spuInfo.stock > 0) {
			mNoGoods.setVisibility(View.GONE);
		} else {
			mNoGoods.setVisibility(View.VISIBLE);
		}
	}

	private void updateProductDetail() {
		 descripView.loadDataWithBaseURL(null, getCurSpuInfo().description, "text/html", "UTF-8", null);
	}

	/***
	 * 点击后的当前规格按钮的状态改变
	 * 
	 * @param （BUTTON）v
	 */
	private void upNewBtnState() {
		final SpuInfo spuInfo = getCurSpuInfo();
		if (spuInfo == null) {
			return;
		}
		mGoodName.setText(spuInfo.name);
		if (mShoppingCartMgr.isInfoExist(spuInfo.barCode)) {
			mCurSelectedPropertyButton.setBackgroundResource(R.drawable.has_inshopcart_selector);
			mCurSelectedPropertyButton.setTextColor(Color.rgb(195, 140, 86));
		} else {
			if (spuInfo.stock > 0) {
				mCurSelectedPropertyButton.setBackgroundResource(R.drawable.property_normal_selected);
				mCurSelectedPropertyButton.setTextColor(Color.rgb(195, 140, 86));
			} else {
				mCurSelectedPropertyButton.setBackgroundResource(R.drawable.no_good_press);
				mCurSelectedPropertyButton.setTextColor(Color.rgb(195, 140, 86));
			}
		}
	}

	/***
	 * 点击规格按钮后的回调
	 * 
	 * @param v
	 */
	private void PropertySelected(View v) {
		upOldBtnState();
		boolean res = setCurSpuInfo((String) v.getTag());
		if (!res) {
			return;
		}
		mCurSelectedPropertyButton = (Button) v;
		upNewBtnState();
		checkAddShopState();
		setPrice();
		showDisplayAttrs();
		updateViewpager();
		updateProductDetail();
		updateActiveInfos();
	}

	private void updateActiveInfos() {
		final SpuInfo info = getCurSpuInfo();
		if(info == null || info.activeInfos == null || info.activeInfos.size() == 0){
			activeWrap.setVisibility(View.GONE);
			activeSplit.setVisibility(View.GONE);
			commentView.setVisibility(View.GONE);
		}else{
			final ActiveInfo aInfo = info.activeInfos.get(0);
			if(aInfo.getComments() != null && !aInfo.getComments().equals("")){
				commentView.setVisibility(View.VISIBLE);
				comment.setText(aInfo.getComments());
			}else{
				commentView.setVisibility(View.GONE);
			}
			GoodsinfoActiveAdp adapter = new GoodsinfoActiveAdp(info.activeInfos);
			mActiveLv.setAdapter(adapter);
			activeWrap.setVisibility(View.VISIBLE);
			activeSplit.setVisibility(View.VISIBLE);
		}
	}

	private boolean setCurSpuInfo(String tag) {
		boolean res = false;
		curSpuInfo = null;
		if (StringUtil.isEmpty(tag))
			res = false;
		if (mInfo == null || mInfo.spuInfoList == null || mInfo.spuInfoList.size() == 0) {
			return false;
		}
		for (int i = 0; i < mInfo.spuInfoList.size(); i++) {
			SpuInfo info = mInfo.spuInfoList.get(i);
			if (tag.equals(info.barCode)) {
				curSpuInfo = info;
				res = true;
			}
		}
		return res;
	}

	private SpuInfo getCurSpuInfo() {
		return curSpuInfo;
	}

	private void updateViewpager() {
		mTipGroup.removeAllViews();
		mViewPager.setAdapter(null);
		SpuInfo info = getCurSpuInfo();
		if (info == null) {
			return;
		}
		ArrayList<String> imgs = info.imageLinks;
		if (imgs == null || imgs.size() == 0) {
			return;
		}
		initTips(imgs.size());
		ImagePageAdapter adapter = new ImagePageAdapter(imgs);
		mViewPager.setAdapter(adapter);
	}

	private void AttrSelected(String barcode) {
		final SpuInfo spuInfo = getCurSpuInfo();
		if (barcode.equals(mBarCode)) {
			return;
		}
		mBarCode = barcode;
		mTipGroup.removeAllViews();
		mGoodName.setText(spuInfo.name);
		checkAddShopState();
		setPrice();
		mViewPager.setAdapter(null);
		clickableRecord();
		disAttrClickable();
		updateProductDetail();
	}

	private void clickableRecord() {
		for (int index = 0; index < mAttrsviewgroup.size(); index++) {
			ArrayList<Button> group = mAttrsviewgroup.get(index);
			for (int idx = 0; idx < group.size(); idx++) {
				View item = group.get(idx);
				if (item.isClickable()) {
					item.setTag(R.id.tag_skuclickable, 1);
				} else {
					item.setTag(R.id.tag_skuclickable, 0);
				}
			}
		}
	}

	private void disAttrClickable() {
		for (int index = 0; index < mAttrsviewgroup.size(); index++) {
			ArrayList<Button> group = mAttrsviewgroup.get(index);
			for (int idx = 0; idx < group.size(); idx++) {
				View item = group.get(idx);
				item.setClickable(false);
			}
		}
	}

	class ImagePageAdapter extends PagerAdapter {
		private ArrayList<String> imgs;

		public ImagePageAdapter(ArrayList<String> imgsParam) {
			imgs = imgsParam;
		}

		public void setImgs(ArrayList<String> imgs) {
			this.imgs = imgs;
		}

		@Override
		public int getCount() {
			return imgs.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ImageView imageView = new ImageView(GoodsInfoActivity.this);
			ImageAware imageAware = new ImageViewAware(imageView, false);
			ImageLoader.getInstance().displayImage(imgs.get(position), imageAware, ImageHelper.initBarcodePathOption());
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}
	}

	/***
	 * 设置要显示的价格栏内容，根据活动的不同而展示
	 */
	private void setPrice() {
		final SpuInfo sInfo = getCurSpuInfo();
		if (sInfo == null) {
			return;
		}
		if(sInfo.activeInfos == null || sInfo.activeInfos.size() == 0){
			cxPriceLayout.setVisibility(View.VISIBLE);
			mpricefixTitle.setText("免税价：");
			mMsTitle.setVisibility(View.INVISIBLE);
			mCxPrice.setText("¥" + sInfo.msPrice);
			mMsPrice.setVisibility(View.INVISIBLE);
			return;
		}
		final ActiveInfo aInfo = sInfo.activeInfos.get(0);
		if(aInfo != null && aInfo.getType() == ActiveType.Cut){
			cxPriceLayout.setVisibility(View.VISIBLE);
			mpricefixTitle.setText("促销价：");
			mCxPrice.setText("¥" + aInfo.getActive_price());
			mMsTitle.setVisibility(View.VISIBLE);
			mMsPrice.setVisibility(View.VISIBLE);
			mMsPrice.setText(sInfo.msPrice);
			mMsPrice.setTextColor(android.graphics.Color.GRAY);
			mMsTitle.setTextColor(android.graphics.Color.GRAY);
			mMsPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中间横线
			mMsPrice.getPaint().setAntiAlias(true);// 抗锯齿
		}else{
			cxPriceLayout.setVisibility(View.VISIBLE);
			mpricefixTitle.setText("免税价：");
			mMsTitle.setVisibility(View.INVISIBLE);
			mCxPrice.setText("¥" +sInfo.msPrice);
			mMsPrice.setVisibility(View.INVISIBLE);
		}
	}

	private void showDisplayAttrs() {
		mDisPlayLayout.removeAllViews();
		final SpuInfo sInfo = getCurSpuInfo();
		if (sInfo == null) {
			return;
		}
		for (DisplayAttrGroup group : sInfo.baseAttrGroups) {
			LinearLayout grouplayout = (LinearLayout) mInflater.inflate(R.layout.display_attrgroup, null, false);
			TextView groupName = (TextView) grouplayout.findViewById(R.id.attr_group_name);
			groupName.setText(group.getName());
			mDisPlayLayout.addView(grouplayout);
			for (DisplayAttrItem item : group.getAttrItems()) {
				TextView attrItem = (TextView) mInflater.inflate(R.layout.display_attritem, null, false);
				StringBuilder sb = new StringBuilder();
				sb.append(item.getName());
				sb.append(" : ");
				sb.append(item.getVaule());
				attrItem.setText(sb.toString());
				mDisPlayLayout.addView(attrItem);
			}
		}
		for (DisplayAttrGroup group : sInfo.displayAttrGroups) {
			LinearLayout grouplayout = (LinearLayout) mInflater.inflate(R.layout.display_attrgroup, null, false);
			TextView groupName = (TextView) grouplayout.findViewById(R.id.attr_group_name);
			groupName.setText(group.getName());
			mDisPlayLayout.addView(grouplayout);
			for (DisplayAttrItem item : group.getAttrItems()) {
				TextView attrItem = (TextView) mInflater.inflate(R.layout.display_attritem, null, false);
				StringBuilder sb = new StringBuilder();
				sb.append(item.getName());
				sb.append(" : ");
				sb.append(item.getVaule());
				attrItem.setText(sb.toString());
				mDisPlayLayout.addView(attrItem);
			}
		}
	}

	private void setAllButton(boolean enabled) {
		for (int idx = 0; idx < mChildBtns.size(); idx++) {
			mChildBtns.get(idx).setClickable(enabled);
		}
	}

	private void setImageBackground(int selectItems) {
		for (int i = 0; i < mTips.length; i++) {
			if (i == selectItems) {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				mTips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

	class ViewPagerChangeImp implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			setImageBackground(arg0);
		}
	}

	class GoodsinfoActiveAdp extends BaseAdapter {
		private LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		private ArrayList<ActiveInfo> aInfos;
		public GoodsinfoActiveAdp(ArrayList<ActiveInfo> infos) {
			aInfos = infos;
		}

		@Override
		public int getCount() {
			return aInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.goodsinfo_activeinfo, null, false);
			TextView activeTitleTv = (TextView) convertView.findViewById(R.id.titleTv);
			TextView activeDescrptionTv = (TextView) convertView.findViewById(R.id.descriptionTv);
			ImageView privilegeIcon = (ImageView) convertView.findViewById(R.id.privilege_icon);
			View wrap = (View) convertView.findViewById(R.id.cutLayout);
			final ActiveInfo info = aInfos.get(position);
			activeTitleTv.setText(info.getType_name());
			if(info.getDesc() != null ){
				activeDescrptionTv.setText(info.getDesc());
			}else if(info.getShort_desc() != null ){
				activeDescrptionTv.setText(info.getShort_desc());
			}
			if(info.getPrivilegeType() == ActiveInfo.PrivilegeType.PRIVILEGE_TYPE){
				privilegeIcon.setVisibility(View.VISIBLE);
			}else{
				privilegeIcon.setVisibility(View.GONE);
			}
			wrap.setTag(info);
			wrap.setOnClickListener(new ActiveSellListener());
			return convertView;
		}
	}
	
}
