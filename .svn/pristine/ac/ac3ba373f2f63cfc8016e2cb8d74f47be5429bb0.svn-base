package com.hhyg.TyClosing.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.PickUpInfo;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;

/**
 * Created by mjf on 16/9/13.
 */
public class OrderConfirmDialog extends CustomConfirmDialog {
    protected JSONObject mObInfo = null;
    private String mTime;

    public void setMsgInfo(String msgInfo) {
        JSONObject object = JSON.parseObject(msgInfo);
        this.mObInfo = object;
    }

    public void setTime(String str){
        mTime = str;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new AlertDialog.Builder(this.getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.orderconform_dialog, null);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setLayout(1200, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button confirmBtn = (Button) layout.findViewById(R.id.confirm_btn);
        Button cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);
        ImageView closeBtn=(ImageView) layout.findViewById(R.id.close_btn);

        PickUpInfo info =  MyApplication.GetInstance().getUserSelectAir();
        String str = info.name;

        String salerInfo = ClosingRefInfoMgr.getInstance().getSalerName() +  "  " +  ClosingRefInfoMgr.getInstance().getUserName();

        //提示信息
        TextView mgs = (TextView) layout.findViewById(R.id.msg1);
        str = "离岛人姓名:" + mObInfo.getString("userName") + "\r\n" + "离岛机场:" + str + "\r\n" + "离岛航班:" +
                mObInfo.getString("flightNum") + "\r\n" + "提货时间:" + mTime + "\r\n";
        mgs.setText(str);


        mgs = (TextView) layout.findViewById(R.id.msg2);
        str = "离岛人身份证:" + mObInfo.getString("idCard") + "\r\n" + "离岛时间:" + mObInfo.getString("flightDate") + "\r\n" +
                "手机号码:" + mObInfo.getString("phone") + "\r\n" + "销售员:" + salerInfo + "\r\n";
        ;
        mgs.setText(str);

        mgs = (TextView) layout.findViewById(R.id.msg3);
        str = "1.请确认您填写的以上信息与实际收货人一致" + "" + "\r\n" + "2.如因个人原因无法退货,只做全额退款处理,货款返还时间为15个工作日";
        mgs.setText(str);

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

        return dialog;
    }
}
