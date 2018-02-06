package rw.rt.com.mykomplek;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by embowth on 02/02/2018.
 */

public class ChangeProfileActivity extends AppCompatActivity {

    EditText txtNama, txtTanggalLahir;
    RadioButton rbLaki, rbPerempuan;
    Button btnSave;

    Calendar myCalendar;

    AQuery aq;

    DatabaseHelper mDatabaseHelper;

    RadioGroup rGroup;

    String jenisKelamin,id_user, nama, tanggalLahir;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profil);

        getSupportActionBar().setTitle("Ubah Profil");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        txtNama = (EditText)findViewById(R.id.editProfileNama);
        txtTanggalLahir = (EditText)findViewById(R.id.editProfileTanggalLahir);
        btnSave = (Button)findViewById(R.id.editProfileBtnSave);
        rbLaki = (RadioButton)findViewById(R.id.rbLaki);
        rbPerempuan = (RadioButton)findViewById(R.id.rbPerempuan);
        rGroup = (RadioGroup)findViewById(R.id.rGroup);
        myCalendar = Calendar.getInstance();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            id_user = data.getString(data.getColumnIndex("id_user"));
            nama = data.getString(data.getColumnIndex("nama"));
            tanggalLahir = data.getString(data.getColumnIndex("tanggal_lahir"));
            jenisKelamin = data.getString(data.getColumnIndex("jenis_kelamin"));

            txtNama.setText(nama);
            txtTanggalLahir.setText(tanggalLahir);
            if(jenisKelamin.equals("1")){
                rbLaki.setChecked(true);
            }else{
                rbPerempuan.setChecked(true);
            }
        }else{
            Toast.makeText(this,"Terjadi kesalahan saat membuka halaman",Toast.LENGTH_LONG).show();
            finish();
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        txtTanggalLahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ChangeProfileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    public void saveProfile(){
        txtNama.setError(null);
        txtTanggalLahir.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (HeroHelper.isEmpty(txtNama)) {
            txtNama.setError("Password lama tidak boleh kosong");
            focusView = txtNama;
            cancel = true;
        } else if (HeroHelper.isEmpty(txtTanggalLahir)) {
            txtTanggalLahir.setError("Password baru tidak boleh kosong");
            focusView = txtTanggalLahir;
            cancel = true;
        }else{
            int selectedId = rGroup.getCheckedRadioButtonId();

            if(selectedId==R.id.rbLaki){
                jenisKelamin = "1";
            }else{
                jenisKelamin = "0";
            }

            changeProfile();
        }
    }

    private void changeProfile() {
        String url = HeroHelper.BASE_URL + "change_profil.php";

        Map<String, String> param = new HashMap<>();

        param.put("nama_lengkap", txtNama.getText().toString());
        param.put("tanggal_lahir", txtTanggalLahir.getText().toString());
        param.put("jenis_kelamin", jenisKelamin);
        param.put("id_user", id_user);

        ProgressDialog pdialog = new ProgressDialog(ChangeProfileActivity.this);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(ChangeProfileActivity.this);

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

                            mDatabaseHelper.updateProfil(txtNama.getText().toString(),txtTanggalLahir.getText().toString(),jenisKelamin);

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

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtTanggalLahir.setText(sdf.format(myCalendar.getTime()));
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


