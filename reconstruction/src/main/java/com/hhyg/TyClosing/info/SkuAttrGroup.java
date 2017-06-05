package com.hhyg.TyClosing.info;


public class SkuAttrGroup {
	private String attrName;
	private SkuAttr[] attrs;
	
	public SkuAttrGroup(String attrName, SkuAttr[] attrs) {
		this.attrName = attrName;
		this.attrs = attrs;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public SkuAttr[] getAttrs() {
		return attrs;
	}
	public void setAttrs(SkuAttr[] attrs) {
		this.attrs = attrs;
	}
	
}
