package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;

/**
 * Created by Lenovo on 2017/11/10.
 */

public class EngineUtils {
    //分享应用
    public static void shareApplication(Context context, AppInfo appInfo){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"推荐您使用一款软件，名字叫："+appInfo.appName
        +"下载路径：hrrps//play.google.com/store/apps/details?id="+appInfo.packageName);
        context.startActivity(intent);
    }

    //开启应用程序
    public static void startApplication(Context context,AppInfo appInfo){
        //打开这个应用程序的入口activity
        PackageManager pm=context.getPackageManager();
        Intent intent=pm.getLaunchIntentForPackage(appInfo.packageName);
        if (intent!=null){
            context.startActivity(intent);
        }else {
            Toast.makeText(context,"该应用没有启动界面",Toast.LENGTH_SHORT).show();
        }
    }

    //开启应用设置界面
    public static void SettingAppDetail(Context context,AppInfo appInfo){
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:"+appInfo.packageName));
        context.startActivity(intent);
    }

    //卸载应用
    public static void uninstallApplication(Context context,AppInfo appInfo){
        if(appInfo.isUserApp){
            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:"+appInfo.packageName));
            context.startActivity(intent);
        }
    }

    //关于应用
    public static void aboutApplication(Context context,AppInfo appInfo){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(appInfo.appName);
        builder.setMessage("Version:"+appInfo.version+"\n"+"Install time:"+appInfo.installTime+"\n"+"Certificate issuer:"+appInfo.signature+"\n"+"Permissions:"+"\n"+appInfo.permissions);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        }).create();
        builder.show();
    }

    //应用活动
    public static void appActivity(Context context,AppInfo appInfo){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Activity");
        builder.setMessage(appInfo.appActivity);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        }).create();
        builder.show();
    }

}
