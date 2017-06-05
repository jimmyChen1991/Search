package com.hhyg.TyClosing.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;

import java.util.ArrayList;
import java.util.List;

public class QuotaQueryDialog extends CustomConfirmDialog {
    protected JSONObject mObInfo = null;

    public void setMsgInfo(com.alibaba.fastjson.JSONObject object) {
        this.mObInfo = object;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new AlertDialog.Builder(this.getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(this.getActivity());
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.quotaquery, null);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setLayout(1200, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Button confirmBtn = (Button) layout.findViewById(R.id.confirm_btn);
        Button cancelBtn = (Button) layout.findViewById(R.id.cancel_btn);
        ImageView closeBtn=(ImageView) layout.findViewById(R.id.close_btn);

        List<String> list = new ArrayList();
        TextView mgs = (TextView) layout.findViewById(R.id.text21);
        list.add("·发证机关: ");
        mgs.setText(list.get(0) + this.mObInfo.getString("organization"));
        setTextColor(mgs,list);
        list.clear();

        mgs = (TextView) layout.findViewById(R.id.text31);
        list.add("·本年度消费额度: ");
        list.add("·本年度已购买征税商品数量: ");
        mgs.setText(list.get(0) + this.mObInfo.getString("limit_amount") + "\r\n" + list.get(1) + this.mObInfo.getString("taxgoods_count_year"));
        setTextColor(mgs,list);
        list.clear();

        mgs = (TextView) layout.findViewById(R.id.text32);
        list.add("·本年剩余免税额度: ");
        mgs.setText(list.get(0) + this.mObInfo.getString("residua_amount"));
        setTextColor(mgs,list);
        list.clear();

        TextView txt41 = (TextView) layout.findViewById(R.id.text41);
        TextView txt42 = (TextView) layout.findViewById(R.id.text42);

        JSONArray arr =  this.mObInfo.getJSONArray("cit_count_time");
        if(arr == null || arr.size() == 0){
            txt41.setVisibility(View.GONE);
            txt42.setVisibility(View.GONE);
            layout.findViewById(R.id.title4).setVisibility(View.GONE);
        }
        else {
            String strRow1 = "";
            String strRow2 = "";
            List<String> list1 = new ArrayList();
            List<String> list2 = new ArrayList();
            for (int i = 0; i < arr.size(); i++) {
                JSONObject ob = arr.getJSONObject(i);
                List<String> l = i % 2 == 0 ? list1 : list2;
                String s = "·" + ob.getString("cit_name");
                if(i % 2 == 0)
                    strRow1 +=  s + " : " + ob.getString("remark") + "\n";
                else
                    strRow2 +=  s + " : " + ob.getString("remark") + "\n";
                l.add(s);
            }
            txt41.setText(strRow1);
            setTextColor(txt41,list1);
            txt42.setText(strRow2);
            setTextColor(txt42,list2);
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


    public void setTextColor( TextView textView, List<String> listColored){
        String strAllText = textView.getText().toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(strAllText);
        for (int i = 0; i < listColored.size(); i++){
            String str = listColored.get(i);
            int nIndex = strAllText.indexOf(str);
            if(nIndex != -1){
                ForegroundColorSpan redSpan = new ForegroundColorSpan(android.graphics.Color.parseColor("#666666"));
                builder.setSpan(redSpan, nIndex,nIndex + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(builder);
    }
}
