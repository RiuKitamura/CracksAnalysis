package com.example.cracksanalysis;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class UpdateActivity extends AppCompatActivity {

    ArrayList<Model> mList;
    HistoryListAdapter mAdapter = null;
    ImageView imageViewIcon;

    final int CAMERA_REQUEST_CODE1 = 1;
    final int CAMERA_REQUEST_CODE2 = 0;
    String pathToFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mList = new ArrayList<>();

        imageViewIcon = findViewById(R.id.up_add_photo_btn);
        final EditText edtNamaB = findViewById(R.id.up_nama_bangunan);
        final EditText edtLantai = findViewById(R.id.up_jml_lantai);
        final EditText edtThn = findViewById(R.id.up_thn_dibuat);
        final EditText edtAlamatB = findViewById(R.id.up_alamat_bangunan);
        final EditText edtLati = findViewById(R.id.up_latitude);
        final EditText edtLongi = findViewById(R.id.up_longitude);
        final EditText edtNama = findViewById(R.id.up_nama_person);
        final EditText edtAlamat = findViewById(R.id.up_alamat_person);
        final EditText edtNomor = findViewById(R.id.up_nomor_person);
        Button btnUpdate = findViewById(R.id.update_btn);

        final int kode = getIntent().getExtras().getInt("id");

        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("masuk");
                SelectImage();
            }
        });
        System.out.println("masuk");
        System.out.println("kode "+kode);
        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM data_bangunan WHERE id="+kode);
        mList.clear();
        while (cursor.moveToNext()){
            System.out.println("masuk dddddd");
            int id = cursor.getInt(0);
            String nama_b = cursor.getString(1);
            String lantai = cursor.getString(2);
            String thn = cursor.getString(3);
            String alamat_b = cursor.getString(4);
            String lati = cursor.getString(5);
            String longi = cursor.getString(6);
            byte[] poto = cursor.getBlob(7);
            String nama = cursor.getString(8);
            String alamat = cursor.getString(9);
            String nomor = cursor.getString(10);

            System.out.println("nama b "+nama_b);
            System.out.println("lantai:" +lantai);
            System.out.println("tahun "+thn);
            System.out.println("alamat b "+alamat);
            System.out.println("lati "+lati);
            System.out.println("longi "+longi);
            System.out.println("nama "+nama);
            System.out.println("alamat "+alamat);
            System.out.println("nomor "+nomor);

            edtNamaB.setText(nama_b);
            edtLantai.setText(lantai);
            edtThn.setText(thn);
            edtAlamatB.setText(alamat_b);
            edtLati.setText(lati);
            edtLongi.setText(longi);
            imageViewIcon.setImageBitmap(BitmapFactory.decodeByteArray(poto,0,poto.length));
            edtNama.setText(nama);
            edtAlamat.setText(alamat);
            edtNomor.setText(nomor);

            getSupportActionBar().setTitle(nama_b);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mList.add(new Model(id, nama_b, lantai, thn, alamat_b,lati, longi, poto, nama, alamat, nomor));



        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    MainActivity.mSQLiteHelper.updateData(
                            edtNamaB.getText().toString().trim(),
                            edtLantai.getText().toString().trim(),
                            edtThn.getText().toString().trim(),
                            edtAlamatB.getText().toString().trim(),
                            edtLati.getText().toString().trim(),
                            edtLongi.getText().toString().trim(),
                            imageViewToByte(imageViewIcon),
                            edtNama.getText().toString().trim(),
                            edtAlamat.getText().toString().trim(),
                            edtNomor.getText().toString().trim(),
                            kode

                    );
                    Toast.makeText(getApplicationContext(), "Update sukses", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UpdateActivity.this,MainActivity.class);
                    startActivity(i);
                }
                catch (Exception error){
                    Log.e("Update error", error.getMessage());
                }
                //updateList();
            }
        });
    }

    private void SelectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(intent.resolveActivity(getPackageManager()) != null){
                        File photoFile = null;
                        photoFile = createPhotoFile();
                        if(photoFile != null){
                            pathToFile = photoFile.getAbsolutePath();
                            Uri photoUri = FileProvider.getUriForFile(UpdateActivity.this, "com.thecodecity.cameraandroid.fileprovider", photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(intent, CAMERA_REQUEST_CODE1);
                        }
                    }


                } else if (items[i].equals("Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE2);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();

    }

    private  File createPhotoFile(){
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try{
            image = File.createTempFile(name,".jpg", storageDir);
        }
        catch (IOException e){
            Log.d("mylog","Excep : "+e.toString());
        }
        return image;
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(requestCode == CAMERA_REQUEST_CODE1 && resultCode == RESULT_OK){
////            Bundle bundle = data.getExtras();
////            final Bitmap bmp = (Bitmap) bundle.get("data");
////            poto.setImageBitmap(bmp);
////            isi_gambar=true;
//            Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            String pictureFilePath = "";
//            File f = new File(pictureFilePath);
//            Uri picUri = Uri.fromFile(f);
//            galleryIntent.setData(picUri);
//            this.sendBroadcast(galleryIntent);
            Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
            CropImage.activity(Uri.fromFile(new File(pathToFile)))
                    .setAspectRatio(1,1)
                    .start(this);
//            poto.setImageBitmap(bitmap);
//            isi_gambar=true;
        }
        else if(requestCode == CAMERA_REQUEST_CODE2 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri(); //Mengubah data image kedalam Uri
                Bitmap  mBitmap = null;
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    int a = mBitmap.getWidth();
                    int b = mBitmap.getHeight();
                    System.out.println("width = "+a+", height = "+b);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Menampilkan Gambar pada ImageView
                Picasso.get().load(imageUri).into(imageViewIcon);

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //Menangani Jika terjadi kesalahan
                String error = result.getError().toString();
                Log.d("Exception", error);
                Toast.makeText(getApplicationContext(), "Crop Image Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 0, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

//    private void updateList() {
//        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM data_bangunan ORDER BY id DESC");
//        mList.clear();
//        while (cursor.moveToNext()){
//            int id = cursor.getInt(0);
//            String namaB = cursor.getString(1);
//            String lantai = cursor.getString(2);
//            String thn = cursor.getString(3);
//            String alamatB = cursor.getString(4);
//            String lati = cursor.getString(5);
//            String longi = cursor.getString(6);
//            byte[] image = cursor.getBlob(7);
//            String nama = cursor.getString(8);
//            String alamat = cursor.getString(9);
//            String nomor = cursor.getString(10);
//        }
//        mAdapter.notifyDataSetChanged();
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed(){
        UpdateActivity.ViewDialog alert = new UpdateActivity.ViewDialog();
        alert.showDialog(UpdateActivity.this);

    }

    public class ViewDialog {
        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.popup_edit);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            Button mDialogNo = dialog.findViewById(R.id.frmNo2);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"No" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            Button mDialogOk = dialog.findViewById(R.id.frmOk2);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Ok" ,Toast.LENGTH_SHORT).show();
                    UpdateActivity.super.onBackPressed();
                }
            });

            dialog.show();
        }
    }
}
