package com.weihuagu.receiptnotice.action;

import android.content.SharedPreferences;


import com.weihuagu.receiptnotice.MainApplication;
import com.weihuagu.receiptnotice.OnlyWriteToDateBase;
import com.weihuagu.receiptnotice.filteringmiddleware.PostMapFilter;
import com.weihuagu.receiptnotice.util.LogUtil;
import com.weihuagu.receiptnotice.util.PreferenceUtil;
import com.weihuagu.receiptnotice.util.RandomUtil;
import com.weihuagu.receiptnotice.util.message.MessageSendBus;

import java.util.Map;


public class HandlePost implements IDoPost, AsyncResponse {
    protected String posturl=null;

    public HandlePost() {
        getPostUrl();
    }

    protected String getPostUrl(){
        PreferenceUtil preference=new PreferenceUtil(MainApplication.getAppContext());
        posturl=preference.getPostUrl();
        return posturl;
    }
    @Override
    public void doPost(Map<String, String> params) {
        if(this.posturl==null|params==null)
            return;
        LogUtil.debugLog("开始准备进行post");
        if(params.get("repeatnum")!=null){
            doPostTask(params,null);
            return;
        }

        PreferenceUtil preference=new PreferenceUtil(MainApplication.getAppContext());
        PostMapFilter mapfilter=new PostMapFilter(preference,params,this.posturl);
        Map<String, String> recordmap=mapfilter.getLogMap();
        Map<String, String> postmap=mapfilter.getPostMap();

        doPostTask(postmap,recordmap);

    }

    protected void doPostTask(Map<String, String> postmap,Map<String, String> recordmap){
        PostTask mtask = new PostTask();
        String tasknum= RandomUtil.getRandomTaskNum();
        mtask.setRandomTaskNum(tasknum);
        mtask.setOnAsyncResponse(this);
        if(recordmap!=null)
            LogUtil.postRecordLog(tasknum,recordmap.toString());
        else
            LogUtil.postRecordLog(tasknum,postmap.toString());

        mtask.execute(postmap);


    }


    @Override
    public void onDataReceivedSuccess(String[] returnstr) {
        LogUtil.debugLog("Post Receive-returned post string");
        LogUtil.debugLog(returnstr[2]);
        LogUtil.postResultLog(returnstr[0],returnstr[1],returnstr[2]);
        MessageSendBus.postMessageWithFinishedonePost(returnstr);
        new OnlyWriteToDateBase().onePostWriteToDateBase(returnstr[2]);

    }

    @Override
    public void onDataReceivedFailed(String[] returnstr, Map<String, String> postedmap) {
        // TODO Auto-generated method stub
        LogUtil.debugLog("Post Receive-post error");
        LogUtil.postResultLog(returnstr[0],returnstr[1],returnstr[2]);
        PreferenceUtil preference=new PreferenceUtil(MainApplication.getAppContext());
        if(preference.isPostRepeat()){
            String repeatlimit=preference.getPostRepeatNum();
            int limitnum=Integer.parseInt(repeatlimit);
            String repeatnumstr=postedmap.get("repeatnum");
            int repeatnum=Integer.parseInt(repeatnumstr);
            if(repeatnum<=limitnum)
                doPost(postedmap);
        }

    }
}
