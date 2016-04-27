package com.pos1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat") public class Utility {
	/*
	 * getBytes() Convert bitmap image to byte array and retrun byte[]
	 * getPhoto() convert byte[] to bitmap and retrun bitmap image
	 */
	public static File MY_IMG_DIR, Default_DIR;
	public static File Copy_sourceLocation;
	public static File Paste_Target_Location;
	public static Intent pictureActionIntent = null;
	public static final int CAMERA_PICTURE = 1;
	public static String PATIENT_ID="";


	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
		String currentDate = sdf.format(new Date());

		return currentDate;
	}
	
	public static String Get_Random_File_Name(String pid)
	{
		final Calendar c = Calendar.getInstance();
		int myYear = c.get(Calendar.YEAR);
		int myMonth = c.get(Calendar.MONTH);
		int myDay = c.get(Calendar.DAY_OF_MONTH);
		myMonth=myMonth+1;
		String Random_Image_Text = pid+"_" + myDay + myMonth + myYear;
		return Random_Image_Text;
	}



	public static File Create_MY_IMAGES_DIR() {
		try {
			// Get SD Card path & your folder name
			MY_IMG_DIR = new File(Environment.getExternalStorageDirectory(),"/MEDS/" + Utility.PATIENT_ID + "/");

			// check if exist
			if (!MY_IMG_DIR.exists()) {
				// Create New folder
				MY_IMG_DIR.mkdirs();
				Log.i("path", ">>.." + MY_IMG_DIR);
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.e("Create_MY_IMAGES_DIR", "" + e);
		}
		return MY_IMG_DIR;
	}
	
	public static boolean validateZipcode(String zip) {
		Pattern pattern = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
		Matcher m = pattern.matcher(zip);
		if(!m.matches())
    		return false;
    	else
    		return true;		
	}
	
	public static boolean emailValidator(String email){
    	Pattern pattern = Patterns.EMAIL_ADDRESS;  //Validating emails
    	Matcher matcher = pattern.matcher(email);
    	if(!matcher.matches())
    		return false;
    	else
    		return true;
    }
	
	public static boolean validateCityName(String name) {		
	    Pattern pattern = Pattern.compile("[a-zA-Z]+(?:[ '-][a-zA-Z]+)*");
	  	Matcher matcher = pattern.matcher(name);
	  	if(!matcher.matches())
	  		return false;
	  	else
	  		return true;
		
	}
	
	public static boolean validateStateName(String name) {		
		String[] states = {"alabama",
	    		"alaska", 
	    		"arizona", 
	    		"arkansas", 
	    		"california", 
	    		"colorado", 
	    		"connecticut", 
	    		"delaware", 
	    		"florida", 
	    		"georgia", 
	    		"hawaii", 
	    		"idaho", 
	    		"illinois indiana", 
	    		"iowa", 
	    		"kansas", 
	    		"kentucky", 
	    		"louisiana", 
	    		"maine", 
	    		"maryland", 
	    		"massachusetts", 
	    		"michigan", 
	    		"minnesota", 
	    		"mississippi", 
	    		"missouri", 
	    		"montana nebraska", 
	    		"nevada", 
	    		"new hampshire", 
	    		"new jersey", 
	    		"new mexico", 
	    		"new york", 
	    		"north carolina", 
	    		"north dakota", 
	    		"ohio", 
	    		"oklahoma", 
	    		"oregon", 
	    		"pennsylvania rhode island", 
	    		"south carolina", 
	    		"south dakota", 
	    		"tennessee", 
	    		"texas", 
	    		"utah", 
	    		"vermont", 
	    		"virginia", 
	    		"washington", 
	    		"west virginia", 
	    		"wisconsin", 
	    		"wyoming"};
	    
	    List<String> stateList = Arrays.asList(states);
	    if(stateList.contains(name.toLowerCase()))
	    	return true;
	    else
	    	return false;
	    
	}
	
	public static String[] getStates() {
		String[] states = {"Alabama",
	    		"Alaska", 
	    		"Arizona", 
	    		"Arkansas", 
	    		"California", 
	    		"Colorado", 
	    		"Connecticut", 
	    		"Delaware", 
	    		"Florida", 
	    		"Georgia", 
	    		"Hawaii", 
	    		"Idaho", 
	    		"Illinois Indiana", 
	    		"Iowa", 
	    		"Kansas", 
	    		"Kentucky", 
	    		"Louisiana", 
	    		"Maine", 
	    		"Maryland", 
	    		"Massachusetts", 
	    		"Michigan", 
	    		"Minnesota", 
	    		"Mississippi", 
	    		"Missouri", 
	    		"Montana Nebraska", 
	    		"Nevada", 
	    		"New Hampshire", 
	    		"New Jersey", 
	    		"New Mexico", 
	    		"New York", 
	    		"North Carolina", 
	    		"North Dakota", 
	    		"Ohio", 
	    		"Oklahoma", 
	    		"Oregon", 
	    		"Pennsylvania Rhode Island", 
	    		"South Carolina", 
	    		"South Dakota", 
	    		"Tennessee", 
	    		"Texas", 
	    		"Utah", 
	    		"Vermont", 
	    		"Virginia", 
	    		"Washington", 
	    		"West Virginia", 
	    		"Wisconsin", 
	    		"Wyoming"};
		
		return states;
	}

	public static Bitmap getResizedBitmap(Uri imageUri, int newWidth, int newHeight, Context context) {
		Bitmap bm;
		Bitmap resizedBitmap = null;
		
		try {
			bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(),imageUri);
			int width = bm.getWidth();
		    int height = bm.getHeight();
		    float scaleWidth = ((float) newWidth) / width;
		    float scaleHeight = ((float) newHeight) / height;
		    // CREATE A MATRIX FOR THE MANIPULATION
		    Matrix matrix = new Matrix();
		    // RESIZE THE BIT MAP
		    matrix.postScale(scaleWidth, scaleHeight);

		    // "RECREATE" THE NEW BITMAP
		    resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	    return resizedBitmap;
	}
	
}
