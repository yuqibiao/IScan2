package com.afrid.iscan.ui.fragment;

import android.Manifest;
import android.bluetooth.BluetoothGatt;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.afrid.iscan.R;
import com.afrid.iscan.adapter.BTDeviceAdapter;
import com.afrid.iscan.service.BluetoothService;
import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleGattCallback;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.exception.BleException;
import com.wpx.WPXMain;
import com.wpx.util.WPXUtils;
import com.yyyu.baselibrary.utils.MyLog;
import com.yyyu.baselibrary.utils.MyToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 功能：蓝牙设备连接Fragment
 *
 * @author yu
 * @version 1.0
 * @date 2017/6/23
 */

public class BTDeviceScanFragment extends BaseFragment {

    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.pb_loading)
    ContentLoadingProgressBar pb_loading;
    @BindView(R.id.list_device)
    ListView listDevice;
    Unbinder unbinder;
    private BleManager bleManager;
    private BluetoothService mBluetoothService;
    private BTDeviceAdapter btDeviceAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bt_device_scan;
    }

    @Override
    protected void beforeInit() {
        bleManager = new BleManager(getActivity());
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        btDeviceAdapter = new BTDeviceAdapter(getActivity());
        listDevice.setAdapter(btDeviceAdapter);
    }

    @Override
    protected void initListener() {
        listDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScanResult scanResult = btDeviceAdapter.getScanResultList().get(position);
                WPXUtils.bondDevice(scanResult.getDevice());
                /*bleManager.connectDevice(btDeviceAdapter.getScanResultList().get(position),
                        true, new BleGattCallback() {
                            @Override
                            public void onNotFoundDevice() {
                                MyToast.showLong(getContext(), "onNotFoundDevice！！");
                            }

                            @Override
                            public void onFoundDevice(ScanResult scanResult) {
                                MyToast.showLong(getContext(), "onFoundDevice！！");
                            }

                            @Override
                            public void onConnectSuccess(BluetoothGatt gatt, int status) {
                                MyToast.showLong(getContext(), "连接设备成功！！");
                            }

                            @Override
                            public void onConnectFailure(BleException exception) {
                                MyToast.showLong(getContext(), "onConnectFailure！！");
                            }
                        });*/
            }
        });
    }

    /**
     * 开始扫描蓝牙设备
     */
    @OnClick(R.id.btn_start)
    public void startScan() {
        btDeviceAdapter.clear();
        checkPermissions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        unbindService();
    }

    private void bindService() {
        Intent bindIntent = new Intent(getActivity(), BluetoothService.class);
        getActivity().bindService(bindIntent, mFhrSCon, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        getActivity().unbindService(mFhrSCon);
    }

    private ServiceConnection mFhrSCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBluetoothService = ((BluetoothService.BluetoothBinder) service).getService();
            mBluetoothService.setScanCallback(callback);
            mBluetoothService.scanDevice();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBluetoothService = null;
        }
    };

    /**
     * 扫描结果回调
     */
    private BluetoothService.Callback callback = new BluetoothService.Callback() {
        @Override
        public void onStartScan() {
            pb_loading.setVisibility(View.VISIBLE);
        }

        @Override
        public void onScanning(ScanResult result) {
            MyLog.e("============" + result.getDevice().getAddress());
            btDeviceAdapter.addResult(result);
            btDeviceAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScanComplete() {
            pb_loading.setVisibility(View.GONE);
        }

        @Override
        public void onConnecting() {

        }

        @Override
        public void onConnectFail() {
            MyToast.showLong(getContext(), "蓝牙连接失败");
        }

        @Override
        public void onDisConnected() {
            MyToast.showLong(getContext(), "蓝牙已断开");
        }

        @Override
        public void onServicesDiscovered() {

        }
    };


    /**
     * 权限检查
     */
    private void checkPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions, 12);
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
                if (mBluetoothService == null) {
                    bindService();
                } else {
                    mBluetoothService.scanDevice();
                }
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

}
