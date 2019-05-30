/*
 * Created By WeihuaGu (email:weihuagu_work@163.com)
 * Copyright (c) 2017
 * All right reserved.
 */

package com.weihuagu.receiptnotice;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Deque;

public class OneFileUtil{
        private File file;
        public OneFileUtil(File file){
                this.file=file;
        }
        private String clearOnegroup(Deque<String> onegroup){
                String tmp="";
                while(onegroup.size()>0){
                    String first=onegroup.pollFirst();
                    tmp=tmp+"\n"+first;
                }
                return tmp;
        
        }
        public  ArrayList mergeByFlagline(String startflagline,String endflagline,ArrayList filelist){
                if(filelist.size()==0)
                    return null;
                ArrayList<String> merge=new ArrayList<String>();
                Iterator fileiterator = filelist.iterator();
                Deque<String> onegroup = new LinkedList<String>();
                while (fileiterator.hasNext()) {
                    String o = (String)fileiterator.next();
                    onegroup.offerLast(o);
                    if(onegroup.peekFirst().contains(startflagline)&&onegroup.peekLast().contains(endflagline)){
                        merge.add(clearOnegroup(onegroup));
                    
                    }
                   
                }
                return merge;
        
        
        
        
        }
        public  ArrayList getFileList(){
                ArrayList<String> arrayList = new ArrayList<String>();
                FileReader fr = null;
                try{
                        if (!file.exists())
                                return null;

                        fr = new FileReader(file);
                        BufferedReader bf = new BufferedReader(fr);
                        String str;
                        while ((str = bf.readLine()) != null) {
                                //按行处理
                                arrayList.add(str);
                        }
                        if(arrayList.size()==0)
                            return null;
                        else
                            return arrayList;


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
