package rw.rt.com.mykomplek;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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


public class PemilihanActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    AQuery aq;

    String idUser, idGroup;

    private ListView lvPemilihan;
    private PemilihanAdapter adapter;
    private List<ItemPemilihan> mDataPemilihan;

    TextView alertPemilihan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pemilihan);

        getSupportActionBar().setTitle("Pemilihan Ketua RT");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);


        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            idUser = data.getString(data.getColumnIndex("id_user"));
            idGroup = data.getString(data.getColumnIndex("id_group"));
        }

        getListPemilihan();

    }

    public void getListPemilihan(){
        String url = HeroHelper.BASE_URL + "get_list_sesi_pemilihan.php";

        Map<String, String> param = new HashMap<>();
        param.put("id_group", this.idGroup);
        param.put("id_user",this.idUser);

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

                            lvPemilihan = (ListView)findViewById(R.id.lvPemilihan);

                            mDataPemilihan = new ArrayList<>();

                            JSONArray jsonData = jsonObject.getJSONArray("pemilihan");
                            JSONObject objData;

                            if(jsonData.length()>0) {
                                for (int i = 0; i < jsonData.length(); i++) {
                                    objData = jsonData.getJSONObject(i);
                                    mDataPemilihan.add(new ItemPemilihan(Integer.parseInt(objData.getString("id_sesi_pemilihan")), objData.getString("periode_pemilihan"), objData.getString("tanggal")));
                                }

                                adapter = new PemilihanAdapter(getApplicationContext(), mDataPemilihan);
                                lvPemilihan.setAdapter(adapter);

                                lvPemilihan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //do something..
                                        Intent i = new Intent(PemilihanActivity.this, LayananDetailActivity.class);
                                        i.putExtra("id_layanan", view.getTag().toString());
                                        startActivity(i);
                                    }
                                });
                            }else{

                                alertPemilihan = (TextView)findViewById(R.id.txtPemilihanNotFound);
                                lvPemilihan = (ListView)findViewById(R.id.lvPemilihan);

                                lvPemilihan.setVisibility(View.GONE);
                                alertPemilihan.setVisibility(View.VISIBLE);

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
        getListPemilihan();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
