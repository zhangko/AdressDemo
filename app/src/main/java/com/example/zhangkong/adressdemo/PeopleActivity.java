package com.example.zhangkong.adressdemo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhangkong.adressdemo.Util.MyDBHelper;

/**
 * Created by ZhangKong on 2015/5/21.
 */
public class PeopleActivity extends ActionBarActivity {
    private MyDBHelper myhelper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personinformation);
        myhelper = new MyDBHelper(this);
        String name = "";
        String number = "";
        String telephone = ""  ;
        String phone = "" ;
        String email = "" ;
        String code = getIntent().getStringExtra("MSG").toString();
        Cursor cursor = myhelper.codequery("contacttable",code);
        if(cursor.moveToFirst()){
             name =  cursor.getString(1);
           number = cursor.getString(2);
          telephone = cursor.getString(6);
             phone =  cursor.getString(7);
           email =  cursor.getString(8);
        }

        TextView nametext = (TextView)findViewById(R.id.name);
        nametext.setText(name);
        TextView numbertext = (TextView)findViewById(R.id.number);
        numbertext.setText(number);
        TextView teleText = (TextView)findViewById(R.id.telephone);
        teleText.setText(telephone);
        TextView phoneText = (TextView)findViewById(R.id.phone);
        phoneText.setText(phone);
        TextView emainText = (TextView)findViewById(R.id.email);
        emainText.setText(email);
        Button backbutton = (Button)findViewById(R.id.fanhui);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PeopleActivity.this.finish();
            }
        });








    }
}
