package com.hhyg.TyClosing.mgr;

public class ShopcarFullCalaer {
	private String full;
	private String fullReduce;
	private ActiveCast cast;
	private String title;
	public ShopcarFullCalaer(String full, String fullReduce,String activeId) {
		super();
		this.full = full;
		this.fullReduce = fullReduce;
		this.cast = new FullReduceCast(activeId);
	}
	public String fullCala(){
		String[] fullattrs;
		String[] fullReduces;
		String bound = "";
		String next ="";
		fullattrs = full.split(",");
		fullReduces = fullReduce.split(",");
		double casted = Double.valueOf(cast.getActiveCast()) + Double.valueOf(cast.getActiveCut());
		for(int idx = (fullattrs.length-1);idx>=0;idx--){
			double full = Double.parseDouble(fullattrs[idx]);
			double fullLevelReduce = Double.parseDouble(fullReduces[idx]);
			if(casted >= full){
				bound = "已满足[满" + String.format("%.0f", full) +"减" +String.format("%.0f", fullLevelReduce) +"]";
				if(idx  != (fullattrs.length - 1)){
					String nextfull = fullattrs[idx + 1];
					String nextfullReduce = fullReduces[idx + 1];
					double nextfullNum = Double.valueOf(nextfull);
					String nextbound = String.format("%.0f",nextfullNum - casted);
					next = "再购" + nextbound +"立享[满" +nextfull +"减" +nextfullReduce + "]";
				}
				break;
			}
			if(idx == 0){
				String nextfull = fullattrs[idx];
				String nextfullReduce = fullReduces[idx];
				double nextfullNum = Double.valueOf(nextfull);
				String nextbound = String.format("%.0f",nextfullNum - casted);
				next = "再购" + nextbound +"立享[满" +nextfull +"减" +nextfullReduce + "]";
			}
		}
		String result;
		if(bound.isEmpty()){
			if(next.isEmpty()){
				result = "";
				title = "";
			}else{
				title = "去凑单>";
				result = next;
			}
		}else{
			if(next.isEmpty()){
				result = bound;
				title = "再逛逛>";
			}else{
				result = bound + "," +next;
				title = "去凑单>";
			}
		}
		return result;
	}
	
	public String getTitle(){
		fullCala();
		return title;
	}
}
