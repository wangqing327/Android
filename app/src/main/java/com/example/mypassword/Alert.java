package com.example.mypassword;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Alert extends Dialog {

    public Alert(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        Alert malert;
        private int mode;
        private Button button;
        private View mlayout;
        private EditText pwd,  pwd2;


        public Builder(final Context context, int mode) {
            this.mode = mode;
            malert = new Alert(context, R.style.Theme_AppCompat_DayNight_Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (mode == 1) {
                mlayout = inflater.inflate(R.layout.alert_layout, null);
            } else if (mode == 2) {
                mlayout = inflater.inflate(R.layout.alert_zuce, null);
//                tip = mlayout.findViewById(R.id.Edit_tip1);
//                tip_x = mlayout.findViewById(R.id.Edit_tip1_x);
                pwd2 = mlayout.findViewById(R.id.Edit_pwd2);
            }
            pwd = mlayout.findViewById(R.id.Edit_pwd);
            button = mlayout.findViewById(R.id.alert_button);
            button.setTag(mode);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag();
                    DatabaseHelper helper = new DatabaseHelper(context, "p.db", null, 1);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    if (i == 1) {
                        //登录界面
                        String p = pwd.getText().toString();
                        Cursor cr = db.rawQuery("SELECT password from zpassword where password = '" + p + "'", null);
                        if (cr.getCount() >= 1) {
                            //登录成功
                            while (cr.moveToNext()) {
                                if (p.equals(cr.getString(0))) {
                                    //登录成功
                                    malert.dismiss();
                                }
                            }
                        } else {
                            Toast.makeText(context, "密码错误！", Toast.LENGTH_SHORT).show();
                            pwd.setText("");
                            pwd.requestFocus();
                        }
                        //关闭记录集
                        cr.close();
                    } else if (i == 2) {
                        //注册界面
                        String pw1 = pwd.getText().toString();
                        pw1 = pw1.trim();

                        String pw2 = pwd2.getText().toString();
                        pw2 = pw2.trim();//删除首尾空白
//
                        if (pw1.length() < 6) {//密码长度小于6不合格
                            Toast.makeText(context, "密码设置过短，请重新输入！", Toast.LENGTH_SHORT).show();
                            pwd2.setText("");
                            pwd.setText("");
                            pwd.requestFocus();//获得焦点

                        } else if (pw1.equals("") | pw2.equals("")) {
                            Toast.makeText(context, "字段设置不完整，请核查", Toast.LENGTH_SHORT).show();

                        } else if (!pw1.equals(pw2)) {
                            Toast.makeText(context, "两次输入密码不一致", Toast.LENGTH_SHORT).show();

                        } else {
                            //验证都通过
                            malert.dismiss();
                            ContentValues values = new ContentValues();
                            values.put("password", AES.encrypt("wangqing00",pw1));
                            values.put("date", MyAppModel.getdate());
                            //values.put("tip1", tip_text);
                            //values.put("tip1_x", tipx_text);
                            long id = db.insert("zpassword", null, values);
                            Log.d("Insert", "insertid-->" + id);
                        }

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



    }


}
