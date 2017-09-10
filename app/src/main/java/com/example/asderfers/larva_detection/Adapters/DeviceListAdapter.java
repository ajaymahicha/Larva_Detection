package com.example.asderfers.larva_detection.Adapters;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asderfers.larva_detection.DeviceList;
import com.example.asderfers.larva_detection.Model.Device;
import com.example.asderfers.larva_detection.R;

/**
 * Created by akm on 09-Sep-17.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder>{

    private static final String TAG = DeviceListAdapter.class.getSimpleName();

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView deviceId, devicename, devicestatus,devicesecret;

        DeviceViewHolder(final View itemView, final Activity activity) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            deviceId = (TextView)itemView.findViewById(R.id.device_id);
            devicename = (TextView)itemView.findViewById(R.id.device_name);
            devicestatus = (TextView)itemView.findViewById(R.id.device_status);
            devicesecret= (TextView)itemView.findViewById(R.id.device_secret);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //todo add edit and delete
//                    Intent intent = new Intent(activity,DeviceDetailsActivity.class);
//                    intent.putExtra("deviceSecret",devicesecret.toString());
//                    activity.startActivity(intent);
//                    //Toast.makeText(itemView.getContext(),"Clicked",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    DeviceList deviceList;
    Activity parentActivity;

    public DeviceListAdapter(DeviceList list,Activity activity){
        this.deviceList = list;
        this.parentActivity = activity;
    }

    public void updateDeviceList(DeviceList newDeviceList) {
        this.deviceList = newDeviceList;
    }

    @Override
    public int getItemCount() {
//        Log.d(TAG,"Adapter getItemCount = " + deviceList.deviceCount());
        if(deviceList == null)
            return 0;
        return deviceList.deviceCount();
    }
    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device_list, viewGroup, false);
        DeviceViewHolder deviceViewHolder = new DeviceViewHolder(v,parentActivity);
        return deviceViewHolder;
    }
    @Override
    public void onBindViewHolder(DeviceViewHolder deviceViewHolder, int i) {
        Device device = deviceList.getDevice(i);
        deviceViewHolder.devicesecret.setText(device.getdeviceSecret());
        deviceViewHolder.devicename.setText(device.getdevicename());
        deviceViewHolder.deviceId.setText(device.getdeviceId());
        deviceViewHolder.devicestatus.setText(device.getdevicestatus());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
