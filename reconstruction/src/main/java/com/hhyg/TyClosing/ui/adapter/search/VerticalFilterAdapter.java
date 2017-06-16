package com.hhyg.TyClosing.ui.adapter.search;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.entities.search.FilterBean;

/**
 * Created by user on 2017/6/16.
 */

public class VerticalFilterAdapter extends BaseQuickAdapter<FilterBean,BaseViewHolder> {

    public VerticalFilterAdapter() {
        super(R.layout.adapter_verticalfilter);
    }

    @Override
    protected void convert(BaseViewHolder helper, FilterBean item) {
        helper.setText(R.id.filterName,item.getName())
                .setText(R.id.selectName,item.getSelectedName());
        if(item.isShowNow()){
            helper.itemView.setBackgroundResource(R.drawable.catebg);
        }else{
            helper.itemView.setBackgroundColor(Constants.GRAY_COLOR);
        }
    }

    //need notifyDataSetChanged
    public void clearSelectStatus(){
        for (FilterBean bean : getData()){
            bean.setShowNow(false);
        }
    }
}
