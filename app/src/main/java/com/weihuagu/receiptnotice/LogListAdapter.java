package com.weihuagu.receiptnotice;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.content.Context;
import java.util.ArrayList;
public class LogListAdapter extends Adapter<LogListAdapter.RecyclerHolder>{
        private final LayoutInflater mLayoutInflater;
        private ArrayList loglist;
       
        LogListAdapter(Context context){
            mLayoutInflater = LayoutInflater.from(context);
        }
        public void setLoglist(ArrayList loglist){
            this.loglist=loglist;
        }
        @Override
        public int getItemCount() {
                return loglist == null ? 0 : loglist.size();
        }


        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
                String onerecord=(String)loglist.get(position);
                if(holder.recordtext==null)
                    return;
                if(onerecord!=null)
                    holder.recordtext.setText(onerecord);
                else
                    LogUtil.debugLogWithDeveloper("获取到的loglist 的item text为空");
                
        }
        
        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           return new RecyclerHolder(mLayoutInflater.inflate(R.layout.item_log_text, parent, false));

        }





        class RecyclerHolder extends RecyclerView.ViewHolder {
                private TextView recordtext;
                RecyclerHolder(View view){
                        super(view);
                        recordtext=view.findViewById(R.id.text_view);
                }

        }

}
