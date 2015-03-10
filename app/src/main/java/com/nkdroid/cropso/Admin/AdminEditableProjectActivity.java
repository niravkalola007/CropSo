package com.nkdroid.cropso.Admin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nkdroid.cropso.Custom.AppConstants;
import com.nkdroid.cropso.R;
import com.nkdroid.cropso.model.PrefUtils;
import com.nkdroid.cropso.model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AdminEditableProjectActivity extends ActionBarActivity {
    private Toolbar toolbar;

    private EditText etFirstName,etLastName,etEmail,etMobile,etPostion;
    private TextView txtEditManager;
    private User projectmanager;
    private ProgressDialog progressDialog;
    private String method = "POST";
    private static InputStream is = null;
    private int code;
    private String json = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_editable_project);
        setActionBar();
        projectmanager= PrefUtils.getManager(AdminEditableProjectActivity.this);
        etFirstName= (EditText) findViewById(R.id.etFirstName);
        etFirstName.setText(projectmanager.fname);
        etLastName= (EditText) findViewById(R.id.etLastName);
        etLastName.setText(projectmanager.lname);
        etEmail= (EditText) findViewById(R.id.etEmail);
        etEmail.setText(projectmanager.mobile);

        etMobile= (EditText) findViewById(R.id.etMobile);
        etMobile.setText(projectmanager.mobile);
        etPostion= (EditText) findViewById(R.id.etPostion);
        etPostion.setText(projectmanager.designation);

        txtEditManager= (TextView) findViewById(R.id.txtEditManager);
        txtEditManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateManager();
            }
        });

    }

    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_launcher);
            toolbar.setTitle("Edit Project Manager");
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateManager() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(AdminEditableProjectActivity.this);
                progressDialog.setMessage("Updating Project Manager...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Making HTTP request
                    // check for request method
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("username",etEmail.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("id",projectmanager.id+""));
                    nameValuePairs.add(new BasicNameValuePair("fname",etFirstName.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("lname",etLastName.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("mobile",etMobile.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("registration_type","Project Manager"));
                    nameValuePairs.add(new BasicNameValuePair("designation",etPostion.getText().toString().trim()+""));
                    if (method.equals("POST")) {
                        // request method is POST
                        // defaultHttpClient
                        Log.e("name value", nameValuePairs + "");
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(AppConstants.UPDATE_USER_DETAILS);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();

                    } else if (method == "GET") {
                        // request method is GET
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
                        String GET_SERVICE=AppConstants.UPDATE_USER_DETAILS + "?" + paramString;
                        HttpGet httpGet = new HttpGet(GET_SERVICE);
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    is.close();
                    json = sb.toString();
                    Log.e("response msg===> ", json.toString());


                } catch (HttpHostConnectException e) {
                    e.printStackTrace();

                } catch (SocketException e) {
                    e.printStackTrace();

                } catch (UnknownHostException e) {
                    e.printStackTrace();

                } catch (SocketTimeoutException e) {
                    e.printStackTrace();

                } catch (ConnectTimeoutException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                try {
                    final JSONObject jsonObject = new JSONObject(json);

                    String responseCode=jsonObject.getString("message");
                    if(responseCode.equalsIgnoreCase("success")){
                        Toast.makeText(AdminEditableProjectActivity.this, "Project Manager Updated Successfully...", Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Toast.makeText(AdminEditableProjectActivity.this, "Error, Please Try again...", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }

}
