package com.hhyg.TyClosing.di.module;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.GoodRecAdapter;
import com.hhyg.TyClosing.allShop.adapter.HorizontalFilterAdapter;
import com.hhyg.TyClosing.allShop.adapter.PeopertyPopAdapter;
import com.hhyg.TyClosing.apiService.SearchSevice;
import com.hhyg.TyClosing.entities.CommonParam;
import com.hhyg.TyClosing.entities.search.SearchGoodsParam;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by user on 2017/6/9.
 */
@Module
public class SearchGoodsModule {
    private SearchGoodsParam.DataBean beanParam;
    private Context context;
    public SearchGoodsModule(SearchGoodsParam.DataBean beanParam,Context c) {
        this.beanParam = beanParam;
        context = c ;
    }
    @Provides
    SearchGoodsParam provideSearchGoodParam(CommonParam commonParam, SearchGoodsParam.DataBean bean)
    {
        SearchGoodsParam param = new SearchGoodsParam();
        param.setChannel(commonParam.getChannelId());
        param.setImei(commonParam.getImei());
        param.setShopid(commonParam.getShopId());
        param.setPlatformId(commonParam.getPlatformId());
        param.setData(bean);
        return param;
    }

    @Provides
    SearchGoodsParam.DataBean provideBean(){
        SearchGoodsParam.DataBean bean = new SearchGoodsParam.DataBean();
        bean.setPageNo(1);
        bean.setPageSize("100");
        bean.setAvailable("1");
        bean.setSortType("0");
        bean.setClass1Id(beanParam.getClass1Id());
        bean.setClass2Id(beanParam.getClass2Id());
        bean.setClass3Id(beanParam.getClass3Id());
        bean.setBrandId(beanParam.getBrandId());
        bean.setKeyword(beanParam.getKeyword());
        bean.setActivityId(beanParam.getActivityId());
        return bean;
    }

    @Provides
    SearchSevice provideService(@Named("indexApi") Retrofit retrofit){
        return  retrofit.create(SearchSevice.class);
    }

    @Provides
    Gson provideGson(){
        return new Gson();
    }

    @Provides
    @LayoutRes int layoutName(){
        return R.layout.adapter_searchgood;
    }

    @Provides
    GoodRecAdapter provideGoodAdapter(@LayoutRes int layout){
        GoodRecAdapter adapter = new GoodRecAdapter(layout);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        adapter.isFirstOnly(false);
        return adapter;
    }

    @Provides
    MaterialDialog provideDialog(){
        return new MaterialDialog.Builder(context)
                .theme(Theme.LIGHT)
                .iconRes(R.drawable.hhyglogo)
                .content("拼命加载中...")
                .title("请稍后")
                .canceledOnTouchOutside(false)
                .build();
    }

    @Provides
    HorizontalFilterAdapter provideHorizontalAdapter(){
        HorizontalFilterAdapter adapter = new HorizontalFilterAdapter();
        return adapter;
    }

    @Provides
    PeopertyPopAdapter providePopAdapter(){
        return new PeopertyPopAdapter();
    }

}
