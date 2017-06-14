package com.hhyg.TyClosing.allShop.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.entities.search.FilterItem;

/**
 * Created by user on 2017/6/13.
 */

public class PeopertyPopAdapter extends BaseQuickAdapter<FilterItem,BaseViewHolder>{
    public PeopertyPopAdapter() {
        super(R.layout.adapter_peopertypop);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterItem item) {
        helper.setText(R.id.name,item.getName());
    }
}
