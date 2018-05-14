package com.itheima.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SdClearActivity extends Activity {
       @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	TextView view = new TextView(getApplicationContext());
    	view.setText("SDCacheClearActivity");
    	setContentView(view);
    }
}
