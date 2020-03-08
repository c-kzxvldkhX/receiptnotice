package com.weihuagu.receiptnotice.view;

import java.util.ArrayList;
import java.util.Collections;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.weihuagu.receiptnotice.util.FileLogUtil;
import com.weihuagu.receiptnotice.R;

public class FileLogActivity extends AppCompatActivity {
        private RecyclerView recyclerView;
        private LogListAdapter mAdapter;
        private RecyclerView.LayoutManager layoutManager;

        private TextView mTextView;
        private Toolbar myToolbar;
        private boolean loglist_is_a_wholetext=true;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_log);
                initView();
                setLogText();
        }

        private void initView(){
                myToolbar= (Toolbar) findViewById(R.id.my_toolbar);
                setSupportActionBar(myToolbar);
                mTextView = (TextView) findViewById(R.id.tv_log);

        }

        private void initLoglistView(boolean reverseorder){
                recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                //recyclerView.setHasFixedSize(true);
                // use a linear layout manager
                layoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(layoutManager);
                // specify an adapter (see also next example)
                mAdapter = new LogListAdapter(getApplicationContext());
                ArrayList loglist= FileLogUtil.getLogList();
                //LogUtil.debugLogWithDeveloper("打印通过filelogutil获取到的file log list");
                if(loglist!=null&&loglist.size()>0){
                    loglist_is_a_wholetext=false;
                    if(reverseorder)
                    Collections.reverse(loglist);
                }
                else{
                        loglist_is_a_wholetext=true;
                        return;
                }
                mAdapter.setLoglist(loglist);
                recyclerView.setAdapter(mAdapter);
        }


        private void setLogText(){
                initLoglistView(false);
                if(loglist_is_a_wholetext){
                    String log = FileLogUtil.readLogText();
                    mTextView.setText(log);
                }
                
        }

        private void clearLog(){
                FileLogUtil.clearLogFile();
                setLogText();
                Toast.makeText(getApplicationContext(), "已经清空日志",Toast.LENGTH_SHORT).show();
        }
        private void showReverse(){
                initLoglistView(true); 
        }
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // TODO Auto-generated method stub
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.log, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                        case R.id.action_clearlog:
                                // User chose the "Settings" item, show the app settings UI...
                                clearLog();
                                return true;
                        case R.id.action_reverseshow:
                                showReverse();
                                return true;
                        default:
                                // If we got here, the user's action was not recognized.
                                // Invoke the superclass to handle it.
                                return super.onOptionsItemSelected(item);

                }
        }

        @Override
        protected void onResume() {
                super.onResume();
                setLogText();
        }



}
