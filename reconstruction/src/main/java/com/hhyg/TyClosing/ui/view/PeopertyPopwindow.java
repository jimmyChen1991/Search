package com.hhyg.TyClosing.ui.view;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.PeopertyPopAdapter;

/**
 * Created by user on 2017/6/13.
 */

public class PeopertyPopwindow extends PopupWindow {

    public PeopertyPopwindow(Activity context, PeopertyPopAdapter adapter) {
        super(context);
        View content = LayoutInflater.from(context).inflate(R.layout.popwindow_peoperty,null);
        RecyclerView recyclerView = (RecyclerView) content.findViewById(R.id.pop_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(context,GridLayoutManager.VERTICAL,3,false));
        recyclerView.setAdapter(adapter);
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setWidth(metrics.widthPixels);
        setHeight(metrics.heightPixels/3);
        setOutsideTouchable(true);
        setFocusable(true);
        setContentView(content);
    }




}
