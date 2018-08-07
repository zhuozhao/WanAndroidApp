package com.zz.wanandroid;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zz.wanandroid.app.MyApplication;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.bean.User;
import com.zz.wanandroid.view.MyProgressDialogFragment;
import com.zz.wanandroid.viewmodel.UserViewModel;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private EditText passwordEdt,usernameEdt;
    private MyProgressDialogFragment dialogFragment;
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        usernameEdt =  findViewById(R.id.usernameEdt);
        passwordEdt =  findViewById(R.id.passwordEdt);
        addTextChanged();

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        dialogFragment = MyProgressDialogFragment.newInstance("登录","正在登录...");
        userViewModel.getMutableLiveData().observe(this, new Observer<Result<User>>() {
            @Override
            public void onChanged(@Nullable Result<User> userResult) {
                //{"data":{"collectIds":[],"email":"","icon":"","id":7831,"password":"123456","type":0,"username":"wanandroid2019"},"errorCode":0,"errorMsg":""}
                dialogFragment.dismiss();
                if(userResult!=null){
                    if (userResult.getErrorCode()==0&&userResult.getData()!=null){
                        MyApplication.getINSTANCE().setUser(userResult.getData());
                        saveUser(userResult.getData());
                        Toast.makeText(LoginActivity.this,"登录成功!",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this,userResult.getErrorMsg()+"",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(LoginActivity.this,"登录出错,请稍后再试!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        initUser();
    }

    private void initUser(){
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        usernameEdt.setText(sharedPreferences.getString("username",""));
        passwordEdt.setText( sharedPreferences.getString("password",""));
    }

    private void addTextChanged(){

        usernameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()<6){
                    usernameEdt.setError("用户名长度至少6位");
                }else {
                    usernameEdt.setError(null);
                }
            }
        });

        passwordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()<6){
                    passwordEdt.setError("密码长度至少6位");
                }else {
                    passwordEdt.setError(null);
                }
            }
        });



    }

    /**
     * 登录提交
     * @param view
     */
    public void login(View view){


        if(usernameEdt.getText().length()<6){
            usernameEdt.requestFocus();
            usernameEdt.setError("用户名长度至少6位");
            return;
        }

        if(passwordEdt.getText().length()<6){
            passwordEdt.requestFocus();
            passwordEdt.setError("密码长度至少6位");
            return;
        }

        dialogFragment.show(getSupportFragmentManager(),"register");

        userViewModel.login(usernameEdt.getText().toString(),passwordEdt.getText().toString());
    }

    /**
     * 去注册
     * @param view
     */
    public void goToRegister(View view){

        Intent intent = new Intent();
        intent.setClass(this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveUser(User user){


        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        //步骤2-2：将获取过来的值放入文件
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putString("userJson", new Gson().toJson(user));
        //步骤3：提交
        editor.apply();

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

