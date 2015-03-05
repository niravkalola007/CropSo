package com.nkdroid.cropso.ProjectManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nkdroid.cropso.R;

import java.util.ArrayList;

public class PmAllEmployeeListActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private ListView admin_project_list;
    private ProjectListAdapter projectListAdapter;
    private ArrayList<String> projectList;
    private TextView txt_add_projectmanager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm_all_employee_list);
        admin_project_list = (ListView) findViewById(R.id.pm_all_resource_list);
        txt_add_projectmanager= (TextView) findViewById(R.id.txt_add_project);
        txt_add_projectmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PmAllEmployeeListActivity.this,PmAllAddNewEmployee.class);
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
        projectListAdapter = new ProjectListAdapter(PmAllEmployeeListActivity.this, projectList);
        admin_project_list.setAdapter(projectListAdapter);
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
            ImageView img_edit,img_delete;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_project_edit_delete, parent, false);

                holder = new ViewHolder();
                holder.img_edit= (ImageView) convertView.findViewById(R.id.img_edit);
                holder.img_delete= (ImageView) convertView.findViewById(R.id.img_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(PmAllEmployeeListActivity.this,PmAllResourceEditableDetailActivity.class);
                    startActivity(intent);
                }
            });

            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(PmAllEmployeeListActivity.this);
                    alert.setTitle("Delete");
                    alert.setMessage("Delete this record ?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });

                    alert.show();
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(PmAllEmployeeListActivity.this,PmAllEmployeeDetailActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }
}
