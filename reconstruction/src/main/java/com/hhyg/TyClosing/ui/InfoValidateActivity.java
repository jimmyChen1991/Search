package com.hhyg.TyClosing.ui;

import static com.hhyg.TyClosing.config.Constants.IS_DEBUG_MODE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetExceptionAlert;
import com.hhyg.TyClosing.info.ActiveColumns;
import com.hhyg.TyClosing.info.ActiveInfo.ActiveType;
import com.hhyg.TyClosing.info.PickUpInfo;
import com.hhyg.TyClosing.info.ShopCartItem;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.hhyg.TyClosing.ui.dialog.CustomAlertDialog;
import com.hhyg.TyClosing.ui.dialog.CustomConfirmDialog;
import com.hhyg.TyClosing.util.IdentityUtil;
import com.hhyg.TyClosing.util.ProgressDialogUtil;
import com.hhyg.TyClosing.util.StringUtil;
import com.hhyg.TyClosing.util.Validate;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

public class InfoValidateActivity extends Activity implements View.OnClickListener, View.OnFocusChangeListener,android.text.TextWatcher  {

    private MyApplication mApp = MyApplication.GetInstance();
    private EditText dateText, userName, idCode, flightNum, phone;
    private ShoppingCartMgr shoppingCartMgr = ShoppingCartMgr.getInstance();
    private CommitCallback commitCallback = new CommitCallback();
    private TextView idCodeTip, userNameTip, flightNumTip, dateTip;
    private boolean idChecked = false;//身份证是否正确
    private boolean userNameChecked = false;//姓名是否正确
    private boolean flightNumChecked = false;
    private boolean dateChecked = false;
    private boolean phoneChecked = false;
    private android.os.Handler handler = new android.os.Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.info_validate);
        if(shoppingCartMgr.getColumns() == null){
        	Intent it = new Intent();
        	it.setClass(this, ShopCartActivity.class);
        	startActivity(it);
        	finish();
        }
        ImageButton btn = (ImageButton) findViewById(R.id.back_to_settle_method);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.toolbar2);
        userName = (EditText) findViewById(R.id.userName);
        idCode = (EditText) findViewById(R.id.idCode);
        flightNum = (EditText) findViewById(R.id.flightNum);
        idCodeTip = (TextView) findViewById(R.id.id_tip);
        userNameTip = (TextView) findViewById(R.id.userName_tip);
        flightNumTip = (TextView) findViewById(R.id.flightNum_tip);
        dateTip = (TextView) findViewById(R.id.date_tip);
        //时间控件
        dateText = (EditText) findViewById(R.id.date);
        phone = (EditText) findViewById(R.id.phone);
        dateText.setFocusableInTouchMode(false);
        btn.setOnClickListener(this);
        dateText.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        idCode.setOnFocusChangeListener(this);
        userName.setOnFocusChangeListener(this);
        flightNum.setOnFocusChangeListener(this);
        dateText.setOnFocusChangeListener(this);
        phone.setOnFocusChangeListener(this);
        idCode.addTextChangedListener(this);

        Logger.GetInstance().Debug("InfoValidActivity on Create end");

        if(IS_DEBUG_MODE == true) {
            String idcard[] = new String[]{"52263519890117727X"," 522635198101135012"," 522635197408242598"," 522635197306216575"," 522635198504252512",
                    " 522635197301239575"," 451025198809227698"," 451025198008241094"," 451025198602264010"," 451025197801204796"};
            idCode.setText(idcard[3]);
            flightNum.setText("MF5054");
            userName.setText("李飞");
            phone.setText("15010729526");

            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd 20:10");
            String date = sDateFormat.format(new java.util.Date());
            dateText.setText(date);
        }
        Logger.GetInstance().Track("InfoValidActivity on Create");
    }
    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override public void afterTextChanged(Editable s) {
        int nLength = s.length();
        for (int i = 0; i < nLength; i++){
            char c = s.charAt(i);
            if('x'== c){
                s.replace(i,i + 1,"X");
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.idCode:
                if (!hasFocus) {
                    checkIdCode();
                } else {
                    ScrollViewScrollToBottom();
                }

                break;
            case R.id.userName:
                if (!hasFocus) {
                    //校验姓名
                    checkUserName();

                } else {
                    ScrollViewScrollToBottom();
                }
                break;
            case R.id.flightNum:
                if (!hasFocus) {
                    //校验航班号
                    checkFlightNum();
                } else {
                    ScrollViewScrollToBottom();
                }
                break;
            case R.id.date:
                if (!hasFocus) {
                    //校验航班时间
                    checkDate();
                }
                break;
            case R.id.phone:
                if (!hasFocus) {
                    checkPhone();
                }
        }
    }

    public void ScrollViewScrollToBottom() {
        handler.postAtTime(new Runnable() {
            @Override
            public void run() {
                ((ScrollView) findViewById(R.id.ScrollView)).scrollTo(0, 400);
            }
        },2000);
    }


    /**
     * 校验身份证规则
     */
    private void checkIdCode() {
        String idCodeStr = idCode.getText().toString();
        idCodeStr = idCodeStr.trim();
        boolean card = IdentityUtil.validateCard(idCodeStr);
        if (!card) {
            idCodeTip.setText(getString(R.string.id_code_err));
            idCodeTip.setVisibility(View.VISIBLE);
            idChecked = false;
        } else {
            idChecked = true;
            idCodeTip.setText(null);
            idCodeTip.setVisibility(View.GONE);
        }
    }

    /**
     * 校验姓名
     */
    private void checkUserName() {
        String userNameStr = userName.getText().toString();
        if (StringUtil.isEmpty(userNameStr) || userNameStr.length() > 30) {
            userNameTip.setText(getString(R.string.username_err));
            userNameTip.setVisibility(View.VISIBLE);
            userNameChecked = false;
        } else {
            userNameTip.setText(null);
            userNameTip.setVisibility(View.GONE);
            userNameChecked = true;
        }
    }

    private void checkFlightNum() {
        String flightNumStr = flightNum.getText().toString();
        if (isLetterDigit(flightNumStr) && flightNum.length() < 31) {
            flightNumTip.setText(null);
            flightNumTip.setVisibility(View.GONE);
            flightNumChecked = true;
        } else {
            flightNumTip.setText(getString(R.string.flightNum_err));
            flightNumTip.setVisibility(View.VISIBLE);
            flightNumChecked = false;
        }
    }

    private void checkDate() {
        String date = dateText.getText().toString();
        if (StringUtil.isEmpty(date)) {
            dateTip.setText(getString(R.string.date_err));
            dateTip.setVisibility(View.VISIBLE);
            dateChecked = false;
        } else {
            dateTip.setText(null);
            dateTip.setVisibility(View.GONE);
            dateChecked = true;
        }
    }

    private void checkPhone(){
        EditText t = (EditText) findViewById(R.id.phone);
        TextView v = (TextView)findViewById(R.id.date_tip_phone);
        v.setVisibility(View.VISIBLE);
        phoneChecked = false;
        if (t.getText().equals("")) {
            v.setText("*请填写手机号码");
            return;
        }
        if (Validate.isMobileNO(t.getText().toString()) == false) {
            v.setText("*请填写正确格式的手机号码");
            return;
        }
        phoneChecked = true;
        v.setText(null);
        v.setVisibility(View.GONE);
    }


    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {     //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {   //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.back_to_settle_method:
                // TODO Auto-generated method stub
                Logger.GetInstance().Debug("InfoValidateActivity onclick back");
                Intent intent = new Intent();
                intent.setClass(InfoValidateActivity.this, ShopCartActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.toolbar2:
                checkIdCode();
                checkUserName();
                checkFlightNum();
                checkDate();
                checkPhone();
                if (!userNameChecked || !idChecked || !dateChecked || !flightNumChecked || !phoneChecked) {
                    Logger.GetInstance().Debug("InfoValidateActivity Check does not pass ");
                    return;
                }
                postdata();
                break;
            case R.id.date:
                setTheme(R.style.DateTheme);
                final View viewDialog = getLayoutInflater().inflate(
                        R.layout.activity_datatimepicker, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(InfoValidateActivity.this);
                final DatePicker datePicker = (DatePicker) viewDialog.findViewById(R.id.dpPicker);
                final TimePicker timePicker = (TimePicker) viewDialog.findViewById(R.id.tpPicker);
                timePicker.setIs24HourView(true);

                builder.setPositiveButton(R.string.queding, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        int hour = timePicker.getCurrentHour();
                        String monthStr = "";
                        String dayStr = "";
                        String hourStr = "";
                        if (hour < 10) {
                            hourStr = "0" + hour;
                        } else {
                            hourStr = hour + "";
                        }

                        if (month < 9) {
                            monthStr = "0" + (month + 1);
                        } else {
                            monthStr = Integer.toString(month + 1);
                        }

                        if (day < 10) {
                            dayStr = "0" + (day);
                        } else {
                            dayStr = Integer.toString(day);
                        }

                        int minute = timePicker.getCurrentMinute();
                        String minuteStr = "";
                        if (minute < 10) {
                            minuteStr = "0" + minute;
                        } else {
                            minuteStr = minute + "";
                        }
                        dateText.setText(datePicker.getYear() + "-" + monthStr + "-" + dayStr +
                                " " + hourStr + ":" + minuteStr);
                        dateChecked = true;
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setView(viewDialog);
                alertDialog.show();
                break;
        }

    }

    private class CommitCallback implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            NetExceptionAlert netExceptionAlert = new NetExceptionAlert(InfoValidateActivity.this,dialog);
            boolean netErrorr = netExceptionAlert.netExceptionProcess(msgId,msg);
            if(netErrorr){
                return ;
            }
            int errcode = 0;
            JSONObject jsonObj = JSONObject.parseObject(msg);
            try {
                errcode = jsonObj.getIntValue("errcode");
            } catch (JSONException e) {
                Logger.GetInstance().Exception(e.getMessage() + " JSon is :" + jsonObj.toJSONString());
                e.printStackTrace();
            } catch (Exception e) {
                Logger.GetInstance().Exception(e.getMessage());
                e.printStackTrace();
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            try {
                if (errcode == 1) {
                    JSONObject j = new JSONObject();
                    try {
                        j.put("idCard", idCode.getText().toString().trim());
                        j.put("flightDate", dateText.getText());
                        j.put("userName", userName.getText().toString().replace("\n", ""));
                        j.put("flightNum", flightNum.getText().toString().toUpperCase());
                        EditText t = (EditText) findViewById(R.id.phone);
                        j.put("phone", t.getText().toString());
                    }
                    catch (Exception e){
                        Logger.GetInstance().Exception(e.getMessage());
                    }
                    
                    JSONArray json = new JSONArray();
                    if(shoppingCartMgr.getColumns() == null){
                    	Intent it = new Intent();
                    	it.setClass(InfoValidateActivity.this, ShopCartActivity.class);
                    	startActivity(it);
                    	finish();
                    	return;
                    }
                    for(ActiveColumns column : shoppingCartMgr.getColumns()){
                    	if(column.getaInfo().getType() == ActiveType.NoStock){
                    		continue;
                    	}
                    	if(column.getCartItems() != null){
                    		for(ShopCartItem item : column.getCartItems()){
                				JSONObject item_JOBJ = new JSONObject();
                				item_JOBJ.put("barcode", item.getBarCode());
                				item_JOBJ.put("number", item.getCnt());
                				item_JOBJ.put("goods_name", item.getName());
                				item_JOBJ.put("goods_attr", item.getAttrInfo());
                				item_JOBJ.put("goods_img", item.getImgUrl());
                				item_JOBJ.put("brand_name", item.getBrand());
                				item_JOBJ.put("goods_price", item.getCitPrice());
                				if(column.getaInfo().getActiveId() != null){
                					item_JOBJ.put("activity", column.getaInfo().getActiveId());
                				}else{
                					item_JOBJ.put("activity", "");
                				}
                				JSONArray a = jsonObj.getJSONObject("data").getJSONArray("goodsSku");
                                   for (int i = 0; i < a.size(); i++){
                                       JSONObject ob = a.getJSONObject(i);
                                       if(ob.getString("barcode").equals(item.getBarCode())){
                                       	item_JOBJ.put("tax_display_txt", ob.getString("tax_display_txt"));
                                       }
                                   }
                                   json.add(item_JOBJ);
                			}
                    	}
                    }

                    JSONObject jALl = new JSONObject();
                    jALl.put("userInfo",j);
                    jALl.put("goodsSku",json);

                    jALl.put("discountActive",jsonObj.getJSONObject("data").getString("discountActive"));
                    jALl.put("everyDiscountActive", jsonObj.getJSONObject("data").getString("everyDiscountActive"));
                    jALl.put("everyFullActive", jsonObj.getJSONObject("data").getString("everyFullActive"));
                    jALl.put("fullActive",jsonObj.getJSONObject("data").getString("fullActive"));
                    jALl.put("money_rate",jsonObj.getJSONObject("data").getString("money_rate"));
                    jALl.put("goodsPrice",jsonObj.getJSONObject("data").getString("goodsPrice"));
                    jALl.put("finalPrice",jsonObj.getJSONObject("data").getString("finalPrice"));
                    jALl.put("deliverTime",jsonObj.getJSONObject("data").getString("deliverTime"));
                    jALl.put("available",jsonObj.getJSONObject("data").getString("available"));
                    jALl.put("token",jsonObj.getJSONObject("data").getString("token"));
                    jALl.put("SpecialCount",jsonObj.getJSONObject("data").getString("SpecialCount"));
                    EditText t = (EditText) findViewById(R.id.phone);
                    jALl.put("phone", t.getText().toString());

                    Intent intent = new Intent();
                    intent.putExtra("data",jALl.toString());
                    Logger.GetInstance().Debug(jsonObj.get("data").toString());
                    intent.setClass(InfoValidateActivity.this, OrderConformActivity.class);
                    startActivity(intent);

                } else if (errcode == 2) {
                    CustomAlertDialog customAlertDialog = new CustomAlertDialog();
                    customAlertDialog.setMsgInfo(jsonObj.get("msg").toString());
                    customAlertDialog.setAction(new CustomAlertDialog.Action() {
                        @Override
                        public void process() {

                        }

                        @Override
                        public void close() {

                        }
                    });
                    customAlertDialog.show(getFragmentManager(), "customAlertDialog");
                } else if (errcode == 3) {
                    CustomConfirmDialog customConfirmDialog = new CustomConfirmDialog();
                    customConfirmDialog.setMsgInfo(jsonObj.get("msg").toString());
                    customConfirmDialog.setConfirmBtnText(getString(R.string.goback_to_shopcart));
                    customConfirmDialog.setCancelBtnText(getString(R.string.queding));
                    customConfirmDialog.setAction(new CustomConfirmDialog.Action() {
                        @Override
                        public void process() {
                            Intent intent = new Intent();
                            intent.setClass(InfoValidateActivity.this, ShopCartActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void cancel() {

                        }

                        @Override
                        public void close() {

                        }
                    });
                    customConfirmDialog.show(getFragmentManager(), "customConfirmDialog");
                }
            } catch (JSONException e) {
                Logger.GetInstance().Exception(e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                Logger.GetInstance().Exception(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    Dialog dialog = null;
    private void postdata() {
        dialog = ProgressDialogUtil.show(this, false);
        dialog.show();
        JSONObject param = new JSONObject();
        JSONObject param1 = new JSONObject();
        JSONArray json = new JSONArray();
        if(shoppingCartMgr.getColumns() == null){
        	Intent it = new Intent();
        	it.setClass(this, ShopCartActivity.class);
        	startActivity(it);
        	finish();
        	return;
        }
        for(ActiveColumns column : shoppingCartMgr.getColumns()){
        	if(column.getaInfo().getType() == ActiveType.NoStock){
        		continue;
        	}
        	if(column.getCartItems() != null){
        		for(ShopCartItem item : column.getCartItems()){
    				JSONObject item_JOBJ = new JSONObject();
    				item_JOBJ.put("barcode", item.getBarCode());
    				item_JOBJ.put("number", item.getCnt());
    				if(column.getaInfo().getActiveId() != null){
    					item_JOBJ.put("activity", column.getaInfo().getActiveId());
    				}else{
    					item_JOBJ.put("activity", "");
    				}
    				json.add(item_JOBJ);
    			}
        	}
        }
		param.put("idCard", idCode.getText().toString().trim());
		param.put("flightDate", dateText.getText());
		param.put("userName", userName.getText().toString().replace("\n", ""));
		param.put("flightNum", flightNum.getText().toString());
		EditText t = (EditText) findViewById(R.id.phone);
		param.put("phone", t.getText().toString());

		PickUpInfo info = mApp.getUserSelectAir();;//(PickUpInfo) CacheUtilManager.getInstance().getDefaultCache().get(KEY_AIR_ATG);
        mApp.getUserSelectAir();

        param.put("deliverPlace", info.id);

		param.put("goodsSku", json);
		param1.put("data", param);
		param1.put("op", "ownpaycheck");
        mApp.post(Constants.getServiceUrl(), JsonPostParamBuilder.makeParam(param1), commitCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("InfoValidateActivity");
        Logger.GetInstance().Track("InfoValidateActivity on onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.GetInstance().Track("InfoValidateActivity on onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("InfoValidateActivity");
        Logger.GetInstance().Track("InfoValidateActivity on onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.GetInstance().Track("InfoValidateActivity on onDestroy");
    }
}
