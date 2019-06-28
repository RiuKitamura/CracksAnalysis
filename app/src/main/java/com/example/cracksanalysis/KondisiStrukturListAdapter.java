package com.example.cracksanalysis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class KondisiStrukturListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Model2> kolomList;

    public KondisiStrukturListAdapter(Context context, int layout, ArrayList<Model2> kolomList) {
        this.context = context;
        this.layout = layout;
        this.kolomList = kolomList;
    }
    @Override
    public int getCount() {
        return kolomList.size();
    }

    @Override
    public Object getItem(int position) {
        return kolomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView strukturTxt;
        TextView textKondisi;
        ImageButton addPoto;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        KondisiStrukturListAdapter.ViewHolder holder = new KondisiStrukturListAdapter.ViewHolder();

        if (row == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.textKondisi = row.findViewById(R.id.keterangan_level);
            holder.strukturTxt = row.findViewById(R.id.list_txt);
            holder.addPoto = row.findViewById(R.id.add_foto);

            holder.addPoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model2 model = kolomList.get(position);
                    Toast.makeText(context, "camera "+model.getId(), Toast.LENGTH_SHORT).show();

                }
            });

//            holder.imageView = row.findViewById(R.id.poto_bangunan);
            row.setTag(holder);
        }
        else {
            holder = (KondisiStrukturListAdapter.ViewHolder)row.getTag();
        }

        Model2 model = kolomList.get(position);
        int a =position+1;
        holder.strukturTxt.setText(a+"");
        System.out.println("ini dia aaaa "+model.getLevel());
        if(model.getLevel()==0){
            holder.textKondisi.setText("Tidak rusak");
        }
        if(model.getLevel()==1){
            holder.textKondisi.setText("Rusak Ringan");
        }
        if(model.getLevel()==2){
            holder.textKondisi.setText("Rusak Sedang");
        }
        if(model.getLevel()==3){
            holder.textKondisi.setText("Rusak Berat");
        }
        holder.addPoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model2 model = kolomList.get(position);
                Intent i = new Intent(context.getApplicationContext(), AmbilGambarRetakan.class);
                i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                Bundle bun = new Bundle();
                bun.putInt("id", model.getId());
                bun.putInt("stuk",model.getStuktur());
                i.putExtras(bun);
                context.getApplicationContext().startActivity(i);

            }
        });

//        byte[] recordImage = model.getPoto();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage,0,recordImage.length);
//        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
