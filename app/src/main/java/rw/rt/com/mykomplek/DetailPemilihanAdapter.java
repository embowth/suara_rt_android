package rw.rt.com.mykomplek;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by embowth on 09/02/2018.
 */

public class DetailPemilihanAdapter extends BaseAdapter {

    private Context mcontext;
    private List<ItemListKandidat> mList;
    private AlertDialog dialog;

    public DetailPemilihanAdapter(Context mcontext, List<ItemListKandidat> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
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
                .setMessage("Masukkan kode verikasi yang anda terima")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        new DownloadImageTask(img).execute(HeroHelper.BASE_URL_IMG+mList.get(position).getUrl());

        v.setTag(mList.get(position).getId());
        return v;
    }
}

