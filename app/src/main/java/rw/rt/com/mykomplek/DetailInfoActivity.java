package rw.rt.com.mykomplek;

import android.app.ProgressDialog;
import android.content.Intent;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by embowth on 27/01/2018.
 */

public class DetailInfoActivity extends AppCompatActivity {

    private ListView lvDetail;
    private InformasiDetailAdapter adapter;
    private List<ItemDetailInformasi> mDetailItem;

    String id_thread,page;

    AQuery aq;

    TextView txtJudul;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

        getSupportActionBar().setTitle("Informasi Warga");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        txtJudul = (TextView)findViewById(R.id.txtJudulInfo);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id_thread= "";
                page = "";
            } else {
                id_thread= extras.getString("thread_id");
                page = extras.getString("page");
            }
        } else {
            id_thread = (String) savedInstanceState.getSerializable("thread_id");
            page = (String) savedInstanceState.getSerializable("page");
        }

        getListDetail();
    }

    public void getListDetail(){
        String url = HeroHelper.BASE_URL + "get_detail_thread.php";

        Map<String, String> param = new HashMap<>();
        param.put("id_thread", this.id_thread);

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
                        String judul = jsonObject.getString("judul");
                        if (result.equalsIgnoreCase("true")){

                            txtJudul.setText(judul);

                            lvDetail = (ListView)findViewById(R.id.listViewDetail);

                            mDetailItem = new ArrayList<>();

                            JSONArray jsonData = jsonObject.getJSONArray("details");
                            JSONObject objData;

                            for(int i=0;i < jsonData.length();i++){
                                objData = jsonData.getJSONObject(i);
                                mDetailItem.add(new ItemDetailInformasi(Integer.parseInt(objData.getString("id_content")),"#"+ (i+1) + " " +objData.getString("sender")+ " / " + objData.getString("tanggal_post"),objData.getString("content")));
                            }

                            adapter = new InformasiDetailAdapter(getApplicationContext(),mDetailItem);
                            lvDetail.setAdapter(adapter);

                            lvDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //do something..

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

        if(id == R.id.action_reply_thread){
            Intent i = new Intent(DetailInfoActivity.this,ReplyInfoActivity.class);
            i.putExtra("id_thread", id_thread);
            i.putExtra("judul", txtJudul.getText().toString());
            startActivity(i);
        }else{
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!page.equalsIgnoreCase("archive")){
            getMenuInflater().inflate(R.menu.reply_thread_menu, menu);
        }

        return true;
    }

    @Override
    protected void onResume() {

        getListDetail();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
