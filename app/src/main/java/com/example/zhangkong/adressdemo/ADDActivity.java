package com.example.zhangkong.adressdemo;

import android.app.Activity;
import android.content.ContentValues;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleExpandableListAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangKong on 2015/5/18.
 */
public class ADDActivity extends ActionBarActivity {
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
    private Cursor c;
    private Button companybutton;
    private Button holidaybutton;
    private Button workdaybutton;
    private Button configbutton;
    private CompanyFragment companyfragment;
    private HolidayFragment holidayfragment;
    private WorkdayFragment workdayfragment;
    private ConfigFragment configfragment;

    private ViewPager myviewpager;


   private FragmentManager fm;

    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlayout);
        LinearLayout searchlayout = (LinearLayout)findViewById(R.id.searchlayout);
        searchlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ADDActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        myviewpager = (ViewPager)findViewById(R.id.myviewpager);
        companyfragment = new CompanyFragment();
        holidayfragment = new HolidayFragment();
        workdayfragment = new WorkdayFragment();
        configfragment = new ConfigFragment();

        mFragments.add(companyfragment);
        mFragments.add(holidayfragment);
        mFragments.add(workdayfragment);
        mFragments.add(configfragment);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount()
            {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0)
            {
                return mFragments.get(arg0);
            }
        };
        myviewpager.setAdapter(mAdapter);
        myviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
//                FragmentTransaction transaction = fm.beginTransaction();
//                // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
//                hideFragments(transaction);
                switch (i){
                    case 0:
                        companybutton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        holidaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        workdaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        configbutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        break;
                    case 1:
                        holidaybutton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        companybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        workdaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        configbutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        break;
                    case 2:
                        workdaybutton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        holidaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        companybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        configbutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        break;
                    case 3:
                        configbutton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        holidaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        workdaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        companybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


         companybutton = (Button)findViewById(R.id.company);
        companybutton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        holidaybutton = (Button)findViewById(R.id.holiday);
        workdaybutton = (Button)findViewById(R.id.workday);
        configbutton = (Button)findViewById(R.id.config);
        companybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companybutton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                holidaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                workdaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                configbutton.setBackgroundColor(getResources().getColor(R.color.gray));
                myviewpager.setCurrentItem(0);

            }
        });
        holidaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holidaybutton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                companybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                workdaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                configbutton.setBackgroundColor(getResources().getColor(R.color.gray));
                myviewpager.setCurrentItem(1);
            }
        });
        workdaybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workdaybutton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                holidaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                companybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                configbutton.setBackgroundColor(getResources().getColor(R.color.gray));
                myviewpager.setCurrentItem(2);
            }
        });
        configbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configbutton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                holidaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                workdaybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                companybutton.setBackgroundColor(getResources().getColor(R.color.gray));
                myviewpager.setCurrentItem(3);
            }
        });
    }

}
