package com.hhyg.TyClosing.di.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 2017/6/7.
 */
@Module
public class NetModule {
    @Provides
    @Singleton
    @Named("serviceApi")
    Retrofit provideRetrofit(Converter.Factory convertFtry, CallAdapter.Factory adapterFtry,@Named("serviceUrl") String url){
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(convertFtry).addCallAdapterFactory(adapterFtry).build();
    }

    @Provides
    @Singleton
    @Named("indexApi")
    Retrofit provideRetrofit_IndexApi(Converter.Factory convertFtry, CallAdapter.Factory adapterFtry,@Named("indexUrl") String url){
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(convertFtry).addCallAdapterFactory(adapterFtry).build();
    }

    @Provides
    Converter.Factory provideGsonConvertFactory(){
        return GsonConverterFactory.create();
    }

    @Provides
    CallAdapter.Factory provideRxJava2AdapterFactory(){
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @Named("serviceUrl")
    String provideServiceApiUrlStr(){
        return "search.mianshui365.net/";
    }

    @Provides
    @Named("indexUrl")
    String provideIndexApiUrlStr(){
        return "search_commonapi.mianshui365.net/";
    }
}
