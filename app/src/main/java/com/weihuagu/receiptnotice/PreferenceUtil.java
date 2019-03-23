package com.weihuagu.receiptnotice;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
public class PreferenceUtil{
        SharedPreferences sharedPref=null;
        Context context=null;
        public PreferenceUtil(Context context){
                this.context=context;
                init();
        }
        public void init(){
                sharedPref=PreferenceManager.getDefaultSharedPreferences(this.context);

        }
        public String getDeviceid(){
                return this.sharedPref.getString("deviceid","");
        }
        public boolean isEncrypt(){
                return this.sharedPref.getBoolean("isencrypt",false);
        }
        public String getEncryptMethod(){
                return this.sharedPref.getString("encryptmethod",null);
        }
        public String getPasswd(){
                return this.sharedPref.getString("passwd",null);
        }

}
