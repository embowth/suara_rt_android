package rw.rt.com.mykomplek;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by embowth on 22/01/2018.
 */

public class addInfoActivity extends AppCompatActivity {

    EditText txtJudul, txtKonten;
    Button btnSave;
    TextView errorJudul, errorKonten, txtCategory;
    String group,user;

    DatabaseHelper mDatabaseHelper;
    AQuery aq;

    String extraCategory, extraNamaCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addinfo_activity);

        mDatabaseHelper = new DatabaseHelper(this);

        getSupportActionBar().setTitle("Buat Informasi Baru");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        txtJudul = (EditText)findViewById(R.id.txtJudulThread);
        txtKonten = (EditText)findViewById(R.id.txtContentThread);
        btnSave = (Button)findViewById(R.id.btnSaveInformasi);
        errorJudul = (TextView)findViewById(R.id.txtErrorJudul);
        errorKonten = (TextView)findViewById(R.id.txtErrorKonten);
        txtCategory = (TextView)findViewById(R.id.txtKategori);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                extraCategory= "";
                extraNamaCategory = "";
            } else {
                extraCategory= extras.getString("category");
                extraNamaCategory= extras.getString("nama_category");
            }
        } else {
            extraCategory= (String) savedInstanceState.getSerializable("category");
            extraNamaCategory= (String) savedInstanceState.getSerializable("nama_category");
        }

        txtCategory.setText(extraNamaCategory);

        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            group = data.getString(data.getColumnIndex("id_group"));
            user = data.getString(data.getColumnIndex("id_user"));
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewThread();
            }
        });
    }

    private void saveNewThread(){

        String judul = txtJudul.getText().toString();
        String konten = txtKonten.getText().toString();

        if(judul.isEmpty() || judul.equals("") || judul.length() < 5){
            errorJudul.setText(R.string.emptyJudulInformasi);
        }else if(konten.isEmpty() || konten.equals("") || konten.length() < 5){
            errorKonten.setText(R.string.emptyKontenInformasi);
        }else{
            errorJudul.setText("");
            errorKonten.setText("");
            insertNewThread(judul,konten);
        }

    }

    private void insertNewThread(String pJudul, String pKonten){
        String url = HeroHelper.BASE_URL + "save_new_thread.php";

        Map<String, String> param = new HashMap<>();
        param.put("judul", pJudul);
        param.put("konten", pKonten);
        param.put("group", this.group);
        param.put("category",this.extraCategory);
        param.put("user",this.user);

        ProgressDialog pdialog = new ProgressDialog(addInfoActivity.this);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(addInfoActivity.this);

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
                        String id_thread = jsonObject.getString("id_thread");
                        if (result.equalsIgnoreCase("true")){

                            HeroHelper.pesan(getApplicationContext(), pesan);
                            Intent i = new Intent(addInfoActivity.this,DetailInfoActivity.class);
                            i.putExtra("thread_id",id_thread);
                            startActivity(i);
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
        return super.onOptionsItemSelected(item);

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
