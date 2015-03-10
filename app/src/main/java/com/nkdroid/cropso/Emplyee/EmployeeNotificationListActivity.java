package com.nkdroid.cropso.Emplyee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.nkdroid.cropso.Custom.AppConstants;
import com.nkdroid.cropso.R;
import com.nkdroid.cropso.model.Notification;
import com.nkdroid.cropso.model.NotificationList;
import com.nkdroid.cropso.model.PrefUtils;
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

public class EmployeeNotificationListActivity extends ActionBarActivity {

    private ListView employee_notification_list;
    private ProjectListAdapter projectListAdapter;
    private NotificationList notificationListClass;
    private ArrayList<Notification> notificationList;
    private Toolbar toolbar;
    private static InputStream is = null;
    private int code;
    private String json = null;
    private ProgressDialog progressDialog;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_notification_list);
        employee_notification_list= (ListView) findViewById(R.id.employee_notification_list);
        setActionBar();


        user= PrefUtils.getUser(EmployeeNotificationListActivity.this);
        getNotificationList();

    }

    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_launcher);
            toolbar.setTitle("Notifications");
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

        private ArrayList<Notification> redeemList;
        private Context context;
        private LayoutInflater mInflater;
        private ViewHolder holder;

        public ProjectListAdapter(FragmentActivity activity, ArrayList<Notification> redeemGiftCodesList) {
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
            TextView txtNotificationTitle,txtNotificationDate,txtNotificationMessage;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_employee_notification_list, parent, false);

                holder = new ViewHolder();
                holder.txtNotificationTitle= (TextView) convertView.findViewById(R.id.txtNotificationTitle);
                holder.txtNotificationDate= (TextView) convertView.findViewById(R.id.txtNotificationDate);
                holder.txtNotificationMessage= (TextView) convertView.findViewById(R.id.txtNotificationMessage);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtNotificationTitle.setText(redeemList.get(position).title);
            holder.txtNotificationDate.setText(redeemList.get(position).created_date);
            holder.txtNotificationMessage.setText(redeemList.get(position).message);
            return convertView;
        }

    }

    private void getNotificationList() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(EmployeeNotificationListActivity.this);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Fetching notifications...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                String response=getJsonStringfromUrl(AppConstants.NOTIFICATIONS+user.id);
                notificationListClass=new GsonBuilder().create().fromJson(response,NotificationList.class);
                notificationList=notificationListClass.notificationList;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();

                projectListAdapter = new ProjectListAdapter(EmployeeNotificationListActivity.this, notificationList);
                employee_notification_list.setAdapter(projectListAdapter);

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
