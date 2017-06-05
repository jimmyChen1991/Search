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
 * 自定义确认框
 * Created by chenggang on 2016/8/19.
 */
public class CustomConfirmDialog extends DialogFragment {

    /**
     * 自定义提示信息
     */
    protected String msgInfo;
    /**
     * 第一个按钮的文字
     */
    protected String confirmBtnText;
    /**
     * 第二个按钮的文字
     */
    protected String cancelBtnText;
    /**
     * 操作动作
     */
    protected Action action;
    protected AlertDialog dialog;

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
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.confirm_dialog, null);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setLayout(900, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button confirmBtn = (Button) layout.findViewById(R.id.confirm_btn);
        Button cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);
        ImageView closeBtn=(ImageView) layout.findViewById(R.id.close_btn);



        //提示信息
        TextView mgs = (TextView) layout.findViewById(R.id.msg);
        mgs.setText(msgInfo);

        //按钮1文字
        if (this.confirmBtnText != null) {
            confirmBtn.setText(this.confirmBtnText);
        }
        //按钮2文字
        if (this.cancelBtnText != null) {
            cancelBtn.setText(this.cancelBtnText);
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.process();
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.cancel();
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
         * 点击取消按钮后的处理逻辑
         */
        void cancel();

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

    public String getConfirmBtnText() {
        return confirmBtnText;
    }

    public void setConfirmBtnText(String confirmBtnText) {
        this.confirmBtnText = confirmBtnText;
    }

    public String getCancelBtnText() {
        return cancelBtnText;
    }

    public void setCancelBtnText(String cancelBtnText) {
        this.cancelBtnText = cancelBtnText;
    }
}
