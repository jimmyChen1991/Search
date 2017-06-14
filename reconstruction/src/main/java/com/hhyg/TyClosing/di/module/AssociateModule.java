package com.hhyg.TyClosing.di.module;

import com.google.gson.Gson;
import com.hhyg.TyClosing.apiService.AssociateSevice;
import com.hhyg.TyClosing.entities.CommonParam;
import com.hhyg.TyClosing.entities.associate.AssociateParam;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by user on 2017/6/14.
 */
@Module
public class AssociateModule {

    @Provides
    AssociateSevice provideService(@Named("indexApi") Retrofit retrofit){
        return retrofit.create(AssociateSevice.class);
    }

    @Provides
    AssociateParam provideParam(CommonParam commonParam){
        AssociateParam param = new AssociateParam();
        param.setChannel(commonParam.getChannelId());
        param.setImei(commonParam.getImei());
        param.setPlatformId(commonParam.getPlatformId());
        param.setShopid(commonParam.getShopId());
        return param;
    }

    @Provides
    Gson provideGson(){
        return new Gson();
    }
}
