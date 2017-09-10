package com.example.asderfers.larva_detection;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asderfers.larva_detection.Adapters.DeviceListAdapter;
import com.example.asderfers.larva_detection.App.UserDetailsDialog;
import com.example.asderfers.larva_detection.App.lardet;
import com.example.asderfers.larva_detection.Controllers.AsyncResponseHandler;
import com.example.asderfers.larva_detection.Controllers.DeviceListController;
import com.example.asderfers.larva_detection.Controllers.SyncResponseHandler;
import com.example.asderfers.larva_detection.Network.AddNewDevice;

import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {
    DeviceList devicelist;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Device List");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewDevice();
            }
        });
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.deviceList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);

        final DeviceListAdapter adapter = new DeviceListAdapter(null,this);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        notice = (TextView)findViewById(R.id.no_device_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent);

        DeviceList initialDeviceList = DeviceListController.getDeviceListSynchronously(new SyncResponseHandler() {
            @Override
            public void onSyncWait() {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        notice.setText("Loading devices...");
                        notice.setVisibility(View.VISIBLE);
//                        swipeRefreshLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });
            }

            @Override
            public void finishSyncWait() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onUpdate(DeviceList updatedDeviceList) {
                if (updatedDeviceList.deviceCount() > 0) {
                    adapter.updateDeviceList(updatedDeviceList);
                    recyclerView.setAdapter(adapter);
                    notice.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    notice.setVisibility(View.VISIBLE);
                    notice.setText("No device to view");
                    recyclerView.setVisibility(View.GONE);
                }

            }
        });
        adapter.updateDeviceList(initialDeviceList);
        recyclerView.setAdapter(adapter);

//        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //after refresh complete set this
//                swipeRefreshLayout.setRefreshing(false);
                DeviceListController.getDeviceListAsync(new AsyncResponseHandler() {
                    @Override
                    public void onResponse(final DeviceList newDeviceList) {
//                        swipeRefreshLayout.setRefreshing(true);
//                        DeviceListAdapter adapter = new DeviceListAdapter(newDeviceList,getActivity());
                        if (newDeviceList.deviceCount() > 0) {
                            adapter.updateDeviceList(newDeviceList);
                            recyclerView.setAdapter(adapter);
                            notice.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            notice.setVisibility(View.VISIBLE);
                            notice.setText("No devices to view");
                            recyclerView.setVisibility(View.GONE);
                        }
//                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void duringWait() {

                    }

                    @Override
                    public void finishWait() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            showProfile();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void onUserLogout() {
        lardet.App.clearLoginCredentials();
        Intent i = new Intent(this,LoginActivity.class);
        //TODO clear user object
        startActivity(i);
        StartActivity.this.finish();
    }
    private void showProfile(){
        Dialog dialog = new Dialog(StartActivity.this,R.style.DialogSlideAnimSmall);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_setting);
        setDialogLayoutParams(dialog);
        new UserDetailsDialog(dialog, new UserDetailsDialog.LogoutHandler() {
            @Override
            public void onLogout(JSONObject logoutResponse) {
                onUserLogout();
            }
            @Override
            public void onError(Exception e) {
                //TODO //case when logout unsucessfull
                e.printStackTrace();
                onUserLogout();
            }
        }).setUpDialog();
    }
    private void AddNewDevice(){
        final Dialog dialog = new Dialog(StartActivity.this,R.style.DialogSlideAnimBottom);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_new_device);
        dialog.findViewById(R.id.create_device_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((TextView) dialog.findViewById(R.id.devicename)).getText().toString();
                String id = ((TextView) dialog.findViewById(R.id.deviceid)).getText().toString();
                AddNewDevice addNewDevice = new AddNewDevice(getNewDeviceResponseHandler(dialog), name,id);
                addNewDevice.getAddNewDeviceResponse();
            }
        });
        setDialogLayoutParams(dialog);
    }
    private AddNewDevice.AddNewDeviceResponseHandler getNewDeviceResponseHandler(final Dialog dialog){
        AddNewDevice.AddNewDeviceResponseHandler addNewDeviceResponseHandler = new AddNewDevice.AddNewDeviceResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("addNewDevice", response.toString());
                try{
                    boolean resp = response.getBoolean("error");
                    String msg = "Device created successfully!";
                    if(resp)
                        msg = "There was some error while creating the thread.Please try again!";
                    else {
                        dialog.dismiss();
                    }
                    Toast.makeText(StartActivity.this, msg, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                Log.d("addNewDevice","failure");
            }

            @Override
            public void onError(Exception e) {
                Log.d("addNewDevice",e.toString());
                e.printStackTrace();
            }
        };
        return addNewDeviceResponseHandler;
    }
    private void setDialogLayoutParams(Dialog dialog){
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
