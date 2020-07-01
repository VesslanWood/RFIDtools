package cn.rrg.rdv.activities.connect;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import cn.rrg.rdv.R;
import cn.rrg.rdv.activities.main.PM3FlasherMainActivity;
import cn.rrg.rdv.activities.tools.DeviceConnectActivity;
import cn.rrg.rdv.activities.tools.Proxmark3NewTerminalInitActivity;
import cn.rrg.rdv.callback.ConnectFailedCtxCallback;
import cn.rrg.rdv.models.AbstractDeviceModel;
import cn.rrg.rdv.models.Proxmark3Rdv4SppModel;
import cn.rrg.rdv.models.Proxmark3Rdv4UsbModel;
import cn.dxl.common.util.PermissionUtil;

/**
 * 专供RDV4连接设备
 * 可以使用USB 与 SPP两种方式连接设备
 *
 * @author DXL
 */
public class Proxmark3Rdv4RRGConnectActivity
        extends DeviceConnectActivity implements ConnectFailedCtxCallback {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //这里我们申请一下权限，因为RDV4需要蓝牙!
        PermissionUtil pu = new PermissionUtil(this);
        pu.setRequestCode(0x666);
        pu.request(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    public AbstractDeviceModel[] getModels() {
        return new AbstractDeviceModel[]{
                new Proxmark3Rdv4SppModel(),
                new Proxmark3Rdv4UsbModel()
        };
    }

    @Override
    public Class getTarget() {
        return Proxmark3NewTerminalInitActivity.class;
    }

    @Override
    public String getConnectingMsg() {
        return getString(R.string.tips_view_use_termux);
    }

    @Override
    public ConnectFailedCtxCallback getCallback() {
        return this;
    }

    @Override
    public void onFailed(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.connect_faild)
                        .setMessage(R.string.connect_errr_msg_1)
                        .setPositiveButton(getString(R.string.flash) + "(OTG)", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(context, PM3FlasherMainActivity.class));
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }
        });
    }
}
