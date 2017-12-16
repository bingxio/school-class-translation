package com.meniao.classweb.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.meniao.classweb.R;
import com.meniao.classweb.db.User;
import com.meniao.classweb.views.StatusBarCompat;
import com.rengwuxian.materialedittext.MaterialEditText;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Meniao Company on 2017/9/15.
 */

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;

    private MaterialEditText materialEditText_name;
    private MaterialEditText materialEditText_pass;
    private MaterialEditText materialEditText_phone;
    private MaterialEditText materialEditText_qq;
    private MaterialEditText materialEditText_weixin;
    private MaterialEditText materialEditText_email;
    private Button button;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        init();
        initViews();
        initToolbar();
    }

    private void init() {
        StatusBarCompat.compat(AddUserActivity.this, getResources().getColor(R.color.colorContentPrimaryDark));
        toDialog();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        materialEditText_name = (MaterialEditText) findViewById(R.id.name);
        materialEditText_pass = (MaterialEditText) findViewById(R.id.pass);
        materialEditText_phone = (MaterialEditText) findViewById(R.id.phone);
        materialEditText_qq = (MaterialEditText) findViewById(R.id.qq);
        materialEditText_weixin = (MaterialEditText) findViewById(R.id.weixin);
        materialEditText_email = (MaterialEditText) findViewById(R.id.email);
        button = (Button) findViewById(R.id.register_ok);
        button.setOnClickListener(this);
    }

    private void initToolbar() {
        toolbar.setTitle(getResources().getString(R.string.register));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorContentPrimaryDark));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextAppearance(this, R.style.ToolbarCustomTitle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_ok) {
            String name = materialEditText_name.getText().toString();
            String pass = materialEditText_pass.getText().toString();
            String phone = materialEditText_phone.getText().toString();
            String qq = materialEditText_qq.getText().toString();
            String weixin = materialEditText_weixin.getText().toString();
            String email = materialEditText_email.getText().toString();

            int name_size = materialEditText_name.getText().length();
            int pass_size = materialEditText_pass.getText().length();
            int phone_size = materialEditText_phone.getText().length();
            int qq_size = materialEditText_qq.getText().length();
            int weixin_size = materialEditText_weixin.getText().length();
            int email_size = materialEditText_email.getText().length();

            if (TextUtils.isEmpty(name) | TextUtils.isEmpty(pass) | TextUtils.isEmpty(phone) | TextUtils.isEmpty(qq) |
                    TextUtils.isEmpty(weixin) | TextUtils.isEmpty(email)) {
                Toast.makeText(this, "有空未填", Toast.LENGTH_SHORT).show();
            } else if (name_size > 12 | name_size < 2 | pass_size > 12 | pass_size < 3 | phone_size > 11 | qq_size > 12 |
                    weixin_size > 18 | email_size > 15) {
                register(name, pass, phone, qq, weixin, email);
            } else {
                Toast.makeText(this, "格式错误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    private void toDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddUserActivity.this);
        builder.setMessage("必须填写正确的信息，以便交易成功");
        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void register(String name, final String pass, String phone, String qq, String weixin, String email) {
        progressDialog = new ProgressDialog(AddUserActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("登录中");
        progressDialog.setCancelable(false);
        progressDialog.show();

        User user = new User();
        user.setUsername(name);
        user.setPassword(pass);
        user.setPhone(phone);
        user.setQq(qq);
        user.setNotice(0);
        user.setWeixin(weixin);
        user.setEmail(email);
        user.setInteger(0);
        user.signUp(new SaveListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    progressDialog.dismiss();
                    Toast.makeText(AddUserActivity.this, "注册成功, 请登录", Toast.LENGTH_LONG).show();
                    AddUserActivity.this.finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(AddUserActivity.this, "注册失败" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
