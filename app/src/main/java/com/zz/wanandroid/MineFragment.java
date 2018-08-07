package com.zz.wanandroid;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.xw.repo.VectorCompatTextView;
import com.zz.wanandroid.app.MyApplication;
import com.zz.wanandroid.bean.User;


public class MineFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "MineFragment";
    private VectorCompatTextView aboutBtn,favoriteBtn;
    private ImageView headImageView;
    private TextView usernameTv;
    private RelativeLayout nightModeLayout;
    private Switch dayModeSwitch;
    public MineFragment() {
    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        aboutBtn = view.findViewById(R.id.aboutBtn);
        headImageView = view.findViewById(R.id.headImageView);
        usernameTv = view.findViewById(R.id.usernameTv);
        favoriteBtn = view.findViewById(R.id.favoriteBtn);
        nightModeLayout = view.findViewById(R.id.nightModeLayout);
        aboutBtn.setOnClickListener(this);
        headImageView.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);

        dayModeSwitch = view.findViewById(R.id.dayModeSwitch);
        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            dayModeSwitch.setChecked(true);
        }else {
            dayModeSwitch.setChecked(false);
        }
        dayModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
                    ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }else {
                    ((AppCompatActivity)getActivity()).getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.aboutBtn:
                intent.setClass(getActivity(),AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.headImageView:
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.favoriteBtn:
                intent.setClass(getActivity(),MyCollectActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        showUsername();
        super.onResume();
    }


    private void showUsername(){

        User user = MyApplication.getINSTANCE().getUser();
        if(user!=null){
            if(usernameTv!=null){
                usernameTv.setText(user.getUsername()+"");
            }
        }else {
            if(usernameTv!=null){
                usernameTv.setText("点击登录");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
