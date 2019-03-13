package com.weihuagu.receiptnotice;;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import java.util.List;

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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
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
}

