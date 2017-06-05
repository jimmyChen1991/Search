package com.hhyg.TyClosing.info;
import java.util.ArrayList;


public class SpuInfo {
		public String attrInfo;	
		public int stock;
		public int  citAmount;
		public String imgUrl = "";
		public String activeName;
		public String activeId = "";
		public double citPrice;
		public double activePrice;
		public double activeCut;
		public String msPrice;
		public String full;
		public String fullReduce;
		public String barCode;
		public String name;
		public ArrayList<String> imageLinks;
		public String description;
		public int shelveStatus;
		public ArrayList<DisplayAttrGroup> baseAttrGroups;
		public ArrayList<DisplayAttrGroup> displayAttrGroups;
		public ArrayList<ActiveInfo> activeInfos;
		public SpuInfo() {
			imageLinks =  new ArrayList<String> ();
		}
		public boolean checkfull(){
			if(checkfullblank()){
				return false;
			}
			String[] fullattrs = full.split(",");
			for(int idx = 0;idx<fullattrs.length;idx++){
		    	try{
		    		Double.valueOf(fullattrs[idx]);
		    		}catch(NumberFormatException e){
		    			return true;
		    	}
			}
	    	return false;
		}
		public boolean checkfullblank(){
			if(full.equals("")){
				return true;
			}
			return false;
		}
		public String getActiveName(){
			String result;
			if(activeName.length()>120){
				result = activeName.substring(0, 120)+"...";
			}else{
				result = activeName;
			}
			return result;
		}
		public boolean isShelve(){
			return shelveStatus == 1;
		}
		public void setImgUrl(String url){
			imgUrl = url;
		}
		
	}