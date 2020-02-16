package com.weihuagu.receiptnotice.view;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.weihuagu.receiptnotice.util.PreferenceUtil;
import com.weihuagu.receiptnotice.R;

public class IllustrateDecryptActivity extends AppCompatActivity {
        private TextView text_method;
        private TextView text_passwd;
        private TextView text_iv;
        private PreferenceUtil preference;
        private void initView() {
                text_method = (TextView) findViewById(R.id.info_text_method);
                text_passwd = (TextView) findViewById(R.id.info_text_passwd);
                text_iv = (TextView) findViewById(R.id.info_text_iv);
                preference=new PreferenceUtil(getBaseContext());

        }
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_illustratedecrypt);
                initView();
                setText();
        }

        private void setText(){
                String encrypt_type=preference.getEncryptMethod(); 
                if(encrypt_type==null){
                        text_method.setText("您没有设置加密方法");
                        return;
                }
                if(encrypt_type.equals("des")){
                        String method="DES/CBC/PKCS5Padding";
                        text_method.setText("解密的方法为:"+method);
                        String key=preference.getPasswd();
                        if(key!=null){
                                text_passwd.setText("解密秘钥为:"+key+"(des秘钥必须为8位,如果你设置的不是8位，请修改)");
                                text_iv.setText("解密的初始化向量为:"+key);
                        }
                }
                if(encrypt_type.equals("md5")){

                        text_method.setText("解密的方法为:"+"md5校验");
                        String key=preference.getPasswd();
                        if(key!=null){
                                text_passwd.setText("md5加密方法为md5(md5(price + type) + secretkey)");

                        }
                }

        }
}
