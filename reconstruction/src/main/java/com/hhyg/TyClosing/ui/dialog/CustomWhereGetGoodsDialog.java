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

/**
 * Created by mjf on 2016/10/13.
 */
public class CustomWhereGetGoodsDialog extends DialogFragment {
    protected String msgInfo;
    protected String confirmBtnText;
    protected String cancelBtnText;
    protected CustomWhereGetGoodsDialog.Action action;
    protected AlertDialog dialog;
    private int index = 0;
    public String getMsgInfo() {
        return msgInfo;
    }
    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }

    @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new AlertDialog.Builder(this.getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.customwheregetgoodsdialog, null);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setLayout(900, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button confirmBtn = (Button) layout.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                action.process("" + index);
                dialog.dismiss();
            }
        });

        Button btn = (Button) layout.findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                index = 0;
            }
        });

        btn = (Button) layout.findViewById(R.id.btn2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                index = 1;
            }
        });
        return dialog;
    }

    public interface Action {
        void process(String strTag);
        void cancel();
        void close();
    }
    public CustomWhereGetGoodsDialog.Action getAction() {
        return action;
    }
    public void setAction(CustomWhereGetGoodsDialog.Action action) {
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
