package com.example.zhangkong.adressdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import java.util.Date;
import java.util.List;

/**
 * Created by ZhangKong on 2015/5/25.
 */
public class HolidayFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private String accesskey;
    private String userId;
    private String  holidaytresult;
    private String subtime;
    private String holidaynumber;
    private TextView holidaytext;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment1, container, false);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        long time = new Date().getTime();
        final String timestring = String.valueOf(time);
       subtime = timestring.substring(0,9);
        Log.v("MSG","当前时间戳：" + subtime);
        sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
         accesskey = sharedPreferences.getString("accesskey","");
         userId = sharedPreferences.getString("userId","");
        Log.v("MSG","Key值" + accesskey);
        Log.v("MSG", "用户：" + userId);
        holidaytext = (TextView)getActivity().findViewById(R.id.holidaytext);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String target = "https://service.oa.jiuan-roa.com/MobileOfficeAutomationAPI/YearHolidayWeb.ashx";
                HttpPost httpPost = new HttpPost(target);
                String targetparam = "{" + "\"" + "AccessKey" + "\"" + ":" + "\"" + accesskey + "\"" + "," + "\"" + "DateTime" + "\"" + ":" + "\"" + subtime + "\"" + "," + " \"" + "OAfUserID" + "\"" + ":" + "\"" + userId + "\"" + "}";
                Log.v("MSG", targetparam);
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                BasicNameValuePair pair = new BasicNameValuePair("content", targetparam);
                Log.d("发送信息", "" + pair.toString());
                params.add(new BasicNameValuePair("content", targetparam));
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse httpResponse = MainActivity.httpclient.execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        holidaytresult = EntityUtils.toString(httpResponse.getEntity());
                    } else {
                        holidaytresult = " request failed!!!";
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.v("MSG", holidaytresult);
                try {
                    JSONObject contactobject = new JSONObject(holidaytresult);
                    String personstring = contactobject.getString("ReturnValue");
                    JSONObject holidayobject = new JSONObject(personstring);
                    String name = holidayobject.getString("EmployeeName");
                    String totalday = holidayobject.getString("YearHolidayNumber");
                    String leftday = holidayobject.getString("OverYearHolidayNumber");
                    holidaynumber = "您好：" +  name + "!"+ "\n" + "您共有年假：" + totalday + "天," + "剩余年假数：" + leftday + "天.";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.v("MSG","年假信息：" + holidaynumber);
                holidaytext.post(new Runnable() {
                    @Override
                    public void run() {
                        holidaytext.setText(holidaynumber);
                    }
                });
            }
        }).start();


   }
}
