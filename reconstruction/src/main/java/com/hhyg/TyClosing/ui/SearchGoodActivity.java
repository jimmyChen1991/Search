package com.hhyg.TyClosing.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.gson.Gson;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.GoodRecAdapter;
import com.hhyg.TyClosing.allShop.adapter.HorizontalFilterAdapter;
import com.hhyg.TyClosing.allShop.adapter.PeopertyPopAdapter;
import com.hhyg.TyClosing.apiService.SearchSevice;
import com.hhyg.TyClosing.di.componet.DaggerCommonNetParamComponent;
import com.hhyg.TyClosing.di.componet.DaggerSearchGoodComponent;
import com.hhyg.TyClosing.di.module.CommonNetParamModule;
import com.hhyg.TyClosing.di.module.SearchGoodsModule;
import com.hhyg.TyClosing.entities.search.FilterBean;
import com.hhyg.TyClosing.entities.search.FilterItem;
import com.hhyg.TyClosing.entities.search.FilterType;
import com.hhyg.TyClosing.entities.search.SearchFilterRes;
import com.hhyg.TyClosing.entities.search.SearchGoods;
import com.hhyg.TyClosing.entities.search.SearchGoodsParam;
import com.hhyg.TyClosing.entities.search.SearchType;
import com.hhyg.TyClosing.exceptions.ServiceDataException;
import com.hhyg.TyClosing.exceptions.ServiceMsgException;
import com.hhyg.TyClosing.global.MyApplication;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by user on 2017/6/7.
 */

public class SearchGoodActivity extends AppCompatActivity {
    private static final String TAG = "SearchGoodActivity";
    @BindView(R.id.backbtn)
    ImageButton backbtn;
    @BindView(R.id.searchtitle)
    TextView searchtitle;
    @BindView(R.id.searchbar)
    TextView searchbar;
    @BindString(R.string.search_token)
    String seach_token;
    @Inject
    SearchGoodsParam param_use;
    @Inject
    SearchGoodsParam param;
    @Inject
    SearchGoodsParam param_raw;
    @Inject
    SearchSevice searchSevice;
    @Inject
    GoodRecAdapter goodRecAdapter;
    @Inject
    HorizontalFilterAdapter horizontalFilterAdapter;
    @Inject
    PeopertyPopAdapter popAdapter;
    @Inject
    Gson gson;
    @Inject
    MaterialDialog dialog;
    @BindView(R.id.chosehotsale)
    ImageButton chosehotsale;
    @BindView(R.id.chosenew)
    ImageButton chosenew;
    @BindView(R.id.choseprice)
    ImageButton choseprice;
    @BindView(R.id.tochosebtn)
    ImageButton tochosebtn;
    @BindView(R.id.attr_group_wrap)
    RecyclerView attrGroupWrap;
    @BindView(R.id.goods_wrap)
    RecyclerView goodsWrap;
    @BindView(R.id.backtotop)
    ImageButton backtotop;
    @BindView(R.id.backtomain)
    ImageButton backtomain;
    @BindView(R.id.chosengerenal)
    ImageButton chosengerenal;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    int totalPage;
    SearchType type = SearchType.KEY_WORD;
    CustomPopWindow popWindow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchgood);
        ButterKnife.bind(this);
        searchtitle.setText(getIntent().getStringExtra(getString(R.string.search_content)));
        goodsWrap.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        goodsWrap.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        goodsWrap.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        goodsWrap.setHasFixedSize(true);
        attrGroupWrap.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        attrGroupWrap.setHasFixedSize(true);
        DaggerSearchGoodComponent.builder()
                .applicationComponent(MyApplication.GetInstance().getComponent())
                .commonNetParamComponent(DaggerCommonNetParamComponent.builder().commonNetParamModule(new CommonNetParamModule()).build())
                .searchGoodsModule(new SearchGoodsModule((SearchGoodsParam.DataBean) getIntent().getParcelableExtra(seach_token), this))
                .build().inject(this);
        goodsWrap.setAdapter(goodRecAdapter);
        goodRecAdapter.bindToRecyclerView(goodsWrap);
        attrGroupWrap.setAdapter(horizontalFilterAdapter);
        goodRecAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent it = new Intent(SearchGoodActivity.this, GoodsInfoActivity.class);
                it.putExtra("barcode", (String) view.findViewById(R.id.name).getTag());
                startActivity(it);
            }
        });
        View popContent = LayoutInflater.from(this).inflate(R.layout.popwindow_peoperty,null);
        RecyclerView recyclerView = (RecyclerView) popContent.findViewById(R.id.pop_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(popAdapter);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        popWindow = new CustomPopWindow
                .PopupWindowBuilder(this)
                .setView(popContent)
                .setFocusable(true)
                .setOutsideTouchable(true)
                .size(metrics.widthPixels,(metrics.heightPixels/5)*2)
                .create();
        horizontalFilterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.v(TAG,"add");
                popAdapter.setNewData((ArrayList<FilterItem>) view.getTag());
                popWindow.showAsDropDown(attrGroupWrap,0,0);
            }
        });
        goodRecAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (param.getData().getPageNo() >= totalPage) {
                    goodRecAdapter.loadMoreEnd();
                } else {
                    param.getData().setPageNo(param.getData().getPageNo() + 1);
                    searchSevice.searchGoodsApi(gson.toJson(param))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<SearchGoods>() {
                                @Override
                                public void accept(@NonNull SearchGoods searchGoods) throws Exception {
                                    goodRecAdapter.addData(searchGoods.getData().getGoodsList());
                                    goodRecAdapter.loadMoreComplete();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(@NonNull Throwable throwable) throws Exception {
                                    param.getData().setPageNo(param.getData().getPageNo() - 1);
                                    goodRecAdapter.loadMoreFail();
                                }
                            });
                }
            }
        }, goodsWrap);
        searchSevice.searchGoodsApi(gson.toJson(param))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchGoods>() {
                    @Override
                    public void accept(@NonNull SearchGoods searchGoods) throws Exception {
                        totalPage = searchGoods.getData().getTotalPages();
                        if (searchGoods.getData().getGoodsList().size() == 0) {
                            goodRecAdapter.setEmptyView(R.layout.empty_view);
                        } else {
                            goodRecAdapter.addData(searchGoods.getData().getGoodsList());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.v(TAG, throwable.toString());
                    }
                });
        Observable.just(type)
                .filter(new Predicate<SearchType>() {
                    @Override
                    public boolean test(@NonNull SearchType searchType) throws Exception {
                        return searchType == SearchType.KEY_WORD;
                    }
                })
                .map(new Function<SearchType, SearchGoodsParam>() {
                    @Override
                    public SearchGoodsParam apply(@NonNull SearchType searchType) throws Exception {
                        return param;
                    }
                })
                .flatMap(new Function<SearchGoodsParam, ObservableSource<SearchGoods>>(){
                    @Override
                    public ObservableSource<SearchGoods> apply(@NonNull SearchGoodsParam searchGoodsParam) throws Exception {
                        return searchSevice.searchGoodsApi(gson.toJson(searchGoodsParam));
                    }
                })
                .map(new Function<SearchGoods, SearchGoodsParam>() {
                    @Override
                    public SearchGoodsParam apply(@NonNull SearchGoods searchGoods) throws Exception {
                        param.getData().setKeyword(searchGoods.getData().getSearchKey());
                        return param;
                    }
                })
                .flatMap(new Function<SearchGoodsParam, ObservableSource<SearchFilterRes>>() {
                    @Override
                    public ObservableSource<SearchFilterRes> apply(@NonNull SearchGoodsParam searchGoodsParam) throws Exception {
                        return searchSevice.searchFilterApi(gson.toJson(searchGoodsParam));
                    }
                })
                .map(new Function<SearchFilterRes, ArrayList<FilterBean>>() {
                    @Override
                    public ArrayList<FilterBean> apply(@NonNull SearchFilterRes searchFilterRes) throws Exception {
                        return new FilterHelper(searchFilterRes).invoke();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<FilterBean>>() {
                    @Override
                    public void accept(@NonNull ArrayList<FilterBean> filterBeen) throws Exception {
                        Log.v(TAG,filterBeen.get(0).toString());
                        horizontalFilterAdapter.addData(filterBeen);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.v(TAG,throwable.toString());
                    }
                });
        searchSevice.searchFilterApi(gson.toJson(param))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @OnClick(R.id.backbtn)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.searchbar)
    public void onViewClick() {
        Intent it = new Intent(this, SearchActivity.class);
        startActivity(it);
    }

    @OnClick({R.id.chosengerenal, R.id.chosehotsale, R.id.chosenew, R.id.choseprice})
    public void onViewClicked(final View view){
        SearchGoodsParam.DataBean bean = null;
        try {
            bean = (SearchGoodsParam.DataBean) param.getData().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        switch (view.getId()) {
            case R.id.chosengerenal:
                bean.setSortType("0");
                break;
            case R.id.chosehotsale:
                bean.setSortType("1");
                break;
            case R.id.chosenew:
                bean.setSortType("2");
                break;
            case R.id.choseprice:
                if(bean.getSortType().equals("3")){
                    bean.setSortType("-3");
                }else{
                    bean.setSortType("3");
                }
                break;
        }
        try {
            param_use = (SearchGoodsParam) param.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        bean.setPageNo(1);
        param_use.setData(bean);
        Log.v(TAG,param.toString());
        Log.v(TAG,param_use.toString());
        Observable.just(param_use)
                .flatMap(new Function<SearchGoodsParam, ObservableSource<SearchGoods>>() {
                    @Override
                    public ObservableSource<SearchGoods> apply(@NonNull SearchGoodsParam bean) throws Exception {
                        return  searchSevice.searchGoodsApi(gson.toJson(bean));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        dialog.show();
                    }
                })
                .flatMap(new Function<SearchGoods, ObservableSource<SearchGoods.DataBean>>() {
                    @Override
                    public ObservableSource<SearchGoods.DataBean> apply(@NonNull final SearchGoods searchGoods) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<SearchGoods.DataBean>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<SearchGoods.DataBean> e) throws Exception {
                                if (searchGoods.getErrcode() != 1) {
                                    e.onError(new ServiceMsgException(searchGoods.getMsg()));
                                }
                                if (searchGoods.getData() != null && searchGoods.getData().getGoodsList() != null) {
                                    e.onNext(searchGoods.getData());
                                    e.onComplete();
                                } else {
                                    e.onError(new ServiceDataException());
                                }
                            }
                        });
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                        Log.v(TAG,"final");
                    }
                })
                .subscribe(new Consumer<SearchGoods.DataBean>() {
                    @Override
                    public void accept(@NonNull SearchGoods.DataBean dataBean) throws Exception {
                        totalPage = dataBean.getTotalPages();
                        goodRecAdapter.setNewData(dataBean.getGoodsList());
                        if(dataBean.getGoodsList().size() == 0){
                            goodRecAdapter.setEmptyView(R.layout.empty_view);
                        }
                        param.setData(param_use.getData());
                        Log.v(TAG,"success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Toasty.error(SearchGoodActivity.this,getString(R.string.netconnect_exception)).show();
                        Log.v(TAG,throwable.toString());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.v(TAG,"complete");
                    }
                });
    }
    @OnClick(R.id.tochosebtn)
    public void chosenFilter() {
        drawerLayout.openDrawer(Gravity.RIGHT);
    }

    @OnClick(R.id.backtotop)
    public void onViewClickedTotop() {
        goodsWrap.smoothScrollToPosition(0);
    }

    @OnClick(R.id.backtomain)
    public void onViewClickedTomain() {
        Intent it = new Intent(this, AllShopActivity.class);
        startActivity(it);
    }

    public static class FilterHelper {
        private SearchFilterRes.DataBean searchFilterRes;
        public FilterHelper(SearchFilterRes searchFilterRes) {
            this.searchFilterRes = searchFilterRes.getData();
        }

        public ArrayList<FilterBean> invoke() {
            ArrayList<FilterBean> res = new ArrayList<>();
            if(searchFilterRes.getBrandList() != null && searchFilterRes.getBrandList().size() != 0){
                FilterBean brandBean = new FilterBean();
                brandBean.setType(FilterType.BRAND);
                brandBean.setName("品牌");
                for(SearchFilterRes.DataBean.BrandListBean bean: searchFilterRes.getBrandList()){
                    FilterItem item = new FilterItem();
                    item.setId(bean.getId());
                    item.setName(bean.getName());
                    brandBean.addItem(item);
                }
                res.add(brandBean);
            }
            if(searchFilterRes.getClass3List() != null && searchFilterRes.getClass3List().size() !=0){
                FilterBean cateBean = new FilterBean();
                cateBean.setType(FilterType.CATEGORY);
                cateBean.setName("分类");
                for (SearchFilterRes.DataBean.Class3ListBean bean : searchFilterRes.getClass3List()){
                    FilterItem item = new FilterItem();
                    item.setName(bean.getName());
                    item.setId(bean.getId());
                    cateBean.addItem(item);
                }
                res.add(cateBean);
            }
            if(searchFilterRes.getPriceList() != null && searchFilterRes.getPriceList().size() != 0){
                FilterBean priceBean = new FilterBean();
                priceBean.setType(FilterType.PRICE);
                for(SearchFilterRes.DataBean.PriceListBean bean : searchFilterRes.getPriceList()){
                    FilterItem item = new FilterItem();
                    item.setMaxPrice(bean.getMaxPrice());
                    item.setMinPrice(bean.getMinPrice());
                    priceBean.addItem(item);
                }
//                res.add(priceBean);
            }
            if(searchFilterRes.getPropertyList() != null && searchFilterRes.getPropertyList().size() != 0){
                for (SearchFilterRes.DataBean.PropertyListBean BiBean : searchFilterRes.getPropertyList()){
                    FilterBean peopertyBean = new FilterBean();
                    peopertyBean.setType(FilterType.PEOPERTY);
                    peopertyBean.setName(BiBean.getName());
                    for (String attr : BiBean.getValue()){
                        FilterItem item = new FilterItem();
                        item.setName(attr);
                        peopertyBean.addItem(item);
                    }
                    res.add(peopertyBean);
                }
            }
            return res;
        }
    }
}
