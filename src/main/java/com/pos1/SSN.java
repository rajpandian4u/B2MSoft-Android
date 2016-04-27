package com.pos1;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

public class SSN implements TextWatcher {

private static final String TAG = SSN.class
        .getSimpleName();
private EditText edTxt1;
private boolean isDeletee;

public SSN(EditText edTxt1Phone1) {
    this.edTxt1 = edTxt1Phone1;
    edTxt1.setOnKeyListener(new OnKeyListener() {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                isDeletee = true;
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

		if (isDeletee) {
			isDeletee = false;
			return;
		}
		String val = s.toString();
		
		String a = "";
		String b = "";
		String c = "";
		if (val != null && val.length() > 0) {
			val = val.replace("-","");
			if (val.length() >= 3) {
				a = val.substring(0, 3);
			} else if (val.length() < 3) {
				a = val.substring(0, val.length());
			}
			if (val.length() >= 5) {
				b = val.substring(3, 5);
				c = val.substring(5, val.length());
			} else if (val.length() > 3 && val.length() < 5) {
				b = val.substring(3, val.length());
			}
			StringBuffer stringBuffer = new StringBuffer();
			if (a != null && a.length() > 0) {
				stringBuffer.append(a);
				if (a.length() == 3) {
					stringBuffer.append("-");
				}
			}
			if (b != null && b.length() > 0) {
				stringBuffer.append(b);
				if (b.length() == 2) {
					stringBuffer.append("-");
				}
			}
			if (c != null && c.length() > 0) {
				stringBuffer.append(c);
			}
			edTxt1.removeTextChangedListener(this);
			edTxt1.setText(stringBuffer.toString());
			edTxt1.setSelection(edTxt1.getText().toString().length());
			edTxt1.addTextChangedListener(this);
		} else {
			edTxt1.removeTextChangedListener(this);
			edTxt1.setText("");
			edTxt1.addTextChangedListener(this);
		}

	}
}