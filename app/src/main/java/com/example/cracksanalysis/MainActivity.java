package com.example.cracksanalysis;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button up;

    ListView mListView;
    ArrayList<Model> mList;
    HistoryListAdapter mAdapter = null;

    final int CAMERA_REQUEST_CODE1 = 1;
    final int CAMERA_REQUEST_CODE2 = 0;

    ImageView imageViewIcon;

    public static SQLiteHelper mSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(OpenCVLoader.initDebug()){
            Toast.makeText(this, "berhasill", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "gagall", Toast.LENGTH_SHORT).show();
        }

        mListView = findViewById(R.id.list1);
        mList = new ArrayList<>();
        mAdapter = new HistoryListAdapter(this, R.layout.history_layout, mList);
        mListView.setAdapter(mAdapter);

        mSQLiteHelper = new SQLiteHelper(this, "kerusakan_bangunan.sqlite", null, 1);
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS data_bangunan(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nama_bangunan VARCHAR, jumlah_lantai VARCHAR, tahun VARCHAR, alamat_bangunan VARCHAR, latitude VARCHAR, " +
                "longitude VARCHAR, poto BLOB, nama VARCHAR, alamat VARCHAR, nomor_hp VARCHAR)");

        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS data_kerusakan(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_bangunan INTEGER, struktur INTEGER, level_kerusakan INTEGER)");

        //mSQLiteHelper.getReadableDatabase();
        //GET ALL DATA FROM SQLITE
        Cursor cursor = mSQLiteHelper.getData("SELECT id, nama_bangunan, jumlah_lantai, tahun, alamat_bangunan,latitude, " +
                "longitude, nama, alamat, nomor_hp FROM data_bangunan ORDER BY id DESC");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nama_b = cursor.getString(1);
            String lantai = cursor.getString(2);
            String thn = cursor.getString(3);
            String alamat_b = cursor.getString(4);
            String lati = cursor.getString(5);
            String longi = cursor.getString(6);
            //byte[] poto = cursor.getBlob(7);
            String nama = cursor.getString(7);
            String alamat = cursor.getString(8);
            String nomor = cursor.getString(9);

            mList.add(new Model(id, nama_b, lantai, thn, alamat_b,lati, longi, nama, alamat, nomor));

        }
        mAdapter.notifyDataSetChanged();
        if(mList.size()==0){
            Toast.makeText(this, "Data kosong", Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = mSQLiteHelper.getData("SELECT id FROM data_bangunan ORDER BY id DESC");
                ArrayList<Integer> arrID = new ArrayList<Integer>();
                while (c.moveToNext()){
                    arrID.add(c.getInt(0));
                }
                moveToDiagonis(arrID.get(position));

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items = {"Update", "Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setTitle("Pilih sebuah aksi");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            Cursor c = mSQLiteHelper.getData("SELECT id FROM data_bangunan ORDER BY id DESC");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            moveToUpdate(arrID.get(position));

                            //showDialogUpdate(MainActivity.this, arrID.get(position));
                        }
                        if (which == 1){
                            Cursor c = mSQLiteHelper.getData("SELECT id FROM data_bangunan ORDER BY id DESC");
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

        ImageButton btn_add = (ImageButton) findViewById(R.id.add_btn);
        btn_add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this,FormActivity.class);
                startActivity(i);
            }
        });
    }

    void moveToDiagonis(final int id){
        Intent i = new Intent(this, DiagnosisActivity.class);
        Bundle bun = new Bundle();
        bun.putInt("id", id);
        i.putExtras(bun);
        startActivity(i);
    }

    private void showDialogDelete(final int id) {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(MainActivity.this);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Apa anda yakin untuk menghapus?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    mSQLiteHelper.deleteData(id);
                    Toast.makeText(MainActivity.this, "Penghapusan berhasil", Toast.LENGTH_SHORT).show();
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

    public void moveToUpdate(final int position){
        Intent i = new Intent(this, UpdateActivity.class);
        Bundle bun = new Bundle();
        bun.putInt("id", position);
        i.putExtras(bun);
        startActivity(i);
    }

//    private void showDialogUpdate(Activity activity, final int position){
//        final Dialog dialog = new Dialog(activity);
//        dialog.setContentView(R.layout.update_dialog);
//        dialog.setTitle("Update");
//
//        imageViewIcon = dialog.findViewById(R.id.up_add_photo_btn);
//        final EditText edtNamaB = dialog.findViewById(R.id.up_nama_bangunan);
//        final EditText edtLantai = dialog.findViewById(R.id.up_jml_lantai);
//        final EditText edtThn = dialog.findViewById(R.id.up_thn_dibuat);
//        final EditText edtAlamatB = dialog.findViewById(R.id.up_alamat_bangunan);
//        final EditText edtLati = dialog.findViewById(R.id.up_latitude);
//        final EditText edtLongi = dialog.findViewById(R.id.up_longitude);
//        final EditText edtNama = dialog.findViewById(R.id.up_nama_person);
//        final EditText edtAlamat = dialog.findViewById(R.id.up_alamat_person);
//        final EditText edtNomor = dialog.findViewById(R.id.up_nomor_person);
//        Button btnUpdate = dialog.findViewById(R.id.update_btn);
//
//
//        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels*0.95);
//        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels*0.7);
//        dialog.getWindow().setLayout(width,height);
//        dialog.show();
//
//        imageViewIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SelectImage();
//            }
//        });
//        btnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    mSQLiteHelper.updateData(
//                            edtNamaB.getText().toString().trim(),
//                            edtLantai.getText().toString().trim(),
//                            edtThn.getText().toString().trim(),
//                            edtAlamatB.getText().toString().trim(),
//                            edtLati.getText().toString().trim(),
//                            edtLongi.getText().toString().trim(),
//                            imageViewToByte(imageViewIcon),
//                            edtNama.getText().toString().trim(),
//                            edtAlamat.getText().toString().trim(),
//                            edtNomor.getText().toString().trim(),
//                            position
//
//                    );
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), "Update sukses", Toast.LENGTH_SHORT).show();
//                    finish();
//                    overridePendingTransition(0, 0);
//                    startActivity(getIntent());
//                    overridePendingTransition(0, 0);
//                }
//                catch (Exception error){
//                    Log.e("Update error", error.getMessage());
//                }
//                updateList();
//            }
//        });
//
//    }


    private void updateList() {
        Cursor cursor = mSQLiteHelper.getData("SELECT id, nama_bangunan, jumlah_lantai, tahun, alamat_bangunan,latitude, " +
                "longitude, nama, alamat, nomor_hp FROM data_bangunan ORDER BY id DESC");
        mList.clear();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String namaB = cursor.getString(1);
            String lantai = cursor.getString(2);
            String thn = cursor.getString(3);
            String alamatB = cursor.getString(4);
            String lati = cursor.getString(5);
            String longi = cursor.getString(6);
//            byte[] image = cursor.getBlob(7);
            String nama = cursor.getString(7);
            String alamat = cursor.getString(8);
            String nomor = cursor.getString(9);
        }
        mAdapter.notifyDataSetChanged();
    }

//    private void SelectImage(){
//
//        final CharSequence[] items={"Camera","Gallery", "Cancel"};
//
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
//        builder.setTitle("Add Image");
//
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (items[i].equals("Camera")) {
//
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, CAMERA_REQUEST_CODE1);
//
//                } else if (items[i].equals("Gallery")) {
//
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
//                    startActivityForResult(intent, CAMERA_REQUEST_CODE2);
//
//                } else if (items[i].equals("Cancel")) {
//                    dialogInterface.dismiss();
//                }
//            }
//        });
//        builder.show();
//
//    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(resultCode== Activity.RESULT_OK){

            if(requestCode==CAMERA_REQUEST_CODE1){

                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                imageViewIcon.setImageBitmap(bmp);

            }else if(requestCode==CAMERA_REQUEST_CODE2){

                Uri selectedImageUri = data.getData();
                imageViewIcon.setImageURI(selectedImageUri);
            }

        }
    }


    //    private static byte[] imageViewToByte(ImageView image) {
//        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        return byteArray;
//    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
