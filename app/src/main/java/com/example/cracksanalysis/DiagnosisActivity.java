package com.example.cracksanalysis;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiagnosisActivity extends AppCompatActivity {

    ImageView imageViewIcon;
    RelativeLayout kolom, balok, dinding;

    ListView mListView;
    KondisiStrukturListAdapter mAdapter = null;
    int kod;
    int kode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        kolom = findViewById(R.id.kolom_btn);
        balok = findViewById(R.id.balok_btn);
        dinding = findViewById(R.id.dinding_btn);


        getSupportActionBar().setTitle("Kondisi Bangunan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kode = getIntent().getExtras().getInt("id");
        refreshText(kode);
//        int a=0;
//        kod=kode;
//        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT id FROM data_kerusakan WHERE id_bangunan="+kode+" ORDER BY id DESC");
//        while (cursor.moveToNext()){
//            int id = cursor.getInt(0);
//            a++;
//        }
//        TextView jml_kolom = findViewById(R.id.jml_kolom);
//        jml_kolom.setText(a+"");
        TextView namaBg = findViewById(R.id.nama_bangunan_txt);
        imageViewIcon = findViewById(R.id.poto_bangunan2);

        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT nama_bangunan, poto FROM data_bangunan WHERE id="+kode);
        while (cursor.moveToNext()){
            String nama_b = cursor.getString(0);
            byte[] image = cursor.getBlob(1);

            namaBg.setText(nama_b);
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));

        }

        kolom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToKondisiKolom(kode,1);
            }
        });
        balok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToKondisiKolom(kode,2);
            }
        });
        dinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToKondisiKolom(kode, 3);
            }
        });
    }

    void refreshText(int kod){
        int a=0;

        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT id FROM data_kerusakan WHERE id_bangunan="+kod+" AND struktur= 1 ORDER BY id DESC");
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            a++;
        }
        TextView jml_kolom = findViewById(R.id.jml_kolom);
        jml_kolom.setText(a+"");
        a=0;

        cursor = MainActivity.mSQLiteHelper.getData("SELECT id FROM data_kerusakan WHERE id_bangunan="+kod+" AND struktur= 2 ORDER BY id DESC");
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            a++;
        }
        TextView jml_balok = findViewById(R.id.jml_balok);
        jml_balok.setText(a+"");
        a=0;

        cursor = MainActivity.mSQLiteHelper.getData("SELECT id FROM data_kerusakan WHERE id_bangunan="+kod+" AND struktur= 3 ORDER BY id DESC");
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            a++;
        }
        TextView jml_dinding = findViewById(R.id.jml_dinding);
        jml_dinding.setText(a+"");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    void moveToKondisiKolom(final int id, final int a){
        Intent i = new Intent(this, KondisiStruktur.class);
        Bundle bun = new Bundle();
        System.out.println("ini sebelum "+id+" "+a);
        bun.putInt("id", id);
        bun.putInt("stuk",a);
        i.putExtras(bun);
        startActivity(i);
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        refreshText(kode);
        System.out.println("resume");
    }
}
