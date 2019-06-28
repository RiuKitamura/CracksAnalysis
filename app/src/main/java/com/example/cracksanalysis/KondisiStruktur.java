package com.example.cracksanalysis;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class KondisiStruktur extends AppCompatActivity {

    ListView mListView;
    ArrayList<Model2> mList;
    KondisiStrukturListAdapter mAdapter = null;

    final int CAMERA_REQUEST_CODE1 = 1;
    final int CAMERA_REQUEST_CODE2 = 0;
    ImageButton add_kolom;
    int kode, stuktur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kondisi_struktur);

        Intent intent = getIntent();

        Bundle b = getIntent().getExtras();

        kode = b.getInt("id");
        stuktur = b.getInt("stuk");
        System.out.println("ini "+kode+" dan "+stuktur);
        if(stuktur==1){
            getSupportActionBar().setTitle("Kondisi Kolom");
        }
        else if(stuktur==2){
            getSupportActionBar().setTitle("Kondisi Balok");
        }
        else if(stuktur==3){
            getSupportActionBar().setTitle("Kondisi Dinding");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = findViewById(R.id.list_kolom);
        mList = new ArrayList<>();
        mAdapter = new KondisiStrukturListAdapter(this, R.layout.list_kerusakan_layout, mList);
        mListView.setAdapter(mAdapter);

        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM data_kerusakan WHERE id_bangunan="+kode+" AND struktur="+stuktur+" ORDER BY id ASC");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int id_bangunan = cursor.getInt(1);
            int struktur = cursor.getInt(2);
            int level = cursor.getInt(3);
            System.out.println("leve kerusakan = "+level);
            mList.add(new Model2(id, id_bangunan,struktur,level));

        }
        mAdapter.notifyDataSetChanged();
        if(mList.size()==0){
            Toast.makeText(this, "Data kosong", Toast.LENGTH_SHORT).show();
        }

        add_kolom = findViewById(R.id.add_list_kolom);
        add_kolom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stuktur==1){
                    MainActivity.mSQLiteHelper.insertDataKerusakan(kode,1,0);
                    Toast.makeText(KondisiStruktur.this, "+ 1 Kolom", Toast.LENGTH_SHORT).show();
                }
                else if(stuktur==2){
                    MainActivity.mSQLiteHelper.insertDataKerusakan(kode,2,0);
                    Toast.makeText(KondisiStruktur.this, "+ 1 Balok", Toast.LENGTH_SHORT).show();
                }
                else if(stuktur==3){
                    MainActivity.mSQLiteHelper.insertDataKerusakan(kode,3,0);
                    Toast.makeText(KondisiStruktur.this, "+ 1 Dinding", Toast.LENGTH_SHORT).show();
                }
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"Rusak Ringan", "Rusak Sedang", "Rusak Berat"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(KondisiStruktur.this);

                dialog.setTitle("Level Kerusakan");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM data_kerusakan WHERE id_bangunan="+kode+" AND struktur="+stuktur+" ORDER BY id ASC");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            moveToUpdate(arrID.get(position), 1);

                            //showDialogUpdate(MainActivity.this, arrID.get(position));
                        }
                        if (which == 1){
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM data_kerusakan WHERE id_bangunan="+kode+" AND struktur="+stuktur+" ORDER BY id ASC");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            moveToUpdate(arrID.get(position), 2);

                            //showDialogUpdate(MainActivity.this, arrID.get(position));
                        }
                        if (which == 2){
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM data_kerusakan WHERE id_bangunan="+kode+" AND struktur="+stuktur+" ORDER BY id ASC");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            moveToUpdate(arrID.get(position), 3);

                            //showDialogUpdate(MainActivity.this, arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return;
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(KondisiStruktur.this);

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM data_kerusakan WHERE id_bangunan="+kode+" AND struktur="+stuktur+" ORDER BY id ASC");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }
    private void showDialogDelete(final int id) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(KondisiStruktur.this);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Apa anda yakin untuk menghapus?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    MainActivity.mSQLiteHelper.deleteDataKerusakan(id);
                    Toast.makeText(KondisiStruktur.this, "Penghapusan berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateList();
            }
        });
        dialogDelete.setNegativeButton("Cencel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateList() {
        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM data_kerusakan ORDER BY id ASC");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int id_bangunan = cursor.getInt(1);
            int struktur = cursor.getInt(2);
            int level = cursor.getInt(3);

        }
        mListView = findViewById(R.id.list_kolom);
        mList = new ArrayList<>();
        mAdapter = new KondisiStrukturListAdapter(this, R.layout.list_kerusakan_layout, mList);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM data_kerusakan WHERE id_bangunan="+kode+" AND struktur="+stuktur+" ORDER BY id ASC");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int id_bangunan = cursor.getInt(1);
            int struktur = cursor.getInt(2);
            int level = cursor.getInt(3);
            mList.add(new Model2(id, id_bangunan,struktur,level));

        }
        mAdapter.notifyDataSetChanged();
        if(mList.size()==0){
            Toast.makeText(this, "Data kosong", Toast.LENGTH_SHORT).show();
        }

    }

    void moveToUpdate(int id, int level){
        try{
            MainActivity.mSQLiteHelper.updateDataKerusakan(level,id);
            updateList();
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
        catch (Exception e){
            Log.e("error", e.getMessage());
        }
    }
    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        updateList();
        System.out.println("resume");
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
        super.onBackPressed();
    }
}
