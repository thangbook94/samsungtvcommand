package com.example.samsungtvcontrol.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.samsungtvcontrol.R;
import com.example.samsungtvcontrol.entity.TvInfoDetail;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {
    List<TvInfoDetail> serviceList;
    Activity activity;

    public DeviceAdapter(List<TvInfoDetail> serviceList, Activity activity) {
        this.serviceList = serviceList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        convertView = layoutInflater.inflate(R.layout.service, null);
        TextView id = convertView.findViewById(R.id.deviceInfoId);
        TextView name = convertView.findViewById(R.id.deviceInfoName);
        TextView type = convertView.findViewById(R.id.deviceInfoType);
        TextView version = convertView.findViewById(R.id.deviceInfoVersion);
        TextView uri = convertView.findViewById(R.id.deviceInfoURI);
        id.setTypeface(Typeface.SANS_SERIF);
        version.setTypeface(Typeface.SANS_SERIF);
        id.setText(String.format("%s", serviceList.get(position).getId()));
        name.setText(serviceList.get(position).getName());
        name.setTypeface(Typeface.DEFAULT_BOLD);
        version.setText(String.format("Version: %s", serviceList.get(position).getVersion()));
        type.setText(String.format("Type: %s", serviceList.get(position).getType()));
        uri.setText(String.format("IP: %s", serviceList.get(position).getIp()));
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#CBF38C"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#F08CF3"));

        }
        return convertView;
    }
}
