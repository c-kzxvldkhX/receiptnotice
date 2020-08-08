package com.weihuagu.receiptnotice;
import android.app.Application;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tao.admin.loglib.TLogApplication;
import com.tao.admin.loglib.IConfig;
import com.weihuagu.receiptnotice.util.LogUtil;
import com.weihuagu.receiptnotice.util.message.MessageSendBus;

public class MainApplication extends Application {
        public static Context mContext;
        private BroadcastReceiver timereceiver;

        @Override
        public void onCreate() {
                super.onCreate();
                startNotificationService();
                initLogConfig();
                setSomeGlobal();
                setMessageBus();
                setSomeThingWaitMessage();

        }

        private void initLogConfig(){
                TLogApplication.initialize(this);
                IConfig.getInstance().isShowLog(false)//是否在logcat中打印log,默认不打印
                        .isWriteLog(true)//是否在文件中记录，默认不记录
                        .tag("GoFileService");//logcat 日志过滤tag
        }
        private void startNotificationService(){
                startService(new Intent(this, NotificationCollectorMonitorService.class));
        }
        private void setSomeGlobal(){
                mContext = getApplicationContext();
        }
        public void setMessageBus(){
                LiveEventBus
                        .config()
                        .supportBroadcast(this)
                        .lifecycleObserverAlwaysActive(true);
        }
        public static Context getAppContext(){
                return mContext;
        }
        public void timeInterval(){
            IntentFilter filter=new IntentFilter();
            filter.addAction(Intent.ACTION_TIME_TICK);
            registerReceiver(timereceiver,filter);
        }

        public void setSomeThingWaitMessage(){
            timeInterval();
            timereceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(Intent.ACTION_TIME_TICK)) {
                        LogUtil.debugLog("接受到一分钟广播action_time_tick事件");
                        MessageSendBus.postBaseTimeInterval();
                    }
                }
            };
        }


}
