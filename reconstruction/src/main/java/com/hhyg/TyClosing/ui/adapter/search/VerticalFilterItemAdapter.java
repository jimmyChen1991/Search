package com.hhyg.TyClosing.ui.adapter.search;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.entities.search.FilterItem;

/**
 * Created by user on 2017/6/16.
 */

public class VerticalFilterItemAdapter extends BaseQuickAdapter<FilterItem,BaseViewHolder>{

    public VerticalFilterItemAdapter() {
        super(R.layout.adapter_filteritem);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterItem item) {

    }
}
