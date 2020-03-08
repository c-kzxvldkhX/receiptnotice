package com.weihuagu.receiptnotice.view;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.weihuagu.receiptnotice.R;

public class FollowThirdAppActivity extends AppCompatActivity {
    private EditText pkg;
    private EditText keyword;
    private EditText type;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followthirdapp);
    }
    private void initView() {
        pkg=(EditText)findViewById(R.id.pkg);
        keyword=(EditText)findViewById(R.id.pkg);
        type=(EditText)findViewById(R.id.pkg);
    }
}
