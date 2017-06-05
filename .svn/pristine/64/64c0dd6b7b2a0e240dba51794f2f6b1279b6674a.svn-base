package com.hhyg.TyClosing.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetExceptionAlert;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;

import java.util.*;

import android.view.LayoutInflater;
import android.content.Context;
import android.widget.ListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by mjf on 16/8/17.
 */
public class SelectPayTypeActivity  extends Activity {
    public  String title[]= null;// new String[]{getResources().getString(R.string.pay_type_zhifubao),getResources().getString(R.string.pay_type_weixin)};
    public  String info[] = null;//new String[]{ getResources().getString(R.string.pay_type_use_zhifubao),getResources().getString(R.string.pay_type_use_weixin)};
    public static String image[] = new String[]{ "pay_zhifubao.png","pay_weixin.png"};
    public Timer timer;
    private MyApplication mApp = MyApplication.GetInstance();
    private Handler mHandlerData = new Handler();
    public  String mCantPayPrompt = null;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectpaytype);

        ListView listView = (ListView) findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(mCantPayPrompt != null){
                    if(mCantPayPrompt.equals(""))
                        mCantPayPrompt = "订单已超时,不能支付";
                    Toast toast = Toast.makeText(getApplicationContext(), mCantPayPrompt, Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Intent intent= getIntent();
                String String1=intent.getStringExtra("zhifubaourl");
                String String2=intent.getStringExtra("weixinurl");
                String String3=intent.getStringExtra("orderSn");
                String String4=intent.getStringExtra("finalPrice");
                String String5=intent.getStringExtra("successPayUrl");
                String String6=intent.getStringExtra("whereget");
                String string7=intent.getStringExtra("citOrderSn");
                if (arg2 == 0) {
                    intent.setClass(SelectPayTypeActivity.this, PayZhiFubaoActivity.class);
                    intent.putExtra("zhifubaourl",String1);
                    intent.putExtra("orderSn", String3);
                    intent.putExtra("finalPrice", String4);
                    intent.putExtra("successPayUrl", String5);
                } else {
                    intent.setClass(SelectPayTypeActivity.this, PayWeiXinActivity.class);
                    intent.putExtra("weixinurl",String2);
                    intent.putExtra("orderSn", String3);
                    intent.putExtra("finalPrice", String4);
                }
                intent.putExtra("citOrderSn", string7);
                intent.putExtra("whereget", String6);
                startActivity(intent);
                stopCheckOrder();
            }
        });
        ImageButton scanBt = (ImageButton) findViewById(R.id.button_scan1);
        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        Intent intent=getIntent();
        String String3=intent.getStringExtra("orderSn");
        String String4=intent.getStringExtra("finalPrice");
        TextView t = (TextView)findViewById(R.id.textview_title_success);
        if(t != null){
            String s = getResources().getString(R.string.select_pay_type_order_number) +  "  " + String3;
            t.setText(s);
        }

        t = (TextView)findViewById(R.id.order_pay_money);
        if(t != null){
            String s = getResources().getString(R.string.pay_result_mpney) +  "  " + getResources().getString(R.string.server_settle_moeny) + String4;
            t.setText(s);
        }

        String strWhereToGet = getIntent().getStringExtra("whereget");
        t = (TextView) findViewById(R.id.hejiTitle);
        String str1 = strWhereToGet.contains("三亚") ? "30" : "45";
        String strAll = "支付成功后,最快" + str1 + "分钟后提货,请确认您有足够的时间等待提货";
        t.setText(strAll);

        checkTime();

        Logger.GetInstance().Track("SelectPayTypeActivity on Create");
    }
    protected void onStart() {
        checkTime();
        super.onStart();
    }

    public void checkTime() {
        if(timer != null)
            return;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkOrderStatus();
            }
        }, 1000, 60*1000);
    }

    public void onStop() {
        stopCheckOrder();
        super.onStop();
    }

    //根据时间戳更新订单状态
    public void checkOrderStatus() {
        com.alibaba.fastjson.JSONObject j = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject j1 = new com.alibaba.fastjson.JSONObject();
        String String3 = getIntent().getStringExtra("orderSn");
        j1.put("orderSn", String3);
        j1.put("orderType", "2");
        j.put("data", j1);
        j.put("op","getremainingtime");

        try {
            mApp.post(Constants.getServiceUrl(), JsonPostParamBuilder.makeParam(j), new INetWorkCallBack(){
                public void PostProcess(int msgId, String msg){
                    final int error = msgId;
                    final String message = msg;
                    mHandlerData.post(new Runnable() {
                        public void run() {
                            processCheckStatuys(error,message);
                        }
                    });
                }
            });
        }
        catch (Exception e){
            Logger.GetInstance().Exception(e.getMessage()+ " send data is :" + j.toJSONString());
        }
    }

    public void processCheckStatuys(int msgId, String msg){
        NetExceptionAlert netExceptionAlert = new NetExceptionAlert(SelectPayTypeActivity.this,null);
        boolean netErrorr = netExceptionAlert.netExceptionProcess(msgId,msg);
        if(netErrorr){
            return ;
        }
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
        String str = jsonObject.getString("errcode");
        if("1".equals(str)){
            com.alibaba.fastjson.JSONObject timeInfo = jsonObject.getJSONObject("data");
            TextView textview = (TextView) findViewById(R.id.order_info);
            if(timeInfo != null) {
                String strCanPay = timeInfo.getString("pay_code");
                String strOrderSn = timeInfo.getString("orderSn");
                if("0".equals(strCanPay)){//可以支付
                    String strRemainingTime = timeInfo.getString("remainingTime");
                    String[] sourceStrArray = strRemainingTime.split(":");
                    String strHour = null;
                    String strMinute = "0";
                    if(sourceStrArray.length == 2){
                        strHour = sourceStrArray[0];
                        strMinute = sourceStrArray[1];
                    }
                    String text = null;
                    if(strHour == null || "0".equals(strHour))
                        text = "请于" + strMinute + "分钟";
                    else
                        text = "请于" + strHour + "小时" + strMinute + "分钟";
                    textview.setText(text + "内完成支付,否则订单将自动取消");

                    if("0".equals(strMinute) && "0".equals(strHour)){
                        mCantPayPrompt = "";
                        stopCheckOrder();
                        gotoFailActivity(strOrderSn);
                    }
                }
                else {
                    mCantPayPrompt = "";
                    stopCheckOrder();
                    gotoFailActivity(strOrderSn);
                }
            }
        }
        else{
            String strMsg = jsonObject.getString("msg");
            if(strMsg != null) {
                Toast toast = Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG);
                toast.show();
                mCantPayPrompt = strMsg;
            }
            mCantPayPrompt = "";
            stopCheckOrder();
        }
    }


    public void gotoFailActivity(String str){
        Intent intent = new Intent();
        intent.putExtra("orderSn",str);
        intent.setClass(SelectPayTypeActivity.this, PayFailActivity.class);
        startActivity(intent);
    }

    public void stopCheckOrder() {
        if (timer == null)
            return;
        timer.cancel();
        timer.purge();
        timer = null;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return 2;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.vlist, null);
                holder.title = (TextView)convertView.findViewById(R.id.title);
                holder.info = (TextView)convertView.findViewById(R.id.info);
                holder.viewBtn = (ImageView)convertView.findViewById(R.id.sItemIcon);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }

            String str = position == 0 ? getResources().getString(R.string.pay_type_zhifubao) : getResources().getString(R.string.pay_type_weixin);
            holder.title.setText(str);
            str = position == 0 ? getResources().getString(R.string.pay_type_use_zhifubao) : getResources().getString(R.string.pay_type_use_weixin);
            holder.info.setText(str);
            holder.viewBtn.setImageResource(position == 0 ? R.drawable.pay_zhifubao : R.drawable.pay_weixin);
            return convertView;
        }
    }
    public void onBackPressed() {
        super.onBackPressed();
        back();
    }

    public void back(){
        Intent intent = new Intent();
        intent.setClass(SelectPayTypeActivity.this, HistoryOrderActivity.class);
        startActivity(intent);
    }

    public final class ViewHolder {
        public TextView title;
        public TextView info;
        public ImageView viewBtn;
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("SelectPayTypeActivity");
        Logger.GetInstance().Track("SelectPayTypeActivity on onResume");
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("SelectPayTypeActivity");
        Logger.GetInstance().Track("SelectPayTypeActivity on onPause");
    }
}
