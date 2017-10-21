package cn.edu.gdmec.android.mobileguard.m2theftguard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.R;

/**
 * Created by Lenovo on 2017/10/14.
 */

public class Setup2Activity extends BaseSetupActivity implements View.OnClickListener{
    private TelephonyManager mTelephonyManager;
    private Button mBindSIMBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_2);
        ((RadioButton)findViewById(R.id.rb_second)).setChecked(true);
        //获取电话管理器这个系统服务
        mTelephonyManager= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //找到布局中SIM卡绑定这个按钮
        mBindSIMBtn= (Button) findViewById(R.id.btn_bind_sim);
        mBindSIMBtn.setOnClickListener(this);
        if(isBind()){
            mBindSIMBtn.setEnabled(false);
        }else {
            mBindSIMBtn.setEnabled(true);
        }

    }
    private boolean isBind(){
        String simString=sp.getString("sim",null);
        if(TextUtils.isEmpty(simString)){
            return false;
        }
        return true;
    }

    @Override
    public void showNext() {
        if(!isBind()){
            Toast.makeText(this,"您还没有绑定SIM卡！",Toast.LENGTH_LONG).show();
            return;
        }
        startActivityAndFinishSelf(Setup3Activity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(Setup1Activity.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_bind_sim:
                //绑定SIM卡
                bindSIM();
                break;
        }
    }
    //绑定SIM卡方法
    private void bindSIM(){
        if(!isBind()){
            //使用电话管理服务器来获取SIM卡号
            String simSerialNumber=mTelephonyManager.getSimSerialNumber();
            //存储SIM卡号
            SharedPreferences.Editor edit=sp.edit();
            edit.putString("sim",simSerialNumber);
            edit.commit();
            Toast.makeText(this,"SIM卡绑定成功！",Toast.LENGTH_LONG).show();
            mBindSIMBtn.setEnabled(false);
        }else {
            Toast.makeText(this,"SIM卡已经绑定！",Toast.LENGTH_LONG).show();
            mBindSIMBtn.setEnabled(false);
        }
    }
}
