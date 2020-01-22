package com.weihuagu.receiptnotice.audiorecognize;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;;import androidx.annotation.Nullable;

public class RecordService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            AudioHub hub=new AudioHub();
        }catch (Exception e){

        }
    }
}
