package com.hhyg.TyClosing.allShop.info;
import java.util.ArrayList;
public class CateInfo {
	public String cateId;
	public String netUri;
	public String name;
	public int cateLevel;//1代表1级分类，2代表2级分类，3代表三级分类
	public CateInfo parentInfo;
	public ArrayList<CateInfo> childInfos;
	@Override
	public String toString() {
		String str = "id:"+cateId+"--name:"+name+"--level"+cateLevel;
		return str;
	}
}
