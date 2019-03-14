package com.weihuagu.receiptnotice;

import android.os.Bundle;
import android.app.Notification;
public abstract class NotificationHandle{
        private String pkgtype;
        private Bundle extras;
        private String title;
        private String content;
        private IDoPost postpush;
        public NotificationHandle(String pkgtype,Bundle extras,IDoPost postpush){
                this.pkgtype=pkgtype;
                this.extras=extras;
                this.postpush=postpush;
                // 获取通知标题
                title = extras.getString(Notification.EXTRA_TITLE, "");
                // 获取通知内容
                content = extras.getString(Notification.EXTRA_TEXT, "");

        }
        public  abstract void handleNotification();


















}

