package com.zz.wanandroid;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zz.wanandroid.adapter.MyItemDecoration;
import com.zz.wanandroid.adapter.ProjectListAdapter;
import com.zz.wanandroid.adapter.RecycleViewOnItemClickListener;
import com.zz.wanandroid.bean.Article;
import com.zz.wanandroid.bean.PageData;
import com.zz.wanandroid.viewmodel.BannerModel;
import com.zz.wanandroid.viewmodel.ProjectListViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectsFragment extends Fragment {
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "cid";
    private String name;
    private int cid;

    private RecyclerView projectRecyclerView;
    private ProjectListAdapter projectListAdapter;
    private List<Article> projectList;
    private ProjectListViewModel listViewModel;
    private int currentPage = 1;
    private RefreshLayout refreshLayout;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    public static ProjectsFragment newInstance(String param1, int param2) {
        ProjectsFragment fragment = new ProjectsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_PARAM1);
            cid = getArguments().getInt(ARG_PARAM2);
        }

        projectList = new ArrayList<>();
        projectListAdapter = new ProjectListAdapter(getActivity(), projectList);
        listViewModel = ViewModelProviders.of(this).get(ProjectListViewModel.class);

        listViewModel.getPageDataMutableLiveData().observe(this, new Observer<PageData<Article>>() {
            @Override
            public void onChanged(@Nullable PageData<Article> articlePageData) {

                if(refreshLayout.getState()==RefreshState.Refreshing){
                    refreshLayout.finishRefresh(articlePageData!=null);
                }
                if(refreshLayout.getState()==RefreshState.Loading){
                    refreshLayout.finishLoadMore(articlePageData!=null);
                }
                if (articlePageData==null){
                    return;
                }
                if (currentPage == 1) {
                    projectList.clear();
                }
                projectList.addAll(articlePageData.getDatas());
                projectListAdapter.notifyDataSetChanged();
                currentPage++;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        projectRecyclerView = view.findViewById(R.id.projectRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        projectRecyclerView.setLayoutManager(layoutManager);
        projectRecyclerView.addItemDecoration(new MyItemDecoration(getActivity()));
        projectRecyclerView.setAdapter(projectListAdapter);
        projectListAdapter.setOnItemClickListener(new RecycleViewOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Article article = projectList.get(position);
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
                currentPage = 1;
                listViewModel.getProjectList(currentPage, cid);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                //传入false表示加载失败
                listViewModel.getProjectList(currentPage, cid);
            }
        });
        if(projectList.size()==0){
            refreshLayout.autoRefresh();
        }
        return view;
    }

}
