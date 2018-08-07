package com.zz.wanandroid;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zz.wanandroid.app.MyApplication;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.bean.User;
import com.zz.wanandroid.view.MyProgressDialogFragment;
import com.zz.wanandroid.viewmodel.UserViewModel;

/**
 * 注册账号
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
   private  UserViewModel userViewModel;
   private EditText usernameEdt, passwordEdt,repasswordEdt;
  // private TextInputLayout usernameLayout,passwordLayout,repasswordLayout;
    private MyProgressDialogFragment dialogFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getMutableLiveData().observe(this, new Observer<Result<User>>() {
            @Override
            public void onChanged(@Nullable Result<User> userResult) {

                //{"data":{"collectIds":[],"email":"","icon":"","id":7800,"password":"123456","type":0,"username":"zhaozhuo12345"},"errorCode":0,"errorMsg":""}
                //{"data":null,"errorCode":-1,"errorMsg":"用户名已经被注册！"}

                dialogFragment.dismiss();
                if(userResult!=null){
                    if (userResult.getErrorCode()==0&&userResult.getData()!=null){
                        MyApplication.getINSTANCE().setUser(userResult.getData());
                        Toast.makeText(RegisterActivity.this,"恭喜你注册成功!",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(RegisterActivity.this,userResult.getErrorMsg()+"",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(RegisterActivity.this,"注册失败,请稍后再试!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findView();
        addTextChanged();

        dialogFragment = MyProgressDialogFragment.newInstance("登录","正在登录...");

    }

    private void findView(){

        usernameEdt = findViewById(R.id.usernameEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        repasswordEdt = findViewById(R.id.repasswordEdt);

//        usernameLayout = findViewById(R.id.usernameLayout);
//        passwordLayout = findViewById(R.id.passwordLayout);
//        repasswordLayout = findViewById(R.id.repasswordLayout);

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
     * 点击提交注册
     * @param view
     */
    public void register(View view){


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

        if(!passwordEdt.getText().toString().equals(repasswordEdt.getText().toString())){
            repasswordEdt.requestFocus();
            repasswordEdt.setError("两次密码不一致");
            return;
        }
        dialogFragment.show(getSupportFragmentManager(),"register");
        userViewModel.register(usernameEdt.getText().toString(),passwordEdt.getText().toString(),repasswordEdt.getText().toString());
    }


    /**
     * 去登录
     * @param view
     */
    public void goToLogin(View view){
        Intent intent = new Intent();
        intent.setClass(this,LoginActivity.class);
        startActivity(intent);
        finish();
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
