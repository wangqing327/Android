package com.example.mypassword;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddDate extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button qrn, qxn;
    private EditText edit_user, edit_pwd, edit_bz, edit_url, edit_bz2;
    private Spinner spinner;
    private SQLiteDatabase db;
    private String[] group;// =getResources().getStringArray(R.array.spinner);
    private int position = 0, id = 0, res_id;
    private ImageView img, img2;
    private ScrollView gdrq;
    private String xq;
    private LinearLayout layout;
    private TextView help_text;
    private boolean isExpand;//指示帮助菜单是否打开，初始值是未打开  isExpand = false;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);
        spinner = findViewById(R.id.group);

        group = getResources().getStringArray(R.array.spinner);
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, group);
        spinner.setDropDownVerticalOffset(100);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        qrn = findViewById(R.id.bcxx);
        qxn = findViewById(R.id.qx);
        qrn.setOnClickListener(this);
        qxn.setOnClickListener(this);

        edit_user = findViewById(R.id.user);
        edit_pwd = findViewById(R.id.pwd);
        //获得焦点时全部选中
        edit_pwd.setSelectAllOnFocus(true);
        edit_url = findViewById(R.id.url);
        edit_bz = findViewById(R.id.bz);
        edit_bz2 = findViewById(R.id.bz2);

        layout = findViewById(R.id.help);
        layout.setOnClickListener(this);
        img2 = findViewById(R.id.help_img);
        help_text = findViewById(R.id.help_txt);


        db = MyAppModel.getDb(this);

        img = findViewById(R.id.bstp);

        gdrq = findViewById(R.id.agdrq);
        //纵向、横向滚动条隐藏
        gdrq.setVerticalScrollBarEnabled(false);
        gdrq.setHorizontalScrollBarEnabled(false);

        setTitle(R.string.Name_add);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        if (id > 0) {
            try {
                putDate(id, intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void putDate(int id, Intent intent) {
        xq = intent.getStringExtra("czlx");
        String user, pwd, url, bz, group, bz2;
        if (xq.equals("xiangqing")) {
            user = intent.getStringExtra("user");
            pwd = intent.getStringExtra("pwd");
            url = intent.getStringExtra("url");
            bz = intent.getStringExtra("bz");
            group = intent.getStringExtra("group");
            bz2 = intent.getStringExtra("bz2");
            edit_url.setText(url);
            edit_user.setText(user);
            edit_pwd.setText(pwd);
            edit_bz.setText(bz);
            edit_bz2.setText(bz2);
            ////"网页","电子邮件", "银行卡", "软件", "其他"
            //if(group.equals(""))
            int position = MyAppModel.getgroupID(group);
            spinner.setSelection(position);
            img.setImageDrawable(getDrawble(position));
        } else if (xq.equals("recycler")) {
            try {
                Cursor cursor = db.rawQuery("select _id,`group`,bz1,user,password,url,bz2 from date where _id =" + id, null);
                if (cursor.moveToNext()) {
                    user = cursor.getString(3);
                    pwd = cursor.getString(4);
                    url = cursor.getString(5);
                    bz = cursor.getString(2);
                    group = cursor.getString(1);
                    bz2 = cursor.getString(6);
                    edit_url.setText(AES.decrypt(MyAppModel.getZpassword(), url));
                    edit_user.setText(AES.decrypt(MyAppModel.getZpassword(), user));
                    edit_pwd.setText(AES.decrypt(MyAppModel.getZpassword(), pwd));
                    edit_bz.setText(bz);
                    edit_bz2.setText(bz2);
                    ////"网页","电子邮件", "银行卡", "软件", "其他"
                    //if(group.equals(""))
                    int position = MyAppModel.getgroupID(group);
                    spinner.setSelection(position);
                    img.setImageDrawable(getDrawble(position));
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy() {
        if (db != null) {
            db.close();
        }
        id = 0;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bcxx:
                String pwd, user, url, bz, riqi;
                riqi = MyAppModel.getdate();
                pwd = edit_pwd.getText().toString();
                user = edit_user.getText().toString();
                url = edit_url.getText().toString();
                bz = edit_bz.getText().toString();
                if (bz.isEmpty()) {
                    Toast.makeText(this, getResources().getString(R.string.jysb), Toast.LENGTH_SHORT).show();
                } else if (user.equals("")) {
                    Toast.makeText(this, getResources().getString(R.string.jysb3), Toast.LENGTH_SHORT).show();
                } else {
                    //验证都通过
                    ContentValues values = new ContentValues();
                    values.put("password", AES.encrypt(MyAppModel.zpassword, pwd));
                    values.put("url", AES.encrypt(MyAppModel.zpassword, url));
                    values.put("`group`", group[position]);
                    values.put("bz1", bz);
                    values.put("user", AES.encrypt(MyAppModel.zpassword, user));
                    values.put("date", riqi);
                    values.put("bz2", edit_bz2.getText().toString());
                    long longid;
                    boolean insert;
                    if (id == 0) {
                        //新添加数据并返回新数据Id
                        longid = db.insert("date", null, values);
                        id = new Long(longid).intValue();
                        insert = true;
                    } else {
                        //修改数据
                        longid = db.update("date", values, "_id = " + id, null);
                        insert = false;
                    }
                    values.clear();
                    Toast.makeText(this, longid > 0 ? getResources().getString(R.string.bccg) : getResources().getString(R.string.bcsb), Toast.LENGTH_SHORT).show();

                    if (longid > 0) {

                        //是否添加或修改成功
                        int resultCode = 0;
                        Intent intent = new Intent();
                        intent.putExtra("id", id);
                        intent.putExtra("bz2", edit_bz2.getText().toString());
                        intent.putExtra("group", group[position]);
                        if (TextUtils.isEmpty(xq)) {
                            resultCode = 1;
                            intent.putExtra("label", bz);
                        } else if (xq.equals("recycler")) {
                            intent.putExtra("result", "ok");
                            intent.putExtra("label", bz);
                            resultCode = 2;
                        } else if (xq.equals("xiangqing")) {
                            intent.putExtra("bz", bz);

                            intent.putExtra("res", res_id);
                            intent.putExtra("user", user);
                            intent.putExtra("url", url);
                            intent.putExtra("pwd", pwd);
                            resultCode = 101;
                        }
                        setResult(resultCode, intent);
                        finish();
                    }
                }
                break;

            case R.id.qx:
                setResult(0, null);
                finish();
                break;
            case R.id.help:
                //打开帮助菜单
                Animation animation;
                RotateAnimation rotateAnimation;
                if (!isExpand) {
                    //打开
                    help_text.setVisibility(View.INVISIBLE);
                    //滚动到底部  直接使用fullScroll无用，这里用子程序添加到消息队列中去

                    fullScroll(ScrollView.FOCUS_DOWN);


                    animation = AnimationUtils.loadAnimation(this, R.anim.expand);
                    animation.setDuration(300);
                    animation.setAnimationListener(new AnimListener());
                    rotateAnimation = new RotateAnimation(0, 720,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(300);
                    img2.setImageResource(R.drawable.top);


                    isExpand = true;
                } else {
                    animation = AnimationUtils.loadAnimation(this, R.anim.collapse);
                    animation.setAnimationListener(new AnimListener());
                    animation.setDuration(300);
                    rotateAnimation = new RotateAnimation(0, -720,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotateAnimation.setDuration(300);
                    img2.setImageResource(R.drawable.bootom);

                    isExpand = false;
                }
                help_text.clearAnimation();
                help_text.startAnimation(animation);
                img2.clearAnimation();
                img2.startAnimation(rotateAnimation);
        }
    }

    public void fullScroll(final int index) {
        //滚动到底部  直接使用fullScroll无用，这里添加到消息队列中去
        gdrq.post(new Runnable() {
            @Override
            public void run() {
                gdrq.fullScroll(index);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        img.setImageDrawable(getDrawble(position));
        switch (position) {
            //"网页","电子邮件", "银行卡", "软件", "其他"

            case 0://网页
                edit_url.setHint(R.string.url);
                setEdit_Type(InputType.TYPE_CLASS_TEXT, InputType.TYPE_TEXT_VARIATION_URI,
                        InputType.TYPE_TEXT_VARIATION_URI, InputType.TYPE_CLASS_TEXT,
                        InputType.TYPE_TEXT_VARIATION_URI);
                break;
            case 1:
                edit_url.setHint(R.string.email);
                setEdit_Type(InputType.TYPE_CLASS_TEXT, InputType.TYPE_TEXT_VARIATION_URI,
                        InputType.TYPE_TEXT_VARIATION_URI, InputType.TYPE_CLASS_TEXT,
                        InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
                break;
            case 2:
                edit_url.setHint(R.string.visa);
                setEdit_Type(InputType.TYPE_CLASS_TEXT, InputType.TYPE_TEXT_VARIATION_URI,
                        InputType.TYPE_TEXT_VARIATION_URI, InputType.TYPE_CLASS_TEXT,
                        InputType.TYPE_CLASS_NUMBER);
                break;
            case 3:
                edit_url.setHint(R.string.run);
                setEdit_Type(InputType.TYPE_CLASS_TEXT, InputType.TYPE_TEXT_VARIATION_URI,
                        InputType.TYPE_TEXT_VARIATION_URI, InputType.TYPE_CLASS_TEXT,
                        InputType.TYPE_CLASS_TEXT);
                break;
            case 4:
                edit_url.setHint(R.string.bzxx2);
                setEdit_Type(InputType.TYPE_CLASS_TEXT, InputType.TYPE_TEXT_VARIATION_URI,
                        InputType.TYPE_TEXT_VARIATION_URI, InputType.TYPE_CLASS_TEXT,
                        InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setEdit_Type(int label, int user, int pwd, int bz, int url) {
        edit_bz.setInputType(label);
        edit_user.setInputType(user);
        edit_pwd.setInputType(pwd);
        edit_bz2.setInputType(bz);
        edit_url.setInputType(url);
    }

    public Drawable getDrawble(int position) {
        //网页","电子邮件", "银行卡", "软件", "其他
        switch (position) {
            case 0:
                res_id = R.drawable.wangye;
                return getResources().getDrawable(R.drawable.wangye, null);
            case 1:
                res_id = R.drawable.youjian;
                return getResources().getDrawable(R.drawable.youjian, null);
            case 2:
                res_id = R.drawable.visa;
                return getResources().getDrawable(R.drawable.visa, null);
            case 3:
                res_id = R.drawable.ruanjian_2;
                return getResources().getDrawable(R.drawable.ruanjian_2, null);
            case 4:
                res_id = R.drawable.yonghu;
                return getResources().getDrawable(R.drawable.yonghu, null);
        }
        return null;
    }

    class AnimListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (isExpand) {
                help_text.setVisibility(View.VISIBLE);
                //纵向滚动容器滚动到底
            } else {
                help_text.setVisibility(View.GONE);
                fullScroll(ScrollView.FOCUS_UP);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


}
