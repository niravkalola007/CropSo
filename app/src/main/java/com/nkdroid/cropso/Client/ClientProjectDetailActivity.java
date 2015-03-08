package com.nkdroid.cropso.Client;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.nkdroid.cropso.R;
import com.nkdroid.cropso.model.PrefUtils;
import com.nkdroid.cropso.model.Project;

public class ClientProjectDetailActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private TextView txt_project_feedback;
    private DonutProgress donut_progress;
    private TextView endDate,startDate,txtAbstract,txtModules,txtFunctions,txtLanguages,txtFrontEnd,txtBackEnd;
    private Project project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_project_detail);
        txt_project_feedback= (TextView) findViewById(R.id.txt_project_feedback);
        txt_project_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ClientProjectDetailActivity.this,ClientFeddBackActivity.class);
                startActivity(intent);
            }
        });
        project= PrefUtils.getProject(ClientProjectDetailActivity.this);
        donut_progress= (DonutProgress) findViewById(R.id.donut_progress);
        endDate= (TextView) findViewById(R.id.endDate);
        startDate= (TextView) findViewById(R.id.startDate);
        txtAbstract= (TextView) findViewById(R.id.txtAbstract);
        txtModules= (TextView) findViewById(R.id.txtModules);
        txtFunctions= (TextView) findViewById(R.id.txtFunctions);
        txtLanguages= (TextView) findViewById(R.id.txtLanguages);
        txtFrontEnd= (TextView) findViewById(R.id.txtFrontEnd);
        txtBackEnd= (TextView) findViewById(R.id.txtBackEnd);
        donut_progress.setProgress(Integer.parseInt(project.progress));
        endDate.setText(project.deadline_date);
        startDate.setText(project.start_date);
        txtAbstract.setText(project.abstractvalue);
        txtModules.setText(project.modules);
        txtFunctions.setText(project.functions);
        txtLanguages.setText(project.programming_languages);
        txtFrontEnd.setText(project.front_end);
        txtBackEnd.setText(project.back_end);

        setActionBar();
    }

    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_launcher);
            toolbar.setTitle(project.name);
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
