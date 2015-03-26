package com.nkdroid.cropso.Admin;

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

public class AdminClientDetailActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private TextView txtFname,txtLname,txtEmail,txtMobile,txtDesignation;
    private User client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_detail);
        setActionBar();
        client= PrefUtils.getClient(AdminClientDetailActivity.this);
        txtFname= (TextView) findViewById(R.id.txtFname);
        txtLname= (TextView) findViewById(R.id.txtLname);
        txtEmail= (TextView) findViewById(R.id.txtEmail);
        txtMobile= (TextView) findViewById(R.id.txtMobile);
        txtDesignation= (TextView) findViewById(R.id.txtDesignation);
        txtFname.setText(client.fname);
        txtLname.setText(client.lname);
        txtEmail.setText(client.username);
        txtMobile.setText(client.mobile);
        txtDesignation.setText("");

    }


    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_launcher);
            toolbar.setTitle("Client Detail");
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
