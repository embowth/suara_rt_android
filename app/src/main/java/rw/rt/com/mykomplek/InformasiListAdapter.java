package rw.rt.com.mykomplek;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by embowth on 25/01/2018.
 */

public class InformasiListAdapter extends BaseAdapter{

    private Context mcontext;
    private List<ItemListInformasi> mInformasiList;

    //constructor


    public InformasiListAdapter(Context mcontext, List<ItemListInformasi> mInformasiList) {
        this.mcontext = mcontext;
        this.mInformasiList = mInformasiList;
    }

    @Override
    public int getCount() {
        return mInformasiList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInformasiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v  = View.inflate(mcontext,R.layout.item_list_info,null);
        TextView judul = (TextView)v.findViewById(R.id.txtJudulItem);
        TextView sender = (TextView)v.findViewById(R.id.txtSenderItem);
        TextView post = (TextView)v.findViewById(R.id.txtTanggalItem);

        //set text view content
        judul.setText(mInformasiList.get(position).getJudul());
        sender.setText(mInformasiList.get(position).getSender());
        post.setText(mInformasiList.get(position).getPost());

        //save id to tag
        v.setTag(mInformasiList.get(position).getId());
        return v;
    }
}
