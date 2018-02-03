package rw.rt.com.mykomplek;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by embowth on 02/02/2018.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    EditText passwordLama, passwordBaru, passwordBaru2;
    Button btnSave;

    AQuery aq;

    DatabaseHelper mDatabaseHelper;

    String idUser,password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_password);

        getSupportActionBar().setTitle("Ubah Password");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        passwordLama = (EditText)findViewById(R.id.txtPasswordLama);
        passwordBaru = (EditText)findViewById(R.id.txtPasswordBaru);
        passwordBaru2 = (EditText)findViewById(R.id.txtPasswordBaru2);
        btnSave = (Button)findViewById(R.id.btnPassword);

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            idUser = data.getString(data.getColumnIndex("id_user"));
            password = data.getString(data.getColumnIndex("password"));
        }else{
            idUser ="0";
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewPassword();
            }
        });

    }

    public void saveNewPassword(){
        passwordLama.setError(null);
        passwordBaru.setError(null);
        passwordBaru2.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (HeroHelper.isEmpty(passwordLama)) {
            passwordLama.setError("Password lama tidak boleh kosong");
            focusView = passwordLama;
            cancel = true;
        } else if (HeroHelper.isEmpty(passwordBaru)) {
            passwordBaru.setError("Password baru tidak boleh kosong");
            focusView = passwordBaru;
            cancel = true;
        } else if (HeroHelper.isEmpty(passwordBaru2)) {
            passwordBaru2.setError("Ulangi Password tidak boleh kosong");
            focusView = passwordBaru2;
            cancel = true;
        }else if(!passwordBaru.getText().toString().equals(passwordBaru2.getText().toString())){
            passwordBaru.setError("Password tidak sama");
            passwordBaru2.setError("Password tidak sama");
            focusView = passwordBaru;
            cancel = true;
        }else if(!passwordLama.getText().toString().equals(password)){
            passwordLama.setError("Password lama tidak cocok");
            focusView = passwordLama;
            cancel = true;
        } else {
            changePassword();
        }
    }

    private void changePassword() {
        String url = HeroHelper.BASE_URL + "change_password.php";

        Map<String, String> param = new HashMap<>();

        param.put("password_lama", passwordLama.getText().toString());
        param.put("password_baru", passwordBaru.getText().toString());
        param.put("id_user", this.idUser);

        ProgressDialog pdialog = new ProgressDialog(ChangePasswordActivity.this);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(ChangePasswordActivity.this);

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

                            mDatabaseHelper.updatePassword(passwordBaru.getText().toString());

                            HeroHelper.pesan(getApplicationContext(), pesan);
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

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
