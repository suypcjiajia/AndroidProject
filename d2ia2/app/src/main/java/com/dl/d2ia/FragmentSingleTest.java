package com.dl.d2ia;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dl.com.dl.bo.TestRecord;
import com.dl.component.ProgerssCircle;
import com.dl.db.TestResultDao;
import com.dl.tool.CRCCheck;
import com.dl.tool.HexCode;
import com.dl.tool.TimeUtil;

import java.util.Date;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSingleTest extends Fragment {

    View root;
    ImageView btnSepcimen;
    ImageView btnStandard;
    ImageView btnQuick;
    ProgerssCircle progerssCircle;
    TextView tipInfo;
    ImageView btnSetting;


    private BluetoothAdapter mBluetoothAdapter;

    private HashMap<String,String> mScanedTargetDevices = new HashMap<>();//目标设备列表


    int count = 0;

    MyThread myThread = new MyThread();

    private BleConnector bleConnector  ;//蓝牙连接器

    public static TestResultDao biaoBenDao ;

    public FragmentSingleTest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_single_test, container, false);

        btnSepcimen = root.findViewById(R.id.btnSepcimen);
        btnSepcimen.setOnClickListener(clickBtnSepcimen);

        btnSetting = root.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(clickBtnSetting);

        btnStandard = root.findViewById(R.id.btnStandard);
        btnStandard.setOnClickListener(clickBtnStandard);

        btnQuick = root.findViewById(R.id.btnQuick);
        btnQuick.setOnClickListener(clickBtnQuick);

        progerssCircle = root.findViewById(R.id.progressCricle);
        tipInfo = root.findViewById(R.id.tipInfo);


        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        bleConnector  = new BleConnector(getContext(),mHandler);//蓝牙连接器

        biaoBenDao = new TestResultDao(getContext());

        myThread.start();
        return root;
    }

    public  void addTargetDeviceInfo(BluetoothDevice device){
        mScanedTargetDevices.put(device.getAddress(),device.getName());
    }



    View.OnClickListener  clickBtnSepcimen = new View.OnClickListener(){
        /**
         * 打开病人对话框
         * @param v
         */
        public void onClick(View v){
            InfoDialog.showDialog(getContext());
        }
    };
    View.OnClickListener  clickBtnSetting = new View.OnClickListener(){
        /**
         * 打开设置对话框
         * @param v
         */
        public void onClick(View v){
            SettingDialog.showDialog(getContext());
        }
    };
    View.OnClickListener  clickBtnStandard = new View.OnClickListener(){
        /**
         * 开始标本测试
         * @param v
         */
        public void onClick(View v){
            if(mScanedTargetDevices.size() == 0){
                Toast.makeText(getContext(), "没有发现目标设备",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if( myThread.getScan()){
                Toast.makeText(getContext(), "正在采集",
                        Toast.LENGTH_SHORT).show();
                return;
            }


            for( String mac :mScanedTargetDevices.keySet()){
                BluetoothDevice device=mBluetoothAdapter.getRemoteDevice(mac);
                bleConnector.connect(device);
                break;
            }
        }
    };
    View.OnClickListener  clickBtnQuick = new View.OnClickListener(){
        /**
         * 开始快速测试
         * @param v
         */
        public void onClick(View v){

            TestRecord tmp = new TestRecord();

            tmp.id =  "12345";
            tmp.result = "阳性";
            tmp.testTime = "2021-04-09 08:30";
            tmp.name = "suyp";
            tmp.age = "6";
            tmp.sex = "男";
            tmp.testMan = "test";
            tmp.curver.add("2020-02-17 16:28:01,1");
            tmp.curver.add("2020-02-17 16:29:01,2");
            tmp.curver.add("2020-02-17 16:30:01,4");
            tmp.curver.add("2020-02-17 16:31:01,8");
            tmp.curver.add("2020-02-17 16:32:01,16");
            tmp.curver.add("2020-02-17 16:33:01,24");
            tmp.curver.add("2020-02-17 16:34:01,16");
            tmp.curver.add("2020-02-17 16:35:01,8");
            tmp.curver.add("2020-02-17 16:36:01,4");
            tmp.curver.add("2020-02-17 16:37:01,2");
            tmp.curver.add("2020-02-17 16:38:01,1");

            tmp.curver.add("2020-02-17 16:39:01,1");
            tmp.curver.add("2020-02-17 16:40:01,2");
            tmp.curver.add("2020-02-17 16:41:01,4");
            tmp.curver.add("2020-02-17 16:42:01,8");
            tmp.curver.add("2020-02-17 16:43:01,16");
            tmp.curver.add("2020-02-17 16:44:01,8");
            tmp.curver.add("2020-02-17 16:45:01,4");
            tmp.curver.add("2020-02-17 16:46:01,2");
            tmp.curver.add("2020-02-17 16:37:01,1");

            biaoBenDao.insert(tmp);
        }
    };

    private  void stopAndClean(){
        myThread.setScan(false);
        count = 0;

        progerssCircle.setSweepAngle(0);
        TestResultDao.testRecord.result = "";
        TestResultDao.testRecord.curver.clear();
    }

    private void cleanAndStart(){
        count = 0;

        progerssCircle.setSweepAngle(0);
        TestResultDao.testRecord.result = "";
        TestResultDao.testRecord.curver.clear();
        myThread.setScan(true);
    }

    class MyThread  extends Thread
    {

        Boolean mScan = false;


        public  void setScan(Boolean b){
            mScan = b;
        }

        public  Boolean getScan(){
            return mScan;
        }
        @Override
        public void run() {

            try {

                while (true) {

                    if (!mScan) {
                        sleep(100);
                        continue;
                    }

                    if( count == 20){//结束采集
                        mScan = false;
                        count = 0;
                        biaoBenDao.insert(TestResultDao.testRecord);
                        continue;
                    }

                    count++;


                    sleep(1000 );

                    String message = "010300000001";//指令:获取目标设备的通道数据(波长)
                    byte[] data = HexCode.hex2byte(message.getBytes());
                    byte[] crc = HexCode.hex2byte(CRCCheck.getCRC3(data).getBytes());

                    byte[] send = new byte[data.length + crc.length];
                    for (int i = 0; i < send.length; i++) {
                        if (i < data.length)
                            send[i] = data[i];
                        else
                            send[i] = crc[i - data.length];
                    }
                    bleConnector.Write(send);
                }

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private final Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandleId.onCharacteristicChanged:
                    byte[]readBuf =(byte[])msg.obj;
                    String readMessage=new String(readBuf,0,readBuf.length);
                    tipInfo.setText(readMessage);

                    progerssCircle.setSweepAngle(count*18);
                    break;
                case HandleId.connectState:

                    readBuf =(byte[])msg.obj;
                    readMessage=new String(readBuf,0,readBuf.length);
                    tipInfo.setText(readMessage);
                    if( readMessage.equals("断开连接")){
                        stopAndClean();
                    }else if( readMessage.equals("成功连接到目标设备")){
                        cleanAndStart();
                    }
                    break;


            }
        }
    };





}
