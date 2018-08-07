package com.zz.wanandroid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zz.wanandroid.adapter.CollectArticleListAdapter;
import com.zz.wanandroid.adapter.MyItemDecoration;
import com.zz.wanandroid.adapter.RecycleViewOnItemClickListener;
import com.zz.wanandroid.bean.Article;
import com.zz.wanandroid.bean.PageData;
import com.zz.wanandroid.viewmodel.ArticleQueryViewModel;
import com.zz.wanandroid.viewmodel.CollectViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏
 * @author zz
 */
public class MyCollectActivity extends AppCompatActivity {


    private CollectViewModel collectViewModel;
    private RecyclerView collectRecycleView;
    private RefreshLayout refreshLayout;
    private int pageNum=0;
    private List<Article> articleList;
    private CollectArticleListAdapter articleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        initRecyclerView();
        initRefresh();
        initViewMOdel();
        refreshLayout.autoRefresh();
    }

    private void initViewMOdel(){
        collectViewModel = ViewModelProviders.of(this).get(CollectViewModel.class);
        collectViewModel.getPageDataMutableLiveData().observe(this, new Observer<PageData<Article>>() {
            @Override
            public void onChanged(@Nullable PageData<Article> articlePageData) {

                //{"errorCode":-1,"errorMsg":"请先登录！"}

                if(pageNum==0){
                    refreshLayout.finishRefresh(articlePageData==null?false:true);
                }else {
                    refreshLayout.finishLoadMore(articlePageData==null?false:true);
                }

                if (articlePageData==null){
                    return;
                }

                if (articlePageData.getErrorCode()==-1){
                    Toast.makeText(MyCollectActivity.this,"请先登录!",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent();
                    intent.setClass(MyCollectActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }

                if(pageNum==0){
                    articleList.clear();
                    if (articlePageData!=null&&articlePageData.getTotal()==0){
                    }
                }else {
                    if (articlePageData!=null&&articlePageData.isOver()) {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
                articleList.addAll(articlePageData.getDatas());
                articleListAdapter.notifyDataSetChanged();
                pageNum++;
            }
        });
    }

    private void initRecyclerView(){

        collectRecycleView = findViewById(R.id.collectRecycleView);
        articleList = new ArrayList<>();
        articleListAdapter = new CollectArticleListAdapter(this,articleList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        collectRecycleView.setLayoutManager(layoutManager);
        collectRecycleView.addItemDecoration(new MyItemDecoration(this));
        articleListAdapter.setOnItemClickListener(new RecycleViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Article article = articleList.get(position);
                Intent intent = new Intent();
                intent.setClass(MyCollectActivity.this, ArticleInfoActivity.class);
                intent.putExtra("article",article);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });
        collectRecycleView.setAdapter(articleListAdapter);
    }

    private void initRefresh(){
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 0;
                collectViewModel.collectList(pageNum);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                collectViewModel.collectList(pageNum);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
