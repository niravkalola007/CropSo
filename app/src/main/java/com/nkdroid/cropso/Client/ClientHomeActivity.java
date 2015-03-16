package com.nkdroid.cropso.Client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.google.gson.GsonBuilder;
import com.nkdroid.cropso.Custom.AppConstants;
import com.nkdroid.cropso.LoginActivity;
import com.nkdroid.cropso.R;
import com.nkdroid.cropso.model.NotificationList;
import com.nkdroid.cropso.model.PrefUtils;
import com.nkdroid.cropso.model.Project;
import com.nkdroid.cropso.model.ProjectList;
import com.nkdroid.cropso.model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ClientHomeActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private ListView client_project_list;
    private ProjectListAdapter projectListAdapter;
    private ArrayList<Project> projectList;
    private static InputStream is = null;
    private int code;
    private String json = null;
    private ProgressDialog progressDialog;
    private Project project;
    ProjectList projectListClass;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);
        client_project_list= (ListView) findViewById(R.id.client_project_list);
        user=PrefUtils.getUser(ClientHomeActivity.this);
        setActionBar();
        getProjectList();

    }
    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_launcher);
            toolbar.setTitle("Online Shopping Cart");
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

        private ArrayList<Project> redeemList;
        private Context context;
        private LayoutInflater mInflater;
        private ViewHolder holder;

        public ProjectListAdapter(FragmentActivity activity, ArrayList<Project> redeemGiftCodesList) {
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
            TextView txtProjectTile,txtSrartDate,txtEndDate;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_project_list, parent, false);

                holder = new ViewHolder();
                holder.txtProjectTile= (TextView)convertView. findViewById(R.id.txtProjectTile);
                holder.txtSrartDate= (TextView)convertView. findViewById(R.id.txtSrartDate);
                holder.txtEndDate= (TextView)convertView. findViewById(R.id.txtEndDate);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtProjectTile.setText(redeemList.get(position).name);
            holder.txtSrartDate.setText("Start Date: "+redeemList.get(position).start_date);
            holder.txtEndDate.setText("End Date: "+redeemList.get(position).deadline_date);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefUtils.setProject(ClientHomeActivity.this,redeemList.get(position));
                    Intent intent=new Intent(ClientHomeActivity.this,ClientProjectDetailActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }

    private void getProjectList() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(ClientHomeActivity.this);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Fetching projects...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                String response=getJsonStringfromUrl(AppConstants.CLIENT_PROJECTS+user.id);

                projectListClass=new GsonBuilder().create().fromJson(response,ProjectList.class);

                projectList=projectListClass.projectList;


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();

                projectListAdapter = new ProjectListAdapter(ClientHomeActivity.this, projectList);
                client_project_list.setAdapter(projectListAdapter);

            }
        }.execute();
    }

    public String getJsonStringfromUrl(String url) {

        try {

            StringBuilder builder = new StringBuilder();

            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            code = httpResponse.getStatusLine().getStatusCode();

            if (code == 200) {
                HttpEntity httpentity = httpResponse.getEntity();
                is = httpentity.getContent();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);

                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line + "\n");
                }
                is.close();
            }

            json = builder.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;

    }
}
