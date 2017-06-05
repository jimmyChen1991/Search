package com.hhyg.TyClosing.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.info.GoodSku;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.hhyg.TyClosing.ui.OrderConformActivity;
import com.hhyg.TyClosing.ui.adapter.AutoSettleOrderAdapter;

import java.util.List;

/**
 * Created by chenggang on 2016/8/18.
 */
public class AutoSettleOrderItemsFragment extends Fragment {
    private ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();
    private View view;
    private boolean inited = true;
    private ListView itemListView;
    private List<ShoppingCartInfo> data;
    private LayoutInflater mInflater;
    private ViewGroup mContainer;
    private View mView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       if (inited) {
           mView = inflater.inflate(R.layout.auto_settle_order_items, container);
           mInflater = inflater;
           mContainer = container;
        }
        return mView;
    }

    public void initData(){
        String data = ((OrderConformActivity) getActivity()).getObGoods().toString();
        List<GoodSku> dataList = JSONObject.parseArray(data, GoodSku.class);
        itemListView = (ListView) mView.findViewById(R.id.order_item_list);
        AutoSettleOrderAdapter autoSettleOrderAdapter = new AutoSettleOrderAdapter(dataList, getActivity(), R.layout.auto_settle_order_item_detail);
        itemListView.setAdapter(autoSettleOrderAdapter);
    }
}
