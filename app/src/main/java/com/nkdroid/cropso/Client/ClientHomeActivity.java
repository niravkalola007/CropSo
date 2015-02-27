package com.nkdroid.cropso.Client;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nkdroid.cropso.LoginActivity;
import com.nkdroid.cropso.R;

import java.util.ArrayList;

public class ClientHomeActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private ListView client_project_list;
    private ProjectListAdapter projectListAdapter;
    private ArrayList<String> projectList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        client_project_list= (ListView) findViewById(R.id.client_project_list);
        setActionBar();
        projectList=new ArrayList<>();
        projectList.add("a");
        projectList.add("b");
        projectList.add("c");
        projectList.add("d");
        projectList.add("e");
        projectList.add("f");
        projectListAdapter = new ProjectListAdapter(ClientHomeActivity.this, projectList);
        client_project_list.setAdapter(projectListAdapter);
    }

    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("CropSo");
            toolbar.setSubtitle("Hi! Client");
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent=new Intent(ClientHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_project_list, parent, false);

                holder = new ViewHolder();

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ClientHomeActivity.this,ClientProjectDetailActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }
}
