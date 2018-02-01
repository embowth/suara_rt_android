package rw.rt.com.mykomplek;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by embowth on 01/02/2018.
 */

public class LayananListAdapter extends BaseAdapter {

    private Context mcontext;
    private List<ItemListDataLayanan> mLayananList;

    public LayananListAdapter(Context mcontext, List<ItemListDataLayanan> mLayananList) {
        this.mcontext = mcontext;
        this.mLayananList = mLayananList;
    }

    @Override
    public int getCount() {
        return mLayananList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLayananList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mcontext, R.layout.item_list_layanan,null);
        TextView namaLengkap = (TextView)v.findViewById(R.id.txtLayananNamaLengkap);
        TextView nomorKtp = (TextView)v.findViewById(R.id.txtLayananNoKtp);
        TextView tanggal = (TextView)v.findViewById(R.id.txtLayananTanggal);
        TextView status = (TextView)v.findViewById(R.id.txtLayananStatus);

        namaLengkap.setText(mLayananList.get(position).getNama_lengkap());
        nomorKtp.setText(mLayananList.get(position).getNomor_ktp());
        tanggal.setText(mLayananList.get(position).getTanggal());
        status.setText(mLayananList.get(position).getStatus());

        v.setTag(mLayananList.get(position).getId());
        return v;
    }
}
