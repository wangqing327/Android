package com.example.mypassword;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.text.Html.fromHtml;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    long clicktime;
    final String TAG = "MainActivity";
    private static int selection_id = 0;//记住上次选中的ID
    SQLiteDatabase db;
    EditText edit;
    RecyclerView recycler;

    private List<com.example.mypassword.RecyclerViewAdapter.CarCaption> list = new ArrayList<>();
    private com.example.mypassword.RecyclerViewAdapter adapter;
    private ExRelativeLayout exRelativeLayout;
    private TextView textView, group1, group2, group3, group4, group5, group6;
    private LinearLayout layout;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initExRelativeLayout();
        button = findViewById(R.id.button1);
        button.setOnClickListener(this);
        edit = findViewById(R.id.ssk);
        edit.setSingleLine();//单行模式
        //取消自动完成功能
        //AutoEditAdapter madapter = new AutoEditAdapter(this, null, 0);
        //edit.setAdapter(madapter);

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //改变之前

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //正在改变
            }

            @Override
            public void afterTextChanged(Editable s) {
                //改变之后

                String str = s.toString();
                if (!str.trim().equals("")) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }
                // Log.d(TAG, "afterTextChanged: "+s);
                ppgjz(s.toString());
            }
        });
        db = MyAppModel.getDb(this);
        assert db != null;
        //init();
        recycler = findViewById(R.id.recyclerview);
        //动画效果
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(200);//加入效果
        animator.setRemoveDuration(200);//移除效果
        recycler.setItemAnimator(animator);
        //new manager(列数，纵向)
        StaggeredGridLayoutManager s_manger = new StaggeredGridLayoutManager
                (2, StaggeredGridLayoutManager.VERTICAL);

        recycler.setLayoutManager(s_manger);
        recycler.setPadding(5, 5, 5, 5);

        //设置Item间隔
//        HashMap<String, Integer> map = new HashMap<>();
//        map.put(RecyclerViewAdapter.RecyclerViewItemDecoration.TOP_DECORATION, 5);
//        map.put(RecyclerViewAdapter.RecyclerViewItemDecoration.BOTTOM_DECORATION, 10);
//        map.put(RecyclerViewAdapter.RecyclerViewItemDecoration.LEFT_DECORATION, 5);
//        map.put(RecyclerViewAdapter.RecyclerViewItemDecoration.RIGHT_DECORATION, 5);
//        RecyclerViewAdapter.RecyclerViewItemDecoration decoration = new RecyclerViewAdapter.RecyclerViewItemDecoration(map);
//        RecyclerViewAdapter.RecyclerViewItemDecoration decoration = new RecyclerViewAdapter.RecyclerViewItemDecoration();
//        recycler.addItemDecoration(decoration);
        //为list装载数据
        addDate();
        adapter = new RecyclerViewAdapter(list);
        recycler.setAdapter(adapter);
        adapter.setOnItemLongClickListener(new RecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                Menu menu = popupMenu.getMenu();
                popupMenu.getMenuInflater().inflate(R.menu.menu, menu);
                menu.removeItem(R.id.clear);
                menu.removeItem(R.id.about);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        zxcdcz(item, position);
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openxiangqing(position);
            }
        });


    }

    public void initExRelativeLayout() {
        try {
            exRelativeLayout = findViewById(R.id.title_layout);
            textView = findViewById(R.id.action_bar_title);
            exRelativeLayout.setContentView();
            group1 = findViewById(R.id.all);
            group1.setBackground(getDrawable(R.drawable.linelayout_line));
            group2 = findViewById(R.id.web);
            group3 = findViewById(R.id.email);
            group4 = findViewById(R.id.visa);
            group5 = findViewById(R.id.rj);
            group6 = findViewById(R.id.qt);

            group1.setOnClickListener(this);
            group2.setOnClickListener(this);
            group3.setOnClickListener(this);
            group4.setOnClickListener(this);
            group5.setOnClickListener(this);
            group6.setOnClickListener(this);


            imageView = findViewById(R.id.toporbootom);
//            设置图片框动画  起始度数  终止度数  起始X坐标  起始Y坐标
            final RotateAnimation animation_top = new RotateAnimation(0, 720,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//          动画时间 400ms
            animation_top.setDuration(400);
            final RotateAnimation animation_bot = new RotateAnimation(0, -720,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//          动画时间 400ms
            animation_bot.setDuration(400);
            //imageView.setAnimation(animation);


            layout = findViewById(R.id.xsdw);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (exRelativeLayout.isExpand()) {
                        exRelativeLayout.collapse();
                        imageView.setImageResource(R.drawable.bootom);
                        imageView.clearAnimation();
                        imageView.setAnimation(animation_top);
                        imageView.startAnimation(animation_top);
                    } else {
                        exRelativeLayout.setVisibility(View.INVISIBLE);
                        exRelativeLayout.expand();
                        imageView.setImageResource(R.drawable.top);
                        imageView.clearAnimation();
                        imageView.setAnimation(animation_bot);
                        imageView.startAnimation(animation_bot);
                    }
                    //执行动画

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openxiangqing(int pos) {
        /*  打开详情页面

         */
        RecyclerViewAdapter.CarCaption carCaption = list.get(pos);
        Intent intent = new Intent(MainActivity.this, ChaKan.class);
        intent.putExtra("id", carCaption.id);
        startActivityForResult(intent, 2);
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

    @SuppressLint("DefaultLocale")
    public void addDate() {


        try {
            //cursor = db.query("date", new String[]{"_id", "bz1", "\"group\"","bz2"}, null, null, null, null, null);
            Cursor cursor = db.rawQuery("select _id , bz1 , `group` , bz2  from date", null);
            while (cursor.moveToNext()) {
                RecyclerViewAdapter.CarCaption carCaption;
                String label, bz, group;
                int _id;
                label = cursor.getString(1);
                bz = cursor.getString(3);
                _id = cursor.getInt(0);
                group = cursor.getString(2);
                carCaption = new RecyclerViewAdapter.CarCaption(label, group, bz, _id);

                list.add(carCaption);
            }
            //载入HTML文本
            textView.setText(fromHtml(String.format("%s<font color = red>%d</font>%s", getResources().getString(R.string.title_left),
                    cursor.getCount(), getResources().getString(R.string.title_right))));

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void ppgjz(String where) {
        String sql = "select _id , bz1 , `group`,bz2 from date where bz1 like '%" + where
                + "%' OR bz2 like '%" + where + "%'";
        Cursor cursor = db.rawQuery(sql, null);
        adapter.removeAll();
        list.clear();
        textView.setText(fromHtml(String.format("%s<font color = red>%d</font>%s", getResources().getString(R.string.title_left),
                cursor.getCount(), getResources().getString(R.string.title_right))));
        while (cursor.moveToNext()) {
            RecyclerViewAdapter.CarCaption carCaption;
            String l = cursor.getString(2);
            carCaption = new RecyclerViewAdapter.CarCaption(cursor.getString(1), l, cursor.getString(3), cursor.getInt(0));
            list.add(carCaption);
        }
        cursor.close();
        adapter.thlist(list);
    }

    @SuppressLint("DefaultLocale")
    public void ppgjz2(String group) {
        String sql;
        if (group.equals("全部")) {
            sql = "select _id , bz1 , \"group\",bz2 from date";
        } else if (group.equals("邮件")) {
            sql = "select _id , bz1 ,\"group\",bz2 from date where \"group\" = '电子邮件'";
        } else {
            sql = "select _id ,bz1 ,\"group\",bz2 from date where \"group\" = '" + group + "'";
        }
        Cursor cursor = db.rawQuery(sql, null);
        adapter.removeAll();
        list.clear();
        textView.setText(fromHtml(String.format("%s<font color = red>%d</font>%s", getResources().getString(R.string.title_left),
                cursor.getCount(), getResources().getString(R.string.title_right))));
        while (cursor.moveToNext()) {
            RecyclerViewAdapter.CarCaption carCaption;
            String l = cursor.getString(2);
            carCaption = new RecyclerViewAdapter.CarCaption(cursor.getString(1), l,
                    cursor.getString(3), cursor.getInt(0));
            list.add(carCaption);
        }
        cursor.close();
        adapter.thlist(list);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();//关闭数据库

    }

    //返回键被按下
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                edit.setText("");
                edit.requestFocus();
                v.setVisibility(View.GONE);
                break;
            case R.id.all:
                if (selection_id != 0) {
                    rester();
                    group1.setBackground(getDrawable(R.drawable.linelayout_line));
                    selection_id = 0;
                    ppgjz2(group1.getText().toString());
                }
                break;
            case R.id.web:
                if (selection_id != 1) {
                    rester();
                    group2.setBackground(getDrawable(R.drawable.linelayout_line));
                    selection_id = 1;
                    ppgjz2(group2.getText().toString());
                }
                break;
            case R.id.email:
                if (selection_id != 2) {
                    rester();
                    group3.setBackground(getDrawable(R.drawable.linelayout_line));
                    selection_id = 2;
                    ppgjz2(group3.getText().toString());
                }
                break;
            case R.id.visa:
                if (selection_id != 3) {
                    rester();
                    group4.setBackground(getDrawable(R.drawable.linelayout_line));
                    selection_id = 3;
                    ppgjz2(group4.getText().toString());
                }
                break;
            case R.id.rj:
                if (selection_id != 4) {
                    rester();
                    group5.setBackground(getDrawable(R.drawable.linelayout_line));
                    selection_id = 4;
                    ppgjz2(group5.getText().toString());
                }
                break;
            case R.id.qt:
                if (selection_id != 5) {
                    rester();
                    group6.setBackground(getDrawable(R.drawable.linelayout_line));
                    selection_id = 5;
                    ppgjz2(group6.getText().toString());
                }
                break;
            case R.id.ssk:
                Log.d(TAG, "编辑框被单击");
                edit.requestFocus();
                break;
        }
    }

    public void rester() {
/*
还原表头background
 */

        edit.setText("");

        group1.setBackground(new ColorDrawable());
        group2.setBackground(new ColorDrawable());
        group3.setBackground(new ColorDrawable());
        group4.setBackground(new ColorDrawable());
        group5.setBackground(new ColorDrawable());
        group6.setBackground(new ColorDrawable());

    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0:
                return;
            case 1:
                //添加数据窗口返回的数据
                assert data != null;
                Bundle bundle = data.getExtras();
                assert bundle != null;
                String label = bundle.getString("label");
                String group = bundle.getString("group");
                String bz = bundle.getString("bz2");
                int id = bundle.getInt("id");
                RecyclerViewAdapter.CarCaption carCaption = new RecyclerViewAdapter.CarCaption(label, group, bz, id);

                if (adapter != null) {
                    adapter.addDate(carCaption);
                    textView.setText(fromHtml(String.format("%s<font color = red>%d</font>%s", getResources().getString(R.string.title_left),
                            adapter.getItemCount(), getResources().getString(R.string.title_right))));
                }
                break;
            case 2:
                //数据详情页面返回的数据
                assert data != null;
                String res = data.getStringExtra("result");
                if (res.equals("ok")) {
                    id = data.getIntExtra("id", 0);
                    label = data.getStringExtra("label");
                    group = data.getStringExtra("group");
                    bz = data.getStringExtra("bz2");
                    adapter.updateItem(id, label, group, bz);
                } else if ("delete".equals("delete")) {
                    id = data.getIntExtra("id", 0);
                    adapter.remove_id(id);
                    textView.setText(fromHtml(String.format("%s<font color = red>%d</font>%s", getResources().getString(R.string.title_left),
                            adapter.getItemCount(), getResources().getString(R.string.title_right))));
                }
                break;


        }
    }

    //创建菜单
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return zxcdcz(item, 0);
    }

    public boolean zxcdcz(MenuItem item, final int pos) {
        switch (item.getItemId()) {
            case R.id.add:
                //Toast.makeText(this,"Add",Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(this, AddDate.class), 1);
                break;
            case R.id.clear:
                edit.setText("");
                break;
            case R.id.about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
            case R.id.del:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.title)
                        .setMessage(R.string.message)
                        .setPositiveButton(R.string.but_1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //确认删除
                                RecyclerViewAdapter.CarCaption c = list.get(pos);
                                //i=影响行数
                                int i = db.delete("date", "_id = " + c.id, null);
                                if (i >= 1) {
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.del_tip_chenggong), Toast.LENGTH_SHORT).show();
                                    adapter.remove_position(pos);
                                    textView.setText(fromHtml(String.format("%s<font color = red>%d</font>%s",
                                            getResources().getString(R.string.title_left), adapter.getItemCount(), getResources().getString(R.string.title_right))));
                                } else {
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.del_tip_shibai), Toast.LENGTH_SHORT).show();
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
                break;
            case R.id.chakan:
                openxiangqing(pos);
                break;
            case R.id.edit:
                RecyclerViewAdapter.CarCaption carCaption = list.get(pos);
                Intent intent = new Intent(this, AddDate.class);
                intent.putExtra("id", carCaption.id);
                intent.putExtra("czlx", "recycler");//代表是从表格中直接过去修改的
                //startActivity(intent);
                startActivityForResult(intent, 1);

                break;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.removeItem(R.id.del);
        menu.removeItem(R.id.chakan);
        return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        //menu.add("删除此项数据");
        menu.add(R.string.del);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
        return true;
    }
}

