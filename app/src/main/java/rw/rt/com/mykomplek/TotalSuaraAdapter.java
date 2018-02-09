package rw.rt.com.mykomplek;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by embowth on 09/02/2018.
 */

public class TotalSuaraAdapter extends BaseAdapter {

    private Context mcontext;
    private List<ItemTotalSuara> mList;

    public TotalSuaraAdapter(Context mcontext, List<ItemTotalSuara> mList) {
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

        View v = View.inflate(mcontext, R.layout.item_total_suara,null);
        TextView nama = (TextView)v.findViewById(R.id.txtNamaKandidat);
        TextView total = (TextView)v.findViewById(R.id.txtTotalSuara);

        nama.setText(mList.get(position).getNama());
        total.setText(mList.get(position).getTotal_suara());

        v.setTag(mList.get(position).getId());
        return v;
    }
}
