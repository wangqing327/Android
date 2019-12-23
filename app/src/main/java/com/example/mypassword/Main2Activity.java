package com.example.mypassword;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.widget.Toast.LENGTH_SHORT;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {
    private Button login;
    private EditText edit_pwd;
    private SQLiteDatabase db;
    private ScrollView gdrq;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            login();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//
//        如果Activity继承AppCompatAcitiviy：getSupportActionBar().hide();
//
//        如果Acitivy继承Acitivy:requestWindowFeature(Window.FEATURE_NO_TITLE).

        getSupportActionBar().hide();//去除标题栏
        db = MyAppModel.getDb(this);
        init();
        setContentView(R.layout.activity_main2);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
        edit_pwd = findViewById(R.id.dlmm);
//监测回车键
        edit_pwd.setOnKeyListener(this);
        gdrq = findViewById(R.id.gdrq);
        //纵向、横向滚动条隐藏
        gdrq.setVerticalScrollBarEnabled(false);
        gdrq.setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onDestroy() {
        if (db != null)
            db.close();//关闭数据库
        super.onDestroy();
    }

    //检查数据库中是否有主密码
    public void init() {
        int mode = 0;
        Cursor cursor = db.rawQuery("select _id,password from zpassword order by _id desc", null);
        if (cursor.getCount() <= 0) {
            mode = 2;
            cursor.close();
            Alert dig = new Alert.Builder(this, mode).Create();
            dig.show();
            Window wd = dig.getWindow();
            assert wd != null;
            WindowManager.LayoutParams lp = wd.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wd.setAttributes(lp);
            dig.setCancelable(false);//返回键关闭取消
        } else {

        }

    }

    private long clicktime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - clicktime) > 2000) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.isExit),
                        Toast.LENGTH_SHORT).show();
                clicktime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            //Toast.makeText(this,"回车被按下", LENGTH_SHORT).show();

            return (login());
        }
        return false;

    }

    public boolean login() {
        String pwd = edit_pwd.getText().toString();
        Cursor cursor = db.rawQuery("select password from zpassword where password = '" + AES.encrypt("wangqing00", pwd) + "'", null);
        if (cursor.getCount() >= 1) {
            // Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT)
            //       .show();
            //载入主界面
            startActivity(new Intent(this, MainActivity.class));
            //设置全局密码
            MyAppModel.setZpassword(pwd);

            //销毁方法
            //finish也会经过onDestory
            finish();
            //this.onDestroy();
            // System.exit(0);
            //android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            Toast.makeText(this, getResources().getString(R.string.pwdError),
                    LENGTH_SHORT).show();

            edit_pwd.requestFocus();

        }
        cursor.close();//关闭记录集
        return true;
    }
}
