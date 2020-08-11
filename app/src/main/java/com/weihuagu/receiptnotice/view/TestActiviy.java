package com.weihuagu.receiptnotice.view;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.weihuagu.receiptnotice.R;
import com.weihuagu.receiptnotice.ForTest;


import org.w3c.dom.Text;
import android.widget.Button;
import android.view.View;

public class TestActiviy extends AppCompatActivity implements View.OnClickListener{

    private TextView money;
    private Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        money = (TextView) findViewById(R.id.money);
        button = (Button) findViewById(R.id.action_nitification);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_nitification:
                new ForTest();
                break;

        }
    }
}
