package com.weihuagu.receiptnotice;

import android.support.v7.app.AppCompatActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.SharedPreferences;
import android.widget.Toast;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private Button btnsetposturl;
    private EditText posturl;
    private SharedPreferences sp ;
   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        btnsetposturl=(Button) findViewById(R.id.btnsetposturl);
        btnsetposturl.setOnClickListener(this);
        posturl = (EditText) findViewById(R.id.posturl);
	sp = getSharedPreferences("url", Context.MODE_PRIVATE);
        
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isAuthor=isNotificationServiceEnable();
        if (!isAuthor){
            //直接跳转通知授权界面
            //android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS是API 22才加入到Settings里，这里直接写死
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 是否已授权
     *
     * @return
     */
    private boolean isNotificationServiceEnable() {
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsetposturl:
                setPostUrl();
                break;
        }
    }

    private void setPostUrl() {
       SharedPreferences.Editor edit = sp.edit();
                //通过editor对象写入数据
                edit.putString("posturl",posturl.getText().toString());
                //提交数据存入到xml文件中
                edit.commit();
		Toast.makeText(getApplicationContext(), "已经设置posturl为："+posturl.getText().toString(),
     Toast.LENGTH_SHORT).show();
    }

   


}
