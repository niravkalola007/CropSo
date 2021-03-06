package com.nkdroid.cropso.Admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.nkdroid.cropso.Client.ClientProjectDetailActivity;
import com.nkdroid.cropso.Custom.AppConstants;
import com.nkdroid.cropso.R;
import com.nkdroid.cropso.model.NotificationList;
import com.nkdroid.cropso.model.PrefUtils;
import com.nkdroid.cropso.model.User;
import com.nkdroid.cropso.model.UserList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AdminClientListActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private ListView admin_client_list;
    private ClientListAdapter clientListAdapter;
    private ArrayList<User> clientList;
    private TextView txt_add_client;
    private static InputStream is = null;
    private int code;
    private String json = null;
    private ProgressDialog progressDialog;
    private UserList userListClass;
    private String method = "POST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client_list);
        admin_client_list = (ListView) findViewById(R.id.admin_client_list);
        txt_add_client= (TextView) findViewById(R.id.txt_add_client);
        txt_add_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminClientListActivity.this,AddNewClientActivity.class);
                startActivity(intent);
            }
        });
        setActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsers();
    }

    private void setActionBar(){
        //Set ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_launcher);
            toolbar.setTitle("Clients");
            setSupportActionBar(toolbar);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class ClientListAdapter extends BaseAdapter {

        private ArrayList<User> redeemList;
        private Context context;
        private LayoutInflater mInflater;
        private ViewHolder holder;

        public ClientListAdapter(FragmentActivity activity, ArrayList<User> redeemGiftCodesList) {
            this.context = activity;
            this.redeemList = redeemGiftCodesList;
        }

        @Override
        public int getCount() {

            return redeemList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
        ImageView img_edit,img_delete;
            TextView txtName;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_client_edit_delete, parent, false);

                holder = new ViewHolder();
                holder.img_edit= (ImageView) convertView.findViewById(R.id.img_edit);
                holder.img_delete= (ImageView) convertView.findViewById(R.id.img_delete);
                holder.txtName= (TextView) convertView.findViewById(R.id.txtName);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtName.setText(redeemList.get(position).fname+" "+redeemList.get(position).lname);
            holder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefUtils.setClient(AdminClientListActivity.this,redeemList.get(position));
                    Intent intent=new Intent(AdminClientListActivity.this,AdminEditableClientActivity.class);
                    startActivity(intent);
                }
            });

            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AdminClientListActivity.this);
                    alert.setTitle("Delete");
                    alert.setMessage("Delete this record ?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        deleteUser(redeemList.get(position).id);
                        dialog.dismiss();

                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });

                    alert.show();
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefUtils.setClient(AdminClientListActivity.this,redeemList.get(position));
                    Intent intent=new Intent(AdminClientListActivity.this,AdminClientDetailActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }

    private void deleteUser(final String id){

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(AdminClientListActivity.this);
                progressDialog.setMessage("Removing Client...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    // Making HTTP request
                    // check for request method
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    nameValuePairs.add(new BasicNameValuePair("id",id+""));

                    if (method.equals("POST")) {
                        // request method is POST
                        // defaultHttpClient
                        Log.e("name value",nameValuePairs+"");
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(AppConstants.REMOVE_USER);
                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();

                    } else if (method == "GET") {
                        // request method is GET
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
                        String GET_SERVICE=AppConstants.REMOVE_USER + "?" + paramString;
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
                        Toast.makeText(AdminClientListActivity.this, "Client Removed Successfully...", Toast.LENGTH_LONG).show();
                        getUsers();

                    } else {
                        Toast.makeText(AdminClientListActivity.this, "Error, Please Try again...", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void getUsers() {

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(AdminClientListActivity.this);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Fetching client list...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                String response=getJsonStringfromUrl(AppConstants.GET_USER_DETAILS+"2");
                Log.e("response", response+"");
                userListClass=new GsonBuilder().create().fromJson(response,UserList.class);
                clientList=userListClass.userArrayList;


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();

                clientListAdapter = new ClientListAdapter(AdminClientListActivity.this, clientList);
                admin_client_list.setAdapter(clientListAdapter);

            }
        }.execute();
    }

    public String getJsonStringfromUrl(String url) {

        try {

            StringBuilder builder = new StringBuilder();

            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);

            code = httpResponse.getStatusLine().getStatusCode();

            if (code == 200) {
                HttpEntity httpentity = httpResponse.getEntity();
                is = httpentity.getContent();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);

                String line = null;
                while ((line = br.readLine()) != null) {
                    builder.append(line + "\n");
                }
                is.close();
            }

            json = builder.toString();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;

    }
}
