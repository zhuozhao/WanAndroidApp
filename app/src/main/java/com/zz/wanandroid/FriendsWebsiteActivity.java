package com.zz.wanandroid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.zz.wanandroid.bean.Website;
import com.zz.wanandroid.viewmodel.FriendViewModel;

import java.util.List;

/**
 * 常用网站
 * @author Administrator
 */
public class FriendsWebsiteActivity extends AppCompatActivity {


    private FlexboxLayout friendFlexboxLayout;
    private FriendViewModel friendViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_website);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        friendFlexboxLayout = findViewById(R.id.friendFlexboxLayout);
        friendViewModel =  ViewModelProviders.of(this).get(FriendViewModel.class);
        friendViewModel.getListMutableLiveData().observe(this, new Observer<List<Website>>() {
            @Override
            public void onChanged(@Nullable List<Website> websites) {
                if (websites!=null){
                    addWebsiteView(websites);
                }
            }
        });

        friendViewModel.getFriendsWebsite();
    }


    private void addWebsiteView(List<Website> websites) {

        for (final Website website : websites) {

            View view = LayoutInflater.from(this).inflate(R.layout.layout_hotkey, null);
            TextView textView = view.findViewById(R.id.hotkeyTextView);
            textView.setText(website.getName());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    intent.setClass(FriendsWebsiteActivity.this,WebsiteActivity.class);
                    intent.putExtra("title",website.getName());
                    intent.putExtra("url",website.getLink());
                    startActivity(intent);
                }
            });
            friendFlexboxLayout.addView(view);
        }
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
