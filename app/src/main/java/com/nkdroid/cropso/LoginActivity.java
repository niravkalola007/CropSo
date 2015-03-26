package com.nkdroid.cropso;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.GsonBuilder;
import com.nkdroid.cropso.Admin.AdminHomeActivity;
import com.nkdroid.cropso.Client.ClientHomeActivity;
import com.nkdroid.cropso.Client.ClientMainActivity;
import com.nkdroid.cropso.Custom.AppConstants;
import com.nkdroid.cropso.Emplyee.EmployeeHomeActivity;
import com.nkdroid.cropso.ProjectManager.PmHomeActivity;
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
import org.json.JSONArray;
import org.json.JSONException;
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


public class LoginActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Spinner spinnerUserType;
    private EditText etEmail,etPassword;
    private TextView txtLogin;
    private GoogleCloudMessaging gcm;
    private String regid;
    private String PROJECT_NUMBER = "837422643092";
    ArrayList<String> userTypeList;
    private ProgressDialog progressDialog;
    private String method = "POST";
    private String LOGIN_SERVICE = AppConstants.LOGIN;
    private InputStream is = null;
    private String json = "",regId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setActionBar();
        etEmail= (EditText) findViewById(R.id.etEmail);
        etPassword= (EditText) findViewById(R.id.etPassword);
        txtLogin= (TextView) findViewById(R.id.txtLogin);
        spinnerUserType= (Spinner) findViewById(R.id.spinnerUserType);

        userTypeList=new ArrayList<>();
        userTypeList.add("Select User Type");
        userTypeList.add("Admin");
        userTypeList.add("Client");
        userTypeList.add("Project Manager");
        userTypeList.add("Employee");

        UserTypeAdapter userTypeAdapter=new UserTypeAdapter(LoginActivity.this,R.layout.temp_item_row,userTypeList);
        spinnerUserType.setAdapter(userTypeAdapter);

        SharedPreferences sharedPreferencesLogin=getSharedPreferences("LOGIN",MODE_PRIVATE);

        boolean isLogin=sharedPreferencesLogin.getBoolean("LOGIN",false);
        int position=sharedPreferencesLogin.getInt("POSITION",0);
        if(isLogin){
            if(position==1){
                Intent i = new Intent(LoginActivity.this, AdminHomeActivity.class);
                startActivity(i);
                finish();
            }else if(position==2){
                Intent i = new Intent(LoginActivity.this, ClientMainActivity.class);
                startActivity(i);
                finish();
            } else if(position==3){
                Intent i = new Intent(LoginActivity.this, PmHomeActivity.class);
                startActivity(i);
                finish();
            } else if(position==4){
                Intent i = new Intent(LoginActivity.this, EmployeeHomeActivity.class);
                startActivity(i);
                finish();
            }
        }

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmptyField(etEmail)==true){
                    Toast.makeText(LoginActivity.this,"Please Enter Email",Toast.LENGTH_LONG).show();
                } else if(isEmailMatch(etEmail)==false){
                    Toast.makeText(LoginActivity.this,"Please Enter Valid Email",Toast.LENGTH_LONG).show();
                } else if(isEmptyField(etPassword)){
                    Toast.makeText(LoginActivity.this,"Please Enter Password",Toast.LENGTH_LONG).show();
                } else if(spinnerUserType.getSelectedItemPosition()==0){
                    Toast.makeText(LoginActivity.this,"Please Select User Type",Toast.LENGTH_LONG).show();
                } else {
                    if(isConnected()==true) {
//                        Toast.makeText(LoginActivity.this, "Done", Toast.LENGTH_LONG).show();
                        getRegId();
                    } else {
                        Toast.makeText(LoginActivity.this, "Internet Connection Unavailable", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("CropSo");
            setSupportActionBar(toolbar);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_us) {

            Intent intent=new Intent(LoginActivity.this,AboutUsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public class UserTypeAdapter extends ArrayAdapter<String> {

        Context context;
        int layoutResourceId;
        ArrayList<String> values;
        // int android.R.Layout.

        public UserTypeAdapter(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            this.context = context;
            this.values=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(LoginActivity.this);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(values.get(position));
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txt = new TextView(LoginActivity.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16,16,16,16);
            txt.setTextSize(18);
            txt.setText(values.get(position));
            txt.setTextColor(Color.parseColor("#494949"));
            return  txt;
        }
    }

    public boolean isEmailMatch(EditText param1) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(param1.getText().toString()).matches();
    }

    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }


    // Check Internet Connection
    public  boolean isConnected() {
        ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return  isConnected;
    }

    public void getRegId(){

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Login...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(LoginActivity.this);
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                        Log.e("GCM ID :", regid);

                    if(regid==null || regid==""){
                        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        alert.setTitle("Error");
                        alert.setMessage("Internal Server Error");
                        alert.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getRegId();
                                dialog.dismiss();
                            }
                        });
                        alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        alert.show();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callLoginService();
                            }
                        });
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        }.execute();

    } // end of getRegId

    private void callLoginService() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Making HTTP request
                    // check for request method
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("username",etEmail.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("password",etPassword.getText().toString().trim()+""));
                    nameValuePairs.add(new BasicNameValuePair("registration_type",spinnerUserType.getSelectedItemPosition()+""));
                    nameValuePairs.add(new BasicNameValuePair("notification_id",regid+""));
                    Log.e("nameValuePairs",nameValuePairs+"");
                    if (method.equals("POST")) {
                        // request method is POST
                        // defaultHttpClient
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(LOGIN_SERVICE);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();

                    } else if (method == "GET") {
                        // request method is GET
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
                        LOGIN_SERVICE += "?" + paramString;
                        HttpGet httpGet = new HttpGet(LOGIN_SERVICE);
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
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject jsonObjectInner = new JSONObject(jsonObject.getString("result"));
                    User user = new GsonBuilder().create().fromJson(jsonObjectInner.toString(), User.class);
                    PrefUtils.setUser(LoginActivity.this,user);
                    String response=jsonObjectInner.getString("message");
                    if(response.equalsIgnoreCase("success")){
                        // Store GCM ID in sharedpreference
                        SharedPreferences sharedPreferences=getSharedPreferences("GCM",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("GCM_ID",regid);
                        editor.commit();
                        SharedPreferences sharedPreferencesLogin=getSharedPreferences("LOGIN",MODE_PRIVATE);
                        SharedPreferences.Editor editorLogin=sharedPreferencesLogin.edit();
                        editorLogin.putBoolean("LOGIN",true);
                        editorLogin.putInt("POSITION",spinnerUserType.getSelectedItemPosition());
                        editorLogin.commit();
                        if(spinnerUserType.getSelectedItemPosition()==1){
                            Intent i = new Intent(LoginActivity.this, AdminHomeActivity.class);
                            startActivity(i);
                            finish();
                        }else if(spinnerUserType.getSelectedItemPosition()==2){
                            Intent i = new Intent(LoginActivity.this, ClientMainActivity.class);
                            startActivity(i);
                            finish();
                        } else if(spinnerUserType.getSelectedItemPosition()==3){
                            Intent i = new Intent(LoginActivity.this, PmHomeActivity.class);
                            startActivity(i);
                            finish();
                        } else if(spinnerUserType.getSelectedItemPosition()==4){
                            Intent i = new Intent(LoginActivity.this, EmployeeHomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this,"Please Enter Valid Username or Password",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
