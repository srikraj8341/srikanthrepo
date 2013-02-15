package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
@SuppressLint({ "NewApi", "NewApi" })
public class LoginActivity extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Resources ressources = getResources(); 
		TabHost tabHost = getTabHost(); 
 
        
        // Profile Activity
		Intent intentProfile = new Intent().setClass(this, ProfileActivity.class);
		TabSpec tabProfile = tabHost
				  .newTabSpec("Profile")
				  .setIndicator("profile", ressources.getDrawable(R.drawable.ic_action_search))
				  .setContent(intentProfile);
		//startActivity(intentProfile);
		
		// Initiate Activity 
		Intent intentInitiate = new Intent().setClass(this, InitiateActivity.class);
		TabSpec tabInitiate = tabHost
				  .newTabSpec("Initiate")
				  .setIndicator("Initiate", ressources.getDrawable(R.drawable.ic_action_search))
				  .setContent(intentInitiate);
		//startActivity(intentInitiate);
		
		// Games Activity
		Intent intentGames = new Intent().setClass(this, GamesActivity.class);
		TabSpec tabGames = tabHost
				  .newTabSpec("Games")
				  .setIndicator("Games", ressources.getDrawable(R.drawable.ic_action_search))
				  .setContent(intentGames);
		
		tabHost.addTab(tabProfile);
		tabHost.addTab(tabInitiate);
		tabHost.addTab(tabGames);
		//startActivity(intentGames);
		tabHost.setCurrentTab(0);
    }

}
