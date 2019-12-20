package com.example.mypassword;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;

public class ChaKan extends AppCompatActivity implements View.OnLongClickListener {
    private ImageView img;
    private TextView user, pwd, url, bzm, bz;
    private SQLiteDatabase db;
    private LinearLayout group1, group2, group3, group4, group5;
    private int id;
    private String group;

    @Override
    protected void onDestroy() {
        if (db.isOpen())
            db.close();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //return super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("result", "ok");
            intent.putExtra("id", id);
            intent.putExtra("group", group);
            intent.putExtra("label", bzm.getText().toString());
            intent.putExtra("bz2", bz.getText().toString());
            setResult(2, intent);
            finish();
            return true;
        }


        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cha_kan);
        user = findViewById(R.id.user);
        pwd = findViewById(R.id.pwd);
        url = findViewById(R.id.url);
        bzm = findViewById(R.id.bzm);
        bz = findViewById(R.id.bz);

        //线性布局器设置长按事件
        group1 = findViewById(R.id.group1);
        group2 = findViewById(R.id.group2);
        group3 = findViewById(R.id.group3);
        group4 = findViewById(R.id.group4);
        group5 = findViewById(R.id.group5);
        group1.setOnLongClickListener(this);
        group2.setOnLongClickListener(this);
        group3.setOnLongClickListener(this);
        group4.setOnLongClickListener(this);
        group5.setOnLongClickListener(this);

        setTitle(R.string.Name_all);
        img = findViewById(R.id.bstp);
        db = MyAppModel.getDb(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        if (id > 0) {
            cxsj(id);
        }
    }


    public void cxsj(int id) {
        String sql = "select user,password,\"group\",url,bz1,bz2 from date where _id =" + id;
        try {
            Cursor cursor = db.rawQuery(sql, null);
            String zmm = MyAppModel.getZpassword();
            if (cursor.moveToNext()) {
                url.setText(AES.decrypt(zmm, cursor.getString(cursor.getColumnIndex("url"))));
                pwd.setText(AES.decrypt(zmm, cursor.getString(cursor.getColumnIndex("password"))));
                user.setText(AES.decrypt(zmm, cursor.getString(cursor.getColumnIndex("user"))));
                bzm.setText(cursor.getString(cursor.getColumnIndex("bz1")));
                bz.setText(cursor.getString(cursor.getColumnIndex("bz2")));
                group = cursor.getString(2);
                if (group.equals(getResources().getString(R.string.wangye))) {
                    img.setImageResource(R.drawable.wangye);
                } else if (group.equals(getResources().getString(R.string.dzyj))) {
                    img.setImageResource(R.drawable.youjian);
                } else if (group.equals(getResources().getString(R.string.yhk))) {
                    img.setImageResource(R.drawable.visa);
                } else if (group.equals(getResources().getString(R.string.rj))) {
                    img.setImageResource(R.drawable.ruanjian_2);
                } else if (group.equals(getResources().getString(R.string.qt))) {
                    img.setImageResource(R.drawable.yonghu);
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onLongClick(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        Menu menu = popupMenu.getMenu();
        //getMenuInflater().inflate(R.menu.update_menu, menu);
        //menu.clear();
        menu.add(R.string.edit);
        menu.add(R.string.del);
        switch (v.getId()) {
            case R.id.group1:
                break;
            case R.id.group2:
                menu.add(R.string.copy_user);
                break;
            case R.id.group3:
                menu.add(R.string.copy_pwd);
                break;
            case R.id.group4:
                String tmp = url.getText().toString();
                if (TextUtils.isEmpty(tmp))
                    break;
                menu.add(R.string.open);
                menu.add(R.string.copy_url);
                break;
            case R.id.group5:
                tmp = bz.getText().toString();
                if (TextUtils.isEmpty(tmp))
                    break;
                menu.add(R.string.copy_bz);
                break;

        }


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mymenuclick(item);
                return false;

            }

        });

        popupMenu.show();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case 101:
                id = data.getIntExtra("id", 0);
                String muser, mpwd, murl, mbz;
                muser = data.getStringExtra("user");
                user.setText(muser);
                mpwd = data.getStringExtra("pwd");
                pwd.setText(mpwd);
                murl = data.getStringExtra("url");
                url.setText(murl);
                mbz = data.getStringExtra("bz");
                bzm.setText(mbz);
                group = data.getStringExtra("group");
                //资源ID
                img.setImageResource(data.getIntExtra("res", 0));
                bz.setText(data.getStringExtra("bz2"));
                break;
            case 1:
                setResult(1,data);
                finish();
        }
    }

    public void mymenuclick(MenuItem item) {
        /*
        自定义菜单操作项
         */
        try {
            String title = item.getTitle().toString();
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data;
            String tmp;
            if (title.equals(getResources().getString(R.string.edit))) {
                //修改数据
                Intent intent = new Intent(this, AddDate.class);
                intent.putExtra("id", id);
                intent.putExtra("czlx", "xiangqing");//代表是从详情页面直接过去修改的
                intent.putExtra("user", user.getText().toString());
                intent.putExtra("pwd", pwd.getText().toString());
                intent.putExtra("url", url.getText().toString());
                intent.putExtra("bz", bzm.getText().toString());
                intent.putExtra("bz2", bz.getText().toString());
                intent.putExtra("group", group);
                //startActivity(intent);
                startActivityForResult(intent, 101);
            } else if (title.equals(getResources().getString(R.string.copy_user))) {
                data = ClipData.newPlainText("Label", user.getText().toString());
                cm.setPrimaryClip(data);
                tipmessage(R.string.toast_copy_user);

            } else if (title.equals(getResources().getString(R.string.copy_pwd))) {
                data = ClipData.newPlainText("Label", pwd.getText().toString());
                cm.setPrimaryClip(data);
                tipmessage(R.string.toast_copy_pwd);
            } else if (title.equals(getResources().getString(R.string.copy_url))) {
                tmp = url.getText().toString();
                if (tmp.isEmpty()) {
                    tipmessage(R.string.toast_copy_null);
                } else {
                    data = ClipData.newPlainText("Label", tmp);
                    cm.setPrimaryClip(data);
                    tipmessage(R.string.toast_copy_url);
                }
            } else if (title.equals(getResources().getString(R.string.open))) {
                tmp = url.getText().toString();
                if (tmp.startsWith("http://") | tmp.startsWith("htps://") | tmp.startsWith("bbs://")
                        | tmp.startsWith("email://") | tmp.startsWith("ftp://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmp));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else if (title.equals(getResources().getString(R.string.copy_bz))) {
                data = ClipData.newPlainText("Label", bz.getText().toString());
                cm.setPrimaryClip(data);
                tipmessage(R.string.toast_copy_bz);
            } else if ("添加数据".equals(title)) {
                Intent intent = new Intent(this, AddDate.class);
                startActivityForResult(intent,1);
            } else if ("删除数据".equals(title)) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.title)
                        .setMessage(R.string.message)
                        .setPositiveButton(R.string.but_1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //确认删除
                                //i=影响行数
                                int i = db.delete("date", "_id = " + id, null);
                                if (i >= 1) {
                                    Toast.makeText(ChaKan.this, getResources().getString(R.string.del_tip_chenggong), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("result", "delete");
                                    intent.putExtra("id", id);
                                    setResult(2, intent);
                                    finish();
                                } else {
                                    Toast.makeText(ChaKan.this, getResources().getString(R.string.del_tip_shibai), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.but_2, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelable(true)
                        .create();
                dialog.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tipmessage(int resid) {
        Toast.makeText(this, getResources().getString(resid), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item != null)
            mymenuclick(item);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return super.onMenuOpened(featureId, menu);
    }
}
