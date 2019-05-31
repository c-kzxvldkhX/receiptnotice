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
import java.io.FileReader;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;

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
        public static ArrayList getLogList(){
                        File file = new File(TLogApplication.getAPP().getFilesDir(), IConfig.fileName);
                        if (!file.exists())
                                return null;

                        OneFileUtil fileutil = new OneFileUtil(file);
                        ArrayList filelist=fileutil.getFileList();
                        String startflag="*********************************";
                        String endflag="------------------------------------------";
                        ArrayList filemergelist=fileutil.mergeByFlagline(startflag,endflag,filelist);
                        return filemergelist;

        }

}
