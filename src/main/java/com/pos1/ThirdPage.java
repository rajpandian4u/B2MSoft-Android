package com.pos1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("deprecation")
public class ThirdPage extends Activity {
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	ImageView firstimage1, firstimage2, firstimage3, firstimage4;
	FrameLayout frame1, frame2, frame3, frame4;
	Button close1, close2, close3, close4, btnpdf, delete1, delete2, delete3,
			delete4,back,proceed;
	int selected = 0;
	LinearLayout doctypelay1, doctypelay2, doctypelay3, doctypelay4;
	TextView documenttype1, documenttype2, documenttype3, documenttype4, documenttype1edit, documenttype2edit, documenttype3edit,
			documenttype4edit;
	TextView text_date;
	DigitalClock digital_clock;
	List<Uri> listuri;
	List<String> listname;
	List<String> filename;
	List<String> filetype;
	Spinner documenttypeedit;
	public static String SelectedProof;
	String[] spin = { "Select", "SSN Card", "Driving License", "Passport",
			"Bank Statement", "Others" };
	
	//boolean isSSNSelected = false;
	//boolean isDLSelected = false;
	//boolean isPassportSelected = false;
	//boolean isBankStmtSlected = false;
	int prevPosition = 0;
	private ProgressDialog dialog = null;
	private static String USER_PASS = "record";
	 
    private static String OWNER_PASS = "record";

	@SuppressLint("ClickableViewAccessibility")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.document);
		
		
		text_date = (TextView) findViewById(R.id.text_date);
		digital_clock = (DigitalClock) findViewById(R.id.digital_clock);
		text_date.setText(Utility.getDate());
		Utility.Create_MY_IMAGES_DIR();
		listuri = new ArrayList<Uri>();
		listuri.clear();
		listname = new ArrayList<String>();
		listname.clear();
		filename = new ArrayList<String>();
		filename.clear();
		filetype = new ArrayList<String>();
		filetype.clear();
		firstimage1 = (ImageView) findViewById(R.id.firstimage1);
		firstimage2 = (ImageView) findViewById(R.id.firstimage2);
		firstimage3 = (ImageView) findViewById(R.id.firstimage3);
		firstimage4 = (ImageView) findViewById(R.id.firstimage4);
		frame1 = (FrameLayout) findViewById(R.id.frame1);
		frame2 = (FrameLayout) findViewById(R.id.frame2);
		frame3 = (FrameLayout) findViewById(R.id.frame3);
		frame4 = (FrameLayout) findViewById(R.id.frame4);
		close1 = (Button) findViewById(R.id.close1);
		close2 = (Button) findViewById(R.id.close2);
		close3 = (Button) findViewById(R.id.close3);
		close4 = (Button) findViewById(R.id.close4);
		btnpdf = (Button) findViewById(R.id.btnpdf);
		doctypelay1 = (LinearLayout) findViewById(R.id.doctypelay1);
		doctypelay2 = (LinearLayout) findViewById(R.id.doctypelay2);
		doctypelay3 = (LinearLayout) findViewById(R.id.doctypelay3);
		doctypelay4 = (LinearLayout) findViewById(R.id.doctypelay4);
		documenttype1edit = (TextView) findViewById(R.id.documenttype1edit);
		documenttype2edit = (TextView) findViewById(R.id.documenttype2edit);
		documenttype3edit = (TextView) findViewById(R.id.documenttype3edit);
		documenttype4edit = (TextView) findViewById(R.id.documenttype4edit);
		documenttypeedit = (Spinner) findViewById(R.id.documenttypeedit);
		documenttype1edit.setClickable(true);
		documenttype2edit.setClickable(true);
		documenttype3edit.setClickable(true);
		documenttype4edit.setClickable(true);
		documenttype1edit.setEnabled(true);
		documenttype2edit.setEnabled(true);
		documenttype3edit.setEnabled(true);
		documenttype4edit.setEnabled(true);
        documenttype1 = (TextView) findViewById(R.id.documenttype1);
        documenttype2 = (TextView) findViewById(R.id.documenttype2);
        documenttype3 = (TextView) findViewById(R.id.documenttype3);
        documenttype4 = (TextView) findViewById(R.id.documenttype4);

		delete1 = (Button) findViewById(R.id.delete1);
		delete2 = (Button) findViewById(R.id.delete2);
		delete3 = (Button) findViewById(R.id.delete3);
		delete4 = (Button) findViewById(R.id.delete4);
		proceed = (Button) findViewById(R.id.proceed);
		
		proceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

                SharedPreferences.Editor editor = getSharedPreferences("medspref", MODE_PRIVATE).edit();
                editor.putString("documenttype1", documenttype1.getText().toString().trim());
                editor.putString("documenttype2", documenttype2.getText().toString().trim());
                editor.putString("documenttype3", documenttype3.getText().toString().trim());
                editor.putString("documenttype4", documenttype4.getText().toString().trim());
                editor.putString("documenttype1edit", documenttype1edit.getText().toString().trim());
                editor.putString("documenttype2edit", documenttype2edit.getText().toString().trim());
                editor.putString("documenttype3edit", documenttype3edit.getText().toString().trim());
                editor.putString("documenttype4edit", documenttype4edit.getText().toString().trim());

                editor.commit();

                Intent summary = new Intent(getApplicationContext(),
						SummaryList.class);
				
				startActivity(summary);
				overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

			}
		});
		back=(Button)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent thirdpage = new Intent(getApplicationContext(),
//						PosInputActivity.class);
//				
//				startActivity(thirdpage);
				finish();

			}
		});

		ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spin) {
			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);

				//((TextView) v).setTextSize(14);
				((TextView) v).setTextColor(Color.BLACK);

				return v;

			}

			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {

				View v = super.getDropDownView(position, convertView, parent);
				//((TextView) v).setTextSize(14);
				((TextView) v).setGravity(Gravity.CENTER);

				return v;

			}
		};
		documenttypeedit.setAdapter(classAdapter);
		documenttypeedit
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						SelectedProof = documenttypeedit.getItemAtPosition(
								position).toString();						
						
						if(checkDocumentAvailable()) {
							documenttypeedit.setSelection(prevPosition);
							showToast("Please process the added document as pdf first for further documents upload");
						} else  if(checkDocumentProcessed()){
							documenttypeedit.setSelection(0);
							showToast(SelectedProof +"'s document already uploaded");
						} else {
							prevPosition = position;
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		documenttype1edit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				File fil = new File(filename.get(0));
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(fil), "application/pdf");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				return false;
			}
		});
		documenttype2edit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				File fil = new File(filename.get(1));
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(fil), "application/pdf");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				return false;
			}
		});
		documenttype3edit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				File fil = new File(filename.get(2));
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(fil), "application/pdf");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				return false;
			}
		});
		documenttype4edit.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				File fil = new File(filename.get(3));
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(fil), "application/pdf");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

				return false;
			}
		});
		close1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File fil=new File(listuri.get(0).getPath().toString());
				Log.d("uri", listuri.get(0).toString());
				if(fil.exists())
					fil.delete();
				if(fil.exists())
					fil.deleteOnExit();
				
				if(filetype.size() > 0)
					filetype.remove(0);
		
				listuri.remove(0);
				if (listuri.size() == 1) {
					firstimage1.setScaleType(ScaleType.FIT_XY);
					firstimage1.setImageBitmap(Utility.getResizedBitmap(listuri.get(0), 50, 50, ThirdPage.this));
					close1.setVisibility(View.VISIBLE);
					close2.setVisibility(View.INVISIBLE);
					frame2.setVisibility(View.VISIBLE);
					frame3.setVisibility(View.INVISIBLE);
					firstimage2.setImageResource(R.drawable.imageback);
					frame1.setEnabled(false);
					frame2.setEnabled(true);
				} else if (listuri.size() == 2) {
					firstimage1.setScaleType(ScaleType.FIT_XY);
					firstimage1.setImageBitmap(Utility.getResizedBitmap(listuri.get(0), 50, 50, ThirdPage.this));
					close1.setVisibility(View.VISIBLE);
					frame2.setVisibility(View.VISIBLE);
					firstimage2.setScaleType(ScaleType.FIT_XY);
					firstimage2.setImageBitmap(Utility.getResizedBitmap(listuri.get(1), 50, 50, ThirdPage.this));
					close2.setVisibility(View.VISIBLE);
					close3.setVisibility(View.INVISIBLE);
					frame3.setVisibility(View.VISIBLE);
					frame4.setVisibility(View.INVISIBLE);
					firstimage3.setImageResource(R.drawable.imageback);
					frame1.setEnabled(false);
					frame2.setEnabled(false);
					frame3.setEnabled(true);
				} else if (listuri.size() == 3) {
					firstimage1.setScaleType(ScaleType.FIT_XY);
					firstimage1.setImageBitmap(Utility.getResizedBitmap(listuri.get(0), 50, 50, ThirdPage.this));
					close1.setVisibility(View.VISIBLE);
					frame2.setVisibility(View.VISIBLE);
					firstimage2.setScaleType(ScaleType.FIT_XY);
					firstimage2.setImageBitmap(Utility.getResizedBitmap(listuri.get(1), 50, 50, ThirdPage.this));
					close2.setVisibility(View.VISIBLE);
					frame3.setVisibility(View.VISIBLE);
					firstimage3.setScaleType(ScaleType.FIT_XY);
					firstimage3.setImageBitmap(Utility.getResizedBitmap(listuri.get(2), 50, 50, ThirdPage.this));
					close3.setVisibility(View.VISIBLE);
					frame4.setVisibility(View.VISIBLE);
					close4.setVisibility(View.INVISIBLE);
					firstimage4.setImageResource(R.drawable.imageback);
					
					frame1.setEnabled(false);
					frame2.setEnabled(false);
					frame3.setEnabled(false);
					frame4.setEnabled(true);
				} else {
					firstimage1.setImageResource(R.drawable.imageback);
					close1.setVisibility(View.INVISIBLE);
					frame2.setVisibility(View.INVISIBLE);
					btnpdf.setEnabled(false);
					frame1.setEnabled(true);
				}

			}
		});

		close2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File fil=new File(listuri.get(1).getPath().toString());
				if(fil.exists())
					fil.delete();
				if(fil.exists())
					fil.deleteOnExit();
				
				if(filetype.size() > 1)
					filetype.remove(1);
				
				listuri.remove(1);
				if (listuri.size() == 1) {
					firstimage1.setScaleType(ScaleType.FIT_XY);
					firstimage1.setImageBitmap(Utility.getResizedBitmap(listuri.get(0), 50, 50, ThirdPage.this));
					close1.setVisibility(View.VISIBLE);
					frame2.setVisibility(View.VISIBLE);
					close2.setVisibility(View.INVISIBLE);
					frame3.setVisibility(View.INVISIBLE);
					firstimage2.setImageResource(R.drawable.imageback);
					frame1.setEnabled(false);
					frame2.setEnabled(true);
				} else if (listuri.size() == 2) {
					firstimage1.setScaleType(ScaleType.FIT_XY);
					firstimage1.setImageBitmap(Utility.getResizedBitmap(listuri.get(0), 50, 50, ThirdPage.this));
					close1.setVisibility(View.VISIBLE);
					frame2.setVisibility(View.VISIBLE);
					firstimage2.setScaleType(ScaleType.FIT_XY);
					firstimage2.setImageBitmap(Utility.getResizedBitmap(listuri.get(1), 50, 50, ThirdPage.this));
					close2.setVisibility(View.VISIBLE);
					frame3.setVisibility(View.VISIBLE);
					close3.setVisibility(View.INVISIBLE);
					frame4.setVisibility(View.INVISIBLE);
					firstimage3.setImageResource(R.drawable.imageback);
					frame1.setEnabled(false);
					frame2.setEnabled(false);
					frame3.setEnabled(true);
				} else if (listuri.size() == 3) {
					firstimage1.setScaleType(ScaleType.FIT_XY);
					firstimage1.setImageBitmap(Utility.getResizedBitmap(listuri.get(0), 50, 50, ThirdPage.this));
					close1.setVisibility(View.VISIBLE);
					frame2.setVisibility(View.VISIBLE);
					firstimage2.setScaleType(ScaleType.FIT_XY);
					firstimage2.setImageBitmap(Utility.getResizedBitmap(listuri.get(1), 50, 50, ThirdPage.this));
					close2.setVisibility(View.VISIBLE);
					frame3.setVisibility(View.VISIBLE);
					firstimage3.setScaleType(ScaleType.FIT_XY);
					firstimage3.setImageBitmap(Utility.getResizedBitmap(listuri.get(2), 50, 50, ThirdPage.this));
					close3.setVisibility(View.VISIBLE);
					frame4.setVisibility(View.VISIBLE);
					close4.setVisibility(View.INVISIBLE);
					firstimage4.setImageResource(R.drawable.imageback);
					frame1.setEnabled(false);
					frame2.setEnabled(false);
					frame3.setEnabled(false);
					frame4.setEnabled(true);
				}

			}
		});

		close3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File fil=new File(listuri.get(2).getPath().toString());
				if(fil.exists())
					fil.delete();
				if(fil.exists())
					fil.deleteOnExit();
				
				if(filetype.size() > 2)
					filetype.remove(2);
				
				listuri.remove(2);
				if (listuri.size() == 2) {
					firstimage1.setScaleType(ScaleType.FIT_XY);
					firstimage1.setImageBitmap(Utility.getResizedBitmap(listuri.get(0), 50, 50, ThirdPage.this));
					close1.setVisibility(View.VISIBLE);
					frame2.setVisibility(View.VISIBLE);
					firstimage2.setScaleType(ScaleType.FIT_XY);
					firstimage2.setImageBitmap(Utility.getResizedBitmap(listuri.get(1), 50, 50, ThirdPage.this));
					close2.setVisibility(View.VISIBLE);
					close3.setVisibility(View.INVISIBLE);
					frame3.setVisibility(View.VISIBLE);
					frame4.setVisibility(View.INVISIBLE);
					firstimage3.setImageResource(R.drawable.imageback);
					frame1.setEnabled(false);
					frame2.setEnabled(false);
					frame3.setEnabled(true);
				} else if (listuri.size() == 3) {
					firstimage1.setScaleType(ScaleType.FIT_XY);
					firstimage1.setImageBitmap(Utility.getResizedBitmap(listuri.get(0), 50, 50, ThirdPage.this));
					close1.setVisibility(View.VISIBLE);
					frame2.setVisibility(View.VISIBLE);
					firstimage2.setScaleType(ScaleType.FIT_XY);
					firstimage2.setImageBitmap(Utility.getResizedBitmap(listuri.get(1), 50, 50, ThirdPage.this));
					close2.setVisibility(View.VISIBLE);
					frame3.setVisibility(View.VISIBLE);
					firstimage3.setScaleType(ScaleType.FIT_XY);
					firstimage3.setImageBitmap(Utility.getResizedBitmap(listuri.get(2), 50, 50, ThirdPage.this));
					close3.setVisibility(View.VISIBLE);
					frame4.setVisibility(View.VISIBLE);
					close4.setVisibility(View.INVISIBLE);
					firstimage4.setImageResource(R.drawable.imageback);
					frame1.setEnabled(false);
					frame2.setEnabled(false);
					frame3.setEnabled(false);
					frame4.setEnabled(true);
				}

			}
		});

		close4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File fil=new File(listuri.get(3).getPath().toString());
				if(fil.exists())
					fil.delete();
				if(fil.exists())
					fil.deleteOnExit();
				
				if(filetype.size() > 3)
					filetype.remove(3);
				
				listuri.remove(3);
				firstimage1.setScaleType(ScaleType.FIT_XY);
				firstimage1.setImageBitmap(Utility.getResizedBitmap(listuri.get(0), 50, 50, ThirdPage.this));
				close1.setVisibility(View.VISIBLE);
				frame2.setVisibility(View.VISIBLE);
				firstimage2.setScaleType(ScaleType.FIT_XY);
				firstimage2.setImageBitmap(Utility.getResizedBitmap(listuri.get(1), 50, 50, ThirdPage.this));
				close2.setVisibility(View.VISIBLE);
				frame3.setVisibility(View.VISIBLE);
				firstimage3.setScaleType(ScaleType.FIT_XY);
				firstimage3.setImageBitmap(Utility.getResizedBitmap(listuri.get(2), 50, 50, ThirdPage.this));
				close3.setVisibility(View.VISIBLE);
				frame4.setVisibility(View.VISIBLE);
				close4.setVisibility(View.INVISIBLE);
				firstimage4.setImageResource(R.drawable.imageback);
				frame1.setEnabled(false);
				frame2.setEnabled(false);
				frame3.setEnabled(false);
				frame4.setEnabled(true);

			}
		});

		frame1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!SelectedProof.equalsIgnoreCase("Select") ) {
						selected = 1;
						Utility.pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	
						Utility.pictureActionIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
						startActivityForResult(Utility.pictureActionIntent,	Utility.CAMERA_PICTURE);
					//}
				} else
					Toast.makeText(getApplicationContext(),	"Select Document Type", Toast.LENGTH_SHORT).show();
				}
			});
		
		frame2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!SelectedProof.equalsIgnoreCase("Select")) {
						selected = 2;
						Utility.pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

						fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

						Utility.pictureActionIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
						startActivityForResult(Utility.pictureActionIntent,	Utility.CAMERA_PICTURE);

					//}
				} else
					Toast.makeText(getApplicationContext(),	"Select Document Type", Toast.LENGTH_SHORT).show();

				}
			});

		frame3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!SelectedProof.equalsIgnoreCase("Select")) {
						selected = 3;
						Utility.pictureActionIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

						fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

						Utility.pictureActionIntent.putExtra(
								MediaStore.EXTRA_OUTPUT, fileUri);
						startActivityForResult(Utility.pictureActionIntent,
								Utility.CAMERA_PICTURE);
					//}					
				} else
					Toast.makeText(getApplicationContext(),
							"Select Document Type", Toast.LENGTH_SHORT).show();
				}
			});
		frame4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!SelectedProof.equalsIgnoreCase("Select")) {
					/*if(checkDocumentAvailable()) {
						documenttypeedit.setSelection(0);
						showToast(SelectedProof +"'s document already uploaded");
					} else {*/
						selected = 4;
						Utility.pictureActionIntent = new Intent(
								android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

						fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

						Utility.pictureActionIntent.putExtra(
								MediaStore.EXTRA_OUTPUT, fileUri);
						startActivityForResult(Utility.pictureActionIntent,
								Utility.CAMERA_PICTURE);
					//}					
				} else
					Toast.makeText(getApplicationContext(),
							"Select Document Type", Toast.LENGTH_SHORT).show();

			}

		});

		btnpdf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				

				ProcessPdf processPdf = new ProcessPdf() {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						dialog = ProgressDialog.show(ThirdPage.this, "", "Processing ...");
					}
					
					@Override
					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						dialog.dismiss();						
						filetype.clear();
						listuri.clear();
						selected = 0;
						if(documenttypeedit != null)
							documenttypeedit.setSelection(0);
						
						 if (filename.size() == 1) {
						     doctypelay1.setVisibility(View.VISIBLE);
                             documenttype1edit.setText(listname.get(0));
						     documenttype1.setText(SelectedProof);
						     } else if (filename.size() == 2) {
						            doctypelay1.setVisibility(View.VISIBLE);
						            documenttype1edit.setText(listname.get(0));
						            doctypelay2.setVisibility(View.VISIBLE);
						            documenttype2edit.setText(listname.get(1));
                                    documenttype2.setText(SelectedProof);
						       } else if (filename.size() == 3) {
						        doctypelay1.setVisibility(View.VISIBLE);
						        documenttype1edit.setText(listname.get(0));
						        doctypelay2.setVisibility(View.VISIBLE);
						        documenttype2edit.setText(listname.get(1));
						        doctypelay3.setVisibility(View.VISIBLE);
						        documenttype3edit.setText(listname.get(2));
                                documenttype3.setText(SelectedProof);
						       }

						       else if (filename.size() == 4) {
						        doctypelay1.setVisibility(View.VISIBLE);
						        documenttype1edit.setText(listname.get(0));
						        doctypelay2.setVisibility(View.VISIBLE);
						        documenttype2edit.setText(listname.get(1));
						        doctypelay3.setVisibility(View.VISIBLE);
						        documenttype3edit.setText(listname.get(2));
						        doctypelay4.setVisibility(View.VISIBLE);
						        documenttype4edit.setText(listname.get(3));
                                documenttype4.setText(SelectedProof);
						       }

						firstimage1.setImageResource(R.drawable.imageback);
						firstimage2.setVisibility(View.GONE);
						firstimage3.setVisibility(View.GONE);
						firstimage4.setVisibility(View.GONE);
						frame2.setVisibility(View.GONE);
						frame3.setVisibility(View.GONE);
						frame4.setVisibility(View.GONE);
						close1.setVisibility(View.GONE);
						close2.setVisibility(View.GONE);
						close3.setVisibility(View.GONE);
						close4.setVisibility(View.GONE);
						btnpdf.setEnabled(false);
						frame1.setEnabled(true);
						frame2.setEnabled(false);
						frame3.setEnabled(false);
						frame4.setEnabled(false);
					}
				};
				
				String[] params = {""};
				processPdf.execute(params);

			}			
		});

		delete1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File fil = new File(filename.get(0));
				if (fil.exists())
					fil.delete();
				File fil1 = new File(listuri.get(0).toString());
				if (fil1.exists())
					fil1.delete();
				filename.remove(0);
				listname.remove(0);
				if (filename.size() == 0) {
					doctypelay1.setVisibility(View.INVISIBLE);
				} else if (filename.size() == 1) {
					doctypelay1.setVisibility(View.VISIBLE);
					doctypelay2.setVisibility(View.INVISIBLE);
					documenttype1edit.setText(listname.get(0));
				} else if (filename.size() == 2) {
					doctypelay1.setVisibility(View.VISIBLE);
					doctypelay2.setVisibility(View.VISIBLE);
					doctypelay3.setVisibility(View.INVISIBLE);
					documenttype1edit.setText(listname.get(0));
					documenttype2edit.setText(listname.get(1));
				} else if (filename.size() == 3) {
					doctypelay1.setVisibility(View.VISIBLE);
					doctypelay2.setVisibility(View.VISIBLE);
					doctypelay3.setVisibility(View.VISIBLE);
					doctypelay4.setVisibility(View.INVISIBLE);
					documenttype1edit.setText(listname.get(0));
					documenttype2edit.setText(listname.get(1));
					documenttype3edit.setText(listname.get(2));
				}

			}
		});

		delete2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File fil = new File(filename.get(1));
				if (fil.exists())
					fil.delete();
				File fil1 = new File(listuri.get(1).toString());
				if (fil1.exists())
					fil1.delete();
				filename.remove(1);
				listname.remove(1);
				if (filename.size() == 1) {
					doctypelay1.setVisibility(View.VISIBLE);
					doctypelay2.setVisibility(View.INVISIBLE);
					documenttype1edit.setText(listname.get(0));
				} else if (filename.size() == 2) {
					doctypelay1.setVisibility(View.VISIBLE);
					doctypelay2.setVisibility(View.VISIBLE);
					doctypelay3.setVisibility(View.INVISIBLE);
					documenttype1edit.setText(listname.get(0));
					documenttype2edit.setText(listname.get(1));
				} else if (filename.size() == 3) {
					doctypelay1.setVisibility(View.VISIBLE);
					doctypelay2.setVisibility(View.VISIBLE);
					doctypelay3.setVisibility(View.VISIBLE);
					doctypelay4.setVisibility(View.INVISIBLE);
					documenttype1edit.setText(listname.get(0));
					documenttype2edit.setText(listname.get(1));
					documenttype3edit.setText(listname.get(2));
				}

			}
		});
		delete3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File fil = new File(filename.get(2));
				if (fil.exists())
					fil.delete();
				File fil1 = new File(listuri.get(2).toString());
				if (fil1.exists())
					fil1.delete();
				filename.remove(2);
				listname.remove(2);
				if (filename.size() == 2) {
					doctypelay1.setVisibility(View.VISIBLE);
					doctypelay2.setVisibility(View.VISIBLE);
					doctypelay3.setVisibility(View.INVISIBLE);
					documenttype1edit.setText(listname.get(0));
					documenttype2edit.setText(listname.get(1));
				} else if (filename.size() == 3) {
					doctypelay1.setVisibility(View.VISIBLE);
					doctypelay2.setVisibility(View.VISIBLE);
					doctypelay3.setVisibility(View.VISIBLE);
					doctypelay4.setVisibility(View.INVISIBLE);
					documenttype1edit.setText(listname.get(0));
					documenttype2edit.setText(listname.get(1));
					documenttype3edit.setText(listname.get(2));
				}

			}
		});

		delete4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				File fil = new File(filename.get(3));
				if (fil.exists())
					fil.delete();
				File fil1 = new File(listuri.get(3).toString());
				if (fil1.exists())
					fil1.delete();
				filename.remove(3);
				listname.remove(3);
				doctypelay1.setVisibility(View.VISIBLE);
				doctypelay2.setVisibility(View.VISIBLE);
				doctypelay3.setVisibility(View.VISIBLE);
				doctypelay4.setVisibility(View.INVISIBLE);
				documenttype1edit.setText(listname.get(0));
				documenttype2edit.setText(listname.get(1));
				documenttype3edit.setText(listname.get(2));

			}
		});

	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),"/MEDS/"+Utility.PATIENT_ID+"/"+SelectedProof);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				// Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
				// + IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		File mediaFile;
		String str1=Get_Random_File_Name(Utility.PATIENT_ID);
		
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath()+File.separator+SelectedProof+"_" +  str1 + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Utility.CAMERA_PICTURE && resultCode == RESULT_OK) {
			btnpdf.setClickable(true);
			btnpdf.setEnabled(true);
			listuri.add(fileUri);
			//if(filetype.size()==0)	
				filetype.add(SelectedProof);
			//else{}
			
			if (selected == 1) {
				firstimage1.setScaleType(ScaleType.FIT_XY);
				firstimage1.setImageURI(fileUri);
				close1.setVisibility(View.VISIBLE);
				frame2.setVisibility(View.VISIBLE);
				firstimage2.setImageResource(R.drawable.imageback);
				firstimage2.setVisibility(View.VISIBLE);
				frame1.setEnabled(false);
				frame2.setEnabled(true);
			} else if (selected == 2) {
				firstimage2.setScaleType(ScaleType.FIT_XY);
				firstimage2.setImageURI(fileUri);
				close2.setVisibility(View.VISIBLE);
				frame3.setVisibility(View.VISIBLE);
				firstimage3.setImageResource(R.drawable.imageback);
				firstimage3.setVisibility(View.VISIBLE);
				frame2.setEnabled(false);
				frame3.setEnabled(true);
			} else if (selected == 3) {
				firstimage3.setScaleType(ScaleType.FIT_XY);
				firstimage3.setImageURI(fileUri);
				close3.setVisibility(View.VISIBLE);
				frame4.setVisibility(View.VISIBLE);
				firstimage4.setImageResource(R.drawable.imageback);
				firstimage4.setVisibility(View.VISIBLE);
				frame3.setEnabled(false);
				frame4.setEnabled(true);
			} else if (selected == 4) {
				firstimage4.setScaleType(ScaleType.FIT_XY);
				firstimage4.setImageURI(fileUri);
				close4.setVisibility(View.VISIBLE);
				frame4.setEnabled(false);
			}
		} else if (resultCode == RESULT_CANCELED) {
			Toast toast = Toast.makeText(this, "User Cancelled",
					Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if (keyCode == KeyEvent.KEYCODE_BACK) {
         //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
         return true;
         }
         return super.onKeyDown(keyCode, event);    
    }
    
    public static String generateRandomString(int length) throws Exception {

		  StringBuffer buffer = new StringBuffer();
		  String characters = "1234567890";

		 
		  
		  int charactersLength = characters.length();

		  for (int i = 0; i < length; i++) {
		   double index = Math.random() * charactersLength;
		   buffer.append(characters.charAt((int) index));
		  }
		  return buffer.toString();
		 }
    
    public static String Get_Random_File_Name(String str)
    {
    	String randome=null;
    	try {
			randome=generateRandomString(2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     final Calendar c = Calendar.getInstance();
	     int myYear = c.get(Calendar.YEAR);
	     int myMonth = c.get(Calendar.MONTH);
	     int myDay = c.get(Calendar.DAY_OF_MONTH);
	     myMonth=myMonth+1;
	     String Random_Image_Text = str+"_" + myDay + myMonth + myYear+"_"+ randome;
	     return Random_Image_Text;
    }
    
    public static PdfPCell createImageCell(String path)
            throws DocumentException, IOException {
        Image img = Image.getInstance(path);
        PdfPCell cell = new PdfPCell(img, true);
        return cell;
    }
    
    private boolean checkDocumentAvailable() {
    	if(filetype.size() > 0) {    		
    		return true;
    	} else {
    		return false;
    	}
    }
    
    private boolean checkDocumentProcessed() {
    	if(filename.size() > 0) {  
    		boolean isAvailable = false;
    		for(String name : filename){
    			if (name.contains(SelectedProof)) {
    				isAvailable = true;
    				break;
    			}
    		}
    		if(isAvailable)
    			return true;
    		else
    			return false;
    	} else {
    		return false;
    	}
    }
    
    private void showToast(String msg) {
    	Toast.makeText(ThirdPage.this, msg, Toast.LENGTH_LONG).show();
    }
    
    class ProcessPdf extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
						 

			String path1=Utility.MY_IMG_DIR + "/"+SelectedProof;
		    String Strrr=SelectedProof+"_"+Get_Random_File_Name(Utility.PATIENT_ID) + ".pdf";
		    String output = Utility.MY_IMG_DIR + "/"+SelectedProof+"/"+Strrr ;

            /* RG - Need to find out why this is done */
		    /* SharedPreferences.Editor editor = getSharedPreferences("medspref", MODE_PRIVATE).edit();
			editor.putString(SelectedProof, SelectedProof);
			editor.commit(); */
			
			listname.add(Strrr);
			filename.add(output);
			Document document1 = new Document(PageSize.A4);
			try {
			FileOutputStream fos = new FileOutputStream(output);
			
				PdfWriter writer = PdfWriter.getInstance(document1, fos);
				writer.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(),
	                    PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
				writer.open();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			document1.open();
			 File files[] = new File(path1).listFiles();
			 PdfPTable table = new PdfPTable(1);
			    for (int ii = 0; ii < files.length; ii++) {
			        table.setWidthPercentage(95);
			        try {
			        	if(files[ii].getName().toString().contains(".jpg"))
			        	{
			        		table.addCell(createImageCell(files[ii].getAbsolutePath()));
			        		
			        	}
			        	
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			    try {
					document1.add(table);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				document1.close();
			    
			    for (int ii = 0; ii < files.length; ii++) {
			        	if(files[ii].getName().toString().contains(".jpg"))
			        	{
			        		File f=new File(files[ii].getAbsolutePath());
			        		if(f.exists())
			        			f.delete();
			        		if(f.exists())
			        			f.deleteOnExit();				        		
			        	}
			       
			    }
			
			return "completed";
		}
		
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
    	
    }

}
