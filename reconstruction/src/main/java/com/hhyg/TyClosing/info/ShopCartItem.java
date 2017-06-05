package com.hhyg.TyClosing.info;

import java.util.ArrayList;

public class ShopCartItem {
	  	private String name;
	  	private String brand;     //品牌
	  	private String imgUrl;
	  	private String attrInfo;	
	  	private int stock;       //库存
	  	private int  citAmount;   //限购数量
	  	private String citPrice;		//免税价
	  	private String activePrice;
	  	private String barCode;
	  	private int cnt;			//数量
	  	private int typeId;	
	  	private String typeName;
	  	private String time; //
	  	private String stockInfo;
	  	private ArrayList<ActiveInfo> aInfos;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getBrand() {
			return brand;
		}
		public void setBrand(String brand) {
			this.brand = brand;
		}
		public String getImgUrl() {
			return imgUrl;
		}
		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}
		public String getAttrInfo() {
			return attrInfo;
		}
		public void setAttrInfo(String attrInfo) {
			this.attrInfo = attrInfo;
		}
		public int getStock() {
			return stock;
		}
		public void setStock(int stock) {
			this.stock = stock;
		}
		public int getCitAmount() {
			return citAmount;
		}
		public void setCitAmount(int citAmount) {
			this.citAmount = citAmount;
		}
		public String getCitPrice() {
			return citPrice;
		}
		public void setCitPrice(String citPrice) {
			this.citPrice = citPrice;
		}
		public String getBarCode() {
			return barCode;
		}
		public void setBarCode(String barCode) {
			this.barCode = barCode;
		}
		public int getCnt() {
			return cnt;
		}
		public void setCnt(int cnt) {
			this.cnt = cnt;
		}
		public int getTypeId() {
			return typeId;
		}
		public void setTypeId(int typeId) {
			this.typeId = typeId;
		}
		public String getTypeName() {
			return typeName;
		}
		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public ArrayList<ActiveInfo> getaInfos() {
			return aInfos;
		}
		public void setaInfos(ArrayList<ActiveInfo> aInfos) {
			this.aInfos = aInfos;
		}
	  	public void setActivePrice(String activePrice) {
			this.activePrice = activePrice;
		}
	  	public String getActivePrice() {
			return activePrice;
		}
	  	public void setStockInfo(String stockInfo) {
			this.stockInfo = stockInfo;
		}
	  	public String getStockInfo() {
			return stockInfo;
		}
	  	
}
