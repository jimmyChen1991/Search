package com.hhyg.TyClosing.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.*;
import android.widget.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.*;
import com.hhyg.TyClosing.info.PickUpInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.ui.dialog.CustomAlertDialog;
import com.hhyg.TyClosing.ui.dialog.CustomConfirmDialog;
import com.hhyg.TyClosing.util.ProgressDialogUtil;
import com.hhyg.TyClosing.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.umeng.analytics.MobclickAgent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hhyg.TyClosing.config.Constants.IS_DEBUG_MODE;

/**
 * Created by mjf on 16/8/18.
 */
public class OrderConformActivity extends Activity {
    private List lTicket = new ArrayList();
    private List lCard = new ArrayList();
    private List lHongbao = new ArrayList();
    private ArrayList<PickUpInfo> mPickUpInfos;
    private ticketAdapter adapter = null;
    private cardAdapter mCardAdapter = null;
    private hongbaoAdapter mHongbaoAdapter = null;
    private JSONObject obInfo = null;
    private JSONArray obGoods = null;
    private JSONObject ob = null;

    private String isTax = "0";//0 无税，1 有税
    private String taxFormula;//商品税公式
    private PickUpInfo mInfoPickUp = null;//用户选择额的提货机场
    private LinearLayout linearLayout;

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mRightRelativeLayout;

    private final String CANUSE = "1";  //可以使用
    private final String INUSE = "2"; //正在使用
    private final String CANTUSE = "3"; //正在使用
    private final String KEYTAG = "u";

    private checkTicketCallback mCheckTicketCallBack = new checkTicketCallback();
    private checkCardCallback mCheckCardCallback = new checkCardCallback();
    private cancelCardCallback mCancelCardCallback = new cancelCardCallback();
    private getCodeCallback mgetCodeCallback = new getCodeCallback();
    private getSendCodeCallback msendCodeCallback = new getSendCodeCallback();
    private checkCodeCallback mCheckodeCallback = new checkCodeCallback();
    private getHongbaoCallback mGetHongbaoCallback  = new getHongbaoCallback();


    private buyCallback mbuyCallBack = new buyCallback();
    private Dialog mDialog = null;

    private EditText mTicketEdit;
    private EditText mCardNumberEdit;
    private EditText mCarPasswordEdit;
    public android.os.Handler mHandler = new android.os.Handler();
    private ClosingRefInfoMgr mClosingRefInfoMgr = ClosingRefInfoMgr.getInstance();
    private String mGetGoodsTime;
    public Timer timer = null;

    private final  int TIMNERCOUNT = 60;
    private int nTimerLeft= 0;
    private String mStrPhone = "";
    private String mSign = null;
    private String mToken = null;
    private boolean mbCanGetCode = true;


    public void findView() {
        mTicketEdit = (EditText) findViewById(R.id.username_code_number);
        mCardNumberEdit = (EditText) findViewById(R.id.username_card_number);
        mCarPasswordEdit = (EditText) findViewById(R.id.username_card_password);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderconform);
        findView();
        adapter = new ticketAdapter(this);
        String strJson = getIntent().getStringExtra("data");
        com.alibaba.fastjson.JSONObject jsonObject = null;
        try {
            jsonObject = com.alibaba.fastjson.JSONObject.parseObject(strJson);
        } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage() + " json data is :" + strJson);
        }
        ob = jsonObject;
        obInfo = jsonObject.getJSONObject("userInfo");
        obGoods = jsonObject.getJSONArray("goodsSku");
        if("1".equals(jsonObject.getString("available"))){
            mStrPhone = jsonObject.getString("phone");
            mToken = jsonObject.getString("token");
            TextView t = (TextView)findViewById(R.id.memeberaccount);
            t.setText("账户 ：" + mStrPhone);
            RelativeLayout lay = (RelativeLayout)findViewById(R.id.main1);
            lay.setVisibility(View.VISIBLE);
        }
        else{
            TextView t = (TextView)findViewById(R.id.textview_no_hongbao);
            t.setVisibility(View.VISIBLE);
        }

        //有特权码商品
        if("0".equals(jsonObject.getString("SpecialCount")) == false){
            View t = findViewById(R.id.textview_title71);
            t.setVisibility(View.VISIBLE);

            EditText et = (EditText) findViewById(R.id.username_code_number);
            et.setEnabled(false);

            Button btn = (Button)findViewById(R.id.button_use_code);
            btn.setEnabled(false);

            TextView text = (TextView)findViewById(R.id.textview_no_hongbao);
            text.setText("*订单商品使用了特权码活动，不可再使用红包了哦~");
            text.setTextColor(android.graphics.Color.parseColor("#ff0000"));
            text.setTextSize(13);
            t.setVisibility(View.VISIBLE);
        }

        mInfoPickUp = MyApplication.GetInstance().getUserSelectAir();//(PickUpInfo)CacheUtilManager.getInstance().getDefaultCache().get(KEY_AIR_ATG);
        com.hhyg.TyClosing.ui.fragment.AutoSettleOrderItemsFragment fragment =
                (com.hhyg.TyClosing.ui.fragment.AutoSettleOrderItemsFragment)(getFragmentManager().findFragmentById(R.id.orderDetailFragment));
        fragment.initData();
        if(IS_DEBUG_MODE == true) {
//            EditText t = (EditText) findViewById(R.id.username_code_number);
//            t.setText("3b39a4cd");
//            t = (EditText) findViewById(R.id.username_card_number);
//            t.setText("100000350000145");
//            t = (EditText) findViewById(R.id.username_card_password);
//            t.setText("8cce8c7d");
        }

        //验证优惠券
        Button btn = (Button) findViewById(R.id.button_use_code);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clazz = this.getClass().getName();
                String method = Thread.currentThread().getStackTrace()[2].getMethodName();
                Logger.GetInstance().Debug("enter  " + clazz + "  " + method);
                if (checkTicket() == false)
                    return;

                com.alibaba.fastjson.JSONObject Json = new com.alibaba.fastjson.JSONObject();
                Json.put("op", "getcoupon");
                Json.put("number", mTicketEdit.getText().toString());
                Json.put("order_amount", ob.getString("finalPrice"));
                showDlg();

                try {
                    MyApplication.GetInstance().post(Constants.getIndexUrl() + "?r=coupons/validation",
                            JsonPostParamBuilder.makeParam(Json), mCheckTicketCallBack);
                } catch (Exception e) {
                    Logger.GetInstance().Exception(e.getMessage() + " send data is :" + Json.toString());
                    e.printStackTrace();
                }
            }
        });


        //添加礼品卡
        btn = (Button) findViewById(R.id.button_use_card);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clazz = this.getClass().getName();
                String method = Thread.currentThread().getStackTrace()[2].getMethodName();
                Logger.GetInstance().Debug("enter  " + clazz + "  " + method);

                if (checkCard() == false)
                    return;

                JSONObject map1 = new JSONObject();
                map1.put("giftCode", mCardNumberEdit.getText().toString());
                map1.put("giftPwd", mCarPasswordEdit.getText().toString());
                map1.put("orderPrice", getOrderPrice());
                map1.put("giftKey", getCardCaheckNetArg());
                com.alibaba.fastjson.JSONObject map = new com.alibaba.fastjson.JSONObject();
                map.put("op", "getgiftcard");
                map.put("data", map1);
                showDlg();
                try {
                    MyApplication.GetInstance().post(Constants.getServiceUrl() + "?r=giftcard/search",
                            JsonPostParamBuilder.makeParam(map), mCheckCardCallback);
                } catch (Exception e) {
                    Logger.GetInstance().Exception(e.getMessage() + " send data is :" + map.toString());
                    e.printStackTrace();
                }
            }
        });

        //获取验证码
        btn = (Button) findViewById(R.id.button_get_code);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Button btn = (Button) findViewById(R.id.button_get_code);
                btn.setClickable(false);
                getCode(mStrPhone);
            }
        });

        //验证验证码
        btn = (Button) findViewById(R.id.button_code_check);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                EditText t = (EditText) findViewById(R.id.member_input_code);
                String str = t.getText().toString();
                if(StringUtil.isEmpty(str)){
                    showToast("请输入验证码");
                    return;
                }
                JSONObject mapData = new JSONObject();
                mapData.put( "mobile_phone", mStrPhone);
                mapData.put( "token", mToken);
                mapData.put( "code",str);
                mapData.put( "finalPrice",getJsonObjet(ob, "finalPrice", "0.00"));

                JSONObject map = new JSONObject();
                map.put( "op", "getdiscountinfo");
                map.put( "data", mapData);

                showDlg();
                try {
                    String url = Constants.getIndexUrl() + "?r=userdiscount/getdiscountinfo";
                    MyApplication.GetInstance().post(url, JsonPostParamBuilder.makeParam(map), mGetHongbaoCallback);
                } catch (Exception e) {
                    Logger.GetInstance().Exception(e.getMessage() + " send data is :" + map.toString()) ;
                }
            }
        });

        //去支付
        btn = (Button) findViewById(R.id.button_goto_pay);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                com.hhyg.TyClosing.ui.dialog.OrderConfirmDialog customConfirmDialog = new com.hhyg.TyClosing.ui.dialog.OrderConfirmDialog();
                obInfo.put("port", mInfoPickUp.name);
                customConfirmDialog.setMsgInfo(obInfo.toString());
                customConfirmDialog.setConfirmBtnText(getString(R.string.goback_to_shopcart));
                customConfirmDialog.setTime(mGetGoodsTime);
                customConfirmDialog.setCancelBtnText(getString(R.string.queding));
                customConfirmDialog.setAction(new CustomConfirmDialog.Action() {
                    @Override public void process() {toBuy();}
                    @Override public void cancel() {}
                    @Override public void close() {}
                });
                customConfirmDialog.show(getFragmentManager(), "customConfirmDialog");
            }
        });


        ImageButton scanBt = (ImageButton) findViewById(R.id.button_scan1);
        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String clazz = this.getClass().getName();
                Logger.GetInstance().Debug("enter  " + clazz + "  " + "button_scan1 onClick");
                finish();
            }
        });

        //所有可用的优惠券
        ListView listView = (ListView) findViewById(R.id.list_ticket);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                JSONObject jsonobject = (JSONObject) lTicket.get(arg2);
                int nResult = touchedTicketData(jsonobject);
                if(3 == nResult || 0 == nResult ){
                    showToast("不满足使用条件");
                }
//                if (INUSE.equals(JosnGetString(jsonobject, KEYTAG))) {
//                    jsonobject.put( KEYTAG, CANUSE);
//                    updateAll();
//                } else {
//                    double d = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00")) - getCardMoney() - getHongbaoMoney();
//                    float fRule = getRuleMoney(jsonobject);
//                    if (d - fRule > 0) {
//                        for (int i = 0; i < lTicket.size(); i++) {
//                            JSONObject j = (JSONObject) lTicket.get(i);
//                            j.put( KEYTAG, CANUSE);
//                        }
//                        jsonobject.put(KEYTAG, INUSE);
//                        updateAll();
//                    } else {
//                        showToast("不满足使用条件");
//                    }
//                }
            }
        });
        //所有可用的礼品卡
        listView = (ListView) findViewById(R.id.listViewCard);
        mCardAdapter = new cardAdapter(this);
        listView.setAdapter(mCardAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                JSONObject jsonobject = (JSONObject) lCard.get(arg2);
                if (INUSE.equals(JosnGetString(jsonobject, KEYTAG))) {
                    cancelCard(getJsonObjet(jsonobject, "id", ""), INUSE);
                } else if (CANUSE.equals(JosnGetString(jsonobject, KEYTAG))) {
                    float fAll = getCardMoney() + getTicketMoney() + getHongbaoMoney();
                    double d = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00"));
                    String str = getJsonObjet(jsonobject, "money", "");
                    if (d - fAll <= Float.parseFloat(str)) {
                        showToast("不满足使用条件");
                    } else {
                        cancelCard(getJsonObjet(jsonobject, "id", ""), CANUSE);
                    }
                }
            }
        });

        //所有可用的红包
        listView = (ListView) findViewById(R.id.list_hongbao);
        mHongbaoAdapter = new hongbaoAdapter(this);
        listView.setAdapter(mHongbaoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                JSONObject jsonobject = (JSONObject) lHongbao.get(arg2);
                if (INUSE.equals(JosnGetString(jsonobject, KEYTAG))) {
                    jsonobject.put( KEYTAG, CANUSE);
                    updateAll();
                } else {
                    double d = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00")) - getCardMoney() - getTicketMoney();
                    float fRule = getHongbaoRuleMoney(jsonobject);
                    if (d - fRule > 0) {
                        for (int i = 0; i < lHongbao.size(); i++) {
                            JSONObject j = (JSONObject) lHongbao.get(i);
                            j.put( KEYTAG, CANUSE);
                        }
                        jsonobject.put(KEYTAG, INUSE);
                        updateAll();
                    } else {
                        showToast("不满足使用条件");
                    }
                }
            }
        });


        setUserInfo();
        setGoodsImages();
        setMoney();
        floatLayer();
        setProductTax();
        EditText edit = (EditText) findViewById(R.id.selectTime);
        findViewById(R.id.textview_time).setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        setHeightByID(R.id.toolbar5, 320);

        Logger.GetInstance().Track("OrderConformActivity on Create");
    }

    public void stoptimer() {
        mHandler.post(new Runnable() {
            public void run() {
                if (timer != null){
                    timer.cancel();
                    timer.purge();
                    timer = null;
                    Button btn = (Button) findViewById(R.id.button_get_code);
                    btn.setText("获取验证码");
                    btn.setEnabled(true);
                    btn.setClickable(true);
                }
            }
        });
    }

    public void starttimer() {
        if(timer!= null)
            return;
        nTimerLeft = 60;
        timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                if(nTimerLeft == 0){
                    stoptimer();
                }
                else {
                    mHandler.post(new Runnable() {
                        public void run() {
                            Button btn = (Button) findViewById(R.id.button_get_code);
                            btn.setText("剩余"+nTimerLeft+"秒");
                            btn.setEnabled(false);
                            nTimerLeft--;
                        }
                    });
                }
            }
        }, 0,  1000);
    }

    private boolean checkCard() {
        boolean bReturn = true;
        final String Input = mCardNumberEdit.getText().toString();
        if (Input.equals("")) {
            showToast("请填写礼品卡");
            bReturn = false;
        }

        if (bReturn == true) {
            final String password = mCarPasswordEdit.getText().toString();
            if (password.equals("")) {
                showToast("请填写礼品卡密码");
                bReturn = false;
            }
        }
        if (bReturn == true) {
            for (int i = 0; i < lCard.size(); i++) {
                JSONObject jsonobject = (JSONObject) lCard.get(i);
                String str = jsonobject.getString("barcode");
                if (Input.equals(str)) {
                    showToast("礼品卡不能重复验证使用");
                    bReturn = false;
                }
            }
        }
        return bReturn;
    }

    //验证码成功之后显示的UI
    private  void showSuccessUI(){
        View t = findViewById(R.id.button_success_icon);
        t.setVisibility(View.VISIBLE);

        t = findViewById(R.id.button_success_txt);
        t.setVisibility(View.VISIBLE);

        t = findViewById(R.id.toolbar119);
        t.setVisibility(View.GONE);

        t = findViewById(R.id.button_code_check);
        t.setVisibility(View.GONE);
    }



    private void resetTicketData() {
        double fall = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00"));
        for (int i = 0; i < lTicket.size(); i++) {
            JSONObject jsonobject = (JSONObject) lTicket.get(i);
            if(fall <= getRuleMoney(jsonobject))//不满足使用条件
                jsonobject.put(KEYTAG,CANTUSE);
        }
    }


    /*
    返回值：0，不可使用
            1，取消选中
            2，选中正在使用
            3，满足使用门槛，但优惠额度太大，产品要求用户至少要掏一分钱
    */
    private int  touchedTicketData(JSONObject jsonTouched) {
        if(CANTUSE.equals(jsonTouched.getString(KEYTAG)))//不满足使用条件
            return 0;
        if (INUSE.equals(JosnGetString(jsonTouched, KEYTAG))) {
            jsonTouched.put(KEYTAG, CANUSE);
            updateAll();
            return 1;
        }
        else{
            double fall = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00")) - getCardMoney() - getHongbaoMoney();
            float fRule = 0;
            String str = getJsonObjet(jsonTouched, "rule", "");
            String[] chrstr = str.split(":");
            if(chrstr.length == 2)
                fRule = Float.parseFloat(chrstr[1]);//优惠的金额
            if (fall - fRule > 0) { //用户至少要掏一分钱
                for (int i = 0; i < lTicket.size(); i++) {
                    JSONObject j = (JSONObject) lTicket.get(i);
                    if(CANTUSE.equals(j.getString(KEYTAG)) == false)
                        j.put(KEYTAG, CANUSE);
                }
                jsonTouched.put(KEYTAG, INUSE);
                updateAll();
                return 2;
            }
            else{
                return 3;
            }
        }
    }


    private void setDefaultValue(){
        JSONObject jsonobjectMax = null;
        float fMax = 0;

        double fall = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00")) - getCardMoney();
        for (int i = 0; i < lTicket.size(); i++) {
            JSONObject jsonobject = (JSONObject) lTicket.get(i);
            if(CANUSE.equals(jsonobject.getString(KEYTAG)) == false)
                continue;
            float fRule = 0;
            String str = getJsonObjet(jsonobject, "rule", "");
            String[] chrstr = str.split(":");
            if(chrstr.length == 2)
                fRule = Float.parseFloat(chrstr[1]);//优惠的金额
            if(fall - fRule > 0){
                if(fMax < fRule){
                    fMax = fRule;
                    jsonobjectMax = jsonobject;
                }
            }
        }

        if(jsonobjectMax != null){
            touchedTicketData(jsonobjectMax);
        }

        jsonobjectMax = null;
        fMax = 0;
        fall = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00")) - getCardMoney() - getTicketMoney();
        for (int i = 0; i < lHongbao.size(); i++) {
            JSONObject jsonobject = (JSONObject) lHongbao.get(i);
            if (CANUSE.equals(jsonobject.getString(KEYTAG)) == false)
                continue;
            float fRule = getHongbaoRuleMoney(jsonobject);
            if (fall - fRule > 0) {
                if(fMax < fRule){
                    fMax = fRule;
                    jsonobjectMax = jsonobject;
                }
            }
        }


        if(jsonobjectMax != null){
            for (int i = 0; i < lHongbao.size(); i++) {
                JSONObject j = (JSONObject) lHongbao.get(i);
                j.put( KEYTAG, CANUSE);
            }
            jsonobjectMax.put(KEYTAG, INUSE);
        }
    }


    private boolean checkTicket() {
        boolean bReturn = true;
        final String Input = mTicketEdit.getText().toString();
        if (Input.equals("")) {
            showToast("请填写优惠券");
            bReturn = false;
        }

        if (bReturn == true) {
            for (int i = 0; i < lTicket.size(); i++) {
                JSONObject jsonobject = (JSONObject) lTicket.get(i);
                String str = jsonobject.getString("id");
                if (Input.equals(str)) {
                    showToast("优惠券不能重复验证使用");
                    bReturn = false;
                }
            }
        }
        return bReturn;
    }

    private class checkTicketCallback implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                Logger.GetInstance().Error("Json parse error :" + msg);
            }

            mHandler.post(new Runnable() {
                public void run() {
                    hideDlg();
                }
            });
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(OrderConformActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                return;
            }
            final JSONObject data = jsonObject.getJSONObject("data");
            String type = data.getString("type");
            if (type.equals("1")) {
                data.put("id", mTicketEdit.getText().toString());
                data.put(KEYTAG,CANUSE);
                lTicket.add(data);
                mHandler.post(new Runnable() {
                    public void run() {
                        setHeightByID(R.id.toolbar7, 310 + lTicket.size() * 100);
                        resetTicketData();
                        double d = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00"));
                        float fleftMoney = (float) d - getCardMoney() - getHongbaoMoney();
                        String str = getJsonObjet(data, "rule", "");
                        String[] chrstr = str.split(":");
                        float fRule = 0;
                        if (chrstr.length == 2) {
                            fRule = Float.parseFloat(chrstr[1]);
                        }
                        if (fleftMoney > fRule) {
                            for (int i = 0; i < lTicket.size(); i++) {
                                JSONObject jsonobject = (JSONObject) lTicket.get(i);
                                if(CANTUSE.equals(jsonobject.getString(KEYTAG)))
                                    continue;
                                jsonobject.put(KEYTAG,CANUSE);
                            }
                            data.put(KEYTAG, INUSE);
                        }
                        updateAll();
                    }
                });
            } else {
                final String str = data.getString("info");
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

    private float getRuleMoney(JSONObject j){
        String str = getJsonObjet(j, "rule", "");
        String[] chrstr = str.split(":");
        if(chrstr.length == 2) {
            return Float.parseFloat(chrstr[0]);
        }
        return 0;
    }

    private float getHongbaoRuleMoney(JSONObject j){
        String str = getJsonObjet(j, "money", "");
        return Float.parseFloat(str);
    }

    private class checkCodeCallback implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                Logger.GetInstance().Error("Json parse error :" + msg);
            }

            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(OrderConformActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                return;
            }
            String type = jsonObject.getString("errcode");
            if (type.equals("1")) {
                final JSONObject data = jsonObject.getJSONObject("data");
                EditText t = (EditText) findViewById(R.id.username_card_number);
                final String Input = t.getText().toString();
                data.put("id", Input);
                data.put(KEYTAG, CANUSE);
                mHandler.post(new Runnable() {
                    public void run() {
                        lCard.add(data);
                        setHeightByID(R.id.toolbar8, 480 + lCard.size() * 100);
                        updateAll();
                        double d = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00"));
                        float fleftMoney = (float) d - getTicketMoney() - getCardMoney();
                        if (fleftMoney > Float.parseFloat(getJsonObjet(data, "money", "0")))
                            cancelCard(Input, CANUSE);
                    }
                });
            } else {
                final String str = getJsonObjet(jsonObject, "msg", "");
                mHandler.post(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        hideDlg();
                    }
                });
            }
        }
    }


    private class checkCardCallback implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                Logger.GetInstance().Error("Json parse error :" + msg);
            }

            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(OrderConformActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                return;
            }
            String type = jsonObject.getString("errcode");
            if (type.equals("1")) {
                final JSONObject data = jsonObject.getJSONObject("data");
                EditText t = (EditText) findViewById(R.id.username_card_number);
                final String Input = t.getText().toString();
                data.put("id", Input);
                data.put(KEYTAG, CANUSE);
                mHandler.post(new Runnable() {
                    public void run() {
                        lCard.add(data);
                        setHeightByID(R.id.toolbar8, 480 + lCard.size() * 100);
                        updateAll();
                        double d = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00"));
                        float fleftMoney = (float) d - getTicketMoney() - getCardMoney();
                        if (fleftMoney > Float.parseFloat(getJsonObjet(data, "money", "0")))
                            cancelCard(Input, CANUSE);
                    }
                });
            } else {
                final String str = getJsonObjet(jsonObject, "msg", "");
                mHandler.post(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        hideDlg();
                    }
                });
            }
        }
    }

    private List getdeliverTime() {
        List l = new ArrayList();
        String[] aa = JosnGetString(ob, "deliverTime").split(",");
        if (aa.length == 2) {
            Date date1 = formateDate(aa[0], "yyyy-MM-dd HH:mm");
            l.add(date1);
            Date date2 = formateDate(aa[1], "yyyy-MM-dd HH:mm");
            l.add(date2);
        }
        return l;
    }

    private Date formateDate(String dstr, String strFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
        Date date = null;
        try {
            date = sdf.parse(dstr);
        } catch (ParseException e) {
            Logger.GetInstance().Exception(e.getMessage() + " str is :" + sdf + " and format is " + strFormat );
            e.printStackTrace();
        }
        return date;
    }

    public String getTicketNetArg() {
        for (int i = 0; i < lTicket.size(); i++) {
            JSONObject jsonobject = (JSONObject) lTicket.get(i);
            String str = getJsonObjet(jsonobject, KEYTAG, "");
            if   (INUSE.equals(str))
                return getJsonObjet(jsonobject, "id", "");
        }
        return "";
    }


    public float getCardMoney() {
        float fReturn = 0;
        for (int i = 0; i < lCard.size(); i++) {
            JSONObject jsonobject = (JSONObject) lCard.get(i);
            String str = getJsonObjet(jsonobject, KEYTAG, "");
            if (INUSE.equals(str)) {
                str = getJsonObjet(jsonobject, "money", "");
                fReturn += Float.parseFloat(str);
            }
        }
        return fReturn;
    }

    public float getTicketMoney() {
        for (int i = 0; i < lTicket.size(); i++) {
            JSONObject jsonobject = (JSONObject) lTicket.get(i);
            String str = getJsonObjet(jsonobject, KEYTAG, "");
            if (INUSE.equals(str)) {
                str = getJsonObjet(jsonobject, "reduce_money", "");
                return Float.parseFloat(str);
            }
        }
        return 0;
    }

    public float getHongbaoMoney() {
        for (int i = 0; i < lHongbao.size(); i++) {
            JSONObject jsonobject = (JSONObject) lHongbao.get(i);
            String str = getJsonObjet(jsonobject, KEYTAG, "");
            if (INUSE.equals(str)) {
                str = getJsonObjet(jsonobject, "money", "");
                return Float.parseFloat(str);
            }
        }
        return 0;
    }



    public void updateAll() {
        mHandler.post(new Runnable() {
            public void run() {
                mCardAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                mHongbaoAdapter.notifyDataSetChanged();
                setMoney();
            }
        });
    }

    public String getHongbaoNetArg() {
        String strReturn = "";
        for (int i = 0; i < lHongbao.size(); i++) {
            JSONObject jsonobject = (JSONObject) lHongbao.get(i);
            String str = getJsonObjet(jsonobject, "bonus_id", "");
            if (INUSE.equals(getJsonObjet(jsonobject, KEYTAG, ""))) {
                strReturn = str;
                break;
            }
        }
        return strReturn;
    }



    public String getCardNetArg() {
        String strReturn = "";
        for (int i = 0; i < lCard.size(); i++) {
            JSONObject jsonobject = (JSONObject) lCard.get(i);
            String str = getJsonObjet(jsonobject, "temp_order_key", "");
            if (INUSE.equals(getJsonObjet(jsonobject, KEYTAG, ""))) {
                strReturn = str;
                break;
            }
        }
        return strReturn;
    }

    public String getCardCaheckNetArg() {
        for (int i = 0; i < lCard.size(); i++) {
            JSONObject jsonobject = (JSONObject) lCard.get(i);
            String str = getJsonObjet(jsonobject, "temp_order_key", "");
            if (StringUtil.isEmpty(str) == false)
                return str;
        }
        return "";
    }


    public String getOrderPrice() {
        float fAll = getCardMoney() + getTicketMoney() + getHongbaoMoney();
        double d = Double.parseDouble(getJsonObjet(ob, "finalPrice", "0.00"));
        float f = (float) d - fAll;
        if (f < 0) {
            f = 0;
            Logger.GetInstance().Error("getOrderPrice Error : f is " + f);
        }
        String strReturn = String.format("%.2f", ((f * 100) / 100.0f));
        return strReturn;
    }


    private class cancelCardCallback implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            mHandler.post(new Runnable() {
                public void run() {
                    hideDlg();
                }
            });
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                Logger.GetInstance().Exception(e.getMessage() + " parse string is :" + msg);
            }
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(OrderConformActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                return;
            }
            String str = jsonObject.getString("errcode");
            if (StringUtil.isEmpty(str) == false) {
                if (str.equals("1")) {
                    JSONObject j1 = jsonObject.getJSONObject("data");
                    final String checkFlag = getJsonObjet(j1, "checkFlag", "");
                    final String barcode = getJsonObjet(j1, "barcode", "");
                    mHandler.post(new Runnable() {
                        public void run() {
                            for (int i = 0; i < lCard.size(); i++) {
                                JSONObject jsonobject = (JSONObject) lCard.get(i);
                                String str1 = getJsonObjet(jsonobject, "id", "");
                                if (str1.equals(barcode)) {
                                    jsonobject.put(KEYTAG, "0".equals(checkFlag) ? CANUSE : INUSE);
                                    break;
                                }
                            }
                            updateAll();
                        }
                    });
                } else {
                    str = getJsonObjet(jsonObject, "msg", "");
                    showToast(str);
                }
            }
        }
    }

    public void cancelCard(String strCardNumber, String isInUse) {
        if (StringUtil.isEmpty(strCardNumber))
            return;
        JSONObject map = new JSONObject();
        map.put( "op", "giftcardcheckin");
        JSONObject map1 = new JSONObject();
        map1.put( "giftCode", strCardNumber);
        map1.put( "checkFlag", INUSE.equals(isInUse) ? "0" : "1");
        map1.put( "giftKey", getCardCaheckNetArg());
        map.put("data", map1);

        showDlg();
        try {
            MyApplication.GetInstance().post(Constants.getIndexUrl() + "?r=giftcard/checkin",
                    JsonPostParamBuilder.makeParam(map), mCancelCardCallback);
        } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage() + " send data is :" + map.toJSONString());
        }
    }

    public void getCode(String strPhone) {
        if (StringUtil.isEmpty(strPhone))
            return;

        JSONObject mapData = new JSONObject();
        mapData.put( "mobilephone", strPhone);
        JSONObject map = new JSONObject();
        map.put( "op", "secury");
        map.put( "data", mapData);
        showDlg();
        try {
            String url =Constants.getIndexUrl();
            url += "?r=login/secury";
            MyApplication.GetInstance().post(url,
                    JsonPostParamBuilder.makeParam(map), mgetCodeCallback);
        } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage() + " send data is :" + map.toJSONString());
        }
    }
    private class getCodeCallback implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            mHandler.post(new Runnable() {
                public void run() {
                    hideDlg();
                }
            });
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                Logger.GetInstance().Exception(e.getMessage() + " msg is :" + msg);
            }
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(OrderConformActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                setBtnEnable();
                return;
            }
            String str = jsonObject.getString("errcode");
            if (StringUtil.isEmpty(str) == false && str.equals("1")) {
                com.alibaba.fastjson.JSONObject ob1  = jsonObject.getJSONObject("data");
                if(ob1 != null) {
                    mSign = ob1.getString("sign");
                    sendCodeToPhone(ob1.getString("timestamp"), ob1.getString("sign"));
                }
            }
            else{
                String strError = jsonObject.getString("msg");
                if(StringUtil.isEmpty(strError))
                    strError = "服务器数据错误";
                showToast(strError);
                setBtnEnable();
            }
        }
    }
    public void sendCodeToPhone(String timestamp,String sign ) {
        if (StringUtil.isEmpty(mStrPhone))
            return;

        JSONObject mapData = new JSONObject();
        mapData.put( "mobilephone", mStrPhone);
        mapData.put( "timestamp", timestamp);
        mapData.put( "sign", sign);
        JSONObject map = new JSONObject();
        map.put( "op", "send");
        map.put( "data", mapData);

        mHandler.post(new Runnable() {
            public void run() {
                showDlg();
            }
        });

        try {
            String url =  Constants.getIndexUrl();
            url += "?r=login/send";
            MyApplication.GetInstance().post(url,
                    JsonPostParamBuilder.makeParam(map), msendCodeCallback);
        } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage() + " send data is :" + map.toJSONString());
        }
    }

    private class getSendCodeCallback implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            mHandler.post(new Runnable() {
                public void run() {
                    hideDlg();
                }
            });
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                Logger.GetInstance().Exception(e.getMessage()+ " parse data is :" + msg);
            }
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(OrderConformActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                setBtnEnable();
                return;
            }
            String str = jsonObject.getString("errcode");
            if (StringUtil.isEmpty(str) == false && str.equals("1")) {
                mHandler.post(new Runnable() {
                    public void run() {
                        starttimer();
                    }
                });
            }
            else{
                String strError = jsonObject.getString("msg");
                if(StringUtil.isEmpty(strError))
                    strError = "服务器数据错误";
                showToast(strError);
                setBtnEnable();
            }
        }
    }

    private void setBtnEnable(){
        if( mbCanGetCode == false)
            return;;
        mHandler.post(new Runnable() {
            public void run() {
                Button btn = (Button) findViewById(R.id.button_get_code);
                btn.setText("获取验证码");
                btn.setEnabled(true);
                btn.setClickable(true);
            }
        });
    }



    private class getHongbaoCallback implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            mHandler.post(new Runnable() {
                public void run() {
                    hideDlg();
                }
            });
            com.alibaba.fastjson.JSONObject jsonObject = null;
            try {
                jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
            } catch (Exception e) {
                Logger.GetInstance().Exception(e.getMessage()+ " parse data is :" + msg);
            }
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(OrderConformActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                return;
            }
            String str = jsonObject.getString("errcode");
            if (StringUtil.isEmpty(str) == false && str.equals("1")) {
                com.alibaba.fastjson.JSONObject hongbaoOb = jsonObject.getJSONObject ("data");
                if(hongbaoOb != null) {
                    lHongbao.clear();
                    com.alibaba.fastjson.JSONArray arr = hongbaoOb.getJSONArray("bonus");//红包
                    for (int i = 0 ; i < arr.size(); i++){
                        com.alibaba.fastjson.JSONObject data = arr.getJSONObject(i);
                        data.put("id", data.getString("bonus_id"));
                        data.put(KEYTAG, CANUSE);
                        lHongbao.add(data);
                    }

                    lTicket.clear();
                    //去掉一次性从网络接口返回的优惠券，防止数据多次加载
                    for (int i = lTicket.size() - 1 ; i >= 0 ; i--){
                        com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject)lTicket.get(i);
                        if("0".equals(data.getString("isuerinput"))){
                            lTicket.remove(i);
                        }
                    }

                    arr = hongbaoOb.getJSONArray("coupons");//优惠券
                    for (int i = 0 ; i < arr.size(); i++){
                        com.alibaba.fastjson.JSONObject data = arr.getJSONObject(i);
                        data.put("id", data.getString("code_str"));
                        data.put(KEYTAG, CANUSE);
                        data.put("isuerinput", "0");
                        lTicket.add(data);
                    }
                    resetTicketData();
                    setDefaultValue();
                }
                mHandler.post(new Runnable() {
                    public void run() {
                        if(lHongbao.size() != 0){
                            RelativeLayout layout = (RelativeLayout) findViewById(R.id.toolbar71);
                            layout.setVisibility(View.VISIBLE);
                            TextView t = (TextView)findViewById(R.id.textview_no_hongbao);
                            t.setVisibility(View.GONE);
                            setHeightByID(R.id.toolbar71, 160 + lHongbao.size() * 100);
                        }
                        else{
                            TextView t = (TextView)findViewById(R.id.textview_no_hongbao);
                            t.setVisibility(View.VISIBLE);
                        }

                        setHeightByID(R.id.toolbar7, 310 + lTicket.size() * 100);
                        updateAll();

                        showSuccessUI();
                    }
                });
            }
            else{
                String strErrorMsg = jsonObject.getString("msg");
                if(StringUtil.isEmpty(strErrorMsg))
                    strErrorMsg = "验证码验证错误，稍后请重发验证码，并再次验证";
                final String msgerr = strErrorMsg;
                mHandler.post(new Runnable() {
                    public void run() {
                        showToast(msgerr);
                    }
                });
            }
        }
    }

    public void toBuy() {
        String clazz = this.getClass().getName();
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        Logger.GetInstance().Debug("enter  " + clazz + "  " + "button_goto_pay onClick");
        final JSONObject mapData = new JSONObject();
        JSONObject mapData1 = new JSONObject();
        mapData1.put("idCard", obInfo.get("idCard"));
        mapData1.put("flightDate", obInfo.get("flightDate"));
        mapData1.put("userName", obInfo.get("userName"));
        mapData1.put("flightNum", obInfo.get("flightNum"));
        if (ClosingRefInfoMgr.getInstance().isShopTypeOutside()) {
            String[] aa = JosnGetString(ob, "deliverTime").split(",");
            if (aa.length == 2) {
                mapData1.put("deliverTime", aa[0]);
            }
        } else {
            mapData1.put("deliverTime", JosnGetString(ob, "deliverTime"));
        }

        mapData1.put("couponCode", getTicketNetArg());
        mapData1.put("giftcardCode", "");
        mapData1.put("giftcardPwd", "");
        mapData1.put("giftcardKey", getCardNetArg());
        mapData1.put("bonusNumber",getHongbaoNetArg());
        mapData1.put("goodsSku", getGoodAgr());
        mapData1.put("token",mToken);
        mapData1.put("phone", obInfo.get("phone"));
        PickUpInfo info = MyApplication.GetInstance().getUserSelectAir();//(PickUpInfo) CacheUtilManager.getInstance().getDefaultCache().get(KEY_AIR_ATG);
        mapData1.put("deliverPlace", info.id);
        mapData1.put("submitTime", new Date().getTime());
        mapData.put("data", mapData1);
        mapData.put("op", "owntopay");

        showDlg();
        String strJson = JsonPostParamBuilder.makeParam(mapData);
        try {
            String url = Constants.getServiceUrl();
            MyApplication.GetInstance().post(url, strJson, mbuyCallBack);
        } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage()+ " send data is :" + mapData.toJSONString());
        }
    }


    private class buyCallback implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            com.alibaba.fastjson.JSONObject jsonObject = JsonPostParamBuilder.parseJsonFromString(msg);;
            final com.alibaba.fastjson.JSONObject aJsonObject  = jsonObject;
            hideDlg();
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(OrderConformActivity.this, null);
            if (netExceptionAlert.netExceptionProcess(msgId, msg)) {
                return;
            }
            mHandler.post(new Runnable() {
                public void run() {
                    hideDlg();
                    String type = aJsonObject.getString("errcode");
                    if (type.equals("1")) {
                        UserTrackMgr.getInstance().onEvent("tobuy","");
                        JSONObject data = aJsonObject.getJSONObject("data");
                        Intent intent = new Intent();
                        String strOrderID = data.getString("orderSn");
                        intent.putExtra("zhifubaourl", data.getString("alipayUrl"));
                        intent.putExtra("weixinurl", data.getString("wxPayUrl"));
                        intent.putExtra("orderSn", data.getString("orderSn"));
                        intent.putExtra("finalPrice", data.getString("finalPrice"));
                        intent.putExtra("successPayUrl", data.getString("successPayUrl"));
                        intent.putExtra("citOrderSn", data.getString("citOrderSn"));
                        int specialCount = data.getIntValue("SpecialCount");
                        if(specialCount > 0){
                            Intent it = new Intent();
                            it.setAction(getResources().getString(R.string.privilege_unbind_receiver));
                            sendBroadcast(it);
                        }
                        PickUpInfo info = MyApplication.GetInstance().getUserSelectAir();;
                        intent.putExtra("whereget", info.name);

                        intent.setClass(OrderConformActivity.this, SelectPayTypeActivity.class);
                        startActivity(intent);
                        ShoppingCartMgr.getInstance().clear();
                        ShoppingCartMgr.getInstance().setColumns(null);
                    }else {
                        String errMsg = aJsonObject.getString("msg").toString();
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog();
                        customAlertDialog.setMsgInfo(errMsg);
                        customAlertDialog.setAction(new CustomAlertDialog.Action() {
                            @Override public void process() {}
                            @Override public void close() {}
                        });
                        customAlertDialog.show(getFragmentManager(), "customAlertDialog");
                    }
                }
            });
        }
    }


    private void setProductTax() {
        ImageView tipTaxImageView = (ImageView) findViewById(R.id.tax_formula_desc);
        TextView taxFormulaTV = (TextView) findViewById(R.id.tax_formula);
        if ("1".equals(isTax)) {
            taxFormulaTV.setText(taxFormula);
            taxFormulaTV.setVisibility(View.VISIBLE);
        } else {
            tipTaxImageView.setVisibility(View.INVISIBLE);
            taxFormulaTV.setVisibility(View.GONE);
        }
        tipTaxImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.tax_relativeLayout);
                if (relativeLayout.getVisibility() == View.VISIBLE) {
                    relativeLayout.setVisibility(View.GONE);
                } else {
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void floatLayer() {
        linearLayout = (LinearLayout) findViewById(R.id.toolbar6);
        mRightRelativeLayout = (RelativeLayout) findViewById(R.id.fragmentLayout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.order_coform_drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mDrawerLayout.openDrawer(mRightRelativeLayout);
            }
        });
    }

    public class ticketAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public ticketAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        @Override public int getCount() {
            return lTicket.size();
        }
        @Override public Object getItem(int position) {
            return null;
        }
        @Override public long getItemId(int position) {
            return position;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            OrderConformActivity.ViewHolder holder = null;
            if (convertView == null) {
                holder = new OrderConformActivity.ViewHolder();
                convertView = mInflater.inflate(R.layout.orderitem, null);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.info = (TextView) convertView.findViewById(R.id.info);
                holder.viewImage = (ImageView) convertView.findViewById(R.id.sItemIcon);
                convertView.setTag(holder);
            } else {
                holder = (OrderConformActivity.ViewHolder) convertView.getTag();
            }
            JSONObject j = (JSONObject) lTicket.get(position);
            holder.title.setText(getResources().getString(R.string.server_settle_moeny) + getJsonObjet(j, "reduce_money", ""));
            holder.info.setText(getJsonObjet(j, "info", ""));
            String strStatus = getJsonObjet(j, KEYTAG, "");
            setHolderViewByStatus(strStatus, holder);
            return convertView;
        }
    }

    public void setHolderViewByStatus(String strStatus, OrderConformActivity.ViewHolder holder) {
        holder.info.setTextColor(android.graphics.Color.parseColor("#676767"));
        holder.title.setTextColor(android.graphics.Color.parseColor("#c18d56"));
        holder.viewImage.setImageResource(INUSE.equals(strStatus) ? R.drawable.select_1 : R.drawable.select_2);
    }


    public class cardAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public cardAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        @Override public int getCount() {
            return lCard.size();
        }
        @Override public Object getItem(int position) {
            return null;
        }
        @Override public long getItemId(int position) {
            return 0;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            OrderConformActivity.ViewHolder holder = null;
            if (convertView == null) {
                holder = new OrderConformActivity.ViewHolder();
                convertView = mInflater.inflate(R.layout.orderitem, null);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.info = (TextView) convertView.findViewById(R.id.info);
                holder.viewImage = (ImageView) convertView.findViewById(R.id.sItemIcon);
                convertView.setTag(holder);
            } else {
                holder = (OrderConformActivity.ViewHolder) convertView.getTag();
            }

            JSONObject j = (JSONObject) lCard.get(position);
            holder.title.setText(getResources().getString(R.string.server_settle_moeny) + getJsonObjet(j, "money", ""));
            holder.info.setText(getJsonObjet(j, "detail", ""));
            String strStatus = getJsonObjet(j, KEYTAG, "");
            setHolderViewByStatus(strStatus, holder);
            return convertView;
        }
    }


    public class hongbaoAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public hongbaoAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        @Override public int getCount() {
            return lHongbao.size();
        }
        @Override public Object getItem(int position) {
            return null;
        }
        @Override public long getItemId(int position) {
            return 0;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            OrderConformActivity.ViewHolder holder = null;
            if (convertView == null) {
                holder = new OrderConformActivity.ViewHolder();
                convertView = mInflater.inflate(R.layout.orderitem, null);
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.info = (TextView) convertView.findViewById(R.id.info);
                holder.viewImage = (ImageView) convertView.findViewById(R.id.sItemIcon);
                convertView.setTag(holder);
            } else {
                holder = (OrderConformActivity.ViewHolder) convertView.getTag();
            }

            JSONObject j = (JSONObject) lHongbao.get(position);
            holder.title.setText(getResources().getString(R.string.server_settle_moeny) + getJsonObjet(j, "money", ""));
            holder.info.setText(getJsonObjet(j, "title", ""));
            String strStatus = getJsonObjet(j, KEYTAG, "");
            setHolderViewByStatus(strStatus, holder);
            return convertView;
        }
    }



    public void setGoodsImages() {
        int nCount = obGoods.size();
        int nImageCount = 0;
        if (nCount >= 5) {
            nImageCount = 5;
        } else {
            nImageCount = nCount;
            ImageView v = (ImageView) findViewById(R.id.button_goods6);
            if (v != null) {
                v.setVisibility(View.GONE);
                LinearLayout mrlay = (LinearLayout) findViewById(R.id.toolbar6);
                android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
                pp.height = 250;
                mrlay.setLayoutParams(pp);
            }
        }

        try {
            int allBtn[] = new int[]{R.id.button_goods2, R.id.button_goods3, R.id.button_goods4, R.id.button_goods5, R.id.button_goods7};
            int allCover[] = new int[]{R.id.button_Cover2, R.id.button_Cover3, R.id.button_Cover4, R.id.button_Cover5, R.id.button_Cover7};

            for (int i = 0; i < 5; i++) {
                ImageView btn = (ImageView) findViewById(allBtn[i]);
                if (btn != null)
                    btn.setVisibility(View.INVISIBLE);

                TextView btnCover = (TextView) findViewById(allCover[i]);
                if (btnCover != null)
                    btnCover.setVisibility(View.INVISIBLE);
            }
            for (int i = 0; i < nImageCount; i++) {
                JSONObject info = obGoods.getJSONObject(i);
                ImageView imgView = (ImageView) findViewById(allBtn[i]);
                TextView btnCover = (TextView) findViewById(allCover[i]);
                if (imgView == null || btnCover == null)
                    continue;
                ImageAware imageAware = new ImageViewAware(imgView, false);
        		ImageLoader.getInstance().displayImage(info.getString("goods_img"), imageAware, ImageHelper.initBarcodePathOption());
                String strT = info.getString("tax_display_txt");
                btnCover.setVisibility(StringUtil.isEmpty(strT) == false ? View.VISIBLE : View.INVISIBLE);
                imgView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage());
            e.printStackTrace();
        }
    }

    public void setUserInfo() {

        String strTime = "";
        if (ClosingRefInfoMgr.getInstance().isShopTypeOutside() == false) {
            strTime = ob.getString("deliverTime");
        } else {
            Calendar c = Calendar.getInstance();
            List ar = getdeliverTime();
            if (ar.size() == 2) {
                c.setTime((Date) ar.get(0));
                strTime += c.get(Calendar.YEAR) + "-" + formatIntToString((c.get(Calendar.MONTH) + 1), 10) + "-" +
                        formatIntToString(c.get(Calendar.DAY_OF_MONTH), 10);
                strTime += "  ";
                strTime += formatIntToString(c.get(Calendar.HOUR_OF_DAY), 10) + ":" + formatIntToString(c.get(Calendar.MINUTE), 10) + "至";

                c.setTime((Date) ar.get(1));
                strTime += formatIntToString(c.get(Calendar.HOUR_OF_DAY), 10) + ":" + formatIntToString(c.get(Calendar.MINUTE), 10) + "之间";
            }
        }
        mGetGoodsTime = strTime;
        String String1 = obInfo.getString("userName");
        String String2 = obInfo.getString("idCard");
        String String3 = obInfo.getString("flightNum");
        String String4 = obInfo.getString("flightDate");
        String String5 = obInfo.getString("phone");

        TextView text = (TextView) findViewById(R.id.textview_info_1);
        StringBuilder builder = new StringBuilder("离岛人姓名：").append(String1).append("\n").
                append("手机号码：").append(String5).append("\n").
                append("离岛时间：").append(String4).append("\n").
                append("提货时间：").append(strTime);
        text.setText(builder.toString());

        PickUpInfo info = MyApplication.GetInstance().getUserSelectAir();;//(PickUpInfo) CacheUtilManager.getInstance().getDefaultCache().get(KEY_AIR_ATG);
        String strAir = info.name;
        text = (TextView) findViewById(R.id.textview_info_2);

        String salerInfo = mClosingRefInfoMgr.getSalerName() + " " + mClosingRefInfoMgr.getUserName();
        builder = new StringBuilder("离岛人身份证：").append(String2).append("\n").
                append("离岛航班：").append(String3).append("\n").
                append("离岛机场：").append(strAir).append("\n").
                append("销售员：").append(salerInfo);
        text.setText(builder.toString());
    }

    public String formatIntToString(int n, int njuge) {
        if (n < njuge) {
            return "0" + n;
        }
        return "" + n;
    }

    public JSONArray getGoodAgr() {
        JSONArray listReturn = new JSONArray();
        for (int i = 0; i < obGoods.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonobject = obGoods.getJSONObject(i);
            String s = jsonobject.getString("barcode");
            String s1 = jsonobject.getString("number");
            JSONObject map = new JSONObject();
            map.put("barcode", s);
            map.put("number", s1);
            map.put("activity", jsonobject.getString("activity"));
            listReturn.add(map);
        }
        return listReturn;
    }

    public void setMoney() {
        if (ob == null)
            return;
        String str = null;
        TextView text = (TextView) findViewById(R.id.textview_info1);

        String strMoney = getResources().getString(R.string.server_settle_moeny);
        str = getResources().getString(R.string.server_settle_goodsPrice) + "\n" + strMoney +
                formatMoney(getJsonObjet(ob, "goodsPrice", "0"));
        text.setText(str);

        text = (TextView) findViewById(R.id.textview_info3);
        str = getResources().getString(R.string.server_settle_discountActive) + "\n" + strMoney +
                formatMoney(getJsonObjet(ob, "discountActive", "0"));
        text.setText(str);

        text = (TextView) findViewById(R.id.textview_info4);
        str = getResources().getString(R.string.server_settle_1) + "\n" + strMoney +
                formatMoney(getJsonObjet(ob, "fullActive", "0"));
        text.setText(str);

        text = (TextView) findViewById(R.id.textview_info41);
        text.setText("满件折优惠" +  "\n" + strMoney +
                formatMoney(getJsonObjet(ob, "everyDiscountActive", "0")));

        text = (TextView) findViewById(R.id.textview_info42);
        text.setText("每满减优惠" +  "\n" + strMoney +
                formatMoney(getJsonObjet(ob, "everyFullActive", "0")));

        float f = getTicketMoney();
        text = (TextView) findViewById(R.id.textview_info5);
        str = getResources().getString(R.string.server_settle_moeny1) + "\n" + strMoney + formatMoney(f);
        text.setText(str);

        f = getCardMoney();
        text = (TextView) findViewById(R.id.textview_info6);
        str = getResources().getString(R.string.server_settle_card) + "\n" + strMoney + formatMoney(f);
        text.setText(str);

        f = getHongbaoMoney();
        text = (TextView) findViewById(R.id.textview_info61);
        str = getResources().getString(R.string.server_settle_hongbao) + "\n" + strMoney + formatMoney(f);
        text.setText(str);

        text = (TextView) findViewById(R.id.textview_info7);
        str = getResources().getString(R.string.server_settle_money_rate) + "\n" + strMoney +
                formatMoney(getJsonObjet(ob, "money_rate", "0"));
        text.setText(str);


        String all = getOrderPrice();
        text = (TextView) findViewById(R.id.textview_info8);
        str = getResources().getString(R.string.server_settle_finalPrice) + "\n" + strMoney + formatMoney(all);;
        text.setText(str);


        text = (TextView) findViewById(R.id.bottomTitle);
        str = getResources().getString(R.string.server_settle_finalPrice) + "  " + getResources().getString(R.string.server_settle_moeny) + all;
        text.setText(str);
        Spannable word = new SpannableString(text.getText());
        word.setSpan(new AbsoluteSizeSpan(25), 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setText(word);
    }


    public void setHeightByID(int id, int height) {
        RelativeLayout mrlay;
        mrlay = (RelativeLayout) findViewById(id);
        android.view.ViewGroup.LayoutParams pp = mrlay.getLayoutParams();
        pp.height = height;
        mrlay.setLayoutParams(pp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(mRightRelativeLayout)) {
                mDrawerLayout.closeDrawer(mRightRelativeLayout);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public String getData() {
        return getIntent().getStringExtra("data");
    }

    public JSONArray getObGoods() {
        return obGoods;
    }

    public void setObGoods(JSONArray obGoods) {
        this.obGoods = obGoods;
    }

    public final class ViewHolder {
        public TextView title;
        public TextView info;
        public ImageView viewImage;
    }

    public String formatMoney(String str) {
        int nIndex = str.indexOf(".00");
        if(nIndex != -1)
            str = str.substring(0,nIndex);
        return str;
    }

    public String formatMoney(float f) {
        String str = String.format("%.2f", ((f * 100) / 100.0f));
        str = formatMoney(str);
        return str;
    }


    public String getJsonObjet(JSONObject j, String key, String strdefault) {
        if (j == null || key == null) {
            return strdefault;
        }
        String str = j.getString(key);
        if (StringUtil.isEmpty(str))
            return strdefault;
        return str;
    }

    public String JosnGetString(com.alibaba.fastjson.JSONObject ob, String key) {
        String strReturn = ob.getString(key);
        if (strReturn == null)
            strReturn = "";
        return strReturn;
    }

    public void showDlg() {
        hideDlg();
        mDialog = ProgressDialogUtil.show(this, false);
        mDialog.show();
    }


    public void hideDlg() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
    public void showToast(String msg) {
        final String _msg = msg;
        mHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), _msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("OrderConformActivity");
    }

    protected void onPause() {
        stoptimer();
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("OrderConformActivity");
        Logger.GetInstance().Track("OrderConformActivity on onPause");
    }
}