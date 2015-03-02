package com.nkdroid.cropso.Emplyee;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nkdroid.cropso.R;

public class EmployeeProjectDetailActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private TextView txtAddTask,txtAddSuggetion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_project_detail);
        txtAddTask= (TextView) findViewById(R.id.txtAddTask);
        txtAddSuggetion= (TextView) findViewById(R.id.txtAddSuggetion);

        txtAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTask=new Intent(EmployeeProjectDetailActivity.this,EmployeeTaskListActivity.class);
                startActivity(intentTask);
            }
        });
        txtAddSuggetion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentTask=new Intent(EmployeeProjectDetailActivity.this,EmployeeSuggetionListActivity.class);
                startActivity(intentTask);
            }
        });
        setActionBar();
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

}
