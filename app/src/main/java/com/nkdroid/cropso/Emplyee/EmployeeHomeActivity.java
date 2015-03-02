package com.nkdroid.cropso.Emplyee;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nkdroid.cropso.LoginActivity;
import com.nkdroid.cropso.R;

public class EmployeeHomeActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private TextView txtProjects,txtNotifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        setActionBar();
        txtProjects= (TextView) findViewById(R.id.txtProjects);
        txtNotifications= (TextView) findViewById(R.id.txtNotifications);
        txtProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentProjects=new Intent(EmployeeHomeActivity.this,EmployeeProjectListActivity.class);
                startActivity(intentProjects);
            }
        });

        txtNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNotifications=new Intent(EmployeeHomeActivity.this,EmployeeNotificationListActivity.class);
                startActivity(intentNotifications);
            }
        });
    }

    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("CropSo");
            toolbar.setSubtitle("Hi! Employee");
            setSupportActionBar(toolbar);
        }
    }


}
