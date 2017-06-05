package com.hhyg.TyClosing.ui;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.hhyg.TyClosing.ui.base.BaseActivity;
import com.hhyg.TyClosing.ui.view.AutoClearEditText;
import com.hhyg.TyClosing.ui.view.AutoClearEditText.OnCommitBtnListener;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class CustomerCodeCaptureActivity extends BaseActivity implements Callback,CaptureActivityHandler.DecodeCallBack{
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private MyApplication mApp = MyApplication.GetInstance();
	private UIHandler mUiHandler = new UIHandler();
	private ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();
	private ClosingRefInfoMgr mClosingRefInfoMgr = ClosingRefInfoMgr.getInstance();
	private boolean mQuit =false;
	private String mCustomercode;
	private final Object lock1 = new Object();
	private AutoClearEditText mCustomerInput;
	private Button mCommitBtn;
	private ImageButton mBackBtn;
	private boolean canCommit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customercode_capture_activity);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		findView();
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		canCommit = true;
		Logger.GetInstance().Track("CustomerCodeCaptureActivity on Create");
	}
	private void findView() {
		mCustomerInput = (AutoClearEditText) findViewById(R.id.customercode_input);
		mCommitBtn = (Button) findViewById(R.id.commitbtn);
		mBackBtn = (ImageButton) findViewById(R.id.button_back);
		setCommitBtnOnClickListener();
		mCustomerInput.setNormalDrawable(null);
	}
	private void setCommitBtnOnClickListener(){
		mCustomerInput.setOnCommitBtnListener(new OnCommitBtnListener(){
			@Override
			public void OnCanCommit() {
				setCommitBtnCanClick();
			}
			@Override
			public void OnCantCommit() {
				setCommitBtnCantClick();
			}});
		mCommitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!canCommit){
					return;
				}
				final String editcode = mCustomerInput.getText().toString();
				if(checkStringBycustomerCodeRule(editcode)){
					mCustomercode = editcode;
					commitOrder2server();
				}else{
					Toast.makeText(CustomerCodeCaptureActivity.this, "无效的顾客码", Toast.LENGTH_SHORT).show();
				}
			}
		});
		mBackBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToShoppingCartActivity();
			}
		});
	}
	
	private void jumpToShoppingCartActivity() {
		openActivityWithEnding(ShopCartActivity.class);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		init();
		MobclickAgent.onResume(this);
		MobclickAgent.onPageStart("CustomerCodeCaptureActivity");
		Logger.GetInstance().Track("CustomerCodeCaptureActivity on onResume");
	}
	
	private void setCommitBtnCanClick(){
		mCommitBtn.setClickable(true);
		mCommitBtn.setBackgroundResource(R.drawable.button_corner_selector);
	}
	
	private void setCommitBtnCantClick(){
		mCommitBtn.setClickable(false);
		mCommitBtn.setBackgroundResource(R.drawable.cancelbtn_shape);
	}
	
	private boolean checkStringBycustomerCodeRule(String customercode){
		return customercode.length()>=16;
	}
	
	private void init() {
		mQuit = false;
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mQuit = true;
			jumpToShoppingCartActivity();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
		MobclickAgent.onPause(this);
		MobclickAgent.onPageEnd("CustomerCodeCaptureActivity");
		Logger.GetInstance().Track("CustomerCodeCaptureActivity on onPause");
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * 解码完毕的回调
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		if(mQuit) {
			return;
		}
		synchronized (lock1) {
			inactivityTimer.onActivity();
			playBeepSoundAndVibrate();
			mCustomercode = result.getText();
			if (mCustomercode.equals("")||mCustomercode.length()<16){
				Toast.makeText(CustomerCodeCaptureActivity.this, "无效的顾客码", Toast.LENGTH_SHORT).show();
				mUiHandler.sendEmptyMessage(3);
			}else {
				viewfinderView.setVisibility(View.GONE);
				commitOrder2server();
			}
		}
	}
	
	private void commitOrder2server(){
		canCommit = false;
		mApp.post(Constants.getServiceUrl(), makeCommitParam(), new CommitOrderCallBack());
	}
	private String makeCommitParam(){
    	JSONObject submitorObj = new JSONObject();
        submitorObj.put("op", "submitorder");
        JSONArray data = new JSONArray();
        ArrayList<ShoppingCartInfo> mInfoList = mShoppingCartMgr.getAll();
        for (int spuIdx = 0; spuIdx < mInfoList.size(); spuIdx++) {
            ShoppingCartInfo info = mInfoList.get(spuIdx);
            JSONObject obj = new JSONObject();
            obj.put("barcode", info.barCode);
            obj.put("count", info.cnt);
            data.add(obj);
        }
        JSONObject submitorObj1 = new JSONObject();
        submitorObj1.put("salerid",mClosingRefInfoMgr.getSalerInfo().getSalerId());//// TODO: 2016/10/12  
        submitorObj1.put("usercode",mCustomercode);
        submitorObj1.put("goods_sku",data);
        submitorObj.put("data", submitorObj1);
        submitorObj.put("device_type", "android");
        submitorObj.put("imei", mApp.getAndroidId());
        submitorObj.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
        submitorObj.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
        return submitorObj.toString();
	}
	private class CommitOrderCallBack implements INetWorkCallBack  {
		@Override
		public void PostProcess(int msgId, String msg) {
			// TODO Auto-generated method stub
			if(mQuit) {
				return;
			}
			synchronized (lock1) {
				if (msgId == mApp.MSG_TYPE_VALUE) {
					String msgBody = msg;
					try {
						ParseMsg(msgBody);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mUiHandler.sendEmptyMessage(2);	
					}
				} else if (msgId == mApp.MSG_TYPE_ERROR) {
					Message message = Message.obtain();
					message.obj = msg;
					message.what = 1;
					mUiHandler.sendMessage(message);	
				}
			}
		}
		
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}
	private class UIHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0: {
					viewfinderView.setVisibility(View.VISIBLE);
					mShoppingCartMgr.clear();
	                Intent intent = new Intent();
	                intent.setClass(CustomerCodeCaptureActivity.this, SuccessActivity.class);
	                Bundle bundle = new Bundle();
	                bundle.putString("orderId", (String) msg.obj);
	                bundle.putString("customcode", mCustomercode);
	                intent.putExtras(bundle);
	                startActivity(intent); 
	                finish();
					break;
				}
				case 1: {
					viewfinderView.setVisibility(View.VISIBLE);
					Toast.makeText(CustomerCodeCaptureActivity.this, (String) msg.obj,
							Toast.LENGTH_SHORT).show();
					Intent intent = getIntent();
					overridePendingTransition(0, 0);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					finish();
					overridePendingTransition(0, 0);
					startActivity(intent);
					canCommit = true;
					break;
				}
				case 2: {
					viewfinderView.setVisibility(View.VISIBLE);
					Toast.makeText(CustomerCodeCaptureActivity.this, "后台JSON数据异常",
							Toast.LENGTH_SHORT).show();
                    Intent intent = getIntent();
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    canCommit = true;
					break;
				}
                case 3:{
                    Intent intent = getIntent();
                    overridePendingTransition(0, 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    break;
                    }
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}
	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
	private void ParseMsg(String msgBody) throws JSONException {
        JSONObject jsonObj = JSONObject.parseObject(msgBody);
        String op = jsonObj.getString("op");
        if (op.equals("submitorder")) {
            int errcode = jsonObj.getIntValue("errcode");
            if (errcode == 1) {
            	JSONObject data =  jsonObj.getJSONObject("data");
                String orderId = data.getString("orderid");
                Message m = new Message();
                m.obj = orderId;
                m.what = 0;//success
                mUiHandler.sendMessage(m);
            } else {
            	String errMsg = jsonObj.getString("msg");
                Message m = new Message();
                m.obj = errMsg;
                m.what = 1;//fail
                mUiHandler.sendMessage(m);
            }
        } 
	}
}
