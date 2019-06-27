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
import android.widget.Toast;

import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class NLService extends NotificationListenerService implements AsyncResponse, IDoPost, ActionStatusBarNotification {
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
                            notihandle.setStatusBarNotification(sbn);
                            notihandle.setActionStatusbar(this);
                            notihandle.handleNotification();
                            notihandle.removeNotification();
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
        
        public void removeNotification(StatusBarNotification sbn){
            PreferenceUtil preference=new PreferenceUtil(getBaseContext());
            if(preference.isRemoveNotification()){
                if (Build.VERSION.SDK_INT >=21)
                    cancelNotification(sbn.getKey());
                else
                    cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
                sendToast("receiptnotice移除了包名为"+sbn.getPackageName()+"的通知");
            }
        }

        private void sendBroadcast(String msg) {
                Intent intent = new Intent(getPackageName());
                intent.putExtra("text", msg);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        
        private void sendToast(String msg){
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
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
                if(this.posturl==null|params==null)
                        return;
                LogUtil.debugLog("开始准备进行post"); 
                if(params.get("repeatnum")!=null){
                        doPostTask(params,null);
                        return;
                }
                Map<String, String> postmap=params;
                Map<String, String> recordmap=new HashMap<String,String>();
                PreferenceUtil preference=new PreferenceUtil(getBaseContext());
                
                postmap.put("encrypt","0");
                postmap.put("url",this.posturl);
                String deviceid=preference.getDeviceid();
                postmap.put("deviceid",(!deviceid.equals("")? deviceid:DeviceInfoUtil.getUniquePsuedoID()));
                recordmap.putAll(postmap);
                
                if(preference.isEncrypt()){
                        String encrypt_type=preference.getEncryptMethod();
                        if(encrypt_type!=null){
                                String key=preference.getPasswd();
                                EncryptFactory encryptfactory=new EncryptFactory(key);
                                Log.d(TAG,"加密方法"+encrypt_type);
                                Log.d(TAG,"加密秘钥"+key);
                                Encrypter encrypter=encryptfactory.getEncrypter(encrypt_type);
                                if(encrypter!=null&&key!=null){
                                        
                                        postmap=encrypter.transferMapValue(postmap);
                                        postmap.put("url",this.posturl);
                                }

                        }
                }

                
                recordmap.remove("encrypt");
                doPostTask(postmap,recordmap);
               
                        

        }

        private void doPostTask(Map<String, String> postmap,Map<String, String> recordmap){
                PostTask mtask = new PostTask();
                String tasknum=RandomUtil.getRandomTaskNum();
                mtask.setRandomTaskNum(tasknum);
                mtask.setOnAsyncResponse(this);
                if(recordmap!=null)
                    LogUtil.postRecordLog(tasknum,recordmap.toString());
                else
                    LogUtil.postRecordLog(tasknum,postmap.toString());

                mtask.execute(postmap);


        }


        @Override
        public void onDataReceivedSuccess(String[] returnstr) {
                Log.d(TAG,"Post Receive-returned post string");
                Log.d(TAG,returnstr[2]);
                LogUtil.postResultLog(returnstr[0],returnstr[1],returnstr[2]);


        }
        @Override
        public void onDataReceivedFailed(String[] returnstr,Map<String ,String> postedmap) {
                // TODO Auto-generated method stub
                Log.d(TAG,"Post Receive-post error");
                LogUtil.postResultLog(returnstr[0],returnstr[1],returnstr[2]);
                PreferenceUtil preference=new PreferenceUtil(getBaseContext());
                if(preference.isPostRepeat()){
                    String repeatlimit=preference.getPostRepeatNum();
                    int limitnum=Integer.parseInt(repeatlimit);
                    String repeatnumstr=postedmap.get("repeatnum");
                    int repeatnum=Integer.parseInt(repeatnumstr);
                    if(repeatnum<=limitnum)
                            doPost(postedmap);
                }

        }
}
