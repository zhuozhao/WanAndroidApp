package com.zz.wanandroid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zz.wanandroid.adapter.MyItemDecoration;
import com.zz.wanandroid.adapter.RecycleViewOnItemClickListener;
import com.zz.wanandroid.adapter.TreeArticleListAdapter;
import com.zz.wanandroid.bean.Article;
import com.zz.wanandroid.bean.PageData;
import com.zz.wanandroid.bean.Tree;
import com.zz.wanandroid.bean.TreeChildren;
import com.zz.wanandroid.viewmodel.ProjectListViewModel;
import com.zz.wanandroid.viewmodel.TreeArticleViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 体系列表文章
 * @author Administrator
 */
public class TreeArticlesFragment extends Fragment {

    private static final String TAG = "TreeArticlesFragment";
    private static final String ARG_PARAM1 = "param1";
    private Tree tree;
    private List<TreeChildren> treeChildrens;
    private RecyclerView treeRecyclerView;

    private TreeArticleViewModel treeArticleViewModel;
    private List<Object> objectList;
    private TreeArticleListAdapter articleListAdapter;
    private RefreshLayout refreshLayout;
    private int index =0;
    public TreeArticlesFragment() {
        // Required empty public constructor
    }

    public static TreeArticlesFragment newInstance(Tree tree) {
        TreeArticlesFragment fragment = new TreeArticlesFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, tree);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tree = (Tree) getArguments().getSerializable(ARG_PARAM1);
            treeChildrens = tree.getChildren();
        }

        objectList = new ArrayList<>();
        articleListAdapter = new TreeArticleListAdapter(getActivity(), objectList);

        treeArticleViewModel = ViewModelProviders.of(this).get(TreeArticleViewModel.class);
        treeArticleViewModel.getPageDataMutableLiveData().observe(this, new Observer<PageData<Article>>() {
            @Override
            public void onChanged(@Nullable PageData<Article> articlePageData) {

                if(index==0){
                    objectList.clear();
                    refreshLayout.finishRefresh(200);
                }else {
                    refreshLayout.finishLoadMore(100);
                }
                if(articlePageData==null){
                    return;
                }
                index++;
                List<Article> articles = articlePageData.getDatas();
                if (articles.size() > 0) {
                    Article article = articles.get(0);
                    for (TreeChildren children : treeChildrens) {
                        if (children.getId() == article.getChapterId()) {
                            objectList.add(children);
                            objectList.addAll(articles);
                            break;
                        }
                    }
                }
                articleListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree_articles, container, false);
        treeRecyclerView = view.findViewById(R.id.treeRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        treeRecyclerView.setLayoutManager(layoutManager);
        treeRecyclerView.addItemDecoration(new MyItemDecoration(getActivity()));
        treeRecyclerView.setAdapter(articleListAdapter);

        articleListAdapter.setOnItemClickListener(new RecycleViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Article article = (Article) objectList.get(position);
                Intent intent = new Intent();
                intent.setClass(getActivity(), ArticleInfoActivity.class);
                intent.putExtra("article",article);
                startActivity(intent);
            }
        });

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                index = 0;
                treeArticleViewModel.getTreeArticle(0, treeChildrens.get(0).getId());
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //传入false表示加载失败
                if(index<treeChildrens.size()){
                    treeArticleViewModel.getTreeArticle(0, treeChildrens.get(index).getId());
                }else {
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }
            }
        });

        if(objectList.size()==0){
            refreshLayout.autoRefresh();
        }

        return view;
    }

    private void update(){
        List<TreeChildren> childrenList = tree.getChildren();
        for (TreeChildren children : childrenList) {
            treeArticleViewModel.getTreeArticle(0, children.getId());
        }
    }

}
