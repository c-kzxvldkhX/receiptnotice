package com.weihuagu.receiptnotice;
import android.app.Application;
import android.content.Intent;
import android.content.Context;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tao.admin.loglib.TLogApplication;
import com.tao.admin.loglib.IConfig;
public class MainApplication extends Application {
        public static Context mContext;

        @Override
        public void onCreate() {
                super.onCreate();
                startNotificationService();
                initLogConfig();
                setSomeGlobal();
                setMessageBus();
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

}
