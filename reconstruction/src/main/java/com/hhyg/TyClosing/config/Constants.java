package com.hhyg.TyClosing.config;

import android.os.Environment;
import android.graphics.Color;
public class Constants {
	
	
	//SD卡路径
	public static final String SDPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	
	//应用缓存路径
	public static final String BASE_LOCAL_PATH = SDPATH + "/hhyg/";
	
	//图片存储路径
	public static final String BASE_LOCAL_PATH_PIC = BASE_LOCAL_PATH + "pic/";
	
	//品牌图片存储路径
	public static final String BRAND_LOCAL_PATH_PIC = BASE_LOCAL_PATH_PIC +"brand/";
	
	//分类图片存储路径
	public static final String CAT_LOCAL_PATH_PIC = BASE_LOCAL_PATH_PIC +"cat/";
	
	//专题图片存储路径
	public static final String TITLE_LOCAL_PATH_PIC = BASE_LOCAL_PATH_PIC +"title/";
	
	//商品图片存储路径
	public static final String SHOP_LOCAL_PATH_PIC = BASE_LOCAL_PATH_PIC +"barcode/";
	
	public static final String SPECIAL_LOCAL_PATH_PIC = BASE_LOCAL_PATH_PIC+"special/";
	
	public static final int PAGE_SIZE = 12;

	public static final int SELECTOR_COLOR = Color.rgb(195, 140, 86);
	
	public static final int UNSELECTOR_COLOR = Color.rgb(153, 153, 153);
	
	public static final int UNSELECTOR_BLACK_COLOR = Color.rgb(53, 53, 53);
	
	public static final int WHITE_COLOR = Color.rgb(255, 255, 255);
	
	public static final int GRAY_COLOR = Color.rgb(242, 242, 242);
	
	public static final String PRICE_TITLE = "¥";
	
	public static final String MIANSHUI_TITLE = "免税价: "+PRICE_TITLE;
	
	public static final String ZUXIAO_TITLE = "促销价 "+PRICE_TITLE;
	
	public static final String PRIVILEGE_TITLE = "特权价"+PRICE_TITLE;

	/*
	此开关标志程序是否是测试开发状态，将会影响输
		1.入框的默认参数
		2.友盟SDK是否处在开发状态
	*/
	public static boolean IS_DEBUG_MODE = true;

	//是否打开日志开关，调试使用，日志功能稳定后会删除
	public static final boolean IS_LOG_OPEN = false;
	
	public static final String V4BUYCONDITIONURL = "http://commonapi.mianshui365.com/xiangou.html";//销售主页里的限购条件


	private static  NetrulConfig url = new UrlConfig4Active();
	
	public static final String getIndexUrl(){
		return url.getIndexUrl();
	}
	
	public static final String getServiceUrl(){
		return url.getServiceUrl();
	}

}
