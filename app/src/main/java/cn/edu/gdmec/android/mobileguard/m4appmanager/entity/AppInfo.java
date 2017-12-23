package cn.edu.gdmec.android.mobileguard.m4appmanager.entity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Array;

/**
 * Created by Lenovo on 2017/11/10.
 */

public class AppInfo {
    public String packageName;
    public Drawable icon;
    public String appName;
    public String apkPath;
    public long appSize;
    public boolean isInRoom;
    public boolean isUserApp;
    public boolean isSelected=false;
    public String version;
    public String installTime;
    public String signature;
    public String permissions;
    public String appActivity;
    public String getAppLocation(boolean isInRoom){
        if(isInRoom){
            return "手机内存";
        }else {
            return "外部存储";
        }
    }
    public boolean isLock;
}
