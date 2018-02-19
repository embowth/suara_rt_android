package rw.rt.com.mykomplek;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * Created by embowth on 09/02/2018.
 */

public class DetailPemilihanAdapter extends BaseAdapter {

    private Context mcontext;
    private List<ItemListKandidat> mList;
    private AlertDialog dialog;

    DatabaseHelper mDatabaseHelper;
    AQuery aq;

    String idUser, idGroup;

    private String sesiPemilihan;

    public DetailPemilihanAdapter(Context mcontext, List<ItemListKandidat> mList, String sesiPemilihan) {
        this.mcontext = mcontext;
        this.mList = mList;
        this.sesiPemilihan = sesiPemilihan;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        mDatabaseHelper = new DatabaseHelper(mcontext);
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            idUser = data.getString(data.getColumnIndex("id_user"));
            idGroup = data.getString(data.getColumnIndex("id_group"));
        }

        View v = View.inflate(mcontext, R.layout.item_list_kandidat,null);
        ImageView img = (ImageView)v.findViewById(R.id.imgKandidat);
        TextView nama = (TextView)v.findViewById(R.id.txtNamaKandidat);
        TextView visi = (TextView)v.findViewById(R.id.txtVisi);
        TextView misi = (TextView)v.findViewById(R.id.txtMisi);
        Button btn = (Button)v.findViewById(R.id.btnPilih);

        nama.setText(mList.get(position).getNama());
        visi.setText(mList.get(position).getVisi());
        misi.setText(mList.get(position).getMisi());

        final EditText taskEditText = new EditText(mcontext);
        dialog = new AlertDialog.Builder(mcontext)
                .setTitle("Verifikasi")
                .setMessage("Masukkan kode verikasi yang anda terima di email anda")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        sendVote(task,String.valueOf(mList.get(position-1).getId()));
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
                dialog.show();
            }
        });

        new DownloadImageTask(img).execute(HeroHelper.BASE_URL_IMG+mList.get(position).getUrl());

        v.setTag(mList.get(position).getId());
        return v;
    }

    public void setSesiPemilihan(String sesiPemilihan) {
        this.sesiPemilihan = sesiPemilihan;
    }

    public void sendCode(){
        String url = HeroHelper.BASE_URL + "send_mail.php";

        Map<String, String> param = new HashMap<>();
        param.put("id_group", this.idGroup);
        param.put("id_user",this.idUser);

        ProgressDialog pdialog = new ProgressDialog(mcontext);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(mcontext);

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
                            HeroHelper.pesan(mcontext, pesan);
                        }else {
                            HeroHelper.pesan(mcontext, pesan);

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    HeroHelper.pesan(mcontext, "Invalid URL");
                }
            }
        });
    }

    public void sendVote(String kode, String kandidat){
        String url = HeroHelper.BASE_URL + "send_vote.php";

        Map<String, String> param = new HashMap<>();
        param.put("kode", kode);
        param.put("id_user",this.idUser);
        param.put("id_kandidat",kandidat);
        param.put("id_sesi",sesiPemilihan);

        ProgressDialog pdialog = new ProgressDialog(mcontext);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(mcontext);

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
                            HeroHelper.pesan(mcontext, pesan);
                            ((Activity)mcontext).finish();
                        }else {
                            HeroHelper.pesan(mcontext, pesan);

                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }else {
                    HeroHelper.pesan(mcontext, "Invalid URL");
                }
            }
        });
    }
}

