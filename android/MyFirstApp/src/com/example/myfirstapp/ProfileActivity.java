package com.example.myfirstapp;

import android.app.Activity;
import android.os.Bundle;

public class ProfileActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
      //  getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
