package com.weihuagu.receiptnotice;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.app.Notification;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.Build;

import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class NLService extends NotificationListenerService implements AsyncResponse, IDoPost {
        private String TAG="NLService";
        private String posturl=null;
        private Context context=null;
        private String getPostUrl(){
                SharedPreferences sp=getSharedPreferences("url", 0);
                this.posturl =sp.getString("posturl", "");
                if (posturl==null)
                        return null;
                else
                        return posturl;
        }


        @Override
        public void onNotificationPosted(StatusBarNotification sbn) {
                //        super.onNotificationPosted(sbn);
                //这里只是获取了包名和通知提示信息，其他数据可根据需求取，注意空指针就行
                
                if(getPostUrl()==null)
                        return;

                Notification notification = sbn.getNotification();
                String pkg = sbn.getPackageName();
                if (notification == null) {
                        return;
                }

                Bundle extras = notification.extras;
                if(extras==null)
                        return;

                //接受推送处理
                NotificationHandle notihandle =new NotificationHandleFactory().getNotificationHandle(pkg,notification,this);
                if(notihandle!=null){
                            notihandle.handleNotification();
                            return;
                }
                LogUtil.debugLog("-----------------");
                LogUtil.debugLog("接受到通知消息");
                LogUtil.debugLog("这是检测之外的其它通知");
                LogUtil.debugLog("包名是"+pkg);
                printNotify(getNotitime(notification),getNotiTitle(extras),getNotiContent(extras));
                LogUtil.debugLog("**********************");


        }

        @Override
        public void onNotificationRemoved(StatusBarNotification sbn) {
                if (Build.VERSION.SDK_INT >19)
                        super.onNotificationRemoved(sbn);
        }

        private void sendBroadcast(String msg) {
                Intent intent = new Intent(getPackageName());
                intent.putExtra("text", msg);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        private String getNotitime(Notification notification){

                long when=notification.when;
                Date date=new Date(when);
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String notitime=format.format(date);
                return notitime;

        }

        private String getNotiTitle(Bundle extras){
                String title=null;
                // 获取通知标题
                title = extras.getString(Notification.EXTRA_TITLE, "");
                return title;
        }

        private String getNotiContent(Bundle extras){
                String content=null;
                // 获取通知内容
                content = extras.getString(Notification.EXTRA_TEXT, "");
                return content;
        }

        private void printNotify(String notitime,String title,String content){
                Log.d(TAG,notitime);
                Log.d(TAG,title);
                Log.d(TAG,content);
        }


        public void doPost(Map<String, String> params){
                if(this.posturl==null)
                        return;
                PreferenceUtil preference=new PreferenceUtil(getBaseContext());
                Map<String, String> tmpmap=params;
                Map<String, String> postmap=null;
                String tasknum=RandomUtil.getRandomTaskNum();
                Log.d(TAG,"开始准备进行post");
                PostTask mtask = new PostTask();
                mtask.setRandomTaskNum(tasknum);
                mtask.setOnAsyncResponse(this);
                tmpmap.put("encrypt","0");
                tmpmap.put("url",this.posturl);
                String deviceid=preference.getDeviceid();
                tmpmap.put("deviceid",(!deviceid.equals("")? deviceid:DeviceInfoUtil.getUniquePsuedoID()));

                if(preference.isEncrypt()){
                        String encrypt_type=preference.getEncryptMethod();
                        if(encrypt_type!=null){
                                String key=preference.getPasswd();
                                EncryptFactory encryptfactory=new EncryptFactory(key);
                                Log.d(TAG,"加密方法"+encrypt_type);
                                Log.d(TAG,"加密秘钥"+key);
                                Encrypter encrypter=encryptfactory.getEncrypter(encrypt_type);
                                if(encrypter!=null&&key!=null){
                                        postmap=encrypter.transferMapValue(tmpmap);
                                        postmap.put("url",this.posturl);
                                }

                        }
                }

                Map<String, String> recordmap=tmpmap;
                recordmap.remove("encrypt");
                LogUtil.postRecordLog(tasknum,recordmap.toString());


                if(postmap!=null)
                        mtask.execute(postmap);
                else
                        mtask.execute(tmpmap);

        }


        @Override
        public void onDataReceivedSuccess(String[] returnstr) {
                Log.d(TAG,"Post Receive-returned post string");
                Log.d(TAG,returnstr[2]);
                LogUtil.postResultLog(returnstr[0],returnstr[1],returnstr[2]);


        }
        @Override
        public void onDataReceivedFailed(String[] returnstr) {
                // TODO Auto-generated method stub
                Log.d(TAG,"Post Receive-post error");
                LogUtil.postResultLog(returnstr[0],returnstr[1],returnstr[2]);


        }
}
