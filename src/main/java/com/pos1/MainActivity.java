package com.pos1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;



@SuppressWarnings("deprecation")
public class MainActivity extends Activity implements LocationListener{
	
	private long lastPressedTime;
	private static final int PERIOD = 2000;
	
	ArrayList<String> un, pw;
	Spinner spinner;
	EditText username, password;
	private TextView text_date;
	private DigitalClock digital_clock;
	
	private Button signIn,forgotpass ;
	static Typeface tf;
	
	Context mContext = MainActivity.this;
    SharedPreferences appPreferences;
    boolean isAppInstalled = false;

	private LocationManager locManager;
	Location curLocation;
	String provider;
	TextView locality;
	private ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
    	
		tf = Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf");

		text_date = (TextView) findViewById(R.id.text_date);
		digital_clock = (DigitalClock) findViewById(R.id.digital_clock);
		text_date.setText(Utility.getDate());
		
        appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAppInstalled = appPreferences.getBoolean("isAppInstalled",false);
        
       if(isAppInstalled==false){
	       /**
	        * create short code
	        */
		   Intent shortcutIntent = new Intent(getApplicationContext(),MainActivity.class);
	       shortcutIntent.setAction(Intent.ACTION_MAIN);
	       Intent intent = new Intent();
	       intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	       intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"MEDS");
	       intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.meds));
	       intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	       getApplicationContext().sendBroadcast(intent);
	       /**
	        * Make preference true
	        */
	       SharedPreferences.Editor editor = appPreferences.edit();
	       editor.putBoolean("isAppInstalled", true);
	       editor.commit();
       }
	 

		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		forgotpass = (Button) findViewById(R.id.btnforgot);
		signIn = (Button) findViewById(R.id.btnlogin);
		locality = (TextView) findViewById(R.id.location);

		text_date.setTypeface(tf);
		digital_clock.setTypeface(tf);
		username.setTypeface(tf);
		password.setTypeface(tf);
		forgotpass.setTypeface(tf);
		signIn.setTypeface(tf);

		signIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String un = username.getText().toString().trim();
				String pw = password.getText().toString().trim();

				if (un.equals("") || pw.equals("")) {
					Toast.makeText(MainActivity.this,
							"Please Enter Your Login Details...", Toast.LENGTH_LONG)
							.show();
				}

				else if (pw.equals(un)) {

                    /*
                    RG- User id and password should be same for demo
                    if (pw.equals("lc") && un.equals("lc")) {

                     */
					Toast.makeText(MainActivity.this,
							"Congrats: Login Successfull", Toast.LENGTH_LONG)
							.show();

					Intent i = new Intent(MainActivity.this,
							PosInputActivity.class);
					i.putExtra("user", un);
					startActivity(i);
					overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

				} else {
					Toast.makeText(MainActivity.this,
							"User Name or Password does not match",
							Toast.LENGTH_LONG).show();

				}

			}
		});	
		
		getCurrentLocation();
	    
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                if (event.getDownTime() - lastPressedTime < PERIOD) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Press again to exit.",
                            Toast.LENGTH_SHORT).show();
                    lastPressedTime = event.getEventTime();
                }
                return true;
            }
        }
        return false;   
    }   
    
    private void getCurrentLocation() {
    	locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    	if(locManager != null) {
    		Criteria crit = new Criteria();
    		crit.setAccuracy(Criteria.ACCURACY_FINE);
    		provider = locManager.getBestProvider(crit, false);
    		curLocation = getLastKnownLocation();
    		
    		if(curLocation != null) {
    			onLocationChanged(curLocation);
    		} else {
    			getWeatherCondition("Columbus", "Ohio");
    		}
    		
    		locManager.requestLocationUpdates(provider, 400, 1, this);
    		
    	}
    }
    
    private Location getLastKnownLocation() {
    	LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
    	List<String> providers = mLocationManager.getProviders(true);
    	Location bestLocation = null;
    	
    	for(String provider : providers) {
    		Location loc = mLocationManager.getLastKnownLocation(provider);
    		if(loc == null) {
    			continue;
    		}
    		
    		if(bestLocation == null || loc.getAccuracy() < bestLocation.getAccuracy()) {
    			bestLocation = loc;
    			break;
    		}
    	}
    	
    	return bestLocation;
    	
    }

	@Override
	public void onLocationChanged(Location location) {
		Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
            	Address add = addresses.get(0);
            	if(add.getLocality() != null) {
            		if(locality != null) {
            			locality.setText(" "+add.getLocality()+", "+add.getCountryName());
            			getWeatherCondition(add.getLocality(), add.getCountryName());
            		}
            	} else {
            		if(locality != null) {
            			locality.setText(" "+add.getSubAdminArea()+", "+add.getCountryName());
            			getWeatherCondition(add.getSubAdminArea(), add.getCountryName());
            		}
            	}
            }
                
            
        } catch (IOException e) {
            e.printStackTrace();
        }
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	
	//Weather call
	
	private void getWeatherCondition(String locality, String country) {
		String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+locality+"%2C%20"+country+"%22)&format=xml&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
		RequestHandler handler = new RequestHandler(this){
		/*	protected void onPreExecute()
			{
				dialog = ProgressDialog.show(context, "", "Initializing ...");
			}
		*/
			protected void onPostExecute(String response)
			{				
				processResponse(response);
			}
		};
		
		handler.execute(url);
	}
	
	private void processResponse(String response) {
		//System.out.println(response);		
		if(response.equals("Internet not connected")) {
			if(dialog != null)
				dialog.dismiss();
			
			Toast.makeText(this, response, Toast.LENGTH_LONG).show();			
		} else {
			Document weatherDoc = convertStringToDocument(response);
			//System.out.println(weatherDoc.g));		
			if(weatherDoc != null) {
				parseWeather(weatherDoc);				
			} else {
				if(dialog != null)
					dialog.dismiss();
			}
		}						
	}
	
	 private Document convertStringToDocument(String src){
		Document dest = null;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser;

		try {
		   parser = dbFactory.newDocumentBuilder();
		   dest = parser.parse(new ByteArrayInputStream(src.getBytes()));
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();		 
		} catch (SAXException e) {
			e.printStackTrace();		 
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dest;
	}
	
	private void parseWeather(Document srcDoc){
	    if(srcDoc != null)	{  	
			
			Node tempNode =  srcDoc.getElementsByTagName("yweather:forecast").item(0);
		
			String tempratureHigh = fahrenheitToCelcius(tempNode.getAttributes()
					.getNamedItem("high")
					.getNodeValue()
					.toString());
			String tempratureLow = fahrenheitToCelcius(tempNode.getAttributes()
					.getNamedItem("low")
					.getNodeValue()
					.toString());		
			
			Node conditionNode = srcDoc.getElementsByTagName("yweather:condition").item(0);
			String conditiontext = conditionNode.getAttributes()
					.getNamedItem("text")
					.getNodeValue()
					.toString();
			
			Node curTemp = srcDoc.getElementsByTagName("yweather:condition").item(0);
			String currentTemp = fahrenheitToCelcius(curTemp.getAttributes()
					.getNamedItem("temp")
					.getNodeValue()
					.toString());
			
			TextView curTempText = (TextView) findViewById(R.id.celcius);
			TextView predictedTemp = (TextView) findViewById(R.id.predicted_temp);
			TextView condition = (TextView) findViewById(R.id.condition);
			ImageView curTempImg = (ImageView) findViewById(R.id.celcius_img);
			
			if(curTempText != null) 
				curTempText.setText(currentTemp+"* C");
			
			if(curTempImg != null) {
				if(conditiontext.contains("Cloudy")){
					curTempImg.setImageResource(R.drawable.cloudy);
				} else if(conditiontext.contains("Partial")) {
					curTempImg.setImageResource(R.drawable.weather_img);
				} else if(conditiontext.contains("Sunny")) {
					curTempImg.setImageResource(R.drawable.sunny_day);
				} else if(conditiontext.contains("Snow")) {
					curTempImg.setImageResource(R.drawable.snow);
				} else if(conditiontext.contains("Rainy")) {
					curTempImg.setImageResource(R.drawable.rain);
				} else {
					curTempImg.setImageResource(R.drawable.weather_img);
				}
			}
			
			if(predictedTemp != null)
				predictedTemp.setText(tempratureLow+"* / "+ tempratureHigh+"*");
			
			if(condition != null)
				condition.setText(conditiontext);
			
			if(dialog != null)
				dialog.dismiss();
			//System.out.println(tempratureHigh+ "  "+ tempratureLow+ "  "+ conditiontext + "  "+currentTemp );
			
	    }  	    
    } 
	 
	private String fahrenheitToCelcius(String F) {
		int celcius = 0;
		int fahrenheit = Integer.parseInt(F);
		celcius = ((fahrenheit - 32) * 5) / 9 ;
		//System.out.println(celcius + " "+F);
		return String.valueOf(celcius);
	}
    
}
