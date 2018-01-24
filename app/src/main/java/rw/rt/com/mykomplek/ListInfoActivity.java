package rw.rt.com.mykomplek;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by embowth on 22/01/2018.
 */

public class ListInfoActivity extends AppCompatActivity {

    String extraCategory, extraNamaCategory;

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

        System.out.println(extraCategory);
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
