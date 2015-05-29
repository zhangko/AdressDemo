package com.example.zhangkong.adressdemo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangKong on 2015/5/26.
 */
public class SearchActivity extends ActionBarActivity implements SearchView.OnQueryTextListener{
    private ListView mylistview;
    private MyDBHelper myhelper;
    // private Cursor cursor;
    private String department;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlayout);
        myhelper = new MyDBHelper(this);
        Button button = (Button)findViewById(R.id.cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
         mylistview = (ListView)findViewById(R.id.searchlistview);
        SearchView mysearchview = (SearchView)findViewById(R.id.seachview);
        mysearchview.setOnQueryTextListener(this);
        mysearchview.setIconifiedByDefault(false);
      //  mSearchView.setOnQueryTextListener(this);
        mysearchview.setSubmitButtonEnabled(false);
        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*cursor.moveToPosition(position);
                String code = cursor.getString(2);*/
                if (TextUtils.isEmpty(code)){
                    return;
                }
                Intent intent = new Intent(SearchActivity.this,PeopleActivity.class);
                intent.putExtra("MSG",code);
                startActivity(intent);
            }
        });
    }

    private String code;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (myhelper != null)
            myhelper.close();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if(TextUtils.isEmpty(newText)){
          mylistview.setAdapter(null);
            return true;
        }


            String query = newText.toString();
//        if(stringisnumber(query)){
//            cursor = myhelper.telephonequery("contacttable",query);
//        }else {
//            cursor = myhelper.namequery("contacttable",query);
//        }

        Cursor cursor = myhelper.bothquery("contacttable",query);
        List<Map<String,String>> listitem = new ArrayList<Map<String,String>>();
        if(cursor.moveToFirst())
        {
            boolean flag = true;

            while(flag){
                Cursor departmentcursor = myhelper.departmentquery("companytable",cursor.getString(4));
                Map<String,String> map = new HashMap<String,String>();
                if(departmentcursor.moveToFirst()){
                     department = departmentcursor.getString(1);
                }

                code = cursor.getString(2);
                map.put("name","姓名：" + cursor.getString(1));
                map.put("telephone", "电话：" + cursor.getString(6));
                map.put("department","部门：" + department);
                listitem.add(map);
               flag =  cursor.moveToNext();
                if (departmentcursor != null)
                    departmentcursor.close();
            }
        }
        if (cursor != null)
            cursor.close();

        SimpleAdapter adapter = new SimpleAdapter(this,listitem,R.layout.listitemlayout,new String[]{"name","telephone","department"},new int[]{R.id.nametext,R.id.telephone,R.id.worknumber});
        mylistview.setAdapter(adapter);

            return true;
    }
    private boolean stringisnumber(String str){

        int number = str.length();
        for(int i = 0 ; i < number ; i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
            return true;

    }
}
