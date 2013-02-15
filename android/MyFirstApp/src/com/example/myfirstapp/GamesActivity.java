package com.example.myfirstapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class GamesActivity extends Activity {

	private CheckBox chkBd1, chkBd2, chkBd3, chkBd4;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

    	addListenerOnChkBd1();
    	addListenerOnChkBd2();
    	addListenerOnChkBd3();
    	addListenerOnChkBd4();

    	
    }

	private void addListenerOnChkBd4() {
		chkBd4 = (CheckBox) findViewById(R.id.checkBox4);
		
		chkBd4.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					Toast.makeText(GamesActivity.this,
				 	   "Come dude lets play at NTU at 6PM:)", Toast.LENGTH_LONG).show();
				}				
			}
		});
		
	}

	private void addListenerOnChkBd3() {
		chkBd3 = (CheckBox) findViewById(R.id.checkBox3);
		
		chkBd3.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					Toast.makeText(GamesActivity.this,
				 	   "Come dude lets play at NUS at 7PM:)", Toast.LENGTH_LONG).show();
				}				
			}
		});
		
	}

	private void addListenerOnChkBd2() {
		chkBd2 = (CheckBox) findViewById(R.id.checkBox2);
		
		chkBd2.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					Toast.makeText(GamesActivity.this,
				 	   "Come dude lets play at Bishan at 9PM:)", Toast.LENGTH_LONG).show();
				}				
			}
		});
		
	}

	private void addListenerOnChkBd1() {

		chkBd1 = (CheckBox) findViewById(R.id.checkBox1);
		
		chkBd1.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					Toast.makeText(GamesActivity.this,
				 	   "Come dude lets play at Clementi at 8PM:)", Toast.LENGTH_LONG).show();
				}				
			}
		});
		
	}

}
