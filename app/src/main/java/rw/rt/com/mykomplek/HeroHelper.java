package rw.rt.com.mykomplek;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeroHelper {
    private static final int DEBUG = 1;
    public static final String BASE_URL = "http://192.168.0.12/suara_rt/android/";  // ini IP laptop saya
    public static final String BASE_URL_IMG = "http://192.168.0.12/suara_rt/";  // ini IP laptop saya

    public static final String EMAIL_USER = "email";
    public static final String PASSWORD_USER = "password";
    public static final String ID_GROUP = "id_group";


    //ini method validasi EditText
    public static boolean isEmpty(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isCompare(EditText editText1, EditText editText2) {
        String a = editText1.getText().toString();
        String b = editText2.getText().toString();
        if (a.equals(b)) {
            return false;
        } else {
            return true;
        }
    }

    public static void pesan(Context c, String msg) {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static void pre(String pesan) {
        try {
            if (DEBUG == 1) {
                Log.e("Message",pesan);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // validasi untuk inputan email
    public static boolean isEmailValid(EditText email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email.getText().toString();

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}