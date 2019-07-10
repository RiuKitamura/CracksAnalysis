package com.example.cracksanalysis;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HasilDiagnosisActivity extends AppCompatActivity {

    int kode;
    String level;
    double persen;
    TextView nama_bangunan;
    TextView hasil_diagnosis;
    TextView keterangan;
    ImageView gambar_bangunan,close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_diagnosis);

//        getSupportActionBar().setTitle("Hasil Diagnosis");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nama_bangunan = findViewById(R.id.nama_bangunan_txt2);
        hasil_diagnosis = findViewById(R.id.hasil_diagnosis_txt);
        keterangan = findViewById(R.id.keterangan_txt);
        gambar_bangunan = findViewById(R.id.poto_bangunan3);
        close = findViewById(R.id.close_diagnosis);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HasilDiagnosisActivity.this,MainActivity.class);
                startActivity(i);
            }
        });


        kode = getIntent().getExtras().getInt("id");
//        level = getIntent().getExtras().getString("level");
//        persen = getIntent().getExtras().getDouble("persen");

        String nama_b;
        byte[] image;
        String level="";
        double persen=0;
        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT nama_bangunan, poto, hasil_diagnosis, tingkat_kepercayaan FROM data_bangunan WHERE id="+kode);
        while (cursor.moveToNext()){
            nama_b = cursor.getString(0);
            image = cursor.getBlob(1);
            level = cursor.getString(2);
            persen = cursor.getDouble(3);

            nama_bangunan.setText(nama_b);
            gambar_bangunan.setImageBitmap(BitmapFactory.decodeByteArray(image,0,image.length));

        }

        if(level.equals("1")){
            hasil_diagnosis.setText("Rusak Ringan");
            keterangan.setText("Kerusakan adalah rusak ringan dengan probabilitas "+persen);
        }
        else if(level.equals("2")){
            hasil_diagnosis.setText("Rusak sedang");
            keterangan.setText("Kerusakan adalah rusak sedang dengan probabilitas "+persen);
        }
        else if(level.equals("3")){
            hasil_diagnosis.setText("Rusak Berat");
            keterangan.setText("Kerusakan adalah rusak berat dengan probabilitas "+persen);
        }
        else if(level.equals("12")){
            hasil_diagnosis.setText("Rusak Ringan/Sedang");
            keterangan.setText("Kerusakan adalah rusak ringan dan sedang dengan probabilitas "+persen);
        }
        else if(level.equals("23")) {
            hasil_diagnosis.setText("Rusak Sedang/Berat");
            keterangan.setText("Kerusakan adalah rusak sedang dan berat dengan probabilitas "+persen);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(HasilDiagnosisActivity.this,MainActivity.class);
        startActivity(i);
    }
}
