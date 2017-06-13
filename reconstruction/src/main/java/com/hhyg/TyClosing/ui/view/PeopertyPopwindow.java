package com.hhyg.TyClosing.ui.view;

import android.app.Activity;
import android.content.Context;
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

    public PeopertyPopwindow(Activity context,View content) {
        super(context);
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setWidth(metrics.widthPixels);
        setHeight(metrics.heightPixels/5*2);
        setOutsideTouchable(true);
        setFocusable(true);
        setContentView(content);
    }

}
