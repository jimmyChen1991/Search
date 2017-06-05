package com.hhyg.TyClosing.ui.adapter;

import java.math.BigDecimal;
import java.util.List;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.GoodSku;
import com.hhyg.TyClosing.mgr.RestrictionMgr;
import com.hhyg.TyClosing.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by chenggang on 2016/8/18.
 */
public class AutoSettleOrderAdapter extends BaseAdapter {
    private List<GoodSku> goodSkuList;
    private Context context;
    private LayoutInflater mInflater;
    private ViewHoldr viewHoldr;

    public static class ViewHoldr {
        private ImageView productImg;
        private TextView brandName;
        private TextView productName;
        private TextView psName;
        private TextView count;
        private TextView price;
        private ImageView youshui;
        private  TextView tax;
        private  TextView priceOrign;
    }

    public AutoSettleOrderAdapter(List<GoodSku> goodSkuList, Context context, int layout) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.goodSkuList = goodSkuList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return goodSkuList.size();
    }

    @Override
    public Object getItem(int i) {
        return goodSkuList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            viewHoldr = new ViewHoldr();
            view = mInflater.inflate(R.layout.auto_settle_order_item_detail, null);
            viewHoldr.productName = (TextView) view.findViewById(R.id.productName);
            viewHoldr.productImg=(ImageView)view.findViewById(R.id.pic);
            viewHoldr.brandName=(TextView)view.findViewById(R.id.brandName);
            viewHoldr.psName=(TextView)view.findViewById(R.id.psName);
            viewHoldr.price=(TextView)view.findViewById(R.id.price);
            viewHoldr.count=(TextView)view.findViewById(R.id.product_count);
            viewHoldr.youshui=(ImageView)view.findViewById(R.id.youshui);
            viewHoldr.tax=(TextView)view.findViewById(R.id.tax);
            viewHoldr.priceOrign = (TextView)view.findViewById(R.id.originprice);
            view.setTag(viewHoldr);
        } else {
            viewHoldr = (ViewHoldr) view.getTag();
        }
        GoodSku goodSku=goodSkuList.get(i);
        viewHoldr.productName.setText(goodSku.getGoods_name());
        viewHoldr.brandName.setText(goodSku.getBrand_name());
        viewHoldr.psName.setText(context.getString(R.string.selected)+goodSku.getGoods_attr());
        viewHoldr.price.setText(context.getString(R.string.rmb)+String.format("%.2f",new BigDecimal(goodSku.getGoods_price())));
        viewHoldr.count.setText("X "+goodSku.getNumber());

        if(StringUtil.isEmpty(goodSku.getTax_display_txt()) == false) {
            viewHoldr.tax.setText("商品税: " + goodSku.getTax_display_txt());
            viewHoldr.youshui.setBackgroundResource(R.drawable.youshui);
            viewHoldr.youshui.setVisibility(View.VISIBLE);
            viewHoldr.tax.setVisibility(View.VISIBLE);
        } else {
            viewHoldr.youshui.setVisibility(View.GONE);
            viewHoldr.tax.setVisibility(View.GONE);
        }

//        viewHoldr.youshui.setBackgroundResource(R.drawable.youshui);
//        viewHoldr.youshui.setVisibility(View.VISIBLE);


        if (StringUtil.isEmpty(goodSku.getPrice())) {
            viewHoldr.priceOrign.setVisibility(View.INVISIBLE);
        } else {
            viewHoldr.priceOrign.setVisibility(View.VISIBLE);
            viewHoldr.priceOrign.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
            viewHoldr.priceOrign.setText(goodSku.getPrice());
        }
        
        if(goodSku.goods_img != null && !goodSku.goods_img.equals( viewHoldr.productImg.getTag())){
        	 viewHoldr.productImg.setImageBitmap(null);
		}else if(viewHoldr.productImg.getTag()!=null){
			return view;
		}
        viewHoldr.productImg.setTag(goodSku.goods_img);
		ImageAware imageAware = new ImageViewAware(viewHoldr.productImg, false);
		ImageLoader.getInstance().displayImage(goodSku.goods_img, imageAware, ImageHelper.initBarcodePathOption());

        return view;
    }
}
