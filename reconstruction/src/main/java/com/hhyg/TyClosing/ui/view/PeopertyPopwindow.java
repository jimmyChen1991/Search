package com.hhyg.TyClosing.ui.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.PopupWindow;

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
        setBackgroundDrawable(new ColorDrawable(0000000000));
        setOutsideTouchable(true);
        setFocusable(true);
        setContentView(content);
    }

}
