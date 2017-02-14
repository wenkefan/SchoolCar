package com.fwk.school4.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.widget.Toast;


/**
 * 当网络发生变化时，接收广播并对用户进行提示
 * @author wzz
 * @time 2016/5/11
 *
 */

/**
 * 测试当前网络的变化状态
 * 	wifi  - gprs - 没有网络  (通过广播实现)
 * @author _wzz
 *
 */
public class NetStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        boolean success = false;
        //获得网络连接服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // State state = connManager.getActiveNetworkInfo().getState();
        State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
        if (State.CONNECTED == state) { // 判断是否正在使用WIFI网络
            success = true;
            System.out.println("wzz--------:您的网络已连接到wifi");
            Toast.makeText(context, "您已连接到wifi", Toast.LENGTH_SHORT).show();


            return;
        }
        State state1 = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态
        if (State.CONNECTED == state1) { // 判断是否正在使用GPRS网络
            success = true;
            System.out.println("wzz--------:您的网络已连接到net");
            Toast.makeText(context, "您已连接到2G/3G/4G网络", Toast.LENGTH_SHORT).show();


            return;
        }
        if (!success) {
            Toast.makeText(context, "您的网络连接已中断", Toast.LENGTH_SHORT).show();
        }

    }


}
