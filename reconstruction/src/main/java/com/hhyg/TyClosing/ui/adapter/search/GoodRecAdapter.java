package com.hhyg.TyClosing.ui.adapter.search;


import android.graphics.Paint;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.entities.search.SearchGoods;
import com.hhyg.TyClosing.entities.search.SearchType;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import javax.inject.Inject;

/**
 * Created by chenqiyang on 17/6/11.
 */

public class GoodRecAdapter extends BaseQuickAdapter<SearchGoods.DataBean.GoodsListBean,BaseViewHolder> {
    private SearchType searchType;
    @Inject
    public GoodRecAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public GoodRecAdapter(@LayoutRes int layoutResId, SearchType searchType) {
        super(layoutResId);
        this.searchType = searchType;
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchGoods.DataBean.GoodsListBean item) {
        ActiveInfo aInfo = new ActiveInfo();
        aInfo.setType(item.getActive_type());
        helper.setText(R.id.brandname,item.getBrand_name())
                .setText(R.id.name,item.getName())
                .setVisible(R.id.nostockimg,item.getStock() == 0)
                .setVisible(R.id.privilege_icon,item.isIs_privileged() && searchType != SearchType.PRIVILEGE && searchType != SearchType.ACTIVITY)
                .addOnClickListener(R.id.add);
        TextView indicator = helper.getView(R.id.activite_indicator_text);
        TextView citPrice = helper.getView(R.id.citprice);
        TextView activePrice = helper.getView(R.id.acviteprice);
        if(aInfo.getType() == ActiveInfo.ActiveType.Normal || aInfo.getType() == ActiveInfo.ActiveType.NoStock){
            indicator.setVisibility(View.INVISIBLE);
            citPrice.setVisibility(View.GONE);
            activePrice.setText(Constants.PRICE_TITLE + item.getPrice());
        }else if(aInfo.getType() == ActiveInfo.ActiveType.Cut){
            indicator.setVisibility(View.VISIBLE);
            indicator.setText(item.getActive_detail());
            activePrice.setText(Constants.PRICE_TITLE + item.getPrice());
            citPrice.setVisibility(View.VISIBLE);
            citPrice.setText(Constants.PRICE_TITLE + item.getMianshui_price());
            citPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            citPrice.getPaint().setAntiAlias(true);
        }else{
            activePrice.setText(Constants.PRICE_TITLE+item.getPrice());
            citPrice.setVisibility(View.GONE);
            indicator.setVisibility(View.VISIBLE);
            indicator.setText(item.getActive_detail());
        }
        Button addToshop = helper.getView(R.id.add);
        if(searchType == SearchType.ACTIVITY || searchType == SearchType.PRIVILEGE){
            addToshop.setVisibility(View.VISIBLE);
        }
        final String tag = (String) helper.getView(R.id.goodimg).getTag();
        final String uri = item.getImage();
        if(tag != null && !uri.equals(tag)){
            ImageView s = (ImageView)helper.getView(R.id.goodimg);
            s.setImageBitmap(null);
        }else if(tag != null){
            return;
        }
        helper.getView(R.id.goodimg).setTag(uri);
        ImageAware imageAware = new ImageViewAware((ImageView) helper.getView(R.id.goodimg), false);
        ImageLoader.getInstance().displayImage(item.getImage(), imageAware, ImageHelper.initBarcodePathOption());
    }
}
