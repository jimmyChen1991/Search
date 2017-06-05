package com.hhyg.TyClosing.config;

public class UrlConfig4Active implements NetrulConfig{

	@Override
	public String getIndexUrl() {
		return "http://active_commonapi.mianshui365.net/index.php";
	}

	@Override
	public String getServiceUrl() {
		return "http://active.mianshui365.net/api/MSService.php";
	}

}
