package com.gzdb.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gzdb.warehouse.R;


/**
 * Created by zhumg on 3/27.
 */
public class NumInputView implements View.OnClickListener {

    private View view;
    private Context context;
    private boolean decimalPoint;// 是否支持小数点
    private NumInputListener listener;
    private StringBuilder sb = new StringBuilder();
    // 最大位数
    private int maxlength;

    private View input_bu_num1, input_bu_num2, input_bu_num3, input_bu_num4,
            input_bu_num5, input_bu_num6, input_bu_num7, input_bu_num8,
            input_bu_num9, input_bu_dot, input_bu_num0, input_bu_add;

    public NumInputView(Context context, NumInputListener listener,
                        boolean decimalPoint, int maxlength) {
        this.context = context;
        this.listener = listener;
        this.decimalPoint = decimalPoint;
        this.maxlength = maxlength;
    }

    public void clear() {
        sb.delete(0, sb.length());
    }

    public void setValue(String text) {
        this.clear();
        this.sb.append(text);
    }

    public View getView() {
        if (view == null) {
            view = View.inflate(context, R.layout.widget_num_input, null);
            input_bu_num0 = view.findViewById(R.id.input_bu_num0);
            input_bu_num1 = view.findViewById(R.id.input_bu_num1);
            input_bu_num2 = view.findViewById(R.id.input_bu_num2);
            input_bu_num3 = view.findViewById(R.id.input_bu_num3);
            input_bu_num4 = view.findViewById(R.id.input_bu_num4);
            input_bu_num5 = view.findViewById(R.id.input_bu_num5);
            input_bu_num6 = view.findViewById(R.id.input_bu_num6);
            input_bu_num7 = view.findViewById(R.id.input_bu_num7);
            input_bu_num8 = view.findViewById(R.id.input_bu_num8);
            input_bu_num9 = view.findViewById(R.id.input_bu_num9);
            input_bu_dot = view.findViewById(R.id.input_bu_dot);
            input_bu_add = view.findViewById(R.id.input_bu_add);
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
            if (this.decimalPoint) {
                input_bu_dot.setOnClickListener(this);
            }
            input_bu_add.setOnClickListener(this);
        }
        return this.view;
    }

    // 监听键盘点击事件
    @Override
    public void onClick(View v) {
        if (listener != null) {
            // 如果是后退，则删除
            if (v.getId() == R.id.input_bu_add) {
                if (sb.length() > 0) {
                    sb.delete(sb.length() - 1, sb.length());
                    if(sb.length() < 1) {
                        sb.append('0');
                    }
                    listener.onClickNum(sb.toString());
                }
            } else {
                TextView tv = (TextView) v;
                char c = tv.getText().charAt(0);
                // 如果需要支持小数点
                if (this.decimalPoint) {
                    sb.append(c);
                    if (!isNumber(sb)) {
                        sb.delete(sb.length() - 1, sb.length());// 添加失败，直接删除最后一位
                    }
                } else {
                    if(sb.length() == 1 && sb.charAt(0) == '0') {
                        sb.delete(0, 1);
                    }
                    // 添加进sb
                    sb.append(c);
                }
                if (sb.length() > maxlength) {
                    sb.delete(sb.length() - 1, sb.length());
                    listener.onMax();
                }
                listener.onClickNum(sb.toString());
            }
        }
    }

    public boolean isNumber(StringBuilder editext) {
        // 第1位是.
        if (editext.charAt(0) == '.') {
            // 清空
            return false;
        }
        // 第一位是0，第二位不是.
        if (editext.length() > 1) {
            if (editext.charAt(0) == '0') {
                char c = editext.charAt(1);
                if (c != '.') {
                    return false;
                }
            }
        }
        int count = 0;
        // 多个.
        for (int i = 0; i < editext.length(); i++) {
            if (editext.charAt(i) == '.') {
                count++;
            }
        }
        if (count > 1) {
            // 清空
            return false;
        }
        return true;
    }

    public static interface NumInputListener {
        public boolean onClickNum(String text);
        public void onMax();
    }
}
