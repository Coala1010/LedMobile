package com.example.jacksondanny.myapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBLed.db";
    public static final String DESTINATION_TABLE_NAME = "destlist";
    public static final String DESTINATION_COLUMN_ID = "id";
    public static final String DESTINATION_COLUMN_ADDRESS = "address";
    public static final String DESTINATION_COLUMN_COLOR = "color";
    public static final String DESTINATION_COLUMN_LATITUDE = "latitude";
    public static final String DESTINATION_COLUMN_LONGITUDE = "longitude";

    public static final String STARTLOCATION_TABLE_NAME = "startlist";
    public static final String STARTLOCATION_COLUMN_ID = "startid";
    public static final String STARTLOCATION_COLUMN_ADDRESS = "startaddress";
    public static final String STARTLOCATION_COLUMN_COLOR = "startcolor";
    public static final String STARTLOCATION_COLUMN_LATITUDE = "startlatitude";
    public static final String STARTLOCATION_COLUMN_LONGITUDE = "startlongitude";

    public static final String CONTACTS_TABLE_NAME = "contactlist";
    public static final String CONTACTS_COLUMN_NAME = "contactname";
    public static final String CONTACTS_COLUMN_COLOR = "contactcolor";
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table destlist " +  "(id integer primary key, address text, color text, latitude text, longitude text)");
        db.execSQL("create table startlist " +  "(id integer primary key, startaddress text, startcolor text, startlatitude text, startlongitude text)");
        db.execSQL("create table contactlist " +  "(id integer primary key, contactname text, contactcolor text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS destlist");
        db.execSQL("DROP TABLE IF EXISTS startlist");
        db.execSQL("DROP TABLE IF EXISTS contactlist");
        onCreate(db);
    }

    public boolean insertPhoneContact (String contactname, String contactcolor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contactname", contactname);
        contentValues.put("contactcolor", contactcolor);
        db.insert("contactlist", null, contentValues);
        return true;
    }

    public boolean updatePhoneContact (Integer id, String contactname, String contactcolor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("contactname", contactname);
        contentValues.put("contactcolor", contactcolor);
        db.update("contactlist", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public ArrayList<String> getAllPhoneContactName() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contactlist", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllPhoneContactColor() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contactlist", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_COLOR)));
            res.moveToNext();
        }
        return array_list;
    }

    public Integer removePhoneContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contactlist", "id = ? ",  new String[] { Integer.toString(id) });
    }

    public boolean insertLocation (String address, String color, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("startaddress", address);
        contentValues.put("startcolor", color);
        contentValues.put("startlatitude", latitude);
        contentValues.put("startlongitude", longitude);
        db.insert("startlist", null, contentValues);
        return true;
    }

    public boolean updateLocation (Integer id, String address, String color, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("color", color);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        db.update("startlist", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public int numberOfPhoneContact(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public void deleteLocation(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "delete from startlist", null );
    }
    public int numberOfLocation(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, STARTLOCATION_TABLE_NAME);
        return numRows;
    }
    public Cursor getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from startList where id="+id+"", null );
        return res;
    }

    public double getLatitudeLocation() {
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from startList", null );
     //   res.moveToFirst();
        res.moveToLast();

        return Double.parseDouble(res.getString(res.getColumnIndex(STARTLOCATION_COLUMN_LATITUDE)));
    }
    public double getLongitudeLocation() {
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from startList", null );
        res.moveToLast();

        return Double.parseDouble(res.getString(res.getColumnIndex(STARTLOCATION_COLUMN_LONGITUDE)));
    }
    public int getColorLocation() {
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from startList", null );
        res.moveToLast();

        return Integer.parseInt(res.getString(res.getColumnIndex(STARTLOCATION_COLUMN_COLOR)));
    }


    public boolean insertContact (String address, String color, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("color", color);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
     /*   contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);*/
        db.insert("destlist", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from destlist where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DESTINATION_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String address, String color, String latitude, String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("color", color);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        db.update("destlist", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("destlist", "id = ? ",  new String[] { Integer.toString(id) });
    }

    public Integer deleteContact (String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("destlist", "address = ? ",  new String[] { address });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from destlist", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DESTINATION_COLUMN_ADDRESS)));
        //    array_list.add(res.getString(res.getColumnIndex(DESTINATION_COLUMN_COLOR)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllColors() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from destlist", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DESTINATION_COLUMN_COLOR)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllLatitude() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from destlist", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DESTINATION_COLUMN_LATITUDE)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllLongitude() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from destlist", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DESTINATION_COLUMN_LONGITUDE)));
            res.moveToNext();
        }
        return array_list;
    }
}