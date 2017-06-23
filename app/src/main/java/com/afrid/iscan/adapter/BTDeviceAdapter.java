package com.afrid.iscan.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.afrid.iscan.R;
import com.clj.fastble.data.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：扫描蓝牙设备对应Adapter
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/23
 */

public class BTDeviceAdapter extends BaseAdapter {

    private Context context;
    private List<ScanResult> scanResultList;

    public BTDeviceAdapter(Context context) {
        this.context = context;
        this.scanResultList = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return scanResultList.size();
    }

    @Override
    public ScanResult getItem(int position) {
        if (position > scanResultList.size())
            return null;
        return scanResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.lv_item_bt_device, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_mac = (TextView) convertView.findViewById(R.id.txt_mac);
            holder.txt_rssi = (TextView) convertView.findViewById(R.id.txt_rssi);
        }

        ScanResult result = scanResultList.get(position);
        BluetoothDevice device = result.getDevice();
        String name = device.getName();
        String mac = device.getAddress();
        int rssi = result.getRssi();
        holder.txt_name.setText(name);
        holder.txt_mac.setText(mac);
        holder.txt_rssi.setText(String.valueOf(rssi));
        return convertView;
    }

    public void addResult(ScanResult result) {
        scanResultList.add(result);
    }

    public List<ScanResult> getScanResultList() {
        return scanResultList;
    }

    public void clear() {
        scanResultList.clear();
    }

    class ViewHolder {
        TextView txt_name;
        TextView txt_mac;
        TextView txt_rssi;
    }
}
