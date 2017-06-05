package com.hhyg.TyClosing.ui;

import static android.widget.ListPopupWindow.WRAP_CONTENT;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetExceptionAlert;
import com.hhyg.TyClosing.info.OrderInfo;
import com.hhyg.TyClosing.info.PickUpInfo;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.ui.adapter.HistoryOrderAdapter;
import com.hhyg.TyClosing.ui.adapter.ShopListAdapter;
import com.hhyg.TyClosing.ui.dialog.CustomAlertDialog;
import com.hhyg.TyClosing.util.ProgressDialogUtil;
import com.hhyg.TyClosing.util.StringUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class HistoryOrderActivity extends Activity {
    private ListView mListView;
    private MyApplication mApp = MyApplication.GetInstance();
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mRightRelativeLayout;
    private ListView mSlidingListView;
    private ImageView mNullView;
    private HistoryOrderAdapter mAdapter;
    private ShopListAdapter mShopListAdapter;
    private Timer timer = null;
    private ArrayList<ShoppingCartInfo> mShopList = new ArrayList<ShoppingCartInfo>();//浮层商品信息
    private ArrayList<OrderInfo> mOrderList = new ArrayList<OrderInfo>();//总的历史订单
    private Object lock1 = new Object();
    private boolean mQuit;
    private Button mCancelBtn; //取消订单按钮
    private JSONArray mJSonOrder = null;
    private String mOpenedorderID = null;
    private GetCancelCallBack mCallBack = new GetCancelCallBack();//取消订单回调
    private Handler mCuiHandler = new Handler();
    public android.os.Handler mHandler = new android.os.Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_order);
        mAdapter = new HistoryOrderAdapter(this);
        mListView = (ListView) findViewById(R.id.orderlistview);
        mListView.setAdapter(mAdapter);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerListener(myDrawerListener);
        mCancelBtn = (Button) findViewById(R.id.cancel_btn);
        mNullView = (ImageView) findViewById(R.id.nullorder);
        findSlidingView();
        setShopcarBtn();
        getALlGoods();

        Logger.GetInstance().Track("HistoryOrderActivity on Create");
    }

    private void initShopAdapter() {
        int cnt = mOrderList.size();
        if (cnt == 0) {
            mNullView.setVisibility(View.VISIBLE);
        } else {
            mShopListAdapter = new ShopListAdapter(this);
            mSlidingListView.setAdapter(mShopListAdapter);
        }
    }

    private void setShopcarBtn() {
        ImageButton btn = (ImageButton) findViewById(R.id.back_to_shopcar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuit = true;
                synchronized (lock1) {
                    Intent intent = new Intent();
                    intent.setClass(HistoryOrderActivity.this, ShopCartActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void findSlidingView() {
        mRightRelativeLayout = (RelativeLayout) findViewById(R.id.slidinglayout);
        mSlidingListView = (ListView) findViewById(R.id.ordershoplv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("HistoryOrderActivity");
        Logger.GetInstance().Track("HistoryOrderActivity on onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("HistoryOrderActivity");
        Logger.GetInstance().Track("HistoryOrderActivity on onPause");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDrawerLayout.isDrawerOpen(mRightRelativeLayout)) {
            mDrawerLayout.closeDrawer(mRightRelativeLayout);
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mQuit == true) {
                return false;
            }
            mQuit = true;
            synchronized (lock1) {
                Intent intent = new Intent();
                intent.setClass(HistoryOrderActivity.this, ShopCartActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setListViewHeightBasedOnChildren(String strType) {
        int nMaxCount = 6;
        if("2".equals(strType)){//自助结算
            nMaxCount = 4;
        }
        ViewGroup.LayoutParams params = mSlidingListView.getLayoutParams();
        if(mShopListAdapter.getCount() > nMaxCount) {
             View listItem = mShopListAdapter.getView(0, null, mSlidingListView);
             listItem.measure(0, 0);
             int totalHeight = listItem.getMeasuredHeight() * nMaxCount + mSlidingListView.getDividerHeight() * (nMaxCount - 1);
             params.height = totalHeight;
        }
        else
            params.height = WRAP_CONTENT;
        mSlidingListView.setLayoutParams(params);

        params = findViewById(R.id.splitthree).getLayoutParams();
        ((ViewGroup.MarginLayoutParams) params).topMargin = mShopListAdapter.getCount() == 1 ? 25 : 2;
    }


    public ArrayList<ShoppingCartInfo> getAllShopByOrderId(String orderId) {
        ArrayList<ShoppingCartInfo> arr = new ArrayList<ShoppingCartInfo>();
        JSONArray array = null;
        for (int i = 0; i < mJSonOrder.size(); i++) {
            com.alibaba.fastjson.JSONObject ob = (com.alibaba.fastjson.JSONObject) mJSonOrder.get(i);
            if (ob.get("orderSn").equals(orderId)) {
                array = ob.getJSONArray("goodsSku");
                break;
            }
        }
        if (array == null)
            return arr;
        for (int i = 0; i < array.size(); i++) {
            com.alibaba.fastjson.JSONObject j = (com.alibaba.fastjson.JSONObject) array.get(i);
            ShoppingCartInfo info = new ShoppingCartInfo();
            info.brand = j.getString("brand_name");
            info.name = j.getString("goods_name");
            info.attrInfo = j.getString("goods_attr");
            info.imgUrl = j.getString("goods_img");
            Double d = j.getDouble("goods_price");
            if (d == null) {
                info.citPrice = 0;
            } else
                info.citPrice = d.doubleValue();
            d = j.getDouble("goods_price");
            if (d == null) {
                info.activePrice = 0;
            } else
                info.activePrice = d.doubleValue();
            info.barCode = j.getString("barcode");
            info.cnt = j.getInteger("number");
            info.msPrice = j.getString("goods_price");
            try {
                info.price = j.getString("price");
            } catch (Exception e) {
                Logger.GetInstance().Exception(e.getMessage() + "j is :" + j.toJSONString());
                e.printStackTrace();
            }
            arr.add(info);
        }
        return arr;
    }


    public void getALlGoods() {
        com.alibaba.fastjson.JSONObject j = new com.alibaba.fastjson.JSONObject();
        j.put("op", "getorderoftoday");
        com.alibaba.fastjson.JSONObject j1 = new com.alibaba.fastjson.JSONObject();
        j1.put("salerid", ClosingRefInfoMgr.getInstance().getSalerInfo().getSalerId());
        j.put("data", j1);
        ProgressDialogUtil.show(this);
        try {
            String url = Constants.getServiceUrl();
            mApp.post(url, JsonPostParamBuilder.makeParam(j), new INetWorkCallBack() {
                public void PostProcess(int msgId, String msg) {
                    final int error = msgId;
                    final String message = msg;
                    mCuiHandler.post(new Runnable() {
                        public void run() {
                            listDataHadLoad(error, message);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listDataHadLoad(int msgId, String msg) {
        ProgressDialogUtil.hide();
        NetExceptionAlert netExceptionAlert = new NetExceptionAlert(HistoryOrderActivity.this, null);
        if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
            return;
        }
        com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(msg);
        String error = json.getString("errcode");
        if (error.equals("1")) {
            JSONArray j = (JSONArray) json.getJSONArray("data");
            mJSonOrder = j;
            for (int i = 0; i < j.size(); i++) {
                com.alibaba.fastjson.JSONObject jo = (com.alibaba.fastjson.JSONObject) j.get(i);
                OrderInfo info = new OrderInfo();
                info.orderId = jo.getString("orderSn");
                info.totalCast = jo.getDouble("finalPrice");
                info.totalCnt = jo.getInteger("goodsCount");
                info.commitTime = jo.getString("orderDateTime");
                info.status = jo.getString("orderStatus");
                info.citOrderSn = jo.getString("citOrderSn");
                mOrderList.add(info);
            }
            initShopAdapter();
            mAdapter.setData(mOrderList);
            mAdapter.notifyDataSetChanged();
        } else {
            String strMessage = (String) json.get("msg");
            if (strMessage != null) {
                Toast.makeText(this, strMessage, Toast.LENGTH_LONG).show();
            }
        }

        int cnt = mOrderList.size();
        if (cnt == 0) {
            mNullView.setVisibility(View.VISIBLE);
        }
    }

    public void seeGoodsDetail(OrderInfo info) {
        mShopList = getAllShopByOrderId(info.orderId);
        com.alibaba.fastjson.JSONObject json = getInfoOrderId(info.orderId);
        if (json == null)
            return;
        mOpenedorderID = info.orderId;
        showText(json);
    }


    public void showText(com.alibaba.fastjson.JSONObject json) {
        mShopListAdapter.setData(mShopList);
        mShopListAdapter.notifyDataSetChanged();

        TextView t = (TextView) findViewById(R.id.orderstatus);
        t.setText("  (" + json.getString("orderStatus") + ")");
        String strOrderSn = json.getString("orderSn");
        setPriceView(strOrderSn);

        setListViewHeightBasedOnChildren(json.getString("orderType"));

        String str = "订单流水号: " + strOrderSn;
        if (StringUtil.isEmpty(json.getString("usercode")) == false)
            str += "\n" + "客户码:" + json.getString("usercode");
        t = (TextView) findViewById(R.id.orderdetailid);
        t.setText(str);

        t = (TextView) findViewById(R.id.ordercommittime);
        t.setText("提交时间: " + json.getString("orderDateTime"));

        str = json.getString("citOrderSn");
        if (StringUtil.isEmpty(str) == false) {
            str = "商友订单号:" + str;
            TextView textView = (TextView) findViewById(R.id.citordersn);
            textView.setText(str);
        }

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                cancel();
            }
        });
        mDrawerLayout.openDrawer(mRightRelativeLayout);
    }

    private class GetCancelCallBack implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            mHandler.post(new Runnable() {
                public void run() {
                    ProgressDialogUtil.hide();
                }
            });
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(HistoryOrderActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                return;
            }
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                Logger.GetInstance().Exception(e.getMessage()+ "parse data is :" + msg);

            }
            final com.alibaba.fastjson.JSONObject jb = jsonObject;
            mHandler.post(new Runnable() {
                public void run() {
                    if(jb != null && "1".equals( jb.getString("errcode"))) {
                        Toast.makeText(getApplicationContext(), "订单已取消", Toast.LENGTH_LONG).show();
                        mCancelBtn.setVisibility(View.GONE);
                        TextView t = (TextView) findViewById(R.id.orderstatus);
                        t.setText("  (" + "已取消" + ")");
                        com.alibaba.fastjson.JSONObject j = getInfoOrderId(mOpenedorderID);
                        if (j != null) {
                            j.put("status_code", "1");
                            j.put("orderStatus", "已取消");
                            j.put("pay_code", "1");
                            for (int i = 0; i < mOrderList.size(); i++) {
                                OrderInfo info = mOrderList.get(i);
                                if (info.orderId.equals(mOpenedorderID) == false)
                                    continue;
                                info.status = "已取消";
                            }
                            mAdapter.notifyDataSetChanged();
                            Button btn = (Button) findViewById(R.id.to_pay);
                            btn.setVisibility(View.GONE);
                            TextView textview = (TextView) findViewById(R.id.timeleft);
                            textview.setVisibility(View.GONE);
                            stopCheckOrder();
                        }
                    }
                    else{
                        if(jb != null) {
                            String str1 = jb.getString("errmsg");
                            String str2 = jb.getString("msg");
                            String str3 = "服务器返回错误";
                            String str4 = StringUtil.isEmpty(str1) ? str2 : str1;
                            final String str = StringUtil.isEmpty(str4) ? str3 : str4;
                            mHandler.post(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void cancel(){
        JSONObject cancelObj = new JSONObject();
        cancelObj.put("op", "cancelorder");
        cancelObj.put("orderid", mOpenedorderID);
        com.alibaba.fastjson.JSONObject orderInfo = getInfoOrderId(mOpenedorderID);
        JSONObject cancelObj1 = new JSONObject();
        cancelObj1.put("orderSn", orderInfo.getString("orderSn"));
        cancelObj1.put("orderType", orderInfo.getString("orderType"));
        cancelObj.put("data", cancelObj1);
        ProgressDialogUtil.show(this);
        mApp.post(Constants.getServiceUrl(), JsonPostParamBuilder.makeParam(cancelObj), mCallBack);
    }

    public com.alibaba.fastjson.JSONObject getInfoOrderId(String orderId) {
        for (int i = 0; i < mJSonOrder.size(); i++) {
            com.alibaba.fastjson.JSONObject ob = (com.alibaba.fastjson.JSONObject) mJSonOrder.get(i);
            if (ob.get("orderSn").equals(orderId)) {
                return ob;
            }
        }
        return null;
    }

    private void setPriceView(String orderId) {
        com.alibaba.fastjson.JSONObject j = getInfoOrderId(orderId);

        TextView textview = (TextView) findViewById(R.id.pricecalc);
        textview.setText("商品总价:  ¥" + j.getString("goodsPrice"));
        TextView textview2 = (TextView) findViewById(R.id.pricetv);
        textview2.setText("折扣优惠:  - ¥" + j.getString("discountActive"));
        TextView textview3 = (TextView) findViewById(R.id.pricetotal);
        textview3.setText("合计:  ¥" + j.getString("finalPrice"));
        String str = j.getString("orderType");
        if ("2".equals(str)) {
            //满减优惠
            textview = (TextView) findViewById(R.id.fullActive);
            textview.setVisibility(View.VISIBLE);
            textview.setText("满减优惠:  - ¥" + j.getString("fullActive"));
            //优惠券
            textview = (TextView) findViewById(R.id.couponMoney);
            textview.setVisibility(View.VISIBLE);
            textview.setText("优惠券:  - ¥" + j.getString("couponMoney"));

            //红包
            textview = (TextView) findViewById(R.id.cashMoney);
            textview.setVisibility(View.VISIBLE);
            textview.setText("红包:  - ¥" + j.getString("cashMoney"));

            //商品税
            textview = (TextView) findViewById(R.id.money_rate);
            textview.setVisibility(View.VISIBLE);
            textview.setText("商品税:  + ¥" + j.getString("money_rate"));
            //购物卡
            textview = (TextView) findViewById(R.id.cardMoney);
            textview.setVisibility(View.VISIBLE);
            textview.setText("礼品卡:  - ¥" + j.getString("giftcardMoney"));

            Button btn = (Button) findViewById(R.id.to_pay);
            btn.setVisibility(View.VISIBLE);
            if (btn != null) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        toPay();
                    }
                });
            }

            String strCanPay = j.getString("pay_code");
            btn.setVisibility("1".equals(strCanPay) ? View.GONE : View.VISIBLE);
            textview = (TextView) findViewById(R.id.timeleft);
            textview.setVisibility(btn.getVisibility());

            String strcancel = j.getString("status_code");
            btn = (Button) findViewById(R.id.cancel_btn);
            btn.setVisibility("1".equals(strcancel) ? View.GONE : View.VISIBLE);
        } else {
            findViewById(R.id.fullActive).setVisibility(View.GONE);
            findViewById(R.id.couponMoney).setVisibility(View.GONE);
            findViewById(R.id.money_rate).setVisibility(View.GONE);
            findViewById(R.id.cardMoney).setVisibility(View.GONE);
            findViewById(R.id.to_pay).setVisibility(View.GONE);
            findViewById(R.id.cancel_btn).setVisibility(View.GONE);
            findViewById(R.id.cashMoney).setVisibility(View.GONE);
        }
        textview = (TextView) findViewById(R.id.timeleft);
        textview.setText("");
    }

    public void toPay(){
        com.alibaba.fastjson.JSONObject j = new com.alibaba.fastjson.JSONObject();

        com.alibaba.fastjson.JSONObject j1 = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject orderInfo = getInfoOrderId(mOpenedorderID);
        j1.put("orderSn", orderInfo.getString("orderSn"));
        j.put("data", j1);
        j.put("op", "topay");

        try {
            ProgressDialogUtil.show(this);
            mApp.post(Constants.getServiceUrl(), JsonPostParamBuilder.makeParam(j), new INetWorkCallBack() {
                public void PostProcess(int msgId, String msg) {
                    final int error = msgId;
                    final String message = msg;
                    mCuiHandler.post(new Runnable() {
                        public void run() {
                            payNetDataStatuys(error, message);
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据时间戳更新订单状态
    public void payNetDataStatuys(int msgId, String msg) {
        ProgressDialogUtil.hide();
        NetExceptionAlert netExceptionAlert = new NetExceptionAlert(this, null);
        if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
            return;
        }
        com.alibaba.fastjson.JSONObject j = com.alibaba.fastjson.JSONObject.parseObject(msg);
        String type = j.getString("errcode");
        if (type.equals("1")) {
            com.alibaba.fastjson.JSONObject data = j.getJSONObject("data");
            Intent intent = new Intent();
            intent.putExtra("zhifubaourl", data.getString("alipayUrl"));
            intent.putExtra("weixinurl", data.getString("wxPayUrl"));
            intent.putExtra("orderSn", data.getString("orderSn"));
            intent.putExtra("finalPrice", data.getString("finalPrice"));
            intent.putExtra("successPayUrl", data.getString("successPayUrl"));
            intent.putExtra("citOrderSn", data.getString("citOrderSn"));
            final ArrayList<PickUpInfo> list = ClosingRefInfoMgr.getInstance().getAllPickUpAddr();
            for (int i = 0; i < list.size(); i++) {
                PickUpInfo info = list.get(i);
                if (("" + info.id).equals(data.getString("delivery_place"))) {
                    intent.putExtra("whereget", info.name);
                }
            }
            intent.setClass(HistoryOrderActivity.this, SelectPayTypeActivity.class);
            startActivity(intent);
        } else {
            String errMsg = j.getString("msg").toString();
            CustomAlertDialog customAlertDialog = new CustomAlertDialog();
            customAlertDialog.setMsgInfo(errMsg);
            customAlertDialog.setAction(new CustomAlertDialog.Action() {
                @Override public void process() {}
                @Override public void close() {}
            });
            customAlertDialog.show(getFragmentManager(), "customAlertDialog");
        }
    }

    //根据时间戳更新订单状态
    public void checkOrderStatus() {
        com.alibaba.fastjson.JSONObject j = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject j1 = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject orderInfo = getInfoOrderId(mOpenedorderID);
        j1.put("orderSn", orderInfo.getString("orderSn"));
        j1.put("orderType", orderInfo.getString("orderType"));
        j.put("data", j1);
        j.put("op", "getremainingtime");

        try {
            mApp.post(Constants.getServiceUrl(), JsonPostParamBuilder.makeParam(j), new INetWorkCallBack() {
                public void PostProcess(int msgId, String msg) {
                    final int error = msgId;
                    final String message = msg;
                    mCuiHandler.post(new Runnable() {
                        public void run() {
                            processCheckStatuys(error, message);
                        }
                    });
                }
            });
        } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage() + " send data is :" + j.toJSONString());
            e.printStackTrace();
        }
    }

    public void processCheckStatuys(int msgId, String msg) {
        NetExceptionAlert netExceptionAlert = new NetExceptionAlert(HistoryOrderActivity.this, null);
        if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
            return;
        }
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
        String str = jsonObject.getString("errcode");
        if ("1".equals(str)) {
            final com.alibaba.fastjson.JSONObject orderInfo = getInfoOrderId(mOpenedorderID);
            String strOrderType = orderInfo.getString("orderType");
            com.alibaba.fastjson.JSONObject timeInfo = jsonObject.getJSONObject("data");
            TextView textview = (TextView) findViewById(R.id.timeleft);
            if (timeInfo != null) {
                String strCanPay = timeInfo.getString("pay_code");
                if ("0".equals(strCanPay)) {//可以支付
                    String strRemainingTime = timeInfo.getString("remainingTime");
                    String[] sourceStrArray = strRemainingTime.split(":");
                    String strHour = null;
                    String strMinute = null;
                    if (sourceStrArray.length == 2) {
                        strHour = sourceStrArray[0];
                        strMinute = sourceStrArray[1];
                    }
                    String text = null;
                    if (strHour == null || "0".equals(strHour))
                        text = "请于" + strMinute + "分钟";
                    else
                        text = "请于" + strHour + "小时" + strMinute + "分钟";

                    if ("1".equals(strOrderType)) {//结算台订单
                        textview.setText(text + "前往结算台结算,否则订单将自动取消");
                    } else {
                        textview.setText(text + "内完成支付,否则订单将自动取消");
                    }
                } else {
                    textview.setVisibility(View.GONE);
                    Button btn = (Button) findViewById(R.id.to_pay);
                    btn.setVisibility(View.GONE);

                    String strcancel = timeInfo.getString("pay_code");
                    btn = (Button) findViewById(R.id.cancel_btn);
                    if (btn.getVisibility() != View.GONE)
                        btn.setVisibility("0".equals(strcancel) ? View.GONE : View.VISIBLE);
                }
            }
        } else {
            String strMsg = jsonObject.getString("msg");
            if (strMsg != null) {
                Toast toast = Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public void stopCheckOrder() {
        if (timer == null)
            return;
        timer.cancel();
        timer.purge();
        timer = null;
    }

    DrawerListener myDrawerListener = new DrawerListener() {
        @Override public void onDrawerClosed(View drawerView) {stopCheckOrder();}
        @Override public void onDrawerOpened(View drawerView) {
            com.alibaba.fastjson.JSONObject orderInfo = getInfoOrderId(mOpenedorderID);
            if ("0".equals(orderInfo.getString("pay_code"))) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        checkOrderStatus();
                    }
                }, 1000, 60 * 1000);
            }
        }
        @Override public void onDrawerSlide(View drawerView, float slideOffset) {}
        @Override public void onDrawerStateChanged(int newState) {}
    };
}
