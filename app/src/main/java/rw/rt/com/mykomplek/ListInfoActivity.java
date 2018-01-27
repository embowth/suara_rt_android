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
import android.widget.Toast;

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
 * Created by embowth on 22/01/2018.
 */

public class ListInfoActivity extends AppCompatActivity {

    String extraCategory, extraNamaCategory;
    TextView kategori;

    private ListView lvInformasi;
    private InformasiListAdapter adapter;
    private List<ItemListInformasi> mInformasiList;

    DatabaseHelper mDatabaseHelper;
    AQuery aq;

    String pKategori,pGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listinfo);

        getSupportActionBar().setTitle("Informasi Warga");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

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

        kategori = (TextView)findViewById(R.id.txtKategori);
        kategori.setText(extraNamaCategory);

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            pGroup = data.getString(data.getColumnIndex("id_group"));
        }

        getListInformasi();

    }

    public void getListInformasi(){
        String url = HeroHelper.BASE_URL + "get_list_thread.php";

        Map<String, String> param = new HashMap<>();
        param.put("group", this.pGroup);
        param.put("category",this.extraCategory);

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

                            lvInformasi = (ListView)findViewById(R.id.listViewInfo);

                            mInformasiList = new ArrayList<>();

                            JSONArray jsonData = jsonObject.getJSONArray("listinfo");
                            JSONObject objData;

                            for(int i=0;i < jsonData.length();i++){
                                objData = jsonData.getJSONObject(i);
                                mInformasiList.add(new ItemListInformasi(Integer.parseInt(objData.getString("id_thread")),objData.getString("judul"),objData.getString("sender"),objData.getString("tanggal_post")));
                            }

                            adapter = new InformasiListAdapter(getApplicationContext(),mInformasiList);
                            lvInformasi.setAdapter(adapter);

                            lvInformasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //do something..
                                    Toast.makeText(getApplicationContext(),"id:"+view.getTag(),Toast.LENGTH_LONG).show();
                                }
                            });

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
        int id = item.getItemId();

        if(id == R.id.action_add_thread){
            Intent i = new Intent(ListInfoActivity.this,addInfoActivity.class);
            i.putExtra("category", extraCategory);
            i.putExtra("nama_category", extraNamaCategory);
            startActivity(i);
        }else{
            finish();
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_thread_menu, menu);
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
