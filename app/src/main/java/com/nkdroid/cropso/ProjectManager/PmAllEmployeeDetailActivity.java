package com.nkdroid.cropso.ProjectManager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nkdroid.cropso.R;
import com.nkdroid.cropso.model.PrefUtils;
import com.nkdroid.cropso.model.User;

public class PmAllEmployeeDetailActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private TextView txtFname,txtLname,txtEmail,txtMobile,txtSkill;
    private User employee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm_all_employee_detail);
        setActionBar();
        employee= PrefUtils.getEmployee(PmAllEmployeeDetailActivity.this);
        txtFname= (TextView) findViewById(R.id.txtFname);
        txtLname= (TextView) findViewById(R.id.txtLname);
        txtEmail= (TextView) findViewById(R.id.txtEmail);
        txtMobile= (TextView) findViewById(R.id.txtMobile);
        txtSkill= (TextView) findViewById(R.id.txtSkill);
        txtFname.setText(employee.fname);
        txtLname.setText(employee.lname);
        txtEmail.setText(employee.username);
        txtMobile.setText(employee.mobile);
        txtSkill.setText(employee.skill);

    }


    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_launcher);
            toolbar.setTitle("Employee Detail");
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
