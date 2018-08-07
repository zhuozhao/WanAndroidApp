package com.zz.wanandroid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zz.wanandroid.adapter.ArticleListQueryAdapter;
import com.zz.wanandroid.adapter.MyItemDecoration;
import com.zz.wanandroid.adapter.RecycleViewOnItemClickListener;
import com.zz.wanandroid.bean.Article;
import com.zz.wanandroid.bean.Hotkey;
import com.zz.wanandroid.bean.PageData;
import com.zz.wanandroid.bean.SearchHistory;
import com.zz.wanandroid.viewmodel.ArticleQueryViewModel;
import com.zz.wanandroid.viewmodel.HotkeyViewModel;
import com.zz.wanandroid.viewmodel.SearchHistoryViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * 关键字搜索
 *
 * @author zz
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "SearchActivity";
    private HotkeyViewModel hotkeyViewModel;
    private RecyclerView hotkeyRecycleView;
    private FlexboxLayout hotkeyFlexboxLayout, historyFlexboxLayout;
    private ArticleQueryViewModel queryViewModel;
    private int pageNum = 0;
    private List<Article> articleList;
    private ArticleListQueryAdapter articleListAdapter;
    private LinearLayout hotkeyLayout;
    private SearchHistoryViewModel historyViewModel;
    private List<SearchHistory> histories;
    private RefreshLayout refreshLayout;
    private SearchView searchView;
    private LinearLayout searchDataLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        findView();
        histories = new ArrayList<>();
        articleList = new ArrayList<>();
        articleListAdapter = new ArticleListQueryAdapter(this, articleList);
        articleListAdapter.setOnItemClickListener(new RecycleViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Article article = articleList.get(position);
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, ArticleInfoActivity.class);
                intent.putExtra("article",article);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        hotkeyRecycleView.setLayoutManager(layoutManager);
        hotkeyRecycleView.addItemDecoration(new MyItemDecoration(this));
        hotkeyRecycleView.setAdapter(articleListAdapter);

        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 0;
                if (searchView.getQuery().length() == 0) {
                    refreshLayout.finishRefresh();
                    return;
                }
                queryViewModel.articleQuery(pageNum, searchView.getQuery() + "");
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //传入false表示加载失败
                if (searchView.getQuery().length() == 0) {
                    refreshLayout.finishLoadMore();
                    return;
                }
                queryViewModel.articleQuery(pageNum, searchView.getQuery() + "");
            }
        });

        initViewModel();

        historyViewModel.getHistories(-1);
        hotkeyViewModel.getHotkeys();
    }

    private void findView() {
        hotkeyRecycleView = findViewById(R.id.hotkeyRecycleView);
        hotkeyFlexboxLayout = findViewById(R.id.hotkeyFlexboxLayout);
        historyFlexboxLayout = findViewById(R.id.historyFlexboxLayout);
        searchDataLayout = findViewById(R.id.searchDataLayout);
        hotkeyLayout = findViewById(R.id.hotkeyLayout);
    }

    private void initViewModel() {

        //搜索历史
        historyViewModel = ViewModelProviders.of(this).get(SearchHistoryViewModel.class);
        historyViewModel.getListMutableLiveData().observe(this, new Observer<List<SearchHistory>>() {
            @Override
            public void onChanged(@Nullable List<SearchHistory> hs) {
                histories.clear();
                histories.addAll(hs);
                addHistoryView(histories);
            }
        });


        //热搜词汇
        hotkeyViewModel = ViewModelProviders.of(this).get(HotkeyViewModel.class);
        hotkeyViewModel.getListMutableLiveData().observe(this, new Observer<List<Hotkey>>() {
            @Override
            public void onChanged(@Nullable List<Hotkey> hotkeys) {
                if(hotkeys==null){
                    return;
                }
                addHotkeyView(hotkeys);
            }
        });

        //查询
        queryViewModel = ViewModelProviders.of(this).get(ArticleQueryViewModel.class);
        queryViewModel.getPageDataMutableLiveData().observe(this, new Observer<PageData<Article>>() {
            @Override
            public void onChanged(@Nullable PageData<Article> articlePageData) {

                if(pageNum==0){
                    refreshLayout.finishRefresh(articlePageData==null?false:true);
                }else {
                    refreshLayout.finishLoadMore(articlePageData==null?false:true);
                }

                if (articlePageData==null){
                    return;
                }

                if(pageNum==0){
                    articleList.clear();
                    if (articlePageData!=null&&articlePageData.getTotal()==0){
                        Snackbar snackbar = Snackbar.make(searchDataLayout,"暂无查询到相关文章", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        snackbar.show();
                    }
                }else {
                    if (articlePageData!=null&&articlePageData.isOver()) {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                        Snackbar snackbar = Snackbar.make(searchDataLayout,"暂无更多文章", Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        snackbar.show();
                    }
                }
                articleList.addAll(articlePageData.getDatas());
                articleListAdapter.notifyDataSetChanged();
                pageNum++;
            }
        });

    }

    /**
     * 添加热搜词汇
     * @param hotkeys
     */
    private void addHotkeyView(List<Hotkey> hotkeys) {

        for (final Hotkey hotkey : hotkeys) {

            View view = LayoutInflater.from(this).inflate(R.layout.layout_hotkey, null);
            TextView textView = view.findViewById(R.id.hotkeyTextView);
            textView.setText(hotkey.getName());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pageNum = 0;
                    searchView.setQuery(hotkey.getName(), true);
                }
            });
            hotkeyFlexboxLayout.addView(view);
        }
    }

    /**
     * 添加搜索历史词汇
     * @param histories
     */
    private void addHistoryView(List<SearchHistory> histories) {

        historyFlexboxLayout.removeAllViews();
        for (final SearchHistory history : histories) {

            View view = LayoutInflater.from(this).inflate(R.layout.layout_hotkey, null);
            TextView textView = view.findViewById(R.id.hotkeyTextView);
            textView.setText(history.getKey());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pageNum = 0;
                    searchView.setQuery(history.getKey(), true);
                }
            });
            historyFlexboxLayout.addView(view);
        }
    }

    public void deleteAll(View view) {
        historyViewModel.deleteHistories(histories);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        search.collapseActionView();
        //是搜索框默认展开
        search.expandActionView();

        searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("请输入搜索内容");
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(TAG, "onClose: ");
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotkeyLayout.setVisibility(View.VISIBLE);
                searchDataLayout.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.search:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        if (query.length() == 0) {
            return false;
        }
        pageNum = 0;
        hotkeyLayout.setVisibility(View.GONE);
        searchDataLayout.setVisibility(View.VISIBLE);
        refreshLayout.autoRefresh();
        SearchHistory history = new SearchHistory();
        history.setKey(query);
        history.setUserId(-1);
        historyViewModel.insertHistory(history);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange: ");
        if(newText.length()==0){
            hotkeyLayout.setVisibility(View.VISIBLE);
            searchDataLayout.setVisibility(View.GONE);
        }
        return false;
    }
}
