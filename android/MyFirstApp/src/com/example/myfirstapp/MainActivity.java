package com.example.myfirstapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button submitButton = (Button)findViewById(R.id.button2);
        submitButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intentLogin = new Intent(MainActivity.this, LoginActivity.class);
		    	startActivity(intentLogin);
				
			}
		});
    }

}
