package com.example.mypassword;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyAppModel extends Application {
    private static long cstime;
    private String DbPath;
    public static String zpassword;
    private static final String DCMM = "MyPassword_DCDRMM_沙漠孤孤舟_wangqinq327";

    public void setDbPath(String path) {
        this.DbPath = path;
    }

    public String getDbPath() {
        return this.DbPath;
    }

    public static String getdate() {
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd- HH:mm:ss");
        return sdf.format(date);
    }

    public static String getZpassword() {
        return zpassword;
    }

    public static void setZpassword(String zpassword) {
        MyAppModel.zpassword = zpassword;
    }

    public static int getgroupID(String lable) {
        //网页","电子邮件", "银行卡", "软件", "其他

        if (lable.equals("网页")) {
            return 0;
        } else if (lable.equals("电子邮件")) {
            return 1;
        } else if (lable.equals("银行卡")) {
            return 2;
        } else if (lable.equals("软件")) {
            return 3;
        } else if (lable.equals("其他")) {
            return 4;
        }
        return 0;
    }
    public static SQLiteDatabase getDb(Context context) {
        //String path = getFileStreamPath("p.db").toString();

        DatabaseHelper helper = new DatabaseHelper(context, "p.db", null, 1);
        return helper.getWritableDatabase();
    }

    public static String getDcmm(){return DCMM;}

    public static void ShowToast(Context context,int msgID){
        Toast.makeText(context,msgID,Toast.LENGTH_SHORT).show();
    }
}
