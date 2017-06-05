package com.hhyg.TyClosing.mgr;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.log.Logger;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;


public class UpVersionMgr 
{
    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    /* 下载失败*/
    private static final int DOWNLOAD_FAILED = 3;
    
    private final String BOUNDARY = java.util.UUID.randomUUID().toString();
	private final String PREFIX = "--", LINEND = "\r\n";
	private final String MULTIPART_FROM_DATA = "multipart/form-data";
	private final String CHARSET = "UTF-8";
	private final String END_STR = PREFIX + BOUNDARY + PREFIX + LINEND;
	private static final String SDPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	String path = SDPATH +"/hhyg/"; 
	String upDataFileFolderPath = path+"downLoad/";
	private int mCurVersionCode;
	private int mNewVersionCode;
	private int mUpdataFlag; 
    HashMap<String, String> mHashMap;
    /* 下载保存路径 */
    private String mSavePath;
    /*解析md5*/
    private static final String HASH_ALGORITHM = "MD5";
    /*正确apk字节的md5*/
    private String checkLoadedApkMd5;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private Context mContext;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private String apkResourceUrl;
    private Dialog mDownloadDialog;
    private Handler mVersionHandler;
   
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
            // 正在下载
            case DOWNLOAD:
                // 设置进度条位置
                mProgress.setProgress(progress);
                break;
            case DOWNLOAD_FINISH:
                // 安装文件
                installApk();
                break;
            case DOWNLOAD_FAILED:
            	showDownLoadFailedDialog();
            	break;
            default:
                break;
            }
        };
    };

    public UpVersionMgr(Context context,Handler handler)
    {
        this.mContext = context;
        this.mVersionHandler = handler;
    }

    /**
     * 检测软件更新
     */
    public void checkUpdate()
    {
    	this.mCurVersionCode = getVersionCode(mContext);
        new isUpdataThread().start();
    }

    /**
     * 检查软件是否有更新版本
     * 
     * @return
     */
    private void isUpdate()
    {
        // 获取当前软件版本
    
        this.mCurVersionCode = getVersionCode(mContext);
        new isUpdataThread().start();
        
    }
    

/**
 * 获取软件版本号
 * 
 * @param context
 * @return
 */
private int getVersionCode(Context context)
{
    int versionCode = 0;
    try
    {
        // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
        versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        Logger.GetInstance().Debug("getVersionCode:"+versionCode);
      //  versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
    } catch (NameNotFoundException e) {
        Logger.GetInstance().Exception(e.getMessage());
        e.printStackTrace();
    }
    return versionCode;
}

    /**
     * 显示软件更新对话框
     */
    public void showNoticeDialog()
    {
        // 构造对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        final String message = mContext.getString(R.string.soft_update_info)+"VerSionCode:"+mNewVersionCode;
        builder.setMessage(message);
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {	
            	Logger.GetInstance().Debug("AppStart Start DownLoad Apk");
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {	
            	Logger.GetInstance().Debug("AppStart Cancel DownLoad Apk");
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }
    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog()
    {
        // 构造软件下载对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        builder.setCancelable(false);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        builder.setView(v);
        // 取消更新
        builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {	
            	Logger.GetInstance().Debug("AppStart Cancel DownLoad Apk When Updating");
                dialog.dismiss();
                // 设置取消状态
                cancelUpdate = true;
            }
        });
        mDownloadDialog = builder.create();
        mDownloadDialog.show();
        // 现在文件
        downloadApk();
    }
    private void showDownLoadFailedDialog(){
    	  // 构造软件下载对话框
        AlertDialog.Builder builder = new Builder(mContext);
        builder.setTitle(R.string.soft_update_failed);
        builder.setMessage(R.string.soft_update_failed_contiuned);
        builder.setCancelable(false);
        // 更新
        builder.setPositiveButton(R.string.queding, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                cancelUpdate = false;
                // 显示下载对话框
                showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton(R.string.cancel, new OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.show();
    }

    /**
     * 下载apk文件
     */
    private void downloadApk()
    {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    private class isUpdataThread extends Thread{
    	@Override
    	public void run(){
    		StringBuilder resultData = new StringBuilder("");
			String resultStr = "";
			String strSendData = "";
    		try{
    			URL uri = new URL(Constants.getServiceUrl());
    			JSONObject param1 = new JSONObject();
    			param1.put("op", "updateapkversion");
    			param1.put("versioncode", mCurVersionCode);
    			param1.put("imei", MyApplication.GetInstance().getAndroidId());
    			param1.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
    			param1.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());

                strSendData = param1.toJSONString();

    			final HttpURLConnection conn = (HttpURLConnection) uri
    					.openConnection();
    			conn.setReadTimeout(10 * 1000);
    			
    			conn.setConnectTimeout(10 * 1000);
    			conn.setDoInput(true);// 允许输入
    			conn.setDoOutput(true);// 允许输出
    			conn.setUseCaches(false);
    			conn.setRequestMethod("POST"); // Post方式
    			conn.setRequestProperty("connection", "keep-alive");
    			conn.setRequestProperty("Charsert", "UTF-8");
    			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
    					+ ";boundary=" + BOUNDARY);
    			// 首先组拼文本类型的参数
    			final StringBuilder sb = new StringBuilder();
    					sb.append(PREFIX);
    					sb.append(BOUNDARY);
    					sb.append(LINEND);
    					sb.append("Content-Disposition: form-data; name=\""
    							+"parameter" + "\"" + LINEND);
    					sb.append("Content-Type: text/plain; charset="
    							+ CHARSET + LINEND);
    					sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
    					sb.append(LINEND);
    					sb.append(param1.toString());
    					sb.append(LINEND);
    			DataOutputStream outStream;
    			outStream = new DataOutputStream(conn.getOutputStream());
    			outStream.write((sb.toString() + END_STR).getBytes());
    			outStream.flush();
    			// 得到响应
    			InputStreamReader in = new InputStreamReader(
    					conn.getInputStream());
    			BufferedReader buffer = new BufferedReader(in);
    			String str = null;
    			while ((str = buffer.readLine()) != null) {
    				resultData.append(str);
    			}
    			in.close();
    			resultStr = resultData.toString();
    			JSONObject jsonObj = JSONObject.parseObject(resultStr);
    			int updataFlag = jsonObj.getIntValue("updateflag");
    			mUpdataFlag = updataFlag;
    			int newVersionCode = Integer.valueOf(jsonObj.getString("versioncode"));
    			mNewVersionCode = newVersionCode;
    			String resourceUrl = jsonObj.getString("url");
    			apkResourceUrl = resourceUrl;
    			checkLoadedApkMd5 = jsonObj.getString("md5");
    			mVersionHandler.sendEmptyMessage(mUpdataFlag);
    			Logger.GetInstance().Debug("[UpdataFlag]:"+mUpdataFlag+"[MD5]:"+checkLoadedApkMd5);
    		}catch(Exception e) {
                Logger.GetInstance().Exception(e.getMessage() + " url is " + Constants.getServiceUrl() + " data is :" + strSendData);
            }
    		
    	}
    	
    }
    private class downloadApkThread extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                {
                    // 获得存储卡的路径
                  
                    mSavePath = upDataFileFolderPath + mNewVersionCode+"/";
                    URL url = new URL(apkResourceUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn .setRequestProperty("Accept-Encoding", "identity"); 
                    conn.connect();
               
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                 
                    Logger.GetInstance().Debug(mSavePath+conn.getResponseCode());
                    InputStream is = conn.getInputStream();
                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists())
                    {
                        file.mkdirs();
                    }
                    File apkFile = new File(mSavePath,mNewVersionCode+".apk");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do
                    {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0)
                        {
                            // 下载完成
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e)
            {	Logger.GetInstance().Exception(e.getMessage());
                e.printStackTrace();
            } catch (IOException e)
            {	 Logger.GetInstance().Exception(e.getMessage());
                e.printStackTrace();
            }
            if(cancelUpdate){
            	return;
            }
            if(checkNeedHandlerLoadFailed()){
            	return;
            }
            // 取消下载对话框显示
            mDownloadDialog.dismiss();
            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
        }
    };
    private boolean checkNeedHandlerLoadFailed(){
    	if(!checkLoadedMD5(getFileMd5())){
            handlerLoadFailed();
            return true;
           }
    	return false;
    }
    private void handlerLoadFailed(){
	   	 mDownloadDialog.dismiss();
	   	 mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
    }
    /**
     * 安装APK文件
     */
    private void installApk()
    {
        File apkfile = new File(mSavePath,mNewVersionCode+".apk");
        if (!apkfile.exists())
        {  
            return;
        }
      
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
    private boolean checkLoadedMD5(String data){
    	return data.equals(checkLoadedApkMd5);
    }
	private String getFileMd5(){
        try {
            // 获取一个文件的特征信息，签名信息。
            File file = new File(mSavePath+mNewVersionCode+".apk");
            // md5
            MessageDigest digest = MessageDigest.getInstance("md5");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            fis.close();
            byte[] result = digest.digest();
            StringBuffer sb  = new StringBuffer();
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage());
            e.printStackTrace();
            return "";
        }
    }
}