package com.hhyg.TyClosing.allShop.adapter;


import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
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
        helper.addOnClickListener(R.id.filter);
        Button filter = helper.getView(R.id.filter);
        if(!item.isSelected()){
            filter.setText(item.getName());
            filter.setTextColor(Constants.UNSELECTOR_COLOR);
        }else{
            filter.setText(item.getSelectedName());
            filter.setTextColor(Constants.SELECTOR_COLOR);
        }
    }
}
