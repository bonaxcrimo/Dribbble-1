package com.simon.dribbble.ui.shots;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.simon.agiledevelop.log.LLog;
import com.simon.agiledevelop.mvpframe.BaseActivity;
import com.simon.agiledevelop.mvpframe.RxPresenter;
import com.simon.agiledevelop.state.StateView;
import com.simon.dribbble.R;
import com.simon.dribbble.data.model.ShotEntity;
import com.simon.dribbble.data.remote.DribbbleService;

import java.util.List;

/**
 * Created by: Simon
 * Email: simon.han0220@gmail.com
 * Created on: 2016/9/12 11:24
 */

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    private ShotsAdapter mShotsAdapter;
    private SearchView mSearchView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchPresenter getPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    protected StateView getLoadingView() {
        return (StateView) findViewById(R.id.stateView_loading);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setCommonBackToolBack(toolbar, "搜索");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mSearchView = (SearchView) findViewById(R.id.search_view);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rlv_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        mShotsAdapter = new ShotsAdapter();
        recyclerView.setAdapter(mShotsAdapter);
    }

    @Override
    protected void initEventAndData() {

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                LLog.d("onQueryTextSubmit: " + query);
                if (!TextUtils.isEmpty(query)) {
                    showLoading("正在搜索...");
                    showLoading(0, "");
                    search(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LLog.d("onQueryTextChange: " + newText);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    public void search(String query) {
        mSearchView.clearFocus();
        mPresenter.searchShot(query, 1, DribbbleService.SORT_POPULAR);
    }

    @Override
    public void showLoading(int action, String msg) {

    }

    @Override
    public void onEmpty(String msg) {
        showEmtry(msg, null);
    }

    @Override
    public void onFailed(int action, String msg) {
        showError(msg, null);
    }

    @Override
    public void onCompleted(int action) {

    }

    @Override
    public void setPresenter(RxPresenter presenter) {

    }

    @Override
    public void showSearch(List<ShotEntity> shots) {
        showContent();
        if (null != mShotsAdapter) {
            mShotsAdapter.appendData(shots);
        }
    }
}
