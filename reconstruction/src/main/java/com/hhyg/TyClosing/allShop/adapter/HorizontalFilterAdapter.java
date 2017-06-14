package com.hhyg.TyClosing.allShop.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.entities.search.FilterBean;

/**
 * Created by user on 2017/6/12.
 */

public class HorizontalFilterAdapter extends BaseQuickAdapter<FilterBean,BaseViewHolder>{
    public HorizontalFilterAdapter() {
        super(R.layout.adapter_horizontalfilter);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterBean item) {
        helper.setText(R.id.filter,item.getName())
                .addOnClickListener(R.id.filter);
        helper.getView(R.id.filter).setTag(item.getDataSet());
    }
}
