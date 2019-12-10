package com.weihuagu.receiptnotice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Observer;

import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.Collections;

public class LogListFragment extends Fragment {
    private RecyclerView recyclerView;
    private LogListAdapter mAdapter;
    private ArrayList loglist;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loglist,container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLoglistView();
        subMessage();
    }

    private void initLoglistView(){
        recyclerView =(RecyclerView) getView(). findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new LogListAdapter(getContext());
       loglist =FileLogUtil.getLogList();
        if(loglist==null){
            loglist=new ArrayList<String>();
            loglist.add("推送记录为空");
        }
        //LogUtil.debugLogWithDeveloper("打印通过filelogutil获取到的file log list");
        mAdapter.setLoglist(loglist);
        recyclerView.setAdapter(mAdapter);
    }

    private void updateList(){
        loglist.addAll(FileLogUtil.getLogList());
        mAdapter.notifyDataSetChanged();
        LogUtil.debugLog("更新Loglist in Fragment列表:");
    }

    private void subMessage(){
        LiveEventBus
                .get("update_recordlist", String.class)
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        LogUtil.debugLog("收到订阅消息:"+s);
                        updateList();
                    }
                });
    }
}
