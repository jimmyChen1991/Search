package com.hhyg.TyClosing.mgr;

import com.hhyg.TyClosing.info.ShoppingCartInfo;

public class FullReduceCast extends ActiveShopcat{
	public FullReduceCast(String id) {
		super(id);
	}

	@Override
	public String getActiveCast() {
		return calaMoney(0);
	}

	@Override
	public String getActiveCut() {
		return calaMoney(1);
	}
	
	private String calaMoney(int type){
		String[] fullattrs = null;
		String[] fullReduces = null;
		int fullLenth = 0;
		Double cast = 0.00;
		Double cut = 0.00;
		if(infos.size() == 0){
			return "0.00";
		}
		for(ShoppingCartInfo info : infos){
			if(info.activeId.equals(activeId)){
				cast += info.citPrice * info.cnt;
				if(fullattrs == null){
					fullattrs = info.full.split(",");
					fullReduces = info.fullReduce.split(",");
				}
			}
		}
		if(fullattrs == null){
			return String.format("%.2f", cast);
		}
		fullLenth = fullattrs.length;
		for(int idx1 = (fullLenth-1);idx1>=0;idx1--){
			double full = Double.parseDouble(fullattrs[idx1]);
			double fullLevelReduce = Double.parseDouble(fullReduces[idx1]);
			if(cast >= full){
				cast = cast - fullLevelReduce;
				cut = fullLevelReduce;
				break;
				}	
		}
		if(type == 0){
			return String.format("%.2f", cast);
		}else{
			return String.format("%.2f", cut);
		}
	}
}
