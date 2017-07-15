package org.arnie97.uic.checksum;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
    private EditText et;

    public static int checksum(int[] uic) {
        int sum = 0;
        for (int i = 0; i < 11; i++) {
            int x = i % 2 == 1 ? 1 : 2;
            x *= uic[i];
            x = (x / 10) + (x % 10);
            sum += x;
        }
        return (10 - sum % 10) % 10;
    }

    public static int[] listToPrimitive(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static int[] parseIntArray(String str) {
        List<Integer> list = new ArrayList<>();
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                continue;
            }
            list.add(Character.digit(c, 10));
        }
        return listToPrimitive(list);
    }

    private String uicFormat(String src) {
        int[] digits = parseIntArray(src);
        String dest = new String();

        // format the first 11 digits
        int len = digits.length;
        if (len == 12) {
            len = 11;
        }
        for (int i = 0; i < len; i++) {
            dest += digits[i];
            if (i == 1 || i == 3 || i == 10) {
                dest += '-';
            }
        }

        if (len != 11) {
            alert("Invalid vehicle number.\n11 or 12 digits expected.");
        } else {
            int c = checksum(digits);
            if (digits.length == 11) {
                ;
            } else if (digits[11] == c) {
                alert("Correct checksum.");
            } else {
                alert(String.format("Wrong checksum %d!\nReplaced by %d.", digits[11], c));
            }
            dest += c;
        }
        return dest;
    }

    public void alert(String msg, String title) {
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setTitle(title)
                .show();
    }

    public void alert(String msg) {
        String appName = getResources().getString(R.string.app_name);
        alert(msg, appName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.editText);

        et.setOnKeyListener(new EditText.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
                        keyCode == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    et.setText(uicFormat(et.getText().toString()));
                    return true;
                }
                return false;
            }
        });
    }
}
