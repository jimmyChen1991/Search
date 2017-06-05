package com.hhyg.TyClosing.allShop.handler;


import com.hhyg.TyClosing.global.MyApplication;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
public class SimpleHandler extends Handler{
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
			case 0:
				Toast.makeText(MyApplication.GetInstance(), "数据异常", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(MyApplication.GetInstance(), "网络异常", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(MyApplication.GetInstance(), (String) msg.obj, Toast.LENGTH_SHORT).show();
				break;
		}
	}
}
