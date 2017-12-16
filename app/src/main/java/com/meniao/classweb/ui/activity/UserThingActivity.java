package com.meniao.classweb.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.meniao.classweb.R;
import com.meniao.classweb.Utils;
import com.meniao.classweb.db.User;
import com.meniao.classweb.views.StatusBarCompat;

import org.w3c.dom.Text;

/**
 * Created by Meniao Company on 2017/9/16.
 */

public class UserThingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageView;
    private TextView textView;

    private Intent intent;
    private String name;
    private String phone;
    private String qq;
    private String weixin;
    private String email;
    private String id;
    private Integer integer;

    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_qq;
    private TextView tv_weixin;
    private TextView tv_email;
    private TextView tv_money;
    private CardView tv_logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_thing);

        init();
        initViews();
        initToolbar();
        initUser();
    }

    private void init() {
        StatusBarCompat.compat(UserThingActivity.this, getResources().getColor(R.color.colorContentPrimaryDark));

        intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        qq = intent.getStringExtra("qq");
        weixin = intent.getStringExtra("weixin");
        email = intent.getStringExtra("email");
        id = intent.getStringExtra("id");
        integer = intent.getIntExtra("integer", 0);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.image);
        textView = (TextView) findViewById(R.id.name);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.phone);
        tv_qq = (TextView) findViewById(R.id.qq);
        tv_weixin = (TextView) findViewById(R.id.weixin);
        tv_email = (TextView) findViewById(R.id.email);
        tv_logout = (CardView) findViewById(R.id.logout);
        tv_money = (TextView) findViewById(R.id.money);
    }

    private void initUser() {
        String a = name.substring(0, 1);

        textView.setText(name);
        TextDrawable drawable2 = TextDrawable.builder()
                .buildRound(a, Color.parseColor(Utils.getARadomColors()));
        imageView.setImageDrawable(drawable2);

        tv_name.setText(name);
        tv_phone.setText(phone);
        tv_qq.setText(qq);
        tv_weixin.setText(weixin);
        tv_email.setText(email);
        tv_money.setText("赚了 " + integer.intValue() + " 元");

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logOut();
                UserThingActivity.this.finish();
            }
        });
    }

    private void initToolbar() {
        toolbar.setTitle("用户信息");
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorContentPrimaryDark));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextAppearance(this, R.style.ToolbarCustomTitle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        if (item.getItemId() == R.id.change) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("修改信息请到关于界面联系作者");
            builder.setPositiveButton("知道了", null);
            builder.create().show();
        }
        return true;
    }
}
