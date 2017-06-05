package com.hhyg.TyClosing.ui;

import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.ShopCartAdapter;
import com.hhyg.TyClosing.allShop.adapter.ShopCartAdapter.AddCountListener;
import com.hhyg.TyClosing.allShop.adapter.ShopCartAdapter.AtyModListenerProxy;
import com.hhyg.TyClosing.allShop.adapter.ShopCartAdapter.DeleteItemListener;
import com.hhyg.TyClosing.allShop.adapter.ShopCartAdapter.MinusCountListener;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.PickUpInfo;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.mgr.DeleteMgr;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.presenter.ShopcartPresenter;
import com.hhyg.TyClosing.ui.fragment.SettleTypeDialogFragment;
import com.hhyg.TyClosing.ui.view.ShopcarMenu;
import com.hhyg.TyClosing.ui.view.ShopcarMenu.MENUITEM;
import com.hhyg.TyClosing.ui.view.ShopcarMenu.OnItemClickListener;
import com.hhyg.TyClosing.util.ProgressDialogUtil;
import com.hhyg.TyClosing.view.ShopCartView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ShopCartActivity extends Activity
		implements ShopCartView, OnClickListener, SettleTypeDialogFragment.ISettleTypeListener, AddCountListener,
		MinusCountListener, DeleteItemListener, AtyModListenerProxy {
	private SettleTypeDialogFragment settleTypeDialogFragment;
	private ShopcarMenu mShopcarMenu;
	private ShopcartPresenter mPresenter;
	private ShopCartAdapter mAdapter;
	private ImageButton mBack;
	private ImageButton mMore;
	private ViewGroup mNullView;
	private ViewGroup mValueView;
	private ListView mLv;
	private Button mCommit;
	private TextView mHjTv;
	private TextView mZjTv;
	private TextView mYhTv;
	private Button mWarnning;
	private UIHandler mUihander = new UIHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopcart_aty);
		init();
		findView();

	}

	private void findView() {
		ImageButton home = (ImageButton) findViewById(R.id.home);
		home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserTrackMgr.getInstance().onEvent(" tabartouch", "首页");
				Intent it = new Intent();
				it.setClass(ShopCartActivity.this, AllShopActivity.class);
				startActivity(it);
				finish();
			}
		});
		ImageButton scan = (ImageButton) findViewById(R.id.scan);
		scan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserTrackMgr.getInstance().onEvent(" tabartouch", "扫码");
				Intent it = new Intent();
				it.setClass(ShopCartActivity.this, CaptureActivity.class);
				startActivity(it);
				finish();
			}
		});
		ImageButton brand = (ImageButton) findViewById(R.id.brand);
		brand.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserTrackMgr.getInstance().onEvent(" tabartouch", "品牌");
				Intent it = new Intent();
				it.setClass(ShopCartActivity.this, BrandActivity.class);
				startActivity(it);
				finish();
			}
		});
		ImageButton cate = (ImageButton) findViewById(R.id.cate);
		cate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UserTrackMgr.getInstance().onEvent(" tabartouch", "类别");
				Intent it = new Intent();
				it.setClass(ShopCartActivity.this, CategoryActivity.class);
				startActivity(it);
				finish();
			}
		});
		ImageButton shopcat = (ImageButton) findViewById(R.id.shopcat);
		shopcat.setBackgroundResource(R.drawable.allshop_shopcat_pressed);
		mBack = (ImageButton) findViewById(R.id.back);
		mBack.setOnClickListener(this);
		mMore = (ImageButton) findViewById(R.id.get_more);
		mMore.setOnClickListener(this);
		mCommit = (Button) findViewById(R.id.commitBt);
		mCommit.setOnClickListener(this);
		mHjTv = (TextView) findViewById(R.id.hejiTv);
		mZjTv = (TextView) findViewById(R.id.zjTv);
		mYhTv = (TextView) findViewById(R.id.yhTv);
		mWarnning = (Button) findViewById(R.id.warning);
		mNullView = (ViewGroup) findViewById(R.id.nulllayout);
		mValueView = (ViewGroup) findViewById(R.id.valueLayout);
		mLv = (ListView) findViewById(R.id.shoppingcart_lv);
		mLv.setAdapter(mAdapter);
	}

	private void init() {
		mPresenter = new ShopcartPresenter();
		mPresenter.attach(this);
		settleTypeDialogFragment = new SettleTypeDialogFragment();
		mAdapter = new ShopCartAdapter(this);
		mAdapter.setmAddListener(this);
		mAdapter.setmDelectLister(this);
		mAdapter.setmMinusListener(this);
		mAdapter.setmAtyModProxy(this);
		mShopcarMenu = new ShopcarMenu(this);
		mShopcarMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onClick(MENUITEM item, String str) {
                if (item == MENUITEM.ITEM1) {
                    Intent intent = new Intent();
                    intent.setClass(ShopCartActivity.this, DeleteActivity.class);
                    startActivity(intent);
                }
				if (item == MENUITEM.ITEM2) {
					Intent it = new Intent();
					it.setClass(ShopCartActivity.this, HistoryOrderActivity.class);
					startActivity(it);
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		setAirPort();
		mPresenter.fetchShopCartColumns();
	}

	@Override
	protected void onDestroy() {
		mPresenter.detach();
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.get_more:
			mShopcarMenu.showPopupWindow(v);
			break;
		case R.id.commitBt:
			Intent intent = new Intent();
			intent.setClass(ShopCartActivity.this, InfoValidateActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void startProgress() {
		ProgressDialogUtil.show(this);
	}

	@Override
	public void disProgress() {
		ProgressDialogUtil.hide();
	}

	@Override
	public void exceptionCart() {
		mNullView.setVisibility(View.GONE);
		mValueView.setVisibility(View.GONE);
		mWarnning.setVisibility(View.GONE);
		mCommit.setClickable(false);
		mCommit.setBackgroundResource(R.drawable.cancelbtn_shape);
		mZjTv.setText(ShoppingCartMgr.getInstance().getTotalPrice());
		mHjTv.setText(ShoppingCartMgr.getInstance().getRealCast());
		mYhTv.setText(ShoppingCartMgr.getInstance().getDiscount());
	}
	
	@Override
	public void onlyNoStock() {
		mWarnning.setVisibility(View.GONE);
		mNullView.setVisibility(View.GONE);
		mValueView.setVisibility(View.VISIBLE);
		mAdapter.addItem(ShoppingCartMgr.getInstance().getColumns());
		mCommit.setClickable(false);
		mCommit.setBackgroundResource(R.drawable.cancelbtn_shape);
		mZjTv.setText(ShoppingCartMgr.getInstance().getTotalPrice());
		mHjTv.setText(ShoppingCartMgr.getInstance().getRealCast());
		mYhTv.setText(ShoppingCartMgr.getInstance().getDiscount());
	}
	
	@Override
	public void warnningCart() {
		mNullView.setVisibility(View.GONE);
		mValueView.setVisibility(View.VISIBLE);
		mWarnning.setVisibility(View.VISIBLE);
		mWarnning.setText(ShoppingCartMgr.getInstance().getErrMsg());
		mAdapter.addItem(ShoppingCartMgr.getInstance().getColumns());
		mCommit.setClickable(false);
		mCommit.setBackgroundResource(R.drawable.cancelbtn_shape);
		mZjTv.setText(ShoppingCartMgr.getInstance().getTotalPrice());
		mHjTv.setText(ShoppingCartMgr.getInstance().getRealCast());
		mYhTv.setText(ShoppingCartMgr.getInstance().getDiscount());
	}

	@Override
	public void shopcartList() {
		mWarnning.setVisibility(View.GONE);
		mNullView.setVisibility(View.GONE);
		mValueView.setVisibility(View.VISIBLE);
		mAdapter.addItem(ShoppingCartMgr.getInstance().getColumns());
		mCommit.setClickable(true);
		mCommit.setBackgroundResource(R.drawable.button_corner);
		mZjTv.setText(ShoppingCartMgr.getInstance().getTotalPrice());
		mHjTv.setText(ShoppingCartMgr.getInstance().getRealCast());
		mYhTv.setText(ShoppingCartMgr.getInstance().getDiscount());
	}

	@Override
	public void emptyShopcart() {
		mWarnning.setVisibility(View.GONE);
		mNullView.setVisibility(View.VISIBLE);
		mValueView.setVisibility(View.GONE);
		mAdapter.clear();
		mCommit.setClickable(false);
		mCommit.setBackgroundResource(R.drawable.cancelbtn_shape);
		mZjTv.setText(ShoppingCartMgr.getInstance().getTotalPrice());
		mHjTv.setText(ShoppingCartMgr.getInstance().getRealCast());
		mYhTv.setText(ShoppingCartMgr.getInstance().getDiscount());
	}

	private void setAirPort() {
		final ArrayList<PickUpInfo> list = ClosingRefInfoMgr.getInstance().getAllPickUpAddr();
		final int allBtn[] = new int[] { R.id.button_air1, R.id.button_air2, R.id.button_air3, R.id.button_air4,
				R.id.button_air5 };
		for (int index = 0; index < 5; index++) {
			Button btn = (Button) findViewById(allBtn[index]);
			if (index < list.size()) {
				PickUpInfo info = list.get(index);
				btn.setText(info.name);
				btn.setVisibility(View.VISIBLE);
			} else
				btn.setVisibility(View.GONE);
			btn.setTag(index + "");
			if (ClosingRefInfoMgr.getInstance().getChosenPickupInfoIndex() == index) {
				btn.setTextColor(android.graphics.Color.parseColor("#c48c56"));
				btn.setBackgroundResource(R.drawable.button_sel);
			}
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					for (int i = 0; i < 5; i++) {
						Button btn = (Button) findViewById(allBtn[i]);
						btn.setSelected(v.getTag().equals(i + ""));
						int color1 = android.graphics.Color.parseColor("#c48c56");
						int color2 = android.graphics.Color.parseColor("#484848");
						btn.setTextColor(v.getTag().equals(i + "") ? color1 : color2);
						btn.setBackgroundResource(
								v.getTag().equals(i + "") ? R.drawable.button_sel : R.drawable.button_sel_normal);
						if (v.getTag().equals(i + "")) {
							ClosingRefInfoMgr.getInstance().setAndSaveChosenPickupInfoIndex(i);
						}
					}
				}
			});
		}
	}

	@Override
	public void autoSettle() {
		Intent intent = new Intent();
		intent.setClass(ShopCartActivity.this, InfoValidateActivity.class);
		startActivity(intent);
		settleTypeDialogFragment.dismiss();
	}

	@Override
	public void serverSettler() {
		if (ClosingRefInfoMgr.getInstance().isShopTypeOutside()) {
			Intent intent = new Intent();
			intent.setClass(ShopCartActivity.this, CustomerCodeCaptureActivity.class);
			startActivity(intent);
			settleTypeDialogFragment.dismiss();
			return;
		}
		MyApplication.GetInstance().post(Constants.getServiceUrl(), makeShopCntCheckParam("submitorder"),
				new CommitCallBack());
	}

	private String makeShopCntCheckParam(String op) {
		JSONObject submitorObj = new JSONObject();
		submitorObj.put("op", op);
		JSONArray data = new JSONArray();
		int spuCnt = ShoppingCartMgr.getInstance().getAll().size();
		for (int spuIdx = 0; spuIdx < spuCnt; spuIdx++) {
			ShoppingCartInfo info = ShoppingCartMgr.getInstance().getAll().get(spuIdx);
			JSONObject obj = new JSONObject();
			obj.put("barcode", info.barCode);
			obj.put("count", 1);
			if (info.activeId != null) {
				obj.put("activity", info.activeId);
			} else {
				obj.put("activity", "");
			}
			data.add(obj);
		}
		JSONObject submitorObj1 = new JSONObject();
		submitorObj1.put("usercode", "");
		submitorObj1.put("goods_sku", data);
		String str = ClosingRefInfoMgr.getInstance().getSalerInfo().getSalerId();
		submitorObj1.put("salerid", str);
		submitorObj.put("data", submitorObj1);
		return JsonPostParamBuilder.makeParam(submitorObj);
	}

	private class CommitCallBack implements INetWorkCallBack {
		@Override
		public void PostProcess(int msgId, String msg) {
			if (msgId == MyApplication.GetInstance().MSG_TYPE_VALUE) {
				String msgBody = msg;
				try {
					ProcMsg(msgBody);
				} catch (JSONException e) {
					Logger.GetInstance().Exception(e.getMessage() + "parse data is :" + msg);
					e.printStackTrace();
				}
			} else if (msgId == MyApplication.GetInstance().MSG_TYPE_ERROR) {
				Message message = Message.obtain();
				message.what = 1;
				message.obj = msg;
				mUihander.sendMessage(message);
			}
		}
	}

	private void ProcMsg(String msgBody) throws JSONException {
		JSONObject jsonObj = JSONObject.parseObject(msgBody);
		String op = jsonObj.getString("op");
		if (op.equals("submitorder")) {
			String errMsg = jsonObj.getString("msg");
			int errcode = jsonObj.getIntValue("errcode");
			if (errcode == 1) {
				JSONObject jsonObj1 = jsonObj.getJSONObject("data");
				String orderId = jsonObj1.getString("orderid");
				Message m = new Message();
				m.obj = orderId;
				m.what = 0;// success
				mUihander.sendMessage(m);
			} else {
				Message m = new Message();
				m.obj = errMsg;
				m.what = 1;// fail
				mUihander.sendMessage(m);
			}
		}
	}

	private class UIHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (settleTypeDialogFragment != null) {
				settleTypeDialogFragment.dismiss();
			}
			switch (msg.what) {
			case 0: {
				String orderId = (String) msg.obj;
				ShoppingCartMgr.getInstance().clear();
				Intent intent = new Intent();
				intent.setClass(ShopCartActivity.this, SuccessActivity.class);
				Bundle b = new Bundle();
				b.putString("orderId", orderId);
				intent.putExtras(b);
				startActivity(intent);
				break;
			}
			case 1: {
				String errMsg = (String) msg.obj;
				Toast.makeText(ShopCartActivity.this, errMsg, Toast.LENGTH_SHORT).show();
				break;
			}
			}
		}
	}

	@Override
	public void onDelect(String barcode) {
		if (barcode == null) {
			return;
		}
		final ShoppingCartInfo info = ShoppingCartMgr.getInstance().getInfoByBarCode(barcode);
		if(info == null){
			return;
		}
		if(!DeleteMgr.getInstance().isInfoExist(barcode)){
			DeleteMgr.getInstance().addInfo(info);
		}
		ShoppingCartMgr.getInstance().deleteInfo(barcode);
		mPresenter.fetchShopCartColumns();
	}

	@Override
	public void onMinusCount(String barcode) {
		if (barcode == null) {
			return;
		}
		final int cnt = ShoppingCartMgr.getInstance().getInfoByBarCode(barcode).cnt - 1;
		ShoppingCartMgr.getInstance().updateShopCnt(barcode, cnt);
		mPresenter.fetchShopCartColumns();
	}

	@Override
	public void onAddCount(String barcode) {
		if (barcode == null) {
			return;
		}
		final int cnt = ShoppingCartMgr.getInstance().getInfoByBarCode(barcode).cnt + 1;
		ShoppingCartMgr.getInstance().updateShopCnt(barcode, cnt);
		mPresenter.fetchShopCartColumns();
	}

	@Override
	public void onProxy(String barcode, String id) {
		if (barcode == null || id == null) {
			return;
		}
		ShoppingCartMgr.getInstance().updateActiveId(barcode, id);
		mPresenter.fetchShopCartColumns();
	}

}
