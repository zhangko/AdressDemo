package com.example.zhangkong.adressdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhangkong.adressdemo.Util.MyDBHelper;
import com.example.zhangkong.adressdemo.Util.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhangKong on 2015/5/21.
 */
public class MyListActivity extends Activity{
    private MyDBHelper myhelper;
    private ArrayList<TreeNode> nodelist = new ArrayList<TreeNode>();
    private TreeViewAdapter adapter = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listlayout);
        Button backbutton = (Button)findViewById(R.id.back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyListActivity.this.finish();
            }
        });
        LinearLayout mylinearlayout = (LinearLayout)findViewById(R.id.searchviewlayout);
        mylinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyListActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

        String departmentId = getIntent().getStringExtra("MSG").toString();
        myhelper = new MyDBHelper(this);
        // 一级部门的游标 firstcursor
        Cursor firstcursor = myhelper.query("companytable",departmentId);
        if(firstcursor.moveToFirst()){
            boolean firstflag = true;
            while(firstflag){
                TreeNode tempnode = new TreeNode();
                tempnode.setName(firstcursor.getString(1));
                tempnode.setHaveChild(true);
                tempnode.setLevel(1);
                tempnode.setExpanded(false);
                tempnode.setDepartmenID(firstcursor.getString(2));
                tempnode.setHaveParent(false);
                tempnode.setID(firstcursor.getString(2));
                tempnode.setParent("");
                nodelist.add(tempnode);

                firstflag = firstcursor.moveToNext();
            }
        }
        ListView mylistview = (ListView)findViewById(R.id.treelist);
        adapter = new TreeViewAdapter(this,R.layout.outline,nodelist);
        mylistview.setAdapter(adapter);
        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(nodelist.get(position).isHaveChild()){
                    if(nodelist.get(position).isExpanded()){
                        nodelist.get(position).setExpanded(false);
                        TreeNode temptreenode = nodelist.get(position);
                        ArrayList<TreeNode> temp =  new ArrayList<TreeNode>();
                        for(int i = position + 1; i< nodelist.size();i++){
                            if( temptreenode.getLevel() >= nodelist.get(i).getLevel() ){
                                break;
                            }
                            temp.add(nodelist.get(i));
                        }
                        nodelist.removeAll(temp);
                        adapter.notifyDataSetChanged();



                    }
                    else{
                        nodelist.get(position).setExpanded(true);
                        int level = nodelist.get(position).getLevel();
                        int nextlevel = level + 1;
                        Cursor cursor = myhelper.query("companytable",nodelist.get(position).getDepartmenID());
                        if(cursor.getCount() != 0){
                            if(cursor.moveToFirst()){
                                boolean secondflag = true;
                                int i = 1;
                                while (secondflag){

                                    TreeNode templistnode = new TreeNode();
                                    templistnode.setName(cursor.getString(1));
                                    templistnode.setHaveChild(true);
                                    templistnode.setLevel(nextlevel);
                                    templistnode.setExpanded(false);
                                    templistnode.setDepartmenID(cursor.getString(2));
                                    templistnode.setHaveParent(true);
                                    templistnode.setID(cursor.getString(2));
                                    templistnode.setParent(cursor.getString(2));
                                    nodelist.add(position + 1 ,templistnode);
                                    ++i;
                                    secondflag = cursor.moveToNext();
                                }
                            }
                        Cursor contactcursor = myhelper.contactquery("contacttable",nodelist.get(position).getDepartmenID());
                            if(contactcursor.moveToFirst()){
                                boolean contactflag = true;
                                int i = 1;
                                while(contactflag){
                                    TreeNode templistnode = new TreeNode();
                                    templistnode.setName(contactcursor.getString(1));
                                    templistnode.setHaveChild(false);
                                    templistnode.setLevel(nextlevel);
                                    templistnode.setExpanded(false);
                                    templistnode.setDepartmenID(contactcursor.getString(4));
                                    templistnode.setHaveParent(true);
                                    templistnode.setID(contactcursor.getString(2));
                                    templistnode.setParent(contactcursor.getString(2));
                                    templistnode.setCode(contactcursor.getString(2));
                                    nodelist.add(position + 1 ,templistnode);
                                    ++i;
                                    contactflag = contactcursor.moveToNext();

                                }
                            }

                        }
                        else{
                            Cursor peoplecursor = myhelper.contactquery("contacttable",nodelist.get(position).getDepartmenID());
                            if(peoplecursor.moveToFirst()){
                                boolean peopleflag = true;
                                int i = 1;
                                while(peopleflag){

                                    TreeNode templistnode = new TreeNode();
                                    templistnode.setName(peoplecursor.getString(1));
                                    templistnode.setHaveChild(false);
                                    templistnode.setLevel(nextlevel);
                                    templistnode.setExpanded(false);
                                    templistnode.setDepartmenID(peoplecursor.getString(4));
                                    templistnode.setHaveParent(true);
                                    templistnode.setID(peoplecursor.getString(2));
                                    templistnode.setParent(peoplecursor.getString(2));
                                    templistnode.setCode(peoplecursor.getString(2));
                                    nodelist.add(position + 1 ,templistnode);
                                    ++i;
                                    peopleflag = peoplecursor.moveToNext();

                                }
                            }

                        }

                        adapter.notifyDataSetChanged();



                    }
                }
                else{
                    String peoplecode = nodelist.get(position).getCode();
                    Intent intent = new Intent(MyListActivity.this,PeopleActivity.class);
                    intent.putExtra("MSG",peoplecode);
                    startActivity(intent);

                }


            }
        });

    }
    private class TreeViewAdapter extends ArrayAdapter {

        private LayoutInflater mInflater;
        private List<TreeNode> mfilelist;
        private Bitmap mIconCollapse;
        private Bitmap mIconExpand;

        public TreeViewAdapter(Context context, int textViewResourceId,
                               List objects) {
            super(context, textViewResourceId, objects);
            mInflater = LayoutInflater.from(context);
            mfilelist = objects;
            mIconCollapse = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.outline_list_collapse);
            mIconExpand = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.outline_list_expand);

        }

        public int getCount() {
            return mfilelist.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
			/*if (convertView == null) {*/
            convertView = mInflater.inflate(R.layout.outline, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
			/*} else {
				holder = (ViewHolder) convertView.getTag();
			}*/

            int level = mfilelist.get(position).getLevel();
            holder.icon.setPadding(45 * (level + 1), holder.icon
                    .getPaddingTop(), 0, holder.icon.getPaddingBottom());
            holder.text.setText(mfilelist.get(position).getName());
            if (mfilelist.get(position).isHaveChild()
                    && (mfilelist.get(position).isExpanded() == false)) {
                holder.icon.setImageBitmap(mIconCollapse);
            } else if (mfilelist.get(position).isHaveChild()
                    && (mfilelist.get(position).isExpanded() == true)) {
                holder.icon.setImageBitmap(mIconExpand);
            } else if (!mfilelist.get(position).isHaveChild()){
                holder.icon.setImageBitmap(mIconCollapse);
                holder.icon.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView text;
            ImageView icon;

        }
    }
}
