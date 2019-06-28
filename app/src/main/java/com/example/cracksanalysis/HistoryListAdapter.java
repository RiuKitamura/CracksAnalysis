package com.example.cracksanalysis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model> historyList;


    public HistoryListAdapter(Context context, int layout, ArrayList<Model> historyList) {
        this.context = context;
        this.layout = layout;
        this.historyList = historyList;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        //        CircleImageView imageView;
        TextView textName, textAlamat;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.textName = row.findViewById(R.id.nama_bg_txt);
            holder.textAlamat = row.findViewById(R.id.alamat_bg_txt);
//            holder.imageView = row.findViewById(R.id.poto_bangunan);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        Model model = historyList.get(position);

        holder.textName.setText(model.getNama_b());
        holder.textAlamat.setText(model.getAlamat_b());

//        byte[] recordImage = model.getPoto();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage,0,recordImage.length);
//        holder.imageView.setImageBitmap(bitmap);

        return row;
    }

    customButtonListener customListner;

    public interface customButtonListener {
        public void onButtonClickListner(int position,String value);
    }

}
