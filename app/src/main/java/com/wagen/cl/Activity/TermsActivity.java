package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wagen.cl.R;

public class TermsActivity extends AppCompatActivity {
    TextView txv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        txv_title=(TextView)findViewById(R.id.txv_title);
    }

    public void goback(View view) {
        finish();
    }
}