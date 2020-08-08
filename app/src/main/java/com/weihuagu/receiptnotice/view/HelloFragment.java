package com.weihuagu.receiptnotice.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.weihuagu.receiptnotice.MainApplication;
import com.weihuagu.receiptnotice.R;
import com.weihuagu.receiptnotice.util.LogUtil;
import com.weihuagu.receiptnotice.util.PreferenceUtil;

public class HelloFragment extends Fragment {
    private TextView numofpush;
    private TextView posturl;
    private View rootview;
    private PreferenceUtil preference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview=inflater.inflate(R.layout.fragment_hello,container, false);
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        subMessage();
    }

    private void initView(){
        preference=new PreferenceUtil(getContext());
        numofpush=(TextView)rootview.findViewById(R.id.numofpush);
        setTextWithNumofpush();
        posturl=(TextView)rootview.findViewById(R.id.posturl);
        setTextWithPosturl();
    }
    private void setTextWithNumofpush(){
        numofpush.setText("推送次数"+preference.getNumOfPush());
    }
    private void setTextWithPosturl(){
        if(preference.getPostUrl()!=null)
            posturl.setText("目前的推送地址："+preference.getPostUrl());

    }
    private void resetText(){
        setTextWithPosturl();
        setTextWithNumofpush();
    }
    private void subMessage(){
        LiveEventBus
                .get("message_finished_one_post",String[].class)
                .observeForever(new Observer<String[]>() {
                    @Override
                    public void onChanged(@Nullable String[] testpostbean) {
                        resetText();
                    }
                });
        LiveEventBus
                .get("user_set_posturl",String.class)
                .observeForever(new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String url) {
                        resetText();
                    }
                });
        LiveEventBus
                .get("time_interval",String.class)
                .observeForever(new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String baseinterval) {
                        Toast.makeText(MainApplication.getAppContext(), "接受到一分钟间隔事件，更新推送次数",
                                Toast.LENGTH_SHORT).show();
                        LogUtil.debugLog("接受到一分钟一次的时间间隔事件");
                        resetText();
                    }
                });

    }
}
