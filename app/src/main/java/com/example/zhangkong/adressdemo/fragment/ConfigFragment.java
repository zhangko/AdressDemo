package com.example.zhangkong.adressdemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zhangkong.adressdemo.MainActivity;
import com.example.zhangkong.adressdemo.R;

/**
 * Created by ZhangKong on 2015/5/25.
 */
public class ConfigFragment extends Fragment {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment3, container, false);
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = sp.edit();
        Button button = (Button)getActivity().findViewById(R.id.zhuxiao);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MainActivity.class);
                editor.putBoolean("login",false);
                editor.commit();
                startActivity(intent);
            }
        });

    }
}
