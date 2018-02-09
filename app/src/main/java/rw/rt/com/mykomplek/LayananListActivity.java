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

/**
 * Created by embowth on 01/02/2018.
 */

public class LayananListActivity extends AppCompatActivity {

    TextView subJudul;

    DatabaseHelper mDatabaseHelper;
    AQuery aq;

    String idUser, idGroup, jenisWarga;

    private ListView lvLayanan;
    private LayananListAdapter adapter;
    private List<ItemListDataLayanan> mDataLayanan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_layanan);

        getSupportActionBar().setTitle("Layanan");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        subJudul = (TextView)findViewById(R.id.txtJudulListLayanan);
        subJudul.setText("Daftar Pengajuan");

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            idUser = data.getString(data.getColumnIndex("id_user"));
            idGroup = data.getString(data.getColumnIndex("id_group"));
            jenisWarga = data.getString(data.getColumnIndex("jenis_warga"));
        }

        getListDataLayanan();
    }

    public void getListDataLayanan(){
        String url = HeroHelper.BASE_URL + "get_list_data_layanan.php";

        Map<String, String> param = new HashMap<>();
        param.put("id_group", this.idGroup);
        param.put("id_user",this.idUser);
        param.put("jenis_warga",this.jenisWarga);

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

                            lvLayanan = (ListView)findViewById(R.id.listViewLayanan);

                            mDataLayanan = new ArrayList<>();

                            JSONArray jsonData = jsonObject.getJSONArray("layanan");
                            JSONObject objData;

                            for(int i=0;i < jsonData.length();i++){
                                objData = jsonData.getJSONObject(i);
                                mDataLayanan.add(new ItemListDataLayanan(Integer.parseInt(objData.getString("id_layanan")),objData.getString("nama"),objData.getString("nomor_ktp"),objData.getString("tanggal"),objData.getString("status_layanan")));
                            }

                            adapter = new LayananListAdapter(getApplicationContext(),mDataLayanan);
                            lvLayanan.setAdapter(adapter);

                            lvLayanan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //do something..
                                    Intent i = new Intent(LayananListActivity.this,LayananDetailActivity.class);
                                    i.putExtra("id_layanan",view.getTag().toString());
                                    startActivity(i);
                                }
                            });

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
        int id = item.getItemId();

        if(id == R.id.action_add_layanan){
            Intent i = new Intent(LayananListActivity.this,LayananActivity.class);
            startActivity(i);
        }else{
            finish();
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if(jenisWarga.equals("0")) {
            getMenuInflater().inflate(R.menu.request_layanan_menu, menu);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListDataLayanan();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
