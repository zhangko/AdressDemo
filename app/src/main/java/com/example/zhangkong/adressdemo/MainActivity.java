package com.example.zhangkong.adressdemo;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zhangkong.adressdemo.Util.HttpsClient;
import com.example.zhangkong.adressdemo.Util.MD5Util;
import com.example.zhangkong.adressdemo.Util.MyDBHelper;
import com.example.zhangkong.adressdemo.Util.Person;

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


import java.net.HttpURLConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.*;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;


public class MainActivity extends ActionBarActivity {


    private String username;
    private String password;
    private String pwd;
    private String  result = "";
    public static HttpClient httpclient;
    public HttpURLConnection urlConnection;
    private EditText useredit;
    private EditText passwordedit;
    private android.os.Handler handler;
    private MyDBHelper myhelper;
    private String companyresult = "";
    private String contactresult = "";
    private String accesskey = "";
    private String userId = "";
    private ProgressDialog mDialog1;
    private static final int DIALOG1_KEY = 0;
    private int index = 0;
    private SharedPreferences sp;
    private boolean flag ;
    private boolean loginflag;
    private SharedPreferences.Editor editor;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait while loading...");
        dialog.setIndeterminate(true);
       dialog.setCancelable(true);
        sp = getSharedPreferences("config",MODE_PRIVATE);
         editor = sp.edit();
        flag = sp.getBoolean("readmode",false);
        httpclient = HttpsClient.newHttpsClient();
        myhelper = new MyDBHelper(this);
         useredit = (EditText)findViewById(R.id.username);
         passwordedit = (EditText)findViewById(R.id.password);
        useredit.setText(sp.getString("username",""));
        passwordedit.setText(sp.getString("password",""));
        loginflag = sp.getBoolean("login",false);
        if(loginflag){
            Intent intent = new Intent(MainActivity.this,ADDActivity.class);
            startActivity(intent);
        }
        Button loginbutton = (Button)findViewById(R.id.login);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = useredit.getText().toString();
                password = passwordedit.getText().toString();
                editor.putString("username",username);
                editor.putString("password",password);
                editor.commit();
                pwd = MD5Util.get32MD5Capital(passwordedit.getText().toString(), MD5Util.UTF_16LE);
                Log.v("MSG",pwd);
              //  showDialog(DIALOG1_KEY);
                dialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(!flag){
                            login();
                            getcontact();
                            getinfo();
                            editor.putBoolean("readmode",true);
                            editor.putBoolean("login",true);
                            editor.commit();
                            Intent intent = new Intent(MainActivity.this,ADDActivity.class);
                            dialog.cancel();
                            startActivity(intent);

                        }
                       else{
                            Intent intent = new Intent(MainActivity.this,ADDActivity.class);
                            editor.putBoolean("login",true);
                            editor.commit();
                            dialog.cancel();
                            startActivity(intent);

                        }

                    }
                }).start();
            }
        });
   }


    public void login(){
        String target = "https://service.oa.jiuan-roa.com/MobileOfficeAutomationAPI/LoginWeb.ashx";
       //发送网络请求
        HttpPost httprequest = new HttpPost(target);
        String targetparam = "{" + "\"" + "LogonName" + "\""  + ":" +"\""  + username +  "\""  +"," + " \"" + "PassWord" + "\"" + ":" +  "\"" + pwd + "\"" + "}";
        Log.v("MSG",targetparam);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        BasicNameValuePair pair = new BasicNameValuePair("content",targetparam);
        Log.d("发送信息",""+pair.toString());
        params.add(new BasicNameValuePair("content",targetparam));
        try{
            httprequest.setEntity(new UrlEncodedFormEntity(params));
                //执行网络请求，并得到返回结果
            HttpResponse httpResponse = MainActivity.httpclient.execute(httprequest);
            if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                result = EntityUtils.toString(httpResponse.getEntity());
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    String resultvalue = jsonObject.getString("ReturnValue");
                    JSONObject jsonvalue = new JSONObject(resultvalue);
                    accesskey =jsonvalue.getString("AccessKey");
                    userId = jsonvalue.getString("OAfUserID");
                    editor.putString("accesskey",accesskey);
                    editor.putString("userId",userId);
                    editor.commit();
                    int numbers = jsonvalue.length();
                    JSONArray namearray = jsonvalue.names();
                    ArrayList<HashMap<String,Object>>  listitem = new ArrayList<HashMap<String, Object>>();
                    for(int i = 0 ; i < numbers - 1; i++){
                        HashMap<String , Object> map = new  HashMap<String ,Object>();
                        map.put(namearray.getString(i),jsonvalue.getString(namearray.getString(i)));
                        listitem.add(map);
                    }
                    Log.v("MSG",listitem.get(0).toString());
                    Log.v("MSG",resultvalue);
                }catch (JSONException e){
                   e.printStackTrace();
                }
                Log.v("MSG",result);
            }else{
                result = "请求失败";
            }
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch(ClientProtocolException e) {
           e.printStackTrace();
        }  catch(IOException e){
            e.printStackTrace();
        }
        Log.v("MSG",result);
    }
    public void getcontact() {
        String target = "https://service.oa.jiuan-roa.com/HumanResourcesAPI/EmployeeWeb.ashx";
        HttpPost httpPost = new HttpPost(target);
        String targetparam = "{" + "\"" + "AccessKey" + "\"" + ":" + "\"" + accesskey + "\"" + "," + " \"" + "OAfUserID" + "\"" + ":" + "\"" + userId + "\"" + "}";
        Log.v("MSG", targetparam);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        BasicNameValuePair pair = new BasicNameValuePair("content", targetparam);
        Log.d("发送信息", "" + pair.toString());
        params.add(new BasicNameValuePair("content", targetparam));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = MainActivity.httpclient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                contactresult = EntityUtils.toString(httpResponse.getEntity());
            }
            else{
                contactresult = " request failed!!!";
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }catch(ClientProtocolException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        Log.v("MSG",contactresult);
        try{
            JSONObject contactobject =  new JSONObject(contactresult);
            String personstring = contactobject.getString("ReturnValue");
            JSONArray personarray = new JSONArray(personstring);
            int numbers = personarray.length();
            Log.v("发送人数：","  " + numbers);

            ArrayList<Person> personlist = new ArrayList<Person>();
            ArrayList<HashMap<String,String>>  personarraylist = new ArrayList<HashMap<String,String>>();
            for(int i = 0; i < numbers -1 ;i++){
                Person person = new Person();
                HashMap<String,String> map = new HashMap<String , String>();
                JSONObject personobject = personarray.getJSONObject(i);

                ContentValues values = new ContentValues();
                values.put("Name",personobject.getString("Name"));
                values.put("Code",personobject.getString("Code"));
                values.put("ID",personobject.getString("ID"));
                values.put("DepartmentID",personobject.getString("DepartmentID"));
                values.put("DepartmentCode",personobject.getString("DepartmentCode"));
                values.put("Mobile",personobject.getString("Mobile"));
                values.put("Telephone",personobject.getString("Telephone"));
                values.put("Email",personobject.getString("Email"));
                values.put("FullName",getFullPinYin(personobject.getString("Name")));
                values.put("ShortName",getPinyin(personobject.getString("Name")));
                myhelper.insert("contacttable",values);

                index++;
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        Log.v("MSG","员工人数：" +  index + "个。");
        Log.v("MSG","员工数据库生成成功!!!");
    }
    public void getinfo() {
        String target = "https://service.oa.jiuan-roa.com/HumanResourcesAPI/DepartmentWeb.ashx";
        HttpPost httpPost = new HttpPost(target);
        String targetparam = "{" + "\"" + "AccessKey" + "\"" + ":" + "\"" + accesskey + "\"" + "," + " \"" + "OAfUserID" + "\"" + ":" + "\"" + userId + "\"" + "}";
        Log.v("MSG", targetparam);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        BasicNameValuePair pair = new BasicNameValuePair("content", targetparam);
        Log.d("发送信息", "" + pair.toString());
        params.add(new BasicNameValuePair("content", targetparam));
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = MainActivity.httpclient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                companyresult = EntityUtils.toString(httpResponse.getEntity());
            }
            else{
                companyresult = " request failed!!!";
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }catch(ClientProtocolException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        Log.v("MSG",companyresult);
        try{
            JSONObject companyobject = new JSONObject(companyresult);
            String companystr = companyobject.getString("ReturnValue");
            JSONArray companyarray = new JSONArray(companystr);
            int departmentnum = companyarray.length();
            Log.v("MSG","the number of department is:" + departmentnum);
            List<Map<String,String>> department = new ArrayList<Map<String,String>>();
            List<List<Map<String,String>>> child = new ArrayList<List<Map<String, String>>>();
            for(int index = 0; index < departmentnum - 1; index++){
//                List<Map<String,String>> childdata = new ArrayList<Map<String,String>>();
//                Map<String,String>  map = new HashMap<String ,String>();
//                map.put("department",companyarray.getJSONObject(index).getString("Name"));
//                department.add(map);
                ContentValues values = new ContentValues();
                values.put("Name",companyarray.getJSONObject(index).getString("Name"));

                values.put("Code",companyarray.getJSONObject(index).getString("Code"));
                values.put("ID",companyarray.getJSONObject(index).getString("ID"));
                values.put("ParentID",companyarray.getJSONObject(index).getString("ParentID"));
                values.put("Abbre",companyarray.getJSONObject(index).getString("Abbreviation"));
                values.put("IsCompany",companyarray.getJSONObject(index).getInt("IsCompany"));
                myhelper.insert("companytable",values);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        Log.v("MSG","公司组织架构数据库生成成功");
    }

    public String getPinyin(String chinese){
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] _t = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (_t != null) {
                        pybf.append(_t[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();

    }
    public String getFullPinYin(String chinese){
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
