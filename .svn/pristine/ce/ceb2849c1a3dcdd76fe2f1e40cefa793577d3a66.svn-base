package com.hhyg.TyClosing.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.hhyg.TyClosing.R;

/**
 * 进度弹出框工具类
 *
 */
public class ProgressDialogUtil {

    /**
     * 无标题，message: R.string.alert_loading_msg
     *
     * @param context
     * @param isCanceled true: 可以取消，false: 不可以
     * @return
     */
    public static Dialog show(Context context, boolean isCanceled) {
        return show(context, context.getString(R.string.alert_loading_msg),
                isCanceled);
    }

    /**
     * 无标题，message 传入
     *
     * @param context
     * @param message
     * @param isCanceled
     *            true: 可以取消，false: 不可以
     * @return
     */
    // public static Dialog show(Context context,String message, boolean
    // isCanceled) {
    // LayoutInflater inflater = LayoutInflater.from(context);
    // View v = inflater.inflate(R.layout.loading_dialog, null);
    //
    // TextView msg = (TextView) v.findViewById(R.id.msg);
    // msg.setText(message);
    //
    // View spaceshipImage = (TextView) v.findViewById(R.id.loading);
    // // 加载动画
    // Animation animation = AnimationUtils.loadAnimation(context,
    // R.anim.load_animation);
    // // 使用ImageView显示动画
    // spaceshipImage.startAnimation(animation);
    // // 创建自定义样式dialog
    // Dialog loadingDialog = new Dialog(context, R.style.LoadingDialogTheme);
    // // 是否可以用“返回键”取消
    // loadingDialog.setCancelable(isCanceled);
    // loadingDialog.setCanceledOnTouchOutside(isCanceled);
    // loadingDialog.setContentView(v);
    // loadingDialog.show();
    // return loadingDialog;
    // }
    /**
     * 装点点的ImageView数组
     */
    private static ImageView[] tips;

    public static Dialog show(Context context, String message,
                              boolean isCanceled) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);

        TextView msg = (TextView) v.findViewById(R.id.msg);
        msg.setText(message);

        ImageView imageView = (ImageView) v.findViewById(R.id.loading);
        imageView.setBackgroundResource(R.drawable.load_animation);
        AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
        anim.start();
        // 创建自定义样式dialog
        Dialog loadingDialog = new Dialog(context, R.style.LoadingDialogTheme);
        // 是否可以用“返回键”取消
        loadingDialog.setCancelable(isCanceled);
        loadingDialog.setCanceledOnTouchOutside(isCanceled);
        loadingDialog.setContentView(v);
        loadingDialog.show();
        return loadingDialog;
    }

    @SuppressLint("NewApi")
    private static void initDots(final ViewGroup pointGroup) {
        final AlphaAnimation backgroundColorAnimator = new AlphaAnimation(0.2f,
                1f);
        backgroundColorAnimator.setDuration(1000);


    }

    public static Dialog dlg = null;
    public static void show(Context context) {
        hide();
        dlg = show(context, context.getString(R.string.alert_loading_msg), false);
        dlg.show();
    }

    public static void hide(){
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
            dlg = null;
        }
    }
}
