package com.hhyg.TyClosing.ui.adapter.search;


import android.support.annotation.NonNull;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.entities.search.FilterBean;
import com.hhyg.TyClosing.entities.search.FilterItem;

/**
 * Created by user on 2017/6/13.
 */

public class PeopertyPopAdapter extends BaseQuickAdapter<FilterItem,BaseViewHolder>{
    private FilterBean data;
    public PeopertyPopAdapter() {
        super(R.layout.adapter_peopertypop);
    }
    @Override
    protected void convert(BaseViewHolder helper, FilterItem item) {
        helper.setText(R.id.name,item.getName())
                .setVisible(R.id.selected_icon,item.isSelected());
        TextView tv = helper.getView(R.id.name);
        if(item.isSelected()){
            tv.setTextColor(Constants.SELECTOR_COLOR);
        }else{
            tv.setTextColor(Constants.UNSELECTOR_COLOR);
        }
    }

    public void setFilterData(FilterBean data) {
        this.data = data;
        setNewData(data.getDataSet());
    }

    public FilterBean getFilterData() {
        return data;
    }
}
