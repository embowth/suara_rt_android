package rw.rt.com.mykomplek;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by embowth on 29/01/2018.
 */

public class ReplyInfoActivity extends AppCompatActivity {

    EditText txtComment;
    Button btnSave;
    TextView tJudulComment,errorComment;

    DatabaseHelper mDatabaseHelper;
    AQuery aq;

    String id_thread,judul,user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_info_activity);

        mDatabaseHelper = new DatabaseHelper(this);

        getSupportActionBar().setTitle("Tulis Komentar");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        txtComment = (EditText)findViewById(R.id.txtComment);
        btnSave = (Button)findViewById(R.id.btnSaveComment);
        tJudulComment = (TextView)findViewById(R.id.txtJudulComment);
        errorComment = (TextView)findViewById(R.id.txtErrorComment);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id_thread= "";
                judul="";
            } else {
                id_thread= extras.getString("id_thread");
                judul= extras.getString("judul");
            }
        } else {
            id_thread = (String) savedInstanceState.getSerializable("id_thread");
            judul = (String) savedInstanceState.getSerializable("judul");
        }

        tJudulComment.setText(judul);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveComment();
            }
        });

    }

    private void saveComment(){

       String comm = txtComment.getText().toString();


        if(comm.isEmpty() || comm.equals("") || comm.length() < 5){
            errorComment.setText(R.string.emptyKontenKomentar);
        }else{

            Cursor data = mDatabaseHelper.getData();
            data.moveToFirst();

            if (data.getCount() > 0) {
                user = data.getString(data.getColumnIndex("id_user"));
            }
            insertComment(comm,user);
        }

    }

    private void insertComment(String comment, String user){
        String url = HeroHelper.BASE_URL + "save_thread_comment.php";

        Map<String, String> param = new HashMap<>();
        param.put("comment", comment);
        param.put("user", user);
        param.put("thread", id_thread);


        ProgressDialog pdialog = new ProgressDialog(ReplyInfoActivity.this);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(ReplyInfoActivity.this);

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
