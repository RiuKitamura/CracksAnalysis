package com.example.cracksanalysis;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
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
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class FormActivity extends AppCompatActivity {

    EditText nama_bg, lantai, tahun, alamat_bg, lati, longi, nama, alamat, no_hp;
    ImageView poto;
    Button next;

    final int CAMERA_REQUEST_CODE1 = 1;
    final int CAMERA_REQUEST_CODE2 = 0;
    String pathToFile;

    boolean isi_gambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        getSupportActionBar().setTitle("Formulir");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        nama_bg = (EditText) findViewById(R.id.nama_bangunan);
        lantai = (EditText) findViewById(R.id.jml_lantai);
        tahun = (EditText) findViewById(R.id.thn_dibuat);
        alamat_bg = (EditText) findViewById(R.id.alamat_bangunan);
        lati = (EditText) findViewById(R.id.latitude);
        longi = (EditText) findViewById(R.id.longitude);
        poto = findViewById(R.id.add_photo_btn);
        nama = (EditText) findViewById(R.id.nama_person);
        alamat = (EditText) findViewById(R.id.alamat_person);
        no_hp = (EditText) findViewById(R.id.nomor_person);
        next = findViewById(R.id.next_btn);

        isi_gambar = false;
        poto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("masuk");
                SelectImage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama_bg.getText().length()!=0 && lantai.getText().length()!=0 && tahun.getText().length()!=0
                        && alamat_bg.getText().length()!=0 && lati.getText().length()!=0 && longi.getText().length()!=0
                        && isi_gambar!=false && nama.getText().length()!=0 && alamat.getText().length()!=0
                        && no_hp.getText().length()!=0 ){
                    try{
                        System.out.println("nama b "+nama_bg.getText());
                        System.out.println("lantai:" +lantai.getText());
                        System.out.println("tahun "+tahun.getText());
                        System.out.println("alamat b "+alamat_bg.getText());
                        System.out.println("lati "+lati.getText());
                        System.out.println("longi "+longi.getText());
                        System.out.println("nama "+nama.getText());
                        System.out.println("alamat "+alamat.getText());
                        System.out.println("nomor "+no_hp.getText());
                        MainActivity.mSQLiteHelper.insertData(
                                nama_bg.getText().toString().trim(),
                                lantai.getText().toString().trim(),
                                tahun.getText().toString().trim(),
                                alamat_bg.getText().toString().trim(),
                                lati.getText().toString().trim(),
                                longi.getText().toString().trim(),
                                imageViewToByte(poto),
                                nama.getText().toString().trim(),
                                alamat.getText().toString().trim(),
                                no_hp.getText().toString().trim()
                        );
                        Toast.makeText(FormActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(FormActivity.this,MainActivity.class);
                        startActivity(i);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(FormActivity.this, "Harus diisi semua", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SelectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(FormActivity.this);
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
                            Uri photoUri = FileProvider.getUriForFile(FormActivity.this, "com.thecodecity.cameraandroid.fileprovider", photoFile);
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
                Picasso.get().load(imageUri).into(poto);
                isi_gambar=true;

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //Menangani Jika terjadi kesalahan
                String error = result.getError().toString();
                Log.d("Exception", error);
                Toast.makeText(getApplicationContext(), "Crop Image Error", Toast.LENGTH_SHORT).show();
            }
        }

//        if(resultCode== Activity.RESULT_OK){
//
//            if(requestCode==CAMERA_REQUEST_CODE1){
//
//                Bundle bundle = data.getExtras();
//                final Bitmap bmp = (Bitmap) bundle.get("data");
//                poto.setImageBitmap(bmp);
//                isi_gambar=true;
//
//            }else if(requestCode==CAMERA_REQUEST_CODE2){
//                Bitmap  mBitmap = null;
//                Uri selectedImageUri = data.getData();
//                try {
//                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
//                    int a = mBitmap.getWidth();
//                    int b = mBitmap.getHeight();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //poto.setImageURI(selectedImageUri);
//                poto.setImageBitmap(mBitmap);
//                isi_gambar=true;
//            }
//
//        }
    }


    private static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 0, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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
    public void onBackPressed(){
        ViewDialog alert = new ViewDialog();
        alert.showDialog(FormActivity.this);

    }

    public class ViewDialog {
        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            Button mDialogNo = dialog.findViewById(R.id.frmNo);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"No" ,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            Button mDialogOk = dialog.findViewById(R.id.frmOk);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"Ok" ,Toast.LENGTH_SHORT).show();
                    FormActivity.super.onBackPressed();
                }
            });

            dialog.show();
        }
    }
}
