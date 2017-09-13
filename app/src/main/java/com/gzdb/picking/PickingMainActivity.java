package com.gzdb.picking;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;
import com.gzdb.LoginActivity;
import com.gzdb.response.Api;
import com.gzdb.response.DetailsBean;
import com.gzdb.response.NumPickingBean;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.StatusPickingEnum;
import com.gzdb.response.showPick;
import com.gzdb.utils.GlobalData;
import com.gzdb.utils.PushDispatcher;
import com.gzdb.utils.PushObserver;
import com.gzdb.warehouse.App;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.main.OrderFragment;
import com.gzdb.widget.ScanActivity;
import com.gzdb.widget.TitleViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.utils.SpUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.RoundedImageView;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.Bind;

/**
 * Created by liyunbiao on 2017/8/7.
 * 拣货中心
 */

public class PickingMainActivity extends AfinalActivity implements View.OnClickListener {
    BaseTitleBar baseTitleBar;
    TitleViewPager titleViewPager;
    List<Fragment> fragments = new ArrayList<>();
    public static DrawerLayout mDrawerLayout;
    @Bind(R.id.tv_finish)
    TextView tv_finish;
    @Bind(R.id.edit_encode)
    EditText edit_encode;
    @Bind(R.id.ll_location)
    LinearLayout ll_location;
    @Bind(R.id.ll_print)
    LinearLayout ll_print;
    @Bind(R.id.ll_stock)
    LinearLayout ll_stock;
    @Bind(R.id.ll_group)
    LinearLayout ll_group;

    @Bind(R.id.ll_user)
    LinearLayout ll_user;
    @Bind(R.id.ll_stock_mobile)
    LinearLayout ll_stock_mobile;

    @Bind(R.id.ll_encode)
    LinearLayout ll_encode;
    @Bind(R.id.txt_username)
    TextView txt_username;
    @Bind(R.id.img_userpic)
    RoundedImageView img_userpic;
    @Bind(R.id.txt_rank)
    TextView txt_rank;
    @Bind(R.id.tv_quit_login)
    TextView tv_quit_login;

    static TextView tv_print;

    @Bind(R.id.o1_txt_num)
    TextView o1_txt_num;
    @Bind(R.id.o2_txt_num)
    TextView o2_txt_num;
    @Bind(R.id.o3_txt_num)
    TextView o3_txt_num;
    @Bind(R.id.o4_txt_num)
    TextView o4_txt_num;
    Dialog wheelViewDialog;
    Boolean isInput = true;
    public static final int MESSAGE_CONNECT = 1;
    private PortParameters mPortParam[] = new PortParameters[GpPrintService.MAX_PRINTER_CNT];
    public static final String CONNECT_STATUS = "connect.status";
    private static int mPrinterIndex = 0;
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    private static GpService mGpService = null;
    private PrinterServiceConnection conn = null;
    private int mPrinterId = 0;
    private boolean isPrint = false;

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
            commonectPrint();
        }
    }

    private void commonectPrint() {
        int pos = 0;

        Message message = new Message();
        message.what = MESSAGE_CONNECT;
        message.arg1 = pos;
        mHandler.sendMessage(message);
        App.getUIHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isPrint) {
                    commonectPrint();
                }
            }
        }, 1 * 60 * 1000);
    }

    Boolean CheckPortParamters(PortParameters param) {
        boolean rel = false;
        int type = param.getPortType();
        if (type == PortParameters.BLUETOOTH) {
            if (!param.getBluetoothAddr().equals("")) {
                rel = true;
            }
        }
        return rel;
    }

    void connectOrDisConnectToDevice(int PrinterId) {
        mPrinterId = PrinterId;
        int rel = 0;
        if (mPortParam[PrinterId].getPortOpenState() == false) {
            if (CheckPortParamters(mPortParam[PrinterId])) {
                try {
                    mGpService.closePort(mPrinterId);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                switch (mPortParam[PrinterId].getPortType()) {
                    case PortParameters.USB:
                        try {

                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getUsbDeviceName(), 0);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.ETHERNET:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getIpAddr(), mPortParam[PrinterId].getPortNumber());
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                    case PortParameters.BLUETOOTH:
                        try {
                            rel = mGpService.openPort(PrinterId, mPortParam[PrinterId].getPortType(), mPortParam[PrinterId].getBluetoothAddr(), 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
                if (r != GpCom.ERROR_CODE.SUCCESS) {
                    if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
                        mPortParam[PrinterId].setPortOpenState(true);
                    } else {
                        GpCom.getErrorText(r);
                    }
                }
            } else {

            }
        } else {

            try {
                mGpService.closePort(PrinterId);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_CONNECT:
                    connectOrDisConnectToDevice(message.arg1);
            }
            return false;
        }
    });
    PushObserver mDanmuReceiver = new PushObserver() {

        @Override
        public boolean onMessage(String message) {

            if (message == null) {
                return false;
            }
            if (message.equals("&&&&&referen****")) {
                try {
                    checkProduct();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (message.indexOf(":") != -1) {
                String[] str = message.split(":");
                if (str.length == 6) {
                    SpUtils.saveValue("bluetooth", message);
                }
            }
            return true;
        }
    };


    private void registerDanmuPushHandler() {
        PushDispatcher.sharedInstance().addObserver(mDanmuReceiver);
    }

    private void unregisterDanmuPushHandler() {
        PushDispatcher.sharedInstance().removeObserver(mDanmuReceiver);
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    private void initPortParam() {


        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            PortParamDataBase database = new PortParamDataBase(this);
            mPortParam[i] = new PortParameters();
            mPortParam[i] = database.queryPortParamDataBase("" + i);
            mPortParam[i].setPortOpenState(false);
        }
    }

    public void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GpCom.ACTION_CONNECT_STATUS);
        this.registerReceiver(PrinterStatusBroadcastReceiver, filter);
    }

    private BroadcastReceiver PrinterStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (GpCom.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                if (type == GpDevice.STATE_CONNECTING) {
                    setProgressBarIndeterminateVisibility(true);
                    mPortParam[id].setPortOpenState(false);
                } else if (type == GpDevice.STATE_NONE) {
                    setProgressBarIndeterminateVisibility(false);
                    mPortParam[id].setPortOpenState(false);
                } else if (type == GpDevice.STATE_VALID_PRINTER) {
                    setProgressBarIndeterminateVisibility(false);
                    mPortParam[id].setPortOpenState(true);
                    isPrint = true;
                    tv_print.setText("打印机已连接");
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
                    setProgressBarIndeterminateVisibility(false);
                }
            }
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_picking;
    }

    @Override
    public void initView(View view) {
        initPortParam();
        connection();
        registerBroadcast();
        tv_print = (TextView) findViewById(R.id.tv_print);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.base_drawer);
        registerDanmuPushHandler();
        mDrawerLayout.setOnClickListener(this);
        ll_location.setOnClickListener(this);
        ll_print.setOnClickListener(this);
        tv_quit_login.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        ll_encode.setOnClickListener(this);
        ll_stock.setOnClickListener(this);
        ll_group.setOnClickListener(this);
        ll_user.setOnClickListener(this);
        ll_stock_mobile.setOnClickListener(this);
        o4_txt_num.setVisibility(View.GONE);
        if (Cache.passport.getShowName() != null && Cache.passport.getPhoneNumber() != null) {
            txt_username.setText(Cache.passport.getShowName());
            txt_rank.setText(Cache.passport.getPhoneNumber());
            ImageLoader.getInstance().displayImage(Cache.passport.getHeadImage(), img_userpic);
        } else {
            return;
        }
        edit_encode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    edit_encode.requestFocus();
                    edit_encode.setFocusableInTouchMode(true);
                }
            }
        });
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setCenterTxt("1号拣货员");
        baseTitleBar.setLeftImg(R.mipmap.home_nav_icon_menu);
        baseTitleBar.setRightImg(R.drawable.search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent();
                myintent.setClass(PickingMainActivity.this, SearchPickingActivity.class);
                startActivity(myintent);

            }
        });
        baseTitleBar.setLeftListener(this);
        fragments.add(PickingListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusPickingEnum.NOPICKING, tv_finish, edit_encode));
        fragments.add(PickingListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusPickingEnum.MERGEORDER, tv_finish, edit_encode));
        fragments.add(PickingListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusPickingEnum.NODATA, tv_finish, edit_encode));
        fragments.add(PickingListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusPickingEnum.FINISH, tv_finish, edit_encode));
        titleViewPager = new TitleViewPager(view, getSupportFragmentManager(), fragments, DeviceUtils.screenWidth(PickingMainActivity.this), 0);
        checkProduct();

    }

    public void checkProduct() {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportId());
        httpPost(map, Api.BasesupplychainRemoteURL() + "warehouse/showPickListCount", new HttpCallback<NumPickingBean>() {
            @Override
            public void onSuccess(NumPickingBean data) {
                try {
                    o1_txt_num.setText(data.getUnTouch());
                    o2_txt_num.setText(data.getMerge());
                    o3_txt_num.setText(data.getLack());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void sendLabel(String encode) {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(75, 50); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(20); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(75, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        if (encode == null || encode.length() == 0) {
            return;
        }
        int num = encode.length();
        String strprint = "";
        if (num < 17) {
            strprint = setEmpty(17 - num);
        }
        tsc.add1DBarcode(0, 30, LabelCommand.BARCODETYPE.CODE128,
                300, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, encode + strprint);
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private String setEmpty(int len) {
        String str = "";
        for (int i = 0; i < len; i++) {
            str = str + " ";
        }
        return str;
    }

    public static void printLabel(String num, String orderNum, String encode, String title, String warehouse, String shopName, String address, String imgEncode) {

        LabelCommand tsc = new LabelCommand();
        tsc.addSize(75, 50); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(20); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(25, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        // 绘制简体中文
        tsc.addText(0, 30, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
                num);
        tsc.addText(0, 100, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                orderNum);
        tsc.addText(0, 140, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                encode);
        tsc.addText(0, 180, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                title);
        tsc.addText(0, 220, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_2,
                warehouse);
        tsc.addText(0, 280, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "商家名称:" + shopName);
        tsc.addText(0, 320, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                address);

        tsc.addQRCode(370, 30, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, imgEncode);//二维码
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                tv_print.setText(GpCom.getErrorText(r));
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            mGpService.queryPrinterStatus(mPrinterIndex, 1000, MAIN_QUERY_PRINTER_STATUS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    //TODO 打印-------------------------------------------------
    public void printTestPageClicked() {
        try {
            int rel = mGpService.printeTestPage(mPrinterIndex);
            Log.i("ServiceConnection", "rel " + rel);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public Dialog createWheelViewDialog() {
        final Dialog dialog = new Dialog(PickingMainActivity.this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(PickingMainActivity.this, R.layout.picking_num,
                null);
        TextView tv_dialog_close = (TextView) view.findViewById(R.id.tv_dialog_close);
        TextView tv_dialog_ok = (TextView) view.findViewById(R.id.tv_dialog_ok);
        final EditText edit_num = (EditText) view.findViewById(R.id.edit_num);
        tv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation ani = AnimationUtils.loadAnimation(PickingMainActivity.this, R.anim.alpha_action);
                v.startAnimation(ani);
                wheelViewDialog.dismiss();
            }
        });
        tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation ani = AnimationUtils.loadAnimation(PickingMainActivity.this, R.anim.alpha_action);
                v.startAnimation(ani);
                if (edit_num.getText().toString().length() > 0) {
                    if (isPrint) {

                        try {
                            sendLabel(edit_num.getText().toString());
                            mGpService.queryPrinterStatus(mPrinterIndex, 1000, MAIN_QUERY_PRINTER_STATUS);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                wheelViewDialog.dismiss();


            }
        });
        dialog.setContentView(view);
        return dialog;
    }


    @Override
    public void onClick(View v) {
        Animation ani = AnimationUtils.loadAnimation(PickingMainActivity.this, R.anim.alpha_action);
        v.startAnimation(ani);
        try {
            switch (v.getId()) {
                case R.id.title_left:
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    break;
                case R.id.ll_location:
                    ActivityManager.startActivity(PickingMainActivity.this, AddWarehouseLocationActivity.class);

                    break;
                case R.id.ll_stock:
                    ActivityManager.startActivity(PickingMainActivity.this, StockManagerActivity.class);

                    break;
                case R.id.ll_group:
                    ActivityManager.startActivity(PickingMainActivity.this, PickGroupActivity.class);

                    break;
                case R.id.ll_user:


                    break;
                case R.id.ll_stock_mobile:
                    ActivityManager.startActivity(PickingMainActivity.this, StockMobileActivity.class);

                    break;

                case R.id.ll_print:

                    tv_print.setText("正在连接打印机");
                    commonectPrint();

                    break;
                case R.id.ll_encode:
                    wheelViewDialog = createWheelViewDialog();
                    wheelViewDialog.show();

                    break;
                case R.id.tv_quit_login:
                    Cache.passport = null;
                    ActivityManager.startActivity(PickingMainActivity.this, LoginActivity.class);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            //在这里发送一个广播出去
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterDanmuPushHandler();
        if (conn != null) {
            unbindService(conn); // unBindService
        }

    }


}
