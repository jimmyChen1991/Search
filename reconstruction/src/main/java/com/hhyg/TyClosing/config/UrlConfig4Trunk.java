package com.hhyg.TyClosing.config;

public class UrlConfig4Trunk implements NetrulConfig{

	@Override
	public String getIndexUrl() {
		return "http://commonapi.mianshui365.net/index.php";
	}

	@Override
	public String getServiceUrl() {
		return "http://exps.mianshui365.net/api/MSService.php";
	}

}
