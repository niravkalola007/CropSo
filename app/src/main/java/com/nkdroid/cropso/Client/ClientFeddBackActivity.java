package com.nkdroid.cropso.Client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nkdroid.cropso.Custom.AppConstants;
import com.nkdroid.cropso.R;
import com.nkdroid.cropso.model.PrefUtils;
import com.nkdroid.cropso.model.Project;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientFeddBackActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private RatingBar rattings;
    private EditText etComments;
    private TextView txt_feedback_submit;
    private String method = "POST";
    private InputStream is = null;
    private String json = "",regId="";
    private ProgressDialog progressDialog;
    private User user;
    private Project project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_fedd_back);
        user= PrefUtils.getUser(ClientFeddBackActivity.this);
        project=PrefUtils.getProject(ClientFeddBackActivity.this);
        setActionBar();
        rattings= (RatingBar) findViewById(R.id.rattings);
        etComments= (EditText) findViewById(R.id.etComments);
        txt_feedback_submit= (TextView) findViewById(R.id.txt_feedback_submit);
        txt_feedback_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmptyField(etComments)==true) {
                    Toast.makeText(ClientFeddBackActivity.this, "Please Enter Comments", Toast.LENGTH_LONG).show();
                } else {
                    postComment();
                }

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

    public boolean isEmptyField(EditText param1) {

        boolean isEmpty = false;
        if (param1.getText() == null || param1.getText().toString().equalsIgnoreCase("")) {
            isEmpty = true;
        }
        return isEmpty;
    }

    private void postComment() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(ClientFeddBackActivity.this);
                progressDialog.setMessage("Adding feedback...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Making HTTP request
                    // check for request method
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("comments",etComments.getText().toString()+""));
                    nameValuePairs.add(new BasicNameValuePair("ratings",rattings.getRating()+""));
                    nameValuePairs.add(new BasicNameValuePair("project_id",project.id+""));
                    nameValuePairs.add(new BasicNameValuePair("client_id",user.id+""));

                    if (method.equals("POST")) {
                        // request method is POST
                        // defaultHttpClient
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(AppConstants.CLIENT_FEEDBACK);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();

                    } else if (method == "GET") {
                        // request method is GET
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
                        String GET_SERVICE=AppConstants.CLIENT_FEEDBACK + "?" + paramString;
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
                        Toast.makeText(ClientFeddBackActivity.this, "Feedback Added Successfully...", Toast.LENGTH_LONG).show();
                    finish();

                    } else {
                        Toast.makeText(ClientFeddBackActivity.this, "Error, Please Try again...", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }

}
