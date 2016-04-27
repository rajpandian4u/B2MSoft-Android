package com.pos1;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class ZipCode implements TextWatcher {

private static final String TAG = ZipCode.class
        .getSimpleName();
private EditText edTxt2;
private boolean isDeleteee;

public ZipCode(EditText edTxt2Phone2) {
    this.edTxt2 = edTxt2Phone2;
    edTxt2.setOnKeyListener(new OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                isDeleteee = true;
            }
            return false;
        }
    });
}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public void afterTextChanged(Editable s) {

		if (isDeleteee) {
			isDeleteee = false;
			return;
		}
		String val = s.toString();
		
		String a = "";
		String b = "";
		String c = "";
		if (val != null && val.length() > 0) {
			val = val.replace("-","");
			if (val.length() >= 5) {
				a = val.substring(0, 5);
			} else if (val.length() < 5) {
				a = val.substring(0, val.length());
			}
			if (val.length() >= 10) {
				b = val.substring(5, 10);
				c = val.substring(10, val.length());
			} else if (val.length() > 5 && val.length() < 10) {
				b = val.substring(5, val.length());
			}
			StringBuffer stringBuffer = new StringBuffer();
			if (a != null && a.length() > 0) {
				stringBuffer.append(a);
				if (a.length() == 5) {
					stringBuffer.append("-");
				}
			}
			if (b != null && b.length() > 0) {
				stringBuffer.append(b);
				if (b.length() == 5) {
					stringBuffer.append("-");
				}
			}
			if (c != null && c.length() > 0) {
				stringBuffer.append(c);
			}
			edTxt2.removeTextChangedListener(this);
			edTxt2.setText(stringBuffer.toString());
			edTxt2.setSelection(edTxt2.getText().toString().length());
			edTxt2.addTextChangedListener(this);
		} else {
			edTxt2.removeTextChangedListener(this);
			edTxt2.setText("");
			edTxt2.addTextChangedListener(this);
		}

	}
}