package com.example.samsungtvcontrol.tvcommand;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.example.samsungtvcontrol.R;
import com.samsung.multiscreen.Service;

import java.util.List;

/**
 * @author thangnd46
 * <p>
 * Adapter for displaying the services.
 */

public class ServiceListAdapter extends BaseAdapter {
    List<Service> checklists;
    Activity activity;

    public ServiceListAdapter(List<Service> checklists, Activity activity) {
        this.checklists = checklists;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return checklists.size();
    }

    @Override
    public Object getItem(int position) {
        return checklists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        convertView = layoutInflater.inflate(R.layout.service, null);
        Service service = checklists.get(position);
        EditText title = convertView.findViewById(R.id.line_title);
        EditText content = convertView.findViewById(R.id.line_short_content);
        title.setText(service.getId());
        content.setText(String.format("%s-%s-%s-%s", service.getName(), service.getType(), service.getVersion(), service.getUri()));
        service.getName();
        return convertView;
    }
}
