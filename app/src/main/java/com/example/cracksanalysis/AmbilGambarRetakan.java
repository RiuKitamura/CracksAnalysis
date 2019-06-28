package com.example.cracksanalysis;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class AmbilGambarRetakan extends AppCompatActivity {

    int kode,struktur;
    Button gallery,camera;
    ImageView baris1,baris2,baris3,baris4;
    String pathToFile;
    double tmpx, tmpy;
    int panjang, lebar, tengahx, tengahy;

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

//                moveToUpdate(kode, 1);
//                onBackPressed();

                //Menampilkan Gambar pada ImageView
//                Picasso.get().load(imageUri).into(poto);
//                isi_gambar=true;

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //Menangani Jika terjadi kesalahan
                String error = result.getError().toString();
                Log.d("Exception", error);
                Toast.makeText(getApplicationContext(), "Crop Image Error", Toast.LENGTH_SHORT).show();
            }
        }

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
    void potongGambar(Bitmap gambar){
        Bitmap image = convertBitmap(gambar);
        int row = 4;
        int col = 1;
        panjang = image.getHeight();
        lebar = image.getWidth();
        tmpx = (double) panjang/2;
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

//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        cropedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                        byte[] imageInByte = baos.toByteArray();
//                        try { baos.close(); }
//                        catch (IOException e) { e.printStackTrace(); }
//
//                        StringBuilder check = new StringBuilder();
//                        for(int a = 0; a < imageInByte.length; a++)
//                        {
//                            check.append(Integer.toBinaryString(imageInByte[i]));
//                        }
//
//                        String array[] = check.toString().split("");
//                        for (int a = 0; a<array.length;a++){
//                            System.out.print(array[a]+" ");
//                        }
//                        System.out.println("Panjang array = "+array.length+" tinggi = "+cropedBitmap.getHeight()+" lebar = "+cropedBitmap.getWidth());
//
//                        String array2[][] = new String[cropedBitmap.getHeight()][cropedBitmap.getWidth()];
//                        int isi = 0;
//                        for(int a = 0; a < cropedBitmap.getHeight(); a++){
//                            for(int b = 0; b < cropedBitmap.getWidth(); b++){
//                                array2[a][b] = array[isi];
//                                isi++;
//                                System.out.print(array2[a][b]+" ");
//                            }
//                            System.out.println();
//                        }

                    }
                    else if(i == 1){
                        baris2.setImageBitmap(cropedBitmap);

                        int height = cropedBitmap.getHeight();
                        int width = cropedBitmap.getWidth();
                        int[][] pixels = new int[height][width];

                        for( int a = 0; a < height; a++ ) {
                            for (int b = 0; b < width; b++) {
                                pixels[a][b] = cropedBitmap.getPixel(a,b);
                                System.out.print(pixels[a][b]+" ");
                            }
                            System.out.println();
                        }
                    }
                    else if(i == 2){
                        baris3.setImageBitmap(cropedBitmap);

//                        int pix[][]= new int[cropedBitmap.getHeight()][cropedBitmap.getWidth()];
//                        for(int a = 0; a < cropedBitmap.getHeight(); a++){
//                            for(int b = 0; b < cropedBitmap.getWidth(); b++){
//                                System.out.print(pix[a][b]+" ");
//                            }
//                            System.out.println();
//                        }

                    }
                    else if(i == 3){
                        baris4.setImageBitmap(cropedBitmap);
                    }

                    y += eWidth;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            x += eHeight;
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
