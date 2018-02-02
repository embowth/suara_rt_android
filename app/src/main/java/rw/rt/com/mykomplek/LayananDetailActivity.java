package rw.rt.com.mykomplek;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by embowth on 01/02/2018.
 */

public class LayananDetailActivity extends AppCompatActivity {

    TextView namaLengkap, nomorKtp, tempatLahir, tanggalLahir, jenisKelamin,
            statusPernikahan, keperluan, detKeperluan, statusLayanan;
    Button btnSave;
    String id_layanan;
    AQuery aq;

    DatabaseHelper mDatabaseHelper;
    String jenis_warga;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layanan);

        getSupportActionBar().setTitle("Rincian Pengajuan");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        namaLengkap = (TextView)findViewById(R.id.tvNamaLengkap);
        nomorKtp = (TextView)findViewById(R.id.tvNik);
        tempatLahir = (TextView)findViewById(R.id.tvTempatLahir);
        tanggalLahir = (TextView)findViewById(R.id.tvTanggalLahir);
        jenisKelamin = (TextView)findViewById(R.id.tvJenisKelamin);
        statusPernikahan = (TextView)findViewById(R.id.tvStatus);
        keperluan = (TextView)findViewById(R.id.tvKeperluan);
        detKeperluan = (TextView)findViewById(R.id.tvDetKeperluan);
        statusLayanan = (TextView)findViewById(R.id.tvStatusLayanan);
        btnSave = (Button)findViewById(R.id.btnChangeStatus);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id_layanan= "";
            } else {
                id_layanan= extras.getString("id_layanan");
            }
        } else {
            id_layanan = (String) savedInstanceState.getSerializable("id_layanan");
        }

        getListDetail(id_layanan);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusLayanan(id_layanan);
            }
        });

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            jenis_warga = data.getString(data.getColumnIndex("jenis_warga"));
        }

        if(jenis_warga.equals("1")){
            btnSave.setVisibility(View.VISIBLE);
        }else{
            btnSave.setVisibility(View.INVISIBLE);
        }
    }

    public void getListDetail(String idLayanan){
        String url = HeroHelper.BASE_URL + "get_data_layanan.php";

        Map<String, String> param = new HashMap<>();
        param.put("id_layanan", idLayanan);

        ProgressDialog pdialog = new ProgressDialog(getApplicationContext());
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(getApplicationContext());

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

                            namaLengkap.setText(jsonObject.getString("nama_lengkap"));
                            nomorKtp.setText(jsonObject.getString("nomor_ktp"));
                            tempatLahir.setText(jsonObject.getString("tempat_lahir"));
                            tanggalLahir.setText(jsonObject.getString("tanggal_lahir"));
                            jenisKelamin.setText(jsonObject.getString("jenis_kelamin"));
                            statusPernikahan.setText(jsonObject.getString("status_pernikahan"));
                            keperluan.setText(jsonObject.getString("keperluan"));
                            detKeperluan.setText(jsonObject.getString("detail_keperluan"));
                            statusLayanan.setText(jsonObject.getString("status_layanan"));

                            if(jsonObject.getString("status_layanan_num").equals("2")){
                                btnSave.setVisibility(View.INVISIBLE);
                            }

                        }else {
                            HeroHelper.pesan(getApplicationContext(), "Tidak ditemukan informasi pada kategori ini");

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

    public void changeStatusLayanan(String idLayanan){
        String url = HeroHelper.BASE_URL + "change_status_layanan.php";

        Map<String, String> param = new HashMap<>();
        param.put("id_layanan", idLayanan);

        ProgressDialog pdialog = new ProgressDialog(getApplicationContext());
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(getApplicationContext());

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
                            finish();
                        }else {
                            HeroHelper.pesan(getApplicationContext(), "Tidak ditemukan informasi pada kategori ini");
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
