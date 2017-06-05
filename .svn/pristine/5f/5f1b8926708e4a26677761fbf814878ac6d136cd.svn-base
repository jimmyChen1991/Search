package com.hhyg.TyClosing.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.log.Logger;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.Vector;
/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback ,CaptureActivityHandler.DecodeCallBack{

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
    private UIHandler mUiHandler = new UIHandler();
    private String mCurBarCode = "";
    private boolean mQuit = false;
    private ImageButton scan;
    private ImageButton home;
    private ImageButton shopCat;
    private ImageButton brand;
    private ImageButton cate;
    
    private final Object lock1 = new Object();


    private boolean mQuick;//是否前往购物车或商品详情

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        findView();
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        Logger.GetInstance().Track("CaptureActivity onCreate");
    }
    
	private void findView() {
    	scan = (ImageButton) findViewById(R.id.scan);
    	scan.setBackgroundResource(R.drawable.allshop_scan_pressed);
    	home = (ImageButton) findViewById(R.id.home);
    	home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                UserTrackMgr.getInstance().onEvent(" tabartouch","首页");
                mQuick = true;
                mQuit = true;
                Intent intent = new Intent();
                intent.setClass(CaptureActivity.this, AllShopActivity.class);
                startActivity(intent);
                finish();
			}
		});
    	shopCat = (ImageButton) findViewById(R.id.shopcat);
    	shopCat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                UserTrackMgr.getInstance().onEvent(" tabartouch","购物车");
                Intent intent = new Intent();
                intent.setClass(CaptureActivity.this, ShopCartActivity.class);
                startActivity(intent);
                mQuick = true;
            }

        });
    	brand = (ImageButton) findViewById(R.id.brand);
    	brand.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                UserTrackMgr.getInstance().onEvent(" tabartouch","品牌");
                mQuick = true;
                mQuit = true;
                Intent intent = new Intent();
                intent.setClass(CaptureActivity.this, BrandActivity.class);
                startActivity(intent);
                finish();
			}
		});
    	cate = (ImageButton) findViewById(R.id.cate);
    	cate.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
                UserTrackMgr.getInstance().onEvent(" tabartouch","类别");
                mQuick = true;
                mQuit = true;
                Intent intent = new Intent();
                intent.setClass(CaptureActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish();
			}
		});
    }

    @Override
    protected void onResume() {
        shopCat.setClickable(false);
        super.onResume();
        init();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("CaptureActivity");
        Logger.GetInstance().Track("CaptureActivity on onResume");
    }


    private Handler mClickbleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	shopCat.setClickable(true);
        	Logger.GetInstance().Debug("shopcatBtn SetClickable");
        }
    };

    private void init() {
        Logger.GetInstance().Debug("CaptureActivity init begin");
        mQuit = false;
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        synchronized (lock1) {
            if (hasSurface) {
                initCamera(surfaceHolder);
                Log.d("capture", "captureinit");
            } else {
                surfaceHolder.addCallback(this);
                surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                Log.d("capture", "captureinit");
            }
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
        Logger.GetInstance().Debug("CaptureActivity init end");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Logger.GetInstance().Debug("CaptureActivity onKeyDown begin");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mQuit = true;
            mQuick = true;
            synchronized (lock1) {
                Log.d("capture", " capture keycode_back");
                finish();
                return false;
            }
        }
        Logger.GetInstance().Debug("CaptureActivity onKeyDown end");
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
    	synchronized (lock1) {
            super.onPause();
            Log.d("capture", "capture onpause  start");
            if (handler != null) {
                handler.quitSynchronously();
                handler = null;
                Logger.GetInstance().Debug("CaptureActivity set handler");
            }
            if (mQuick) {
                new Thread(new CloseDriver()).start();
                Logger.GetInstance().Debug("CaptureActivity thread close");
            } else {
                CameraManager.get().closeDriver();
                Logger.GetInstance().Debug("CaptureActivity normal close");
                mQuick = false;
            }
        }
        Logger.GetInstance().Track("CaptureActivity onPause");
    }

    class CloseDriver implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (lock1) {
                CameraManager.get().closeDriver();
                mQuick = false;
                Logger.GetInstance().Debug("Driver Closed");
            }
        }
    }

    @Override
    protected void onDestroy() {
		synchronized (lock1) {
			inactivityTimer.shutdown();
			super.onDestroy();
			Logger.GetInstance().Debug("destory");
		}
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        Logger.GetInstance().Debug("CaptureActivity handleDecode begin");
        if (mQuit) {
            return;
        }
        synchronized (lock1) {
            inactivityTimer.onActivity();
            playBeepSoundAndVibrate();
            mCurBarCode = result.getText();
            Logger.GetInstance().Debug("ScanResultBarCode:" + mCurBarCode);
            if (mCurBarCode.equals("")) {
                Toast.makeText(CaptureActivity.this, "扫码姿势不对", Toast.LENGTH_SHORT).show();
                mUiHandler.sendEmptyMessage(4);
            }else {
                UserTrackMgr.getInstance().onEvent("scancode",mCurBarCode);
                Intent it = new Intent();
                it.setClass(this, GoodsInfoActivity.class);
                it.putExtra("barcode", mCurBarCode);
                startActivity(it);
            }
        }
    }

    Handler mInitHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
        	synchronized (lock1) {
        	Logger.GetInstance().Debug("inithandler init");
            switch (msg.what) {
                case 0: {
                    AlertDialog.Builder builder = new Builder(CaptureActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("无法获得摄像头权限，请检查是否已经打开摄像头权限");
                    builder.setIcon(R.drawable.ic_launcher);
                    builder.setCancelable(false);
                    // 更新
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                            // 显示下载对话框
                        }
                    });
                    builder.create().show();
                    break;
                }
                case 1: {
                    if (handler == null) {
                        handler = new CaptureActivityHandler(CaptureActivity.this, decodeFormats,
                                characterSet);
                    }
                    break;
                }
            }
        }
      }
    };

    private void initCamera(final SurfaceHolder surfaceHolder) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                synchronized (lock1) {
                    try {
                        CameraManager.get().openDriver(surfaceHolder);
                        Logger.GetInstance().Debug("capture initthread");
                    } catch (IOException ioe) {
                        Logger.GetInstance().Exception(ioe.getMessage());
                        Thread.currentThread().interrupt();
                    } catch (RuntimeException e) {
                        Logger.GetInstance().Exception(e.getMessage());
                        mInitHandle.sendEmptyMessage(0);
                    }
                    mInitHandle.sendEmptyMessage(1);
                    mClickbleHandler.sendEmptyMessage(0);
                    Logger.GetInstance().Debug("initEnd");
                }
            }
        }).start();
    }
    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    viewfinderView.setVisibility(View.VISIBLE);
                    Intent intent = new Intent();
                    intent.setClass(CaptureActivity.this, GoodsInfoActivity.class);
                    startActivity(intent);
                    mQuick = true;
                    break;
                }
                case 4:{
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
                Logger.GetInstance().Exception(e.getMessage());
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
}