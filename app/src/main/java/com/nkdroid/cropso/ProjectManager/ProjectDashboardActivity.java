package com.nkdroid.cropso.ProjectManager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nkdroid.cropso.Emplyee.EmployeeHomeActivity;
import com.nkdroid.cropso.R;

public class ProjectDashboardActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private TextView txtDetails,txtSuggetion,txtTask,txtResources,txtEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_dashboard);
        setActionBar();
        txtDetails= (TextView) findViewById(R.id.txtDetails);
        txtSuggetion= (TextView) findViewById(R.id.txtSuggetion);
        txtTask= (TextView) findViewById(R.id.txtTask);
        txtResources= (TextView) findViewById(R.id.txtResources);
        txtEmployee= (TextView) findViewById(R.id.txtEmployee);

        txtResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDashboardActivity.this, PmResourceList.class);
                startActivity(i);
            }
        });

        txtEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDashboardActivity.this, PmEmployeeList.class);
                startActivity(i);
            }
        });
        txtTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDashboardActivity.this, PmTaskList.class);
                startActivity(i);
            }
        });
        txtSuggetion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDashboardActivity.this, PmSuggetionListActivity.class);
                startActivity(i);
            }
        });

        txtDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProjectDashboardActivity.this, PmProjectDetailActivity.class);
                startActivity(i);
            }
        });
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
