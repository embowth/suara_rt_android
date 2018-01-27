package rw.rt.com.mykomplek;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by embowth on 27/01/2018.
 */

public class InformasiDetailAdapter extends BaseAdapter{


    private Context mcontext;
    private List<ItemDetailInformasi> mDetailInfo;

    public InformasiDetailAdapter(Context mcontext, List<ItemDetailInformasi> mDetailInfo) {
        this.mcontext = mcontext;
        this.mDetailInfo = mDetailInfo;
    }

    @Override
    public int getCount() {
        return mDetailInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return mDetailInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v  = View.inflate(mcontext,R.layout.item_detail_info,null);
        TextView sender = (TextView)v.findViewById(R.id.txtSenderDetail);
        TextView content = (TextView)v.findViewById(R.id.txtContentDetail);

        //set text view content
        sender.setText(mDetailInfo.get(position).getSender());
        content.setText(mDetailInfo.get(position).getContent());

        //save id to tag
        v.setTag(mDetailInfo.get(position).getId());
        return v;
    }
}
