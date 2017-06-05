package com.hhyg.TyClosing.ui.view;


import com.hhyg.TyClosing.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;  
public class ShopcarMenu extends PopupWindow implements OnClickListener{
		private Activity activity;
		private View conentView;  
		private View v_item1;
		private View v_item2;
		private OnItemClickListener onItemClickListener;
	    public enum MENUITEM {
			ITEM1, ITEM2, ITEM3
		}
	    public ShopcarMenu(Activity context) {
	    	super(context);
			this.activity = context;
	        LayoutInflater inflater = (LayoutInflater) activity  
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	        conentView = inflater.inflate(R.layout.shopcar_popup_menu, null);  
	        int h = context.getWindowManager().getDefaultDisplay().getHeight();  
	        int w = context.getWindowManager().getDefaultDisplay().getWidth();  
	        // 设置SelectPicPopupWindow的View  
	        this.setContentView(conentView);  
	        // 设置SelectPicPopupWindow弹出窗体的宽  
	        this.setWidth(300); 
	        // 设置SelectPicPopupWindow弹出窗体的高  
	        this.setHeight(LayoutParams.WRAP_CONTENT);  
	        // 设置SelectPicPopupWindow弹出窗体可点击  
	        this.setFocusable(true);  
	        this.setOutsideTouchable(true);  
	        // 刷新状态  
	        this.update();  
	        // 实例化一个ColorDrawable颜色为半透明  
	        ColorDrawable dw = new ColorDrawable(0000000000);  
	        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
	        this.setBackgroundDrawable(dw);  
	        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
	        // 设置SelectPicPopupWindow弹出窗体动画效果  
	        v_item1 = (RelativeLayout) conentView  
	                .findViewById(R.id.delete);  
	        v_item2 = (RelativeLayout) conentView  
	                .findViewById(R.id.order);
	        v_item1.setOnClickListener(this);
	        v_item2.setOnClickListener(this);
	    }  
	  
	    /** 
	     * 显示popupWindow 
	     *  
	     * @param parent 
	     */  
	    public void showPopupWindow(View parent) {  
	        if (!this.isShowing()) {  
	            // 以下拉方式显示popupwindow  
	           // this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 30);
	        	
	        	this.showAsDropDown(parent,100, 18);
	        } else {  
	            this.dismiss();  
	        }  
	    }


		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MENUITEM menuitem = null;
			String str = "";
			if (v == v_item1) {
				menuitem = MENUITEM.ITEM1;
				str = "选项1";
			} else if (v == v_item2) {
				menuitem = MENUITEM.ITEM2;
				str = "选项2";
			}
			if (onItemClickListener != null) {
				onItemClickListener.onClick(menuitem, str);
			}
			dismiss();
		}  
		public interface OnItemClickListener {
			public void onClick(MENUITEM item, String str);
		}
		public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
			this.onItemClickListener = onItemClickListener;
		}
}
