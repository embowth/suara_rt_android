package rw.rt.com.mykomplek;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    AnimationDrawable animateDraw;
    ConstraintLayout constraintLayout;

    EditText email, password;
    Button login;
    AQuery aq;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        getSupportActionBar().hide();

        mDatabaseHelper = new DatabaseHelper(this);

        constraintLayout = (ConstraintLayout)findViewById(R.id.loginLayout);
        animateDraw = (AnimationDrawable)constraintLayout.getBackground();

        animateDraw.setEnterFadeDuration(2000);
        animateDraw.setExitFadeDuration(2000);

        email = (EditText)findViewById(R.id.LoginEmail);
        password = (EditText)findViewById(R.id.LoginPasswd);
        login = (Button)findViewById(R.id.LoginBtn);

        //action login ketika button login di klik
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CHECK();
            }
        });

        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();
        //Log.d("cursor", data.getString(3));
        if(data.getCount() > 0){
            //Log.d("db",data.getString(2));
            //Log.d("db2",data.getString(8));
            LOGIN(data.getString(2),data.getString(8),false);
        }


    }

    private void CHECK() {
        email.setError(null);
        password.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (HeroHelper.isEmpty(email)) {
            email.setError("Email tidak boleh kosong");
            focusView = email;
            cancel = true;
        } else if (!HeroHelper.isEmailValid(email)) {
            email.setError("Email tidak valid");
            focusView = email;
            cancel = true;
        } else if (HeroHelper.isEmpty(password)) {
            password.setError("Password tidak boleh kosong");
            focusView = password;
            cancel = true;
        } else {
            LOGIN(email.getText().toString(),password.getText().toString(), true);
        }
    }

    private void LOGIN(String pEmail, final String pPassword, final Boolean newLog) {
        String url = HeroHelper.BASE_URL + "login_android.php";

        Map<String, String> param = new HashMap<>();

        param.put(HeroHelper.EMAIL_USER, pEmail);
        param.put(HeroHelper.PASSWORD_USER, pPassword);

        ProgressDialog pdialog = new ProgressDialog(MainActivity.this);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(MainActivity.this);

        aq.progress(pdialog).ajax(url, param, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                super.callback(url, object, status);
                if (object != null){
                    HeroHelper.pre("Respon API : " + object);
                    try {
                        //deklarasi JSON
                        JSONObject jsonObject = new JSONObject(object);
                        //memanggil JSON Object
                        String result = jsonObject.getString("success");
                        String pesan = jsonObject.getString("message");
                        if (result.equalsIgnoreCase("true")){
                            if(newLog){
                                JSONArray jsonUser = jsonObject.getJSONArray("user");
                                JSONObject objUser = jsonUser.getJSONObject(0);
                                //Log.d("json", jObj.getString("email_user"));
                                String[] userData = new String[11];
                                userData[0] = new String("1");
                                userData[1] = new String(objUser.getString("id_user"));
                                userData[2] = new String(objUser.getString("email_user"));
                                userData[3] = new String(objUser.getString("role_user"));
                                userData[4] = new String(objUser.getString("id_warga"));
                                userData[5] = new String(objUser.getString("nama_user"));
                                userData[6] = new String(objUser.getString("id_group"));
                                userData[7] = new String(objUser.getString("jenis_warga"));
                                userData[8] = new String(pPassword);
                                userData[9] = new String(objUser.getString("tanggal_lahir"));
                                userData[10] = new String(objUser.getString("jenis_kelamin"));

                                CleanData();
                                AddData(userData);
                            }

                            HeroHelper.pesan(getApplicationContext(), pesan);
                            startActivity(new Intent(MainActivity.this, MenuActivity.class));
                            finish();
                        }else {
                            HeroHelper.pesan(getApplicationContext(), pesan);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    HeroHelper.pesan(getApplicationContext(), "Invalid URL");
                }
            }
        });
    }

    public void AddData(String[] newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);
    }

    public void CleanData(){
        mDatabaseHelper.deleteData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animateDraw != null && !animateDraw.isRunning())
            animateDraw.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animateDraw != null && animateDraw.isRunning())
            animateDraw.stop();
    }
}
