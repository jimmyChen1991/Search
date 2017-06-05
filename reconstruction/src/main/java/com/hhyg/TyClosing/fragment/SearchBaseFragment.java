package com.hhyg.TyClosing.fragment;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.mgr.SearchGoodMgr;
import com.hhyg.TyClosing.ui.view.DrawerTitle;

import android.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class SearchBaseFragment extends Fragment{
	protected RelativeLayout allChoseItem;
	protected ImageView allChosenImg;
	protected TextView allChoseTitle;
	protected ListView contentListView;
	protected SearchGoodMgr mSearchGoodMgr;
	protected DrawerTitle title;
	
	public SearchBaseFragment() {
		super();
	}
	public SearchBaseFragment(DrawerTitle t,SearchGoodMgr mgr) {
		title = t;
		mSearchGoodMgr = mgr;
	}
	
	protected void findView(View root){
		allChoseItem = (RelativeLayout) root.findViewById(R.id.contenttop);
		allChosenImg = (ImageView) root.findViewById(R.id.chosenimg);
		contentListView  = (ListView) root.findViewById(R.id.chosedetail_lv);
		allChoseTitle = (TextView) root.findViewById(R.id.fixtitile);
	}
	public abstract void clearchosenItem();
	public abstract void showChoseContent();
}
