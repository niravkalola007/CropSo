package com.nkdroid.cropso.ProjectManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.nkdroid.cropso.Custom.AppConstants;
import com.nkdroid.cropso.R;
import com.nkdroid.cropso.model.NotificationList;
import com.nkdroid.cropso.model.Technology;
import com.nkdroid.cropso.model.TechnologyList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class PmAllAddNewEmployee extends ActionBarActivity {
    private int code;
    private Spinner spinnerSkill;
    private EditText etFirstName,etLastName,etEmail,etPassword,etMobile;
    private TextView txt_add_client;
    private Toolbar toolbar;
    private String method = "POST";
    private InputStream is = null;
    private String json = "",regId="";
    private ProgressDialog progressDialog;
    ArrayList<Technology> technologyArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm_all_add_new_employee);
        setActionBar();
        etFirstName= (EditText) findViewById(R.id.etFirstName);
        etLastName= (EditText) findViewById(R.id.etLastName);
        etEmail= (EditText) findViewById(R.id.etEmail);
        etPassword= (EditText) findViewById(R.id.etPassword);
        etMobile= (EditText) findViewById(R.id.etMobile);
        spinnerSkill= (Spinner) findViewById(R.id.spinnerSkill);


        getTechnologyList();

        txt_add_client= (TextView) findViewById(R.id.txt_project_manager);
        txt_add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmptyField(etFirstName)) {
                    Toast.makeText(PmAllAddNewEmployee.this, "Please Enter First Name", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(etLastName)) {
                    Toast.makeText(PmAllAddNewEmployee.this, "Please Enter Last Name", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(etEmail)) {
                    Toast.makeText(PmAllAddNewEmployee.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(etPassword)) {
                    Toast.makeText(PmAllAddNewEmployee.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else if (isEmptyField(etMobile)) {
                    Toast.makeText(PmAllAddNewEmployee.this, "Please Enter Mobile", Toast.LENGTH_LONG).show();
                } else if (isEmailMatch(etEmail) == false) {
                    Toast.makeText(PmAllAddNewEmployee.this, "Please Enter Valid Email", Toast.LENGTH_LONG).show();
                } else {
                    addEmployee();
                }
            }
        });
    }
    private void getTechnologyList() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(PmAllAddNewEmployee.this);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Fetching technologies...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                String response=getJsonStringfromUrl(AppConstants.TECHNOLOGY_LIST);
                TechnologyList technologyList=new GsonBuilder().create().fromJson(response, TechnologyList.class);
                technologyArrayList=technologyList.technologyArrayList;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();

                UserTypeAdapter userTypeAdapter=new UserTypeAdapter(PmAllAddNewEmployee.this,R.layout.temp_item_row,technologyArrayList);
                spinnerSkill.setAdapter(userTypeAdapter);

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

    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }


    private void addEmployee() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(PmAllAddNewEmployee.this);
                progressDialog.setMessage("Adding Employee...");
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
                    nameValuePairs.add(new BasicNameValuePair("registration_type","4"));
                    nameValuePairs.add(new BasicNameValuePair("skill",spinnerSkill.getSelectedItemPosition()+""));
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
                        Toast.makeText(PmAllAddNewEmployee.this, "Employee Added Successfully...", Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Toast.makeText(PmAllAddNewEmployee.this, "Error, Please Try again...", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public class UserTypeAdapter extends ArrayAdapter<Technology> {

        Context context;
        int layoutResourceId;
        ArrayList<Technology> values;
        // int android.R.Layout.

        public UserTypeAdapter(Context context, int resource, ArrayList<Technology> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(PmAllAddNewEmployee.this);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position).technology_name);
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(PmAllAddNewEmployee.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(18);
            txt.setText(values.get(position).technology_name);
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;
        }
    }

}
