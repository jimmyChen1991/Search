package com.hhyg.TyClosing.di.module;

import android.util.Log;

import com.hhyg.TyClosing.global.MyApplication;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    Retrofit provideRetrofit(Converter.Factory convertFtry, CallAdapter.Factory adapterFtry,OkHttpClient client,@Named("serviceUrl") String url){
        return new Retrofit.Builder().baseUrl(url).client(client).addConverterFactory(convertFtry).addCallAdapterFactory(adapterFtry).build();
    }

    @Provides
    @Singleton
    @Named("indexApi")
    Retrofit provideRetrofit_IndexApi(Converter.Factory convertFtry, CallAdapter.Factory adapterFtry,OkHttpClient client,@Named("indexUrl") String url){
        return new Retrofit.Builder().baseUrl(url).client(client).addConverterFactory(convertFtry).addCallAdapterFactory(adapterFtry).build();
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
        return "http://search.mianshui365.net/";
    }

    @Provides
    @Named("indexUrl")
    String provideIndexApiUrlStr(){
        return "http://search_commonapi.mianshui365.net/";
    }

    @Provides
    OkHttpClient provideOkClient(HttpLoggingInterceptor interceptor){
        return new OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(5000, TimeUnit.MILLISECONDS).readTimeout(5000,TimeUnit.MILLISECONDS).build();
    }

    @Provides
    HttpLoggingInterceptor provideLogginInterceptor(HttpLoggingInterceptor.Logger l){
        HttpLoggingInterceptor interceptor =  new HttpLoggingInterceptor(l);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return  interceptor;
    }

    @Provides
    HttpLoggingInterceptor.Logger provideLogger(){
        return new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.v("httpLogger",message);
            }
        };
    }
}
