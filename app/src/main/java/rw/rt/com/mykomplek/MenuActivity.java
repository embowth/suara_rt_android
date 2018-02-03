package rw.rt.com.mykomplek;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ImageButton btnPemilihan, btnLayanan, btnInfo, btnDarurat;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    RelativeLayout mRelativeLayout;
    TextView txtNama;

    DatabaseHelper mDatabaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mDatabaseHelper = new DatabaseHelper(this);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView mNavigationView = (NavigationView)findViewById(R.id.navigationView);
        mNavigationView.setNavigationItemSelectedListener(this);
        View header=mNavigationView.getHeaderView(0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnPemilihan = (ImageButton)findViewById(R.id.BtnPemilihan);
        btnLayanan = (ImageButton)findViewById(R.id.BtnLayanan);
        btnInfo = (ImageButton)findViewById(R.id.BtnInfo);
        btnDarurat = (ImageButton)findViewById(R.id.BtnDarurat);

        txtNama = (TextView)header.findViewById(R.id.headerNavName);
        navUsername();

        btnPemilihan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, PemilihanActivity.class));
            }
        });

        btnLayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, LayananListActivity.class));
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, InfoActivity.class));
            }
        });

        btnDarurat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, DaruratActivity.class));
            }
        });

    }

    public void navUsername() {
        Cursor data = mDatabaseHelper.getData();
        data.moveToFirst();

        if (data.getCount() > 0) {
            txtNama.setText(data.getString(data.getColumnIndex("nama")));
        }
    }

    public void userLogout(){
        mDatabaseHelper.deleteData();
        startActivity(new Intent(MenuActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.navLogout){
            userLogout();
        }else if(id == R.id.navAccount){
            startActivity(new Intent(MenuActivity.this,ProfileActivity.class));
        }

        return false;
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
