package com.hhyg.TyClosing.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.apiService.SearchSevice;
import com.hhyg.TyClosing.di.componet.DaggerCommonNetParamComponent;
import com.hhyg.TyClosing.di.componet.DaggerSearchGoodComponent;
import com.hhyg.TyClosing.di.module.CommonNetParamModule;
import com.hhyg.TyClosing.di.module.SearchGoodsModule;
import com.hhyg.TyClosing.entities.search.FilterBean;
import com.hhyg.TyClosing.entities.search.FilterItem;
import com.hhyg.TyClosing.entities.search.FilterType;
import com.hhyg.TyClosing.entities.search.PeoperFilter;
import com.hhyg.TyClosing.entities.search.PeopertyOfCate;
import com.hhyg.TyClosing.entities.search.PerFilterRes;
import com.hhyg.TyClosing.entities.search.PropertyListBean;
import com.hhyg.TyClosing.entities.search.SearchFilterParam;
import com.hhyg.TyClosing.entities.search.SearchFilterRes;
import com.hhyg.TyClosing.entities.search.SearchGoods;
import com.hhyg.TyClosing.entities.search.SearchGoodsParam;
import com.hhyg.TyClosing.entities.search.SearchType;
import com.hhyg.TyClosing.entities.shopcart.CastDetail;
import com.hhyg.TyClosing.entities.shopcart.ShopcartListParam;
import com.hhyg.TyClosing.entities.shopcart.ShopcartListRes;
import com.hhyg.TyClosing.exceptions.ServiceDataException;
import com.hhyg.TyClosing.exceptions.ServiceMsgException;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.ShoppingCartInfo;
import com.hhyg.TyClosing.info.SpuInfo;
import com.hhyg.TyClosing.mgr.ShoppingCartMgr;
import com.hhyg.TyClosing.ui.adapter.search.GoodRecAdapter;
import com.hhyg.TyClosing.ui.adapter.search.HorizontalFilterAdapter;
import com.hhyg.TyClosing.ui.adapter.search.PeopertyPopAdapter;
import com.hhyg.TyClosing.ui.adapter.search.VerticalFilterAdapter;
import com.hhyg.TyClosing.ui.adapter.search.VerticalFilterItemAdapter;
import com.hhyg.TyClosing.ui.view.PeopertyPopwindow;
import com.hhyg.TyClosing.util.PauseOnRecScrollListener;

import java.util.ArrayList;
import java.util.List;

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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
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
    VerticalFilterAdapter verticalFilterAdapter;
    @Inject
    CompositeDisposable compositeDisposable;
    @Inject
    Gson gson;
    @Inject
    MaterialDialog dialog;
    @Inject
    SearchType searchType;
    @Inject
    ArrayList<PeopertyOfCate> peopertyOfCates;
    @Inject
    VerticalFilterItemAdapter verticalFilterItemAdapter;
    @Inject
    ShopcartListParam shopcartListParam;
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
    @BindView(R.id.verticalpeoperty_rv)
    RecyclerView verticalpeopertyRv;
    @BindView(R.id.peopertyitem_rv)
    RecyclerView peopertyitemRv;
    @BindView(R.id.vertical_reset)
    Button verticalReset;
    @BindView(R.id.vertical_confirm)
    Button verticalConfirm;
    int totalPage;
    @BindView(R.id.has_stock_cb)
    CheckBox hasStockCb;
    @BindView(R.id.selected_icon)
    ImageView selectedIcon;
    @BindView(R.id.contenttop)
    RelativeLayout contenttop;
    @BindView(R.id.cast)
    TextView cast;
    @BindView(R.id.cut)
    TextView cut;
    @BindView(R.id.goshopcat)
    Button goshopcat;
    @BindView(R.id.bottomshopcat)
    LinearLayout bottomshopcat;
    @BindView(R.id.fullin)
    TextView fullin;
    @BindView(R.id.full)
    LinearLayout full;
    @BindView(R.id.activitydetail)
    TextView activitydetail;
    @BindView(R.id.activelayout)
    RelativeLayout activelayout;
    private SearchFilterRes rawFilterRes;
    private PeopertyPopwindow popWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchgood);
        ButterKnife.bind(this);
        DaggerSearchGoodComponent.builder()
                .applicationComponent(MyApplication.GetInstance().getComponent())
                .commonNetParamComponent(DaggerCommonNetParamComponent.builder().commonNetParamModule(new CommonNetParamModule()).build())
                .searchGoodsModule(new SearchGoodsModule((SearchGoodsParam.DataBean) getIntent().getParcelableExtra(seach_token), this))
                .build().inject(this);
        initView();
        Observable.just(param)
                .flatMap(new Function<SearchGoodsParam, ObservableSource<SearchGoods>>() {
                    @Override
                    public ObservableSource<SearchGoods> apply(@NonNull SearchGoodsParam searchGoodsParam) throws Exception {
                        return searchSevice.searchGoodsApi(gson.toJson(searchGoodsParam));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<SearchGoods>() {
                    @Override
                    public void accept(@NonNull SearchGoods searchGoods) throws Exception {
                        totalPage = searchGoods.getData().getTotalPages();
                        if (searchGoods.getData().getGoodsList().size() == 0) {
                            goodRecAdapter.setEmptyView(R.layout.empty_view);
                        } else {
                            goodRecAdapter.addData(searchGoods.getData().getGoodsList());
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .filter(new Predicate<SearchGoods>() {
                    @Override
                    public boolean test(@NonNull SearchGoods searchGoods) throws Exception {
                        return searchType == SearchType.KEY_WORD;
                    }
                })
                .map(new Function<SearchGoods, SearchGoodsParam>() {
                    @Override
                    public SearchGoodsParam apply(@NonNull SearchGoods searchGoods) throws Exception {
                        param.getData().setKeyword(searchGoods.getData().getSearchKey());
                        return param;
                    }
                })
                .first(param)
                .toObservable()
                .flatMap(new Function<SearchGoodsParam, ObservableSource<SearchFilterRes>>() {
                    @Override
                    public ObservableSource<SearchFilterRes> apply(@NonNull SearchGoodsParam searchGoodsParam) throws Exception {
                        return searchSevice.searchFilterApi(gson.toJson(searchGoodsParam));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<SearchFilterRes>() {
                    @Override
                    public void accept(@NonNull SearchFilterRes searchFilterRes) throws Exception {
                        horizontalFilterAdapter.setNewData(new FilterHelper(searchFilterRes).invoke());
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<SearchFilterRes, ArrayList<FilterBean>>() {
                    @Override
                    public ArrayList<FilterBean> apply(@NonNull SearchFilterRes searchFilterRes) throws Exception {
                        rawFilterRes = searchFilterRes;
                        return new FilterHelper(searchFilterRes).invokeWithPrice();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<ArrayList<FilterBean>>() {
                    @Override
                    public void accept(@NonNull ArrayList<FilterBean> filterBeen) throws Exception {
                        verticalFilterAdapter.setNewData(filterBeen);
                        if(filterBeen.size() != 0){
                            verticalFilterAdapter.getOnItemClickListener().onItemClick(verticalFilterAdapter, null, 0);
                        }
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.d(TAG, "accept");
                        printErr(throwable);
                        Toasty.error(SearchGoodActivity.this, getString(R.string.netconnect_exception)).show();
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<ArrayList<FilterBean>, ObservableSource<FilterBean>>() {
                    @Override
                    public ObservableSource<FilterBean> apply(@NonNull ArrayList<FilterBean> filterBeen) throws Exception {
                        int size = filterBeen.size();
                        FilterBean sources[] = new FilterBean[size];
                        filterBeen.toArray(sources);
                        return Observable.fromArray(sources);
                    }
                })
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.getType() == FilterType.CATEGORY;
                    }
                })
                .firstElement()
                .toObservable()
                .flatMap(new Function<FilterBean, ObservableSource<SearchGoodsParam.DataBean>>() {
                    @Override
                    public ObservableSource<SearchGoodsParam.DataBean> apply(@NonNull FilterBean filterBean) throws Exception {
                        int size = filterBean.getDataSet().size();
                        SearchGoodsParam.DataBean params[] = new SearchGoodsParam.DataBean[size];
                        for (int idx = 0; idx < size; idx++) {
                            FilterItem item = filterBean.getDataSet().get(idx);
                            SearchGoodsParam.DataBean TmpParam = (SearchGoodsParam.DataBean) param.getData().clone();
                            TmpParam.setClass3Id(item.getId());
                            params[idx] = TmpParam;
                        }
                        return Observable.fromArray(params);
                    }
                })
                .map(new Function<SearchGoodsParam.DataBean, SearchGoodsParam>() {
                    @Override
                    public SearchGoodsParam apply(@NonNull SearchGoodsParam.DataBean dataBean) throws Exception {
                        SearchGoodsParam tmpParam = (SearchGoodsParam) param.clone();
                        tmpParam.setData(dataBean);
                        return tmpParam;
                    }
                })
                .flatMap(new Function<SearchGoodsParam, ObservableSource<PerFilterRes>>() {
                    @Override
                    public ObservableSource<PerFilterRes> apply(@NonNull SearchGoodsParam searchGoodsParam) throws Exception {
                        return searchSevice.searchFilterApi(gson.toJson(searchGoodsParam))
                                .zipWith(Observable.just(searchGoodsParam.getData().getClass3Id()), new BiFunction<SearchFilterRes, String, PerFilterRes>() {
                                    @Override
                                    public PerFilterRes apply(@NonNull SearchFilterRes searchFilterRes, @NonNull String s) throws Exception {
                                        PerFilterRes res = new PerFilterRes();
                                        res.setCateId(s);
                                        res.setRaw(searchFilterRes);
                                        return res;
                                    }
                                })
                                .retry();
                    }
                })
                .map(new Function<PerFilterRes, PeopertyOfCate>() {
                    @Override
                    public PeopertyOfCate apply(@NonNull PerFilterRes perFilterRes) throws Exception {
                        PeopertyOfCate res = new PeopertyOfCate();
                        res.setCateId(perFilterRes.getCateId());
                        res.setDataSet((ArrayList<PropertyListBean>) perFilterRes.getRaw().getData().getPropertyList());
                        return res;
                    }
                })
                .subscribe(new Observer<PeopertyOfCate>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull PeopertyOfCate peopertyOfCate) {
                        peopertyOfCates.add(peopertyOfCate);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        printErr(e);
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (drawerLayout.isDrawerOpen(Gravity.RIGHT))) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void initView() {
        initSearchType();
        searchtitle.setText(getIntent().getStringExtra(getString(R.string.search_content)));
        goodsWrap.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        goodsWrap.setHasFixedSize(true);
        goodsWrap.addOnScrollListener(new PauseOnRecScrollListener(true, true));
        attrGroupWrap.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        attrGroupWrap.setHasFixedSize(true);
        goodsWrap.setAdapter(goodRecAdapter);
        goodRecAdapter.bindToRecyclerView(goodsWrap);
        attrGroupWrap.setAdapter(horizontalFilterAdapter);
        goodRecAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchGoods.DataBean.GoodsListBean bean = (SearchGoods.DataBean.GoodsListBean) adapter.getData().get(position);
                Intent it = new Intent(SearchGoodActivity.this, GoodsInfoActivity.class);
                it.putExtra("barcode", bean.getBarcode());
                startActivity(it);
            }
        });
        goodRecAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SearchGoods.DataBean.GoodsListBean bean = (SearchGoods.DataBean.GoodsListBean) adapter.getItem(position);
                addToShopcart(bean);
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
        verticalpeopertyRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        verticalpeopertyRv.setHasFixedSize(true);
        verticalpeopertyRv.setAdapter(verticalFilterAdapter);
        verticalFilterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                verticalFilterAdapter.clearSelectStatus();
                FilterBean bean = (FilterBean) adapter.getData().get(position);
                bean.setVertacalShow(true);
                adapter.notifyDataSetChanged();
                verticalFilterItemAdapter.setNewData(bean.getDataSet());
                if (bean.isSelected()) {
                    selectedIcon.setVisibility(View.GONE);
                } else {
                    selectedIcon.setVisibility(View.VISIBLE);
                }
            }
        });
        peopertyitemRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        peopertyitemRv.setHasFixedSize(true);
        peopertyitemRv.setAdapter(verticalFilterItemAdapter);
        hasStockCb.setChecked(true);
        hasStockCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    param.getData().setAvailable("1");
                } else {
                    param.getData().setAvailable(null);
                }
            }
        });
        View popContent = LayoutInflater.from(this).inflate(R.layout.popwindow_peoperty, null);
        RecyclerView recyclerView = (RecyclerView) popContent.findViewById(R.id.pop_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(popAdapter);
        Button reset = (Button) popContent.findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popAdapter.getFilterData().getType() == FilterType.CATEGORY) {
                    categoryChange();
                }
                Observable.concat(Observable.just(popAdapter.getFilterData()), Observable.fromIterable(verticalFilterAdapter.getData()).filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.getName().equals(popAdapter.getFilterData().getName());
                    }
                })).subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<FilterBean>() {
                            @Override
                            public void accept(@NonNull FilterBean filterBean) throws Exception {
                                Log.d(TAG, "accept");
                                filterBean.setSelected(false);
                                filterBean.setSelectedName("");
                                if (filterBean.isVertacalShow()) {
                                    selectedIcon.setVisibility(View.VISIBLE);
                                }
                            }
                        })
                        .flatMap(new Function<FilterBean, ObservableSource<FilterItem>>() {
                            @Override
                            public ObservableSource<FilterItem> apply(@NonNull FilterBean filterBean) throws Exception {
                                return Observable.fromIterable(filterBean.getDataSet());
                            }
                        })
                        .doOnNext(new Consumer<FilterItem>() {
                            @Override
                            public void accept(@NonNull FilterItem filterItem) throws Exception {
                                filterItem.setSelected(false);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<FilterItem>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onNext(@NonNull FilterItem filterItem) {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.d(TAG, e.toString());
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "complete");
                                printStatus();
                                verticalFilterItemAdapter.notifyDataSetChanged();
                                verticalFilterAdapter.notifyDataSetChanged();
                                horizontalFilterAdapter.notifyDataSetChanged();
                                popAdapter.notifyDataSetChanged();
                            }
                        });
            }
        });
        final Button confim = (Button) popContent.findViewById(R.id.confirm);
        confim.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          changeSearchGoods();
                                      }
                                  }
        );
        popWindow = new PeopertyPopwindow(this, popContent);

        horizontalFilterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FilterBean bean = (FilterBean) adapter.getData().get(position);
                bean.setShowNow(true);
                adapter.notifyDataSetChanged();
                popAdapter.setFilterData(bean);
                popWindow.showAsDropDown(attrGroupWrap, 0, 0);
            }
        });
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popAdapter.getFilterData().setShowNow(false);
                horizontalFilterAdapter.notifyDataSetChanged();
            }
        });
        verticalFilterItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final FilterItem item = (FilterItem) adapter.getItem(position);
                Log.d(TAG, "22" + item.isSelected());
                FilterBean bean = new FilterBean();
                for (FilterBean tmpBean : verticalFilterAdapter.getData()) {
                    if (tmpBean.isVertacalShow()) {
                        bean = tmpBean;
                        break;
                    }
                }
                FilterBean otherBean = new FilterBean();
                for (FilterBean tmpBean2 : horizontalFilterAdapter.getData()) {
                    if (tmpBean2.getName().equals(bean.getName())) {
                        otherBean = tmpBean2;
                        break;
                    }
                }
                FilterItem otherItem = new FilterItem();
                for (FilterItem tmpItem : otherBean.getDataSet()) {
                    if (tmpItem.getName().equals(item.getName())) {
                        otherItem = tmpItem;
                        break;
                    }
                }
                if (bean.getType() == FilterType.CATEGORY) {
                    removePeoperty();
                    changePeoperty(item);
                    Log.d(TAG, "33");
                    if (bean.isSelected()) {
                        if (item.isSelected()) {
                            item.setSelected(false);
                            otherItem.setSelected(false);
                            bean.setSelected(false);
                            otherBean.setSelected(false);
                        } else {
                            for (FilterItem tmpItem : bean.getDataSet()) {
                                if (tmpItem.isSelected()) {
                                    tmpItem.setSelected(false);
                                    break;
                                }
                            }
                            for (FilterItem tmpItem : otherBean.getDataSet()) {
                                if (tmpItem.isSelected()) {
                                    tmpItem.setSelected(false);
                                    break;
                                }
                            }
                            item.setSelected(true);
                            otherItem.setSelected(true);
                            bean.setSelectedName(item.getName());
                            otherBean.setSelectedName(item.getName());
                        }
                    } else {
                        item.setSelected(true);
                        otherItem.setSelected(true);
                        bean.setSelected(true);
                        bean.setSelectedName(item.getName());
                        otherBean.setSelected(true);
                        otherBean.setSelectedName(item.getName());
                    }
                } else if (bean.getType() == FilterType.PRICE) {
                    if (bean.isSelected()) {
                        if (item.isSelected()) {
                            item.setSelected(false);
                            otherItem.setSelected(false);
                            bean.setSelected(false);
                            otherBean.setSelected(false);
                        } else {
                            for (FilterItem tmpItem : bean.getDataSet()) {
                                if (tmpItem.isSelected()) {
                                    tmpItem.setSelected(false);
                                    break;
                                }
                            }
                            for (FilterItem tmpItem : otherBean.getDataSet()) {
                                if (tmpItem.isSelected()) {
                                    tmpItem.setSelected(false);
                                    break;
                                }
                            }
                            item.setSelected(true);
                            otherItem.setSelected(true);
                            bean.setSelectedName(item.getName());
                            otherBean.setSelectedName(item.getName());
                        }
                    } else {
                        item.setSelected(true);
                        bean.setSelected(true);
                        bean.setSelectedName(item.getName());
                    }
                } else {
                    if (item.isSelected()) {
                        item.setSelected(false);
                        otherItem.setSelected(false);
                        boolean seleted = false;
                        StringBuilder sb = new StringBuilder();
                        for (FilterItem tmpItem : bean.getDataSet()) {
                            if (tmpItem.isSelected()) {
                                seleted = true;
                                sb.append(tmpItem.getName() + "  ");
                            }
                        }
                        if (seleted) {
                            bean.setSelected(true);
                            bean.setSelectedName(sb.toString());
                            otherBean.setSelected(true);
                            otherBean.setSelectedName(sb.toString());
                        } else {
                            bean.setSelected(false);
                            otherBean.setSelected(false);
                        }
                    } else {
                        item.setSelected(true);
                        otherItem.setSelected(true);
                        StringBuilder sb = new StringBuilder();
                        for (FilterItem tmpItem : bean.getDataSet()) {
                            if (tmpItem.isSelected()) {
                                sb.append(tmpItem.getName() + "  ");
                            }
                        }
                        bean.setSelected(true);
                        otherBean.setSelected(true);
                        bean.setSelectedName(sb.toString());
                        otherBean.setSelectedName(sb.toString());
                    }
                }
                if (bean.isSelected()) {
                    selectedIcon.setVisibility(View.GONE);
                } else {
                    selectedIcon.setVisibility(View.VISIBLE);
                }
                horizontalFilterAdapter.notifyDataSetChanged();
                popAdapter.notifyDataSetChanged();
                verticalFilterAdapter.notifyDataSetChanged();
                verticalFilterItemAdapter.notifyDataSetChanged();
                Log.d(TAG, "44");
            }
        });

        popAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                              @Override
                                              public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                  PeopertyPopAdapter popAdapter = (PeopertyPopAdapter) adapter;
                                                  final FilterBean bean = popAdapter.getFilterData();
                                                  final FilterItem item = (FilterItem) adapter.getItem(position);
                                                  FilterBean otherBean = new FilterBean();
                                                  for (FilterBean tmpBean : verticalFilterAdapter.getData()) {
                                                      if (tmpBean.getName().equals(bean.getName())) {
                                                          otherBean = tmpBean;
                                                          break;
                                                      }
                                                  }
                                                  FilterItem verticalItem = new FilterItem();
                                                  for (FilterItem tmpItem : otherBean.getDataSet()) {
                                                      if (tmpItem.getName().equals(item.getName())) {
                                                          verticalItem = tmpItem;
                                                          break;
                                                      }
                                                  }
                                                  if (bean.getType() == FilterType.CATEGORY) {
                                                      removePeoperty();
                                                      changePeoperty(item);
                                                      if (bean.isSelected()) {
                                                          if (item.isSelected()) {
                                                              item.setSelected(false);
                                                              verticalItem.setSelected(false);
                                                              bean.setSelected(false);
                                                              otherBean.setSelected(false);

                                                          } else {
                                                              for (FilterItem tmpItem : bean.getDataSet()) {
                                                                  if (tmpItem.isSelected()) {
                                                                      tmpItem.setSelected(false);
                                                                      break;
                                                                  }
                                                              }
                                                              for (FilterItem tmpItem : otherBean.getDataSet()) {
                                                                  if (tmpItem.isSelected()) {
                                                                      tmpItem.setSelected(false);
                                                                      break;
                                                                  }
                                                              }
                                                              item.setSelected(true);
                                                              verticalItem.setSelected(true);
                                                              bean.setSelectedName(item.getName());
                                                              otherBean.setSelectedName(item.getName());
                                                          }
                                                      } else {
                                                          item.setSelected(true);
                                                          verticalItem.setSelected(true);
                                                          bean.setSelected(true);
                                                          bean.setSelectedName(item.getName());
                                                          otherBean.setSelected(true);
                                                          otherBean.setSelectedName(item.getName());
                                                      }
                                                  } else {
                                                      if (item.isSelected()) {
                                                          item.setSelected(false);
                                                          verticalItem.setSelected(false);
                                                          boolean seleted = false;
                                                          StringBuilder sb = new StringBuilder();
                                                          for (FilterItem tmpItem : bean.getDataSet()) {
                                                              if (tmpItem.isSelected()) {
                                                                  seleted = true;
                                                                  sb.append(tmpItem.getName() + "  ");
                                                              }
                                                          }
                                                          if (seleted) {
                                                              bean.setSelected(true);
                                                              bean.setSelectedName(sb.toString());
                                                              otherBean.setSelected(true);
                                                              otherBean.setSelectedName(sb.toString());
                                                          } else {
                                                              bean.setSelected(false);
                                                              otherBean.setSelected(false);
                                                          }
                                                      } else {
                                                          item.setSelected(true);
                                                          verticalItem.setSelected(true);
                                                          StringBuilder sb = new StringBuilder();
                                                          for (FilterItem tmpItem : bean.getDataSet()) {
                                                              if (tmpItem.isSelected()) {
                                                                  sb.append(tmpItem.getName() + "  ");
                                                              }
                                                          }
                                                          bean.setSelected(true);
                                                          otherBean.setSelected(true);
                                                          bean.setSelectedName(sb.toString());
                                                          otherBean.setSelectedName(sb.toString());
                                                      }
                                                  }
                                                  for (FilterBean itemBean : verticalFilterAdapter.getData()) {
                                                      if (itemBean.isVertacalShow()) {
                                                          if (itemBean.isSelected()) {
                                                              selectedIcon.setVisibility(View.GONE);
                                                          } else {
                                                              selectedIcon.setVisibility(View.VISIBLE);
                                                          }
                                                      }
                                                  }
                                                  horizontalFilterAdapter.notifyDataSetChanged();
                                                  popAdapter.notifyDataSetChanged();
                                                  verticalFilterAdapter.notifyDataSetChanged();
                                                  verticalFilterItemAdapter.notifyDataSetChanged();
                                                  Log.d(TAG, "55");
                                              }
                                          }
        );
    }

    private void printStatus() {
        for (FilterBean bean : verticalFilterAdapter.getData()){
            Log.d(TAG, "x" + bean.isSelected());
            for (FilterItem item : bean.getDataSet()){
                Log.d(TAG, "c" + item.isSelected());
            }
        }
    }

    private void categoryChange() {
        removePeoperty();
        FilterItem filterItem = new FilterItem();
        filterItem.setSelected(false);
        changePeoperty(filterItem);
    }

    private void addToShopcart(SearchGoods.DataBean.GoodsListBean bean) {
        final int choseCnt;
        if(!ShoppingCartMgr.getInstance().isInfoExist(bean.getBarcode())){
            choseCnt = 1;
        }else{
            choseCnt = ShoppingCartMgr.getInstance().getInfoByBarCode(bean.getBarcode()).cnt +1;
        }
        if(choseCnt > bean.getStock()){
            Toast.makeText(MyApplication.GetInstance(), "该商品库存不足", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bean.getStock() > 0){
            SpuInfo spu = new SpuInfo();
            spu.barCode = bean.getBarcode();
            spu.activeId = bean.getActive_code();
            spu.name = bean.getName();
            spu.stock = bean.getStock();
            spu.citAmount = 1000;
            spu.attrInfo = bean.getName();
            spu.msPrice = bean.getMianshui_price();
            if(bean.getImage() != null && !bean.getImage().equals("")){
                ArrayList<String> imgLinks = new ArrayList<String>();
                imgLinks.add(bean.getImage());
                spu.imageLinks = imgLinks;
            }
            ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();
            if(mShoppingCartMgr.isInfoExist(bean.getBarcode())){
                mShoppingCartMgr.updateShopCnt(bean.getBarcode(),choseCnt);
            }else{
                mShoppingCartMgr.addInfo(spu, bean.getName(), bean.getBrand_name(), choseCnt, 1211, bean.getName());
            }
            if(!(searchType == SearchType.PRIVILEGE)){
                mShoppingCartMgr.updateActiveId(bean.getBarcode(), bean.getActive_code());
            }else{
                if(bean.getActive_code() != null){
                    mShoppingCartMgr.updateActiveId(bean.getBarcode(),bean.getActive_code());
                }else{
                    mShoppingCartMgr.updateActiveId(bean.getBarcode(), "");
                }
            }
            Toast.makeText(MyApplication.GetInstance(), "加入购物车成功", Toast.LENGTH_SHORT).show();
            if(!(searchType == SearchType.PRIVILEGE)){
                getCaset();
            }
        }
    }

    private void getCaset() {
        ShoppingCartMgr mShoppingCartMgr = ShoppingCartMgr.getInstance();
        Observable.fromIterable(mShoppingCartMgr.getAll())
                .filter(new Predicate<ShoppingCartInfo>() {
                    @Override
                    public boolean test(@NonNull ShoppingCartInfo shoppingCartInfo) throws Exception {
                        return shoppingCartInfo.activeId.equals(param.getData().getActivityId());
                    }
                })
                .toList()
                .toObservable()
                .map(new Function<List<ShoppingCartInfo>, ShopcartListParam>() {
                    @Override
                    public ShopcartListParam apply(@NonNull List<ShoppingCartInfo> shoppingCartInfos) throws Exception {
                        ArrayList<ShopcartListParam.DataBean> res = new ArrayList<ShopcartListParam.DataBean>();
                        for (ShoppingCartInfo info : shoppingCartInfos){
                            ShopcartListParam.DataBean bean = new ShopcartListParam.DataBean();
                            bean.setBarcode(info.barCode);
                            bean.setActivity(info.activeId);
                            bean.setNumber(""+info.cnt);
                            res.add(bean);
                        }
                        shopcartListParam.setData(res);
                        return shopcartListParam;
                    }
                })
                .flatMap(new Function<ShopcartListParam, ObservableSource<ShopcartListRes>>() {
                    @Override
                    public ObservableSource<ShopcartListRes> apply(@NonNull ShopcartListParam shopcartListParam) throws Exception {
                        return searchSevice.shopcartList(gson.toJson(shopcartListParam));
                    }
                })
                .map(new Function<ShopcartListRes, ShopcartListRes.DataBean>() {
                    @Override
                    public ShopcartListRes.DataBean apply(@NonNull ShopcartListRes shopcartListRes) throws Exception {
                        return shopcartListRes.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopcartListRes.DataBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull ShopcartListRes.DataBean dataBean) {
                        CastDetail detail =  dataBean.getActive_columns().get(param.getData().getActivityId());
                        setPrice(detail.getActive_price(),detail.getPreferentialPrice(),detail.getDesc_fee());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, e.toString());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void setPrice(String cast2, String fee,String comment) {
        if(cast2 != null && fee != null){
            cast.setText("小计   " + getString(R.string.server_settle_moeny) + cast2);
            cut.setText("优惠   " + getString(R.string.server_settle_moeny) + fee);
        }else{
            cast.setText("小计   " + getString(R.string.server_settle_moeny)+ "0");
            cut.setText("优惠   " + getString(R.string.server_settle_moeny) + "0");
        }
        if(comment != null && !comment.equals("")){
            full.setVisibility(View.VISIBLE);
            fullin.setText(Html.fromHtml(comment));
        }else{
            full.setVisibility(View.GONE);
        }
    }

    private void initSearchType() {
        if (searchType == SearchType.ACTIVITY) {
            getCaset();
            full.setVisibility(View.VISIBLE);
            bottomshopcat.setVisibility(View.VISIBLE);
            activelayout.setVisibility(View.VISIBLE);
            activitydetail.setText(getIntent().getStringExtra(getString(R.string.search_desc)));
            goodsWrap.setPadding(40,0,40,220);
        }else if(searchType == SearchType.PRIVILEGE){
            bottomshopcat.setVisibility(View.VISIBLE);
            goodsWrap.setPadding(40,0,40,150);
            activelayout.setVisibility(View.VISIBLE);
            activitydetail.setText(getIntent().getStringExtra(getString(R.string.search_desc)));
            cast.setVisibility(View.GONE);
            cut.setVisibility(View.GONE);
        }
    }

    private void changeSearchGoods() {
        printStatus();
        Observable<SearchFilterParam> A = Observable.fromIterable(verticalFilterAdapter.getData())
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.isSelected();
                    }
                })
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.getType() == FilterType.CATEGORY;
                    }
                })
                .flatMap(new Function<FilterBean, ObservableSource<FilterItem>>() {
                    @Override
                    public ObservableSource<FilterItem> apply(@NonNull FilterBean filterBean) throws Exception {
                        return Observable.fromIterable(filterBean.getDataSet());
                    }
                })
                .filter(new Predicate<FilterItem>() {
                    @Override
                    public boolean test(@NonNull FilterItem filterItem) throws Exception {
                        return filterItem.isSelected();
                    }
                })
                .map(new Function<FilterItem, SearchFilterParam>() {
                    @Override
                    public SearchFilterParam apply(@NonNull FilterItem filterBean) throws Exception {
                        Log.d(TAG, "map _ cate");
                        SearchFilterParam param = new SearchFilterParam();
                        param.setType(FilterType.CATEGORY);
                        param.setParam(filterBean.getId());
                        return param;
                    }
                });
        Observable<SearchFilterParam> B = Observable.fromIterable(verticalFilterAdapter.getData())
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.isSelected();
                    }
                })
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.getType() == FilterType.PRICE;
                    }
                })
                .flatMap(new Function<FilterBean, ObservableSource<FilterItem>>() {
                    @Override
                    public ObservableSource<FilterItem> apply(@NonNull FilterBean filterBean) throws Exception {
                        return Observable.fromIterable(filterBean.getDataSet());
                    }
                })
                .filter(new Predicate<FilterItem>() {
                    @Override
                    public boolean test(@NonNull FilterItem filterItem) throws Exception {
                        Log.d(TAG, "max" + filterItem.getMaxPrice() + "min" + filterItem.getMinPrice() + filterItem.isSelected());
                        return filterItem.isSelected();
                    }
                })
                .map(new Function<FilterItem, SearchFilterParam>() {
                    @Override
                    public SearchFilterParam apply(@NonNull FilterItem filterItem) throws Exception {
                        Log.d(TAG, "map _ price");
                        SearchFilterParam param = new SearchFilterParam();
                        param.setType(FilterType.PRICE);
                        param.setParam(filterItem.getMinPrice());
                        param.setParam2(filterItem.getMaxPrice());
                        return param;
                    }
                });
        Observable<SearchFilterParam> C = Observable.fromIterable(verticalFilterAdapter.getData())
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.getType() == FilterType.BRAND;
                    }
                })
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        Log.d(TAG, "test" + filterBean.isSelected());
                        return filterBean.isSelected();
                    }
                })
                .doOnNext(new Consumer<FilterBean>() {
                    @Override
                    public void accept(@NonNull FilterBean filterBean) throws Exception {
                        Log.d(TAG, filterBean.getName());
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.toString());
                    }
                })
                .flatMap(new Function<FilterBean, ObservableSource<List<FilterItem>>>() {
                    @Override
                    public ObservableSource<List<FilterItem>> apply(@NonNull FilterBean filterBean) throws Exception {
                        return Observable.fromIterable(filterBean.getDataSet()).filter(new Predicate<FilterItem>() {
                            @Override
                            public boolean test(@NonNull FilterItem filterItem) throws Exception {
                                return filterItem.isSelected();
                            }
                        }).toList().toObservable();
                    }
                })
                .map(new Function<List<FilterItem>, SearchFilterParam>() {
                    @Override
                    public SearchFilterParam apply(@NonNull List<FilterItem> filterItems) throws Exception {
                        SearchFilterParam param = new SearchFilterParam();
                        param.setType(FilterType.BRAND);
                        StringBuilder sb = new StringBuilder();
                        for (FilterItem item : filterItems) {
                            Log.d(TAG, item.getName());
                            sb.append(item.getId());
                            sb.append(",");
                        }
                        param.setParam(sb.toString());
                        return param;
                    }
                });
        Observable<FilterBean> cD = Observable.fromIterable(verticalFilterAdapter.getData())
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.isSelected();
                    }
                })
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.getType() == FilterType.PEOPERTY;
                    }
                });

        Observable<SearchFilterParam> D = cD.flatMap(new Function<FilterBean, ObservableSource<FilterItem>>() {
            @Override
            public ObservableSource<FilterItem> apply(@NonNull FilterBean filterBean) throws Exception {
                return Observable.fromIterable(filterBean.getDataSet());
            }})
                .filter(new Predicate<FilterItem>() {
                    @Override
                    public boolean test(@NonNull FilterItem filterItem) throws Exception {
                        return filterItem.isSelected();
                    }
                })
                .toList()
                .toObservable()
                .zipWith(cD, new BiFunction<List<FilterItem>, FilterBean, PeoperFilter>() {
                    @Override
                    public PeoperFilter apply(@NonNull List<FilterItem> filterItems, @NonNull FilterBean filterBean) throws Exception {
                        PeoperFilter filter = new PeoperFilter();
                        ArrayList<String> res = new ArrayList<String>();
                        filter.setName(filterBean.getName());
                        filter.setValues((ArrayList<FilterItem>) filterItems);
                        return filter;
                    }
                })
                .toList()
                .toObservable()
                .map(new Function<List<PeoperFilter>, SearchFilterParam>() {
                    @Override
                    public SearchFilterParam apply(@NonNull List<PeoperFilter> peoperFilters) throws Exception {
                        SearchFilterParam param = new SearchFilterParam();
                        param.setType(FilterType.PEOPERTY);
                        StringBuilder sb = new StringBuilder();
                        for (PeoperFilter filter : peoperFilters) {
                            for (FilterItem item : filter.getValues()) {
                                Log.d(TAG, item.getName() + "222");
                                sb.append(filter.getName());
                                sb.append("_");
                                sb.append(item.getName());
                                sb.append(",");
                            }
                        }
                        param.setParam(sb.toString());
                        return param;
                    }
                });
        Observable.concat(A, B, C, D)
                .toList()
                .toObservable()
                .map(new Function<List<SearchFilterParam>, SearchGoodsParam>() {
                    @Override
                    public SearchGoodsParam apply(@NonNull List<SearchFilterParam> searchFilterParams) throws Exception {
                        SearchGoodsParam tmpParam = (SearchGoodsParam) param.clone();
                        SearchGoodsParam.DataBean data = (SearchGoodsParam.DataBean) tmpParam.getData().clone();
                        Log.d(TAG, "come in");
                        Log.d(TAG, searchFilterParams.toString());
                        for (SearchFilterParam filterParam : searchFilterParams) {
                            Log.d(TAG, filterParam.getParam());
                            if (filterParam.getType() == FilterType.BRAND) {
                                data.setBrandId(filterParam.getParam());
                            } else if (filterParam.getType() == FilterType.CATEGORY) {
                                data.setClass3Id(filterParam.getParam());
                            } else if (filterParam.getType() == FilterType.PRICE) {
                                data.setMinPrice(filterParam.getParam());
                                data.setMaxPrice(filterParam.getParam2());
                            } else {
                                data.setPropertyList(filterParam.getParam());
                            }
                        }
                        tmpParam.setData(data);
                        param_use.setData(data);
                        return tmpParam;
                    }
                })
                .flatMap(new Function<SearchGoodsParam, ObservableSource<SearchGoods>>() {
                    @Override
                    public ObservableSource<SearchGoods> apply(@NonNull SearchGoodsParam searchGoodsParam) throws Exception {
                        return searchSevice.searchGoodsApi(gson.toJson(searchGoodsParam));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dialog.dismiss();
                    }
                })
                .subscribe(new Observer<SearchGoods>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        dialog.show();
                        if (popWindow.isShowing()) {
                            popWindow.dismiss();
                        }
                    }

                    @Override
                    public void onNext(@NonNull SearchGoods searchGoods) {
                        totalPage = searchGoods.getData().getTotalPages();
                        goodRecAdapter.setNewData(searchGoods.getData().getGoodsList());
                        if (searchGoods.getData().getGoodsList().size() == 0) {
                            goodRecAdapter.setEmptyView(R.layout.empty_view);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        printErr(e);
                    }

                    @Override
                    public void onComplete() {
                        param = param_use;
                    }
                });
    }

    private void clearStatus() {
        Observable<FilterBean> A = Observable.fromIterable(horizontalFilterAdapter.getData());
        Observable<FilterBean> B = Observable.fromIterable(verticalFilterAdapter.getData());
        Observable.concat(A, B)
                .doOnNext(new Consumer<FilterBean>() {
                    @Override
                    public void accept(@NonNull FilterBean filterBean) throws Exception {
                        filterBean.setSelected(false);
                        filterBean.setSelectedName("");
                    }
                })
                .flatMap(new Function<FilterBean, ObservableSource<FilterItem>>() {
                    @Override
                    public ObservableSource<FilterItem> apply(@NonNull FilterBean filterBean) throws Exception {
                        return Observable.fromIterable(filterBean.getDataSet());
                    }
                })
                .doOnNext(new Consumer<FilterItem>() {
                    @Override
                    public void accept(@NonNull FilterItem filterItem) throws Exception {
                        filterItem.setSelected(false);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FilterItem>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull FilterItem filterItem) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        categoryChange();
                        horizontalFilterAdapter.notifyDataSetChanged();
                        verticalFilterAdapter.notifyDataSetChanged();
                        verticalFilterItemAdapter.notifyDataSetChanged();
                        selectedIcon.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void changePeoperty(final FilterItem item) {
        Observable<ArrayList<PropertyListBean>> A = Observable.just(rawFilterRes)
                .filter(new Predicate<SearchFilterRes>() {
                    @Override
                    public boolean test(@NonNull SearchFilterRes searchFilterRes) throws Exception {
                        return !item.isSelected();
                    }
                })
                .map(new Function<SearchFilterRes, ArrayList<PropertyListBean>>() {
                    @Override
                    public ArrayList<PropertyListBean> apply(@NonNull SearchFilterRes searchFilterRes) throws Exception {
                        return (ArrayList<PropertyListBean>) searchFilterRes.getData().getPropertyList();
                    }
                });
        Observable<ArrayList<PropertyListBean>> C = Observable.fromIterable(peopertyOfCates)
                .filter(new Predicate<PeopertyOfCate>() {
                    @Override
                    public boolean test(@NonNull PeopertyOfCate peopertyOfCate) throws Exception {
                        Log.d(TAG, "test" + !item.isSelected());
                        return item.isSelected();
                    }
                })
                .filter(new Predicate<PeopertyOfCate>() {
                    @Override
                    public boolean test(@NonNull PeopertyOfCate peopertyOfCate) throws Exception {
                        return peopertyOfCate.getCateId().equals(item.getId());
                    }
                })
                .map(new Function<PeopertyOfCate, ArrayList<PropertyListBean>>() {
                    @Override
                    public ArrayList<PropertyListBean> apply(@NonNull PeopertyOfCate peopertyOfCate) throws Exception {
                        return peopertyOfCate.getDataSet();
                    }
                });

        Observable.concat(A, C)
                .flatMap(new Function<ArrayList<PropertyListBean>, ObservableSource<PropertyListBean>>() {
                    @Override
                    public ObservableSource<PropertyListBean> apply(@NonNull ArrayList<PropertyListBean> propertyListBeen) throws Exception {
                        return Observable.fromIterable(propertyListBeen);
                    }
                })
                .map(new Function<PropertyListBean, FilterBean>() {
                    @Override
                    public FilterBean apply(@NonNull PropertyListBean propertyListBean) throws Exception {
                        FilterBean bean = new FilterBean();
                        bean.setType(FilterType.PEOPERTY);
                        bean.setName(propertyListBean.getName());
                        for (String attr : propertyListBean.getValue()) {
                            FilterItem item = new FilterItem();
                            item.setName(attr);
                            bean.addItem(item);
                        }
                        return bean;
                    }
                })
                .doOnNext(new Consumer<FilterBean>() {
                    @Override
                    public void accept(@NonNull FilterBean filterBean) throws Exception {
                        horizontalFilterAdapter.getData().add(filterBean);
                        verticalFilterAdapter.getData().add(filterBean);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FilterBean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull FilterBean filterBean) {
                        Log.d(TAG, filterBean.getName());

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, e.toString());

                    }

                    @Override
                    public void onComplete() {
                        horizontalFilterAdapter.notifyDataSetChanged();
                        verticalFilterAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void removePeoperty() {
        for (int i = 0; i < horizontalFilterAdapter.getData().size(); i++) {
            if (horizontalFilterAdapter.getData().get(i).getType() == FilterType.PEOPERTY) {
                horizontalFilterAdapter.getData().remove(i);
                --i;
            }
        }
        for (int i = 0; i < verticalFilterAdapter.getData().size(); i++) {
            if (verticalFilterAdapter.getData().get(i).getType() == FilterType.PEOPERTY) {
                verticalFilterAdapter.getData().remove(i);
                --i;
            }
        }
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
    public void onViewClicked(final View view) {
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
                if (bean.getSortType().equals("3")) {
                    bean.setSortType("-3");
                } else {
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
        Observable.just(param_use)
                .flatMap(new Function<SearchGoodsParam, ObservableSource<SearchGoods>>() {
                    @Override
                    public ObservableSource<SearchGoods> apply(@NonNull SearchGoodsParam bean) throws Exception {
                        return searchSevice.searchGoodsApi(gson.toJson(bean));
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
                    }
                })
                .subscribe(new Consumer<SearchGoods.DataBean>() {
                    @Override
                    public void accept(@NonNull SearchGoods.DataBean dataBean) throws Exception {
                        totalPage = dataBean.getTotalPages();
                        goodRecAdapter.setNewData(dataBean.getGoodsList());
                        if (dataBean.getGoodsList().size() == 0) {
                            goodRecAdapter.setEmptyView(R.layout.empty_view);
                        }
                        updateChosenStatus(view);
                        param.setData(param_use.getData());
                        Log.v(TAG, "success");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Toasty.error(SearchGoodActivity.this, getString(R.string.netconnect_exception)).show();
                        Log.v(TAG, throwable.toString());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.v(TAG, "complete");
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        compositeDisposable.add(disposable);
                    }
                });
    }

    private void updateChosenStatus(View view) {
        switch (view.getId()) {
            case R.id.chosengerenal:
                view.setBackgroundResource(R.drawable.allshop_search_goodlist_gerenal_pressed);
                view.setClickable(false);
                chosehotsale.setClickable(true);
                chosehotsale.setBackgroundResource(R.drawable.allshop_search_goodlist_hotsale_normal);
                chosenew.setClickable(true);
                chosenew.setBackgroundResource(R.drawable.allshop_search_goodlist_newarrival_normal);
                choseprice.setClickable(true);
                choseprice.setBackgroundResource(R.drawable.allshop_search_goodlist_price_normal);
                break;
            case R.id.chosehotsale:
                view.setBackgroundResource(R.drawable.allshop_search_goodlist_hotsale_pressed);
                view.setClickable(false);
                chosenew.setBackgroundResource(R.drawable.allshop_search_goodlist_newarrival_normal);
                chosenew.setClickable(true);
                choseprice.setBackgroundResource(R.drawable.allshop_search_goodlist_price_normal);
                choseprice.setClickable(true);
                chosengerenal.setBackgroundResource(R.drawable.allshop_search_goodlist_hotsale_normal);
                chosengerenal.setClickable(true);
                break;
            case R.id.chosenew:
                view.setBackgroundResource(R.drawable.allshop_search_goodlist_newarrival_pressed);
                view.setClickable(false);
                chosehotsale.setBackgroundResource(R.drawable.allshop_search_goodlist_hotsale_normal);
                chosehotsale.setClickable(true);
                choseprice.setBackgroundResource(R.drawable.allshop_search_goodlist_price_normal);
                choseprice.setClickable(true);
                chosengerenal.setBackgroundResource(R.drawable.allshop_search_goodlist_gerenal_normal);
                chosengerenal.setClickable(true);
                break;
            case R.id.choseprice:
                chosehotsale.setBackgroundResource(R.drawable.allshop_search_goodlist_hotsale_normal);
                chosehotsale.setClickable(true);
                chosenew.setBackgroundResource(R.drawable.allshop_search_goodlist_newarrival_normal);
                chosenew.setClickable(true);
                chosengerenal.setBackgroundResource(R.drawable.allshop_search_goodlist_gerenal_normal);
                chosengerenal.setClickable(true);
                choseprice.setClickable(true);
                if (param_use.getData().getSortType().equals("3")) {
                    choseprice.setBackgroundResource(R.drawable.allshop_search_goodlist_price_pressed_up);
                } else {
                    choseprice.setBackgroundResource(R.drawable.allshop_search_goodlist_price_pressed_down);
                }
                break;
        }
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

    @OnClick(R.id.contenttop)
    public void onViewClickAll() {
        if(selectedIcon.getVisibility() == View.VISIBLE){
            return;
        }
        for (FilterBean bean : verticalFilterAdapter.getData()){
            if(bean.isVertacalShow() && bean.getType() == FilterType.CATEGORY){
                categoryChange();
                break;
            }
        }
        Observable<FilterBean> B = Observable.fromIterable(verticalFilterAdapter.getData())
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.isVertacalShow() && filterBean.getType() == FilterType.PRICE;
                    }
                });
        Observable<FilterBean> C = Observable.fromIterable(verticalFilterAdapter.getData())
                .filter(new Predicate<FilterBean>() {
                    @Override
                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.isVertacalShow() && filterBean.getType() != FilterType.PRICE;
                    }
                });
        Observable<FilterBean> D = C
                .map(new Function<FilterBean, String>() {
                    @Override
                    public String apply(@NonNull FilterBean filterBean) throws Exception {
                        return filterBean.getName();
                    }
                })
                .flatMap(new Function<String, ObservableSource<FilterBean>>() {
                    @Override
                    public ObservableSource<FilterBean> apply(@NonNull String s) throws Exception {
                        final String name = s;
                        return Observable.fromIterable(horizontalFilterAdapter.getData())
                                .filter(new Predicate<FilterBean>() {
                                    @Override
                                    public boolean test(@NonNull FilterBean filterBean) throws Exception {
                                        return filterBean.getName().equals(name);
                                    }
                                });
                    }
                });
        Observable.concat(B,C,D)
                .doOnNext(new Consumer<FilterBean>() {
                    @Override
                    public void accept(@NonNull FilterBean filterBean) throws Exception {
                        filterBean.setSelected(false);
                        filterBean.setSelectedName("");
                    }
                })
                .flatMap(new Function<FilterBean, ObservableSource<FilterItem>>() {
                    @Override
                    public ObservableSource<FilterItem> apply(@NonNull FilterBean filterBean) throws Exception {
                        return Observable.fromIterable(filterBean.getDataSet());
                    }
                })
                .doOnNext(new Consumer<FilterItem>() {
                    @Override
                    public void accept(@NonNull FilterItem filterItem) throws Exception {
                        filterItem.setSelected(false);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FilterItem>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull FilterItem filterItem) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, e.toString());
                        printErr(e);
                    }

                    @Override
                    public void onComplete() {
                        selectedIcon.setVisibility(View.VISIBLE);
                        verticalFilterAdapter.notifyDataSetChanged();
                        verticalFilterItemAdapter.notifyDataSetChanged();
                        horizontalFilterAdapter.notifyDataSetChanged();
                    }
                });
    }

    @OnClick(R.id.vertical_confirm)
    public void onViewClickedVerticalConfirm(View view) {
        drawerLayout.closeDrawer(Gravity.RIGHT);
        changeSearchGoods();
    }

    @OnClick(R.id.vertical_reset)
    public void onViewClickedVerticalCancel(View view) {
        clearStatus();
    }

    @OnClick(R.id.goshopcat)
    public void onViewClickedGoshopcat() {
        Intent it = new Intent();
        it.setClass(SearchGoodActivity.this, ShopCartActivity.class);
        startActivity(it);
    }

    public static class FilterHelper {
        private SearchFilterRes.DataBean searchFilterRes;

        public FilterHelper(SearchFilterRes searchFilterRes) {
            this.searchFilterRes = searchFilterRes.getData();
        }

        public ArrayList<FilterBean> invoke() {
            ArrayList<FilterBean> res = new ArrayList<>();
            if (searchFilterRes.getBrandList() != null && searchFilterRes.getBrandList().size() != 0) {
                FilterBean brandBean = new FilterBean();
                brandBean.setType(FilterType.BRAND);
                brandBean.setName("品牌");
                for (SearchFilterRes.DataBean.BrandListBean bean : searchFilterRes.getBrandList()) {
                    FilterItem item = new FilterItem();
                    item.setId(bean.getId());
                    item.setName(bean.getName());
                    brandBean.addItem(item);
                }
                res.add(brandBean);
            }
            if (searchFilterRes.getClass3List() != null && searchFilterRes.getClass3List().size() != 0) {
                FilterBean cateBean = new FilterBean();
                cateBean.setType(FilterType.CATEGORY);
                cateBean.setName("分类");
                for (SearchFilterRes.DataBean.Class3ListBean bean : searchFilterRes.getClass3List()) {
                    FilterItem item = new FilterItem();
                    item.setName(bean.getName());
                    item.setId(bean.getId());
                    cateBean.addItem(item);
                }
                res.add(cateBean);
            }
            if (searchFilterRes.getPropertyList() != null && searchFilterRes.getPropertyList().size() != 0) {
                for (PropertyListBean BiBean : searchFilterRes.getPropertyList()) {
                    FilterBean peopertyBean = new FilterBean();
                    peopertyBean.setType(FilterType.PEOPERTY);
                    peopertyBean.setName(BiBean.getName());
                    for (String attr : BiBean.getValue()) {
                        FilterItem item = new FilterItem();
                        item.setName(attr);
                        peopertyBean.addItem(item);
                    }
                    res.add(peopertyBean);
                }
            }
            return res;
        }

        private ArrayList<FilterBean> getprice(ArrayList<FilterBean> res) {
            if (searchFilterRes.getPriceList() != null && searchFilterRes.getPriceList().size() != 0) {
                FilterBean priceBean = new FilterBean();
                priceBean.setType(FilterType.PRICE);
                priceBean.setName("价格");
                for (SearchFilterRes.DataBean.PriceListBean bean : searchFilterRes.getPriceList()) {
                    FilterItem item = new FilterItem();
                    item.setMaxPrice(bean.getMaxPrice());
                    item.setMinPrice(bean.getMinPrice());
                    item.setName(bean.getMinPrice() + " --- " + bean.getMaxPrice());
                    priceBean.addItem(item);
                }
                res.add(2, priceBean);
            }
            return res;
        }

        public ArrayList<FilterBean> invokeWithPrice() {
            return getprice(invoke());
        }
    }

    private void printErr(@NonNull Throwable e) {
        for (int index = 0; index < e.getStackTrace().length; index++) {
            Log.d(TAG, e.getStackTrace()[index].toString());
        }
    }

}
