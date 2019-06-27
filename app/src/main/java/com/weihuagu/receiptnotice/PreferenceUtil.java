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
        public boolean isEcho(){
                return this.sharedPref.getBoolean("isecho",false);
        }
        public String getEchoServer(){
                return this.sharedPref.getString("echoserver",null);
        }
        public String getEchoInterval(){
                return this.sharedPref.getString("echointerval","");
        }
        public String getEncryptMethod(){
                return this.sharedPref.getString("encryptmethod",null);
        }
        public String getPasswd(){
                return this.sharedPref.getString("passwd",null);
        }
        public boolean isRemoveNotification(){
                return this.sharedPref.getBoolean("isremovenotification",false);
        }
        public boolean isPostRepeat(){
                return this.sharedPref.getBoolean("ispostrepeat",false);
        }
        public String getPostRepeatNum(){
                return this.sharedPref.getString("postrepeatnum","3");
        }

}
