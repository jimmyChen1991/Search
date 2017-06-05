package com.hhyg.TyClosing.fragment;

import com.hhyg.TyClosing.allShop.mgr.AllShopInfoMgr;

import android.app.Fragment;

public abstract class AllShopBaseFragment extends Fragment{
	protected AllShopInfoMgr mAllShopInfoMgr = AllShopInfoMgr.getInstance(); 
	public abstract void setLastestContent();
}
