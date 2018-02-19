package rw.rt.com.mykomplek;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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
import java.util.List;
import java.util.Map;

/**
 * Created by embowth on 09/02/2018.
 */

public class TotalSuaraActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    AQuery aq;


    String idUser, idGroup, id_sesi;

    private ListView lvTotalSuara;
    private TotalSuaraAdapter adapter;
    private List<ItemTotalSuara> mTotalSuara;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_suara);

        getSupportActionBar().setTitle("Hasil Voting");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id_sesi= "";
            } else {
                id_sesi= extras.getString("id_sesi");
            }
        } else {
            id_sesi= (String) savedInstanceState.getSerializable("id_sesi");
        }

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            idUser = data.getString(data.getColumnIndex("id_user"));
            idGroup = data.getString(data.getColumnIndex("id_group"));
        }

        getCount();
    }

    public void getCount(){
        String url = HeroHelper.BASE_URL + "get_total_suara.php";

        Map<String, String> param = new HashMap<>();
        param.put("id_group", this.idGroup);
        param.put("id_user",this.idUser);
        param.put("id_sesi", this.id_sesi);

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

                            lvTotalSuara = (ListView)findViewById(R.id.lvTotalSuara);

                            mTotalSuara = new ArrayList<>();

                            JSONArray jsonData = jsonObject.getJSONArray("pemilihan");
                            JSONObject objData;

                            if(jsonData.length()>0) {
                                for (int i = 0; i < jsonData.length(); i++) {
                                    objData = jsonData.getJSONObject(i);
                                    mTotalSuara.add(new ItemTotalSuara(Integer.parseInt(objData.getString("id_kandidat_pemilihan")), objData.getString("nama"), objData.getString("total")));
                                }

                                adapter = new TotalSuaraAdapter(TotalSuaraActivity.this, mTotalSuara);
                                lvTotalSuara.setAdapter(adapter);


                            }else{

                                //alertPemilihan = (TextView)findViewById(R.id.txtPemilihanNotFound);
                                lvTotalSuara = (ListView)findViewById(R.id.lvDetPemilihan);

                                lvTotalSuara.setVisibility(View.GONE);
                                //alertPemilihan.setVisibility(View.VISIBLE);

                            }
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
