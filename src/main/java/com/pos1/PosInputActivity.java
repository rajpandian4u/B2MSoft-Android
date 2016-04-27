package com.pos1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microblink.activity.Pdf417ScanActivity;
import com.microblink.recognizers.barcode.BarcodeType;
import com.microblink.recognizers.barcode.bardecoder.BarDecoderRecognizerSettings;
import com.microblink.recognizers.barcode.bardecoder.BarDecoderScanResult;
import com.microblink.recognizers.barcode.pdf417.Pdf417RecognizerSettings;
import com.microblink.recognizers.barcode.pdf417.Pdf417ScanResult;
import com.microblink.recognizers.barcode.usdl.USDLRecognizerSettings;
import com.microblink.recognizers.barcode.usdl.USDLScanResult;
import com.microblink.recognizers.barcode.zxing.ZXingRecognizerSettings;
import com.microblink.recognizers.barcode.zxing.ZXingScanResult;
import com.microblink.recognizers.settings.GenericRecognizerSettings;
import com.microblink.recognizers.settings.RecognizerSettings;
import com.microblink.results.barcode.BarcodeDetailedData;
import com.microblink.view.CameraAspectMode;
import com.microblink.view.recognition.RecognizerView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PosInputActivity extends Activity implements
		OnItemSelectedListener, OnClickListener {

	private static final String TAG = "PosInputActivity";

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static String PATH = "/sdcard/MEDS/";

	// directory name to store captured images and videos
	private static final String IMAGE_DIRECTORY_NAME = "POS";
	
	private DatePickerDialog fromDatePickerDialog;
	private SimpleDateFormat dateFormatter;

	private Uri fileUri;

	String address, addresss, citye, zipcodee, homephonee, mobilephonee,
			emailide, contactnamee, contactphonee, ssne, dobe, monthlyincomee,
			medicalnumbere, commentse, patientide, firstnamee, middlenamee,
			lastnamee, noofchildd, statess, un,scompname, smemberid,splannum,sgroupnum,sinsurancecomments;
	static String ssnn;

	private String[] noofchild = { "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "10", "11", "12" };
	private String[] states = Utility.getStates();

	Button cancel, proceed;
	TextView text_date, address1, address2, city, state, zipcode, homephone,
			mobilephone, emailid, contactname, contactphone, ssn, dob,
			marriage, noofchildren, disable, monthlyincome, medicalnumber,
			comments, patientid, firstname, middlename, lastname, personaltext,
			contacttext, title, welcome, profilename, textview,
            compname, memberid,plannum,groupnum,insurancecomments;
	EditText address1edit, address2edit, cityedit, zipcodeedit, homephoneedit,
			mobilephoneedit, emailidedit, contactnameedit, contactphoneedit,
			ssnedit, dobedit, monthlyincomeedit, medicalnumberedit,
			commentsedit, patientidedit, firstnameedit, middlenameedit,
			lastnameedit,compnameedit,plannumedit,memberidedit,groupnumedit,insurancecommentsedit;
	Spinner stateedit, noofchildrenedit;
	RadioButton radio0, radio1, disableradio1, disableradio2;
	DigitalClock digital_clock;

	static Typeface tf;
	StringBuffer data;
	String registr;

	private Bundle extras;

    /*
    RG - Add scan related declaration
     */
    //public static final String LICENSE = "BMLQR53X-FUQZBBJY-5TQ6FUAB-U3DNKABV-A5MSPORX-GPJRWFJ7-FSQ6FUAB-U3DIJJK6";

    private static final int MY_REQUEST_CODE = 1337;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// remove title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.secondpage);

		tf = Typeface.createFromAsset(getAssets(), "font/Roboto-Regular.ttf");

		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				un = null;
			} else {
				un = extras.getString("user");

			}

		}

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		
		findViewsById();
		
		setDateTimeField();
		
		text_date = (TextView) findViewById(R.id.text_date);
		digital_clock = (DigitalClock) findViewById(R.id.digital_clock);
		text_date.setText(Utility.getDate());

		address1 = (TextView) findViewById(R.id.address1);
		address2 = (TextView) findViewById(R.id.address2);
		city = (TextView) findViewById(R.id.city);
		state = (TextView) findViewById(R.id.state);
		zipcode = (TextView) findViewById(R.id.zipcode);
		homephone = (TextView) findViewById(R.id.homephone);
		mobilephone = (TextView) findViewById(R.id.mobilephone);
		emailid = (TextView) findViewById(R.id.emailid);
		contactname = (TextView) findViewById(R.id.contactname);
		contactphone = (TextView) findViewById(R.id.contactphone);
		ssn = (TextView) findViewById(R.id.ssn);
		dob = (TextView) findViewById(R.id.dob);
		marriage = (TextView) findViewById(R.id.marriage);
		noofchildren = (TextView) findViewById(R.id.noofchildren);
		disable = (TextView) findViewById(R.id.disable);
		monthlyincome = (TextView) findViewById(R.id.monthlyincome);
		medicalnumber = (TextView) findViewById(R.id.medicalnumber);
		comments = (TextView) findViewById(R.id.comments);
		patientid = (TextView) findViewById(R.id.patientid);
		firstname = (TextView) findViewById(R.id.firstname);
		middlename = (TextView) findViewById(R.id.middlename);
		lastname = (TextView) findViewById(R.id.lastname);
		personaltext = (TextView) findViewById(R.id.personaltext);
		contacttext = (TextView) findViewById(R.id.contacttext);
		title = (TextView) findViewById(R.id.title);
		welcome = (TextView) findViewById(R.id.welcome);
		profilename = (TextView) findViewById(R.id.profilename);

		address1edit = (EditText) findViewById(R.id.address1edit);
		address2edit = (EditText) findViewById(R.id.address2edit);
		cityedit = (EditText) findViewById(R.id.cityedit);
		zipcodeedit = (EditText) findViewById(R.id.zipcodeedit);
		InputFilter[] filters4 = new InputFilter[1];
		filters4[0] = new InputFilter.LengthFilter(11); //Filter to 10 characters
		zipcodeedit .setFilters(filters4);
		//zipcodeedit.addTextChangedListener(new ZipCode(zipcodeedit));
		homephoneedit = (EditText) findViewById(R.id.homephoneedit);
		InputFilter[] filters2 = new InputFilter[1];
		filters2[0] = new InputFilter.LengthFilter(12); //Filter to 10 characters
		homephoneedit .setFilters(filters2);
		homephoneedit.addTextChangedListener(new PhoneNumber(homephoneedit));
		mobilephoneedit = (EditText) findViewById(R.id.mobilephoneedit);
		InputFilter[] filters3 = new InputFilter[1];
		filters3[0] = new InputFilter.LengthFilter(12); //Filter to 10 characters
		mobilephoneedit .setFilters(filters3);
		mobilephoneedit.addTextChangedListener(new PhoneNumber(mobilephoneedit));
		emailidedit = (EditText) findViewById(R.id.emailidedit);
		contactnameedit = (EditText) findViewById(R.id.contactnameedit);
		
		
		contactphoneedit = ((EditText) findViewById(R.id.contactphoneedit));
		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter.LengthFilter(12); //Filter to 10 characters
		contactphoneedit .setFilters(filters);
		contactphoneedit.addTextChangedListener(new PhoneNumber(contactphoneedit));
		
		ssnedit = (EditText) findViewById(R.id.ssnedit);
		InputFilter[] filters1 = new InputFilter[1];
		filters1[0] = new InputFilter.LengthFilter(11); //Filter to 10 characters
		ssnedit .setFilters(filters1);
		ssnedit.addTextChangedListener(new SSN(ssnedit));
		dobedit = (EditText) findViewById(R.id.dobedit);
		monthlyincomeedit = (EditText) findViewById(R.id.monthlyincomeedit);
		medicalnumberedit = (EditText) findViewById(R.id.medicalnumberedit);
		commentsedit = (EditText) findViewById(R.id.commentsedit);
		patientidedit = (EditText) findViewById(R.id.patientidedit);
		firstnameedit = (EditText) findViewById(R.id.firstnameedit);
		middlenameedit = (EditText) findViewById(R.id.middlenameedit);
		lastnameedit = (EditText) findViewById(R.id.lastnameedit);

		radio0 = (RadioButton) findViewById(R.id.radio0);
		radio1 = (RadioButton) findViewById(R.id.radio1);
		disableradio1 = (RadioButton) findViewById(R.id.disableradio1);
		disableradio2 = (RadioButton) findViewById(R.id.disableradio2);

		stateedit = (Spinner) findViewById(R.id.stateedit);
		noofchildrenedit = (Spinner) findViewById(R.id.noofchildrenedit);

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, noofchild);

		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		noofchildrenedit.setAdapter(adapter1);
		noofchildrenedit.setOnItemSelectedListener(this);

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, states);

		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stateedit.setAdapter(adapter2);
		stateedit.setOnItemSelectedListener(this);
        /*
        RG- Additional field to capture Insurance details
         */
        compname=(TextView) findViewById(R.id.compname);
        compnameedit=(EditText) findViewById(R.id.compnameedit);

        plannum=(TextView) findViewById(R.id.plannum);
        plannumedit=(EditText) findViewById(R.id.plannumedit);

        memberid=(TextView) findViewById(R.id.memberid);
        memberidedit=(EditText) findViewById(R.id.memberidedit);

        groupnum=(TextView) findViewById(R.id.groupnum);
        groupnumedit=(EditText) findViewById(R.id.groupnumedit);

        insurancecomments=(TextView) findViewById(R.id.insurancecomments);
        insurancecommentsedit=(EditText) findViewById(R.id.insurancecommentsedit);


		proceed = (Button) findViewById(R.id.proceed);

		cancel = (Button) findViewById(R.id.cancel);

		disableradio1.setTypeface(tf);
		disableradio2.setTypeface(tf);
		profilename.setTypeface(tf);
		welcome.setTypeface(tf);
		title.setTypeface(tf);
		contacttext.setTypeface(tf);
		personaltext.setTypeface(tf);
		lastname.setTypeface(tf);
		middlename.setTypeface(tf);
		firstname.setTypeface(tf);
		patientid.setTypeface(tf);
		comments.setTypeface(tf);
		medicalnumber.setTypeface(tf);
		monthlyincome.setTypeface(tf);
		disable.setTypeface(tf);
		noofchildren.setTypeface(tf);
		marriage.setTypeface(tf);
		dob.setTypeface(tf);
		ssn.setTypeface(tf);
		contactphone.setTypeface(tf);
		contactname.setTypeface(tf);
		emailid.setTypeface(tf);
		mobilephone.setTypeface(tf);
		homephone.setTypeface(tf);
		zipcode.setTypeface(tf);
		state.setTypeface(tf);
		city.setTypeface(tf);
		address2.setTypeface(tf);
		address1.setTypeface(tf);
		text_date.setTypeface(tf);
		digital_clock.setTypeface(tf);
		cancel.setTypeface(tf);
		proceed.setTypeface(tf);
        compname.setTypeface(tf);
        plannum.setTypeface(tf);
        memberid.setTypeface(tf);
        groupnum.setTypeface(tf);
        insurancecomments.setTypeface(tf);


        compnameedit.setTypeface(tf);
        plannumedit.setTypeface(tf);
        memberidedit.setTypeface(tf);
        groupnumedit.setTypeface(tf);
        insurancecommentsedit.setTypeface(tf);
		lastnameedit.setTypeface(tf);
		middlenameedit.setTypeface(tf);
		firstnameedit.setTypeface(tf);
		patientidedit.setTypeface(tf);
		commentsedit.setTypeface(tf);
		medicalnumberedit.setTypeface(tf);
		monthlyincomeedit.setTypeface(tf);
		dobedit.setTypeface(tf);
		ssnedit.setTypeface(tf);
		contactphoneedit.setTypeface(tf);
		emailidedit.setTypeface(tf);
		mobilephoneedit.setTypeface(tf);
		homephoneedit.setTypeface(tf);
		zipcodeedit.setTypeface(tf);
		cityedit.setTypeface(tf);
		address2edit.setTypeface(tf);
		address1edit.setTypeface(tf);
		contactnameedit.setTypeface(tf);

		radio0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				radio0.setChecked(true);
				radio1.setChecked(false);

			}

		});
		radio1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				radio1.setChecked(true);
				radio0.setChecked(false);

			}

		});
		disableradio1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				disableradio1.setChecked(true);
				disableradio2.setChecked(false);

			}

		});
		disableradio2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				disableradio2.setChecked(true);
				disableradio1.setChecked(false);

			}

		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishFromChild(PosInputActivity.this);

			}
		});

		proceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				address = address1edit.getText().toString().trim();
				addresss = address2edit.getText().toString().trim();
				citye = cityedit.getText().toString().trim();
				if(citye != null) {
					if(citye.length() > 0) {
						if(!Utility.validateCityName(citye)) {
							showToast("Please check the city name.");
							return;
						}
					}					
				}
				
				zipcodee = zipcodeedit.getText().toString().trim();
				if(zipcodee.length() > 0) {
					if(!Utility.validateZipcode(zipcodee)) {
						showToast("Please check the zipcode");
						return;
					}
				}
				
				homephonee = homephoneedit.getText().toString().trim();
				mobilephonee = mobilephoneedit.getText().toString().trim();
				emailide = emailidedit.getText().toString().trim();
				if(emailide.length() > 0) {
					if(!Utility.emailValidator(emailide)) {
						showToast("Please check the email id");
						return;
					}
				}
				
				contactnamee = contactnameedit.getText().toString().trim();
				contactphonee = contactphoneedit.getText().toString().trim();
				ssne = ssnedit.getText().toString().trim();
				dobe = dobedit.getText().toString().trim();
				monthlyincomee = monthlyincomeedit.getText().toString().trim();
				medicalnumbere = medicalnumberedit.getText().toString().trim();
				commentse = commentsedit.getText().toString().trim();
				patientide = patientidedit.getText().toString().trim();
				firstnamee = firstnameedit.getText().toString().trim();
				middlenamee = middlenameedit.getText().toString().trim();
				lastnamee = lastnameedit.getText().toString().trim();
				/*
				RG-include additional fields
				 */
                scompname = compnameedit.getText().toString().trim();
                splannum = plannumedit.getText().toString().trim();
                smemberid = memberidedit.getText().toString().trim();
                sgroupnum = groupnumedit.getText().toString().trim();
                sinsurancecomments= insurancecommentsedit.getText().toString().trim();

                if(statess != null) {
					if(!Utility.validateStateName(statess) || statess.length() < 2) {
						showToast("Please check the state name.");
						return;
					}
				}
				String married = "";
				String disabled = "";
				
				if(disableradio1 != null & radio0 != null) {
					if(disableradio1.isChecked())
						married = "Yes";
					else
						married = "No";
					
					if(radio0.isChecked())
						disabled = "Yes";
					else
						disabled = "No";
				}



				 SharedPreferences.Editor editor = getSharedPreferences("medspref", MODE_PRIVATE).edit();
				 editor.putString("patientId", patientide);
				 editor.putString("fname", firstnamee);
				 editor.putString("mname", middlenamee);
				 editor.putString("lname", lastnamee);
				 editor.putString("dob", dobe);
				 editor.putString("married", married);
				 
				 editor.putString("children", noofchildd);
				 editor.putString("email", emailide);
				 editor.putString("disabled", disabled);
				 editor.putString("address1", address);
				 editor.putString("address2", addresss);
				 editor.putString("city", citye);
				 
				 editor.putString("state", statess);
				 editor.putString("zip", zipcodee);
				 editor.putString("hphone", homephonee);
				 editor.putString("mobile", mobilephonee);
				 editor.putString("cname", contactnamee);
				 editor.putString("cphone", contactphonee);
				 
				 editor.putString("ssn", ssne);
				 editor.putString("income", monthlyincomee);
				 editor.putString("records", medicalnumbere);
				 editor.putString("comment", commentse);
                 /*
                 RG- Persist insurance details
                  */
                 editor.putString("compname", scompname);
                 editor.putString("plannum", splannum);
                 editor.putString("memberid", smemberid);
                 editor.putString("groupnum", sgroupnum);
                 editor.putString("insurancecomments", sinsurancecomments);

				 editor.commit();
				
				if (firstnamee.equals("") || lastnamee.equals("")
						|| patientide.equals("")) {
					Toast.makeText(getApplicationContext(),
							"Please Enter all Primary Details...",
							Toast.LENGTH_LONG).show();
				} else {
					Utility.PATIENT_ID=patientide;
					Utility.Create_MY_IMAGES_DIR();

					registr = "First Name : " + firstnamee
							+ ",\tLast Name : " + lastnamee
							+ ",\tMiddle Name : " + middlenamee
							+ ",\tPatient Id : " + patientide
							+ ",\tAddress1 : " + address
							+ ",\tAddress2 : " + addresss
							+ ",\tCity : " + citye
							+ ",\tState : " + statess
							+ ",\tZip code : " + zipcodee
							+ ",\tHome Phone : " + homephonee
							+ ",\tMobile Phone : " + mobilephonee
							+ ",\tEmail : " + emailide
							+ ",\tContact Name : " + contactnamee
							+ ",\tContact Phone : " + contactphonee
							+ ",\tSSN : " + ssne
                            + ",\tDOB : " + dobe
                            + ",\tMonthly Income : " + monthlyincomee
							+ ",\tMedical Number : " + medicalnumbere
                            + ",\tComments : " + commentse
                            + ",\tCompany Name : " + scompname
                            + ",\tPlan Number : " + splannum
                            + ",\tMember Id : " + smemberid
                            + ",\tGroup Number : " + sgroupnum
                            + ",\tComments : " + sinsurancecomments;



					File dest = new File(Utility.MY_IMG_DIR+"/"+Utility.Get_Random_File_Name(patientide)+".txt");
					if (dest.exists()) {
						dest.delete();
						/* RG- Why updated here ? */
                        Toast.makeText(getApplicationContext(),
								"File Updated", Toast.LENGTH_LONG)
								.show();
						
					} 
					
					try {
						dest.createNewFile();
						FileOutputStream object = new FileOutputStream(dest);
						OutputStreamWriter obj = new OutputStreamWriter(
								object);
						obj.append(registr);
						obj.close();
						object.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Intent thirdpage = new Intent(getApplicationContext(),
							ThirdPage.class);
					thirdpage.putExtra("name", firstnamee);
					startActivity(thirdpage);
					overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
				}

			}
		});

	}

	private void findViewsById() {
		dobedit = (EditText) findViewById(R.id.dobedit);
		dobedit.setInputType(InputType.TYPE_NULL);
		dobedit.requestFocus();
		
	}

	private void setDateTimeField() {
		dobedit.setOnClickListener(this);
		
		Calendar newCalendar = Calendar.getInstance();
		fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

	        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	            Calendar newDate = Calendar.getInstance();
	            if(year <= newDate.get(Calendar.YEAR)) {
	            	if(year == newDate.get(Calendar.YEAR)) {
	            		if(monthOfYear <= newDate.get(Calendar.MONTH)) {
	            			if(monthOfYear == newDate.get(Calendar.MONTH)) {
	            				if(dayOfMonth <= newDate.get(Calendar.DATE)) {
	            					newDate.set(year, monthOfYear, dayOfMonth);
	    		            		dobedit.setText(dateFormatter.format(newDate.getTime()));
	            				} else {
	            					showToast("Please select the correct date.");
	            				}
	            			}else {
	            				newDate.set(year, monthOfYear, dayOfMonth);
			            		dobedit.setText(dateFormatter.format(newDate.getTime()));
	            			}
	            		} else {
	            			showToast("Please select the correct date.");
	            		}
	            	} else {
	            		newDate.set(year, monthOfYear, dayOfMonth);
	            		dobedit.setText(dateFormatter.format(newDate.getTime()));
	            	}
	            } else {
	            	showToast("Please select the correct date.");
	            }      
	            
	            
	        }

	    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// preventing default implementation previous to
			// android.os.Build.VERSION_CODES.ECLAIR
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		switch (parent.getId()) {
		case R.id.noofchildrenedit:
			noofchildd = parent.getItemAtPosition(position).toString();

			break;
		case R.id.stateedit:
			
			statess = parent.getItemAtPosition(position).toString();		

			break;
		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == dobedit) {
			fromDatePickerDialog.show();
		} 
		
	}

    public void btn_scan_onClick(View v){

        Log.i(TAG, "scan will be performed");
        // Intent for ScanActivity
        Intent intent = new Intent(this, Pdf417ScanActivity.class);
        // If you want sound to be played after the scanning process ends,
        // put here the resource ID of your sound file. (optional)
        intent.putExtra(Pdf417ScanActivity.EXTRAS_BEEP_RESOURCE, R.raw.beep);

        // set the license key (for commercial versions only) - obtain your key at
        // http://pdf417.mobi
        // after setting the correct license key,
        intent.putExtra(Pdf417ScanActivity.EXTRAS_LICENSE_KEY, "BMLQR53X-FUQZBBJY-5TQ6FUAB-U3DNKABV-A5MSPORX-GPJRWFJ7-FSQ6FUAB-U3DIJJK6"); // demo license key for package mobi.pdf417.demo
//
        // If you want to open front facing camera, uncomment the following line.
        // Note that front facing cameras do not have autofocus support, so it will not
        // be possible to scan denser and smaller codes.
//        intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_TYPE, (Parcelable)CameraType.CAMERA_FRONTFACE);

        // You need to define array of recognizer settings. There are 4 types of recognizers available
        // in PDF417.mobi SDK.

        // Pdf417RecognizerSettings define the settings for scanning plain PDF417 barcodes.
        Pdf417RecognizerSettings pdf417RecognizerSettings = new Pdf417RecognizerSettings();
        // Set this to true to scan barcodes which don't have quiet zone (white area) around it
        // Use only if necessary because it drastically slows down the recognition process
        pdf417RecognizerSettings.setNullQuietZoneAllowed(true);
        // Set this to true to scan even barcode not compliant with standards
        // For example, malformed PDF417 barcodes which were incorrectly encoded
        // Use only if necessary because it slows down the recognition process
//        pdf417RecognizerSettings.setUncertainScanning(true);

        // BarDecoderRecognizerSettings define settings for scanning 1D barcodes with algorithms
        // implemented by Microblink team.
        BarDecoderRecognizerSettings oneDimensionalRecognizerSettings = new BarDecoderRecognizerSettings();
        // set this to true to enable scanning of Code 39 1D barcodes
        oneDimensionalRecognizerSettings.setScanCode39(true);
        // set this to true to enable scanning of Code 128 1D barcodes
        oneDimensionalRecognizerSettings.setScanCode128(true);
        // set this to true to use heavier algorithms for scanning 1D barcodes
        // those algorithms are slower, but can scan lower resolution barcodes
//        oneDimensionalRecognizerSettings.setTryHarder(true);

        // USDLRecognizerSettings define settings for scanning US Driver's Licence barcodes
        // options available in that settings are similar to those in Pdf417RecognizerSettings
        // if license key does not allow scanning of US Driver's License, this settings will
        // be thrown out from settings array and error will be logged to logcat.
        USDLRecognizerSettings usdlRecognizerSettings = new USDLRecognizerSettings();
        usdlRecognizerSettings.setNullQuietZoneAllowed(true);
        // ZXingRecognizerSettings define settings for scanning barcodes with ZXing library
        // We use modified version of ZXing library to support scanning of barcodes for which
        // we still haven't implemented our own algorithms.
        ZXingRecognizerSettings zXingRecognizerSettings = new ZXingRecognizerSettings();
        // set this to true to enable scanning of QR codes
        zXingRecognizerSettings.setScanQRCode(true);

        // finally, when you have defined your scanning settings, you should put them into array
        // and send that array over intent to scan activity

        RecognizerSettings[] settArray = new RecognizerSettings[] {pdf417RecognizerSettings, oneDimensionalRecognizerSettings, zXingRecognizerSettings, usdlRecognizerSettings};
        // use Pdf417ScanActivity.EXTRAS_RECOGNIZER_SETTINGS_ARRAY to set array of recognizer settings
        intent.putExtra(Pdf417ScanActivity.EXTRAS_RECOGNIZER_SETTINGS_ARRAY, settArray);

        // additionally, there are generic settings that are used by all recognizers or the
        // whole recognition process
        GenericRecognizerSettings genericSettings = new GenericRecognizerSettings();
        // set this to true to enable returning of multiple scan results from single camera frame
        // default is false, which means that as soon as first barcode is found (no matter which type)
        // its contents will be returned.
        genericSettings.setAllowMultipleScanResultsOnSingleImage(true);
        intent.putExtra(Pdf417ScanActivity.EXTRAS_GENERIC_SETTINGS, genericSettings);

        // if you do not want the dialog to be shown when scanning completes, add following extra
        // to intent
        //RG - enable below code to disable the dialog box
        intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_DIALOG_AFTER_SCAN, false);

        // if you want to enable pinch to zoom gesture, add following extra to intent
        intent.putExtra(Pdf417ScanActivity.EXTRAS_ALLOW_PINCH_TO_ZOOM, true);

        // if you want Pdf417ScanActivity to display rectangle where camera is focusing,
        // add following extra to intent
        intent.putExtra(Pdf417ScanActivity.EXTRAS_SHOW_FOCUS_RECTANGLE, true);

        // if you want to use camera fit aspect mode to letterbox the camera preview inside
        // available activity space instead of cropping camera frame (default), add following
        // extra to intent.
        // Camera Fit mode does not look as nice as Camera Fill mode on all devices, especially on
        // devices that have very different aspect ratios of screens and cameras. However, it allows
        // all camera frame pixels to be processed - this is useful when reading very large barcodes.
//        intent.putExtra(Pdf417ScanActivity.EXTRAS_CAMERA_ASPECT_MODE, (Parcelable) CameraAspectMode.ASPECT_FIT);

        // Start Activity
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_REQUEST_CODE && resultCode == Pdf417ScanActivity.RESULT_OK) {
            // First, obtain scan results array. If scan was successful, array will contain at least one element.
            // Multiple element may be in array if multiple scan results from single image were allowed in settings.

            Parcelable[] resultArray = data.getParcelableArrayExtra(Pdf417ScanActivity.EXTRAS_RECOGNITION_RESULT_LIST);

            // Each recognition result corresponds to active recognizer. As stated earlier, there are 4 types of
            // recognizers available (PDF417, Bardecoder, ZXing and USDL), so there are 4 types of results
            // available.

            StringBuilder sb = new StringBuilder();

            for(Parcelable p : resultArray) {
                if(p instanceof Pdf417ScanResult) { // check if scan result is result of Pdf417 recognizer
                    Pdf417ScanResult result = (Pdf417ScanResult) p;
                    // getStringData getter will return the string version of barcode contents
                    String barcodeData = result.getStringData();
                    // isUncertain getter will tell you if scanned barcode contains some uncertainties
                    boolean uncertainData = result.isUncertain();
                    // getRawData getter will return the raw data information object of barcode contents
                    BarcodeDetailedData rawData = result.getRawData();
                    // BarcodeDetailedData contains information about barcode's binary layout, if you
                    // are only interested in raw bytes, you can obtain them with getAllData getter
                    byte[] rawDataBuffer = rawData.getAllData();

                    // if data is URL, open the browser and stop processing result
                    if(checkIfDataIsUrlAndCreateIntent(barcodeData)) {
                        return;
                    } else {
                        // add data to string builder
                        sb.append("PDF417 scan data");
                        if (uncertainData) {
                            sb.append("This scan data is uncertain!\n\n");
                        }
                        sb.append(" string data:\n");
                        sb.append(barcodeData);
                        if (rawData != null) {
                            sb.append("\n\n");
                            sb.append("PDF417 raw data:\n");
                            sb.append(rawData.toString());
                            sb.append("\n");
                            sb.append("PDF417 raw data merged:\n");
                            sb.append("{");
                            for (int i = 0; i < rawDataBuffer.length; ++i) {
                                sb.append((int) rawDataBuffer[i] & 0x0FF);
                                if (i != rawDataBuffer.length - 1) {
                                    sb.append(", ");
                                }
                            }
                            sb.append("}\n\n\n");
                        }
                    }
                } else if(p instanceof BarDecoderScanResult) { // check if scan result is result of BarDecoder recognizer
                    BarDecoderScanResult result = (BarDecoderScanResult) p;
                    // with getBarcodeType you can obtain barcode type enum that tells you the type of decoded barcode
                    BarcodeType type = result.getBarcodeType();
                    // as with PDF417, getStringData will return the string contents of barcode
                    String barcodeData = result.getStringData();
                    if(checkIfDataIsUrlAndCreateIntent(barcodeData)) {
                        return;
                    } else {
                        sb.append(type.name());
                        sb.append(" string data:\n");
                        sb.append(barcodeData);
                        sb.append("\n\n\n");
                    }
                } else if(p instanceof ZXingScanResult) { // check if scan result is result of ZXing recognizer
                    ZXingScanResult result= (ZXingScanResult) p;
                    // with getBarcodeType you can obtain barcode type enum that tells you the type of decoded barcode
                    BarcodeType type = result.getBarcodeType();
                    // as with PDF417, getStringData will return the string contents of barcode
                    String barcodeData = result.getStringData();
                    if(checkIfDataIsUrlAndCreateIntent(barcodeData)) {
                        return;
                    } else {
                        sb.append(type.name());
                        sb.append(" string data:\n");
                        sb.append(barcodeData);
                        sb.append("\n\n\n");
                    }
                } else if(p instanceof USDLScanResult) { // check if scan result is result of US Driver's Licence recognizer
                    USDLScanResult result = (USDLScanResult) p;

                    // USDLScanResult can contain lots of information extracted from driver's licence
                    // you can obtain information using the getField method with keys defined in
                    // USDLScanResult class

                    String name = result.getField(USDLScanResult.kCustomerFullName);
                    Log.i(TAG, "Customer full name is " + name);

                    sb.append(result.getTitle());
                    sb.append("\n\n");
                    sb.append(result.toString());
                    /*
                    RG - Populate the fields if they are part of the US DL barcode
                     */

                    firstnameedit.setText(result.getField(USDLScanResult.kCustomerFirstName));
                    lastnameedit.setText(result.getField(USDLScanResult.kCustomerFamilyName));
                    /*
                    RG - Fix the DOB format
                     */
                    String val = result.getField(USDLScanResult.kDateOfBirth);
                    showToast(val.toString());
                    StringBuffer stringBuffer = new StringBuffer();
                    String a = "";
                    String b = "";
                    String c = "";

                    a = val.substring(0, 2);
                    b = val.substring(2, 4);
                    c = val.substring(4, val.length());
                    stringBuffer.append(a);
                    stringBuffer.append("-");
                    stringBuffer.append(b);
                    stringBuffer.append("-");
                    stringBuffer.append(c);
                    dobedit.setText(stringBuffer.toString());

                    address1edit.setText(result.getField(USDLScanResult.kAddressStreet));
                    address2edit.setText(result.getField(USDLScanResult.kAddressStreet2));
                    cityedit.setText(result.getField(USDLScanResult.kAddressCity));

                    /*
                    RG - fix the zip code
                     */
                    stringBuffer = new StringBuffer();
                    a = "";
                    b = "";
                    val="";
                    val = result.getField(USDLScanResult.kAddressPostalCode);

                    a = val.substring(0, 5);
                    b = val.substring(5, val.length());
                    stringBuffer.append(a);
                    stringBuffer.append("-");
                    stringBuffer.append(b);

                    zipcodeedit.setText(stringBuffer.toString());

                }
            }

            /*
            RG - Comment this and as we not going to sent it to anyother application

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
            startActivity(Intent.createChooser(intent, getString(R.string.UseWith)));


            */

        }
    }

    private boolean checkIfDataIsUrlAndCreateIntent(String data) {
        // if barcode contains URL, create intent for browser
        // else, contain intent for message
        boolean barcodeDataIsUrl;

        try {
            @SuppressWarnings("unused")
            URL url = new URL(data);
            barcodeDataIsUrl = true;
        } catch (MalformedURLException exc) {
            barcodeDataIsUrl = false;
        }

        if (barcodeDataIsUrl) {
            // create intent for browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(data));
            startActivity(intent);
        }

        return barcodeDataIsUrl;
    }
	private void showToast(String msg) {
		Toast.makeText(PosInputActivity.this, msg, Toast.LENGTH_LONG).show();
	}

}
