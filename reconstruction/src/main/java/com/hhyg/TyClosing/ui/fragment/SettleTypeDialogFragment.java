package com.hhyg.TyClosing.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hhyg.TyClosing.R;

/**
 * 结算方式DIALOG
 * Created by chenggang on 2016/8/18.
 */
public class SettleTypeDialogFragment extends DialogFragment {
    private Dialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this.getActivity()).create();
            LayoutInflater inflater = LayoutInflater.from(this.getActivity());
            RelativeLayout layoutt = (RelativeLayout) inflater.inflate(R.layout.settle_type_dialog, null);
            dialog.show();
            dialog.getWindow().setContentView(layoutt);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            dialog.getWindow().setLayout(900, RelativeLayout.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            //自主结算按钮
            final Button autoBtn = (Button) layoutt.findViewById(R.id.auto_settle_btn);
            //服务台结算按钮
            final Button serverBtn = (Button) layoutt.findViewById(R.id.server_settle_btn);

            ImageView closeImage=(ImageView)layoutt.findViewById(R.id.close_btn);
            TextView autoTextView=(TextView)layoutt.findViewById(R.id.auto_settle_text);
            TextView serverTextView =(TextView)layoutt.findViewById(R.id.server_settle_text);
            autoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ISettleTypeListener settleTypeListener = (ISettleTypeListener) getActivity();
                    settleTypeListener.autoSettle();
                }
            });
            SpannableString autoStyledText = new SpannableString(autoTextView.getText());
            autoStyledText.setSpan(new ForegroundColorSpan(Color.rgb(51,51,51)),0,7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            autoTextView.setText(autoStyledText);

            SpannableString serverStyledText = new SpannableString(serverTextView.getText());
            serverStyledText.setSpan(new ForegroundColorSpan(Color.rgb(51,51,51)),0,8, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            serverTextView.setText(serverStyledText);

            serverBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ISettleTypeListener settleTypeListener = (ISettleTypeListener) getActivity();
                    settleTypeListener.serverSettler();
                }
            });

            closeImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }

        return dialog;
    }

    public interface ISettleTypeListener {
        /**
         * 自主结算
         */
        void autoSettle();

        /**
         * 服务台结算
         */
        void serverSettler();

    }
}
