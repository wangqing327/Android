package com.example.mypassword;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;


public class Alert extends Dialog {

    public Alert(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        Alert malert;
        private int mode;
        private Button button;
        private View mlayout;
        private EditText pwd, pwd2, pwd3;


        public Builder(final Context context, int mode) {
            this.mode = mode;
            malert = new Alert(context, R.style.Theme_AppCompat_DayNight_Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (mode == 1) {
                mlayout = inflater.inflate(R.layout.alert_layout, null);
                pwd3 = mlayout.findViewById(R.id.Edit_pwd1);
            } else if (mode == 2) {
                mlayout = inflater.inflate(R.layout.alert_zuce, null);
            }
            pwd = mlayout.findViewById(R.id.Edit_pwd);
            pwd2 = mlayout.findViewById(R.id.Edit_pwd2);
            button = mlayout.findViewById(R.id.alert_button);
            button.setTag(mode);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase db = MyAppModel.getDb(context);
                    switch ((int) v.getTag()) {
                        case 1:
                            //修改登录密码
                            String p2 = pwd2.getText().toString();
                            String p3 = pwd3.getText().toString();
                            if (!p2.trim().equals(p3.trim())) {
                                MyAppModel.ShowToast(context, R.string.pwd_yz);
                                pwd3.setText("");
                                pwd2.setText("");
                                pwd3.requestFocus();
                                return;
                            }
                            String p = pwd.getText().toString();
                            String _p = AES.encrypt("wangqing00",p);
                            Cursor cr = db.rawQuery("SELECT password from zpassword where password = '"
                                    + AES.encrypt("wangqing00", p) + "'", null);
                            if (cr.getCount() >= 1) {
                                //登录成功
                                cr.moveToNext();
                                if (_p.equals(cr.getString(0))) {
                                    ghmm(p,p2,db);
                                    Toast.makeText(context, context.getResources().getString(R.string.szmmcg) +
                                            ":\n" + p2.trim(), Toast.LENGTH_SHORT).show();
                                    malert.dismiss();
                                } else {
                                    MyAppModel.ShowToast(context, R.string.szmmsb);
                                }

                            } else {
                                //密码错误
                                MyAppModel.ShowToast(context, R.string.pwdError);
                                pwd.setText("");
                                pwd.requestFocus();
                            }
                            //关闭记录集
                            cr.close();
                            break;
                        case 2:
                            //注册界面
                            String pw1 = pwd.getText().toString();
                            pw1 = pw1.trim();
                            String pw2 = pwd2.getText().toString();
                            pw2 = pw2.trim();//删除首尾空白//
                            if (pw1.length() < 6) {//密码长度小于6不合格
                                //密码设置过短，请重新输入！
                                MyAppModel.ShowToast(context, R.string.pwd_yz3);
                                pwd2.setText("");
                                pwd.setText("");
                                pwd.requestFocus();//获得焦点
                            } else if (TextUtils.isEmpty(pw1) || TextUtils.isEmpty(pw2)) {
                                //字段设置不完整，请核查
                                MyAppModel.ShowToast(context, R.string.pwd_yz2);
                            } else if (!pw1.equals(pw2)) {
                                //两次输入密码不一致
                                pwd2.setText("");
                                pwd.setText("");
                                pwd.requestFocus();//获得焦点
                                MyAppModel.ShowToast(context, R.string.pwd_yz);
                            } else {
                                //验证都通过
                                malert.dismiss();
                                ContentValues values = new ContentValues();
                                values.put("password", AES.encrypt("wangqing00", pw1));
                                values.put("date", MyAppModel.getdate());
                                long id = db.insert("zpassword", null, values);
                                //Log.d("Insert", "insertid-->" + id);
                                if (id > 0) {
                                    Toast.makeText(context, context.getResources().getString(R.string.szmmcg) +
                                            ":" + pw1, Toast.LENGTH_SHORT).show();
                                } else {
                                    MyAppModel.ShowToast(context, R.string.szmmsb);
                                }

                            }
                            break;

                    }

                }
            });

        }

        public Alert Create() {
            malert.setContentView(mlayout);
            malert.setCancelable(true);//用户可以点击回退键关闭
            malert.setCanceledOnTouchOutside(false);//界外触摸关闭
            return malert;
        }

        private void ghmm(final String ymm, final String xmm, final SQLiteDatabase db) {
            new Thread(new Runnable() {
                String sql = "select _id,user,password,url from date";
                @Override
                public void run() {
                    try {
                        Cursor cursor = db.rawQuery(sql, null);
                        if (cursor.getCount() <= 0) {
                            return;
                        }
                        while (cursor.moveToNext()) {
                            String user = cursor.getString(cursor.getColumnIndex("user"));
                            String pwd = cursor.getString(cursor.getColumnIndex("password"));
                            String url = cursor.getString(cursor.getColumnIndex("url"));
                            int _id = cursor.getInt(cursor.getColumnIndex("_id"));

                            user = AES.decrypt(ymm, user);
                            user = AES.encrypt(xmm, user);

                            pwd = AES.decrypt(ymm, pwd);
                            pwd = AES.encrypt(xmm, pwd);

                            url = AES.decrypt(ymm, url);
                            url = AES.encrypt(xmm, url);

                            ContentValues values = new ContentValues();
                            values.put("user", user);
                            values.put("password", pwd);
                            values.put("url", url);
                            int i = db.update("date", values, "_id = " + _id, null);
                            Log.d("update", "ghmm:" + i);
                            values.clear();
                        }
                        cursor.close();
                        //更新主密码
                        ContentValues val = new ContentValues();
                        val.put("date",MyAppModel.getdate());
                        val.put("password",AES.encrypt("wangqing00",xmm));
                        int i = db.update("zpassword",val,"_id = 1",null);
                        val.clear();
                        MyAppModel.setZpassword(xmm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


}
