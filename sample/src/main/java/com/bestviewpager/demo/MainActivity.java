package com.bestviewpager.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bestViewPager).setOnClickListener(this);
        findViewById(R.id.bestViewPager_listview).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bestViewPager:
                changeActivity(BestViewPagerDemo.class);
                break;
            case R.id.bestViewPager_listview:
                changeActivity(BestViewPagerListViewDemo.class);
                break;
        }
    }


    public void changeActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
