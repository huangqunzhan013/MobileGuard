package cn.edu.gdmec.android.mobileguard.m4appmanager.entity;

import android.graphics.drawable.Drawable;

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

    public String getAppLocation(boolean isInRoom){
        if(isInRoom){
            return "手机内存";
        }else {
            return "外部存储";
        }
    }
}