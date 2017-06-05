package com.hhyg.TyClosing.info;

import java.util.ArrayList;

public class ActiveColumns {
	
	private ActiveInfo aInfo;
	private String totalPrice;
	private String discount;
	private String realCast;
	private ArrayList<ShopCartItem> cartItems;
	public ActiveInfo getaInfo() {
		return aInfo;
	}
	public void setaInfo(ActiveInfo aInfo) {
		this.aInfo = aInfo;
	}
	public String getTotal_price() {
		return totalPrice;
	}
	public void setTotal_price(String total_price) {
		this.totalPrice = total_price;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getReal_cast() {
		return realCast;
	}
	public void setReal_cast(String real_cast) {
		this.realCast = real_cast;
	}
	public ArrayList<ShopCartItem> getCartItems() {
		return cartItems;
	}
	public void setCartItems(ArrayList<ShopCartItem> cartItems) {
		this.cartItems = cartItems;
	}
	
	
	
}
