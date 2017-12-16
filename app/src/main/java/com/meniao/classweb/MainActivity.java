package com.meniao.classweb;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.meniao.classweb.adapter.FragmentAdapter;
import com.meniao.classweb.db.User;
import com.meniao.classweb.ui.activity.RegisterActivity;
import com.meniao.classweb.ui.activity.UserThingActivity;
import com.meniao.classweb.ui.fragment.DynamicFragment;
import com.meniao.classweb.ui.fragment.RankingFragment;
import com.meniao.classweb.ui.fragment.TransactionFragment;
import com.meniao.classweb.utils.SharedPreferenceUtils;
import com.meniao.classweb.views.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private LinearLayout toolbar_linearlayout;

    private List<Fragment> list;
    private FragmentAdapter fragmentAdapter;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;


    private int color;
    private int resColor;
    private String[] title = new String[]{"广场", "交易", "排行"};

    private SharedPreferenceUtils sharedPreferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initViews();
        initToolbar();
        initTab();
    }

    private void init() {
        StatusBarCompat.compat(MainActivity.this, getResources().getColor(R.color.colorContentPrimaryDark));
        Bmob.initialize(this, "a90c1fa04989eedd493b5d6e45dcd870");

        sharedPreferenceUtils = new SharedPreferenceUtils(MainActivity.this, "shard_data");
        if (BmobUser.getCurrentUser(User.class) == null) {
            loginDialog();
        } else {
            User user = BmobUser.getCurrentUser(User.class);
            fetchUserInfo(); // 同步后台用户信息
            if (user.getNotice().intValue() == 1) {
                showWarningDialog();
            }
        }
    }

    // 设置彩色龙效果，主色，Toolbar 反色
    private void setColorToolbar(int color, int resColor) {
        StatusBarCompat.compat(MainActivity.this, color);
        toolbar.setBackgroundColor(resColor);
        tabLayout.setBackgroundColor(color);
        linearLayout.setBackgroundColor(color);
        toolbar_linearlayout.setBackgroundColor(color);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        toolbar_linearlayout = (LinearLayout) findViewById(R.id.toolbar_linearlayout);
    }

    private void initToolbar() {
        toolbar.setTitle(getResources().getString(R.string.title));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorContentPrimary));
        setSupportActionBar(toolbar);
    }

    private void initTab() {
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorHeight(7);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorContentPrimaryDark));
        linearLayout.setBackgroundColor(getResources().getColor(R.color.colorContentPrimaryDark));
        toolbar_linearlayout.setBackgroundColor(getResources().getColor(R.color.colorContentPrimaryDark));

        // tabLayout.addTab(tabLayout.newTab().setText(title[0]));
        // tabLayout.addTab(tabLayout.newTab().setText(title[1]));
        tabLayout.addTab(tabLayout.newTab().setText(title[0]));
        tabLayout.addTab(tabLayout.newTab().setText(title[1]));
        tabLayout.addTab(tabLayout.newTab().setText(title[2]));

        list = new ArrayList<>();
        list.add(new DynamicFragment());
        list.add(new RankingFragment());
        list.add(new TransactionFragment());

        // myAdapter = new MyAdapter(getSupportFragmentManager());
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(new DynamicFragment(), title[0]);
        fragmentAdapter.addFragment(new RankingFragment(), title[1]);
        fragmentAdapter.addFragment(new TransactionFragment(), title[2]);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        color = Color.parseColor("#D32F2F");
                        resColor = Color.parseColor("#F44336");
                        setColorToolbar(color, resColor);
                        break;
                    case 1:
                        color = Color.parseColor("#1976d2");
                        resColor = Color.parseColor("#2196f3");
                        setColorToolbar(color, resColor);
                        break;
                    case 2:
                        color = Color.parseColor("#388E3C");
                        resColor = Color.parseColor("#4CAF50");
                        setColorToolbar(color, resColor);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

//    private void selectTabFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.framelayout, fragment);
//        fragmentTransaction.commit();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_user:
                if (User.getCurrentUser() != null) {
                    User user = User.getCurrentUser(User.class);

                    Intent intent = new Intent(MainActivity.this, UserThingActivity.class);
                    intent.putExtra("name", user.getUsername());
                    intent.putExtra("phone", user.getPhone());
                    intent.putExtra("qq", user.getQq());
                    intent.putExtra("weixin", user.getWeixin());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("integer", user.getInteger());
                    intent.putExtra("id", user.getObjectId());
                    startActivity(intent);
                } else {
                    startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                }
                break;
        }
        return true;
    }

    private void loginDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(getResources().getString(R.string.content_login))
                .setPositiveButton(getResources().getString(R.string.login), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }

    private void showWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("检测到后台信息更新，请重新登录");
        builder.setCancelable(false);
        builder.setPositiveButton("登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User a = BmobUser.getCurrentUser(User.class);
                User user = new User();
                user.setNotice(0);
                user.update(a.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {

                    }
                });
                User.logOut();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        builder.create().show();
    }

    private void fetchUserInfo() {
        User.fetchUserInfo(new FetchUserInfoListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {

            }
        });
    }

    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}