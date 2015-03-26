package com.nkdroid.cropso.Admin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nkdroid.cropso.Custom.AppConstants;
import com.nkdroid.cropso.R;

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
import java.util.regex.Pattern;

public class AddNewProjectManager extends ActionBarActivity {
    private TextView txt_add_client;
    private Toolbar toolbar;
    private EditText etFirstName,etLastName,etEmail,etPassword,etMobile,etPostion;
    private String method = "POST";
    private InputStream is = null;
    private String json = "",regId="";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_project_manager);
        setActionBar();
        etFirstName= (EditText) findViewById(R.id.etFirstName);
        etLastName= (EditText) findViewById(R.id.etLastName);
        etEmail= (EditText) findViewById(R.id.etEmail);
        etPassword= (EditText) findViewById(R.id.etPassword);

        etMobile= (EditText) findViewById(R.id.etMobile);
        etPostion= (EditText) findViewById(R.id.etPostion);
        etPostion.setVisibility(View.GONE);
        txt_add_client= (TextView) findViewById(R.id.txt_add_client);
        txt_add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmptyField(etFirstName)){
                    Toast.makeText(AddNewProjectManager.this, "Please Enter First Name", Toast.LENGTH_LONG).show();
                } else if(isEmptyField(etLastName)){
                    Toast.makeText(AddNewProjectManager.this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
                } else if(isEmptyField(etEmail)){
                    Toast.makeText(AddNewProjectManager.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                } else if(isEmptyField(etPassword)){
                    Toast.makeText(AddNewProjectManager.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else if(isEmptyField(etMobile)){
                    Toast.makeText(AddNewProjectManager.this, "Please Enter Mobile", Toast.LENGTH_LONG).show();
                }  else if(isEmailMatch(etEmail)==false){
                    Toast.makeText(AddNewProjectManager.this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();
                } else {
                    addClient();
                }
            }
        });
    }
    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isEmailMatch(EditText param1) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
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
    private void addClient() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(AddNewProjectManager.this);
                progressDialog.setMessage("Adding Client...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Making HTTP request
                    // check for request method
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("username",etEmail.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("password",etPassword.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("fname",etFirstName.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("lname",etLastName.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("mobile",etMobile.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("registration_type","3"));

                    if (method.equals("POST")) {
                        // request method is POST
                        // defaultHttpClient
                        Log.e("name value", nameValuePairs + "");
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(AppConstants.ADD_USER_DETAILS);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();

                    } else if (method == "GET") {
                        // request method is GET
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
                        String GET_SERVICE=AppConstants.ADD_USER_DETAILS + "?" + paramString;
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
                        Toast.makeText(AddNewProjectManager.this, "Project Manager Added Successfully...", Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Toast.makeText(AddNewProjectManager.this, "Error, Please Try again...", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
