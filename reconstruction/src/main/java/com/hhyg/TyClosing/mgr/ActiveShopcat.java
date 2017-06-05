package com.hhyg.TyClosing.mgr;

import java.util.ArrayList;

import com.hhyg.TyClosing.info.ShoppingCartInfo;

public abstract class ActiveShopcat implements ActiveCast{
	protected String activeId;
	protected ArrayList<ShoppingCartInfo> infos = ShoppingCartMgr.getInstance().getAll();
	public ActiveShopcat(String id) {
		activeId = id;
	}
}
