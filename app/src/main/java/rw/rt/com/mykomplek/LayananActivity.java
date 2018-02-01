package rw.rt.com.mykomplek;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

public class LayananActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    String userId,groupId;
    EditText namaLengkap,nomor_ktp,tempat_lahir,tanggal_lahir,det_keperluan;

    Button btnSave;

    Spinner tipeLayanan,jenisKelamin,statusPernikahan;

    AQuery aq;

    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_layanan);

        getSupportActionBar().setTitle("Layanan RT");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        namaLengkap = (EditText)findViewById(R.id.txtNamaLengkap);
        nomor_ktp = (EditText)findViewById(R.id.txtNik);
        tempat_lahir = (EditText)findViewById(R.id.txtTempatLahir);
        tanggal_lahir = (EditText)findViewById(R.id.txtTanggalLahir);
        det_keperluan = (EditText)findViewById(R.id.txtDetKeperluan);
        btnSave = (Button)findViewById(R.id.btnSaveLayanan);
        myCalendar = Calendar.getInstance();

        tipeLayanan = (Spinner)findViewById(R.id.spinnerKeperluan);
        String[] items = new String[]{"Surat Pengantar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        tipeLayanan.setAdapter(adapter);

        jenisKelamin = (Spinner)findViewById(R.id.spinnerJenisKelamin);
        String[] itemJK = new String[]{"Laki-laki","Perempuan"};
        ArrayAdapter<String> adapterJK = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, itemJK);
        jenisKelamin.setAdapter(adapterJK);

        statusPernikahan = (Spinner)findViewById(R.id.spinnerStatus);
        String[] itemSt = new String[]{"Belum Menikah","Menikah"};
        ArrayAdapter<String> adapterSt = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, itemSt);
        statusPernikahan.setAdapter(adapterSt);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLayanan();
            }
        });

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

        tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(LayananActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            userId = data.getString(data.getColumnIndex("id_user"));
            groupId = data.getString(data.getColumnIndex("id_group"));
        }
    }

    public void saveLayanan(){
        namaLengkap.setError(null);
        nomor_ktp.setError(null);
        tempat_lahir.setError(null);
        tanggal_lahir.setError(null);
        det_keperluan.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (HeroHelper.isEmpty(namaLengkap)) {
            namaLengkap.setError("Nama tidak boleh kosong");
            focusView = namaLengkap;
            cancel = true;
        } else if (HeroHelper.isEmpty(nomor_ktp)) {
            nomor_ktp.setError("Nomor KTP tidak boleh kosong");
            focusView = nomor_ktp;
            cancel = true;
        } else if (HeroHelper.isEmpty(tempat_lahir)) {
            tempat_lahir.setError("Tempat lahir tidak boleh kosong");
            focusView = tempat_lahir;
            cancel = true;
        } else if (HeroHelper.isEmpty(tanggal_lahir)) {
            tempat_lahir.setError("Tanggal lahir tidak boleh kosong");
            focusView = tanggal_lahir;
            cancel = true;
        } else if (HeroHelper.isEmpty(det_keperluan)) {
            tempat_lahir.setError("Detail keperluan tidak boleh kosong");
            focusView = det_keperluan;
            cancel = true;
        } else {
            insertLayanan();
        }


    }

    private void insertLayanan(){
        String url = HeroHelper.BASE_URL + "save_new_layanan.php";

        Map<String, String> param = new HashMap<>();
        param.put("id_user", this.userId);
        param.put("id_group", this.groupId);
        param.put("nama", namaLengkap.getText().toString());
        param.put("no_ktp", nomor_ktp.getText().toString());
        param.put("tempat_lahir",tempat_lahir.getText().toString());
        param.put("tanggal_lahir",tanggal_lahir.getText().toString());
        param.put("jenis_kelamin",jenisKelamin.getSelectedItem().toString());
        param.put("status_pernikahan", statusPernikahan.getSelectedItem().toString());
        param.put("keperluan",tipeLayanan.getSelectedItem().toString());
        param.put("det_keperluan",det_keperluan.getText().toString());


        ProgressDialog pdialog = new ProgressDialog(LayananActivity.this);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(LayananActivity.this);

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

        tanggal_lahir.setText(sdf.format(myCalendar.getTime()));
    }

    public boolean onOptionsItemSelected(MenuItem item){

        finish();
        return true;

    }

}
