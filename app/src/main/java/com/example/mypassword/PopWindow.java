package com.example.mypassword;

import androidx.appcompat.app.ActionBar;

import android.app.Activity;

import android.content.Context;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class PopWindow extends PopupWindow {
    private PopupWindow popupWindow;
    private View popview;
    private Button button;
    private EditText editText;
    private Activity f;

    public PopWindow(final Activity context) {
        super(context);
        f = context;
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popview = layoutInflater.inflate(R.layout.popup, null, false);
        popupWindow = new PopupWindow(popview,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);

        button = popview.findViewById(R.id.qd);
        editText = popview.findViewById(R.id.EditText);
       // String password = editText.getText().toString();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editText.getText().toString();
                if(password.equals("")){
                    toast("密码不能为空！");
                    return;
                }else{
                    toast("您输入的密码是:"+password);
                    popupWindow.dismiss();
                }

            }
        });
        popupWindow.setFocusable(true);

        popupWindow.setAnimationStyle(-1);//设置动画过渡


    }

    public boolean isShow() {
        return popupWindow.isShowing();
    }

    public void setVisible(boolean b) {
        if (b) {
            View parent = LayoutInflater.from(f)
                    .inflate(R.layout.activity_main, null);
            popupWindow.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
        } else {
            if (isShow()) {
                //销毁
                popupWindow.dismiss();
            }
        }
    }
    //弹出提示消息
    private void toast(String tip){
        Toast.makeText(f, tip, Toast.LENGTH_SHORT).show();
    }
}
