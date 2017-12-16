package com.meniao.classweb.ui.activity;

import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.meniao.classweb.R;
import com.meniao.classweb.db.User;
import com.meniao.classweb.views.StatusBarCompat;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Meniao Company on 2017/9/15.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView tv_forget;
    private TextView tv_register;
    private ProgressDialog progressDialog;

    private MaterialEditText materialEditText_name;
    private MaterialEditText materialEditText_pass;
    private Button button_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        initViews();
        initToolbar();
    }

    private void init() {
        StatusBarCompat.compat(RegisterActivity.this, getResources().getColor(R.color.colorContentPrimaryDark));
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        materialEditText_name = (MaterialEditText) findViewById(R.id.name);
        materialEditText_pass = (MaterialEditText) findViewById(R.id.pass);
        button_ok = (Button) findViewById(R.id.login_ok);
        button_ok.setOnClickListener(this);
        tv_forget = (TextView) findViewById(R.id.forget);
        tv_register = (TextView) findViewById(R.id.register);
        tv_forget.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    private void initToolbar() {
        toolbar.setTitle(getResources().getString(R.string.user));
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorContentPrimaryDark));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextAppearance(this, R.style.ToolbarCustomTitle);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_ok:
                String name = materialEditText_name.getText().toString();
                String pass = materialEditText_pass.getText().toString();

                login(name, pass);
                break;

            case R.id.forget:
                forget();
                break;

            case R.id.register:
                startActivity(new Intent(RegisterActivity.this, AddUserActivity.class));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    private void login(String name, String pass) {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("登录中");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (name.isEmpty() | pass.isEmpty()) {
            Toast.makeText(this, "空", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            User user = new User();
            user.setUsername(name);
            user.setPassword(pass);
            user.login(new SaveListener<User>() {

                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        Toast.makeText(RegisterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        RegisterActivity.this.finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "登录失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void forget() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
        dialog.setMessage("如果有其他问题，请在关于界面里联系作者");

        final EditText edit = new EditText(RegisterActivity.this);
        edit.setSingleLine(true);
        edit.setHint("输入注册时的邮箱");
        edit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edit.setTextSize(18);
        edit.setId(R.id.edit_forget_email);

        dialog.setView(edit, 65, 0, 65, 0);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String email = edit.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "空", Toast.LENGTH_SHORT).show();
                } else {
                    User.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null)
                                Toast.makeText(RegisterActivity.this, "重置密码邮件已发送到" + email + "中", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(RegisterActivity.this, "发送错误，此邮箱是否已注册？", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        dialog.create().show();
    }
}
