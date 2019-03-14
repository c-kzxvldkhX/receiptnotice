package com.weihuagu.receiptnotice;
import android.app.Application;
import android.content.Intent;

import com.tao.admin.loglib.TLogApplication;
import com.tao.admin.loglib.IConfig;
public class MainApplication extends Application {

        @Override
        public void onCreate() {
                super.onCreate();
                startNotificationService();
        }

        private void initLogConfig(){
                TLogApplication.initialize(this);
                IConfig.getInstance().isShowLog(true)//是否在logcat中打印log,默认不打印
                .isWriteLog(true)//是否在文件中记录，默认不记录
                .fileSize(100000)//日志文件的大小，默认0.1M,以bytes为单位
                .tag("myTag");//logcat 日志过滤tag
        }
        private void startNotificationService(){
                startService(new Intent(this, NotificationCollectorMonitorService.class));
        }

}
