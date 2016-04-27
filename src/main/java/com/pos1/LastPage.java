package com.pos1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.TextView;

public class LastPage extends Activity {
	TextView text_date;
	DigitalClock digital_clock;
	ImageView image1;
	Button logout,newtask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// remove title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.last);
		
		text_date = (TextView) findViewById(R.id.text_date);
		digital_clock = (DigitalClock) findViewById(R.id.digital_clock);
		text_date.setText(Utility.getDate());
		image1 =(ImageView)findViewById(R.id.textView1);
		 Animation myFadeInAnimation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.myanimation);
		 //Animation myFadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clockwise);
		    //Animation myFadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);

		//fade it in, and fade it out. 
		    image1.startAnimation(myFadeInAnimation1);
		   // image1.startAnimation(myFadeInAnimation);
		    
		    logout = (Button) findViewById(R.id.signout);
		    newtask = (Button) findViewById(R.id.nexttask);
		    logout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// clear back stack
                    startActivity(myIntent);
                    finish();
                }
          });
		    
		    newtask.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent newIntent = new Intent(getApplicationContext(),PosInputActivity.class);
					startActivity(newIntent);
					
				}
		    	
		    });

		
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
         //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
         return true;
         }
         return super.onKeyDown(keyCode, event);    
    }

}
