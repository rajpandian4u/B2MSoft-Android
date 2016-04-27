package com.pos1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;

public class RequestHandler extends AsyncTask<String, String, String> {

	private static final String TAG = RequestHandler.class.getName();
	
	protected Context context = null;
	private String url = "";
	
	public RequestHandler(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(String... args) {
		String response = "";
		try {
			setRequestParams(args);	
			response = QueryYahooWeather();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!Utilities.isNetworkAvailable(context)) {
			return "Internet not connected" ;
		} else {
			return response;
		}		
	}
	
	private void setRequestParams(String... args) {
		url = args[0];
	}		

	private String QueryYahooWeather() {
	  String qResult = "";

	  HttpClient httpClient = new DefaultHttpClient();
	     HttpGet httpGet = new HttpGet(url);
	  
	     try {
		      HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();
		    
		      if (httpEntity != null){
			       InputStream inputStream = httpEntity.getContent();
			       Reader in = new InputStreamReader(inputStream);
			       BufferedReader bufferedreader = new BufferedReader(in);
			       StringBuilder stringBuilder = new StringBuilder();
			     
			       String stringReadLine = null;
		
			       while ((stringReadLine = bufferedreader.readLine()) != null) {
			        stringBuilder.append(stringReadLine + "\n");
			       }
			     
			       qResult = stringBuilder.toString();
		      }
		} catch (ClientProtocolException e) {
			e.printStackTrace();		
		} catch (IOException e) {
			e.printStackTrace();
		}

		return qResult;
	}
}
