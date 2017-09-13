package com.gzdb.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ToastUtil;

/**
 * Created by zhumg on 3/27.
 */
public class BuyCountInputDialog extends Dialog implements View.OnClickListener {

    //取消按钮
    View no_btn;
    View ok_btn;
    View del_btn;
    View add_btn;
    TextView tv_num;
    NumInputView numInputView;
    NumInputView.NumInputListener listener;
    int maxValue;

    public BuyCountInputDialog(final Context context, NumInputView.NumInputListener listener,
                               int maxLength) {
        super(context, R.style.NoTitle_Dialog);
        setContentView(R.layout.dialog_buycount_input);

        this.listener = listener;

        tv_num = (TextView) findViewById(R.id.tv_num);
        del_btn = findViewById(R.id.ll_sub);
        add_btn = findViewById(R.id.ll_add);
        del_btn.setOnClickListener(this);
        add_btn.setOnClickListener(this);

        numInputView = new NumInputView(context, new NumInputView.NumInputListener() {
            @Override
            public boolean onClickNum(String text) {
                //如果能填充，则填充

                BuyCountInputDialog.this.tv_num.setText(text);
                return true;
            }

            public void onMax() {
                ToastUtil.showToast(context, "购买数量不能超过 9999 哦!");
            }
        }, false, maxLength);
        ((RelativeLayout) findViewById(R.id.bottom_layout)).addView(numInputView.getView());

        no_btn = numInputView.getView().findViewById(R.id.wni_no);
        ok_btn = numInputView.getView().findViewById(R.id.wni_ok);
        no_btn.setOnClickListener(this);
        ok_btn.setOnClickListener(this);

        Window dialogWindow = getWindow();
        // 设置dialog的宽高属性
        dialogWindow.setLayout(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT);

    }

    public void setTxtValue(String v, int maxValue) {
        this.maxValue = maxValue;
        tv_num.setText(v);
        numInputView.setValue(v);
    }

    @Override
    public void onClick(View arg0) {
        if (arg0.getId() == R.id.wni_no) {
            this.dismiss();
        } else if (arg0.getId() == R.id.wni_ok) {
            //更新出去
            if (this.listener.onClickNum(tv_num.getText().toString())) {
                this.dismiss();
            }
        } else if (arg0.getId() == R.id.ll_sub) {
            int old_v = 0;
            try {
                old_v = Integer.parseInt(tv_num.getText().toString());
                old_v = old_v - 1;
                if (old_v < 0) {
                    old_v = 0;
                }
                //设置
                numInputView.setValue(old_v + "");
                tv_num.setText(old_v + "");
            } catch (Exception e) {

            }
        } else if (arg0.getId() == R.id.ll_add) {
            int old_v = 0;
            try {
                old_v = Integer.parseInt(tv_num.getText().toString());
                old_v = old_v + 1;
                if (old_v > maxValue) {
                    ToastUtil.showToast(getContext(), "库存不足");
                    return;
                }
                //设置
                numInputView.setValue(old_v + "");
                tv_num.setText(old_v + "");
            } catch (Exception e) {

            }
        }
    }


//	public void setTxtValue(String number) {
//		this.tv_num.setText(number);
//		numInputView.setValue(number);
//	}
}
