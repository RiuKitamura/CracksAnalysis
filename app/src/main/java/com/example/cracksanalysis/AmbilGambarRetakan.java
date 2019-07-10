package com.example.cracksanalysis;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class AmbilGambarRetakan extends AppCompatActivity {

    int kode,struktur;
    Button gallery,camera;
    ImageView baris1,baris2,baris3,baris4;
    String pathToFile;
    double tmpx, tmpy;
    int panjang, lebar, tengahx, tengahy;

    double d1, d2, d3, d4, dd1, dd2, dd3, dd4;
    String data_txt;

    private static final int WRITE_EXTERNAL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambil_gambar_retakan);

        gallery = findViewById(R.id.galery_btn);
        camera = findViewById(R.id.camera_btn);
        baris1 = findViewById(R.id.baris_1);
        baris2 = findViewById(R.id.baris_2);
        baris3 = findViewById(R.id.baris_3);
        baris4 = findViewById(R.id.baris_4);

        Bundle b = getIntent().getExtras();
        kode = b.getInt("id");
        struktur = b.getInt("stuk");
        Toast.makeText(this, ""+kode+" "+struktur, Toast.LENGTH_SHORT).show();

        if(struktur==1){
            getSupportActionBar().setTitle("Ambil Gambar Kolom");
        }
        else if(struktur==2){
            getSupportActionBar().setTitle("Ambil Gambar Balok");
        }
        else if(struktur==3){
            getSupportActionBar().setTitle("Ambil Gambar Dinding");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null){
                    File photoFile = null;
                    photoFile = createPhotoFile();
                    if(photoFile != null){
                        pathToFile = photoFile.getAbsolutePath();
                        Uri photoUri = FileProvider.getUriForFile(AmbilGambarRetakan.this, "com.thecodecity.cameraandroid.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, 1);
                    }
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                //startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                startActivityForResult(intent, 0);
            }
        });
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            CropImage.activity(Uri.fromFile(new File(pathToFile)))
                    .setAspectRatio(1,1)
                    .start(this);
        }
        else if(requestCode == 0 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri(); //Mengubah data image kedalam Uri
                Bitmap mBitmap = null;
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    potongGambar(mBitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //Menangani Jika terjadi kesalahan
                String error = result.getError().toString();
                Log.d("Exception", error);
                Toast.makeText(getApplicationContext(), "Crop Image Error", Toast.LENGTH_SHORT).show();
            }
        }

//        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri imageUri = result.getUri(); //Mengubah data image kedalam Uri
//                Bitmap mBitmap = null;
//                try {
//                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                    potongGambar(mBitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
////                moveToUpdate(kode, 1);
////                onBackPressed();
//
//                //Menampilkan Gambar pada ImageView
////                Picasso.get().load(imageUri).into(poto);
////                isi_gambar=true;
//
//            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
//                //Menangani Jika terjadi kesalahan
//                String error = result.getError().toString();
//                Log.d("Exception", error);
//                Toast.makeText(getApplicationContext(), "Crop Image Error", Toast.LENGTH_SHORT).show();
//            }
//        }

    }

    void moveToUpdate(int id, int level){
        try{
            MainActivity.mSQLiteHelper.updateDataKerusakan(level,id);
            //updateList();
//            finish();
//            overridePendingTransition(0, 0);
//            startActivity(getIntent());
//            overridePendingTransition(0, 0);
        }
        catch (Exception e){
            Log.e("error", e.getMessage());
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

    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }

    void olahgambar(Bitmap gambar){
        Bitmap image = convertBitmap(gambar);
        Mat mat = new Mat();
        Bitmap bmp32 = image.copy(Bitmap.Config.ARGB_8888, true);
        Utils.bitmapToMat(bmp32, mat);
        Rect roi = new Rect(0,0,227,57);
        Mat cropped = new Mat(mat,roi);
        System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
        System.out.println(cropped.height()+" "+cropped.width());


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void potongGambar(Bitmap gambar){
        Bitmap image = convertBitmap(gambar);
        int row = 4;
        int col = 1;
        panjang = image.getHeight();
        lebar = image.getWidth();
        tmpx = (double) lebar/2;
        tmpy = (double) panjang/2;
        tengahx = (int) Math.ceil(tmpx);
        tengahy = (int) Math.ceil(tmpy);
        System.out.println("width = "+panjang+", height = "+lebar);
        System.out.println("tengahx = "+tengahx+", tengahy = "+tengahy);

        String array2d[][] = new String[panjang][lebar];

        //width and height of each piece
        int eWidth = lebar / col;
        int eHeight = panjang / row;

        int x = 0;
        int y = 0;

        double g1,g2,g3,g4;

        for (int i = 0; i < row; i++) {
            y = 0;
            for (int j = 0; j < col; j++) {
                try {
                    System.out.println("creating piece: "+i+" "+j);
                    Bitmap cropedBitmap = Bitmap.createBitmap(image, y, x, eWidth, eHeight);
//                    BufferedImage SubImgage = image.getSubimage(y, x, eWidth, eHeight);
//                    File outputfile = new File("C:/temp/TajMahal"+i+j+".jpg");
//                    ImageIO.write(SubImgage, "jpg", outputfile);
                    if(i == 0){
                        baris1.setImageBitmap(cropedBitmap);
                        getImageData(cropedBitmap,1);

                    }
                    else if(i == 1){
                        baris2.setImageBitmap(cropedBitmap);
                        getImageData(cropedBitmap,2);

                    }
                    else if(i == 2){
                        baris3.setImageBitmap(cropedBitmap);
                        getImageData(cropedBitmap,3);

                    }
                    else if(i == 3){
                        baris4.setImageBitmap(cropedBitmap);
                        getImageData(cropedBitmap,4);
                    }

                    y += eWidth;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
//                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
//                    requestPermissions(permissions, WRITE_EXTERNAL);
//                }
//                else{
//                    saveToTxt(data_txt);
//                }
//            }
//            else {
//                saveToTxt(data_txt);
//            }

            x += eHeight;
        }

        data_txt = d1+" # "+d2+" # "+d3+" # "+d4+" # "+dd1+" # "+dd2+" # "+dd3+" # "+dd4;

        saveToTxt(data_txt);


    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode){
//            case WRITE_EXTERNAL: {
//                if (grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    saveToTxt(data_txt);
//                }
//                else {
//                    Toast.makeText(this, "Permisi diperlukan", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }

    public void  saveToTxt(String data){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        try{
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/My Files/");
            dir.mkdirs();
            String fileName = "MyFile_" + timeStamp + ".txt";
            File file = new File(dir,fileName);

            FileWriter fw =new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();

            Toast.makeText(this, ""+dir, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void getImageData(Bitmap img, int kode){
        int w = img.getWidth();
        int h = img.getHeight();
        int[] data = new int[w * h];
        img.getPixels(data, 0, w, 0, 0, w, h);

        int[][] pixel = new int[img.getHeight()][img.getWidth()];
        System.out.println("tinggi "+img.getHeight()+" lebar "+img.getWidth()+"panjangnya adalah "+data.length);
        int c=0;
        int tengahX = img.getWidth()/2;
        int tengahY = img.getHeight()/2;

        System.out.println("tengah ->: "+tengahY+" "+tengahX);
        for( int a = 0; a < img.getHeight(); a++ ) {
            for (int b=0;b<img.getWidth();b++){
                pixel[a][b]=data[c];
                c++;
                System.out.print(pixel[a][b]+" ");
            }
            System.out.println();
        }
        double d=0;
        double dd=0;
        int jum_o=0;
        for(int a=0;a<img.getHeight();a++){
            for(int b=0;b<img.getWidth();b++){
                if(pixel[a][b]==0){
                    d=d + Math.sqrt(Math.pow(a-tengahY,2)+Math.pow(b-tengahX,2));
                    dd=dd + Math.sqrt(Math.pow(a-tengahy,2)+Math.pow(b-tengahx,2));
                    jum_o++;
                }
            }
        }
        System.out.println("jum d -> "+d+" d2 -> "+dd+" jum 0 -> "+jum_o);
        if(kode==1){
            d1=d/jum_o;
            dd1=dd/jum_o;
            System.out.println("jum rata -> "+d1+" jum rata2 "+dd1);
        }
        else if(kode==2){
            d2=d/jum_o;
            dd2=dd/jum_o;
            System.out.println("jum rata -> "+d2+" jum rata2 "+dd2);
        }
        else if(kode==3){
            d3=d/jum_o;
            dd3=dd/jum_o;
            System.out.println("jum rata -> "+d3+" jum rata2 "+dd3);
        }
        else if(kode==4){
            d4=d/jum_o;
            dd4=dd/jum_o;
            System.out.println("jum rata -> "+d4+" jum rata2 "+dd4);
        }
    }


    public Bitmap convertBitmap(Bitmap input) {
        int width = input.getWidth();
        int height = input.getHeight();
        Bitmap firstPass =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Bitmap secondPass =  Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas firstCanvas = new Canvas(firstPass);
        Paint colorFilterMatrixPaint = new Paint();
        colorFilterMatrixPaint.setColorFilter(new ColorMatrixColorFilter(new float[]{
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                1, 1, 1, -1, 0
        }));

        firstCanvas.drawBitmap(input, 0, 0, colorFilterMatrixPaint);

        Canvas secondCanvas = new Canvas(secondPass);
        Paint colorFilterMatrixPaint2 = new Paint();
        colorFilterMatrixPaint2.setColorFilter(new ColorMatrixColorFilter(new float[]{
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,
                0, 0, 0, 255, -255
        }));

        secondCanvas.drawBitmap(firstPass, 0, 0, colorFilterMatrixPaint2);

        int pixels[] = new int[width * height];
        byte pixelsMap[] = new byte[width * height];
        secondPass.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixelsMap[(x * y) + y] = (byte) ((pixels[(x * y) + y] >> 24) * -1);
            }
        }
        return secondPass;
    }




}

//    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//    //                        cropedBitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
//    byte[] byteArray = stream.toByteArray();
//    StringBuilder check = new StringBuilder();
//                        for(int a = 0; a < byteArray.length; a++)
//        {
//        check.append(Integer.toBinaryString(byteArray[a]));
//        }
//        int panjang2= cropedBitmap.getHeight();
//        int lebar2= cropedBitmap.getWidth();
//
//        System.out.println("panjang 2 = "+panjang2+" lebar 2 = "+lebar2);
//
//        String array[] = check.toString().split("");
//
//        int panjangArray = array.length;
//        System.out.println("panjang array = "+panjangArray);
//
//        for(int b = 0; b < array.length; b++){
//        System.out.print(array[b]+" ");
//        }
//
//        int isi=0;
//        for(int baris = 0; baris < panjang2; baris++){
//        for(int kolom = 0; kolom < lebar; kolom++){
//        array2d[baris][kolom] = array[isi];
//        System.out.print(array2d[baris][kolom]+" ");
//        isi++;
//        }
//        System.out.println();
//        }
//        System.out.println("ini dia 000000000000000000000000000000000000000000000000000000000000000");
//        for(int baris = 0; baris < panjang; baris++){
//        for(int kolom = 0; kolom < lebar; kolom++){
//        System.out.print(array2d[baris][kolom]);
//        }
//        System.out.println();
//        }
//}
