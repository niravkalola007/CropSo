package com.nkdroid.cropso.ProjectManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nkdroid.cropso.R;

import java.util.ArrayList;

public class PmAddNewEmployee extends ActionBarActivity {

    private Toolbar toolbar;
    private ListView employee_task_list;
    private ProjectListAdapter projectListAdapter;
    private ArrayList<String> projectList;
    private TextView txtAddNewEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm_add_new_employee);
        employee_task_list= (ListView) findViewById(R.id.employee_task_list);
        txtAddNewEmployee= (TextView) findViewById(R.id.txtAddNewEmployee);
        txtAddNewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PmAddNewEmployee.this,PmAddNewEmployee.class);
                startActivity(intent);
            }
        });
        setActionBar();
        projectList =new ArrayList<>();
        projectList.add("a");
        projectList.add("b");
        projectList.add("c");
        projectList.add("d");
        projectList.add("e");
        projectList.add("f");
        projectListAdapter = new ProjectListAdapter(PmAddNewEmployee.this, projectList);
        employee_task_list.setAdapter(projectListAdapter);
    }
    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_launcher);
            toolbar.setTitle("CropSo");
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class ProjectListAdapter extends BaseAdapter {

        private ArrayList<String> redeemList;
        private Context context;
        private LayoutInflater mInflater;
        private ViewHolder holder;

        public ProjectListAdapter(FragmentActivity activity, ArrayList<String> redeemGiftCodesList) {
            this.context = activity;
            this.redeemList = redeemGiftCodesList;
        }

        @Override
        public int getCount() {

            return redeemList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
//            ImageView img_edit,img_delete;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_emp_remove, parent, false);

                holder = new ViewHolder();
//                holder.img_edit= (ImageView) convertView.findViewById(R.id.img_edit);
//                holder.img_delete= (ImageView) convertView.findViewById(R.id.img_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

    }



}
