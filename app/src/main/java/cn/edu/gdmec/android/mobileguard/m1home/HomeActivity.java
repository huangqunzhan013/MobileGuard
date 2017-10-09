package cn.edu.gdmec.android.mobileguard.m1home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import android.text.TextUtils;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m1home.adapter.HomeAdapter;
import cn.edu.gdmec.android.mobileguard.m2theftguard.dialog.InterPasswordDialog;
import cn.edu.gdmec.android.mobileguard.m2theftguard.dialog.SetupPasswordDialog;
import cn.edu.gdmec.android.mobileguard.m2theftguard.utils.MD5Utils;


public class HomeActivity extends AppCompatActivity {
    private GridView gv_home;
    private long mExitTime;
    //存储手机防盗密码
    private SharedPreferences msharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        gv_home = (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.print(i);
                switch (i) {
                    case 0:
                        if (isSetUpPassword()) {
                            showInterPswdDialog();
                        } else {
                            showSetUpPswdDialog();
                        }
                        break;
                }
            }
        });
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(HomeActivity.this, cls);
        startActivity(intent);
    }

    //退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) < 2000) {
                System.exit(0);
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show();
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //弹出密码对话框
    private void showSetUpPswdDialog() {
        final SetupPasswordDialog setupPasswordDialog = new SetupPasswordDialog(HomeActivity.this);
        setupPasswordDialog.setCallBack(new SetupPasswordDialog.MyCallBack() {
            @Override
            public void ok() {
                String firstPwsd = setupPasswordDialog.mFirstPWDET.getText().toString().trim();
                String affirmPwsd = setupPasswordDialog.mAffirmET.getText().toString().trim();
                if (!TextUtils.isEmpty(firstPwsd) && !TextUtils.isEmpty(affirmPwsd)) {
                    if (firstPwsd.equals(affirmPwsd)) {
                        //存储密码
                        savePswd(affirmPwsd);
                        setupPasswordDialog.dismiss();
                        showInterPswdDialog();
                    } else {
                        Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void cancel() {
                setupPasswordDialog.dismiss();
            }
        });
        setupPasswordDialog.setCancelable(true);
        setupPasswordDialog.show();
    }

    private void showInterPswdDialog() {
        final String password = getPassword();
        final InterPasswordDialog mInPswdDialog = new InterPasswordDialog(HomeActivity.this);
        mInPswdDialog.setMyCallBack(new InterPasswordDialog.MyCallBack() {
            @Override
            public void confirm() {
                if (TextUtils.isEmpty(mInPswdDialog.getPassword())) {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                } else if (password.equals(MD5Utils.encode(mInPswdDialog.getPassword()))) {
                    //进入防盗主界面
                    mInPswdDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "可以进入手机防盗模块", Toast.LENGTH_LONG).show();
                } else {
                    mInPswdDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "密码有误，请重新输入", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void cancle() {
                mInPswdDialog.dismiss();
            }
        });
        mInPswdDialog.setCancelable(true);
        //对话框显示
        mInPswdDialog.show();
    }

    //保存密码
    private void savePswd(String affirmPwsd) {
        SharedPreferences.Editor editor = msharedPreferences.edit();
        //密码加密
        editor.putString("PhoneAntiTheftPWD", MD5Utils.encode(affirmPwsd));
        editor.commit();
    }

    //获取密码
    private String getPassword() {
        String password = msharedPreferences.getString("PhoneAntiTheftPWD", null);
        if(TextUtils.isEmpty(password)){
            return "";
        }
        return password;
    }
    //判断是否设置防盗密码
    private boolean isSetUpPassword(){
        String password = msharedPreferences.getString("PhoneAntiTheftPWD",null);
        if(TextUtils.isEmpty(password)){
            return false;
        }
        return true;
    }

}