package rw.rt.com.mykomplek;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by embowth on 09/02/2018.
 */

public class PemilihanAdapter extends BaseAdapter {

    private Context mcontext;
    private List<ItemPemilihan> mList;

    public PemilihanAdapter(Context mcontext, List<ItemPemilihan> mList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mcontext, R.layout.item_list_pemilihan,null);
        TextView periode = (TextView) v.findViewById(R.id.txtPeriode);
        TextView waktu = (TextView)v.findViewById(R.id.txtWaktu);

        periode.setText(mList.get(position).getPeriode());
        waktu.setText(mList.get(position).getWaktu());

        v.setTag(mList.get(position).getId());
        return v;
    }
}
