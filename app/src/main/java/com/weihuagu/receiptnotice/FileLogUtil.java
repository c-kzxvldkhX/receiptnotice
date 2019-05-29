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
                FileReader fr = null;
                try{
                        File file = new File(TLogApplication.getAPP().getFilesDir(), IConfig.fileName);
                        if (!file.exists())
                                return null;

                        fr = new FileReader(file);
                        BufferedReader bf = new BufferedReader(fr);
                        String str;
                        while ((str = bf.readLine()) != null) {
                                //按行处理
                        }
                        return null;


                } catch (Throwable ex) {
                        ex.printStackTrace();
                } finally {
                        try {
                                if (fr != null)
                                        fr.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
                return null;
        }

}
