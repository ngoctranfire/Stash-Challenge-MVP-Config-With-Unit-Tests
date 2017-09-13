package com.stashinvest.stashchallenge.ui.main;

import android.os.Bundle;

import com.stashinvest.stashchallenge.R;
import com.stashinvest.stashchallenge.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, MainFragment.newInstance())
                .commit();
    }
}
