package com.zz.wanandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.zz.wanandroid.adapter.MyPageAdapter;
import com.zz.wanandroid.util.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zz
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private static final String TAG = "MainActivity";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_mine:
                    viewPager.setCurrentItem(3);
                    break;
                default:
                    break;
            }
            return false;
        }
    };

    private ViewPager viewPager;
    private List<Fragment> fragments;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        }
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "玩Android", Toast.LENGTH_SHORT).show();
            }
        });

        navigation = findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.myViewPage);

        fragments = new ArrayList<>();
        fragments.add(HomePageFragment.newInstance());
        fragments.add(TreeListFragment.newInstance());
        fragments.add(ProjectListFragment.newInstance());
        fragments.add(MineFragment.newInstance());

        MyPageAdapter myPageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments, null);
        viewPager.setAdapter(myPageAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tools_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.friend:
                Intent intent2 = new Intent(this,FriendsWebsiteActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        navigation.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
