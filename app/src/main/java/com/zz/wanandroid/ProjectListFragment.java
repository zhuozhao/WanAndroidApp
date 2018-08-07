package com.zz.wanandroid;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zz.wanandroid.adapter.MyPageAdapter;
import com.zz.wanandroid.bean.Tree;
import com.zz.wanandroid.viewmodel.ProjectTreeModel;

import java.util.ArrayList;
import java.util.List;


public class ProjectListFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ProjectListFragment";

    private TabLayout tabLayout;
    private ViewPager tabViewPager;
    private ProjectTreeModel projectTreeModel;
    private List<Fragment> fragments;
    private List<Tree> trees;
    private MyPageAdapter myPageAdapter;
    private View viewNodata;

    public ProjectListFragment() {
    }

    public static ProjectListFragment newInstance() {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        fragments = new ArrayList<>();
        trees = new ArrayList<>();
        myPageAdapter = new MyPageAdapter(getChildFragmentManager(),fragments,trees);
        projectTreeModel = ViewModelProviders.of(this).get(ProjectTreeModel.class);
        projectTreeModel.getListMutableLiveData().observe(this, new Observer<List<Tree>>() {
            @Override
            public void onChanged(@Nullable List<Tree> ts) {

                Log.d(TAG, "onChanged: ts:"+ts);
                trees.clear();
                if(ts!=null){
                    trees.addAll(ts);
                }
                fragments.clear();
                for (Tree tree :trees){
                    ProjectsFragment fragment = ProjectsFragment.newInstance(""+tree.getName(),+tree.getId());
                    fragments.add(fragment);
                }
                myPageAdapter.notifyDataSetChanged();

                if(ts==null){
                    viewNodata.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabViewPager = view.findViewById(R.id.tabViewPager);
        tabViewPager.setAdapter(myPageAdapter);
        tabLayout.setupWithViewPager(tabViewPager);

        if(projectTreeModel.getListMutableLiveData().getValue()==null){
            projectTreeModel.updateDate();
        }
        viewNodata = view.findViewById(R.id.layoutNoData);
        viewNodata.setOnClickListener(this);

        if(projectTreeModel.getListMutableLiveData().getValue()==null){
            projectTreeModel.updateDate();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.layoutNoData){
            viewNodata.setVisibility(View.GONE);
            projectTreeModel.updateDate();
        }
    }
}
