package com.hhyg.TyClosing.info;

import java.util.ArrayList;

public class ShoppingCartInfo {
	    public String name;
	    public String spuName;
	    public String brand;     //品牌
	    public String imgUrl;
		public String attrInfo;	
		public int stock;       //库存
		public int  citAmount;   //限购数量
		public String activeName; //活动名称
		public double citPrice;		//税价
		public double activePrice; //折后价格
		public double activeCut;  //折扣
		public String full;			//满减的触发边界
		public String fullReduce; //满减
		public String barCode;
		public int cnt;			//数量
		public int typeId;	
		public String typeName;
		public String activeId = "";
		public String price; //免税价
		public String time; //
		public String msPrice;
		public ArrayList<ActiveInfo> aInfos;
	}