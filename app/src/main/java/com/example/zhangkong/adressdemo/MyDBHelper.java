package com.example.zhangkong.adressdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by ZhangKong on 2015/5/15.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    // 声明数据库名字和数据库中的表
    private static final String DBname = "address.db";
    private static final String Companytable = "companytable";
    private static final String Contacttable = "contacttable";
    public SQLiteDatabase mydatabase;

    public MyDBHelper(Context context)
    {
        super(context ,DBname,null,1);
    }
    public void onCreate(SQLiteDatabase db){
        this.mydatabase = db;

        String table1 = "create table companytable(_id integer primary key autoincrement,Name varchar, ID varchar, ParentID varchar,Code varchar,Abbre varchar,IsCompany integer)";
        String table2 = "create table contacttable(_id integer primary key autoincrement,Name varchar,Code varchar,ID varchar,DepartmentID varchar,DepartmentCode varchar,Mobile varchar,Telephone varchar,Email varchar,FullName varchar,ShortName varchar,Sex integer)";
        mydatabase.execSQL(table1);
        mydatabase.execSQL(table2);


    }
    public void insert(String tablename,ContentValues values){
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tablename,null,values);
    }
    public Cursor query(String tablename){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(tablename,null,null,null,null,null,null);
        return c;
    }
    public Cursor query(String tablename,String parentid){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(tablename,null,"ParentID = ?",new String[]{parentid},null,null,null);
        return c;
    }

    public Cursor contactquery(String tablename,String departmentid){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(tablename,null,"DepartmentID = ?",new String[] {departmentid},null,null,null);
        return  c;
    }
    public Cursor companyquery(String tablename){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(tablename,null,"IsCompany = 1",null,null,null,null);
        return c;
    }
    public Cursor codequery(String tablename,String code){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(tablename,null,"Code = ?",new String[]{code},null,null,null);
        return c;
    }
    public Cursor namequery(String tablename,String namequery){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c= db.query(tablename,null,"Name like ?",new String[]{namequery + "%"},null,null,null);
        return c;
    }
    public Cursor telephonequery(String tablename,String telephone){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(tablename,null,"Mobile like ?",new String[]{telephone + "%"},null,null,null);
        return c;
    }
    public Cursor departmentquery(String tablename,String id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(tablename,null,"ID = ?",new String[]{id},null,null,null);
        return c;
    }
    public Cursor bothquery(String tablename, String bothstring){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(tablename,null,"FullName like ? or ShortName like ? or Mobile like ?",new String[]{bothstring + "%",bothstring + "%",bothstring + "%"},null,null,null);
        return c;
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion ,int newVersion){

    }


}
