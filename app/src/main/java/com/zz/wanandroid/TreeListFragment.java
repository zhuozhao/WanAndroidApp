package com.zz.wanandroid;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.zz.wanandroid.viewmodel.TreeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zz
 */
public class TreeListFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "TreeListFragment";

    private TreeModel treeModel;

    private List<Tree> treeList;
    private TabLayout tabLayout;
    private ViewPager tabViewPager;
    private List<Fragment> fragments;
    private MyPageAdapter myPageAdapter;
    private View viewNodata;

    public TreeListFragment() {
    }

    public static TreeListFragment newInstance() {
        TreeListFragment fragment = new TreeListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = new ArrayList<>();
        treeList = new ArrayList<>();
        myPageAdapter = new MyPageAdapter(getChildFragmentManager(),fragments,treeList);

        treeModel = ViewModelProviders.of(this).get(TreeModel.class);
        treeModel.getListMutableLiveData().observe(this, new Observer<List<Tree>>() {
            @Override
            public void onChanged(@Nullable List<Tree> trees) {

                Log.d(TAG, "onChanged: trees:"+trees);
                treeList.clear();
                fragments.clear();
                myPageAdapter.notifyDataSetChanged();
                if (trees != null) {
                    treeList.addAll(trees);
                }
                for (Tree tree :treeList){
                    TreeArticlesFragment fragment = TreeArticlesFragment.newInstance(tree);
                    fragments.add(fragment);
                }
                myPageAdapter.notifyDataSetChanged();

                if(trees==null){
                    viewNodata.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tree_list, container, false);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabViewPager = view.findViewById(R.id.tabViewPager);
        tabViewPager.setAdapter(myPageAdapter);
        tabLayout.setupWithViewPager(tabViewPager);
        viewNodata = view.findViewById(R.id.layoutNoData);
        viewNodata.setOnClickListener(this);

        if(treeModel.getListMutableLiveData().getValue()==null){
            treeModel.updateDate();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==R.id.layoutNoData){
            viewNodata.setVisibility(View.GONE);
            treeModel.updateDate();
        }
    }
}
