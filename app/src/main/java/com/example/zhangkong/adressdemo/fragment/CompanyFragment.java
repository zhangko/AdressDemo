package com.example.zhangkong.adressdemo.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.zhangkong.adressdemo.Util.MyDBHelper;
import com.example.zhangkong.adressdemo.MyListActivity;
import com.example.zhangkong.adressdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangKong on 2015/5/25.
 */
public class CompanyFragment extends Fragment {
    private MyDBHelper myhelper;
    private Cursor c;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frag_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myhelper = new MyDBHelper(getActivity());
        c = myhelper.companyquery("companytable");
        final List<String> companyId = new ArrayList<String>();
        int number = c.getCount();
        Log.v("MSG", "一共有子公司：" + number + "个");
        c.moveToFirst();
        while (!c.isLast()){
            companyId.add(c.getString(2));
            c.moveToNext();
        }
        String str =  companyId.toString();
        Log.v("MSG","公司ID字符串：" + str);
        ListView listView = (ListView)getActivity().findViewById(R.id.fraglistview);
        //  final ListView listviewdepart = (ListView)findViewById(R.id.departmentlist);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,c,new String[]{"Name"},new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c.moveToPosition(position);
                String departmentId = c.getString(2);
                Intent intent = new Intent(getActivity(),MyListActivity.class);
                intent.putExtra("MSG",departmentId);
                startActivity(intent);
            }
        });



    }


}
