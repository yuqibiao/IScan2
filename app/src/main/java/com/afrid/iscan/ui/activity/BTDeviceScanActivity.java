package com.afrid.iscan.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.afrid.btprinter.BTPrinterManager;
import com.afrid.iscan.R;
import com.afrid.iscan.adapter.DeviceAdapter;
import com.afrid.iscan.utils.bt.BTManager;
import com.afrid.swingu.utils.SwingUManager;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能：蓝牙设备连接Activity
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/23
 */

public class BTDeviceScanActivity extends BaseActivity {

    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.pb_loading)
    ContentLoadingProgressBar pb_loading;
    @BindView(R.id.list_device)
    ListView listDevice;
    private DeviceAdapter btDeviceAdapter;
    private BTManager btManager;
    private BTPrinterManager btPrinterManager;
    private SwingUManager swingUManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bt_device_scan;
    }

    @Override
    public void beforeInit() {
        btManager = BTManager.getInstance(this);
        swingUManager = SwingUManager.getInstance(this);
        btPrinterManager = BTPrinterManager.getInstance(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        btDeviceAdapter = new DeviceAdapter(this);
        listDevice.setAdapter(btDeviceAdapter);
    }

    @Override
    protected void initListener() {
        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = btDeviceAdapter.getBluetoothDeviceList().get(position);
                String deviceName = device.getName();
                if (deviceName.startsWith("NP")) {
                    btPrinterManager.connect(device);
                } else if (deviceName.startsWith("SwingU")) {
                    swingUManager.connectDevice(device);
                    swingUManager.getInstance(BTDeviceScanActivity.this).startReader();
                }
            }
        });
    }

    /**
     * 开始扫描蓝牙设备
     */
    @OnClick(R.id.btn_start)
    public void startScan() {
        if (btPrinterManager.isConnected() && swingUManager.isConnected()) {
            MyToast.showShort(this, resourceUtils.getStr(R.string.device_connected));
            return;
        }
        btDeviceAdapter.clear();
        btManager.toSysBTActivity(this);
        //checkPermissions();
    }

    Set<String> tags = new HashSet<>();
    @OnClick(R.id.btn_stop)
    public void readerTest() {
        swingUManager.startReader();
        swingUManager.setOnReadResultListener(new SwingUManager.OnReadResultListener() {
            @Override
            public void onRead(String tagId) {
                boolean isAdd = tags.add(tagId);
                if (isAdd){
                    MyLog.e(tagId+"=====size"+tags.size());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //---刷新蓝牙设备（已绑定的）
        Set<BluetoothDevice> deviceSet = btManager.getBoundsDevice();
        List deviceList = new ArrayList();
        Iterator it = deviceSet.iterator();
        while (it.hasNext()) {
            deviceList.add(it.next());
        }
        btDeviceAdapter.setBluetoothDeviceList(deviceList);
        btDeviceAdapter.notifyDataSetChanged();

    }

    /**
     * 权限检查
     */
    private void checkPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(this, deniedPermissions, 12);
        }
    }

    /**
     * 权限申请
     *
     * @param permission
     */
    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                BTManager btManager = BTManager.getInstance(this);
                btManager.startDescovery();
                break;
        }
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 12:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, com.afrid.iscan.ui.activity.BTDeviceScanActivity.class);
        activity.startActivity(intent);
    }

}
