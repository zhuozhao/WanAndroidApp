package com.zz.wanandroid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zz.wanandroid.adapter.ArticleListAdapter;
import com.zz.wanandroid.adapter.BannerPageAdapter;
import com.zz.wanandroid.adapter.MyItemDecoration;
import com.zz.wanandroid.adapter.RecycleViewOnItemClickListener;
import com.zz.wanandroid.bean.Article;
import com.zz.wanandroid.bean.Banner;
import com.zz.wanandroid.bean.PageData;
import com.zz.wanandroid.viewmodel.ArticleViewModel;
import com.zz.wanandroid.viewmodel.BannerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * @author zz
 */
public class HomePageFragment extends Fragment {

    private static final String TAG = "HomePageFragment";
    private ArticleViewModel articleViewModel;
    private BannerModel bannerModel;
    private RecyclerView recyclerView;
    private BannerPageAdapter bannerPageAdapter;
    private List<View> views;
    private ViewPager viewPager;

    private List<Article> articleList;
    private ArticleListAdapter articleListAdapter;
    private int currentPage =0;
    private   RefreshLayout refreshLayout;
    public static HomePageFragment newInstance() {
        final HomePageFragment fragment = new HomePageFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        views = new ArrayList<>();
        bannerPageAdapter = new BannerPageAdapter(views);

        articleList = new ArrayList<>();
        articleListAdapter = new ArticleListAdapter(getActivity(),articleList);

        bannerModel = ViewModelProviders.of(this).get(BannerModel.class);
        bannerModel.getListMutableLiveData().observe(this, new Observer<List<Banner>>() {
            @Override
            public void onChanged(@Nullable List<Banner> banners) {

                Log.d(TAG, "onChanged: banners:"+banners);
                if(banners==null){
                    return;
                }
                views.clear();
                for (Banner banner :banners){
                    Log.d(TAG, "onChanged: "+banner.getTitle());

                    LayoutInflater lf = LayoutInflater.from(getActivity());
                    View view = lf.inflate(R.layout.banner_view, null);
                    ImageView imageView = view.findViewById(R.id.imageView);
                    TextView bannerTitle = view.findViewById(R.id.bannerTitle);
                    bannerTitle.setText(banner.getTitle()+"");
                    Glide.with(HomePageFragment.this).load(banner.getImagePath()).into(imageView);
                    views.add(view);
                }
                bannerPageAdapter.notifyDataSetChanged();
            }
        });

        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        articleViewModel.getPageDataMutableLiveData().observe(this, new Observer<PageData<Article>>() {
            @Override
            public void onChanged(@Nullable PageData<Article> articlePageData) {

                Log.d(TAG, "onChanged: articlePageData:"+articlePageData);

                if(currentPage==0){
                    refreshLayout.finishRefresh(articlePageData==null?false:true);
                }else {
                    refreshLayout.finishLoadMore(articlePageData==null?false:true);
                }

                if (articlePageData==null){
                    return;
                }

                if(currentPage==0){
                    articleList.clear();
                }
                articleList.addAll(articlePageData.getDatas());
                articleListAdapter.notifyDataSetChanged();
                currentPage++;

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyItemDecoration(getActivity()));
        recyclerView.setAdapter(articleListAdapter);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_head,container,  false);
        viewPager = headView.findViewById(R.id.viewPager);
        viewPager.setAdapter(bannerPageAdapter);

        articleListAdapter.setHeadView(headView);
        articleListAdapter.setOnItemClickListener(new RecycleViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Article article = articleList.get(position-1);
                Intent intent = new Intent();
                intent.setClass(getActivity(),ArticleInfoActivity.class);
                intent.putExtra("article",article);
                startActivity(intent);
            }
        });

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                currentPage =0;
                articleViewModel.getArticle(0);
                bannerModel.updateDate();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //传入false表示加载失败
                articleViewModel.getArticle(currentPage);
            }
        });
        refreshLayout.autoRefresh();
        return view;
    }


}
