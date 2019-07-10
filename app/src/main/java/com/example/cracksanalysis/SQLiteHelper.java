package com.example.cracksanalysis;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {
    SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    public void queryData (String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertData(String namaB, String lantai, String thn, String alamatB, String lati,
                           String longi, byte[] image, String nama, String alamat, String hp){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO data_bangunan VALUES(NULL,?,?,?,?,?,?,?,?,?,?,null,null)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,namaB);
        statement.bindString(2,lantai);
        statement.bindString(3,thn);
        statement.bindString(4,alamatB);
        statement.bindString(5,lati);
        statement.bindString(6,longi);
        statement.bindBlob(7,image);
        statement.bindString(8,nama);
        statement.bindString(9,alamat);
        statement.bindString(10,hp);

        statement.executeInsert();

    }
    public void updateDataLevel(String data, double persen, int id){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE data_bangunan SET hasil_diagnosis = ?, tingkat_kepercayaan = ? WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindString(1,data);
        statement.bindDouble(2, persen);
        statement.bindDouble(3,(double) id);

        statement.execute();
        database.close();

    }

    public void insertDataKerusakan (int id_bangunan, int struktur, int level_kerusakan){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO data_kerusakan VALUES(NULL,?,?,?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindDouble(1,(double)id_bangunan);
        statement.bindDouble(2,(double)struktur);
        statement.bindDouble(3,(double)level_kerusakan);

        statement.executeInsert();

    }
    public void insertDataGambar (String data){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "INSERT INTO data_gambar VALUES(?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,data);

        statement.executeInsert();
    }

    public void updateData(String namaB, String lantai, String thn, String alamatB, String lati,
                           String longi, byte[] image, String nama, String alamat, String hp, int id){

        SQLiteDatabase database = getWritableDatabase();

        String  sql = "UPDATE data_bangunan SET nama_bangunan = ?, jumlah_lantai = ?, tahun = ?," +
                "alamat_bangunan = ?, latitude = ?, longitude = ?, poto = ?, nama = ?, alamat = ?, " +
                "nomor_hp = ? WHERE id = ?";

        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1,namaB);
        statement.bindString(2,lantai);
        statement.bindString(3,thn);
        statement.bindString(4,alamatB);
        statement.bindString(5,lati);
        statement.bindString(6,longi);
        statement.bindBlob(7,image);
        statement.bindString(8,nama);
        statement.bindString(9,alamat);
        statement.bindString(10,hp);
        statement.bindDouble(11, (double)id);

        statement.execute();
        database.close();

    }

    public void updateDataKerusakan (int level, int id){
        System.out.println("ini dddddiaaa "+level+"  "+id);

        SQLiteDatabase database = getWritableDatabase();

        String  sql = "UPDATE data_kerusakan SET level_kerusakan = ? WHERE id = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.bindDouble(1,(double)level);
        statement.bindDouble(2,(double)id);
        statement.execute();
        database.close();

    }

    public void deleteData(int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM data_bangunan WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteData2(int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM data_kerusakan WHERE id_bangunan = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public void deleteDataKerusakan(int id){
        SQLiteDatabase database = getWritableDatabase();

        String sql = "DELETE FROM data_kerusakan WHERE id = ?";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return  database.rawQuery(sql,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
