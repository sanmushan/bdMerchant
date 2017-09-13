package com.gzdb.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gzdb.warehouse.R;


/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class InputPayPasswordDialog extends Dialog implements View.OnClickListener {

    StringBuilder sb = new StringBuilder();
    int inputCount = 0;
    TextView tvs[];
    View findPass = null;
    View input_bu_num1, input_bu_num2, input_bu_num3, input_bu_num4,
            input_bu_num5, input_bu_num6, input_bu_num7, input_bu_num8,
            input_bu_num9, input_bu_dot, input_bu_num0, input_bu_add;
    TextView tvMoney;
    View closeBtn = null;

    InputPasswordCallBack callback = null;

    public InputPayPasswordDialog(Context context, InputPasswordCallBack callback) {

        super(context, R.style.NoTitle_Dialog);
        setContentView(R.layout.dialog_inputpassword);
        this.callback = callback;
        tvs = new TextView[6];
        tvs[0] = (TextView)findViewById(R.id.i_1);
        tvs[1] = (TextView)findViewById(R.id.i_2);
        tvs[2] = (TextView)findViewById(R.id.i_3);
        tvs[3] = (TextView)findViewById(R.id.i_4);
        tvs[4] = (TextView)findViewById(R.id.i_5);
        tvs[5] = (TextView)findViewById(R.id.i_6);

        findPass = findViewById(R.id.i_find);
        findPass.setOnClickListener(this);

        tvMoney = (TextView)findViewById(R.id.i_money);

        input_bu_num0 = findViewById(R.id.input_bu_num0);
        input_bu_num1 = findViewById(R.id.input_bu_num1);
        input_bu_num2 = findViewById(R.id.input_bu_num2);
        input_bu_num3 = findViewById(R.id.input_bu_num3);
        input_bu_num4 = findViewById(R.id.input_bu_num4);
        input_bu_num5 = findViewById(R.id.input_bu_num5);
        input_bu_num6 = findViewById(R.id.input_bu_num6);
        input_bu_num7 = findViewById(R.id.input_bu_num7);
        input_bu_num8 = findViewById(R.id.input_bu_num8);
        input_bu_num9 = findViewById(R.id.input_bu_num9);
        input_bu_dot = findViewById(R.id.input_bu_dot);
        input_bu_add = findViewById(R.id.input_bu_add);
        input_bu_dot.setVisibility(View.INVISIBLE);
        closeBtn = findViewById(R.id.closeBtn);

        input_bu_num0.setOnClickListener(this);
        input_bu_num1.setOnClickListener(this);
        input_bu_num2.setOnClickListener(this);
        input_bu_num3.setOnClickListener(this);
        input_bu_num4.setOnClickListener(this);
        input_bu_num5.setOnClickListener(this);
        input_bu_num6.setOnClickListener(this);
        input_bu_num7.setOnClickListener(this);
        input_bu_num8.setOnClickListener(this);
        input_bu_num9.setOnClickListener(this);
        input_bu_add.setOnClickListener(this);

        closeBtn.setOnClickListener(this);

        setOnKeyListener(keylistener);

        Window dialogWindow = getWindow();
        // 设置dialog的宽高属性
        dialogWindow.setLayout(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT);
    }

    public void show() {
        if(this.tvs != null) {
            for (int i = 0; i < tvs.length; i++) {
                tvs[i].setText("");
            }
        }
        sb.delete(0, sb.length());
        inputCount = 0;
        super.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        if(id == R.id.i_find) {
//            //show findPass
//            Intent intent = new Intent(this.getContext(), FoundPayPassWordActivity.class);
//            this.getContext().startActivity(intent);
//            return;
//        }
        if(id == R.id.closeBtn) {
            this.dismiss();
            if(callback != null) {
                callback.callback(null);
            }
            return;
        }
        String key = null;
        switch(id) {
            case R.id.input_bu_num0:
                key = "0";
                break;
            case R.id.input_bu_num1:
                key = "1";
                break;
            case R.id.input_bu_num2:
                key = "2";
                break;
            case R.id.input_bu_num3:
                key = "3";
                break;
            case R.id.input_bu_num4:
                key = "4";
                break;
            case R.id.input_bu_num5:
                key = "5";
                break;
            case R.id.input_bu_num6:
                key = "6";
                break;
            case R.id.input_bu_num7:
                key = "7";
                break;
            case R.id.input_bu_num8:
                key = "8";
                break;
            case R.id.input_bu_num9:
                key = "9";
                break;
            case R.id.input_bu_add:
                if(inputCount == 0) return;
                inputCount -= 1;
                tvs[inputCount].setText("");
                sb.delete(sb.length()-1, sb.length());
                return;
            default:
                return;
        }
        if(inputCount == 6 || key == null) return;
        sb.append(key);
        //设置
        this.tvs[this.inputCount].setText("*");
        inputCount += 1;
        if(inputCount == 6) {
            this.dismiss();
            if(callback != null) {
                callback.callback(sb.toString());
            }
        }
    }

    public static interface InputPasswordCallBack {
        public void callback(String password);
    }

    public void setShowMoney(String money) {
        this.tvMoney.setText(money);
        this.show();
    }

    OnKeyListener keylistener = new OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    } ;
}
