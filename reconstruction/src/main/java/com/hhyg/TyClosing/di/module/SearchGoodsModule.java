package com.hhyg.TyClosing.di.module;

import com.google.gson.Gson;
import com.hhyg.TyClosing.apiService.SearchSevice;
import com.hhyg.TyClosing.entities.CommonParam;
import com.hhyg.TyClosing.entities.SearchGoodsParam;


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
    public SearchGoodsModule(SearchGoodsParam.DataBean beanParam) {
        this.beanParam = beanParam;
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
        bean.setPageNo("1");
        bean.setAvailable("1");
        bean.setClass1Id(beanParam.getClass1Id());
        bean.setClass2Id(beanParam.getClass2Id());
        bean.setClass3Id(beanParam.getClass3Id());
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
}
