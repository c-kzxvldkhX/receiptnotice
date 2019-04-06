package com.weihuagu.receiptnotice;;

import android.app.ActivityManager;
import android.app.Service;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.util.List;
import java.util.Date;
import java.lang.System;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import com.google.gson.Gson;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xinghui on 9/20/16.
 * <p>
 * calling this in your Application's onCreate
 * startService(new Intent(this, NotificationCollectorMonitorService.class));
 * <p>
 * BY THE WAY Don't Forget to Add the Service to the AndroidManifest.xml File.
 * <service android:name=".NotificationCollectorMonitorService"/>
 */
public class NotificationCollectorMonitorService extends Service {

        /**
         * {@link Log#isLoggable(String, int)}
         * <p>
         * IllegalArgumentException is thrown if the tag.length() > 23.
         */
        private static final String TAG = "NotifiCollectorMonitor";

        @Override
        public void onCreate() {
                super.onCreate();
                Log.d(TAG, "onCreate() called");
                ensureCollectorRunning();
                echoServer();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
                return START_STICKY;
        }

        private void echoServer(){
                PreferenceUtil preference=new PreferenceUtil(getBaseContext());
                Gson gson = new Gson();
                if(preference.isEncrypt()&&(preference.getEchoServer()!=null)){
               // AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
               // Intent myIntent = new Intent();
               // myIntent.setAction("com.weihuagu.receiptnotice.echo");
               // myIntent.setPackage("com.weihuagu.receiptnotice");
                //  PendingIntent sender = PendingIntent.getService(getBaseContext(),0,myIntent,0);
                //    LogUtil.debugLog("start to echo alarm");
                //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5 * 1000,  5 * 1000,sender);
                //startService(myIntent);
                try{
                        Date date=new Date(System.currentTimeMillis());
                        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time=format.format(date);
                        DeviceBean device=new DeviceBean();
                        String deviceid=preference.getDeviceid();
                        deviceid=(deviceid!="" ? deviceid:DeviceInfoUtil.getUniquePsuedoID());
                        device.setDeviceid(deviceid);
                        device.setTime(time);
                        LogUtil.debugLog("start socketio");
                        Socket mSocket= IO.socket(preference.getEchoServer());
                        mSocket.connect();
                        mSocket.emit("echo",gson.toJson(device));
                        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                                @Override
                                public void call(Object... args) {echoServer();}
                        });
                        LogUtil.debugLog(gson.toJson(device));
                } catch (URISyntaxException e) {StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        LogUtil.debugLog(sw.toString()); }

                 }


        }
        private void ensureCollectorRunning() {
                ComponentName collectorComponent = new ComponentName(this, /*NotificationListenerService Inheritance*/ NLService.class);
                Log.v(TAG, "ensureCollectorRunning collectorComponent: " + collectorComponent);
                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                boolean collectorRunning = false;
                List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
                if (runningServices == null ) {
                        Log.w(TAG, "ensureCollectorRunning() runningServices is NULL");
                        return;
                }
                for (ActivityManager.RunningServiceInfo service : runningServices) {
                        if (service.service.equals(collectorComponent)) {
                                Log.w(TAG, "ensureCollectorRunning service - pid: " + service.pid + ", currentPID: " + Process.myPid() + ", clientPackage: " + service.clientPackage + ", clientCount: " + service.clientCount
                                                + ", clientLabel: " + ((service.clientLabel == 0) ? "0" : "(" + getResources().getString(service.clientLabel) + ")"));
                                if (service.pid == Process.myPid() /*&& service.clientCount > 0 && !TextUtils.isEmpty(service.clientPackage)*/) {
                                        collectorRunning = true;
                                }
                        }
                }
                if (collectorRunning) {
                        Log.d(TAG, "ensureCollectorRunning: collector is running");
                        return;
                }
                Log.d(TAG, "ensureCollectorRunning: collector not running, reviving...");
                toggleNotificationListenerService();
        }

        private void toggleNotificationListenerService() {
                Log.d(TAG, "toggleNotificationListenerService() called");
                ComponentName thisComponent = new ComponentName(this, /*getClass()*/ NLService.class);
                PackageManager pm = getPackageManager();
                pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                pm.setComponentEnabledSetting(thisComponent, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        }

        @Override
        public IBinder onBind(Intent intent) {
                return null;
        }

        public class DeviceBean{
                public String deviceid;
                public String connectedtime;
                public void setDeviceid(String deviceid){
                        this.deviceid=deviceid;
                }
                public void setTime(String time){
                        this.connectedtime=time;
                }

        }

}

