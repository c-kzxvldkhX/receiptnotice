/*
 * Created By WeihuaGu (email:weihuagu_work@163.com)
 * Copyright (c) 2017
 * All right reserved.
 */

package com.weihuagu.receiptnotice;
import com.tao.admin.loglib.FileUtils;
import com.tao.admin.loglib.TLogApplication;
import com.tao.admin.loglib.IConfig;
import java.io.File;

public class FileLogUtil extends FileUtils{
        public static boolean clearLogFile() {
                try {
                        File file = new File(TLogApplication.getAPP().getFilesDir(), IConfig.fileName);
                        if(file.delete()) 
                                return true;
                        else 
                                return false;
                        
                } catch(Exception e) {
                        e.printStackTrace();
                        return false;
                }




        }

}
