package com.hhyg.TyClosing.ui;
import java.io.IOException;
import java.lang.ref.WeakReference;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.handler.SimpleHandler;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.dao.InitInfoDao;
import com.hhyg.TyClosing.dao.PickUpInfoDao;
import com.hhyg.TyClosing.dao.SalerInfoDao;
import com.hhyg.TyClosing.global.DbOpenHelper;
import com.hhyg.TyClosing.global.FileOperator;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.info.PickUpInfo;
import com.hhyg.TyClosing.info.SalerInfo;
import com.hhyg.TyClosing.mgr.*;
import com.hhyg.TyClosing.ui.base.BaseActivity;
import com.hhyg.TyClosing.ui.view.AutoClearEditText;
import com.hhyg.TyClosing.ui.view.AutoClearEditText.OnCommitBtnListener;
import com.hhyg.TyClosing.util.ProgressDialogUtil;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;

import static com.hhyg.TyClosing.config.Constants.IS_DEBUG_MODE;

public class SalerLoginActivity extends BaseActivity{
	private MyApplication mApp = MyApplication.GetInstance();
	private DbOpenHelper mDb = DbOpenHelper.GetInstance();
	private AutoClearEditText mSalerNameInputEdit;
	private AutoClearEditText mSalerPwdInputEdit;
	private Button mCommitBtn;
	private ProgressView mProgressView;
	private UiHandler mUiHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.salermanlogin_activity);
		findView();
		setWidgetOnClickListener();
		initDataBase();
		init();
		initView();
		setEditInputLengthListener();
		ServerLogMgr.getInstance();
		if(IS_DEBUG_MODE)
			MobclickAgent.setDebugMode(true);
		//Logger.GetInstance().Track("SalerLoginActivity on Create");
	}

	private void initDataBase() {
		mDb.execuate("CREATE TABLE IF NOT EXISTS shoppingcart(name, brand,spuname,attrinfo, stock,citamount,activename,citprice,activeprice,activecut,barcode,cnt,typeid,typename,activeid,full,fullreduce,imgurl,msprice)");
        mDb.execuate("CREATE TABLE IF NOT EXISTS deletetable(name, brand,spuname,attrinfo, stock,citamount,activename,citprice,activeprice,activecut,barcode,cnt,typeid,typename,activeid,full,fullreduce,privilegecut,privilegeprice,imgurl,msprice)");
        mDb.execuate("CREATE TABLE IF NOT EXISTS order_info(orderid,totalcast,totalcnt,committime,ordertime,orderstatus,PRIMARY KEY(orderid))");
        mDb.execuate("CREATE TABLE IF NOT EXISTS orderitem_price(totalprice,cut,totalcast,orderid,FOREIGN KEY (orderid) REFERENCES order_info(orderid))");
        mDb.execuate("CREATE TABLE IF NOT EXISTS shoppingcat_infos(brand,name,attrinfo,citprice,activeprice,barcode,cnt,orderid,FOREIGN KEY (orderid) REFERENCES order_info(orderid) )");
        mDb.execuate("CREATE TABLE IF NOT EXISTS order_img_info(path,orderid,FOREIGN KEY (orderid) REFERENCES order_info(orderid))");
        mDb.execuate("CREATE TABLE IF NOT EXISTS init_info(shopid,channleid)");
        mDb.execuate("CREATE TABLE IF NOT EXISTS pickup_info(id,name,prepareTime)");
        mDb.execuate("CREATE TABLE IF NOT EXISTS saler_info(salerid,username,salername,shopname,shoptype)");
	}
	public void disProgressView(){
		mProgressView.stopProgressing();
	}
	private void init(){
        FileOperator.createFileDirectory(mApp.BASE_LOCAL_PATH);
        FileOperator.createFileDirectory(mApp.BASE_LOCAL_PATH_PIC);
//        DeleteMgr.getInstance().Init();
	}
	
	private void initView(){
		mProgressView = new ProgressView(this);
		mUiHandler = new UiHandler(this);
		mSalerNameInputEdit.setNormalDrawable(null);
		mSalerPwdInputEdit.setNormalDrawable(null);
		setCommitBtnCantClick();
		initVersionCodeView();
		initDeviceIdView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("SalerLoginActivity");
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("salerLoginActivity");
	}

	private void initDeviceIdView() {
		TextView deviceIdView = (TextView) findViewById(R.id.device_id);
		String deviceId = MyApplication.GetInstance().getAndroidId();
		deviceIdView.setText("设备号  "+deviceId);
	}
	private void initVersionCodeView() {
		TextView versionName = (TextView) findViewById(R.id.versionname);
		versionName.setText("版本   "+ VersionMgr.getAppVersionCode(this));
	}
	
	private void findView() {
		mSalerNameInputEdit = (AutoClearEditText) findViewById(R.id.salername_input);
		mSalerPwdInputEdit = (AutoClearEditText) findViewById(R.id.salerpwd_input);
		mCommitBtn = (Button) findViewById(R.id.commmitbtn);
		if (IS_DEBUG_MODE == true){
			//mSalerNameInputEdit.setText("rygcxs");//日月广场店
			mSalerNameInputEdit.setText("hktest");//线上
			mSalerPwdInputEdit.setText("123456");
			//mSalerNameInputEdit.setText("三亚管理");//三亚店
			//mSalerPwdInputEdit.setText("123456");
		}
	}


	private void setWidgetOnClickListener(){
		mCommitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fetchLoginData();
			}
		});
	}
	private void setEditInputLengthListener(){
		mSalerPwdInputEdit.setOnCommitBtnListener(new OnCommitBtnListener(){
			@Override
			public void OnCanCommit() {
				final boolean isPwdLegal = LoginRuleCheck.pwdCheck(mSalerNameInputEdit.getText().toString());
				if(isPwdLegal){
					setCommitBtnCanClick();
				}
			}
			@Override
			public void OnCantCommit() {
				setCommitBtnCantClick();
			}});
		mSalerNameInputEdit.setOnCommitBtnListener(new OnCommitBtnListener(){
			@Override
			public void OnCanCommit() {
				final boolean isUserNameLegal = LoginRuleCheck.uesrNameCheck(mSalerPwdInputEdit.getText().toString());
				if(isUserNameLegal){
					setCommitBtnCanClick();
				}
			}
			@Override
			public void OnCantCommit() {
				setCommitBtnCantClick();
			}});
	}
	private void setCommitBtnCanClick(){
		mCommitBtn.setClickable(true);
		mCommitBtn.setBackgroundResource(R.drawable.button_corner_selector);
	}
	private void setCommitBtnCantClick(){
		mCommitBtn.setClickable(false);
		mCommitBtn.setBackgroundResource(R.drawable.cancelbtn_shape);
	}
	private void fetchLoginData(){
		mProgressView.showProgressing();
		mApp.post(Constants.getIndexUrl() + "?r=login/login", makeLoginJsonParam(), new NetCallBackHandlerException(mUiHandler,new LoginProc(mUiHandler)));
	}
	private String makeLoginJsonParam() {
		final String userName = mSalerNameInputEdit.getText().toString();
		final String pwd = mSalerPwdInputEdit.getText().toString();
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("op", "login");
		jsonParam.put("imei", mApp.getAndroidId());
		JSONObject dataParam = new JSONObject();
		dataParam.put("username", userName);
		dataParam.put("password", pwd);
		jsonParam.put("data", dataParam);
		return jsonParam.toString();
	}
	
	private void jumpToAllShopActivity() {
		openActivityWithEnding(AllShopActivity.class);
	}
	
	static class LoginRuleCheck{
		public static boolean uesrNameCheck(String ueserName){
			return ueserName.length()>0;
		}
		public static boolean pwdCheck(String pwd){
			return pwd.length()>0;
		}
	}
	
	static class ProgressView{
		Dialog dialog = null;
		Context context = null;
		public ProgressView(Context c){
			context = c;
		}
		public void showProgressing(){
			dialog = ProgressDialogUtil.show(context, false);
			dialog.show();
		}
		public void stopProgressing(){
			dialog.dismiss();
		}
	}
	
	static class LoginProc implements ProcMsgHelper{
		private ClosingRefInfoMgr closingRefInfoMgr = ClosingRefInfoMgr.getInstance();
		private PickUpInfoDao dao;
		private Handler handler;
		public LoginProc(Handler h) {
			super();
			dao = new PickUpInfoDao();
			handler = h;
		}
		@Override
		public void ProcMsg(String msgBody) throws JSONException, IOException{
            JSONObject jsonObj = JSONObject.parseObject(msgBody);
            handlerInitInfo(jsonObj);
            handlerPickInfo(jsonObj);
            handlerSalerInfo(jsonObj);
            handler.sendEmptyMessage(6);//数据解析完成回调
		}
		private void handlerInitInfo(JSONObject jsonObj) throws JSONException{
				JSONObject data = jsonObj.getJSONObject("data");
				String shopId = data.getString("shopid");
	            int channelId = data.getIntValue("channel");
	            closingRefInfoMgr.setChannelId(channelId);
	            closingRefInfoMgr.setShopId(shopId);
	            updataInitBaseData(shopId,String.valueOf(channelId));
		}
		private void updataInitBaseData(String shopId,String channleId){
			InitInfoDao dao = new InitInfoDao();
			dao.updata(shopId, channleId);
		}
		private void handlerPickInfo(JSONObject jsonObj) throws JSONException{
			JSONObject data = jsonObj.getJSONObject("data");
			closingRefInfoMgr.setLatestPayTime(data.getIntValue("maxpaytime"));
			JSONArray pickUpInfoArr = data.getJSONArray("pickupaddr");
			closingRefInfoMgr.clear();
			dao.delete();
			for (int idx = 0; idx < pickUpInfoArr.size(); idx++) {
                 JSONObject obj = (JSONObject) pickUpInfoArr.get(idx);
                 PickUpInfo info = new PickUpInfo();
                 info.name = obj.getString("addr");
                 info.id = obj.getIntValue("id");
                 info.prepareTime = obj.getIntValue("preparegoodstime");
                 closingRefInfoMgr.add(info);
                 dao.insert(info);
            }
		}
		private void handlerSalerInfo(JSONObject jsonObj) throws JSONException{
			JSONObject data = jsonObj.getJSONObject("data");
			SalerInfo info = new SalerInfo();
			info.setSalerId(data.getString("salerid"));
			info.setUserName(data.getString("username"));
			info.setSalerName(data.getString("salername"));
			info.setShopName(data.getString("shopname"));
			info.setShopType(data.getString("shoptype"));
			updataSalerInfoBaseData(info);
			closingRefInfoMgr.setSalerInfo(info);
		}
		private void updataSalerInfoBaseData(SalerInfo salerInfo){
			SalerInfoDao dao = new SalerInfoDao();
			dao.updata(salerInfo);
		}
	}
	static class UiHandler extends SimpleHandler{
		WeakReference<SalerLoginActivity> mSalerLoginActivity;
		public UiHandler(SalerLoginActivity activity){
			mSalerLoginActivity = new WeakReference<SalerLoginActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			disProgressView();
			super.handleMessage(msg);
			switch(msg.what){
				case 4:{
					Toast.makeText(MyApplication.GetInstance(), (String) msg.obj, Toast.LENGTH_SHORT).show();
					break;
				}
				case 6:{
					Toast.makeText(MyApplication.GetInstance(), "登录成功", Toast.LENGTH_SHORT).show();
					jumpToAllShopActivity();
					break;
				}
			}
		}
		private void disProgressView(){
			SalerLoginActivity loginAty = mSalerLoginActivity.get();
			if(loginAty != null){
				loginAty.disProgressView();
			}
		}
		private void jumpToAllShopActivity(){
			SalerLoginActivity loginAty = mSalerLoginActivity.get();
			if(loginAty != null){
				loginAty.jumpToAllShopActivity();
			}
		}
	}
}
