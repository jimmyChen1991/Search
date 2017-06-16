package com.hhyg.TyClosing.ui.adapter.search;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.entities.associate.AssociateRes;

import javax.inject.Inject;

/**
 * Created by user on 2017/6/14.
 */

public class AssociateAdapter extends BaseQuickAdapter<AssociateRes.DataBean,BaseViewHolder>{
    @Inject
    public AssociateAdapter() {
        super(R.layout.adapter_associate);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssociateRes.DataBean item) {
        helper.setText(R.id.name,item.getName())
                .setText(R.id.count,"约" + item.getCount() + "个商品");
    }
}
