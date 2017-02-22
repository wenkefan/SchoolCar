package com.fwk.school4.weight;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.fwk.school4.constant.Keyword;
import com.fwk.school4.listener.FacheListener;
import com.fwk.school4.model.BanciBean;
import com.fwk.school4.model.ZuofeiBean;
import com.fwk.school4.network.HTTPURL;
import com.fwk.school4.network.api.ZuofeiNetWork;
import com.fwk.school4.ui.Jie.JieStationMapActivity;
import com.fwk.school4.ui.MainActivity;
import com.fwk.school4.ui.Song.SongStationMapActivity;
import com.fwk.school4.utils.SharedPreferencesUtils;

/**
 * Created by fanwenke on 16/12/14.
 */

public class MainDialog {
    static FacheListener listener;

    public static void setBackListener(FacheListener listeners) {
        listener = listeners;
    }

    /**
     * 是本人的班次  点击时的弹窗
     *
     * @param context
     * @param name
     * @param fangxiang
     * @param position
     */
    public static void ShowJRBanci(final Activity context, String name, final int fangxiang, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示").setMessage("\"" + name + "\"" + "是否发车");
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent;
                if (fangxiang == 1) {
                    //接幼儿
                    intent = new Intent(context, JieStationMapActivity.class);

                } else {
                    //送幼儿
                    intent = new Intent(context, SongStationMapActivity.class);

                }
                listener.BackListener(intent);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 点击别人的班次时的弹窗
     */
    public static void ShowDLBanci(final Activity context, String name, String teachername, final int fangxiang, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("警告").setMessage(name + "的随车老师是：" + teachername + "\n" + "是否代理发车");
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent;
                if (fangxiang == 1) {
                    //接幼儿
                    intent = new Intent(context, JieStationMapActivity.class);

                } else {
                    //送幼儿
                    intent = new Intent(context, SongStationMapActivity.class);

                }

                listener.BackListener(intent);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 推出重新进入时
     */
    public static void Beagin(final MainActivity context, BanciBean.RerurnValueBean bean) {
        String name = bean.getBusScheduleName();
        final int fangxiang = bean.getAttendanceDirections();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("警告").setMessage(name + "正在运行中...");
        builder.setNegativeButton("返回列表", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.requestBanci();
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent;
                if (fangxiang == 1) {
                    //接幼儿
                    intent = new Intent(context, JieStationMapActivity.class);

                } else {
                    //送幼儿
                    intent = new Intent(context, SongStationMapActivity.class);
                }
                intent.putExtra(Keyword.POTIONIT, -1);
                context.startActivity(intent);
                context.finish();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 未全部上车或者下车
     */
    public static boolean jixu = false;

    public static void Shangxiac(final Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("警告").setMessage("还有学生没有上下车，是否发车？");
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                jixu = true;
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 点击其他班次，去引导作废
     *
     * @param context
     * @param bean
     */
    public static void YYX(final Activity context, BanciBean.RerurnValueBean bean) {
        String name = bean.getBusScheduleName();
        final int fangxiang = bean.getAttendanceDirections();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("警告").setMessage("已有班次：" + name + "正在运行中。" + "\n继续进行请注销班次：" + name);
        builder.setNegativeButton("前往注销", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent;
                if (fangxiang == 1) {
                    //接幼儿
                    intent = new Intent(context, JieStationMapActivity.class);

                } else {
                    //送幼儿
                    intent = new Intent(context, SongStationMapActivity.class);
                }
                intent.putExtra(Keyword.POTIONIT, -1);
                intent.putExtra(Keyword.SELECTZUOFEI, true);
                context.startActivity(intent);
                context.finish();
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public static void ZF(final Activity context, final ZuofeiNetWork work) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle("警告").setMessage("确定注销本此记录？");
        builder.setNegativeButton("注销", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferencesUtils sp = new SharedPreferencesUtils();

                work.setUrl(Keyword.ZUOFEI,HTTPURL.API_ZUO_FEI + sp.getInt(Keyword.SP_PAICHEDANHAO), ZuofeiBean.class);
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
