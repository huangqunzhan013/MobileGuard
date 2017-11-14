package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.app.backup.BackupDataInputStream;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.channels.SelectionKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;

/**
 * Created by Lenovo on 2017/11/10.
 */

public class AppInfoParser {
//    获取手机里面的所有的应用程序
    public static List<AppInfo> getAppInfos(Context context){
        //获取包管理器
        PackageManager pm=context.getPackageManager();
        //返回1个list，包含所有程序包括系统程序
        List<PackageInfo> packInfos=pm.getInstalledPackages(0);
        List<AppInfo> appinfos=new ArrayList<AppInfo>();
        for (PackageInfo packInfo:packInfos){
            AppInfo appinfo=new AppInfo();
            String packname=packInfo.packageName;
            appinfo.packageName=packname;
            Drawable icon=packInfo.applicationInfo.loadIcon(pm);
            appinfo.icon=icon;
            String appname=packInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName=appname;
            //应用程序apk包的路径
            String apkpath=packInfo.applicationInfo.sourceDir;
            appinfo.apkPath=apkpath;
            File file=new File(apkpath);
            long appSize=file.length();
            appinfo.appSize=appSize;

            String version=packInfo.versionName;
            appinfo.version=version;
            appinfo.installTime=new Date(packInfo.firstInstallTime).toLocaleString();
            try {
                PackageInfo packinfo=pm.getPackageInfo(packname,PackageManager.GET_SIGNATURES);
                byte[] signs=packinfo.signatures[0].toByteArray();
                CertificateFactory cf=CertificateFactory.getInstance("X509");
                X509Certificate certificate=(X509Certificate)cf.generateCertificate(new ByteArrayInputStream(signs));
                if (certificate!=null){
                    appinfo.signature=certificate.getIssuerDN().toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            PackageInfo packageInfo1=null;
            try {
                packageInfo1=pm.getPackageInfo(packname,PackageManager.GET_PERMISSIONS);
                if (packageInfo1.requestedPermissions!=null){
                    for (String pio : packageInfo1.requestedPermissions){
                        appinfo.permissions=appinfo.permissions+pio+"\n";
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            PackageInfo packageInfo2=null;
            try {
              packageInfo2=pm.getPackageInfo(packname,PackageManager.GET_ACTIVITIES);
                ActivityInfo[] appact=packageInfo2.activities;
                List<ActivityInfo> a=new ArrayList<>();
                    for (ActivityInfo pack:appact){
                        a.add(pack);
                        appinfo.appActivity=a.toString();
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //应用程序安装的位置
            int flags=packInfo.applicationInfo.flags;
            //表示安装在SD卡上的应用程序
            if((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags)!=0){
                appinfo.isInRoom=false;
            }else {
                //手机内存
                appinfo.isInRoom=true;
            }
            //表示系统程序
            if ((ApplicationInfo.FLAG_SYSTEM & flags)!=0){
                appinfo.isUserApp=false;
            }else {
                //用户应用
                appinfo.isUserApp=true;
            }
            appinfos.add(appinfo);
            appinfo=null;

        }
        return appinfos;
    }
}
