package com.hhyg.TyClosing.ui.dialog;

import com.hhyg.TyClosing.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义弹出框提示框
 * Created by chenggang on 2016/8/19.
 */
public class CustomAlertDialog extends DialogFragment {

    /**
     * 自定义提示信息
     */
    private String msgInfo;
    /**
     * 操作动作
     */
    private Action action;
    private AlertDialog dialog;
    public String getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new AlertDialog.Builder(this.getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.alert_dialog, null);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setLayout(900, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button confirmBtn = (Button) layout.findViewById(R.id.confirm_btn);
        ImageView closeBtn=(ImageView) layout.findViewById(R.id.close_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.process();
                dialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                action.close();
            }
        });

        TextView mgs = (TextView) layout.findViewById(R.id.msg);
        mgs.setText(msgInfo);
        return dialog;
    }


    /**
     * 操作动作接口
     */
    public interface Action {
        /**
         * 点击确定按钮后的处理逻辑
         */
        void process();

        /**
         * 点击关闭按钮后的处理逻辑
         */
        void close();
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
