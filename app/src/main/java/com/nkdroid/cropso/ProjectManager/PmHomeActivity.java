package com.nkdroid.cropso.ProjectManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nkdroid.cropso.LoginActivity;
import com.nkdroid.cropso.R;
import com.nkdroid.cropso.model.PrefUtils;
import com.nkdroid.cropso.model.User;

public class PmHomeActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private User user;
    private TextView txtResources,txtNotifications,txtProjects,txtEmployee,txtClient,txtReports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm_home);
        user= PrefUtils.getUser(PmHomeActivity.this);
        setActionBar();

        txtResources= (TextView) findViewById(R.id.txtResources);
        txtResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PmHomeActivity.this, PmAllResourceListActivity.class);
                startActivity(intent);
            }
        });
        txtNotifications= (TextView) findViewById(R.id.txtNotifications);
        txtNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PmHomeActivity.this, PmAllNotificationList.class);
                startActivity(intent);
            }
        });
        txtProjects= (TextView) findViewById(R.id.txtProjects);
        txtProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PmHomeActivity.this, PmProjectListActivity.class);
                startActivity(intent);
            }
        });
        txtEmployee= (TextView) findViewById(R.id.txtEmployee);
        txtEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PmHomeActivity.this, PmAllEmployeeListActivity.class);
                startActivity(intent);
            }
        });

        txtClient= (TextView) findViewById(R.id.txtClient);
        txtClient.setVisibility(View.GONE);
        txtClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PmHomeActivity.this, PmAllClientListActivity.class);
                startActivity(intent);
            }
        });

        txtReports= (TextView) findViewById(R.id.txtReports);
        txtReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("CropSo");
            toolbar.setSubtitle("   Hi, "+user.fname+" "+user.lname);
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
            SharedPreferences sharedPreferencesLogin=getSharedPreferences("LOGIN",MODE_PRIVATE);
            SharedPreferences.Editor editorLogin=sharedPreferencesLogin.edit();
            editorLogin.putBoolean("LOGIN",false);
            editorLogin.putInt("POSITION",0);
            editorLogin.commit();
            Intent intent=new Intent(PmHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
