package com.hhyg.TyClosing.ui.dialog;

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
import com.hhyg.TyClosing.R;

import java.sql.BatchUpdateException;

public class CustomGoodsTimeDialog extends DialogFragment {
    protected String msgInfo;
    protected String confirmBtnText;
    protected String cancelBtnText;
    protected CustomConfirmDialog.Action action;
    protected AlertDialog dialog;
    public String getMsgInfo() {
        return msgInfo;
    }
    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }
    public String airName = "";

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new AlertDialog.Builder(this.getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.customgoodstimedialog, null);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setLayout(1200, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button confirmBtn = (Button) layout.findViewById(R.id.confirm_btn);
        Button cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);
        ImageView closeBtn=(ImageView) layout.findViewById(R.id.close_btn);

        TextView title = (TextView) layout.findViewById(R.id.title);
        title.setText(airName + "提货说明");

        Button btnorderTime11 = (Button) layout.findViewById(R.id.btn1);
        Button btnorderTime12 = (Button) layout.findViewById(R.id.btn3);
        Button btnorderTime13 = (Button) layout.findViewById(R.id.btn5);
        Button btnorderTime14 = (Button) layout.findViewById(R.id.btn7);

        Button btnTime11 = (Button) layout.findViewById(R.id.btn2);
        Button btnTime12 = (Button) layout.findViewById(R.id.btn4);
        Button btnTime13 = (Button) layout.findViewById(R.id.btn6);
        Button btnTime14 = (Button) layout.findViewById(R.id.btn8);

        Button btnorderTime21 = (Button) layout.findViewById(R.id.btn21);
        Button btnorderTime23 = (Button) layout.findViewById(R.id.btn23);
        Button btnTime21 = (Button) layout.findViewById(R.id.btn22);
        Button btnTime23 = (Button) layout.findViewById(R.id.btn24);

        btnorderTime21.setText("0:00至21:00之间下单");
        btnorderTime23.setText("21:00至23:59之间下单");

        if(airName.contains("三亚")){
            btnorderTime11.setText("6:00至21:00之间下单");
            btnorderTime12.setText("21:00至次日6:00之间下单");
            btnTime11.setText("最快2小时可提货");
            btnTime12.setText("最快次日8:00可提货");

            btnTime21.setText("最快次日6:00可提货");
            btnTime23.setText("最快第三日6:00可提货");

            btnTime13.setVisibility(View.GONE);
            btnorderTime13.setVisibility(View.GONE);
            btnTime14.setVisibility(View.GONE);
            btnorderTime14.setVisibility(View.GONE);
        }
        else {
            btnorderTime11.setText("6:00至17:00之间下单");
            btnorderTime12.setText("17:00至21:00之间下单");
            btnorderTime13.setText("21:00至23:59之间下单");
            btnorderTime14.setText("00:00至次日6:00之间下单");

            btnTime11.setText("最快6小时可提货");
            btnTime12.setText("最快次日6:00可提货");
            btnTime13.setText("最快次日12:00可提货");
            btnTime14.setText("最快次日12:00可提货");

            btnTime21.setText("最快次日18:00可提货");
            btnTime23.setText("最快第三日18:00可提货");
        }

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(action != null)
                    action.process();
                dialog.dismiss();
            }
        });
        return dialog;
    }
    public interface Action {
        void process();
        void cancel();
        void close();
    }
    public CustomConfirmDialog.Action getAction() {
        return action;
    }
    public void setAction(CustomConfirmDialog.Action action) {
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
