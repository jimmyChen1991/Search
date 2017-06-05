package com.hhyg.TyClosing.mgr;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import com.hhyg.TyClosing.log.Logger;

public class VersionMgr {
	public static String getAppVersionName(Context context) {    
	    String versionName = "";    
	    try {    
	        // ---get the package info---    
	        PackageManager pm = context.getPackageManager();    
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);    
	        versionName = pi.versionName;    
	        if (versionName == null || versionName.length() <= 0) {    
	            return "";    
	        }    
	    } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage());
	        Log.e("VersionInfo", "Exception", e);    
	    }    
	    return versionName;    
	} 
	
	public static int getAppVersionCode(Context context) {    
	    int versionName = 1;    
	    try {    
	        // ---get the package info---    
	        PackageManager pm = context.getPackageManager();    
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);    
	        versionName = pi.versionCode;
	        
	        if (versionName <= 0) {    
	            return 0;    
	        }    
	    } catch (Exception e) {
            Logger.GetInstance().Exception(e.getMessage());
	        Log.e("VersionInfo", "Exception", e);    
	    }    
	    return versionName;    
	} 

}