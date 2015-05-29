package com.example.zhangkong.adressdemo;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;


import com.example.zhangkong.adressdemo.Util.MyDBHelper;

import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangKong on 2015/5/11.
 */
public class ShowActivity extends Activity  {
    private String strvalue = "";
    private String accesskey = "";
    private String userId = "";
    public static HttpClient httpclient;
    private String companyresult = "";
    private String contactresult = "";
    private Handler handler;
    private SimpleExpandableListAdapter adapter;
    private ExpandableListView listView;
    private MyDBHelper myhelper;
    private int i = 0;
    private int j = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showlayout);
        String departmentId = getIntent().getStringExtra("MSG").toString();
        myhelper = new MyDBHelper(this);
        Cursor cursor = myhelper.query("companytable",departmentId);
        List<Map<String,String>> departmentlist = new ArrayList<Map<String,String>>();
        List<List<Map<String,String>>> child = new ArrayList<List<Map<String,String>>>();

       if(cursor.moveToFirst()){
           boolean ishavenext = !cursor.isLast();
           while(ishavenext){
               Map<String,String> departmentmap = new HashMap<String,String>();
               departmentmap.put("Department",cursor.getString(1));
               departmentlist.add(departmentmap);
               List<Map<String,String>> childdata = new ArrayList<Map<String,String>>();
               Cursor sondepartmentcursor = myhelper.query("companytable",cursor.getString(2));

               ++i;
               Log.v("MSG","一级部门：" + cursor.getString(1) + "第" + i + "个一级部门");
               if(sondepartmentcursor.moveToFirst()){
                        boolean isgoon = !sondepartmentcursor.isLast();
                   while ( isgoon) {
                       String contactstring = sondepartmentcursor.getString(2);
                       Log.v("MSG","二级部门" + sondepartmentcursor.getString(1));

                       Cursor soncursor = myhelper.contactquery("contacttable",contactstring);

                       if(soncursor.moveToFirst()){
                           boolean isContinue =!soncursor.isLast();
                           while(isContinue){
                               Map<String,String> map = new HashMap<String,String>();

                               ++j;
                               map.put("Name","姓名：" + soncursor.getString(1));
                               map.put("Code","工号：" + soncursor.getString(2));
                               Log.v("MSG","员工名称：" + "第" + j + " 个员工" +   soncursor.getString(1));
                               childdata.add(map);
                               // soncursor.moveToNext();
                               isContinue = soncursor.moveToNext();

                           }
                       }
                      isgoon =  sondepartmentcursor.moveToNext();
                   }
                   /*while(!sondepartmentcursor.isLast()){
                       String contactstring = sondepartmentcursor.getString(2);
                       Log.v("MSG","二级部门" + sondepartmentcursor.getString(1));
                       sondepartmentcursor.moveToNext();
                       Cursor soncursor = myhelper.contactquery("contacttable",contactstring);

                       if(soncursor.moveToFirst()){
                           while(!soncursor.isLast()){
                               Map<String,String> map = new HashMap<String,String>();

                               ++j;
                               map.put("Name","姓名：" + soncursor.getString(1));
                               map.put("Code","工号：" + soncursor.getString(2));
                               Log.v("MSG","员工名称：" + "第" + j + " 个员工" +   soncursor.getString(1));
                               childdata.add(map);
                               soncursor.moveToNext();


                           }
                       }
                   }*/
               }

               Cursor peoplecursor = myhelper.contactquery("contacttable",cursor.getString(2));
                    Log.v("MSG","没有二级部门的一级部门" + cursor.getString(2));
               if(peoplecursor.moveToFirst()){

                   while(!peoplecursor.isLast()){

                       Map<String,String> map = new HashMap<String,String>();
                       map.put("Name","姓名：" + peoplecursor.getString(1));
                       map.put("Code","工号：" + peoplecursor.getString(2));
                       childdata.add(map);
                       peoplecursor.moveToNext();
                   }

               }
               ishavenext = cursor.moveToNext();
               child.add(childdata);


           }
       }

       ExpandableListView mylistview = (ExpandableListView)findViewById(R.id.mylistview);
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(this,departmentlist,android.R.layout.simple_expandable_list_item_1,new String[]{"Department"},new int[]{android.R.id.text1},child,R.layout.simple_expandable_list_item_2,new String[]{"Name","Code"},new int[]{android.R.id.text1,android.R.id.text2});
        mylistview.setAdapter(adapter);



    }


}
