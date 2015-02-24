package com.nkdroid.cropso;

import android.app.AlertDialog;
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
import com.nkdroid.cropso.Admin.AdminHomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class LoginActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private Spinner spinnerUserType;
    private EditText etEmail,etPassword;
    private TextView txtLogin;
    private GoogleCloudMessaging gcm;
    private String regid;
    private String PROJECT_NUMBER = "92884720384";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setActionBar();
        etEmail= (EditText) findViewById(R.id.etEmail);
        etPassword= (EditText) findViewById(R.id.etPassword);
        txtLogin= (TextView) findViewById(R.id.txtLogin);
        spinnerUserType= (Spinner) findViewById(R.id.spinnerUserType);

        ArrayList<String> userTypeList=new ArrayList<>();
        userTypeList.add("Select User Type");
        userTypeList.add("Admin");
        userTypeList.add("Client");
        userTypeList.add("Project Manager");
        userTypeList.add("Employee");

        UserTypeAdapter userTypeAdapter=new UserTypeAdapter(LoginActivity.this,R.layout.temp_item_row,userTypeList);
        spinnerUserType.setAdapter(userTypeAdapter);

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
                        Toast.makeText(LoginActivity.this, "Done", Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

                        //TODO post web service
                        // Store GCM ID in sharedpreference
                        SharedPreferences sharedPreferences=getSharedPreferences("GCM",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("GCM_ID",regid);
                        editor.commit();

                        Intent i = new Intent(LoginActivity.this, AdminHomeActivity.class);
                        startActivity(i);
                        finish();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        }.execute();

    } // end of getRegId
}
