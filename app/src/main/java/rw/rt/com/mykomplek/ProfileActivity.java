package rw.rt.com.mykomplek;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by embowth on 02/02/2018.
 */

public class ProfileActivity extends AppCompatActivity {

    TextView nama, email, tanggal_lahir, alamat, no_rt, no_rw, nomor, blok;
    DatabaseHelper mDatabaseHelper;
    AQuery aq;

    String id_user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isi_profil);

        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        nama = (TextView)findViewById(R.id.profileNama);
        email = (TextView)findViewById(R.id.profileEmail);
        tanggal_lahir = (TextView)findViewById(R.id.profileTglLahir);
        alamat = (TextView)findViewById(R.id.profileAlamat);
        no_rt = (TextView)findViewById(R.id.profileRt);
        no_rw = (TextView)findViewById(R.id.profileRw);
        nomor = (TextView)findViewById(R.id.profileNo);
        blok = (TextView)findViewById(R.id.profileBlok);

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            id_user = data.getString(data.getColumnIndex("id_user"));
        }else{
            id_user ="0";
        }

        getDataProfile(id_user);

    }

    public void getDataProfile(String idUser){
        String url = HeroHelper.BASE_URL + "get_profile.php";

        Map<String, String> param = new HashMap<>();
        param.put("id_user", idUser);

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

                            nama.setText(": "+jsonObject.getString("nama"));
                            email.setText(": "+jsonObject.getString("email"));
                            tanggal_lahir.setText(": "+jsonObject.getString("tanggal_lahir"));
                            alamat.setText(": "+jsonObject.getString("alamat"));
                            no_rt.setText(": "+jsonObject.getString("rt"));
                            no_rw.setText(": "+jsonObject.getString("rw"));
                            nomor.setText(": "+jsonObject.getString("no"));
                            blok.setText(": "+jsonObject.getString("blok"));

                        }else {
                            HeroHelper.pesan(getApplicationContext(), "Gagal mendapatkan data");
                            finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();

        if(id == R.id.menuEditProfile){
            Intent i = new Intent(ProfileActivity.this,ChangeProfileActivity.class);
            startActivity(i);
        }else if(id == R.id.menuEditPassword){
            Intent i = new Intent(ProfileActivity.this,ChangePasswordActivity.class);
            startActivity(i);
        }else{
            finish();
        }
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataProfile(id_user);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
