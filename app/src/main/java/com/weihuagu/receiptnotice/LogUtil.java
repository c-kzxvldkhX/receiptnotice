/*
 * Created By WeihuaGu (email:weihuagu_work@163.com)
 * Copyright (c) 2017
 * All right reserved.
 */

package com.weihuagu.receiptnotice;
import android.util.Log;
import com.tao.admin.loglib.Logger;

public class LogUtil {
        public static String TAG="NLService";
        public static String DEBUGTAG="NLDebugService";
        public static String EXCEPTIONTAG="NLExceptionService";
        public static void infoLog(String info){
                Log.i(TAG,info);
        }

        public static void debugLog(String info){
                Log.d(TAG,info);
        }

        public static void debugLogWithJava(String info){
                System.out.println(DEBUGTAG+":"+info);
        }

        public static void postRecordLog(String post){
                Logger.i("推送记录", post);
        }

}
