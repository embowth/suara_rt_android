package rw.rt.com.mykomplek;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class InfoActivity extends AppCompatActivity {

    AQuery aq;

    DatabaseHelper mDatabaseHelper;
    ImageButton btn[] = new ImageButton[10];

    LinearLayout linLayout;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setTitle("Informasi Warga");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_chevron_left_white_24dp);

        linLayout = (LinearLayout) findViewById(R.id.infoLinearLayout);



        try {
            getInfo();
        }catch (Exception e){
            Log.d("error:",e.toString());
        }

    }

    private void getInfo() {
        String url = HeroHelper.BASE_URL + "get_info_category.php";

        Map<String, String> param = new HashMap<>();
        param.put("parent_category", "0");


        ProgressDialog pdialog = new ProgressDialog(InfoActivity.this);
        pdialog.setCancelable(true);
        pdialog.setMessage("Loading . . .");

        aq = new AQuery(InfoActivity.this);

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

                            JSONArray jsonData = jsonObject.getJSONArray("info_cat");
                            JSONObject objData;

                            ArrayList<Button> listButtons = new ArrayList<>();
                            int drawId;
                            Drawable icon_btn;
                            String ic;
                            for(int i=0;i < jsonData.length();i++){
                                objData = jsonData.getJSONObject(i);
                                Button button = new Button(InfoActivity.this);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.topMargin = 35;
                                params.leftMargin= 70;
                                params.rightMargin= 70;
                                button.setPadding(50,25,50,25);
                                button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                button.setText(objData.getString("nama_category"));

                                ic = objData.getString("icon");
                                System.out.println(ic);
                                if(!ic.equals("null")) {
                                    drawId = getResources().getIdentifier(objData.getString("icon"), "mipmap", getPackageName());
                                    icon_btn = getResources().getDrawable(drawId);
                                    button.setCompoundDrawablesWithIntrinsicBounds(icon_btn, null, null, null);
                                }

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(InfoActivity.this,ListInfoActivity.class));
                                    }
                                });

                                listButtons.add(button);
                                //optional: add your buttons to any layout if you want to see them in your screen
                                linLayout.addView(button,params);
                            }


                        }else {
                            HeroHelper.pesan(getApplicationContext(), pesan);
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
