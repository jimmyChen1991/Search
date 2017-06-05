package com.hhyg.TyClosing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.PrivilegeInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.presenter.BindPrivilegePresenter;
import com.hhyg.TyClosing.presenter.CancelBindPrivilegePresenter;
import com.hhyg.TyClosing.presenter.IsprivilegeUserPresenter;
import com.hhyg.TyClosing.presenter.MyPrivilegeInfoPresenter;
import com.hhyg.TyClosing.ui.dialog.QuotaQueryDialog;
import com.hhyg.TyClosing.ui.view.AutoClearEditText;
import com.hhyg.TyClosing.util.ProgressDialogUtil;
import com.hhyg.TyClosing.util.StringUtil;
import com.hhyg.TyClosing.util.ToastUtil;
import com.hhyg.TyClosing.util.Validate;
import com.hhyg.TyClosing.view.BindPrivilegeView;
import com.hhyg.TyClosing.view.CancelBindPrivilegeView;
import com.hhyg.TyClosing.view.IsPrivilegeUserView;
import com.hhyg.TyClosing.view.MyPrivilegeView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import static com.hhyg.TyClosing.config.Constants.IS_DEBUG_MODE;

/**
 * Created by mjf on 2016/11/22.
 */
public class SalerMainPageActivity extends Activity implements android.text.TextWatcher,BindPrivilegeView,IsPrivilegeUserView,MyPrivilegeView,CancelBindPrivilegeView{
    private EditText mEdit;
    private Object lock1 = new Object();
    private MyApplication mApp = MyApplication.GetInstance();
    private SearchCallBack mCallBack = new SearchCallBack();
    private ViewGroup mBindWrap;
    private ViewGroup mPrivilegeInfoWrap;
    private AutoClearEditText mEditText;
    private Button mBindprivilegeBtn;
    private BindPrivilegePresenter mBindPresenter;
    private IsprivilegeUserPresenter mIsprivilegeUserPresenter;
    private MyPrivilegeInfoPresenter mMyPrivilegePresenter;
    private CancelBindPrivilegePresenter mCancelBindPrivilegePresenter;
    private TextView mPrivilegeTitle_Tv;
    private TextView mTime_Tv;
    private TextView mDiscount_desc_Tv;
    private TextView mDetail_desc_Tv;
    private Button mCancelBindBtn;
    private Button mMoreGoods;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salermainpage);
        findView();
        mIsprivilegeUserPresenter = new IsprivilegeUserPresenter();
        mIsprivilegeUserPresenter.attach(this);
        mBindPresenter = new BindPrivilegePresenter();
        mBindPresenter.attach(this);
        mMyPrivilegePresenter = new MyPrivilegeInfoPresenter();
        mMyPrivilegePresenter.attach(this);
        mCancelBindPrivilegePresenter = new CancelBindPrivilegePresenter();
        mCancelBindPrivilegePresenter.attach(this);
        Logger.GetInstance().Track("SalerMainPageActivity on Create");
    }

    private void findView(){
        mEdit = (EditText)findViewById(R.id.card);
        mEdit.addTextChangedListener(this);
        if(IS_DEBUG_MODE)
            mEdit.setText("532628198206219261");
        Button scanBt = (Button) findViewById(R.id.serch);
        scanBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        ImageButton btn = (ImageButton) findViewById(R.id.button_scan);
        btn.setOnClickListener(new  View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SalerMainPageActivity.this, AllShopActivity.class);
                startActivity(intent);
                finish();
            }
        });

        RelativeLayout mrlay = (RelativeLayout) findViewById(R.id.condition);
        mrlay.setOnClickListener(new  View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SalerMainPageActivity.this, BuyConditionActivity.class);
                startActivity(intent);
            }
        });
        mBindprivilegeBtn = (Button) findViewById(R.id.bindprivelege_btn);
        mBindprivilegeBtn.setOnClickListener(new  View.OnClickListener() {
            @Override public void onClick(View v) {
                final String code = mEditText.getText().toString();
                mBindPresenter.bindPrivilegeCode(code);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        mEditText = (AutoClearEditText) findViewById(R.id.privilege_code_edit);
        mEditText.setOnCommitBtnListener(new AutoClearEditText.OnCommitBtnListener() {
            @Override
            public void OnCanCommit() {
                mBindprivilegeBtn.setBackgroundResource(R.drawable.button_corner_selector);
                mBindprivilegeBtn.setClickable(true);
            }

            @Override
            public void OnCantCommit() {
                mBindprivilegeBtn.setBackgroundResource(R.drawable.cancelbtn_shape);
                mBindprivilegeBtn.setClickable(false);
            }
        });
        mEditText.setNormalDrawable(null);
        mBindWrap = (ViewGroup) findViewById(R.id.bindcode_wrap);
        mPrivilegeInfoWrap = (ViewGroup) findViewById(R.id.privilege_info_wrap);
        mPrivilegeTitle_Tv = (TextView) findViewById(R.id.privilege_title);
        mDiscount_desc_Tv = (TextView) findViewById(R.id.discount_des);
        mTime_Tv = (TextView) findViewById(R.id.time_tv);
        mDetail_desc_Tv = (TextView) findViewById(R.id.detail_desc);
        mCancelBindBtn = (Button) findViewById(R.id.cancel_bind_btn);
        mMoreGoods = (Button) findViewById(R.id.privilege_goods);
        mMoreGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivilegeInfo info = (PrivilegeInfo) v.getTag();
                if(info == null){
                    return;
                }
                ArrayList<String> activities = info.getActivityCodes();
                if(activities == null){
                    ToastUtil.getShortToastByString(SalerMainPageActivity.this,"该特权码无活动");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for(String activity : activities){
                    sb.append(activity);
                    sb.append(",");
                }
                Intent it = new Intent();
                it.setClass(SalerMainPageActivity.this,ActiveSellActivity.class);
                it.putExtra("activeId",sb.toString());
                it.putExtra("isPrivilege",true);
                it.putExtra("privilegeName",info.getTitle());
                startActivity(it);
            }
        });
        mCancelBindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setClickable(false);
                v.setBackgroundResource(R.drawable.cancelbtn_shape);
                mCancelBindPrivilegePresenter.cancelBindPrivilege((String) v.getTag());
            }
        });

        Button btn1 = (Button) findViewById(R.id.logout);
        btn1.setOnClickListener(new  View.OnClickListener() {
            @Override public void onClick(View v) {
                unregistSaler();
            }
        });

        TextView v = (TextView)findViewById(R.id.headertitle);
        v.setText(ClosingRefInfoMgr.getInstance().getSalerName() + " " + ClosingRefInfoMgr.getInstance().getUserName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsprivilegeUserPresenter.detach();
        mBindPresenter.detach();
        mMyPrivilegePresenter.detach();
        mCancelBindPrivilegePresenter.detach();
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


    private void unregistSaler() {
        final Dialog dialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.allshop_salerlogout_dialog, null);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setLayout(880, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        TextView exitContent=(TextView)layout.findViewById(R.id.warnning_content);
        exitContent.setText("确认退出账号  "+ ClosingRefInfoMgr.getInstance().getSalerInfo().getUserName()+"  " +ClosingRefInfoMgr.getInstance().getSalerInfo().getSalerName()+"?");
        Button exitBtn = (Button) layout.findViewById(R.id.summit_btn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setClass(SalerMainPageActivity.this, SalerLoginActivity.class);
                startActivity(intent);
            }
        });
        Button cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void search(){
        String strUserInput = mEdit.getText().toString();
        if(StringUtil.isEmpty(strUserInput)){
            Toast.makeText(getApplicationContext(), "请填写要查询的身份证号", Toast.LENGTH_LONG).show();
            return;
        }
        if(Validate.checkIdCode(strUserInput) == false){
            Toast.makeText(getApplicationContext(), "请输入正确格式的身份证号", Toast.LENGTH_LONG).show();
            return;
        }

        ProgressDialogUtil.show(this);
        com.alibaba.fastjson.JSONObject cancelObj = new com.alibaba.fastjson.JSONObject();
        cancelObj.put("op", "getcheckcard");
        cancelObj.put("imei", mApp.getAndroidId());
        cancelObj.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());

        com.alibaba.fastjson.JSONObject Obj1 = new com.alibaba.fastjson.JSONObject();
        Obj1.put("idCard", strUserInput);
        cancelObj.put("data", Obj1);
        try {

            mApp.post(Constants.getIndexUrl() + "?r=usercard/checkcard", JsonPostParamBuilder.makeParam(cancelObj), mCallBack);
        } catch (Exception e){
            Logger.GetInstance().Exception(e.getMessage() + " send data is :" + cancelObj.toJSONString());
            e.printStackTrace();
        }
    }

    @Override
    public void isPrivilegeUser() {
        mMyPrivilegePresenter.fetchMyPrivilegeInfo();
    }

    @Override
    public void isNotPrivilegeUser() {
        mBindWrap.setVisibility(View.VISIBLE);
    }

    @Override
    public void isPrivilegeExceptionView() {

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
    public void bindSuccess() {
        ToastUtil.getShortToastByString(this,getResources().getString(R.string.bind_success));
        mBindWrap.setVisibility(View.GONE);
        mMyPrivilegePresenter.fetchMyPrivilegeInfo();
        Intent it = new Intent();
        it.setAction(getResources().getString(R.string.privilege_unbind_receiver));
        sendBroadcast(it);
    }

    @Override
    public void bindFailed(String reason) {
        ToastUtil.getShortToastByString(SalerMainPageActivity.this,reason);
    }

    @Override
    public void showMyPrivilegeInfo(PrivilegeInfo info) {
        mPrivilegeInfoWrap.setVisibility(View.VISIBLE);
        mPrivilegeTitle_Tv.setText(info.getTitle());
        mDiscount_desc_Tv.setText(info.getDiscountDesc());
        mDetail_desc_Tv.setText(info.getDetailDesc());
        StringBuilder sb = new StringBuilder();
        sb.append(info.getStartTime());
        sb.append("----");
        sb.append(info.getEndTime());
        sb.append("有效");
        mTime_Tv.setText(sb.toString());
        mCancelBindBtn.setTag(info.getCode());
        mMoreGoods.setTag(info);
    }

    @Override
    public void cancelBindSuccess() {
        mCancelBindBtn.setBackgroundResource(R.drawable.button_corner_selector);
        mCancelBindBtn.setClickable(true);
        mPrivilegeInfoWrap.setVisibility(View.GONE);
        mBindWrap.setVisibility(View.VISIBLE);
        Intent it = new Intent();
        it.setAction(getResources().getString(R.string.privilege_unbind_receiver));
        sendBroadcast(it);
    }

    @Override
    public void cancelBindFailed(String reason) {
        mCancelBindBtn.setBackgroundResource(R.drawable.button_corner_selector);
        mCancelBindBtn.setClickable(true);
        if(null != reason){
            ToastUtil.getShortToastByString(SalerMainPageActivity.this,reason);
        }
    }

    private class SearchCallBack implements INetWorkCallBack {
        @Override public void PostProcess(int msgId, String msg) {
            synchronized (lock1) {
                if (msgId == mApp.MSG_TYPE_VALUE) {
					com.alibaba.fastjson.JSONObject jsonObject = null;
					jsonObject = com.alibaba.fastjson.JSONObject.parseObject(msg);
					Message message = new Message();
					String errcode = jsonObject.getString("errcode");
					if ("1".equals(errcode)) {
						message.obj = jsonObject.getJSONObject("data");
						message.what = 0;
					} else {
						message.obj = jsonObject.getJSONObject("msg");
						message.what = 2;
					}
					mCuiHandler.sendMessage(message);
                } else if (msgId == mApp.MSG_TYPE_ERROR) {
                	Message message = Message.obtain();
    				message.obj = msg;
    				message.what = 1;
    				mCuiHandler.sendMessage(message);
                }
            }
        }
    }

    private Handler mCuiHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            ProgressDialogUtil.hide();
            switch (msg.what) {
                case 0:
                    QuotaQueryDialog customAlertDialog = new QuotaQueryDialog();
                    customAlertDialog.setMsgInfo((com.alibaba.fastjson.JSONObject)msg.obj);
                    customAlertDialog.show(getFragmentManager(), "QuotaQueryDialog");
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "后台数据格式异常", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };


    protected void onResume() {
        super.onResume();
        mIsprivilegeUserPresenter.isPrivilegeUser();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("SalerMainPageActivity on onPause");
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd("SalerMainPageActivity");
        Logger.GetInstance().Track("SalerMainPageActivity on onPause");
    }
}


