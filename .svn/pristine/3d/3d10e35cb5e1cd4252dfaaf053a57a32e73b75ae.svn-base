package com.hhyg.TyClosing.ui.adapter;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.info.OrderInfo;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.ui.HistoryOrderActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class HistoryOrderAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private ArrayList<OrderInfo> mOrderList = new ArrayList<OrderInfo>();
    private Context mContext = null;
    public HistoryOrderAdapter(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    public void setData(ArrayList<OrderInfo> arr){
        mOrderList = arr;
        this.notifyDataSetChanged();
    }

    class ViewHolder {
        public TextView orderId;
        public ImageView splitImg;
        public LinearLayout imgGroup;
        public TextView priceText;
        public Button btnToPay;
    }

    @Override public int getCount() {return mOrderList.size();}
    @Override public Object getItem(int position) {return null;}
    @Override public long getItemId(int position) {return 0;}

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        OrderInfo info = mOrderList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.order_item, null);
            viewHolder = new ViewHolder();
            viewHolder.orderId = (TextView) convertView.findViewById(R.id.orderid);
            viewHolder.splitImg = (ImageView) convertView.findViewById(R.id.orderlistitemsplit0);
            viewHolder.imgGroup = (LinearLayout) convertView.findViewById(R.id.imggroup);
            viewHolder.priceText = (TextView) convertView.findViewById(R.id.orderprice);
            viewHolder.btnToPay = (Button) convertView.findViewById(R.id.confirm_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final OrderInfo ainfo = info;
        convertView.setOnClickListener(new  View.OnClickListener() {
            @Override public void onClick(View v) {
                ((HistoryOrderActivity)mContext).seeGoodsDetail(ainfo);
            }
        });

        ArrayList<ShoppingCartInfo> shop = ((HistoryOrderActivity)mContext).getAllShopByOrderId(info.orderId);
        viewHolder.orderId.setText("订单流水号: " + info.orderId);
        viewHolder.splitImg.setVisibility(View.VISIBLE);
        setImgGroup(shop, viewHolder.imgGroup);
        String totalcast = String.format("%.2f", info.totalCast);
        viewHolder.priceText.setText("共" + info.totalCnt + "件商品，合计:¥" + totalcast);
        if (viewHolder.btnToPay != null) {
            viewHolder.btnToPay.setText(info.status);
        }
        return convertView;
    }

    private void setImgGroup(ArrayList<ShoppingCartInfo> list, LinearLayout group) {
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(200, 200);
        ll.setMargins(10, 0, 20, 0);
        group.removeAllViews();
        int count = 0;
        if (list.size() > 5) {
            count = 5;
        } else {
            count = list.size();
        }
        for (int idx = 0; idx < count; idx++) {
            final ShoppingCartInfo Sinfo = list.get(idx);
            ImageView img = new ImageView(mContext);
            img.setLayoutParams(ll);
            ImageAware imageAware = new ImageViewAware(img, false);
    		ImageLoader.getInstance().displayImage(Sinfo.imgUrl, imageAware, ImageHelper.initBarcodePathOption());
            group.addView(img);
        }
        if (list.size() > 5) {
            Bitmap btm = ((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.order_more)).getBitmap();
            ImageView img = new ImageView(mContext);
            img.setLayoutParams(ll);
            img.setImageBitmap(btm);
            group.addView(img);
        }
    }
}