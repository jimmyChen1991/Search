package com.hhyg.TyClosing.mgr;

import com.hhyg.TyClosing.info.ShoppingCartInfo;

public class ActiveCutCast extends ActiveShopcat{

	public ActiveCutCast(String id) {
		super(id);
	}
	@Override
	public String getActiveCast() {
		if(infos.size() == 0){
			return "0.00";
		}
		double cast = 0.00;
		for(ShoppingCartInfo info :infos){
			if(activeId.equals(info.activeId)){
				cast += info.activePrice * info.cnt;
			}
		}
		return String.format("%.2f", cast);
	}

	@Override
	public String getActiveCut() {
		double cut = 0.00;
		if(infos.size() == 0){
			return "0.00";
		}
		for(ShoppingCartInfo info :infos){
			if(activeId.equals(info.activeId)){
				cut += (info.citPrice - info.activePrice) * info.cnt;
			}
		}
		return String.format("%.2f", cut);
	}

}
