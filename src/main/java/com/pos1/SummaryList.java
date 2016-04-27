package com.pos1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SummaryList extends Activity {
    Activity activity;
    RecyclerView recList;    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);        
        setContentView(R.layout.summary);

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        ArrayList<String[]> summaryList = new ArrayList<String[]> ();
        String[] values = {"jkskj", ""};
        summaryList.add(values);
       
        Button proceed = (Button) findViewById(R.id.proceed);
        proceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent last = new Intent(getApplicationContext(),
						LastPage.class);
				
				startActivity(last);
				overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

			}
		});
		
        Button back=(Button)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

        
        ContactAdapter contactAdapter = new ContactAdapter(summaryList, this);
        recList.setAdapter(contactAdapter);
    }    
}