package com.hhyg.TyClosing.ui.base;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	protected void openActivity(Class<?> pClass) {
		openActivity(pClass, null);
	}

	/**
	 * 通过类名启动Activity，并且含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	public void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}
	/**
	 * 通过类名启动Activity，并且finish自己，含有Bundle数据
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivityWithEnding(Class<?> pClass,Bundle pBundle){
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		finish();
	}
	/**
	 * 通过类名启动Activity，并且finish自己
	 * 
	 * @param pClass
	 * @param pBundle
	 */
	protected void openActivityWithEnding(Class<?> pClass){
		openActivityWithEnding(pClass,null);
	}
	
	
}
